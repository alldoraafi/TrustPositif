package com.example.trustpositif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;


import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.google.api.services.gmail.model.Message;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static android.app.Activity.RESULT_OK;

public class FragmentSubmit extends Fragment implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 2000;
    static final int REQUEST_AUTHORIZATION = 2001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 2002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 2003;

    //    private ArrayList<String> pathList;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };

    View view;
    Button secondButton;
    TextView mOutputText;
    ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_submit, container, false);
        mOutputText = (TextView) view.findViewById(R.id.textView2);
        mProgress = new ProgressDialog(FragmentSubmit.this.getActivity());
        secondButton = (Button) view.findViewById(R.id.button2);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = FragmentURL.getURL();
                mOutputText.setText(url);

                getResultsFromApi();
            }
        });
        mProgress.setMessage(getString(R.string.send_progress));
        mCredential = GoogleAccountCredential.usingOAuth2(
                FragmentSubmit.this.getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        return view;
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void showMessage(String message) {
        Toast toast = Toast.makeText(FragmentSubmit.this.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            showMessage("No network available!");
//        } else if (isEmpty(editTextSubject)) {
//            showMessage("Subject is required!"); Jika Kategori Kosong
//        }
        } else {
            new MakeRequestTask(FragmentSubmit.this.getActivity(), mCredential).execute();
        }
    }

//    private static boolean isEmpty(EditText editText) {
//        return editText.getText().toString().trim().length() <= 0;
//    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(FragmentSubmit.this.getActivity(),
                Manifest.permission.GET_ACCOUNTS)) {
            String accountName = FragmentSubmit.this.getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    //Modify for image attachment.
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showMessage(getString(R.string.noRequestGPlayService));
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                FragmentSubmit.this.getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) FragmentSubmit.this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(FragmentSubmit.this.getActivity());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(FragmentSubmit.this.getActivity());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                FragmentSubmit.this.getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {

        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;
        private FragmentActivity activity;

        MakeRequestTask(FragmentActivity activity, GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
            this.activity = activity;
        }

        /**
         * Background task to call Gmail API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         *
         * @return List of Strings labels.
         * @throws IOException based on user input
         */
        private String getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "me";

            String recipient = getString(R.string.recipient_email);
            String sender = mCredential.getSelectedAccountName();
            String subject = FragmentKategori.getKategori();
            String body = FragmentURL.getURL() + "\n" + "Keterangan";
            MimeMessage mimeMessage;
            String response = "";
            try {
                mimeMessage = createEmail(
                        recipient,
                        sender,
                        subject,
                        body);
                response = sendMail(mService, user, mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return response;
        }

        //Send Email Method
        private String sendMail(Gmail service,
                                String userId,
                                MimeMessage email)
                throws MessagingException, IOException {
            Message message = createMessageWithEmail(email);

            //GMail's official method to send email with oauth2.0
            message = service.users().messages().send(userId, message).execute();

            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
            return message.getId();
        }

        // Create Email Parameters Method
        private MimeMessage createEmail(String recipient,
                                        String sender,
                                        String subject,
                                        String bodyText)
                throws MessagingException {
            Properties properties = new Properties();
            Session session = Session.getDefaultInstance(properties, null);

            MimeMessage email = new MimeMessage(session);
            InternetAddress senderAddress = new InternetAddress(sender);
            InternetAddress recipientAddress = new InternetAddress(recipient);

            email.setFrom(senderAddress);
            email.addRecipient(javax.mail.Message.RecipientType.TO, recipientAddress);
            email.setSubject(subject);

            //create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();

            BodyPart messageBody = new MimeBodyPart();
            messageBody.setText(bodyText);
            multipart.addBodyPart(messageBody);

            //Access PathList from Fragment Screenshot
            if (!(FragmentScreenshot.getImage().isEmpty())) {
                int size = FragmentScreenshot.getImage().size();
                MimeBodyPart attachmentBody[];
                attachmentBody = new MimeBodyPart[size];
                String filename[];
                filename = new String[size];
                DataSource source[];
                source = new DataSource[size];

                for (int i = 0; i < FragmentScreenshot.getImage().size(); i++) {
                    attachmentBody[i] = new MimeBodyPart();
                    filename[i] = FragmentScreenshot.getImage().get(i);
                    source[i] = new FileDataSource(filename[i]);
                    attachmentBody[i].setDataHandler(new DataHandler(source[i]));
                    attachmentBody[i].setFileName(filename[i]);
                    multipart.addBodyPart(attachmentBody[i]);
                }
            }

            email.setContent(multipart);
            return email;
        }

        private Message createMessageWithEmail(MimeMessage email)
                throws MessagingException, IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            email.writeTo(bytes);
            String encodeEmail = Base64.
                    encodeBase64URLSafeString(bytes.toByteArray());
            Message message = new Message();
            message.setRaw(encodeEmail);
            return message;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null || output.length() == 0) {
                showMessage(getString(R.string.noResult));
            } else {
                showMessage(getString(R.string.email_success));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            FragmentSubmit.REQUEST_AUTHORIZATION);
                } else {
                    String placeHolder = getString(R.string.errorRequestCancelled) + "\n" + mLastError.getMessage();
                    showMessage(placeHolder);
                    Log.v("ERROR", mLastError + "");
                }
            } else {
                showMessage(getString(R.string.request_cancelled));
            }
        }
    }
}
