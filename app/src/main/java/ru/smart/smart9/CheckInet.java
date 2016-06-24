package ru.smart.smart9;

import android.util.Base64;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckInet {
    int status;
    static int responseCode = 0;
    public CheckInet(){
    }
    public int GetHTTPResponse(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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
