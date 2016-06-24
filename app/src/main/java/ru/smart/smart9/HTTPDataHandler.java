package ru.smart.smart9;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cfsuman on 31/05/2015.
 */
public class HTTPDataHandler {

    static String stream = null;
    Object objectusername = Model.instance().get_username();
    String username = objectusername.toString();

    Object objectuserpassword = Model.instance().get_userpassword();
    String userpassword = objectuserpassword.toString();
    String sendcode = username+":"+userpassword;

    public HTTPDataHandler(){
    }

    public String GetHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            final String basicAuth = "Basic " + Base64.encodeToString(sendcode.getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty ("Authorization", basicAuth);

            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                int responseCode = urlConnection.getResponseCode();
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                // Do something
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }


        return stream;

    }
}

