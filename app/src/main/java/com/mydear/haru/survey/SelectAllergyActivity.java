package com.mydear.haru.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mydear.haru.R;
import com.mydear.haru.fragment.allergy.AllergySelectDefaultFragment;
import com.mydear.haru.fragment.allergy.AllergySelectYesFragment;

import io.github.muddz.styleabletoast.StyleableToast;

public class SelectAllergyActivity extends AppCompatActivity {

    private TextView tv_question;

    private Button btn_next;

    private RadioGroup yes_or_no;
    private RadioButton yes;
    private RadioButton no;

    private FragmentManager fragmentManager;
    private AllergySelectYesFragment allergySelectYesFragment;
    private AllergySelectDefaultFragment allergySelectDefaultFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_allergy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // default: 뒤로가기 버튼 모양

        fragmentManager = getSupportFragmentManager();
        allergySelectYesFragment = new AllergySelectYesFragment();
        allergySelectDefaultFragment = new AllergySelectDefaultFragment();

        tv_question = findViewById(R.id.tv_question);
        changeTextColor(tv_question, "알레르기", "#FD6F22", false);

        showFragment(1);

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        yes_or_no = findViewById(R.id.yes_or_no);
        yes_or_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(yes.isChecked()) {
                    showFragment(2);
                } else if (no.isChecked()) {
                    showFragment(1);
                }
            }
        });

        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // firebase에 데이터들 저장될 수 있도록 구현하기
                Boolean isChecked = yes.isChecked() ? allergySelectYesFragment.isChecked() : false;
                Intent intent = new Intent(SelectAllergyActivity.this, PerfumeSelectActivity.class);

                if (!yes.isChecked() && !no.isChecked()) {
                    Toast.makeText(SelectAllergyActivity.this, "제외 여부를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (yes.isChecked() && !isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectAllergyActivity.this);
                    builder.setMessage("체크된 항목이 없습니다. 이대로 넘어가시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 다음 액티비티로 넘어가게끔
                            Toast.makeText(SelectAllergyActivity.this, "yes", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            overridePendingTransition(0,0);  // 전환모션 제거
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 닫기
                            Toast.makeText(SelectAllergyActivity.this, "항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                else {
                    StyleableToast.makeText(SelectAllergyActivity.this, "click", Toast.LENGTH_SHORT, R.style.allCheckToast).show();
                    startActivity(intent);
                    overridePendingTransition(0,0);  // 전환모션 제거
                }
            }
        });
    }

    public void showFragment(int index) {

        switch (index) {
            case 1:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.layout_fragment, allergySelectDefaultFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
            case 2:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.layout_fragment, allergySelectYesFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
        }
    }

    public void showIngredient(View v) {
        TextView test = findViewById(v.getId());
        String str = test.getText().toString().trim();
        StyleableToast.makeText(SelectAllergyActivity.this, str + "✨", Toast.LENGTH_SHORT, R.style.allCheckToast).show();

        Intent intent = new Intent(SelectAllergyActivity.this, IngredientActivity.class);
        intent.putExtra("ingredient_name", str);
        startActivity(intent);
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
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}