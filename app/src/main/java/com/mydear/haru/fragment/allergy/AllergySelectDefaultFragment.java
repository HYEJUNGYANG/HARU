package com.mydear.haru.fragment.allergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.mydear.haru.R;

public class AllergySelectDefaultFragment extends Fragment {

    private ConstraintLayout fragment_allergy_default;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allergy_default, container, false);

        setInitialize(view);
        return view;
    }

    private void setInitialize(View view) {
        fragment_allergy_default = view.findViewById(R.id.fragment_allergy_default);
    }
}
