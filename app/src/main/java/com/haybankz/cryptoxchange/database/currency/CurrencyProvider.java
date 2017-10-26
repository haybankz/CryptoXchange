package com.haybankz.cryptoxchange.database.currency;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.haybankz.cryptoxchange.database.DbHelper;


/**
 * Created by LENOVO on 10/5/2017.
 */

public class CurrencyProvider extends ContentProvider {

    private DbHelper dbHelper;

    private static final int CURRENCY = 1000;
    private static  final int CURRENCY_ID = 1001;
    private static final String CURRENCY_AUTHORITY = "com.haybankz.cryptoxchange.currency";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        sUriMatcher.addURI(CURRENCY_AUTHORITY, CurrencyContract.PATH_CURRENCIES, CURRENCY);


        sUriMatcher.addURI(CURRENCY_AUTHORITY, CurrencyContract.PATH_CURRENCIES + "/#", CURRENCY_ID);



    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match){

            case CURRENCY:
                cursor = database.query(CurrencyContract.CurrencyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case CURRENCY_ID:
                selection = CurrencyContract.CurrencyEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(CurrencyContract.CurrencyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown uri: "+uri);


        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert( Uri uri,  ContentValues values) {

        int match = sUriMatcher.match(uri);
        switch(match){
            case CURRENCY:
                getContext().getContentResolver().notifyChange(uri, null);
                return insertExchange(uri, values);

            default:
                throw  new IllegalArgumentException("Insertion is not supported for: "+uri);
        }

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int result;
        switch (match){
            case CURRENCY:
                result = database.delete(CurrencyContract.CurrencyEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case CURRENCY_ID:
                selection = CurrencyContract.CurrencyEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = database.delete(CurrencyContract.CurrencyEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            default:
                throw new IllegalArgumentException("Deleting is not supported for "+uri);
        }


    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int result;

        switch(match){
            case CURRENCY:
                result = database.update(CurrencyContract.CurrencyEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case CURRENCY_ID:
                selection = CurrencyContract.CurrencyEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                result = database.update(CurrencyContract.CurrencyEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            default:
                throw new IllegalArgumentException("Editing is not supported for :"+uri);

        }

    }

    private Uri insertExchange(Uri uri, ContentValues values){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long newRowId = database.insert(CurrencyContract.CurrencyEntry.TABLE_NAME, null, values);
        if(newRowId == -1){
            Toast.makeText(getContext(), "Failed to insert row for: " + uri, Toast.LENGTH_LONG).show();
            return null;
        }

        String id = String.valueOf(newRowId);

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(uri, id);
    }

}
