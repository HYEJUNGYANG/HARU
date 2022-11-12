package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

        View nav_header_view = navigationView.getHeaderView(0);
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
                Toast.makeText(MainActivity.this, "나의 두피 타입 버튼을 클릭하셨습니다 ☆*: .｡. o(≧▽≦)o .｡.:*☆", Toast.LENGTH_SHORT).show();
            }
        });
        
        layout_logout = nav_header_view.findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "로그아웃 버튼을 클릭하셨습니다 ☆*: .｡. o(≧▽≦)o .｡.:*☆", Toast.LENGTH_SHORT).show();
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

        // 버튼 잘 눌리는지 테스트 해보기 위함
        btn_survey = findViewById(R.id.btn_survey);
        btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Toast.makeText(MainActivity.this, "화장품 성분 분석하기 버튼을 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "메뉴 버튼 클릭! ✨", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}