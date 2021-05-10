package com.agnet.uza.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.pages.inventories.products.NewInvProductFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProductPhotoSelectorDialog extends DialogFragment implements View.OnClickListener {
    private static final int picId = 123;
    private NewInvProductFragment newProductFragment;
    private LinearLayout launchCamera;

    public void setParentFragment(NewInvProductFragment newProductFragment) {
        this.newProductFragment = newProductFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_product_photo_selector, container, false);
        getDialog().setTitle("Sample");
        launchCamera = view.findViewById(R.id.launch_camera);
        launchCamera.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Match the request 'pic id with requestCode
        if (requestCode == picId) {

            Toast.makeText(getContext(), ""+requestCode, Toast.LENGTH_SHORT).show();
            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            ((NewInvProductFragment) newProductFragment).displaySelectedImg(photo);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.launch_camera:
                launchCamera();
                break;

        }
    }

    private void launchCamera() {
        Intent camera_intent
                = new Intent(MediaStore
                .ACTION_IMAGE_CAPTURE);

        // Start the activity with camera_intent,
        // and request pic id
        startActivityForResult(camera_intent, picId);
    }


}