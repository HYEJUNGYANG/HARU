package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductInfoActivity extends AppCompatActivity {

//    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference productRef;

    private Button btn_product_detail;
    private Button btn_product_ingredient;
    private Button btn_product_perfume;

    private ImageView iv_product;
    private ImageView iv_product_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // toolbar 왼쪽 버튼
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_back);

        // firebase storage
//        storage = FirebaseStorage.getInstance();
        storageRef = FirebaseStorage.getInstance("gs://mydear-bb10e.appspot.com/")
                .getReference();
        productRef = storageRef.child("product");

        iv_product = findViewById(R.id.iv_product);
        iv_product_detail = findViewById(R.id.iv_produce_detail);

        btn_product_detail = findViewById(R.id.btn_product_detail);
        btn_product_ingredient = findViewById(R.id.btn_product_ingredient);
        btn_product_perfume = findViewById(R.id.btn_product_perfume);

        getImage("허니");
    }

    // firebase storage img 불러오기
    public void getImage(String folderName) {
        StorageReference productPathRef = storageRef.child("product/" + folderName + "/product.png");
        StorageReference detailPathRef = storageRef.child("product/" + folderName + "/product.png");
//        Glide.with(this).load(pathRef).into(iv_product);
        productPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(iv_product);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductInfoActivity.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        detailPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(iv_product_detail);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductInfoActivity.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
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