package com.haybankz.cryptoxchange.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.haybankz.cryptoxchange.database.exchange.ExchangeContract.ExchangeEntry;
import com.haybankz.cryptoxchange.model.Exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class ExchangeUtils {

    private static ContentResolver mContentResolver;


    public static ArrayList<Exchange> getExchangeListFromDb(ContentResolver contentResolver) {

        mContentResolver = contentResolver;

        ArrayList<Exchange> exchanges = new ArrayList<>();

        String[] projection = {ExchangeEntry._ID, ExchangeEntry.COLUMN_EXCHANGE_CRYPTO,
                ExchangeEntry.COLUMN_EXCHANGE_WORLD, ExchangeEntry.COLUMN_EXCHANGE_RATE};


        Cursor c = contentResolver.query(ExchangeEntry.CONTENT_URI, projection, null, null, null);

        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndexOrThrow(ExchangeEntry._ID));
            String crypto = c.getString(c.getColumnIndexOrThrow(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO));
            String world = c.getString(c.getColumnIndexOrThrow(ExchangeEntry.COLUMN_EXCHANGE_WORLD));
            double rate = c.getDouble(c.getColumnIndexOrThrow(ExchangeEntry.COLUMN_EXCHANGE_RATE));

            Exchange exchange = new Exchange(id, crypto, world, rate);
            exchanges.add(exchange);

        }

        c.close();
        return exchanges;
    }


    public static ArrayList<Exchange> getRateFromMap(List<Exchange> exc, Map<String, Map<String, Double>> exchangeRateMap) {
        ArrayList<Exchange> exchanges = new ArrayList<>();


        for (Exchange e : exc) {
            Exchange exchange = searchMapForRate(e, exchangeRateMap);
            exchanges.add(exchange);
        }
        return exchanges;
    }


    public static Exchange searchMapForRate(Exchange exchange, Map<String, Map<String, Double>> exchangeRateMap) {

        Map<String, Double> rateMap = exchangeRateMap.get(exchange.getCryptoCurrency());

        if (rateMap != null) {
            double rate = rateMap.get(exchange.getWorldCurrency());
            exchange.setRate(rate);
            return exchange;
        } else {
            return null;
        }
    }


    public static ArrayList<Exchange> getExchangeRates(Context context) {

        List<Exchange> oldExchangeRate = getExchangeListFromDb(context.getContentResolver());

        if (oldExchangeRate.size() > 0) {

            ArrayList<String> cryptos = CurrencyUtils.getCryptoCurrencies(context.getContentResolver());

            ArrayList<String> worldCur = CurrencyUtils.getWorldCurrencies(context.getContentResolver());

            Map<String, Map<String, Double>> exchangeRateMap = ApiUtils.getExchangeFromApi(cryptos, worldCur);


            if (exchangeRateMap.size() > 0) {


                ArrayList<Exchange> newExchangeRate = getRateFromMap(oldExchangeRate, exchangeRateMap);

//                updateExchangeRates(newExchangeRate);

                return newExchangeRate;

            } else {

                return null;
            }


        }
        return new ArrayList<>();
    }


//    public static void updateExchangeRate(ArrayList<Exchange> exchanges ){

        public static void updateExchangeRate(Exchange exchange ){

//        for(Exchange exchange : exchanges){

            ContentValues value = new ContentValues();
            value.put(ExchangeEntry.COLUMN_EXCHANGE_RATE, exchange.getRate());

            String selections = ExchangeEntry.COLUMN_EXCHANGE_CRYPTO + "=? and " + ExchangeEntry.COLUMN_EXCHANGE_WORLD + "=? ";
            String[] selectionArgs = {exchange.getCryptoCurrency(), exchange.getWorldCurrency()};


            mContentResolver.update(ExchangeEntry.CONTENT_URI, value, selections, selectionArgs);


//        }


    }


}
