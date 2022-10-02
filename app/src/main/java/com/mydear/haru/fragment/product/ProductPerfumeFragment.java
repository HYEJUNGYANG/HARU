package com.mydear.haru.fragment.product;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.mydear.haru.R;

public class ProductPerfumeFragment extends Fragment {
    private String product_name;

    public ProductPerfumeFragment(String product_name) {
        this.product_name = product_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_perfume, container, false);

        setInitialize(view);

        return view;
    }

    private void setInitialize(View view) {
        //
    }
}
