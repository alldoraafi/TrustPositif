package com.example.trustpositif;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by AR-Laptop on 8/9/2017.
 */

public class ScreenSlidePagerActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {
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

    private static final int NUM_PAGES = 4;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private RadioGroup progressGroup;
    private Button nextButton;
    private ImageView trustImg;

    TextView mOutputText;
    ProgressDialog mProgress;

    ImageView faqbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faqbutton = (ImageView) findViewById(R.id.faqButton);
        faqbutton.setImageResource(R.drawable.faq);
        faqbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScreenSlidePagerActivity.this, faqActivity.class);
                startActivity(i);
                //finish();
            }
        });

        mOutputText = (TextView) findViewById(R.id.titlekategori);
        mProgress = new ProgressDialog(ScreenSlidePagerActivity.this);
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 3) {
                    String url = FragmentURL.getURL();
                    //mOutputText.setText(url);

                    getResultsFromApi();
                } else {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }
            }
        });

        mProgress.setMessage(getString(R.string.send_progress));
        mCredential = GoogleAccountCredential.usingOAuth2(
                ScreenSlidePagerActivity.this, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        trustImg = (ImageView) findViewById(R.id.trustImage);
        trustImg.setImageResource(R.drawable.aduan_konten);
        mPager = (ViewPager) findViewById(R.id.viewPager1);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((RadioButton) progressGroup.getChildAt(0)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) progressGroup.getChildAt(1)).setChecked(true);
                        break;
                    case 2:
                        ((RadioButton) progressGroup.getChildAt(2)).setChecked(true);
                        break;
                    case 3:
                        ((RadioButton) progressGroup.getChildAt(3)).setChecked(true);
                        break;
                    case 4:
                        ((RadioButton) progressGroup.getChildAt(4)).setChecked(true);
                        break;
                }
                if (position == 3) {
                    nextButton.setText("Kirim");
                    nextButton.getBackground().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_ATOP);
                } else {
                    nextButton.setText("Lanjutkan");
                    nextButton.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Hide the keyboard.
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mPager.getWindowToken(), 0);
            }
        });

        progressGroup = (RadioGroup) findViewById(R.id.progressGroup);
        progressGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        mPager.setCurrentItem(0);
                        break;
                    case R.id.radioButton2:
                        mPager.setCurrentItem(1);
                        break;
                    case R.id.radioButton3:
                        mPager.setCurrentItem(2);
                        break;
                    case R.id.radioButton4:
                        mPager.setCurrentItem(3);
                        break;
                    //case R.id.radioButton5:mPager.setCurrentItem(4); break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment url = new FragmentURL();
        Fragment screenshot = new FragmentScreenshot();
        Fragment kategori = new FragmentKategori();
        Fragment keterangan = new FragmentKeterangan();
        Fragment submit = new FragmentSubmit();

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return url;
                case 1:
                    return screenshot;
                case 2:
                    return kategori;
                case 3:
                    return keterangan;
                case 4:
                    return submit;
                default:
                    return new FragmentURL();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(ScreenSlidePagerActivity.this);

        dialog.setTitle("Pelaporan Berhasil");
        dialog.setContentView(R.layout.dialog_konfirmasi);
        Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        okButton.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something here
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
        dialog.show();
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(ScreenSlidePagerActivity.this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            showMessage("Pastikan anda terhubung dengan internet.");
        } else if (isEmpty(FragmentURL.getURL())) {
            showMessage("Anda belum mengisi URL.");
        } else if (FragmentScreenshot.getImage().isEmpty()) {
            showMessage("Tambahkan setidaknya 1 Screenshot.");
        } else if (isEmpty(FragmentKategori.getKategori())) {
            showMessage("Anda belum memilih Kategori.");
        } else {
            new ScreenSlidePagerActivity.MakeRequestTask(mCredential).execute();
        }
    }

    private static boolean isEmpty(String text) {
        return text.length() <= 0;
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(ScreenSlidePagerActivity.this,
                Manifest.permission.GET_ACCOUNTS)) {
            String accountName = ScreenSlidePagerActivity.this.getPreferences(Context.MODE_PRIVATE)
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
                                ScreenSlidePagerActivity.this.getPreferences(Context.MODE_PRIVATE);
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
                (ConnectivityManager) ScreenSlidePagerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                apiAvailability.isGooglePlayServicesAvailable(ScreenSlidePagerActivity.this);
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
                apiAvailability.isGooglePlayServicesAvailable(ScreenSlidePagerActivity.this);
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
                ScreenSlidePagerActivity.this,
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

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
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
            String keterangan = FragmentKeterangan.getKeterangan();
            String body = FragmentURL.getURL() + "\n\n" + keterangan;
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
                showDialog();
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
