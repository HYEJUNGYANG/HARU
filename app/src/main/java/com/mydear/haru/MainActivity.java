package com.mydear.haru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mydear.haru.survey.SelectSurveyActivity;
import com.mydear.haru.survey.SurveyResultActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private Dialog surveyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        surveyDialog = new Dialog(MainActivity.this);  // 초기화
        surveyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // 타이틀 제거
        surveyDialog.setContentView(R.layout.activity_survey_dialog); // xml 레이아웃

        showSurveyDialog();

        Button btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSurveyDialog();
            }
        });

        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SurveyResultActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showSurveyDialog() {
        surveyDialog.show();

        // 주의사항!! 여기서는 findViewById 앞에 다이얼로그 이름을 붙여줘야 한다

        Button btn_yes = surveyDialog.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectSurveyActivity.class);
                startActivity(intent);
                surveyDialog.dismiss();
            }
        });

        Button btn_no = surveyDialog.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyDialog.dismiss();
            }
        });
    }
}
































