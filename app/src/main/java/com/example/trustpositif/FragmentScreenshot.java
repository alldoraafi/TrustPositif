package com.example.trustpositif;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nileshp.multiphotopicker.*;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


import static android.app.Activity.RESULT_OK;

public class FragmentScreenshot extends Fragment {
    View view;
    private ImageView imageView[];
    ImageView screenshot;
    TextView text_screenshot;
    private static ArrayList<String> pathList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screenshot, container, false);

        //ImageView Screenshot
        imageView = new ImageView[3];
        imageView[0] = (ImageView) view.findViewById(R.id.SCView1);
        imageView[1] = (ImageView) view.findViewById(R.id.SCView2);
        imageView[2] = (ImageView) view.findViewById(R.id.SCView3);

        //Ikon screenshot
        screenshot = (ImageView) view.findViewById(R.id.hint_image_screenshot);
        screenshot.setImageResource(R.drawable.screenshot);

        //Text petunjuk
        text_screenshot = (TextView) view.findViewById(R.id.text_hint_screenshot);

        //Tombol pilih gambar
        Button pickImageButton = (Button) view.findViewById(R.id.pick_image_button);
        pickImageButton.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return view;
    }

    //Membuka galeri dalam ponsel
    private void openGallery() {
        Intent mIntent = new Intent(FragmentScreenshot.this.getActivity(), PickImageActivity.class);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 3);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    //??
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            pathList = intent.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            ArrayList<Uri> imageUri = new ArrayList<>();
            StringBuilder peringatan = new StringBuilder("");
            boolean warn = false;
            for (ImageView x : imageView) {
                x.setImageResource(0);
            }
            if (pathList != null && !pathList.isEmpty()) {
                for (int i = 0; i < pathList.size(); i++) {
                    if (pathList.get(i) != null) {
                        if (checkFileSize(pathList.get(i)) <
                                Long.parseLong(getString(R.string.screenshot_max_size))) {
                            imageUri.add(Uri.parse(pathList.get(i)));
                        } else {
                            peringatan.append("Gambar " +
                                    String.valueOf(i + 1) +
                                    " lebih dari " + getString(R.string.screenshot_max_size) +
                                    " KB\n");
                            warn = true;
                        }
                    }
                }
                if (imageUri.size() > 0) {
                    screenshot.setVisibility(View.INVISIBLE);
                    text_screenshot.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < imageUri.size(); i++) {
                        imageView[i].setImageURI(imageUri.get(i));
                    }
                } else {
                    screenshot.setVisibility(View.VISIBLE);
                    text_screenshot.setVisibility(View.VISIBLE);
                }
                if (warn) {
                    Toast toast = Toast.makeText(getActivity(), peringatan, Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                screenshot.setVisibility(View.VISIBLE);
                text_screenshot.setVisibility(View.VISIBLE);
            }
        }
    }

    public long checkFileSize(String imageUri) {
        File file = new File(imageUri);
        long length = file.length() / 1024;
        return length;
    }

    //Mendapatkan gambar yang telah dipilih
    public static ArrayList<String> getImage() {
        if (!(pathList == null)) {
            return pathList;
        } else {
            ArrayList<String> returnPathlist;
            returnPathlist = new ArrayList<>();
            return returnPathlist;
        }

    }
}
