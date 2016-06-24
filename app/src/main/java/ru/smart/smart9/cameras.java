package ru.smart.smart9;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



public class cameras extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params;
    JSONObject reader;
    TableLayout tl;
    TableRow    tr1;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();

    Object objectmode = Model.instance().get_netmode();
    String netmode = objectmode.toString();

    String urlString  = server+"/android/cameras.php?input=cameras";
    JSONArray sensorsArray;

    ImageView graf;
    int widthdisplay;
    int heightdisplay;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameras);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthdisplay = size.x;
        heightdisplay = size.y;
        layout1 = (LinearLayout) findViewById(R.id.linear_cameras);
        btn = (Button) layout1.findViewById(R.id.button_cameras_refresh);
        btn.setText("ОЖИДАНИЕ...");
        new ProcessJSON().execute(urlString);
    }
    private class ProcessJSON extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }

        protected void onPostExecute(String stream){

            if(stream !=null){
                btn.setText("ОБНОВИТЬ");
                customButton(stream);

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end


    public void customButton(String stream) {
        try {
            if (!stream.equalsIgnoreCase("Not_found")) {
                reader = new JSONObject(stream);
                sensorsArray = reader.getJSONArray("sensors");
            }
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(1, 1, 1, 1);

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btn.setText("ОЖИДАНИЕ...");
                    tl.removeAllViews();
                    new ProcessJSON().execute(urlString);
                }
            });

            tl=(TableLayout)findViewById(R.id.table_cameras);
            for(int i = 0; i < sensorsArray.length(); i ++){
                JSONObject c = sensorsArray.getJSONObject(i);
                String title = c.getString("name");
                final String imgpath_big = c.getString("imgpath_big");
                final String inetpath = c.getString("inetpath");
                final String localpath = c.getString("localpath");
                final String time = c.getString("time");
                final String hash = c.getString("hash");
                String curpath;
                if (netmode.equalsIgnoreCase("inet"))
                {curpath = inetpath+time+"&st="+hash;}
                else
                {curpath = localpath+time+"&st="+hash;}
                final String path = curpath;

                TableRow tr = new TableRow(this);
                TableRow trb = new TableRow(this);
                tr1 = new TableRow(this);
                tr.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(this);
                textview.setText(title);
                tr.addView(textview);
                Button buttn = new Button(this);
                buttn.setText("Смотреть онлайн");
                buttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         {

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setPackage("org.videolan.vlc.betav7neon");
                            i.setDataAndType(Uri.parse(path), "video/h264");
                            startActivity(i);

                        }
                    }
                });
                trb.addView(buttn);
                final String bigimg = server+"/"+imgpath_big;
                graf = new ImageView(this);
                graf.setAdjustViewBounds(true);
                graf.setScaleType(ImageView.ScaleType.FIT_CENTER);
                graf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bigimg)));

                    }
                });
                new LoadImage(graf).execute(bigimg);
                tr1.addView(graf);
                tl.addView(tr, new TableLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                tl.addView(trb, new TableLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                tl.addView(tr1, new TableLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imgv;
        LoadImage( ImageView imgv) {
            this.imgv = imgv;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }
        @Override
        protected void onPostExecute(Bitmap result) {

            int endedWidth = widthdisplay;
            int originalWidth = result.getWidth();
            int originalHeight = result.getHeight();
            float scale = (float) endedWidth / originalWidth;
            int newHeight = Math.round(originalHeight * scale);
            imgv.setMaxWidth(endedWidth);
            imgv.setMaxHeight(newHeight);
            imgv.setImageBitmap(result);
        }
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {

                stream = getHttpConnection(url);
                if (stream != null) {
                    bitmap = BitmapFactory.
                            decodeStream(stream, null, bmOptions);
                    stream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }
        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}

