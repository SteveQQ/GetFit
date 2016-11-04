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

public class FoodGet extends FatSecretService{

    public FoodGet(Context context){
        super(context);
    }

    public String buildRequest(Object... inputs){ //Generate parametrized parameters

        Long foodId = (Long)inputs[0];

        ArrayList<String> params = new ArrayList<>(generateParams(foodId));
        String[] template = new String[1];
        Collections.sort(params);
        return URL + "?" + join(params.toArray(template), "&");
    }

    public ArrayList<String> generateParams(Object... inputs) { //Generate not parametrized parameters

        Long foodId = (Long)inputs[0];
        ArrayList<String> params = new ArrayList<>();
        params.add("oauth_consumer_key=" + BuildConfig.API_KEY);
        params.add("oauth_signature_method=HMAC-SHA1");
        params.add("oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString());
        params.add("oauth_nonce=" + getNonce());
        params.add("oauth_version=1.0");
        params.add("format=json");
        params.add("method=food.get");
        params.add("food_id=" + foodId);
        try {
            params.add("oauth_signature=" + sign(buildSignatureBaseString(params)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }
}
