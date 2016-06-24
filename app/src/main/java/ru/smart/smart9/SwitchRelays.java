package ru.smart.smart9;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import ru.smart.smart9.R;

public class SwitchRelays extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();

    String keyswitch = server+"/key.php?";
    String urlString = server+"/android/getsensors.php?input=keys";

    //Object objectusername = Model.instance().get_username();
   // String username = objectusername.toString();

    //Object objectuserpassword = Model.instance().get_userpassword();
    //String userpassword = objectuserpassword.toString();
    //String sendcode = username+":"+userpassword;

    int respcode = 0;
    int typepress;
    JSONArray sensorsArray;
    JSONObject reader;
    TableLayout tl;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switches);
        layout1 = (LinearLayout) findViewById(R.id.linear_switches);
        btn = (Button)layout1.findViewById(R.id.button_refresh_switches);
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
                btn.setText("ОБНОВИТЬ");
                customButton(stream);
            }
        }
    }

    public void customButton(String stream) {

            try {
                reader = new JSONObject(stream);
                sensorsArray = reader.getJSONArray("sensors");
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);

                tl=(TableLayout)findViewById(R.id.table_switches);


                final int id_ = btn.getId();
                btn = ((Button) findViewById(id_));
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        btn.setText("ОЖИДАНИЕ...");
                        tl.removeAllViews();
                        new ProcessJSON().execute(urlString);

                    }
                });


                for(int i = 0; i < sensorsArray.length(); i ++){
                    JSONObject c = sensorsArray.getJSONObject(i);
                    String title = c.getString("key_title");
                    final int status = Integer.parseInt(c.getString("key_pio"));
                    final String label = c.getString("key_label");
                    final int Index = i;
                    int fontsize=16;
                    TableRow tr = new TableRow(this);
                    tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    TextView textview = new TextView(this);
                    textview.setText(title);
                    textview.setTextSize(fontsize);
                    tr.addView(textview);
                    final Button buttn = new Button(this);
                    if (status == 1){
                    buttn.setText("     ON     ");
                    buttn.setTextColor(Color.GREEN);}
                    else {
                        buttn.setText("    OFF     ");
                        buttn.setTextColor(Color.RED);
                    }
                    buttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (buttn.getText() != "Switching..."){

                                typepress = -1;
                            respcode = 0;
                            if (buttn.getText() == "     ON     ") {
                                typepress = 0;
                            } else {
                                typepress = 1;
                            }
                            buttn.setText("Switching...");
                            buttn.setTextColor(Color.BLUE);
                            final String urlstring = keyswitch + "key_label=" + label + "&key_pio=" + typepress;
                            new MyAsyncTask(typepress, buttn).execute(urlstring);
                            }
                        }
                    });
                    tr.addView(buttn);
                    tl.addView(tr, new TableLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
        Button btn;
        int typepress;
        long startTime, endTime, delta;

        MyAsyncTask(int typepress, Button btn) {
            this.btn = btn;
            this.typepress = typepress;
         }
     @Override
        protected void onPreExecute()
        {
           startTime = System.currentTimeMillis();
        }
        protected Double doInBackground(String... params) {
            //postData(params[0]);
            HTTPGet gg = new HTTPGet();
            respcode = gg.GetHTTPResponse(params[0]);

            return null;
        }

        protected void onPostExecute(Double result){
            endTime= System.currentTimeMillis();
            delta=endTime-startTime;
            Toast.makeText(getApplicationContext(), "onPostExecute complete:" + respcode+ "Wait:" + delta,
                    Toast.LENGTH_SHORT).show();
            if ((respcode == 200) && typepress == 0) {
                btn.setText("    OFF     ");
                btn.setTextColor(Color.RED);
            }
            if ((respcode == 200) && typepress == 1) {
                btn.setText("     ON     ");
                btn.setTextColor(Color.GREEN);
            }
            typepress = -1;

        }
        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
        }



    }




}


