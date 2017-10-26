package com.haybankz.cryptoxchange.database.exchange;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class ExchangeContract {

    private ExchangeContract(){}

    public static final String PATH_EXCHANGE = "exchange";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.haybankz.cryptoxchange.exchange");


    public static final class ExchangeEntry implements BaseColumns {

        public final static String TABLE_NAME = "exchange";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EXCHANGE_CRYPTO = "crypto_currency";
        public final static String COLUMN_EXCHANGE_WORLD = "world_currency";



        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXCHANGE);


    }

}
