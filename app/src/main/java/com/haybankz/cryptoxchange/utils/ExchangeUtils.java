package com.haybankz.cryptoxchange.utils;

import android.content.ContentResolver;
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


    public static List<Exchange> getExchangeListFromDb(ContentResolver contentResolver) {

        List<Exchange> exchanges = new ArrayList<>();

        String[] projection = {ExchangeEntry._ID, ExchangeEntry.COLUMN_EXCHANGE_CRYPTO,
                ExchangeEntry.COLUMN_EXCHANGE_WORLD};


        Cursor c = contentResolver.query(ExchangeEntry.CONTENT_URI, projection, null, null, null);

        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndexOrThrow(ExchangeEntry._ID));
            String crypto = c.getString(c.getColumnIndexOrThrow(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO));
            String world = c.getString(c.getColumnIndexOrThrow(ExchangeEntry.COLUMN_EXCHANGE_WORLD));

            Exchange exchange = new Exchange(id, crypto, world);
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


    public static ArrayList<Exchange> getExchangeListWithRate(Context context) {

        List<Exchange> rateLessExchange = getExchangeListFromDb(context.getContentResolver());

        if (rateLessExchange.size() > 0) {

            ArrayList<String> cryptos = CurrencyUtils.getCryptoCurrencies(context.getContentResolver());

            ArrayList<String> worldCur = CurrencyUtils.getWorldCurrencies(context.getContentResolver());

            Map<String, Map<String, Double>> exchangeRateMap = ApiUtils.getExchangeFromApi(cryptos, worldCur);


            if (exchangeRateMap.size() > 0) {


                ArrayList<Exchange> exchangesWithRate = getRateFromMap(rateLessExchange, exchangeRateMap);


                return exchangesWithRate;

            } else {

                return null;
            }


        }
        return new ArrayList<>();
    }


}
