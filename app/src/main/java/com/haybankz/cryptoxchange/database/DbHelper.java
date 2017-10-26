package com.haybankz.cryptoxchange.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.haybankz.cryptoxchange.database.currency.CurrencyContract.CurrencyEntry;
import com.haybankz.cryptoxchange.database.exchange.ExchangeContract.ExchangeEntry;
//import com.haybankz.cryptoxchange.model.Currency;
//import com.haybankz.cryptoxchange.model.Exchange;


/**
 * Created by LENOVO on 10/3/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cryptoxchange.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    //SQL statement to create currencies table
    private static final String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE "+ CurrencyEntry.TABLE_NAME + " (" +
            CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CurrencyEntry.COLUMN_CURRENCY_NAME + TEXT_TYPE +" NOT NULL"+ COMMA_SEP +
            CurrencyEntry.COLUMN_CURRENCY_FULLNAME + TEXT_TYPE +" NOT NULL"+ COMMA_SEP +
            CurrencyEntry.COLUMN_CURRENCY_CATEGORY + " INTEGER NOT NULL" +

            " )";


    //SQL statement to create exchange table
    private static final String SQL_CREATE_EXCHANGE_TABLE = "CREATE TABLE "+ ExchangeEntry.TABLE_NAME + " (" +
            ExchangeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ExchangeEntry.COLUMN_EXCHANGE_CRYPTO + TEXT_TYPE +" NOT NULL"+ COMMA_SEP +
            ExchangeEntry.COLUMN_EXCHANGE_WORLD + TEXT_TYPE +" NOT NULL" +

            " )";



    //SQL statement to delete currencies and exchange tables respectively
    private static final String SQL_DELETE_CURRENCY_ENTRIES = "DROP TABLE IF EXIST "+ CurrencyEntry.TABLE_NAME;
    private static final String SQL_DELETE_EXCHANGE_ENTRIES = "DROP TABLE IF EXIST "+ ExchangeEntry.TABLE_NAME;



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CURRENCY_TABLE);
        db.execSQL(SQL_CREATE_EXCHANGE_TABLE);

        insertCurrencies(db);
//        insertExchange(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CURRENCY_ENTRIES);
        db.execSQL(SQL_DELETE_EXCHANGE_ENTRIES);

        onCreate(db);
    }




    /*Populate currencies table with both 2 cryptocurrencies(i.e BTC and ETH)
     * and 20 major world currencies
      */
    public void insertCurrencies(SQLiteDatabase db){

        ContentValues values =  new ContentValues();
        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "BTC");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Bitcoin");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_CRYPTO);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "ETH");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Ethereum");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_CRYPTO);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

//        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "TBC");
//        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "The Billion coin");
//        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_CRYPTO);
//        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "NGN");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Nigerian Naira");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
         db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "USD");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "US Dollar");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "JPY");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Japanese Yen");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "GBP");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "British Pound");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "CHF");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Swiss Franc");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "CAD");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Canadian Dollar");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "AUD");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Australian Dollar");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "NZD");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "New Zealand Dollar");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "ZAR");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "South African Rand");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "EUR");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Euro");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "INR");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Indian Ruppee");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "AED");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "UAE Dirham");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "GHS");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Ghana Cedi");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "KES");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Kenya shilling");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "KWD");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Kuwait Dinar");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "SAR");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Saudi Arabian Riyal");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);

        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "SCR");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Seychellois Rupee");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "XOF");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "West Africa CFA franc");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "UGX");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Ugandan shilling");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);


        values.put(CurrencyEntry.COLUMN_CURRENCY_NAME, "BRL");
        values.put(CurrencyEntry.COLUMN_CURRENCY_FULLNAME, "Brazilian Real");
        values.put(CurrencyEntry.COLUMN_CURRENCY_CATEGORY, CurrencyEntry.CURRENCY_WORLD);
        db.insert(CurrencyEntry.TABLE_NAME, null, values);
    }


    /*Populate exchange with one exchange(BTC and NGN) */
    public void insertExchange(SQLiteDatabase db){

        ContentValues values = new ContentValues();

        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "NGN");
        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "BTC");
        db.insert(ExchangeEntry.TABLE_NAME, null, values);

//        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "USD");
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "ETH");
//        db.insert(ExchangeEntry.TABLE_NAME, null, values);

//        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "NGN");
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "ETH");
//        db.insert(ExchangeEntry.TABLE_NAME, null, values);

//        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "EUR");
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "ETH");
//        db.insert(ExchangeEntry.TABLE_NAME, null, values);
//
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "NGN");
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "BTC");
//        db.insert(ExchangeEntry.TABLE_NAME, null, values);
//
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, "EUR");
//        values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, "BTC");
//        db.insert(ExchangeEntry.TABLE_NAME, null, values);

    }

}
