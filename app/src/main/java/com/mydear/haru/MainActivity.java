package com.mydear.haru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mydear.haru.survey.SurveyActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_search;
    private Button btn_survey;
    private Button btn_ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "두피케어 제품 찾기 버튼을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 버튼 잘 눌리는지 테스트 해보기 위함
        btn_survey = findViewById(R.id.btn_survey);
        btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
//                finish();
            }
        });

        btn_ingredients = findViewById(R.id.btn_ingredients);
        btn_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "화장품 성분 분석하기 버튼을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}