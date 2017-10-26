package com.haybankz.cryptoxchange.ui;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.haybankz.cryptoxchange.ClickListener;
import com.haybankz.cryptoxchange.ExchangeLoader;
import com.haybankz.cryptoxchange.ExchangeRecyclerAdapter;
import com.haybankz.cryptoxchange.R;
import com.haybankz.cryptoxchange.RecyclerTouchListener;
import com.haybankz.cryptoxchange.model.Exchange;
import com.haybankz.cryptoxchange.utils.CurrencyUtils;
import com.haybankz.cryptoxchange.database.exchange.ExchangeContract.ExchangeEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Exchange>> {

//    CoordinatorLayout coordinateLayout;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView exchangeRecyclerView;
    ExchangeRecyclerAdapter exchangeRecycleradapter;


    View emptyView;
    View progressBar;
    View statusTextView;
    View noExchangeView;


    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    Context context;
//    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        getSupportActionBar().setIcon(R.drawable.ic_title);


        //Instantiate widgets, layout, exchangeRecycleradapter and context
        fab = (FloatingActionButton) findViewById(R.id.fab);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        exchangeRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar =  findViewById(R.id.progressBar);
        statusTextView = (View) findViewById(R.id.status_view);
        emptyView =  (View) findViewById(R.id.empty_view);
        noExchangeView = (View) findViewById(R.id.no_exchange_view);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2&3);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL|StaggeredGridLayoutManager.HORIZONTAL);
//        exchangeRecyclerView.setLayoutManager(gridLayoutManager);
        exchangeRecyclerView.setLayoutManager(staggeredGridLayoutManager);


        //instantiate exchange recycler exchangeRecycleradapter
         exchangeRecycleradapter = new ExchangeRecyclerAdapter(this, new ArrayList<Exchange>());

        context = this;




        //On click listener for floating action button
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //instantiate a new dialog
               final Dialog dialog = new Dialog(context);

                //dialog shouldnt disappear when outer screen is clicked/pressed
                dialog.setCancelable(false);

                //set custom view for dialog
                dialog.setContentView(R.layout.dialog_add_exchange);


                //instantiate spinners for cryptocurrencies and worldcurrencies respectively
                final Spinner cSpinner =  dialog.findViewById(R.id.spinner_crypto);
                final Spinner wSpinner = dialog.findViewById(R.id.spinner_world);


                //Instantiate buttons
                Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
                Button saveBtn = (Button) dialog.findViewById(R.id.btn_add);


                //get String list of cryptocurrencies from db
                ArrayList<String> crypt = CurrencyUtils.getCryptoCurrencies(context.getContentResolver());

                //convert list to string array
                String[] cryptos = crypt.toArray(new String[crypt.size()]);


                //get String list of worldcurrencies from db
                ArrayList<String> world = CurrencyUtils.getWorldCurrencies(context.getContentResolver());

                //convert list to string array
                String[] worldCurr = world.toArray(new String[world.size()]);


                //create arrayadapter using cryptocurrencies string array
                ArrayAdapter<String> cryptosAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, cryptos );

                //set exchangeRecycleradapter to cryptoCurrencies spinner
                cSpinner.setAdapter(cryptosAdapter);



                //create arrayadapter using worldcurrencies string array
                ArrayAdapter<String> worldAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, worldCurr );

                //set exchangeRecycleradapter to worldCurrencies spinner
                wSpinner.setAdapter(worldAdapter);


                //set onclicklistener for cancel button
                cancelBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //dismiss dialog
                        dialog.dismiss();
                    }
                });



                //set onclicklistener for save button
                saveBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getApplicationContext(), "Save btn clicked\nCrypto" + cSpinner.getSelectedItem().toString()
