package com.cu.gastosmerchant1.Data;

public class Transaction_History implements Comparable {
    int Coin_earned,Totalcoin,Coin_spent,Totalspent;
    long timestamp = 0;
    String  Status;
    String Mode;
    String Payment_type;
    String Merchant_uid;
    String user_uid;
    String user_name;
    String data_dump;
    Double amount_paid,actual_amount;
    String tran_ref,merchant_name;

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getTran_ref() {
        return tran_ref;
    }

    public void setTran_ref(String tran_ref) {
        this.tran_ref = tran_ref;
    }

    public Double getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(Double amount_paid) {
        this.amount_paid = amount_paid;
    }

    public Double getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(Double actual_amount) {
        this.actual_amount = actual_amount;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }



    public String getMerchant_uid() {
        return Merchant_uid;
    }

    public void setMerchant_uid(String merchant_uid) {
        Merchant_uid = merchant_uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getData_dump() {
        return data_dump;
    }

    public void setData_dump(String data_dump) {
        this.data_dump = data_dump;
    }



    public String getStatus () {
        return Status;
    }

    public void setStatus ( String status ) {
        Status = status;
    }

    public String getMode () {
        return Mode;
    }

    public void setMode ( String mode ) {
        Mode = mode;
    }

    public String getPayment_type () {
        return Payment_type;
    }

    public void setPayment_type ( String payment_type ) {
        Payment_type = payment_type;
    }

    public int getCoin_earned () {
        return Coin_earned;
    }

    public void setCoin_earned ( int coin_earned ) {
        Coin_earned = coin_earned;
    }

    public int getTotalcoin () {
        return Totalcoin;
    }

    public void setTotalcoin ( int totalcoin ) {
        Totalcoin = totalcoin;
    }

    public int getCoin_spent () {
        return Coin_spent;
    }

    public void setCoin_spent ( int coin_spent ) {
        Coin_spent = coin_spent;
    }

    public int getTotalspent () {
        return Totalspent;
    }

    public void setTotalspent ( int totalspent ) {
        Totalspent = totalspent;
    }

    public long getTimestamp () {
        return timestamp;
    }

    public void setTimestamp ( long timestamp ) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo (Object o) {
        Long time  =((((Transaction_History) o).getTimestamp()) );
//        return  (int)(this.timestamp%10000) -(int)time;
        return time.compareTo(this.getTimestamp());
    }
}