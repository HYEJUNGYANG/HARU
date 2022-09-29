package com.mydear.haru.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mydear.haru.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

public class SurveyResultActivity extends AppCompatActivity {
    private TextView tv_name;
    private TextView tv_description;
    private TextView tv_head;
    private TextView tv_logo;
    private TextView tv_type;
    private TextView tv_recommend;

    private TextView tv_circle1;
    private TextView tv_circle2;
    private TextView tv_circle3;
    private TextView tv_circle4;
    private TextView tv_circle5;

    private TextView tv_c1;
    private TextView tv_c2;
    private TextView tv_c3;
    private TextView tv_c4;
    private TextView tv_c5;
    private TextView tv_c6;
    private TextView tv_c7;
    private TextView tv_c8;
    private TextView tv_c9;
    private TextView tv_c10;
    private TextView tv_c11;
    private TextView tv_c12;

    private Button btn_search;

    private DatabaseReference mDatabase;

    private String type = "";
    private List<Integer> result;

    private boolean isType = false, isCare = false, isTrait = false;

    private Map<String, String> care;
    private Map<String, String> trait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // default: 뒤로가기 버튼 모양, 툴바 왼쪽 버튼 사용 여부
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24); // 왼쪽 버튼 모양 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거

        mDatabase = FirebaseDatabase.getInstance().getReference();
        result = new ArrayList<Integer>();

        care = new HashMap<>();
        trait = new HashMap<>();

        tv_name = findViewById(R.id.tv_name);
        tv_description = findViewById(R.id.tv_description);
        tv_head = findViewById(R.id.tv_head);
        tv_type = findViewById(R.id.tv_type);
        tv_recommend = findViewById(R.id.tv_recommend);

        tv_circle1 = findViewById(R.id.tv_circle1);
        tv_circle2 = findViewById(R.id.tv_circle2);
        tv_circle3 = findViewById(R.id.tv_circle3);
        tv_circle4 = findViewById(R.id.tv_circle4);
        tv_circle5 = findViewById(R.id.tv_circle5);

        tv_logo = findViewById(R.id.tv_logo);
        tv_logo.bringToFront();

        tv_c1 = findViewById(R.id.tv_c1);
        tv_c2 = findViewById(R.id.tv_c2);
        tv_c3 = findViewById(R.id.tv_c3);
        tv_c4 = findViewById(R.id.tv_c4);
        tv_c5 = findViewById(R.id.tv_c5);
        tv_c6 = findViewById(R.id.tv_c6);
        tv_c7 = findViewById(R.id.tv_c7);
        tv_c8 = findViewById(R.id.tv_c8);
        tv_c9 = findViewById(R.id.tv_c9);
        tv_c10 = findViewById(R.id.tv_c10);
        tv_c11 = findViewById(R.id.tv_c11);
        tv_c12 = findViewById(R.id.tv_c12);

        changeTextColor(tv_name, "양혜정", "#3A4CA8", true);

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SurveyResultActivity.this, SelectAllergyActivity.class);
                startActivity(intent);

                StyleableToast.makeText(SurveyResultActivity.this, "click", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
            }
        });

        Intent getResult = getIntent(); // 넘겨준 값을 받아옴
        for (int i=1; i<=4; i++) {
            result.add(getResult.getIntExtra("group" + Integer.toString(i) + "Result", 0));
        }
        getType(result);
    }

    public void getType(List<Integer> result) {
        for (int i=1; i<=4; i++) {
            int gr = result.get(i-1);

            mDatabase.child("Database").child("MyDear").child("Problems").child("Group" + Integer.toString(i))
                    .child(gr >= 9 ? "Over" : "Under").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            type += task.getResult().getValue();

                            isType = type.length() == 4 ? true : false;
                            getCare();
//                            setResultOnFirebase();
                        }
                    });
        }
    }

    public void getCare() {
        if (!isType) {
            return;
        }
        System.out.println("타입결과는?????? " + type);
        mDatabase.child("Database").child("MyDear").child("Type").child(type).child("Care").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        care = (Map<String, String>) task.getResult().getValue();
                        isCare = true;
                        getTrait();
                    }
                });
    }

    public void getTrait() {
        if (!isType) {
            return;
        }
        mDatabase.child("Database").child("MyDear").child("Type").child(type).child("Trait").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        trait = (Map<String, String>) task.getResult().getValue();
                        isTrait = true;
                        getValueFromFirebase();
                    }
                });
    }

    /**
     * Firebase 값 읽어오는 것은 비동기적이므로 그 작업이후에 DB 업로드하기 위함
     */
    public void setResultOnFirebase() {
        if (isType) {
        }
    }

    // textview, 변경하고 싶은 단어, 변경하고자 하는 색, bold체 여부
    public void changeTextColor(TextView tv_str, String w, String color, boolean isBold) {
        String content = tv_str.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = w;
        int start = content.indexOf(word);
        int end = start + word.length();
        // ForegroundColorSpan : 글자 색상 지정
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // StyleSpan: 글자의 스타일 지정 (ex. bold, italic...)
        if (isBold) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tv_str.setText(spannableString); // 바꾼 텍스트 적용
    }

    public void getValueFromFirebase() {
        if (!isType || !isCare || !isTrait) {
            return;
        }
        // 두피 타입 부분
        mDatabase.child("Database").child("MyDear").child("Type").child(type).child("description").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        tv_description.setText(task.getResult().getValue() + "예요.");
                        changeTextColor(tv_description, task.getResult().getValue().toString(), "#3A4CA8", true);
                        tv_head.setText(task.getResult().getValue() + "는 이런 특징이 있어요.");
                    }
                });

        // 오각형 가운데 부분
        mDatabase.child("Database").child("MyDear").child("Type").child(type).child("keyword").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String str = task.getResult().getValue().toString();
                        if (str.contains("n")) {
                            String[] s = str.split("n");
                            tv_type.setText(s[0] + "\n" + s[1]);
                            return;
                        }
                        tv_type.setText(str);
                    }
                });

        // 5가지 특징
        for (Map.Entry<String, String> pair : trait.entrySet()) {
            String str = pair.getValue();
            String[] s = str.split("n");
            
            int length = s.length;
            String last = length == 2 ? "" : "\n" + s[2];

            switch (pair.getKey()) {
                case "t1":
                    tv_circle1.setText(s[0] + "\n" + s[1] + last);
                    break;
                case "t2":
                    tv_circle2.setText(s[0] + "\n" + s[1] + last);
                    break;
                case "t3":
                    tv_circle3.setText(s[0] + "\n" + s[1] + last);
                    break;
                case "t4":
                    tv_circle4.setText(s[0] + "\n" + s[1] + last);
                    break;
                case "t5":
                    tv_circle5.setText(s[0] + "\n" + s[1] + last);
                    break;
            }
        }

        // 케어 추천 부분
        for (Map.Entry<String, String> pair : care.entrySet()) {
            // pair.getKey() -> key값, pair.getValue() -> value값
            switch (pair.getKey()) {
                case "c1":
                    tv_c1.setText(pair.getValue());
                    tv_c1.setVisibility(View.VISIBLE);
                    break;
                case "c2":
                    tv_c2.setText(pair.getValue());
                    tv_c2.setVisibility(View.VISIBLE);
                    break;
                case "c3":
                    tv_c3.setText(pair.getValue());
                    tv_c3.setVisibility(View.VISIBLE);
                    break;
                case "c4":
                    tv_c4.setText(pair.getValue());
                    tv_c4.setVisibility(View.VISIBLE);
                    break;
                case "c5":
                    tv_c5.setText(pair.getValue());
                    tv_c5.setVisibility(View.VISIBLE);
                    break;
                case "c6":
                    tv_c6.setText(pair.getValue());
                    tv_c6.setVisibility(View.VISIBLE);
                    break;
                case "c7":
                    tv_c7.setText(pair.getValue());
                    tv_c7.setVisibility(View.VISIBLE);
                    break;
                case "c8":
                    tv_c8.setText(pair.getValue());
                    tv_c8.setVisibility(View.VISIBLE);
                    break;
                case "c9":
                    tv_c9.setText(pair.getValue());
                    tv_c9.setVisibility(View.VISIBLE);
                    break;
                case "c10":
                    tv_c10.setText(pair.getValue());
                    tv_c10.setVisibility(View.VISIBLE);
                    break;
                case "c11":
                    tv_c11.setText(pair.getValue());
                    tv_c11.setVisibility(View.VISIBLE);
                    break;
                case "c12":
                    tv_c12.setText(pair.getValue());
                    tv_c12.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // 툴바 왼쪽 버튼 클릭시
                // 이후 설정
                StyleableToast.makeText(SurveyResultActivity.this, "menu", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}