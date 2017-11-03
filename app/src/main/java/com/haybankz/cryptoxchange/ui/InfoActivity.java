package com.haybankz.cryptoxchange.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.haybankz.cryptoxchange.R;
import com.haybankz.cryptoxchange.model.Currency;
import com.haybankz.cryptoxchange.utils.CurrencyUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {

    TableLayout tableLayout;
    ArrayList<Currency> currencies;
    View tableHeader;
    View tableRow;
    LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        //Instatantiate layout
        tableLayout = (TableLayout) findViewById(R.id.table_layout);


        headerLayout = (LinearLayout) findViewById(R.id.header);

        //get ArrayList of all currencies in db
        currencies = CurrencyUtils.getAllCurrencies(getContentResolver());


        //instatiate table row item for use as column headers
        tableHeader = LayoutInflater.from(this).inflate(R.layout.info_table_header, null, false);
        TextView rowNoHeader = tableHeader.findViewById(R.id.row_no);
        TextView nameHeader = tableHeader.findViewById(R.id.row_name);
        TextView fullnameHeader = tableHeader.findViewById(R.id.row_full_name);




        //set column header text
        rowNoHeader.setText(getResources().getText(R.string.flag));
        nameHeader.setText(getResources().getText(R.string.name));
        fullnameHeader.setText(getResources().getText(R.string.fullname));

        //set background color for column header
        tableHeader.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        //add header to table
        headerLayout.addView(tableHeader);



        //for each currency in currencies arraylist
        for (Currency currency : currencies) {

            //create a table row
            tableRow = LayoutInflater.from(this).inflate(R.layout.info_table_row, null, false);

            //instantiate each cell for the row
            ImageView noTv = tableRow.findViewById(R.id.img_row);
            TextView nameTv = tableRow.findViewById(R.id.row_name);
            TextView fullnameTv = tableRow.findViewById(R.id.row_full_name);
//            TextView categoryTv = tableRow.findViewById(R.id.row_category);

            //Set cells values
            nameTv.setText(currency.getName());
            fullnameTv.setText(currency.getFullname());


            Picasso.with(this.getApplicationContext())
                    .load("file:///android_asset/flags/" + currency.getName() +".png")
                    .into(noTv);


            //add row to table
            tableLayout.addView(tableRow);

        }

    }
}
