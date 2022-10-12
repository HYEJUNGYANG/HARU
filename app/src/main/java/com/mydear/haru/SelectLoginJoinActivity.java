package com.mydear.haru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SelectLoginJoinActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_join;


    private Dialog surveyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login_join);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // title 제거

        // 상태바 투명처리
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLoginJoinActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLoginJoinActivity.this, JoinActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

//        surveyDialog = new Dialog(SelectLoginJoinActivity.this);  // 초기화
//        surveyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // 타이틀 제거
//        surveyDialog.setContentView(R.layout.activity_survey_dialog); // xml 레이아웃

//        showSurveyDialog();
    }

//    public void showSurveyDialog() {
//        surveyDialog.show();
//
//        // 주의사항!! 여기서는 findViewById 앞에 다이얼로그 이름을 붙여줘야 한다
//
//        Button btn_yes = surveyDialog.findViewById(R.id.btn_yes);
//        btn_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SelectLoginJoinActivity.this, MainActivity.class);
//                startActivity(intent);
//                surveyDialog.dismiss();
//            }
//        });
//
//        Button btn_no = surveyDialog.findViewById(R.id.btn_no);
//        btn_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                surveyDialog.dismiss();
//            }
//        });
//    }
}
































