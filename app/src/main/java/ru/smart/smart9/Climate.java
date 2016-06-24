package ru.smart.smart9;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



public class Climate extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params;
    Object objectusername = Model.instance().get_username();
    String username = objectusername.toString();

    Object objectuserpassword = Model.instance().get_userpassword();
    String userpassword = objectuserpassword.toString();
    String sendcode = username+":"+userpassword;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();

    String urlString = server+"/android/climate.php?input=climate";
    final Context context = this;
    int respcode = 0;



    JSONArray sensorsArray;
    JSONObject reader;
    LinearLayout tl;

    int widthdisplay;
    int  heightdisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.climateset);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthdisplay = size.x;
        heightdisplay = size.y;


        new ProcessJSON().execute(urlString);
    }
    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }
        protected void onPostExecute(String stream) {
            if (stream != null) {

                customButton(stream);
            }
        }
    }

    public void customButton(String stream) {


        try {

            reader = new JSONObject(stream);
            sensorsArray = reader.getJSONArray("sensors");
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            layout1 = (LinearLayout) findViewById(R.id.climateset);
            tl=(LinearLayout)findViewById(R.id.climatebut);
            Button btn = (Button)layout1.findViewById(R.id.button_c);
            final int id_ = btn.getId();
            btn = ((Button) findViewById(id_));
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    tl.removeAllViews();
                    new ProcessJSON().execute(urlString);
                }
            });

            final ArrayList<String> results = new ArrayList<String>();
            final ArrayList<String> temparray = new ArrayList<String>();
            final ArrayList<String> managarray = new ArrayList<String>();
            final ArrayList<String> current_temp = new ArrayList<String>();
            for(int i = 0; i < sensorsArray.length(); i ++){
                JSONObject c = sensorsArray.getJSONObject(i);
                String title = c.getString("key_title");
                String temp =  c.getString("temperature");
                String managable = c.getString("managable");
                String dev_value = c.getString("dev_value");
                results.add(title);
                temparray.add(temp);
                managarray.add(managable);
                current_temp.add(dev_value);
            }
            int count=results.size();
            ImageView[] graf = new ImageView[results.size()];
            final Spinner[] spinner = new Spinner[results.size()];
            for(int i=0;i<count;i++)
            {
                final int Index = i;


                final String showtemp = current_temp.get(i)+ "C";

                final String str=results.get(i)+ ", температура : ";

                String temptext = "<font color=\"blue\">"+str+"</font> <font color=\"red\">"+showtemp+"</font>";

                LinearLayout tr1 = new LinearLayout(this);
                LinearLayout tr2 = new LinearLayout(this);

                LinearLayout tr3 = new LinearLayout(this);
                LinearLayout tr4 = new LinearLayout(this);


                TextView textview = new TextView(this);

                textview.setText(Html.fromHtml(temptext));
                JSONObject d = sensorsArray.getJSONObject(i);

                final String grafik = d.getString("graf");
                graf[Index] = new ImageView(this);
                graf[Index].setScaleType(ImageView.ScaleType.MATRIX);

                String urlgraf2 =server+"/graph.php?dev_cl="+grafik;
                new LoadImage(graf[Index]).execute(urlgraf2);
                final String manag = d.getString("managable");

                tr1.addView(textview);
                tl.addView(tr1);

                if (manag.equalsIgnoreCase("1")) {
                    final TextView settemp = new TextView(this);
                    settemp.setText("Установить температуру:");
                    settemp.setTextColor(Color.BLUE);
                    final String label = d.getString("key_label");
                    final String strold = d.getString("temperature");
                    spinner[Index] = new Spinner(this);
                    final List<String> spinnerArray = new ArrayList<String>();
                    spinnerArray.add("0");
                    spinnerArray.add("5");
                    spinnerArray.add("10");
                    spinnerArray.add("15");
                    spinnerArray.add("20");
                    spinnerArray.add("22");
                    spinnerArray.add("24");
                    spinnerArray.add("26");
                    if (label.equalsIgnoreCase("sauna")) {
                        spinnerArray.clear();
                        spinnerArray.add("0");
                        spinnerArray.add("50");
                        spinnerArray.add("55");
                        spinnerArray.add("60");
                        spinnerArray.add("65");
                        spinnerArray.add("70");
                        spinnerArray.add("75");
                        spinnerArray.add("80");
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);



                    int spinnerPosition = spinnerArrayAdapter.getPosition(strold);
                    spinner[Index].setAdapter(spinnerArrayAdapter);
                    spinner[Index].setSelection(spinnerPosition);
                    final int oldposition = spinnerPosition;
                    spinner[Index].post(new Runnable() {
                        @Override
                        public void run() {
                            spinner[Index].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(
                                        AdapterView<?> parent, View view, int position, long id) {
                                    settemp.setText("Устанавливается...");
                                    final String urlstring = server+"/android/inserttemp.php";

                                    final String strpos = spinnerArray.get(position);
                                    new settemptask(position, oldposition, settemp, label, strpos, spinner[Index]).execute(urlstring);
                                }

                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    });
                    tr2.addView(settemp);
                    tr2.addView(spinner[Index]);
                    tl.addView(tr2);


                }

                tr3.addView(graf[Index]);
                tl.addView(tr3);
                View v = new View(this);
                v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50));
                v.setBackgroundResource(R.color.back);
                tr4.addView(v);
                tl.addView(tr4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class settemptask extends AsyncTask<String, Integer, Double>{
        Spinner spn;
        int position;
        int oldposition;
        TextView settemp;
        String label;
        String urlParameters;
        String strpos;
        settemptask(int position, int oldposition,  TextView settemp, String label, String strpos, Spinner spn) {

            this.position = position;
            this.oldposition = oldposition;
            this.settemp =settemp;
            this.spn = spn;
            this.label = label;
            this.strpos = strpos;

        }
        @Override
        protected void onPreExecute()
        {
            try
            {
                urlParameters =
                        "key_label=" + URLEncoder.encode(label, "UTF-8") +
                                "&temperature=" + URLEncoder.encode(strpos, "UTF-8");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        protected Double doInBackground(String... params) {
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result){
            if ((respcode == 200) ) {
            }
            if ((respcode != 200)) {
                spn.setSelection(oldposition);
            }
            settemp.setText("Установить температуру:");

        }
        protected void onProgressUpdate(Integer... progress){

        }

        public void postData(String urlString) {
            //String response = "";
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                final String basicAuth = "Basic " + Base64.encodeToString(sendcode.getBytes(), Base64.NO_WRAP);
                urlConnection.setRequestProperty("Authorization", basicAuth);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                urlConnection.setRequestProperty("Content-Language", "en-US");
                urlConnection.setUseCaches (false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream (
                        urlConnection.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response1 = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response1.append(line);
                    response1.append('\r');
                }
                rd.close();
                response1.toString();
                urlConnection.connect();
                respcode = urlConnection.getResponseCode();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

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
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                bitmap = BitmapScaler.scaleToFitWidth(bitmap, widthdisplay-50);
                stream.close();
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


