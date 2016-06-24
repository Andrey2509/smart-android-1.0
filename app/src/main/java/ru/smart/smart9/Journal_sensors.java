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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import ru.smart.smart9.R;


public class Journal_sensors extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params2;
    LinearLayout.LayoutParams params;
    JSONObject reader;
    TableLayout tl;
    private RadioGroup radioGroup;

    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();
    String urlString  = server+"/android/journal.php?input=keys_hist_sensors";
    JSONArray sensorsArray;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
        layout1 = (LinearLayout) findViewById(R.id.linear_journal);
        btn = (Button) layout1.findViewById(R.id.button_refresh_journal);
        new ProcessJSON().execute(urlString+"&period=day");
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
               radioGroup = (RadioGroup) findViewById(R.id.radio_period_journal);
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

            tl=(TableLayout)findViewById(R.id.table_journal);
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
                TableRow.LayoutParams bParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                bParams.topMargin = 3;
                TextView text1 = new TextView(this);
                TextView text2 = new TextView(this);
                TextView text3 = new TextView(this);
                text1.setLayoutParams(aParams);
                text2.setLayoutParams(aParams);
                text3.setLayoutParams(bParams);
                text1.setBackgroundColor(Color.WHITE);
                text2.setBackgroundColor(Color.WHITE);
                text3.setBackgroundColor(Color.WHITE);
                text1.setTypeface(null, Typeface.BOLD);
                text2.setTypeface(null, Typeface.BOLD);
                text3.setTypeface(null, Typeface.BOLD);
                text1.setGravity(Gravity.CENTER);
                text2.setGravity(Gravity.CENTER);
                text3.setGravity(Gravity.CENTER);
                text1.setText("Дата");
                tr.addView(text1);
                text2.setText("Имя устройства");
                tr.addView(text2);
                text3.setText("Статус");
                tr.addView(text3);
                tl.addView(tr);
                String key_i_pio;
                for (int i = 0; i < sensorsArray.length(); i++) {
                    params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(2, 2, 2, 2);
                    JSONObject c = sensorsArray.getJSONObject(i);
                    String key_title = c.getString("key_title");
                    String key_j_date = c.getString("key_j_date");
                    String pio = c.getString("key_i_pio");
                    if (pio.equalsIgnoreCase("1")) {
                        key_i_pio = "Вкл";
                    } else {
                        key_i_pio = "Откл";
                    }
                    TableRow tr1 = new TableRow(this);
                    TextView txt1 = new TextView(this);
                    TextView txt2 = new TextView(this);
                    TextView txt3 = new TextView(this);
                    txt1.setLayoutParams(aParams);
                    txt2.setLayoutParams(aParams);
                    txt3.setLayoutParams(bParams);
                    txt1.setBackgroundColor(Color.WHITE);
                    txt2.setBackgroundColor(Color.WHITE);
                    txt3.setBackgroundColor(Color.WHITE);
                    txt1.setGravity(Gravity.CENTER);
                    txt2.setGravity(Gravity.CENTER);
                    txt3.setGravity(Gravity.CENTER);
                    txt1.setText(key_j_date);
                    txt1.setTextSize(fontsize);
                    tr1.addView(txt1);
                    txt2.setText(key_title);
                    txt2.setTextSize(fontsize);
                    tr1.addView(txt2);
                    txt3.setText(key_i_pio);
                    if (key_i_pio.equalsIgnoreCase("Вкл")) {
                        txt3.setTextColor(Color.GREEN);
                    } else {
                        txt3.setTextColor(Color.RED);
                    }
                    txt3.setTextSize(fontsize);
                    tr1.addView(txt3);
                    tl.addView(tr1);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





}

