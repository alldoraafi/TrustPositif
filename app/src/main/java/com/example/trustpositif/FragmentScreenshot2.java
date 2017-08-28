package com.example.trustpositif;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by AR-Laptop on 8/8/2017.
 */

//public class FragmentScreenshot2 extends Fragment {
//    private static final int PICK_IMAGE = 100;
//    View view;
//    private ImageView imageView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        view = inflater.inflate(R.layout.fragment_screenshot,container, false);
//        imageView = (ImageView) view.findViewById(R.id.SCView);
//        Button pickImageButton = (Button) view.findViewById(R.id.pick_image_button);
//        pickImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGallery();
//            }
//        });
//        return view;
//    }
//
//    private void openGallery() {
//        Intent gallery =
//                new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
//            Uri imageUri = data.getData();
//            imageView.setImageURI(imageUri);
//        }
//    }
//}
