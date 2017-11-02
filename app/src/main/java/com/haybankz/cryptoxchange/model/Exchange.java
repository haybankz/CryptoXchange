package com.haybankz.cryptoxchange.model;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class Exchange {

    private int id;
    private String cryptoCurrency;
    private String worldCurrency;
    private double rate;

    public Exchange(){

    }

    public Exchange(int id,String cryptoCurrency, String worldCurrency, double rate) {
        this.id = id;
        this.cryptoCurrency = cryptoCurrency;
        this.worldCurrency = worldCurrency;
        this.rate = rate;
    }

    public Exchange(int id, String cryptoCurrency, String worldCurrency) {
        this.id = id;
        this.cryptoCurrency = cryptoCurrency;
        this.worldCurrency = worldCurrency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getWorldCurrency() {
        return worldCurrency;
    }

    public void setWorldCurrency(String worldCurrency) {
        this.worldCurrency = worldCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
