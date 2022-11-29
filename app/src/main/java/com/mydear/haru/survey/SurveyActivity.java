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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mydear.haru.JSONParser;
import com.mydear.haru.R;
import com.mydear.haru.User;
import com.mydear.haru.fragment.SurveyGroup1Fragment;
import com.mydear.haru.fragment.SurveyGroup2Fragment;
import com.mydear.haru.fragment.SurveyGroup3Fragment;
import com.mydear.haru.fragment.SurveyGroup4Fragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        result = new Result();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        // 두피 진단 결과가 있지만 다시 하는 경우를 위한 초기화
        if (!user.getType().equals("none")) {
            user.setType("none");
            mDatabase.child("Database").child("MyDear").child("User").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()) {
                        return;
                    }

                    String user_id = user.getId();
                    Log.e("userId: ", "" + user_id);
                    user_id = getHash(user_id);
                    String user_type = user.getType();
                    Log.e("Type: ", "다시 측정!!" + user_type);

                    String users = String.valueOf(task.getResult().getValue());
                    int count = Integer.parseInt(JSONParser.getJsonObject(users, "count"));
                    for(int i = 0; i < count; i++) {
                        String user = JSONParser.getJsonObject(users, String.valueOf(i));
                        String id = JSONParser.getJsonObject(user, "id");
                        if (id.equals(user_id)) {
                            mDatabase.child("Database").child("MyDear").child("User").child(String.valueOf(i)).child("type").setValue(user_type);
                        }
                    }
                }
            });
        }

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

                    // 액티비티 전환 및 그 액티비티로 값 전달
                    Intent intent = new Intent(SurveyActivity.this, SurveyResultActivity.class);

                    // 값 전달
                    intent.putExtra("user", user);
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

                // group4를 제외한 나머지 그룹은 전부 체크가 되었을 때
                if(result.isGroup1IsChecked() && result.isGroup2IsChecked()
                        && result.isGroup3IsChecked() && !result.isGroup4IsChecked()) {
                    return;  // 버튼 변경 없이 완료하기 유지한채 그룹4 체크되도록
                }
                else {
                    btn_submit.setVisibility(View.GONE);
                    btn_back.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.VISIBLE);
                }
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

    public static String getHash(String str) {
        String digest = "";
        try{

            // 암호화
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); // SHA-256 해시함수를 사용
            sh.update(str.getBytes()); // str의 문자열을 해싱하여 sh에 저장
            byte byteData[] = sh.digest(); // sh 객체의 다이제스트를 얻는다.

            // 얻은 결과를 string으로 변환
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            digest = sb.toString();
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace(); digest = null;
        }
        return digest;
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