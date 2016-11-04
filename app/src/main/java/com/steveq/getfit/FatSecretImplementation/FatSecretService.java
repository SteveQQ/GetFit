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

import org.apache.commons.lang.RandomStringUtils;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class FatSecretService {


    final static protected String METHOD = "GET";
    final static protected String URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String ALGORITHM = "HmacSHA1";


    private Context mContext;
    private String resultJsonString;
    public FatSecretService(Context context){
        mContext = context;
    }

    public void execMethod(String resquest) throws Exception{
        final String jsonString;
        if(isNetworkAvailable()){

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(resquest)
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

    protected abstract ArrayList<String> generateParams(Object... inputs);

    protected abstract String buildRequest(Object... inputs);

    protected String buildSignatureBaseString(ArrayList<String> params){

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

    protected static String sign(String sbs) throws UnsupportedEncodingException {

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

    protected static String percentEncoding(String s){
        return Uri.encode(s, "-,.,_,~");
    }

    protected static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    protected String getNonce() {
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

}
