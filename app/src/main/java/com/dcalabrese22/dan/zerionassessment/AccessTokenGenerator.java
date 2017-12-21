package com.dcalabrese22.dan.zerionassessment;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


import java.security.PrivateKey;
import java.security.Signature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by dan on 12/21/17.
 */

public class AccessTokenGenerator {


    public static String createToken(String iss) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String encodedHeader = Base64.encodeToString(header.getBytes(), Base64.DEFAULT);
        Log.d("Header", encodedHeader);

        long timeStamp = System.currentTimeMillis()/1000;

        JSONObject objPayload = new JSONObject();
        Log.d("timestamp", String.valueOf(timeStamp));
        try {
            objPayload.put("iss", iss);
            objPayload.put("aud", "https://app.iformbuilder.com/exzact/api/oauth/token");
            objPayload.put("exp", timeStamp + 300);
            objPayload.put("iat", timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String encodedPayload = Base64.encodeToString(objPayload.toString().getBytes(),
                Base64.DEFAULT);
        Log.d("Payload", encodedPayload);

        String secret = "35a17e4ff49f8b66f04e63edd3449052ae72e7e2";
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            String comb = encodedHeader + "." + encodedPayload;
            Log.d("combined", comb);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            sha256.init(secretKeySpec);
            byte[] sig = sha256.doFinal(comb.getBytes("UTF-8"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
