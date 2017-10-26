package com.haybankz.cryptoxchange.utils;

import android.content.ContentResolver;
import android.database.Cursor;

import com.haybankz.cryptoxchange.database.currency.CurrencyContract.CurrencyEntry;
import com.haybankz.cryptoxchange.model.Currency;


import java.util.ArrayList;

/**
 * Created by LENOVO on 10/5/2017.
 */

public class CurrencyUtils {

    public static ArrayList<String> getCryptoCurrencies(ContentResolver contentResolver){

        ArrayList<String> cryptos = new ArrayList<>();

        String[] projection = {CurrencyEntry._ID, CurrencyEntry.COLUMN_CURRENCY_NAME, CurrencyEntry.COLUMN_CURRENCY_CATEGORY};

        String selection = CurrencyEntry.COLUMN_CURRENCY_CATEGORY + "=?";

        String[] selectionArgs = new String[]{String.valueOf(CurrencyEntry.CURRENCY_CRYPTO)};

        Cursor cur = contentResolver.query(CurrencyEntry.CONTENT_URI,projection, selection, selectionArgs, null);

        while(cur.moveToNext()){

            String crypto = cur.getString(cur.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CURRENCY_NAME));
            cryptos.add(crypto);

        }
        cur.close();

        return cryptos;
    }

    public static ArrayList<String> getWorldCurrencies(ContentResolver contentResolver){

        ArrayList<String> worldCur = new ArrayList<>();

        String[] projection = {CurrencyEntry._ID, CurrencyEntry.COLUMN_CURRENCY_NAME, CurrencyEntry.COLUMN_CURRENCY_CATEGORY};

        String selection = CurrencyEntry.COLUMN_CURRENCY_CATEGORY + "=?";
        String[] selectionArgs = {String.valueOf(CurrencyEntry.CURRENCY_WORLD)};
        String sortOrder = CurrencyEntry.COLUMN_CURRENCY_NAME + " ASC";

        Cursor cur = contentResolver.query(CurrencyEntry.CONTENT_URI,projection, selection, selectionArgs, sortOrder);

        while(cur.moveToNext()){

            String world = cur.getString(cur.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CURRENCY_NAME));
            worldCur.add(world);

        }

        cur.close();
        return worldCur;
    }


    public static ArrayList<Currency> getAllCurrencies(ContentResolver contentResolver){

        ArrayList<Currency> currencies = new ArrayList<>();



        String[] projection = {CurrencyEntry._ID, CurrencyEntry.COLUMN_CURRENCY_NAME,
                CurrencyEntry.COLUMN_CURRENCY_FULLNAME,
                CurrencyEntry.COLUMN_CURRENCY_CATEGORY};

        String sortOrder = CurrencyEntry.COLUMN_CURRENCY_CATEGORY + " ASC";

        Cursor cur = contentResolver.query(CurrencyEntry.CONTENT_URI,projection, null, null, sortOrder);

        while(cur.moveToNext()){


            int id = cur.getInt(cur.getColumnIndexOrThrow(CurrencyEntry._ID));
            String name = cur.getString(cur.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CURRENCY_NAME));
            String fullname = cur.getString(cur.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CURRENCY_FULLNAME));
            int category = cur.getInt(cur.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CURRENCY_CATEGORY));

            Currency currency = new Currency(id, name, fullname, category);

            currencies.add(currency);

        }

        cur.close();

        return currencies;
    }
}
