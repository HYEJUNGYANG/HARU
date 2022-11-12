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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mydear.haru.MainActivity;
import com.mydear.haru.SelectLoginJoinActivity;
import com.mydear.haru.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class RecommendActivity extends AppCompatActivity {
    private TextView tv_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_home);

        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        changeTextColor(tv_toolbar_title, "혜정", "#1187CF", false);
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

    // menu를 toolbar에 생성해즘
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recommend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RecommendActivity.this, MainActivity.class);
                // 이후 여기에 값 전달 or 값 저장 하게 설정 해두기
                StyleableToast.makeText(RecommendActivity.this, "HOME ✨", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
                startActivity(intent);
//                finish();
                finishAffinity();
                return true;
            case R.id.menu_filter:
                // 이후에 filter 클릭시 액티비티 전환되게
                StyleableToast.makeText(RecommendActivity.this, "Filter menu click", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}