package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.adapters.ImgIconListAdapter;
import com.agnet.uza.models.ImgIcon;
import com.camerakit.CameraKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SelectImageFragment extends Fragment {

    private FragmentActivity _c;

    private Gson _gson;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private FloatingActionButton _fab;
    private RecyclerView _imgIconList;
    private LinearLayoutManager _iconListManager;
    private Button _captureImg;
    private static final int CAPTURE_MEDIA = 368;
    private CameraKitView _cameraKitView;
    private ImageView _imgPreview;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_image, container, false);
        _c = getActivity();


        //initialization
        _gson = new Gson();
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _fab = _c.findViewById(R.id.fab);
        _captureImg = view.findViewById(R.id.capture_img);

        _cameraKitView = view.findViewById(R.id.camera);
        _imgPreview = view.findViewById(R.id.img_preview);

        _captureImg.setOnClickListener(photoOnClickListener);


        //set items

        _imgIconList = view.findViewById(R.id.img_icon_list);
        _iconListManager = new GridLayoutManager(_c, 3);

        _imgIconList.setHasFixedSize(true);
        ImgIconListAdapter adapter = new ImgIconListAdapter(_c, getImgIcons());
        _imgIconList.setAdapter(adapter);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                    Toast.makeText(_c, "acha miyeyusho", Toast.LENGTH_SHORT).show();
                    File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");


                    Bitmap myBitmap = BitmapFactory.decodeFile(savedPhoto.getAbsolutePath());

                    _imgPreview.setImageBitmap(myBitmap);

                    try {
                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                        outputStream.write(photo);
                        outputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        Log.e("CKDemo", "Exception in photo callback");
                    }
                }
            });
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        _cameraKitView.onStart();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        _cameraKitView.onResume();
        _fab.setVisibility(View.GONE);

    }

    @Override
    public void onPause() {
        _cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        _cameraKitView.onStop();
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        _cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public List<ImgIcon> getImgIcons() {

        List<ImgIcon> imgIcons = new ArrayList<>();
        imgIcons.add(new ImgIcon("Bia", ""));
        imgIcons.add(new ImgIcon("Maji", ""));
        imgIcons.add(new ImgIcon("Mafuta", ""));

        imgIcons.add(new ImgIcon("Wine", ""));
        imgIcons.add(new ImgIcon("Shati", ""));
        imgIcons.add(new ImgIcon("Tshirt", ""));

        imgIcons.add(new ImgIcon("Sigara", ""));
        imgIcons.add(new ImgIcon("Food", ""));
        imgIcons.add(new ImgIcon("Soda", ""));

        return imgIcons;
    }


}
