package com.haybankz.cryptoxchange.database.currency;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class CurrencyContract {

    private CurrencyContract(){}

    public static final String PATH_CURRENCIES = "currencies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.haybankz.cryptoxchange.currency");

    public static final class CurrencyEntry implements BaseColumns {

        public final static String TABLE_NAME = "currencies";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CURRENCY_NAME = "name";
        public final static String COLUMN_CURRENCY_FULLNAME = "fullname";


        public final static String COLUMN_CURRENCY_CATEGORY = "category";




        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CURRENCIES);


        public final static String STRING_CRYPTO = "crypto currency";
        public final static String STRING_WORLD = "world currency";

        public final static int CURRENCY_CRYPTO = 1;
        public final static int CURRENCY_WORLD = 2;

    }
}
