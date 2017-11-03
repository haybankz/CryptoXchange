package com.haybankz.cryptoxchange.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.haybankz.cryptoxchange.model.Exchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LENOVO on 10/3/2017.
 */

public class ApiUtils {

    private final static String BASE_URL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
    private final static String SUPPLEMENTARY_URL = "&tsyms=";
   private static List<String>  cryptoCur, worldCur;




    public static Map<String, Map<String, Double>> extractMessageFromJson(String jsonResponse) {

        Map<String, Map<String, Double>> currMap = new HashMap<>();

        if (TextUtils.isEmpty(jsonResponse) || TextUtils.equals(jsonResponse, "")) {

            return currMap;
        } else {
            try {

                JSONObject jsonRootObject = new JSONObject(jsonResponse);

                for( String crypt : cryptoCur){

                    JSONObject cryptObject = jsonRootObject.optJSONObject(crypt);

                    Map<String, Double> rateMap = new HashMap<>();

                    for(String world : worldCur){

                        Double rate = cryptObject.optDouble(world);
                        rateMap.put(world, rate);
                    }

                    currMap.put(crypt, rateMap);
                }

            } catch (JSONException e) {

                Log.e("ApiUtils", "error parsing the exchange JSON result", e);

            }
        }

        return currMap;
    }

    public static Map<String, Map<String, Double>> getExchangeFromApi(ArrayList<String> cryptos, ArrayList<String> world) {

        cryptoCur = cryptos;
        worldCur = world;

        String urlString = createUrlString(cryptos, world);



        String jsonResponse = "";

        try {


            jsonResponse = getJsonResponse(urlString);

        } catch (IOException e) {

            Log.e("ApiUtil", "Error parsing the exchange json result", e);
        }

        Map<String, Map<String, Double>>  curr = extractMessageFromJson(jsonResponse);

        return curr;
    }


    public static String createUrlString(ArrayList<String> cryptos, ArrayList<String> worldCur){

        String cryptoString = "";
        String worldCurString = "";


        // convert crypto list to string
        for(int i = 0; i < cryptos.size(); i++){
            if(i == 0){

                cryptoString += cryptos.get(i);

            }
            else{
                cryptoString += ","+cryptos.get(i);
            }
        }


        // convert worldCur list to  string
        for(int i = 0; i < worldCur.size(); i++){
            if(i == 0){
                worldCurString += worldCur.get(i);
            }
            else{
                worldCurString += ","+worldCur.get(i);
            }
        }

        return BASE_URL + cryptoString + SUPPLEMENTARY_URL + worldCurString;
    }

    public static String getJsonResponse(String url) throws IOException{

        String jsonResponse = "";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        jsonResponse = response.body().string();

        return jsonResponse;
    }
    
}
