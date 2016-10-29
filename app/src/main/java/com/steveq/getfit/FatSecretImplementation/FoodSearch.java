package com.steveq.getfit.FatSecretImplementation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.steveq.getfit.BuildConfig;
import com.steveq.getfit.R;
import com.steveq.getfit.model.Food;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FoodSearch {

    //-----CONSTANTS-----//
    final static private String METHOD = "GET";
    final static private String URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String ALGORITHM = "HmacSHA1";
    //-----CONSTANTS-----//

    private Context mContext;
    private String resultJsonString;
    public FoodSearch(Context context){
        mContext = context;
    }

    public void foodsSearch(String query, int page) throws Exception{
        final String jsonString;
        if(isNetworkAvailable()){

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(buildRequest(query, page))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {

                    String line;
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    while ((line = reader.readLine()) != null) {
                        Log.d("GETFITres", line);
                        builder.append(line);
                    }
                    Log.d("builder", builder.toString());
                    resultJsonString = builder.toString();
                    }
                }
            );
        } else {
            Toast.makeText(mContext, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }


    }

    public String getResultJsonString() {
        return resultJsonString;
    }

    public String buildRequest(String query, int pageNumber) throws Exception { //Generate parametrized parameters
        ArrayList<String> params = new ArrayList<String>(generateParams());
        String[] template = new String[1];
        params.add("page_number=" + pageNumber);
        params.add("search_expression=" + percentEncoding(query));
        params.add("oauth_signature=" + sign(buildSignatureBaseString(params)));
        Collections.sort(params);
        return URL + "?" + join(params.toArray(template), "&");
    }

    private ArrayList<String> generateParams() { //Generate not parametrized parameters

        ArrayList<String> params = new ArrayList<>();
        params.add("oauth_consumer_key=" + BuildConfig.API_KEY);
        params.add("oauth_signature_method=HMAC-SHA1");
        params.add("oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString());
        params.add( "oauth_nonce=" + getNonce());
        params.add("oauth_version=1.0");
        params.add("format=json");
        params.add("method=foods.search");
        params.add("max_results=10");
        return params;
    }

    public String buildSignatureBaseString(ArrayList<String> params){

        ArrayList<String> p = new ArrayList<>(params);
        Collections.sort(p);
        String[] template = new String[1];
        StringBuilder builder = new StringBuilder();

        builder.append(METHOD);
        builder.append("&");
        builder.append(percentEncoding(URL));
        builder.append("&");
        builder.append(percentEncoding(join(p.toArray(template), "&")));

        return builder.toString();
    }

    private static String sign(String sbs) throws UnsupportedEncodingException {

        String key = BuildConfig.SS + "&";
        SecretKey sk = new SecretKeySpec(key.getBytes(), ALGORITHM);
        String sign = "";
        try {
            Mac m = Mac.getInstance(ALGORITHM);
            m.init(sk);
            sign = percentEncoding(new String(Base64.encode(m.doFinal(sbs.getBytes()), Base64.DEFAULT)).trim());
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        }
        return sign;
    }

    //----- Helper Functions -----//

    public static String percentEncoding(String s){
        return Uri.encode(s, "-,.,_,~");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private String getNonce() {
        return RandomStringUtils.random(32, mContext.getResources().getString(R.string.alpha_num).toCharArray());
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    //----- Helper Functions -----//
}
