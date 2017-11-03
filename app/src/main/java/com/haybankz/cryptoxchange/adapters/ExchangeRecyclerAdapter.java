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

    private ArrayList<Exchange> mExchange;

    private Activity mActivity;

    public ExchangeRecyclerAdapter(Activity mActivity, ArrayList<Exchange> mExchange) {

        this.mActivity = mActivity;

        this.mExchange = mExchange;

    }

    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_card_item, parent, false);

        return new ExchangeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExchangeViewHolder holder, int position) {
        final Exchange newExchange = mExchange.get(position);
        final ArrayList<Exchange> oldExchanges = ExchangeUtils.getExchangeListFromDb(mActivity.getContentResolver());
        Exchange oldExchange = new Exchange();
        try {
             oldExchange = oldExchanges.get(position);
        }catch(IndexOutOfBoundsException ex){
            Log.e("", ex.getLocalizedMessage());
        }

        holder.bindExchange(mActivity.getApplicationContext(), newExchange, oldExchange.getRate());

    }


    public void addAll(List<Exchange> exc){
        mExchange.clear();
        mExchange.addAll(exc);
        notifyDataSetChanged();
    }

    public void clear (){
        mExchange.clear();
        notifyDataSetChanged();
    }


    public Exchange getItem(int position){
        return mExchange.get(position);
    }

    public void remove(Exchange exchange){
        mExchange.remove(exchange);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mExchange.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    static class ExchangeViewHolder extends RecyclerView.ViewHolder {

        TextView cryptoTv;
        TextView worldTv;
        TextView rateTv;
        ImageView coinImage, worldImage;

        View mView;

        ExchangeViewHolder(View itemView){
            super(itemView);


            cryptoTv = itemView.findViewById(R.id.text_crypto_name);
            worldTv = itemView.findViewById(R.id.text_world_name);
            rateTv = itemView.findViewById(R.id.text_rate);
            coinImage = itemView.findViewById(R.id.img_crypto);
            worldImage = itemView.findViewById(R.id.img_world);

        }


        private void bindExchange(Context context, Exchange newExchange, double oldRate) {
            cryptoTv.setText(newExchange.getCryptoCurrency());
            worldTv.setText(newExchange.getWorldCurrency());

            BigDecimal rate = BigDecimal.valueOf(newExchange.getRate());

            String formatted = String.format(Locale.US,"%,.2f", rate) ;

            rateTv.setText(formatted);

            if(newExchange.getRate() > oldRate && oldRate != 0.0){
                rateTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));


            }else if(newExchange.getRate() < oldRate ){
                rateTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));

            }else if(newExchange.getRate() == oldRate || oldRate == 0.0){
                rateTv.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
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
