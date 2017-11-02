package com.haybankz.cryptoxchange;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.haybankz.cryptoxchange.model.Exchange;
import com.haybankz.cryptoxchange.utils.ExchangeUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class ExchangeLoader extends AsyncTaskLoader<ArrayList<Exchange>> {


    private String mUrl;
    private List<Exchange> mExchanges;
    Context mContext;

    public ExchangeLoader(Context context, String url) {
        super(context);
        mContext = context;

        mUrl = url;
    }

    public ExchangeLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public ArrayList<Exchange> loadInBackground() {

        return ExchangeUtils.getExchangeRates(mContext);
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }






}
