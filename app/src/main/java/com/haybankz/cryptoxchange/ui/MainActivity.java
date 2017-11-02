package com.haybankz.cryptoxchange.ui;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.haybankz.cryptoxchange.listeners.ClickListener;
import com.haybankz.cryptoxchange.ExchangeLoader;
import com.haybankz.cryptoxchange.adapters.ExchangeRecyclerAdapter;
import com.haybankz.cryptoxchange.R;
import com.haybankz.cryptoxchange.listeners.RecyclerTouchListener;
import com.haybankz.cryptoxchange.model.Exchange;
import com.haybankz.cryptoxchange.utils.CurrencyUtils;
import com.haybankz.cryptoxchange.database.exchange.ExchangeContract.ExchangeEntry;
import com.haybankz.cryptoxchange.utils.ExchangeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Exchange>> {

//    CoordinatorLayout coordinateLayout;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView exchangeRecyclerView;
    ExchangeRecyclerAdapter exchangeRecycleradapter;


//    View emptyView;
//    View progressBar;
//    View statusTextView;
    View noExchangeView;

    TextView updateTimeTextView;


    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    Context context;
//    Dialog dialog;

    ArrayList<Exchange> oldExchange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        oldExchange = ExchangeUtils.getExchangeListFromDb(context.getContentResolver());

//        getSupportActionBar().setIcon(R.drawable.ic_title);


        //Instantiate widgets, layout, exchangeRecycleradapter and context
        fab = (FloatingActionButton) findViewById(R.id.fab);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setRefreshing(true);

        exchangeRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        progressBar =  findViewById(R.id.progressBar);
//        statusTextView = (View) findViewById(R.id.status_view);
//        emptyView =  (View) findViewById(R.id.empty_view);
        noExchangeView = (View) findViewById(R.id.no_exchange_view);

        updateTimeTextView = (TextView) findViewById(R.id.updateTimeText);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2&3);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL|StaggeredGridLayoutManager.HORIZONTAL);
//        exchangeRecyclerView.setLayoutManager(gridLayoutManager);
        exchangeRecyclerView.setLayoutManager(staggeredGridLayoutManager);


        //instantiate exchange recycler exchangeRecycleradapter
         exchangeRecycleradapter = new ExchangeRecyclerAdapter(this, oldExchange);

         updateTimeTextView.setText(getUpdateTime());

        Log.e("UpdateTime--read", "last updated: "+ getUpdateTime());







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
            exchangeRecyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);
//            statusTextView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(this, "Cant load feed", Toast.LENGTH_SHORT).show();


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

            exchangeRecyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);
//            statusTextView.setVisibility(View.VISIBLE);


            getLoaderManager().restartLoader(1, null, this);

            //stop refreshing
            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(this, "Cant load feed", Toast.LENGTH_SHORT).show();

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




            noExchangeView.setVisibility(View.GONE);
            exchangeRecyclerView.setVisibility(View.VISIBLE);


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
        oldExchange = ExchangeUtils.getExchangeListFromDb(context.getContentResolver());

        return new ExchangeLoader(this);


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Exchange>> loader, ArrayList<Exchange> exchanges) {
//        progressBar.setVisibility(View.GONE);

        if (exchanges != null) {

            if (exchanges.size() != 0) {



                exchangeRecycleradapter.addAll(exchanges);

                exchangeRecyclerView.setVisibility(View.VISIBLE);
//                emptyView.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                statusTextView.setVisibility(View.GONE);
                noExchangeView.setVisibility(View.GONE);

//                Toast.makeText(this, "Exchange rates refreshed", Toast.LENGTH_SHORT).show();


                Calendar calendar = Calendar.getInstance();


                long date = calendar.getTime().getTime();

                String dateString = getDateString(date);

                updateTimeTextView.setText(dateString);

                updateTime(dateString);

                runLayoutAnimation();


            }else{

//                exchangeRecyclerView.setVisibility(View.GONE);
//                emptyView.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                statusTextView.setVisibility(View.GONE);
                noExchangeView.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "Cant refresh feed", Toast.LENGTH_SHORT).show();

            }


        } else {

            exchangeRecyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Cant refresh feed", Toast.LENGTH_SHORT).show();

//            emptyView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            statusTextView.setVisibility(View.GONE);
            noExchangeView.setVisibility(View.GONE);

        }

        swipeRefreshLayout.setRefreshing(false);





//        ExchangeUtils.updateExchangeRate(exchangeRecycleradapter.getAllExchanges());

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Exchange>> loader) {
        exchangeRecycleradapter.clear();
        exchangeRecycleradapter.notifyDataSetChanged();

    }

    public String getDateString(long dateInTimeMillis) {
        Date date = new Date(dateInTimeMillis);
//        String dateString = "";

        String format = "dd MMM, yyyy hh:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        // System.err.format("%40s %s\n", format, dateFormat.format(date));


        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return String.format("%30s %s", "", dateFormat.format(date));
    }


    public void updateTime(String date){
        SharedPreferences sharedPreferences = getSharedPreferences("cryptoxchange", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastUpdate", date);

        editor.apply();

        Log.e("UpdateTime--write", "last updated: "+ date);
    }


    public String getUpdateTime(){
        SharedPreferences sharedPreferences = getSharedPreferences("cryptoxchange", Context.MODE_PRIVATE);


        return sharedPreferences.getString("lastUpdate","");
    }


    private void runLayoutAnimation(){
        final Context context = exchangeRecyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        exchangeRecyclerView.setLayoutAnimation(controller);
        exchangeRecyclerView.getAdapter().notifyDataSetChanged();
        exchangeRecyclerView.scheduleLayoutAnimation();
    }




}


