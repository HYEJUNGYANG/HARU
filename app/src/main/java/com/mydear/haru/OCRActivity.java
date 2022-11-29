package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OCRActivity extends AppCompatActivity {

    private LinearLayout layout_ex;
    private ConstraintLayout layout_crop;
    private ConstraintLayout layout_result;

    private TextView tv_description;
    private TextView tv_description2;
    private TextView tv_notice;
    private TextView tv_txt_result;
    private TextView tv_ocr;
    private EditText et_write;
    private Button btn_camera;
    private Button btn_re;
    private Button btn_submit;

    private ImageView iv_crop;
    private ImageView iv_cropped;
    private ImageView iv_result;

    private Bitmap orgBitmap = null;
    private Bitmap croppedBitmap = null;
    private Point startPos = null;
    private Point endPos = null;

    private TessBaseAPI mTess;
    private String mCurrentPhotoPath;
    private String datapath = "" ; //언어데이터가 있는 경로
    private String lang = "kor";

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
        iv_cropped = findViewById(R.id.iv_cropped);
        iv_result = findViewById(R.id.iv_result);

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

        tv_description = findViewById(R.id.tv_description);
        String str_description = tv_description.getText().toString();
        tv_description2 = findViewById(R.id.tv_description2);
        String str_description2 = tv_description2.getText().toString();
        tv_txt_result = findViewById(R.id.tv_txt_result);
        tv_ocr = findViewById(R.id.tv_ocr);

        et_write = findViewById(R.id.et_write);

        datapath = getFilesDir() + "/tesseract/";

        checkFile(new File(datapath + "tessdata/"));

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

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
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(croppedBitmap == null) {
                    return;
                }

                mTess.setImage(croppedBitmap);
                String OCRresult = mTess.getUTF8Text();
                tv_txt_result.setText(OCRresult);
                et_write.setText(OCRresult);

                layout_crop.setVisibility(View.GONE);
                layout_result.setVisibility(View.VISIBLE);

//                mTess.setImage(croppedBitmap);
//                String OCRresult = mTess.getUTF8Text();
//
//                tv_ocr.setText(OCRresult);
            }
        });

        iv_crop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Point x = null;
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        croppedBitmap = null;
                        x = toViewRawXY(iv_crop);

                        startPos = new Point((int)event.getRawX(), (int)event.getRawY() - x.y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        x = toViewRawXY(iv_crop);

                        endPos = new Point((int)event.getRawX(), (int)event.getRawY() - x.y);
                        Log.e("startPos", String.valueOf(startPos.x) + ", " + String.valueOf(startPos.y));
                        Log.e("endPos", String.valueOf(endPos.x) + ", " + String.valueOf(endPos.y));

                        if(startPos == null) {
                            break;
                        }

                        if(endPos.x <= startPos.x || endPos.y <= startPos.y) {
                            break;
                        }

                        if(orgBitmap == null) {
                            break;
                        }

                        croppedBitmap = CropBitmap(orgBitmap, startPos, endPos);

                        iv_cropped.setImageBitmap(croppedBitmap);
                        iv_result.setImageBitmap(croppedBitmap);
                        iv_cropped.setVisibility(View.VISIBLE);
                        iv_crop.setVisibility(View.GONE);
                        break;
                }

                return true;
            }
        });
    }

    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            // ㅇㅁㄹㅁㅇㄴㄹ
            mCurrentPhotoPath = datafile.getAbsolutePath();
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Point toViewRawXY(View view){
        View parentView = view.getRootView();
        int sumX = 0;
        int sumY = 0;

        boolean chk = false;
        while (!chk) {
            sumX = sumX + view.getLeft();
            sumY = sumY + view.getTop();

            view = (View) view.getParent();
            if(parentView == view){
                chk = true;
            }
        }

        return new Point(sumX, sumY);
    }

    private Bitmap CropBitmap(Bitmap orgBitmap, Point startPos, Point endPos) {
        Point start = new Point(
                (int)((float)startPos.x / iv_crop.getWidth() * orgBitmap.getWidth()),
                (int)((float)startPos.y / iv_crop.getHeight() * orgBitmap.getHeight()));
        Point end = new Point(
                (int)((float)endPos.x / iv_crop.getWidth() * orgBitmap.getWidth()),
                (int)((float)endPos.y / iv_crop.getHeight() * orgBitmap.getHeight()));

        int croppedWidth = end.x - start.x < orgBitmap.getWidth() / 4 ? orgBitmap.getWidth() / 4 : end.x - start.x;
        int croppedHeight = end.y - start.y < orgBitmap.getHeight() / 4 ? orgBitmap.getHeight() / 4 : end.y - start.y;

        croppedWidth = start.x + croppedWidth > orgBitmap.getWidth() ? orgBitmap.getWidth() - start.x : croppedWidth;
        croppedHeight = start.y + croppedHeight > orgBitmap.getHeight() ? orgBitmap.getHeight() - start.y : croppedHeight;

        Bitmap res = Bitmap.createBitmap(orgBitmap, start.x, start.y, croppedWidth, croppedHeight);

        return res;
    }

    // 사진찍기
    public void takePicture() {
        int permissionCheck = ContextCompat.checkSelfPermission(OCRActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(OCRActivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_CODE);

        } else {
            iv_crop.setVisibility(View.VISIBLE);
            iv_cropped.setVisibility(View.GONE);
            tv_ocr.setText("");
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

            orgBitmap = imageBitmap;
            iv_crop.setImageBitmap(imageBitmap);


            layout_ex.setVisibility(View.GONE);
            layout_crop.setVisibility(View.VISIBLE);
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