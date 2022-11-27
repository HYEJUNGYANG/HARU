package com.mydear.haru.fragment.search;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mydear.haru.Product;
import com.mydear.haru.ProductAdapter;
import com.mydear.haru.R;

import java.util.ArrayList;

public class SearchProductResultFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> arrayList;
    private DatabaseReference mDatabase;

    private boolean isBrand = false;
    private boolean isName = false;
    private boolean isURL = false;
    private boolean isAll = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product_result, container, false);

        setInitialize(view);
        getData(view);

        return view;
    }

    private void setInitialize(View view) {
        // 초기값 세팅
        recyclerView = view.findViewById(R.id.layout_search_product);
        recyclerView.setHasFixedSize(true);  // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void getData(View view) {
//        mDatabase.child("Database").child("MyDear").child("products").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Firebase DB 데이터를 받아오는 곳
//                arrayList.clear();
//                for (DataSnapshot snap: snapshot.getChildren()) {
//                    Product product = snap.getValue(Product.class);
//                    Log.e(TAG, "product에 뭐가 들었을까요" + product.getIv_product());
//                    arrayList.add(product);
//                }
//                adapter.notifyDataSetChanged();  // 리스트 저장 및 새로고침
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // DB 가져오던 중 에러 발생 시
//                Log.e(TAG, "에러: ", error.toException());
//            }
//        });


        arrayList.clear();
        for (int i=0; i<10; i++) {
            Product product = new Product();
            mDatabase.child("Database").child("MyDear").child("products").child("0").child("brand").get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
//                        q1.setText(task.getResult().getValue().toString());
                            Log.e(TAG, "값 확인용 " + task.getResult().getValue());
                            product.setTv_brand(task.getResult().getValue().toString());

//                        arrayList.add(product);
//                        Log.e(TAG, "product값" + product.getIv_product() );
                        }
                    });
        }

//        adapter = new ProductAdapter(arrayList, view.getContext());
        recyclerView.setAdapter(adapter);
    }

    public boolean callback(Product product) {
        isAll = isBrand && isName && isURL;
        if (isAll) {
            arrayList.add(product);
            adapter.notifyDataSetChanged();  // 리스트 저장 및 새로고침
            return true;
        }
        return false;
    }
}
