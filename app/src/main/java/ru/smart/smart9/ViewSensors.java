package ru.smart.smart9;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import ru.smart.smart9.R;


public class ViewSensors extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params2;
    LinearLayout.LayoutParams params;
    JSONObject reader;
    TableLayout tl;
    JSONArray sensorsArray;
    Button btn ;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();
    String urlString  = server + "/android/getsensors.php?input=input";
       @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sensors);
           layout1 = (LinearLayout) findViewById(R.id.linear_sensors);
                   btn = (Button) layout1.findViewById(R.id.button_refresh_sensors);
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
            }

            }
        }

    public void customButton(String stream) {

        try {
            if (!stream.equalsIgnoreCase("Not_found")) {
                reader = new JSONObject(stream);
                sensorsArray = reader.getJSONArray("sensors");
            }
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(1, 1, 1, 1);
            tl=(TableLayout)findViewById(R.id.table_sensors);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btn.setText("ОЖИДАНИЕ...");
                    tl.removeAllViews();
                    new ProcessJSON().execute(urlString);

                }
            });

            if (!stream.equalsIgnoreCase("Not_found")) {
                tl.setGravity(Gravity.CENTER);
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
                gd.setGradientCenter(0.f, 1.f);
                gd.setLevel(2);
                int fontsize=16;
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
                text1.setLayoutParams(aParams);
                text2.setLayoutParams(bParams);
                text1.setBackgroundColor(Color.WHITE);
                text2.setBackgroundColor(Color.WHITE);
                text1.setTypeface(null, Typeface.BOLD);
                text2.setTypeface(null, Typeface.BOLD);
                text1.setGravity(Gravity.CENTER);
                text2.setGravity(Gravity.CENTER);
                text1.setText("      Датчик      ");
                tr.addView(text1);
                text2.setText("   Статус   ");
                tr.addView(text2);
                tl.addView(tr);
                final ArrayList<String> results = new ArrayList<String>();

                for (int i = 0; i < sensorsArray.length(); i++) {
                    params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(2, 2, 2, 2);
                    JSONObject c = sensorsArray.getJSONObject(i);
                    String key_title = c.getString("key_title");
                    String key_pio = c.getString("key_pio");
                    TableRow tr1 = new TableRow(this);
                    TextView txt1 = new TextView(this);
                    TextView txt2 = new TextView(this);
                    txt1.setLayoutParams(aParams);
                    txt2.setLayoutParams(bParams);
                    txt1.setBackgroundColor(Color.WHITE);
                    txt2.setBackgroundColor(Color.WHITE);
                    txt1.setGravity(Gravity.CENTER);
                    txt2.setGravity(Gravity.CENTER);
                    txt1.setText(key_title);
                    txt1.setTextSize(fontsize);
                    tr1.addView(txt1);
                    String status;
                    if (key_pio.equalsIgnoreCase("1")) {
                        status = "Ok";
                        txt2.setTextColor(Color.GREEN);
                    }
                    else {
                        status = "Check!";
                        txt2.setTextColor(Color.RED);
                    }

                    txt2.setText(status);
                    txt2.setTextSize(fontsize);
                    tr1.addView(txt2);
                    tl.addView(tr1);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}



