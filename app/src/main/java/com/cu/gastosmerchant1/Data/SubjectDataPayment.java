package com.cu.gastosmerchant1.Data;

import android.graphics.Bitmap;

public class SubjectDataPayment {

    String img, name,payment;

    public SubjectDataPayment(String img, String name, String payment) {
        this.img = img;
        this.name = name;
        this.payment = payment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
