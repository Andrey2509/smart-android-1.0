package ru.smart.smart9;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import ru.smart.smart9.R;


public class Journal_images extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params2;
    LinearLayout.LayoutParams params;
    JSONObject reader;
    TableLayout tl;
    private RadioGroup radioGroup;
    private RadioButton radioSexButton;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();

    String urlString  = server+"/android/journal.php?input=images";
    JSONArray sensorsArray;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_images);
        layout1 = (LinearLayout) findViewById(R.id.linear_journal_images);
        btn = (Button) layout1.findViewById(R.id.button_refresh_journal_images);
        new ProcessJSON().execute(urlString + "&period=day");

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
       ;

            radioGroup = (RadioGroup) findViewById(R.id.radio_period_journal_images);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    btn.setText("ОЖИДАНИЕ...");
                    RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                    String text = checkedRadioButton.getText().toString();
                    String period = "day";
                    if (text.equalsIgnoreCase("За сутки")) {
                        period = "day";
                    }
                    if (text.equalsIgnoreCase("За неделю")) {
                        period = "week";
                    }
                    if (text.equalsIgnoreCase("За месяц")) {
                        period = "month";
                    }
                    tl.removeAllViews();

                    new ProcessJSON().execute(urlString+"&period="+period);
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btn.setText("ОЖИДАНИЕ...");
                    tl.removeAllViews();
                    int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    String period = "day";
                    if (index == 0 )
                    {period = "day";}
                    if (index == 1)
                    {period = "week";}
                    if (index == 2)
                    {period = "month";}

                    new ProcessJSON().execute(urlString+"&period="+period);

                }
            });


            tl=(TableLayout)findViewById(R.id.table_journal_images);
             if (!stream.equalsIgnoreCase("Not_found")) {
                tl.setGravity(Gravity.CENTER);
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
                gd.setGradientCenter(0.f, 1.f);
                gd.setLevel(2);
                int fontsize=14;

                TableRow tr = new TableRow(this);
                TableRow.LayoutParams aParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                aParams.topMargin = 3;
                aParams.rightMargin = 3;
                aParams.height = 75;
                TableRow.LayoutParams bParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                bParams.topMargin = 3;
                bParams.height = 75;
                TextView text1 = new TextView(this);
                TextView text2 = new TextView(this);
                TextView text3 = new TextView(this);
                TextView text4 = new TextView(this);
                text1.setLayoutParams(aParams);
                text2.setLayoutParams(aParams);
                text3.setLayoutParams(aParams);
                text4.setLayoutParams(bParams);
                text1.setBackgroundColor(Color.WHITE);
                text2.setBackgroundColor(Color.WHITE);
                text3.setBackgroundColor(Color.WHITE);
                text4.setBackgroundColor(Color.WHITE);
                text1.setTypeface(null, Typeface.BOLD);
                text2.setTypeface(null, Typeface.BOLD);
                text3.setTypeface(null, Typeface.BOLD);
                text4.setTypeface(null, Typeface.BOLD);
                text1.setGravity(Gravity.CENTER);
                text2.setGravity(Gravity.CENTER);
                text3.setGravity(Gravity.CENTER);
                text4.setGravity(Gravity.CENTER);

                text1.setText("Дата");
                tr.addView(text1);
                text2.setText("Датчик");
                tr.addView(text2);
                text3.setText("Камера");
                tr.addView(text3);
                text4.setText("Снимок");
                tr.addView(text4);
                tl.addView(tr);


                for (int i = 0; i < sensorsArray.length(); i++) {
                    params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(2, 2, 2, 2);
                    JSONObject c = sensorsArray.getJSONObject(i);
                    String camera_date = c.getString("camera_date");
                    String pir_name = c.getString("pir_name");
                    String camera_name = c.getString("camera_name");
                    String img = c.getString("img");
                    String babyimg = c.getString("babyimg");
                    TableRow tr1 = new TableRow(this);

                    TextView txt1 = new TextView(this);
                    TextView txt2 = new TextView(this);
                    TextView txt3 = new TextView(this);
                    TextView txt4 = new TextView(this);
                    txt1.setLayoutParams(aParams);
                    txt2.setLayoutParams(aParams);
                    txt3.setLayoutParams(aParams);
                    txt4.setLayoutParams(bParams);
                    txt1.setBackgroundColor(Color.WHITE);
                    txt2.setBackgroundColor(Color.WHITE);
                    txt3.setBackgroundColor(Color.WHITE);
                    txt4.setBackgroundColor(Color.WHITE);
                    txt1.setGravity(Gravity.CENTER);
                    txt2.setGravity(Gravity.CENTER);
                    txt3.setGravity(Gravity.CENTER);
                    txt4.setGravity(Gravity.CENTER);
                    txt1.setText(camera_date);
                    txt1.setTextSize(fontsize);
                    tr1.addView(txt1);
                    txt2.setText(pir_name);
                    txt2.setTextSize(fontsize);
                    tr1.addView(txt2);
                    txt3.setText(camera_name);
                    txt3.setTextSize(fontsize);
                    tr1.addView(txt3);
                    final String bigimg = server+"/"+img;

                    final String smallimg = server+"/"+babyimg;
                    final ImageView graf = new ImageView(this);

                    if (babyimg.contains("jpg"))
                    {
                        new LoadImage(graf).execute(smallimg);
                    }
                    graf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bigimg)));

                        }
                    });
                    tr1.addView(graf);
                    tl.addView(tr1);
                }
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
            imgv.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
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

