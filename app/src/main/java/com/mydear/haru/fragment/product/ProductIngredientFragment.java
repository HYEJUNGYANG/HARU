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

public class ProductIngredientFragment extends Fragment {

    private ConstraintLayout layout_loading;
    private ConstraintLayout layout_content;

    private TextView tv_loading;

    private Activity activity;

    private String product_name;

    public ProductIngredientFragment(String product_name) {
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
        View view = inflater.inflate(R.layout.fragment_product_ingredient, container, false);

        setInitialize(view);

        return view;
    }

    private void setInitialize(View view) {
        layout_loading = view.findViewById(R.id.layout_loading);
        layout_content = view.findViewById(R.id.layout_content);

        tv_loading = view.findViewById(R.id.tv_loading);
    }
}
