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

public class SurveyGroup1Fragment extends Fragment {
    private LinearLayout layoutSurvey1;
    private ConstraintLayout layout_loading;
    private TextView q1;
    private TextView q2;
    private TextView q3;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;

    private int rg1Result = 0, rg2Result = 0, rg3Result = 0;
    private int total;

    private boolean rg1Checked = false ,rg2Checked = false, rg3Checked = false, totalChecked = false;
    private boolean isQ1 = false, isQ2 = false , isQ3 = false;

    public Result result;

    public DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_group1, container, false);
        result = new Result();

        setInitialize(view);
        loadSurvey();

        radioGroup1 = view.findViewById(R.id.radio_category1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c1_1:
                        rg1Result = 5;
                        break;
                    case R.id.c1_2:
                        rg1Result = 4;
                        break;
                    case R.id.c1_3:
                        rg1Result = 3;
                        break;
                    case R.id.c1_4:
                        rg1Result = 2;
                        break;
                    case R.id.c1_5:
                        rg1Result = 1;
                        break;
                }

                rg1Checked = true;
                uploadResult();
            }
        });

        radioGroup2 = view.findViewById(R.id.radio_category2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c2_1:
                        rg2Result = 5;
                        break;
                    case R.id.c2_2:
                        rg2Result = 4;
                        break;
                    case R.id.c2_3:
                        rg2Result = 3;
                        break;
                    case R.id.c2_4:
                        rg2Result = 2;
                        break;
                    case R.id.c2_5:
                        rg2Result = 1;
                        break;
                }

                rg2Checked = true;
                uploadResult();
            }
        });

        radioGroup3 = view.findViewById(R.id.radio_category3);
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c3_1:
                        rg3Result = 5;
                        break;
                    case R.id.c3_2:
                        rg3Result = 4;
                        break;
                    case R.id.c3_3:
                        rg3Result = 3;
                        break;
                    case R.id.c3_4:
                        rg3Result = 2;
                        break;
                    case R.id.c3_5:
                        rg3Result = 1;
                        break;
                }

                rg3Checked = true;
                uploadResult();
            }
        });

        total = rg1Result + rg2Result + rg3Result;

        return view;
    }

    private void setInitialize(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutSurvey1 = view.findViewById(R.id.layoutSurvey1);
        layout_loading = view.findViewById(R.id.layout_loading);

        q1 = view.findViewById(R.id.q1);
        q2 = view.findViewById(R.id.q2);
        q3 = view.findViewById(R.id.q3);
    }

    private void loadSurvey() {
        mDatabase.child("Database").child("MyDear").child("Problems").child("Group1").child("Q1").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        q1.setText(task.getResult().getValue().toString());
                        isQ1 = true;
                        setVisibility();
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group1").child("Q2").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q2.setText(task.getResult().getValue().toString());
                            isQ2 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group1").child("Q3").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q3.setText(task.getResult().getValue().toString());
                            isQ3 = true;
                            setVisibility();
                        }
                    }
                });
    }

    public void setVisibility() {
        if (isQ1 && isQ2 && isQ3) {
            layout_loading.setVisibility(View.GONE);
            layoutSurvey1.setVisibility(View.VISIBLE);
        }
    }

    public void uploadResult() {
        total = rg1Result + rg2Result + rg3Result;
        totalChecked = rg1Checked && rg2Checked && rg3Checked;
        result.setGroup1Result(total);
        result.setGroup1IsChecked(totalChecked);
    }

}
