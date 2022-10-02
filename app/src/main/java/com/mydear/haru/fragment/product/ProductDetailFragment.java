package com.mydear.haru.fragment.product;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mydear.haru.R;

public class ProductDetailFragment extends Fragment {

    private ConstraintLayout layout_loading;
    private ConstraintLayout layout_content;

    private Activity activity;

    private TextView tv_loading;
    private ImageView iv_product_detail;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String product_name;

    public ProductDetailFragment(String product_name) {
        this.product_name = product_name;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        setInitialize(view);
        getDetailImage();

        return view;
    }

    private void setInitialize(View view) {
        layout_loading = view.findViewById(R.id.layout_loading);
        layout_content = view.findViewById(R.id.layout_content);

        tv_loading = view.findViewById(R.id.tv_loading);
        Log.d(TAG, "텍스트 길이: " + tv_loading.getText().toString().length());

        iv_product_detail = view.findViewById(R.id.iv_product_detail);

        storage = FirebaseStorage.getInstance("gs://mydear-bb10e.appspot.com/");
        storageRef = storage.getReference();
    }

    private void getDetailImage() {
        StorageReference productPathRef = storageRef.child("product/" + product_name + "/product.png");
        Log.d(TAG, "productPathRef: " + productPathRef.toString());

        productPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(activity).load(uri).into(iv_product_detail);
                layout_loading.setVisibility(View.GONE);
                layout_content.setVisibility(View.VISIBLE);

                Toast.makeText(activity, "이미지 성공적", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                layout_loading.setVisibility(View.GONE);
                layout_content.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "존재하지 않는 이미지 입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
