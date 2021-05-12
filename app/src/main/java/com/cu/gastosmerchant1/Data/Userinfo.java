package com.cu.gastosmerchant1.Data;

public class Userinfo {
    String ownername,shopname,phoneno,email,address,shoppic,  Location;
    Account_data data;

    public Account_data getData() {
        return data;
    }

    public void setData(Account_data data) {
        this.data = data;
    }

    public String getOwnername () {
        return ownername;
    }

    public void setOwnername ( String ownername )
    {
        this.ownername = ownername;
    }

    public String getShopname () {
        return shopname;
    }

    public void setShopname ( String shopname ) {
        this.shopname = shopname;
    }

    public String getPhoneno () {
        return phoneno;
    }

    public void setPhoneno ( String phoneno ) {
        this.phoneno = phoneno;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public String getShoppic () {
        return shoppic;
    }

    public void setShoppic ( String shoppic ) {
        this.shoppic = shoppic;
    }


    public String getLocation () {
        return Location;
    }

    public void setLocation ( String location ) {
        Location = location;
    }


}
