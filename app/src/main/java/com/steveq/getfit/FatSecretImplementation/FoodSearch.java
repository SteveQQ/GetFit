package com.steveq.getfit.FatSecretImplementation;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.steveq.getfit.BuildConfig;
import com.steveq.getfit.R;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SystemUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodSearch {

    //-----CONSTANTS-----//
    final static private String METHOD = "GET";
    final static private String URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String ALGORITHM = "HmacSHA1";
    //-----CONSTANTS-----//

    private Context mContext;

    public FoodSearch(Context context){
        mContext = context;
    }

    public void foodsSearch(String query, int page) throws Exception{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(buildRequest(query, page))
                .build();

        Headers requestHeaders = request.headers();
        for (int i = 0; i < requestHeaders.size(); i++) {
            Log.d("GETFITres", requestHeaders.name(i) + ": " + requestHeaders.value(i));
        }

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.d("GETFITres", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                Log.d("GETFITres", response.body().contentType().toString());

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                while ((line = reader.readLine()) != null) {
                    Log.d("GETFITres", line);
                }
            }
        });
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
        params.add("oauth_consumer_key=" /*...*/);
        params.add("oauth_signature_method=HMAC-SHA1");
        params.add("oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString());
        params.add( "oauth_nonce=" + getNonce());
        params.add("oauth_version=1.0");
        params.add("format=json");
        params.add("method=foods.search");
        params.add("max_results=5");
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

        String key /*=...*/;
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

    //----- Helper Functions -----//
}
