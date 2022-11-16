package com.mydear.haru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;
    private String productName;  // 상품 이름 받아옴

    public ProductAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
//        this.productName = productName;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_product, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // 매칭시켜줌
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getIv_product())
                .into(holder.iv_imageURL);
        // Image 관련 코드 나중에 꼭 넣기
        holder.tv_brand.setText(arrayList.get(position).getTv_brand());
        holder.tv_name.setText(arrayList.get(position).getTv_product_name());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_imageURL;
        TextView tv_brand;
        TextView tv_name;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_imageURL = itemView.findViewById(R.id.iv_imageURL);
            this.tv_brand = itemView.findViewById(R.id.tv_brand);
            this.tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
