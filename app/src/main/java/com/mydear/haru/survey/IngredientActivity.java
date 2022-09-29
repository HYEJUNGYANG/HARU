package com.mydear.haru.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mydear.haru.R;

public class IngredientActivity extends AppCompatActivity {

    private LinearLayout layout_caution;

    private ImageView iv_haru;

    private TextView tv_name_ko;
    private TextView tv_caution;
    private TextView tv_caution_info1;
    private TextView tv_caution_info2;
    private TextView tv_caution_info3;
    private TextView tv_caution_info4;

    private Button btn_close;

    private boolean isVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        layout_caution = findViewById(R.id.layout_caution);

        iv_haru = findViewById(R.id.iv_haru);
        iv_haru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTest();
            }
        });

        tv_name_ko = findViewById(R.id.tv_name_ko);

        Intent name = getIntent();
        String ingredient_name = name.getStringExtra("ingredient_name");
        tv_name_ko.setText(ingredient_name);

        tv_caution = findViewById(R.id.tv_caution);
        changeTextColor(tv_caution, "주의성분", "#FD9F28", false);
        tv_caution_info1 = findViewById(R.id.tv_caution_info1);
        changeTextColor(tv_caution_info1, "피부 자극, 알레르기 및 건조함을 유발", "#1187CF", false);
        tv_caution_info2 = findViewById(R.id.tv_caution_info2);
        changeTextColor(tv_caution_info2, "혈액으로 발암물질을 보내요", "#1187CF", false);
        tv_caution_info3 = findViewById(R.id.tv_caution_info3);
        changeTextColor(tv_caution_info3, "백내장의 원인", "#1187CF", false);
        tv_caution_info4 = findViewById(R.id.tv_caution_info4);
        changeTextColor(tv_caution_info4, "어린이의 눈에 상해", "#1187CF", false);

        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void clickTest() {
        if (isVisibility) {
            layout_caution.setVisibility(View.GONE);
        }
        else {
            layout_caution.setVisibility(View.VISIBLE);
        }
        isVisibility = !isVisibility;
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
        // 이미 textStyle bold인 경우는 false, true 상관없이 그냥 기본 옵션 따라가고 style 지정 안되어 있을 때만 바뀜
        if (isBold) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tv_str.setText(spannableString); // 바꾼 텍스트 적용
    }
}