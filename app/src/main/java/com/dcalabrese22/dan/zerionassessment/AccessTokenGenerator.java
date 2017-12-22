package com.dcalabrese22.dan.zerionassessment;

import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by dan on 12/21/17.
 */

public class AccessTokenGenerator {

    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String ENCODED_HEADER = Base64.encodeToString(HEADER.getBytes(),
            Base64.NO_WRAP |Base64.URL_SAFE);


    public static String createJwt(String iss) {

        long timeStamp = System.currentTimeMillis()/1000;
        long timePlus5min = timeStamp + 300;

        Log.d("Timestamp", String.valueOf(timeStamp));
        Log.d("time plus 5 ", String.valueOf(timePlus5min));

        String payload = "{\"iss\":\"" + iss + "\"," +
                "\"aud\":\"https://app.iformbuilder.com/exzact/api/oauth/token\"," +
                "\"exp\":" + timePlus5min + "," +
                "\"iat\":" + timeStamp + "}";

        String encodedPayload = Base64.encodeToString(payload.getBytes(),
                Base64.NO_WRAP | Base64.URL_SAFE);
        encodedPayload = encodedPayload.replace("=", "");

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            String comb = ENCODED_HEADER + "." + encodedPayload;
            SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.SECRET_KEY
                    .getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);

            String hash = Base64.encodeToString(mac.doFinal(comb.getBytes()),
                    Base64.URL_SAFE | Base64.NO_WRAP);
            hash = hash.replace("=", "");

            String jwt = comb + "." + hash;
            jwt = jwt.replace("\n", "");
            Log.d("JWT", jwt);
            return jwt;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObjectRequest getAccessToken(final String jwt) {
        final String grantTypeString = "&grant_type=";
        final String grantType = "urn:ietf:params:oauth:grant-type:jwt-bearer";
        final String grantTypeUrlEncode = "urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer";
        final String assertion = "&assertion=";
        String url = "https://app.iformbuilder.com/exzact/api/oauth/token";
        final StringBuilder accessToken = new StringBuilder();

        try {
            JSONObject object = new JSONObject();
            object.put("grant_type", grantType);
            object.put("assertions", jwt);
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                    object,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        accessToken.append(response.getString("access_token"));
                        Log.d("Token", accessToken.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                public byte[] getBody() {
                    Log.d("getBody", "called");
                    String body = grantTypeString + grantTypeUrlEncode +
                          assertion + jwt;
                    return body.getBytes();
                }
            };

            return request;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void makeRequest(String jwt) {
//        try {
//            URL url = new URL("https://app.iformbuilder.com/exzact/api/oauth/token");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
//            connection.setRequestProperty("assertion", jwt);
//
//        }
//    }



}
