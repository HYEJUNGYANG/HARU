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

public class SurveyGroup2Fragment extends Fragment {
    private LinearLayout layoutSurvey2;
    private ConstraintLayout layout_loading;
    private TextView q4;
    private TextView q5;
    private TextView q6;
    private RadioGroup radioGroup4;
    private RadioGroup radioGroup5;
    private RadioGroup radioGroup6;

    private int rg4Result = 0, rg5Result = 0, rg6Result = 0;
    private int total;

    private boolean rg4Checked = false, rg5Checked = false, rg6Checked = false, totalChecked = false;
    private boolean isQ4 = false, isQ5 = false, isQ6 = false;

    public Result result;

    public DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_group2, container, false);
        result = new Result();

        setInitialize(view);
        loadSurvey();

        radioGroup4 = view.findViewById(R.id.radio_category4);
        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c4_1:
                        rg4Result = 5;
                        break;
                    case R.id.c4_2:
                        rg4Result = 4;
                        break;
                    case R.id.c4_3:
                        rg4Result = 3;
                        break;
                    case R.id.c4_4:
                        rg4Result = 2;
                        break;
                    case R.id.c4_5:
                        rg4Result = 1;
                        break;
                }

                rg4Checked = true;
                uploadResult();
            }
        });

        radioGroup5 = view.findViewById(R.id.radio_category5);
        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c5_1:
                        rg5Result = 5;
                        break;
                    case R.id.c5_2:
                        rg5Result = 4;
                        break;
                    case R.id.c5_3:
                        rg5Result = 3;
                        break;
                    case R.id.c5_4:
                        rg5Result = 2;
                        break;
                    case R.id.c5_5:
                        rg5Result = 1;
                        break;
                }

                rg5Checked = true;
                uploadResult();
            }
        });

        radioGroup6 = view.findViewById(R.id.radio_category6);
        radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.c6_1:
                        rg6Result = 5;
                        break;
                    case R.id.c6_2:
                        rg6Result = 4;
                        break;
                    case R.id.c6_3:
                        rg6Result = 3;
                        break;
                    case R.id.c6_4:
                        rg6Result = 2;
                        break;
                    case R.id.c6_5:
                        rg6Result = 1;
                        break;
                }

                rg6Checked = true;
                uploadResult();
            }
        });

        total = rg4Result + rg5Result + rg6Result;

        return view;
    }

    private void setInitialize(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        layoutSurvey2 = view.findViewById(R.id.layoutSurvey2);
        layout_loading = view.findViewById(R.id.layout_loading);

        q4 = view.findViewById(R.id.q4);
        q5 = view.findViewById(R.id.q5);
        q6 = view.findViewById(R.id.q6);
    }

    private void loadSurvey() {
        mDatabase.child("Database").child("MyDear").child("Problems").child("Group2").child("Q1").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q4.setText(task.getResult().getValue().toString());
                            isQ4 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group2").child("Q2").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q5.setText(task.getResult().getValue().toString());
                            isQ5 = true;
                            setVisibility();
                        }
                    }
                });

        mDatabase.child("Database").child("MyDear").child("Problems").child("Group2").child("Q3").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        else {
                            q6.setText(task.getResult().getValue().toString());
                            isQ6 = true;
                            setVisibility();
                        }
                    }
                });
    }

    public void setVisibility() {
        if (isQ4 && isQ5 && isQ6) {
            layout_loading.setVisibility(View.GONE);
            layoutSurvey2.setVisibility(View.VISIBLE);
        }
    }

    public void uploadResult() {
        total = rg4Result + rg5Result + rg6Result;
        totalChecked = rg4Checked && rg5Checked && rg6Checked;
        result.setGroup2Result(total);
        result.setGroup2IsChecked(totalChecked);
    }

}
