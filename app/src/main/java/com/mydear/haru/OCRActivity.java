package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OCRActivity extends AppCompatActivity {

    private TextView tv_description;
    private TextView tv_description2;
    private TextView tv_notice;
    private Button btn_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocractivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 기본 title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_back);

        tv_description = findViewById(R.id.tv_description);
        String str_description = tv_description.getText().toString();
        tv_description2 = findViewById(R.id.tv_description2);
        String str_description2 = tv_description2.getText().toString();

        // 텍스트 색상 변경
        SpannableStringBuilder spannable1 = new SpannableStringBuilder(str_description);
        spannable1.setSpan(
                new ForegroundColorSpan(Color.RED),
                25,
                31,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        spannable1.setSpan(
                new ForegroundColorSpan(Color.RED),
                43,
                61,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_description.setText(spannable1);

        SpannableStringBuilder spannable2 = new SpannableStringBuilder(str_description2);
        spannable2.setSpan(
                new ForegroundColorSpan(Color.RED),
                0,
                7,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        spannable2.setSpan(
                new ForegroundColorSpan(Color.RED),
                36,
                50,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_description2.setText(spannable2);

        tv_notice = findViewById(R.id.tv_notice);
        String str_notice = tv_notice.getText().toString();

        SpannableStringBuilder spannable3 = new SpannableStringBuilder(str_notice);
        spannable3.setSpan(
                new ForegroundColorSpan(Color.RED),
                12,
                19,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_notice.setText(spannable3);


        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OCRActivity.this, "촬영 버튼 클릭! ✨", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "BACK 버튼 클릭! ✨", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}