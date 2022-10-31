package com.mydear.haru;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_id_filled;
    private TextView tv_pw_filled;
    private TextView tv_join;
    private EditText et_id;
    private EditText et_pw;

    private Button btn_login;

    private String str_id;
    private String str_pw;
    private boolean id_value = false;
    private boolean pw_value = false;
    private boolean value = false;

    private String test_id = "test@naver.com";
    private String test_pw = "1234";

    private String testHashId = "";
    private String testHashPw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_id_filled = findViewById(R.id.tv_id_filled);

        tv_pw_filled = findViewById(R.id.tv_pw_filled);

        tv_join = findViewById(R.id.tv_join);
        tv_join.setPaintFlags(tv_join.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // 텍스트 밑줄
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToJoinActivity();
            }
        });

        et_id = findViewById(R.id.et_id);
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str_id = et_id.getText().toString();
                if (str_id.length() != 0) {
                    id_value = true;
                    tv_id_filled.setVisibility(View.INVISIBLE);
                }
                else {
                    id_value = false;
                }
                checkAllValue();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !id_value) {
                    tv_id_filled.setVisibility(View.VISIBLE);
                }
            }
        });

        et_pw = findViewById(R.id.et_pw);
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str_pw = et_pw.getText().toString();
                if (str_pw.length() != 0) {
                    pw_value = true;
                    tv_pw_filled.setVisibility(View.INVISIBLE);
                } else {
                    pw_value = false;
                }
                checkAllValue();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !pw_value) {
                    tv_pw_filled.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                id = getHash(id);
                pw = getHash(pw);

                if (id.equals(testHashId) && pw.equals(testHashPw)) {
                    Toast.makeText(LoginActivity.this, "로그인 성공 ✨", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finishAffinity();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else {
                    Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 임시로 넣은 테스트용 id, pw 확인
        testHashId = getHash(test_id);
        testHashPw = getHash(test_pw);
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

    public void goToJoinActivity() {
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void checkAllValue() {
        value = id_value && pw_value;
        if (value) {
            btn_login.setEnabled(true);
            btn_login.setBackground(getDrawable(R.drawable.edit_rect_navi_loginver));
        }
        else {
            btn_login.setEnabled(false);
            btn_login.setBackground(getDrawable(R.drawable.edit_rect_gray));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }
}