package com.haybankz.cryptoxchange.ui;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haybankz.cryptoxchange.R;
import com.haybankz.cryptoxchange.database.exchange.ExchangeContract.ExchangeEntry;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.Locale;

public class ConversionActivity extends AppCompatActivity {



    TextInputLayout cryptoLayout;
    TextInputLayout worldLayout;

    EditText cryptocurrText;
    EditText worldcurrText;

    ImageView cryptoImage;
    ImageView worldImage;

    String crypto;
    String worldcur;
    double rate = 0;

    TextWatcher cryptoTw;
    TextWatcher worldTw;


    boolean cryptoKeypressed;
    boolean worldKeypressed;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_conversion);



        context = this;

        /*instantiate widgets*/
        cryptocurrText = new EditText(context);
        worldcurrText = new EditText(context);
        cryptoImage = (ImageView) findViewById(R.id.img_conv_crypto);
        worldImage = (ImageView) findViewById(R.id.img_conv_world);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            Slide slide = new Slide(Gravity.TOP);
//            slide.addTarget(R.id.edit_cryptocurr);
//            slide.setInterpolator(AnimationUtils.loadInterpolator(this,
//                    android.R.interpolator.linear_out_slow_in));
//            slide.setDuration(100000);
//            getWindow().setEnterTransition(slide);
//        }





        //set input type for both edittext
        cryptocurrText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        worldcurrText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //set textsize
        cryptocurrText.setTextSize(20);
        worldcurrText.setTextSize(20);

        //set textColor
        cryptocurrText.setTextColor(getResources().getColor(R.color.colorAccent));
        worldcurrText.setTextColor(getResources().getColor(R.color.colorAccent));


        //instantiate text input layouts
        cryptoLayout = (TextInputLayout) findViewById(R.id.edit_cryptocurr);
        worldLayout= (TextInputLayout) findViewById(R.id.edit_worldcurr);



        //get bundle
        Bundle bundle = getIntent().getExtras();


        //if bundle is not null
        if (bundle != null) {

           // get bundle values
            crypto = bundle.getString(ExchangeEntry.COLUMN_EXCHANGE_CRYPTO);
            worldcur = bundle.getString(ExchangeEntry.COLUMN_EXCHANGE_WORLD);
            rate = bundle.getDouble("rate");

            //load currency images
            Picasso.with(this.getApplicationContext()).load("file:///android_asset/flags/" +crypto +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(cryptoImage);

            Picasso.with(this.getApplicationContext()).load("file:///android_asset/flags/" +worldcur +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(worldImage);




            //set edittexts hint
            cryptocurrText.setHint(crypto);
            worldcurrText.setHint(worldcur);
        }

        //add edittexts to layouts
        cryptoLayout.addView(cryptocurrText);
        worldLayout.addView(worldcurrText);

        //set onKeyListener to cryptocurrText
        cryptocurrText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    cryptoKeypressed = true;
                } else {
                    cryptoKeypressed = false;
                }
                return false;
            }
        });


        //set onKeyListener to worldcurrText
        worldcurrText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    worldKeypressed = true;
                } else {
                    worldKeypressed = false;
                }
                return false;
            }
        });


        //Instantiate crytoTextWatch
        cryptoTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //in order to avoid infinite loop when the edittext is cleared
                if (cryptoKeypressed) {
                    String str = s.toString();
                    if (count == 1) {
                        cryptocurrText.setText(null);

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {



                //get text from cryptocurrText, use the input for conversion
                try {

                    if (cryptocurrText.getText().toString() != "") {

                        // replace/delete the comma(thousand separator)
                        double number = Double.parseDouble(cryptocurrText.getText().toString().replace("," , ""));

                        //convert value from cryptocurrency to world currency
                        BigDecimal converted = BigDecimal.valueOf(number * rate);

                        /*format world currency value with comma as thousand separator*/
                        String formatted = String.format(Locale.US,"%,.2f", converted) ;

                        /*remove worldcurrText textWatcher before setting text to it to avoid infinite loop caused
                             by both textChangeListeners of both worldcurrText and cryptocurrText*/
                        worldcurrText.removeTextChangedListener(worldTw);


                        //set worldcurrText text value
                        worldcurrText.setText(formatted);


                        //add textWatcher
                        worldcurrText.addTextChangedListener(worldTw);

                    }
                } catch (NumberFormatException ex) {
                    Log.e("ERROR", ex.getMessage());
                    worldcurrText.removeTextChangedListener(worldTw);
                    worldcurrText.setText(null);
                    worldcurrText.addTextChangedListener(worldTw);
                }


            }
        };


        worldTw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (worldKeypressed) {
                    String str = s.toString();
                    if (count == 1) {
                        worldcurrText.setText(null);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {

                    if (worldcurrText.getText().toString() != "" ) {

                        double number = Double.parseDouble(worldcurrText.getText().toString().replace(",", ""));




                        BigDecimal converted = BigDecimal.valueOf(number / rate);

                        cryptocurrText.removeTextChangedListener(cryptoTw);

                        String formatted = String.format(Locale.US,"%,.5f", converted) ;


                        cryptocurrText.setText(formatted);


                        cryptocurrText.addTextChangedListener(cryptoTw);

                    }
                } catch (NumberFormatException ex) {
                    Log.e("ERROR", ex.getMessage());
                    cryptocurrText.removeTextChangedListener(cryptoTw);
                    cryptocurrText.setText("");
                    cryptocurrText.addTextChangedListener(cryptoTw);
                }


            }
        };



        cryptocurrText.addTextChangedListener(cryptoTw);


        worldcurrText.addTextChangedListener(worldTw);


    }


}
