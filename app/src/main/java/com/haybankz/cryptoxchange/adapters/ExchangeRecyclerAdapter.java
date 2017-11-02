package com.haybankz.cryptoxchange.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haybankz.cryptoxchange.R;
import com.haybankz.cryptoxchange.model.Exchange;
import com.haybankz.cryptoxchange.utils.ExchangeUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by LENOVO on 10/5/2017.
 */

public class ExchangeRecyclerAdapter extends RecyclerView.Adapter<ExchangeRecyclerAdapter.ExchangeViewHolder> {

    ArrayList<Exchange> newExchanges;

    public Activity activity;

    public ExchangeRecyclerAdapter(Activity activity, ArrayList<Exchange> newExchanges){
        this.activity = activity;

//        oldExchanges = ExchangeUtils.getExchangeListFromDb(activity.getContentResolver());
        this.newExchanges = newExchanges;

    }

    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_card_item, parent, false);
        ExchangeViewHolder evh = new ExchangeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExchangeViewHolder holder, int position) {
        final Exchange newExchange = newExchanges.get(position);
        final ArrayList<Exchange> oldExchanges = ExchangeUtils.getExchangeListFromDb(activity.getContentResolver());
        Exchange oldExchange = new Exchange();
        try {
             oldExchange = oldExchanges.get(position);
        }catch(IndexOutOfBoundsException ex){
            Log.e("", ex.getLocalizedMessage());
        }


//        ExchangeUtils.updateExchangeRate(oldExchange);

        holder.bindExchange(activity.getApplicationContext(), newExchange, oldExchange.getRate());
//        holder.cryptoTv.setText(newExchanges.get(position).getCryptoCurrency());
//        holder.worldTv.setText(newExchanges.get(position).getWorldCurrency());
//        holder.rateTv.setText(String.valueOf(newExchanges.get(position).getRate()));



//        if(exc.getCryptoCurrency().equals("BTC")){
//
//           holder.coinImage.setImageResource(R.drawable.ic_btc);
//
//        }else if(exc.getCryptoCurrency().equals("ETH")) {
//
//           holder.coinImage.setImageResource(R.drawable.ic_eth);
//        }

//        Picasso.with(activity.getApplicationContext())
//                .load("file:///android_asset/flags/" + exc.getCryptoCurrency() +".png")
//
//                .into(holder.coinImage);

    }


    public ArrayList<Exchange> getAllExchanges(){
        return newExchanges;
    }

    public void addAll(List<Exchange> exc){
        newExchanges.clear();
        newExchanges.addAll(exc);
        notifyDataSetChanged();
    }

    public void clear (){
        newExchanges.clear();
        notifyDataSetChanged();
    }


    public Exchange getItem(int position){
        return newExchanges.get(position);
    }

    public void remove(Exchange exchange){
        newExchanges.remove(exchange);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newExchanges.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    public static class ExchangeViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView cryptoTv;
        TextView worldTv;
        TextView rateTv;
//        CircleImageView coinImage;
        ImageView coinImage, worldImage;

        View mView;

        ExchangeViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            cv = (CardView) itemView.findViewById(R.id.cardview);
            cryptoTv = (TextView) itemView.findViewById(R.id.text_crypto_name);
            worldTv = (TextView) itemView.findViewById(R.id.text_world_name);
            rateTv = (TextView) itemView.findViewById(R.id.text_rate);
            coinImage = (ImageView) itemView.findViewById(R.id.img_crypto);
            worldImage = (ImageView) itemView.findViewById(R.id.img_world);

        }


        public void bindExchange(Context context, Exchange newExchange, double oldRate ){
            cryptoTv.setText(newExchange.getCryptoCurrency());
            worldTv.setText(newExchange.getWorldCurrency());

            BigDecimal rate = BigDecimal.valueOf(newExchange.getRate());

            String formatted = String.format(Locale.US,"%,.2f", rate) ;

            rateTv.setText(formatted);

            Log.e("Exchange: "+newExchange.getWorldCurrency()+"--"+newExchange.getCryptoCurrency(),"Old rate: "+oldRate+
                    " New rate: "+newExchange.getRate());

            if(newExchange.getRate() > oldRate && oldRate != 0.0){
                rateTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                Log.e("rates", "Increase");


            }else if(newExchange.getRate() < oldRate ){
                rateTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                Log.e("rates", "decrease");

            }else if(newExchange.getRate() == oldRate || oldRate == 0.0){
                rateTv.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                Log.e("rates", "same");
            }




            Picasso.with(context)
                    .load("file:///android_asset/flags/" + newExchange.getWorldCurrency() +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(worldImage);


            Picasso.with(context)
                    .load("file:///android_asset/flags/" + newExchange.getCryptoCurrency() +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(coinImage);

            ExchangeUtils.updateExchangeRate(newExchange);

        }



    }
}
