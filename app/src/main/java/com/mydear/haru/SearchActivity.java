package com.mydear.haru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText et_searchBar;
    Button btn_search;
    TextView tv_result;
    public DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_searchBar = (EditText)findViewById(R.id.et_searchBar);
        tv_result = (TextView)findViewById(R.id.tv_result);

        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = et_searchBar.getText().toString();

                search(keyword);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
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

                updateResult(res);
            }
        });
    }

    public void updateResult(ArrayList<String> res) {
        tv_result.setText("");

        String temp = "";
        for(int i = 0; i < res.size(); i++) {
            temp += res.get(i);
            temp += "\n";
        }

        tv_result.setText(temp);
    }
}