//                                +"\nWorld: "+wSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();


                        //save exchange card
                        saveExchangeCard(cSpinner.getSelectedItem().toString(), wSpinner.getSelectedItem().toString());



                        dialog.dismiss();

                        //refresh exchange card to include newly added card
                        refresh();



                    }
                });

                //show dialog
                dialog.show();
            }


        });



        // add OnItemListener to exchangeRecyclerView
        exchangeRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), exchangeRecyclerView, new ClickListener() {

            //Onclick method
            @Override
            public void onClick(View view, int position) {


                //get exchange from exchangeRecycleradapter using position of item clicked
                Exchange exc = exchangeRecycleradapter.getItem(position);


                //log position clicked
//               Log.e("onClick","crypto position: "+ position);

                //intent to start conversion activity
                Intent intent = new Intent(context, ConversionActivity.class);

                //create bundle
                Bundle bundle = new Bundle();

                //put exchange details in bundle
                bundle.putString(ExchangeEntry.COLUMN_EXCHANGE_WORLD, exc.getWorldCurrency());
                bundle.putString(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, exc.getCryptoCurrency());
                bundle.putDouble("rate", exc.getRate());

                //add bundle to intent
                intent.putExtras(bundle);


                //start conversion activity
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Bundle trans_bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
//                    startActivity(intent, trans_bundle);
//                }else{
                    startActivity(intent);
//                }





            }

            @Override
            public void onLongClick(View view, final int position) {
               final Exchange exc = exchangeRecycleradapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "OnLongClick:\ncrypto: "+ exc.getCryptoCurrency() +"\nworld: "+exc.getWorldCurrency()+"\nRate: "+ exc.getRate(), Toast.LENGTH_LONG).show();
//
//                Log.e("onLongClick","crypto: "+ exc.getCryptoCurrency() +"\nworld: "+exc.getWorldCurrency()+"\nRate: "+ exc.getRate());
//                Log.e("onLongClick","crypto: "+ position);

                final AlertDialog.Builder builder;
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                    builder = new AlertDialog.Builder(context);
//                }else{
                    builder = new AlertDialog.Builder(context);
//                }
                builder.setTitle("Delete")
                        .setMessage("Delete "+exc.getCryptoCurrency()+"-"+exc.getWorldCurrency() +" exchange card?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                exchangeRecycleradapter.remove(exc);
                                exchangeRecycleradapter.notifyItemRemoved(position);


                                deleteExchangeCard(exc.getCryptoCurrency(), exc.getWorldCurrency());
                                refresh();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();


            }
        }));


        //set adapter to recycler view
        exchangeRecyclerView.setAdapter(exchangeRecycleradapter);


        //instantiate connectivity manager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //instantiate network info
         networkInfo = connectivityManager.getActiveNetworkInfo();


        //if network info is not null and network is connected to a network/internet
        if(networkInfo != null && networkInfo.isConnected()){


            // start/load loader with arrayList<Exchange>
            getLoaderManager().initLoader(1, null, this).forceLoad();

        }else {



            // show no internet status
            exchangeRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);
            statusTextView.setVisibility(View.VISIBLE);


        }

        //set color of swipeRefreshLayout refresh icon with either one color or array of color
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent) );


        /*refresh listener for swiperefreshlayout, this happens when the layout is dragged/pulled down*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {


                refresh();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_info:

                // start info activity
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    public void refresh(){


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivityManager.getActiveNetworkInfo();


        // if there is network and its connected
        if(networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().restartLoader(1, null, this);

        }else{

            exchangeRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);
            statusTextView.setVisibility(View.VISIBLE);
            //stop refreshing
            swipeRefreshLayout.setRefreshing(false);

        }
    }



    // save  exchange card
    public void saveExchangeCard(String crypto, String worldCurr){


        // check if combination of cryptocurrency and world currency already exists in db
        String[] projection = { ExchangeEntry._ID};
        String selection = ExchangeEntry.COLUMN_EXCHANGE_CRYPTO +"=? and " + ExchangeEntry.COLUMN_EXCHANGE_WORLD + "=?";
        String[] selectionArgs = {crypto, worldCurr};
        Cursor c = getContentResolver().query(ExchangeEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        // if combination already exists
        if(c.moveToNext()){


            //show snackbar to notify that the exchange already exist
            Snackbar snackbar =  Snackbar.make(swipeRefreshLayout, "Exchange card already exist", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();

        }else{

            //add exchange to database
            ContentValues values = new ContentValues();
            values.put(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO, crypto);
            values.put(ExchangeEntry.COLUMN_EXCHANGE_WORLD, worldCurr);

            getContentResolver().insert(ExchangeEntry.CONTENT_URI, values);







            //show refresh icon
            swipeRefreshLayout.setRefreshing(true);

           Snackbar snackbar =  Snackbar.make(swipeRefreshLayout, "Exchange card added", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();


        }

    }


    // delete  exchange card
    public void deleteExchangeCard(String crypto, String worldCurr){



        String selection = ExchangeEntry.COLUMN_EXCHANGE_CRYPTO +"=? and " + ExchangeEntry.COLUMN_EXCHANGE_WORLD + "=?";
        String[] selectionArgs = {crypto, worldCurr};

        getContentResolver().delete(ExchangeEntry.CONTENT_URI,selection, selectionArgs);


        Snackbar snackbar =  Snackbar.make(swipeRefreshLayout, "Exchange card deleted", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();


            swipeRefreshLayout.setRefreshing(true);


    }


    @Override
    public Loader<ArrayList<Exchange>> onCreateLoader(int i, Bundle bundle) {
//        Toast.makeText(this, "Loader creation starts", Toast.LENGTH_LONG).show();

        return new ExchangeLoader(this);


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Exchange>> loader, ArrayList<Exchange> exchanges) {
        progressBar.setVisibility(View.GONE);

        if (exchanges != null) {

            if (exchanges.size() != 0) {



                exchangeRecycleradapter.addAll(exchanges);

                exchangeRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                statusTextView.setVisibility(View.GONE);
                noExchangeView.setVisibility(View.GONE);

//                Toast.makeText(this, "Exchange rates refreshed", Toast.LENGTH_SHORT).show();

            }else{

                exchangeRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                statusTextView.setVisibility(View.GONE);
                noExchangeView.setVisibility(View.VISIBLE);

            }


        } else {

            exchangeRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            statusTextView.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);

        }

        swipeRefreshLayout.setRefreshing(false);



    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Exchange>> loader) {
        exchangeRecycleradapter.clear();
        exchangeRecycleradapter.notifyDataSetChanged();

    }









}


