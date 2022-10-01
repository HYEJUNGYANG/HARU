package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mydear.haru.fragment.product.ProductDetailFragment;

public class ProductInfoActivity extends AppCompatActivity {

    private ConstraintLayout loading;
    private ConstraintLayout layout_content;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference productRef;

    private Button btn_product_detail;
    private Button btn_product_ingredient;
    private Button btn_product_perfume;

    private ImageView iv_product;
    private ImageView iv_product_detail;

    private FragmentManager fragmentManager;
    private ProductDetailFragment productDetailFragment;
    private FragmentTransaction transaction;

    private String product_name;
    private Boolean isLoadingFinish = false;
    private int num = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // toolbar 왼쪽 버튼
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_back);

        loading = findViewById(R.id.loading);
        layout_content = findViewById(R.id.layout_content);

        // firebase storage
        storage = FirebaseStorage.getInstance("gs://mydear-bb10e.appspot.com/");
        storageRef = storage.getReference();
        productRef = storageRef.child("product");

        product_name = "허니 앤 마카다미아 네이처 샴푸";  // 임시로! 이후에 이전 액티비티에서 이름 밥아오기
        productDetailFragment = new ProductDetailFragment(product_name);
        fragmentManager = getSupportFragmentManager();

        iv_product = findViewById(R.id.iv_product);
//        iv_product_detail = findViewById(R.id.iv_produce_detail);

        btn_product_detail = findViewById(R.id.btn_product_detail);
        btn_product_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonStyle(view, btn_product_ingredient, btn_product_perfume);
            }
        });

        btn_product_ingredient = findViewById(R.id.btn_product_ingredient);
        btn_product_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonStyle(view, btn_product_detail, btn_product_perfume);
            }
        });

        btn_product_perfume = findViewById(R.id.btn_product_perfume);
        btn_product_perfume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonStyle(view, btn_product_detail, btn_product_ingredient);
            }
        });

        getImage(product_name);
    }

    public void changeButtonStyle(View view, Button btn1, Button btn2) {
        Button btn = findViewById(view.getId());
        btn.setBackground(getResources().getDrawable(R.drawable.btn_round2));
        btn.setTextColor(Color.parseColor("#000000"));

        btn1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        btn1.setTextColor(Color.parseColor("#4D000000"));
        btn2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        btn2.setTextColor(Color.parseColor("#4D000000"));
    }

    // firebase storage img 불러오기
    public void getImage(String folderName) {
        StorageReference productPathRef = storageRef.child("product/" + folderName + "/product.png");
//        StorageReference detailPathRef = storageRef.child("product/" + folderName + "/product.png");
        productPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(iv_product);
                loading.setVisibility(View.GONE);
                layout_content.setVisibility(View.VISIBLE);
                showFragment(num);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.GONE);
                layout_content.setVisibility(View.VISIBLE);
                Toast.makeText(ProductInfoActivity.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

//        detailPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(getApplicationContext()).load(uri).into(iv_product_detail);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ProductInfoActivity.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void showFragment(int index) {
        switch (index) {
            case 1:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, productDetailFragment).commitAllowingStateLoss(); //commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "BACK!! ✨", Toast.LENGTH_SHORT).show(); // Main으로 둘 때 잠시만
//                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);  // 애니메이션 제거
    }
}