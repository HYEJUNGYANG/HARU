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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mydear.haru.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PerfumeSelectActivity extends AppCompatActivity {

    private TextView tv_title;

    private Button btn_result;

    private Set<String> list;
    private Map<String, String> perfume;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfume_select);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // default: 뒤로가기 버튼 모양

        perfume = new HashMap<>();
        list = new HashSet<>();

        tv_title = findViewById(R.id.tv_title);
        changeTextColor(tv_title, "1가지 이상", "#FD8A69", false);

        btn_result = findViewById(R.id.btn_result);
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    Toast.makeText(PerfumeSelectActivity.this, "하나 이상 선택해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PerfumeSelectActivity.this, RecommendActivity.class);
                // 이후 값 저장 or 값 넘기기 코드 작성 필수!!
                Toast.makeText(PerfumeSelectActivity.this, "RECOMMEND ✨", Toast.LENGTH_SHORT).show();

                // 나중에 지워질 코드
                for (String s : list) {
                    System.out.println(s + "향");
                }
                //
                startActivity(intent);
            }
        });
    }

    public void clickView(View view) {
        TextView textView = findViewById(view.getId());
        String content = textView.getText().toString();

        changeColorAndStyle(textView, content);
    }

    public void changeColorAndStyle(TextView text, String str) {
        String content = str.substring(1);  // # 제거
        /**
         * -16777216: #000000 (black)
         * -15628337: #1187CF (선택됐을 때 색상)
         */
        if (text.getCurrentTextColor() == -16777216) {  // 선택되지 않았을 때
            if (count == 6) {
                Toast.makeText(this, "최대 6개까지만 선택 가능해요", Toast.LENGTH_SHORT).show();
                return;
            }
            text.setTextColor(-15628337);  // changeTextColor로 통해 일부 색상을 변경하면 text값 불러올 때 기본 검정색으로 고정되기 때문에 작성
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            changeTextColor(text, str, "#1187CF", true);   // Bold로 변경
            count += 1;
            System.out.println("현재 count: " + count);

            savePerfumeText(content);
        }
        else if(text.getCurrentTextColor() == -15628337) {  // 선택된 상태일 때
            text.setTextColor(-16777216);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            changeTextColor(text, str, "#000000", false);
            count = count > 0 ? --count : 0;
            System.out.println("현재 count: " + count);

            savePerfumeText(content);
        }
    }

    // DB에 넘기기 위해 클릭된 향 이름들 저장
    public void savePerfumeText(String str) {
        if (list.contains(str)) {
            list.remove(str);
        }
        else {
            list.add(str);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);  // 전환모션 제거
    }
}