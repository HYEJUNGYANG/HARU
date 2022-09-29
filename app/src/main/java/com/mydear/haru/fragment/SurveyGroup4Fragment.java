package com.mydear.haru.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mydear.haru.R;
import com.mydear.haru.survey.Result;

public class SurveyGroup4Fragment extends Fragment {
    private LinearLayout layoutSurvey4;
    private ConstraintLayout layout_loading;
    private TextView q10;
    private TextView q11;
    private TextView q12;
    private RadioGroup radioGroup10;
    private RadioGroup radioGroup11;
    private RadioGroup radioGroup12;

    private int rg10Result = 0, rg11Result = 0, rg12Result = 0;
    private int total;

    private boolean rg10Checked = false, rg11Checked = false, rg12Checked = false, totalChecked = false;
    private boolean isQ10 = false, isQ11 = false, isQ12 = false;

    public Result result;

    public DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_group4, container, false);
        result = new Result();

        setInitialize(view);
        loadSurvey();

        radioGroup10 = view.findViewById(R.id.radio_category10);
        radioGroup10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c10_1:
                        rg10Result = 5;
                        break;
                    case R.id.c10_2:
                        rg10Result = 4;
                        break;
                    case R.id.c10_3:
                        rg10Result = 3;
                        break;
                    case R.id.c10_4:
                        rg10Result = 2;
                        break;
                    case R.id.c10_5:
                        rg10Result = 1;
                        break;
                }

                rg10Checked = true;
                uploadResult();
            }
        });

        radioGroup11 = view.findViewById(R.id.radio_category11);
        radioGroup11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c11_1:
                        rg11Result = 5;
                        break;
                    case R.id.c11_2:
                        rg11Result = 4;
                        break;
                    case R.id.c11_3:
                        rg11Result = 3;
                        break;
                    case R.id.c11_4:
                        rg11Result = 2;
                        break;
                    case R.id.c11_5:
                        rg11Result = 1;
                        break;
                }

                rg11Checked = true;
                uploadResult();
            }
        });

        radioGroup12 = view.findViewById(R.id.radio_category12);
        radioGroup12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c12_1:
                        rg12Result = 5;
                        break;
                    case R.id.c12_2:
                        rg12Result = 4;
                        break;
                    case R.id.c12_3:
                        rg12Result = 3;
                        break;
                    case R.id.c12_4:
                        rg12Result = 2;
                        break;
                    case R.id.c12_5:
                        rg12Result = 1;
                        break;
                }

                rg12Checked = true;
                uploadResult();
            }
        });

        total = rg10Result + rg11Result + rg12Result;

        return view;
    }

    private void setInitialize(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutSurvey4 = view.findViewById(R.id.layoutSurvey4);
        layout_loading = view.findViewById(R.id.layout_loading);

        q10 = view.findViewById(R.id.q10);
        q11 = view.findViewById(R.id.q11);
        q12 = view.findViewById(R.id.q12);
    }

    private void loadSurvey() {
        mDatabase.child("Database").child("MyDear").child("Problems").child("Group4").child("Q1").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q10.setText(task.getResult().getValue().toString());
                            isQ10 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group4").child("Q2").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q11.setText(task.getResult().getValue().toString());
                            isQ11 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group4").child("Q3").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q12.setText(task.getResult().getValue().toString());
                            isQ12 = true;
                            setVisibility();
                        }
                    }
                });
    }

    public void setVisibility() {
        if (isQ10 && isQ11 && isQ12) {
            layout_loading.setVisibility(View.GONE);
            layoutSurvey4.setVisibility(View.VISIBLE);
        }
    }

    private void uploadResult() {
        total = rg10Result + rg11Result + rg12Result;
        totalChecked = rg10Checked && rg11Checked && rg12Checked;
        result.setGroup4Result(total);
        result.setGroup4IsChecked(totalChecked);
    }

}
