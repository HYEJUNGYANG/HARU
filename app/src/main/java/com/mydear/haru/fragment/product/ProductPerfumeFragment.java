package com.mydear.haru.fragment.product;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.mydear.haru.R;

public class ProductPerfumeFragment extends Fragment {

    private TextView tv_recommend;

    private String product_name;

    public ProductPerfumeFragment(String product_name) {
        this.product_name = product_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_perfume, container, false);

        setInitialize(view);
        changeTextColor(tv_recommend, "건성 두피", "#1187CF", false);

        return view;
    }

    private void setInitialize(View view) {
        tv_recommend = view.findViewById(R.id.tv_recommend);
    }

    // textview, 변경하고 싶은 단어, 변경하고자 하는 색, bold체 여부
    public void changeTextColor(TextView tv_str, String w, String color, boolean isBold) {
        String content = tv_str.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = w;
        int start = content.indexOf(word);
        int end = start + word.length();
        // ForegroundColorSpan : 글자 색상 지정
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // StyleSpan: 글자의 스타일 지정 (ex. bold, italic...)
        if (isBold) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tv_str.setText(spannableString); // 바꾼 텍스트 적용
    }
}
