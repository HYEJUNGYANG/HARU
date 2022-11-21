package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class OCRActivity extends AppCompatActivity {

    private LinearLayout layout_ex;
    private ConstraintLayout layout_crop;
    private ConstraintLayout layout_result;

    private TextView tv_description;
    private TextView tv_description2;
    private TextView tv_notice;
    private Button btn_camera;
    private Button btn_re;

    private ImageView iv_crop;

    private File file;

    private Uri photoUri;

    private static final int REQUEST_IMAGE_CODE = 101;
    private static final int CROP_FROM_CAMERA = 3;  // 가져온 사진을 자르기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocractivity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 기본 title 제거
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.resize_icon_back);

        layout_ex = findViewById(R.id.layout_ex);
        layout_crop = findViewById(R.id.layout_crop);
        layout_result = findViewById(R.id.layout_result);

        iv_crop = findViewById(R.id.iv_crop);

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

        tv_description = findViewById(R.id.tv_description);
        String str_description = tv_description.getText().toString();
        tv_description2 = findViewById(R.id.tv_description2);
        String str_description2 = tv_description2.getText().toString();

        // 텍스트 색상 변경
        SpannableStringBuilder spannable1 = new SpannableStringBuilder(str_description);
        spannable1.setSpan(
                new ForegroundColorSpan(Color.RED),
                25,
                31,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        spannable1.setSpan(
                new ForegroundColorSpan(Color.RED),
                43,
                61,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_description.setText(spannable1);

        SpannableStringBuilder spannable2 = new SpannableStringBuilder(str_description2);
        spannable2.setSpan(
                new ForegroundColorSpan(Color.RED),
                0,
                7,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        spannable2.setSpan(
                new ForegroundColorSpan(Color.RED),
                36,
                50,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_description2.setText(spannable2);

        tv_notice = findViewById(R.id.tv_notice);
        String str_notice = tv_notice.getText().toString();

        SpannableStringBuilder spannable3 = new SpannableStringBuilder(str_notice);
        spannable3.setSpan(
                new ForegroundColorSpan(Color.RED),
                12,
                19,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        );
        tv_notice.setText(spannable3);


        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OCRActivity.this, "촬영 버튼 클릭! ✨", Toast.LENGTH_SHORT).show();
                takePicture();
            }
        });
        btn_re = findViewById(R.id.btn_re);
        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        iv_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(photoUri, "image/*");

                // 임시로 사용할 파일의 경로를 생성
                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                intent.putExtra("outputX", 90);
//                intent.putExtra("outputY", 90);
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1);
//                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                // 임시로 사용할 파일의 경로를 생성
//                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//                photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                cameraIntent.putExtra("return-data", true);
//                startActivityForResult(cameraIntent, CROP_FROM_CAMERA);
            }
        });
    }

    // 사진찍기
    public void takePicture() {
        int permissionCheck = ContextCompat.checkSelfPermission(OCRActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(OCRActivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_CODE);
        } else {
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();  // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");  // 이미지뷰에 Bitmap으로 이미지를 입력

            iv_crop.setImageBitmap(imageBitmap);
            layout_ex.setVisibility(View.GONE);
            layout_crop.setVisibility(View.VISIBLE);

//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(photoUri, "image/*");
//
//            intent.putExtra("outputX", 90);
//            intent.putExtra("outputY", 90);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("scale", true);
//            intent.putExtra("return-data", true);
//            startActivityForResult(intent, CROP_FROM_CAMERA);
        }
        else if (requestCode == CROP_FROM_CAMERA) {
            // 크롭이 된 이후의 이미지를 넘겨 받습니다.
            // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
            // 임시 파일을 삭제합니다.
            final Bundle extras = data.getExtras();

            if(extras != null)
            {
                Bitmap photo = extras.getParcelable("data");
                iv_crop.setImageBitmap(photo);
            }

            // 임시 파일 삭제
            File f = new File(photoUri.getPath());
            if(f.exists())
            {
                f.delete();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 이후 뒤로가기 한번 눌렀을 때 지금까지 진행한 결과 사라진다고 팝업창 띄우고 진짜 뒤로 갈 것인지 묻고 그에 따른 결과 반영하도록 설정하기
                Toast.makeText(this, "BACK 버튼 클릭! ✨", Toast.LENGTH_SHORT).show();
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
    }
}