package com.dcalabrese22.dan.zerionassessment;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dan on 12/21/17.
 */

//Helper class to get the access token
public class AccessTokenHelper {


    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String ENCODED_HEADER = Base64.encodeToString(HEADER.getBytes(),
            Base64.NO_WRAP | Base64.URL_SAFE);


    /**
     * Creates a json web token for authentication
     * @param iss Payload issuer
     * @param secret Secret key
     * @return JWT as a string
     */
    public static String createJwt(String iss, String secret) {

        long timeStamp = System.currentTimeMillis() / 1000;
        long timePlus5min = timeStamp + 300;

        //create encoded payload
        String payload = "{\"iss\":\"" + iss + "\"," +
                "\"aud\":\"https://app.iformbuilder.com/exzact/api/oauth/token\"," +
                "\"exp\":" + timePlus5min + "," +
                "\"iat\":" + timeStamp + "}";
        String encodedPayload = Base64.encodeToString(payload.getBytes(),
                Base64.NO_WRAP | Base64.URL_SAFE);
        encodedPayload = encodedPayload.replace("=", "");

        //create jwt
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            String comb = ENCODED_HEADER + "." + encodedPayload;
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret
                    .getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);

            String hash = Base64.encodeToString(mac.doFinal(comb.getBytes()),
                    Base64.URL_SAFE | Base64.NO_WRAP);
            hash = hash.replace("=", "");

            String jwt = comb + "." + hash;
            jwt = jwt.replace("\n", "");
            return jwt;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the access token to use the iformbuilder api
     *
     * @param jwt Json web token
     * @return access token as a string
     */
    public static String getAccessToken(String jwt) {
        HttpsURLConnection http = null;
        try {
            URL url = new URL("https://app.iformbuilder.com/exzact/api/oauth/token");
            http = (HttpsURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            String grantTypeString = "&grant_type=";
            String grantTypeUrlEncode = "urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer";
            String assertion = "&assertion=";
            String body = grantTypeString + grantTypeUrlEncode + assertion + jwt;
            byte[] bytes = body.getBytes("UTF-8");
            int length = bytes.length;
            http.setFixedLengthStreamingMode(length);
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(bytes);
            InputStream is = http.getInputStream();
            String response = getFromInputStream(is);
            try {
                JSONObject object = new JSONObject(response);
                String token = object.getString("access_token");
                return token;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e) {
            Log.d("Malformed Url", e.toString());
        }catch (IOException e) {
            Log.d("IOException", e.toString());
        }finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return null;
    }

    /**
     * Reads from an inputstream
     * @param is Inputstream to read from
     * @return the contents of the inputstream as a string
     */
    public static String getFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


}
