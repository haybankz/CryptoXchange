package com.haybankz.cryptoxchange;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haybankz.cryptoxchange.model.Exchange;
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

    List<Exchange> exchanges;
    Activity activity;

    public ExchangeRecyclerAdapter(Activity activity, ArrayList<Exchange> exc){
        this.activity = activity;
        exchanges = exc;
    }

    @Override
    public ExchangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_card_item, parent, false);
        ExchangeViewHolder evh = new ExchangeViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExchangeViewHolder holder, int position) {
        final Exchange exc = exchanges.get(position);

        holder.bindExchange(activity.getApplicationContext(), exc);
//        holder.cryptoTv.setText(exchanges.get(position).getCryptoCurrency());
//        holder.worldTv.setText(exchanges.get(position).getWorldCurrency());
//        holder.rateTv.setText(String.valueOf(exchanges.get(position).getRate()));



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

    public void addAll(List<Exchange> exc){
        exchanges.clear();
        exchanges.addAll(exc);
        notifyDataSetChanged();
    }

    public void clear (){
        exchanges.clear();
        notifyDataSetChanged();
    }


    public Exchange getItem(int position){
        return exchanges.get(position);
    }

    public void remove(Exchange exchange){
        exchanges.remove(exchange);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
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


        public void bindExchange(Context context, Exchange exchange){
            cryptoTv.setText(exchange.getCryptoCurrency());
            worldTv.setText(exchange.getWorldCurrency());

            BigDecimal rate = BigDecimal.valueOf(exchange.getRate());

            String formatted = String.format(Locale.US,"%,.2f", rate) ;

            rateTv.setText(formatted);




            Picasso.with(context)
                    .load("file:///android_asset/flags/" + exchange.getWorldCurrency() +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(worldImage);


            Picasso.with(context)
                    .load("file:///android_asset/flags/" + exchange.getCryptoCurrency() +".png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .noFade()
                    .into(coinImage);

        }
    }
}
