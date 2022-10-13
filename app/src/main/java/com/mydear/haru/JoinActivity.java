package com.mydear.haru;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JoinActivity extends AppCompatActivity {

    private ScrollView scrollView;

    private Button btn_join;
    private EditText et_id;
    private EditText et_pw;
    private EditText et_pw_check;
    private EditText et_name;
    private TextView tv_id_filled;
    private TextView tv_pw_filled;
    private TextView tv_wrong;
    private TextView tv_name_filled;

    private String str_id;
    private String str_pw;
    private String str_pw_check;
    private String str_name;

    private boolean id_value = false;
    private boolean pw_value = false;
    private boolean pw_check_value = false;
    private boolean name_value = false;
    private boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollView = findViewById(R.id.scrollview);

        tv_id_filled = findViewById(R.id.tv_id_filled);
        tv_pw_filled = findViewById(R.id.tv_pw_filled);
        tv_wrong = findViewById(R.id.tv_wrong);
        tv_name_filled = findViewById(R.id.tv_name_filled);

        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                Log.d(TAG, "암호화 전 비밀번호: " + id);
                Toast.makeText(JoinActivity.this, "회원가입", Toast.LENGTH_SHORT).show();

                id = getHash(id);
                Log.d(TAG, "암호화 후 비밀번호: " + id);
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
                if(str_id.length() != 0) {
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
                str_pw_check = et_pw_check.getText().toString();
                if (str_pw.length() != 0) {
                    pw_value = true;
                    tv_pw_filled.setVisibility(View.INVISIBLE);

                    if (str_pw_check.length() != 0 && !str_pw.equals(str_pw_check)) {
                        pw_check_value = false;
                        tv_wrong.setVisibility(View.VISIBLE);
                    }
                    else {
                        pw_check_value = true;
                        tv_wrong.setVisibility(View.INVISIBLE);
                    }
                }
                else {
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

        et_pw_check = findViewById(R.id.et_pw_check);
        et_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str_pw = et_pw.getText().toString();
                str_pw_check = et_pw_check.getText().toString();
                if(str_pw_check.length() != 0) {
                    tv_wrong.setVisibility(View.INVISIBLE);
                    if (str_pw.equals(str_pw_check)) {
                        tv_wrong.setVisibility(View.INVISIBLE);
                        pw_check_value = true;
                    }
                    else {
                        Log.d(TAG, "비밀번호 확인 값: " + str_pw_check + ",    비밀번호 값: " + str_pw);
                        tv_wrong.setText("비밀번호가 일치하지 않아요.");
                        tv_wrong.setVisibility(View.VISIBLE);
                        pw_check_value = false;
                    }
                }
                else {
                    pw_check_value = false;
                }
                checkAllValue();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_pw_check.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollBy(0, 500);
                        }
                    }, 100);
                }
                else if(!b && !pw_check_value) {
                    tv_wrong.setText("비밀번호를 다시 입력해주세요.");
                    tv_wrong.setVisibility(View.VISIBLE);
                }
            }
        });
        et_name = findViewById(R.id.et_name);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str_name = et_name.getText().toString();
                if(str_name.length() != 0) {
                    name_value = true;
                    tv_name_filled.setVisibility(View.INVISIBLE);
                }
                else {
                    name_value = false;
                }
                checkAllValue();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 100);
                }
                else if (!b && !name_value) {
                    tv_name_filled.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static String getHash(String str) {
        String digest = "";
        try{

            //암호화
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); // SHA-256 해시함수를 사용
            sh.update(str.getBytes()); // str의 문자열을 해싱하여 sh에 저장
            byte byteData[] = sh.digest(); // sh 객체의 다이제스트를 얻는다.

            //얻은 결과를 string으로 변환
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

    public void checkAllValue() {
        value = id_value && pw_value && pw_check_value && name_value;
        if (value) {
            btn_join.setEnabled(true);
            btn_join.setBackground(getDrawable(R.drawable.edit_rect_navi_loginver));
        } else  {
            btn_join.setEnabled(false);
            btn_join.setBackground(getDrawable(R.drawable.edit_rect_gray));
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
//        finish();
    }
}