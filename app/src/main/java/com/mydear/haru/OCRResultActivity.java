package com.mydear.haru;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class OCRResultActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_product_name;
    private TextView tv_ingredient;
    private ImageView iv_product;

    private Button btn_re;

    private String product_name;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrresult);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 기본 title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_home);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // firebase storage
        storage = FirebaseStorage.getInstance("gs://mydear-bb10e.appspot.com/");
        storageRef = storage.getReference();

        iv_product = findViewById(R.id.iv_product);
        tv_title = findViewById(R.id.tv_title);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_ingredient = findViewById(R.id.tv_ingredient);
        String str_title = tv_title.getText().toString();

        // 텍스트 색상 변경
        SpannableStringBuilder spannable1 = new SpannableStringBuilder(str_title);
        spannable1.setSpan(
                new ForegroundColorSpan(Color.RED),
                2,
                6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_title.setText(spannable1);

        Intent getProductNameIntent = getIntent();
        product_name = getProductNameIntent.getStringExtra("name");
        getProductInfo(product_name);

        btn_re = findViewById(R.id.btn_re);
        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OCRResultActivity.this, OCRActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finishAffinity();
            }
        });
    }

    // firebase storage img 불러오기
    public void getProductInfo(String folderName) {
        StorageReference productPathRef = storageRef.child("product/" + folderName + "/product.png");

        // success
        productPathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 이미지 설정
                Glide.with(getApplicationContext()).load(uri).into(iv_product);
            }
            // fail
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OCRResultActivity.this, "이미지가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "실패: ", e);
            }
        });

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
                    String name = JSONParser.getJsonObject(product, "name");
                    if (name.equals(product_name)) {
                        String brand = JSONParser.getJsonObject(product, "brand");
                        String ingredients = JSONParser.getJsonObject(product, "ingredients").replace("·", ",");

                        tv_product_name.setText(brand + " " + name);
                        tv_ingredient.setText(ingredients);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OCRResultActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finishAffinity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 막기
//        super.onBackPressed();
    }
}