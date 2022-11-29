package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mydear.haru.survey.SurveyActivity;
import com.mydear.haru.survey.SurveyResultActivity;

public class MainActivity extends AppCompatActivity {
    private EditText et_search;

    private Button btn_survey;
    private Button btn_ingredients;

    private ImageView btn_close;
    private LinearLayout layout_type;
    private LinearLayout layout_thumb;
    private LinearLayout layout_notice;
    private LinearLayout layout_logout;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TextView tv_name;
    private TextView tv_id;

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // toolbar title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24);

        navigationView = findViewById(R.id.nav);
        navigationView.setItemIconTintList(null);

        drawerLayout = findViewById(R.id.main_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        View nav_header_view = navigationView.getHeaderView(0);

        tv_name = nav_header_view.findViewById(R.id.tv_name);
        tv_name.setText(user.getName());
        tv_id = nav_header_view.findViewById(R.id.tv_id);
        tv_id.setText(user.getId());

        btn_close = nav_header_view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(navigationView);
            }
        });

        layout_type = nav_header_view.findViewById(R.id.layout_type);
        layout_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // type == "none"인 경우
                if (user.getType().equals("none")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog.setMessage("두피 진단 결과가 존재하지 않아요. 두피 진단을 받으러 가시겠어요? 🚀")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "취소 😖", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                }
                            })
                            .setCancelable(false);   // 백버튼으로 팝업창이 닫히지 않도록 설정
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.getWindow().setGravity(Gravity.CENTER);
                    alertDialog.show();
                    TextView msgView = (TextView) alertDialog.findViewById(android.R.id.message);
                    msgView.setTextSize(22.0f);

                    return;
                }
                // 결과가 이미 존재할 때
                Intent intent = new Intent(MainActivity.this, SurveyResultActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
        
        layout_logout = nav_header_view.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다 👋🏻", Toast.LENGTH_SHORT).show();
                user = null;
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finishAffinity();
            }
        });

        et_search = findViewById(R.id.et_search);
        et_search.setCursorVisible(false);  // == et_search.setInputType(EditorInfo.TYPE_NULL)
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    et_search.clearFocus();
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                }
            }
        });

        btn_survey = findViewById(R.id.btn_survey);
        btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.getType().equals("none")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    dialog.setMessage("두피 진단 결과가 이미 존재해요. 진단을 다시 받으시겠어요?\n다시 받으면 이전 결과는 초기화 돼요 😖")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, "취소 😖", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                }
                            })
                            .setCancelable(false);   // 백버튼으로 팝업창이 닫히지 않도록 설정
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.getWindow().setGravity(Gravity.CENTER);
                    alertDialog.show();
                    TextView msgView = (TextView) alertDialog.findViewById(android.R.id.message);
                    msgView.setTextSize(18.0f);

                    return;
                }
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
//                finish();
            }
        });

        btn_ingredients = findViewById(R.id.btn_ingredients);
        btn_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OCRActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}