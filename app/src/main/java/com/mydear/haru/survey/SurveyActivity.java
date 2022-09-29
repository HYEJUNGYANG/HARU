package com.mydear.haru.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mydear.haru.R;
import com.mydear.haru.fragment.SurveyGroup1Fragment;
import com.mydear.haru.fragment.SurveyGroup2Fragment;
import com.mydear.haru.fragment.SurveyGroup3Fragment;
import com.mydear.haru.fragment.SurveyGroup4Fragment;

import io.github.muddz.styleabletoast.StyleableToast;

public class SurveyActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button btn_back;
    private Button btn_next;
    private Button btn_submit;

    private int surveyPage = 1;

    private FragmentManager fragmentManager;
    private SurveyGroup1Fragment surveyGroup1Fragment;
    private SurveyGroup2Fragment surveyGroup2Fragment;
    private SurveyGroup3Fragment surveyGroup3Fragment;
    private SurveyGroup4Fragment surveyGroup4Fragment;
    private FragmentTransaction transaction;

    public Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        result = new Result();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼

        fragmentManager = getSupportFragmentManager();
        surveyGroup1Fragment = new SurveyGroup1Fragment();
        surveyGroup2Fragment = new SurveyGroup2Fragment();
        surveyGroup3Fragment = new SurveyGroup3Fragment();
        surveyGroup4Fragment = new SurveyGroup4Fragment();

        showFragment(surveyPage);

        btn_submit = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveyPage == 1) {
                    StyleableToast.makeText(SurveyActivity.this, "첫번째 페이지입니다 ✨", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
//                    Toast.makeText(SurveyActivity.this, "첫번째 페이지입니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showFragment(--surveyPage);
                }
            }
        });

        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveyPage == 3) {
                    showFragment(++surveyPage);
                    btn_back.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                    return;
                }
                showFragment(++surveyPage);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result.isGroup1IsChecked() && result.isGroup2IsChecked()
                        && result.isGroup3IsChecked() && result.isGroup4IsChecked()) {
                    StyleableToast.makeText(SurveyActivity.this, "submit!!! ✨😆", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
//                    Toast.makeText(SurveyActivity.this, "submit!!! ✨😆", Toast.LENGTH_SHORT).show();

                    // 액티비티 전환 및 그 액티비티로 값 전달
                    Intent intent = new Intent(SurveyActivity.this, SurveyResultActivity.class);

                    // 값 전달
                    intent.putExtra("group1Result", result.getGroup1Result());
                    intent.putExtra("group2Result", result.getGroup2Result());
                    intent.putExtra("group3Result", result.getGroup3Result());
                    intent.putExtra("group4Result", result.getGroup4Result());

                    // 액티비티 전환
                    startActivity(intent);
                    overridePendingTransition(0,0);  // 전환모션 제거

                    return;
                }
                vibrator.vibrate(200);
                StyleableToast.makeText(SurveyActivity.this, "모든 항목을 체크해주세요 😀", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
//                Toast.makeText(SurveyActivity.this, "모든 항목을 체크해주세요 😀", Toast.LENGTH_SHORT).show();
                btn_submit.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showFragment(int index) {

        switch (index) {
            case 1:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, surveyGroup1Fragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
            case 2:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, surveyGroup2Fragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
            case 3:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, surveyGroup3Fragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
            case 4:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, surveyGroup4Fragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
        }
    }

    // 뒤로가기 누르면 체크여부 값들 전부 초기화 (초기화 하지 않으면 앱 선택 안해도 이전 선택된 값들로 결과 나옴)
    public void initResult() {
        result.setGroup1IsChecked(false);
        result.setGroup2IsChecked(false);
        result.setGroup3IsChecked(false);
        result.setGroup4IsChecked(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // 툴바에 있는 뒤로가기버튼 클릭시
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // 휴대폰에 있는 뒤로가기 버튼 눌렸을 때
    public void onBackPressed() {
        initResult();
        super.onBackPressed();
        finish();
        // 팝업 띄워서 뒤로가면 하던 설문조사 데이터 날아간다는 거 알리고 yes or no 선택하게
        overridePendingTransition(0,0);  // 애니메이션 제거
    }
}