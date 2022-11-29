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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    //private SearchProductResultFragment searchProductResultFragment;
    private FragmentTransaction transaction;
    private ListView lstProducts;
    private ProductAdapter productAdapter;

    private DatabaseReference mDatabase;
    private ArrayList<Product> productDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fragmentManager = getSupportFragmentManager();
        searchWordFragment = new SearchWordFragment();
        //searchProductResultFragment = new SearchProductResultFragment();
        //searchProductResultFragment.setContext(SearchActivity.this);

        lstProducts = (ListView) findViewById(R.id.lstProducts);
        productAdapter = new ProductAdapter(SearchActivity.this);

        et_searchBar = (EditText)findViewById(R.id.et_searchBar);
        et_searchBar.requestFocus();
        et_searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch(i) {
                    case KeyEvent.KEYCODE_ENTER:
                        String keyword = et_searchBar.getText().toString();
                        search(keyword);
                        return true;
                }
                return false;
            }
        });
        // 키보드 띄우기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

//        showFragment(2);
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

        productDatas = new ArrayList<Product>();
        readDatas(productDatas);
    }

//    public void showFragment(int index) {
//
//        switch (index) {
//            case 1:
//                transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment_search, searchWordFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
//                break;
//            case 2:
//                //transaction = fragmentManager.beginTransaction();
//                //transaction.replace(R.id.fragment_search, searchProductResultFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
//                break;
//        }
//    }

    public void readDatas(ArrayList<Product> datas) {
        mDatabase.child("Database").child("MyDear").child("products").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()) {
                    return;
                }

                String products = String.valueOf(task.getResult().getValue());
                int count = Integer.parseInt(JSONParser.getJsonObject(products, "count"));
                for(int i = 0; i < count; i++) {
                    String product = JSONParser.getJsonObject(products, String.valueOf(i));
                    Log.e(TAG, "확인좀: " + product);
                    String name = JSONParser.getJsonObject(product, "name");
                    String brand = JSONParser.getJsonObject(product, "brand");
                    String imageURL = JSONParser.getJsonObject(product, "imageURL");
                    String detailURL = JSONParser.getJsonObject(product, "detail");

                    datas.add(new Product(name, brand, imageURL, detailURL));
                }
            }
        });
    }

    public void search(String keyword) {
        productAdapter.emptyList();

        int count = productDatas.size();
        for (int i = 0; i < count; i++) {
            String name = productDatas.get(i).getTv_product_name();
            String brand = productDatas.get(i).getTv_brand();
            String imageURL = productDatas.get(i).getImageURL();
            String detailURL = productDatas.get(i).getDetailURL();

            if (name.toLowerCase().indexOf(keyword.toLowerCase()) >= 0 ||
                    brand.toLowerCase().indexOf(keyword.toLowerCase()) >= 0) {
                productAdapter.addProduct(name, brand, imageURL, detailURL, new ProductAdapter.OnClickListener() {
                    @Override
                    public void onClick(int index, String name, String brand, String imageURL, String detailURL) {
                        Log.e(TAG, String.valueOf(index));
                        Log.e(TAG, name);
                        Log.e(TAG, brand);
                        Log.e(TAG, imageURL);
                        Log.e(TAG, detailURL);
                        Intent intent = new Intent(SearchActivity.this, ProductInfoActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }
        }

        lstProducts.setAdapter(productAdapter);
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
