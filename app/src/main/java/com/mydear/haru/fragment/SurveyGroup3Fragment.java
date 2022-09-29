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

public class SurveyGroup3Fragment extends Fragment {
    private LinearLayout layoutSurvey3;
    private ConstraintLayout layout_loading;
    private TextView q7;
    private TextView q8;
    private TextView q9;
    private RadioGroup radioGroup7;
    private RadioGroup radioGroup8;
    private RadioGroup radioGroup9;

    private int rg7Result = 0, rg8Result = 0, rg9Result = 0;
    private int total;

    private boolean rg7Checked = false, rg8Checked = false, rg9Checked = false, totalChecked = false;
    private boolean isQ7 = false, isQ8 = false, isQ9 = false;

    public Result result;

    public DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_group3, container, false);
        result = new Result();

        setInitialize(view);
        loadSurvey();

        radioGroup7 = view.findViewById(R.id.radio_category7);
        radioGroup7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c7_1:
                        rg7Result = 5;
                        break;
                    case R.id.c7_2:
                        rg7Result = 4;
                        break;
                    case R.id.c7_3:
                        rg7Result = 3;
                        break;
                    case R.id.c7_4:
                        rg7Result = 2;
                        break;
                    case R.id.c7_5:
                        rg7Result = 1;
                        break;
                }

                rg7Checked = true;
                uploadResult();
            }
        });

        radioGroup8 = view.findViewById(R.id.radio_category8);
        radioGroup8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c8_1:
                        rg8Result = 5;
                        break;
                    case R.id.c8_2:
                        rg8Result = 4;
                        break;
                    case R.id.c8_3:
                        rg8Result = 3;
                        break;
                    case R.id.c8_4:
                        rg8Result = 2;
                        break;
                    case R.id.c8_5:
                        rg8Result = 1;
                        break;
                }

                rg8Checked = true;
                uploadResult();
            }
        });

        radioGroup9 = view.findViewById(R.id.radio_category9);
        radioGroup9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c9_1:
                        rg9Result = 5;
                        break;
                    case R.id.c9_2:
                        rg9Result = 4;
                        break;
                    case R.id.c9_3:
                        rg9Result = 3;
                        break;
                    case R.id.c9_4:
                        rg9Result = 2;
                        break;
                    case R.id.c9_5:
                        rg9Result = 1;
                        break;
                }

                rg9Checked = true;
                uploadResult();
            }
        });

        total = rg7Result + rg8Result + rg9Result;

        return view;
    }

    private void setInitialize(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutSurvey3 = view.findViewById(R.id.layoutSurvey3);
        layout_loading = view.findViewById(R.id.layout_loading);

        q7 = view.findViewById(R.id.q7);
        q8 = view.findViewById(R.id.q8);
        q9 = view.findViewById(R.id.q9);
    }

    private void loadSurvey() {
        mDatabase.child("Database").child("MyDear").child("Problems").child("Group3").child("Q1").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q7.setText(task.getResult().getValue().toString());
                            isQ7 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group3").child("Q2").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q8.setText(task.getResult().getValue().toString());
                            isQ8 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group3").child("Q3").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q9.setText(task.getResult().getValue().toString());
                            isQ9 = true;
                            setVisibility();
                        }
                    }
                });
    }

    public void setVisibility() {
        if (isQ7 && isQ8 && isQ9) {
            layout_loading.setVisibility(View.GONE);
            layoutSurvey3.setVisibility(View.VISIBLE);
        }
    }

    private void uploadResult() {
        total = rg7Result + rg8Result + rg9Result;
        totalChecked = rg7Checked && rg8Checked && rg9Checked;
        result.setGroup3Result(total);
        result.setGroup3IsChecked(totalChecked);
    }

}
