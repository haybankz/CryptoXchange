package com.haybankz.cryptoxchange.model;
import com.haybankz.cryptoxchange.database.currency.CurrencyContract.CurrencyEntry;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class Currency {

    private int id;
    private String name;
    private String fullname;
    private String category;




    public Currency(int id, String name, String fullname, int currencyType) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
       switch(currencyType){

           case CurrencyEntry.CURRENCY_CRYPTO:
               category = CurrencyEntry.STRING_CRYPTO;
               break;
           case
           CurrencyEntry.CURRENCY_WORLD:
               category = CurrencyEntry.STRING_WORLD;
               break;

           default:
               break;


       }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
