package ru.smart.smart9;

import android.util.Base64;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPGet {
    static int responseCode = 0;
    Object objectusername = Model.instance().get_username();
    String username = objectusername.toString();

    Object objectuserpassword = Model.instance().get_userpassword();
    String userpassword = objectuserpassword.toString();
    String sendcode = username+":"+userpassword;
    public HTTPGet(){
    }
       public int GetHTTPResponse(String urlString){
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                final String basicAuth = "Basic " + Base64.encodeToString(sendcode.getBytes(), Base64.NO_WRAP);
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
           return responseCode;
    }
}


