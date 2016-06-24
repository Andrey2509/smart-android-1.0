package ru.smart.smart9;

import android.util.Base64;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CheckServer {
    int status;

    Object objectusername = Model.instance().get_username();
    String username = objectusername.toString();

    Object objectuserpassword = Model.instance().get_userpassword();
    String userpassword = objectuserpassword.toString();
    String sendcode = username+":"+userpassword;

    static int responseCode = 0;
    public CheckServer(){
    }
    public int GetHTTPResponse(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            final String basicAuth = "Basic " + Base64.encodeToString(sendcode.getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("HEAD");
            urlConnection.setConnectTimeout(2000);

            urlConnection.setReadTimeout(2000);
            urlConnection.connect();
            urlConnection.setConnectTimeout(2000);
            urlConnection.setReadTimeout(2000);



            if  (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {status= 200;}

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (status ==200)
        {responseCode = 200;}
        else {responseCode = 0;}
        return responseCode;
    }
}

