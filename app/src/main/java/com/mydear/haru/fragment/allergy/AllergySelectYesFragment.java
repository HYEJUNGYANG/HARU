package com.mydear.haru.fragment.allergy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.mydear.haru.R;

public class AllergySelectYesFragment extends Fragment {

    private ConstraintLayout fragment_allergy_yes;

    private CheckBox cb_1_1;
    private CheckBox cb_1_2;
    private CheckBox cb_2_1;
    private CheckBox cb_2_2;
    private CheckBox cb_3_1;
    private CheckBox cb_3_2;
    private CheckBox cb_3_3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allergy_yes, container, false);

        setInitialize(view);

        return view;
    }

    private void setInitialize(View view) {
        fragment_allergy_yes = view.findViewById(R.id.fragment_allergy_yes);
        cb_1_1 = view.findViewById(R.id.cb_1_1);
        cb_1_2 = view.findViewById(R.id.cb_1_2);
        cb_2_1 = view.findViewById(R.id.cb_2_1);
        cb_2_2 = view.findViewById(R.id.cb_2_2);
        cb_3_1 = view.findViewById(R.id.cb_3_1);
        cb_3_2 = view.findViewById(R.id.cb_3_2);
        cb_3_3 = view.findViewById(R.id.cb_3_3);
    }

    public boolean isChecked() {
        if(!cb_1_1.isChecked() && !cb_1_2.isChecked()
                && !cb_2_1.isChecked() && !cb_2_2.isChecked()
                && !cb_3_1.isChecked() && !cb_3_2.isChecked() && !cb_3_3.isChecked()) {
            return false;
        } else {
            return true;
        }
    }
}
