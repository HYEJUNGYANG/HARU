package com.mydear.haru;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mydear.haru.fragment.search.SearchProductResultFragment;
import com.mydear.haru.fragment.search.SearchWordFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText et_searchBar;
    private Button btn_search;
//    TextView tv_result;
    private ImageView btn_back;

    private FragmentManager fragmentManager;
    private SearchWordFragment searchWordFragment;
    private SearchProductResultFragment searchProductResultFragment;
    private FragmentTransaction transaction;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fragmentManager = getSupportFragmentManager();
        searchWordFragment = new SearchWordFragment();
        searchProductResultFragment = new SearchProductResultFragment();

        et_searchBar = (EditText)findViewById(R.id.et_searchBar);
        et_searchBar.requestFocus();
        // 키보드 띄우기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        showFragment(2);
//        tv_result = (TextView)findViewById(R.id.tv_result);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = et_searchBar.getText().toString();

                search(keyword);
            }
        });
    }

    public void showFragment(int index) {

        switch (index) {
            case 1:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_search, searchWordFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
            case 2:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_search, searchProductResultFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
        }
    }

    public void search(String keyword) {
        mDatabase.child("Database").child("MyDear").child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()) {
                    return;
                }

                ArrayList<String> res = new ArrayList<String>();

                String products = String.valueOf(task.getResult().getValue());
                int count = Integer.parseInt(JSONParser.getJsonObject(products, "count"));
                for(int i = 0; i < count; i++) {
                    String product = JSONParser.getJsonObject(products, String.valueOf(i));
                    String name = JSONParser.getJsonObject(product, "name");

                    if(name.indexOf(keyword) >= 0) {
                        res.add(name);
                    }
                }

//                updateResult(res);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 키보드 숨기기
        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    public void updateResult(ArrayList<String> res) {
//        tv_result.setText("");
//
//        String temp = "";
//        for(int i = 0; i < res.size(); i++) {
//            temp += res.get(i);
//            temp += "\n";
//        }
//
//        tv_result.setText(temp);
    }

}
