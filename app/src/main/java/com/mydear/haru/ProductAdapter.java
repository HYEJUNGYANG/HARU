package com.mydear.haru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private Context mContext;

    private int index;
    private ArrayList<Product> list;

    private OnClickListener clickListener;

    public interface OnClickListener{ // 인터페이스 정의
        void onClick(int index, String name, String brand, String imageURL, String detailURL);
    }

    public ProductAdapter() {
        list = new ArrayList<Product>();
        mContext = null;
    }

    public ProductAdapter(Context context) {
        list = new ArrayList<Product>();
        this.mContext = context;
    }

    public void addProduct(String name, String brand, String imageURL, String detailURL, OnClickListener clickListener) {
        Product product = new Product(name, brand, imageURL, detailURL);
        list.add(product);
        this.clickListener = clickListener;
    }

    public void emptyList() {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            convertView = inflater.inflate(R.layout.list_search_product, parent, false);
        }

        TextView txtTextName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView txtTextBrand = (TextView) convertView.findViewById(R.id.tv_brand);
        ImageView iv_imageURL = (ImageView) convertView.findViewById(R.id.iv_imageURL);

        Product product = list.get(position);

        txtTextName.setText(product.getTv_product_name());
        txtTextBrand.setText(product.getTv_brand());

        loadFireBaseImage(product.getImageURL(), iv_imageURL);

        txtTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position, product.getTv_product_name(), product.getTv_brand(), product.getImageURL(), product.getDetailURL());
            }
        });

        txtTextBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position, product.getTv_product_name(), product.getTv_brand(), product.getImageURL(), product.getDetailURL());
            }
        });

        iv_imageURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(position, product.getTv_product_name(), product.getTv_brand(), product.getImageURL(), product.getDetailURL());
            }
        });

        return convertView;
    }

    private void loadFireBaseImage(String imgFileName, ImageView view) {
        Glide.with((SearchActivity)mContext).load(imgFileName).into(view);
    }
}
