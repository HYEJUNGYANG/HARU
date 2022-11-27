package com.mydear.haru;

public class Product implements Cloneable {
    private String iv_product;
    private String imageURL;
    private String detailURL;
    private String tv_brand;
    private String tv_product_name;

    public Product() {
        setName(null);
        setBrand(null);
        setImageURL(null);
    }

    public Product(String name, String brand, String imageURL, String detailURL) {
        setName(name);
        setBrand(brand);
        setImageURL(imageURL);
        setDetailURL(detailURL);
    }

    public String getIv_product() {
        return iv_product;
    }

    public void setName(String name) {
        this.tv_product_name = name;
    }

    public void setBrand(String brand) {
        this.tv_brand = brand;
    }

    public void setImageURL(String url) {
        this.imageURL = url;
    }

    public void setDetailURL(String url) {
        this.detailURL = url;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public String getDetailURL() { return this.detailURL; }

    public void setIv_product(String iv_product) {
        this.iv_product = iv_product;
    }

    public String getTv_brand() {
        return tv_brand;
    }

    public void setTv_brand(String tv_brand) {
        this.tv_brand = tv_brand;
    }

    public String getTv_product_name() {
        return tv_product_name;
    }

    public void setTv_product_name(String tv_product_name) {
        this.tv_product_name = tv_product_name;
    }
}
