package ru.smart.smart9;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

//import ru.smart.smart9.R;


public class electro extends Activity {
    LinearLayout layout1;
    LinearLayout.LayoutParams params2;
    LinearLayout.LayoutParams params;
    JSONObject reader;
    LinearLayout tl;
    LinearLayout tl2;
    TableLayout tl3;
    JSONArray sensorsArray;
    Button btn ;
    Object objectserver = Model.instance().get_server_current();
    String server = objectserver.toString();
    String urlString  = server + "/android/get_electro.php?input=input";
    String urlString2 = server + "/android/get_electro_array.php?dev_param=T&dev_period=";//month";
    String urlString3  = server + "/android/get_electro_array.php?dev_param=H";
    //String urlString4  = server + "/android/get_electro_array.php?dev_param=U";
    int widthdisplay;
    int heightdisplay;
    private RadioGroup radioGroup;
    String period="day";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electro);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthdisplay = size.x;
        heightdisplay = size.y;
        layout1 = (LinearLayout) findViewById(R.id.linear_electro);
        btn = (Button) layout1.findViewById(R.id.button_refresh_electro);
        new ProcessJSON().execute(urlString);
        new ProcessJSON2().execute(urlString2+"day");
        new ProcessJSON3().execute(urlString3);
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

    private class ProcessJSON2 extends AsyncTask<String, Void, String>{
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
                customButton2(stream);
            }

        }
    }

    private class ProcessJSON3 extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }

        protected void onPostExecute(String stream){
            if(stream !=null){
                //btn.setText("ОБНОВИТЬ");
                customButton3(stream);
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
            tl=(LinearLayout)findViewById(R.id.linear_electro_info);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btn.setText("ОЖИДАНИЕ...");
                    tl.removeAllViews();
                    new ProcessJSON().execute(urlString);
                    new ProcessJSON2().execute(urlString2+"day");
                }
            });

            if (!stream.equalsIgnoreCase("Not_found")) {
                tl.setGravity(Gravity.CENTER);

                    int fontsize=16;
                    TextView text1 = new TextView(this);
                    text1.setText("Текущие показания");
                    text1.setTextColor(Color.MAGENTA );
                    text1.setTextSize(fontsize);
                    text1.setGravity(Gravity.CENTER);
                    tl.addView(text1);
                    JSONObject c = sensorsArray.getJSONObject(0);
                    String T1 = c.getString("T1");
                    String T2 = c.getString("T2");
                    String Uv = c.getString("Uv");
                    String Ia = c.getString("Ia");
                    TextView txt1 = new TextView(this);
                    TextView txt2 = new TextView(this);


                    txt1.setText("T1 = "+T1+"   T2 = "+T2);

                    txt2.setText("Uv = "+Uv+"   Ia = "+Ia);
                   // txt4.setText("Ia = "+Ia);
                    txt1.setTextSize(fontsize);
                    txt2.setTextSize(fontsize);
                    txt1.setTextColor(Color.MAGENTA);
                    txt2.setTextColor(Color.MAGENTA);


                    tl.addView(txt1);
                    tl.addView(txt2);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void customButton2(String stream) {

        try {
            if (!stream.equalsIgnoreCase("Not_found")) {
                reader = new JSONObject(stream);
                sensorsArray = reader.getJSONArray("sensors");
            }

            tl2=(LinearLayout)findViewById(R.id.graphContainer);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    btn.setText("ОЖИДАНИЕ...");
                    tl.removeAllViews();
                    tl2.removeAllViews();
                    new ProcessJSON().execute(urlString);
                    new ProcessJSON2().execute(urlString2+period);
                }
            });
            radioGroup = (RadioGroup) findViewById(R.id.radio_period_electro);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    btn.setText("ОЖИДАНИЕ...");
                    RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                    String text = checkedRadioButton.getText().toString();
                    period = "day";
                    if (text.equalsIgnoreCase("За сутки")) {
                        period = "day";
                    }
                    if (text.equalsIgnoreCase("За неделю")) {
                        period = "week";
                    }
                    if (text.equalsIgnoreCase("За месяц")) {
                        period = "month";
                    }
                    tl2.removeAllViews();

                    new ProcessJSON2().execute(urlString2+period);
                }
            });
            if (!stream.equalsIgnoreCase("Not_found")) {
                tl2.setGravity(Gravity.CENTER);
                LinearLayout Graf= (LinearLayout) findViewById(R.id.graphContainer);
                LineChart chart = new LineChart(this);
                LineChart Uchart = new LineChart(this);

                chart.getAxisLeft().setDrawGridLines(false);
                Uchart.getAxisLeft().setDrawGridLines(false);

                chart.setNoDataText("График энергопотребления");

                Graf.addView(chart,widthdisplay-50,widthdisplay-50);
                Graf.addView(Uchart,widthdisplay-50,widthdisplay-50);

                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<Entry> Uentries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();

                for (int i = 0; i < sensorsArray.length(); i++) {
                    JSONObject c = sensorsArray.getJSONObject(i);

                    String devf_date = c.getString("devf_date");
                    if ( (period.equalsIgnoreCase("week")) || (period.equalsIgnoreCase("month") )) {
                        devf_date = c.getString("elec_date");}


                    String Tot = c.getString("Tot");
                    entries.add(new Entry(Float.parseFloat(Tot), i));
                    labels.add(devf_date);

                    String Uv = c.getString("Uv");
                    Uentries.add(new Entry(Float.parseFloat(Uv), i));


                }
                LineDataSet dataset = new LineDataSet(entries, "кВт");
                dataset.setDrawCubic(true);
                dataset.setCircleRadius(0);
                dataset.setDrawCircleHole(false);
                dataset.setDrawStepped(false);
                dataset.setColor(Color.RED);
                dataset.setDrawValues(false);


                LineDataSet Udataset = new LineDataSet(Uentries, "Вольт");
                Udataset.setDrawCubic(true);
                Udataset.setCircleRadius(0);
                Udataset.setDrawCircleHole(false);
                Udataset.setDrawStepped(false);
                Udataset.setColor(Color.BLUE);
                Udataset.setDrawValues(false);


                LineData data = new LineData(labels, dataset);
                LineData Udata = new LineData(labels, Udataset);

                chart.setData(data);
                chart.setDescription("");
                chart.setBottom(50);
                chart.setNoDataText("График энергопотребления");

                Uchart.setData(Udata);
                Uchart.setDescription("");
                Uchart.setBottom(50);
                Uchart.setNoDataText("График напряжения");

                YAxis yAxis = chart.getAxisRight();

                float mm = yAxis.getAxisMaximum();
                if (mm < 1) {
                yAxis.setLabelCount(11,true);
                yAxis.setAxisMinValue(0);
                yAxis.setAxisMaxValue(1);
                yAxis.setValueFormatter(new MyYAxisValueFormatter());}

                XAxis xAxis = chart.getXAxis();
                xAxis.setTextSize(8f);
                //xAxis.setTextColor(Color.RED);
                xAxis.setLabelRotationAngle(-90);
                if (period.equalsIgnoreCase("day")) {
                xAxis.setSpaceBetweenLabels(2); }
                if (period.equalsIgnoreCase("week")) {
                    xAxis.setSpaceBetweenLabels(7); }
                if (period.equalsIgnoreCase("month")) {
                    xAxis.setSpaceBetweenLabels(1); }


                XAxis UxAxis = Uchart.getXAxis();
                UxAxis.setTextSize(8f);
                //xAxis.setTextColor(Color.RED);
                UxAxis.setLabelRotationAngle(-90);
                if (period.equalsIgnoreCase("day")) {
                    UxAxis.setSpaceBetweenLabels(2); }
                if (period.equalsIgnoreCase("week")) {
                    UxAxis.setSpaceBetweenLabels(7); }
                if (period.equalsIgnoreCase("month")) {
                    UxAxis.setSpaceBetweenLabels(1); }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void customButton3(String stream) {

        try {
            if (!stream.equalsIgnoreCase("Not_found")) {
                reader = new JSONObject(stream);
                sensorsArray = reader.getJSONArray("sensors");
            }

            tl3=(TableLayout) findViewById(R.id.table_electro_journal);


            if (!stream.equalsIgnoreCase("Not_found")) {
                tl3.setGravity(Gravity.CENTER);

                int fontsize=16;

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
                text2.setText("T1");
                tr.addView(text2);
                text3.setText("T2");
                tr.addView(text3);
                tl3.addView(tr);

                for (int i = 1; i < sensorsArray.length(); i++) {
                    params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(2, 2, 2, 2);
                    JSONObject c = sensorsArray.getJSONObject(i);
                    String dev_T1 = c.getString("T1");
                    String lastday = c.getString("lastday");
                    String dev_T2 = c.getString("T2");

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
                    txt1.setText(lastday);
                    txt1.setTextSize(fontsize);
                    tr1.addView(txt1);
                    txt2.setText(dev_T1);
                    txt2.setTextSize(fontsize);
                    tr1.addView(txt2);
                    txt3.setText(dev_T2);
                    txt3.setTextSize(fontsize);
                    tr1.addView(txt3);
                    tl3.addView(tr1);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class MyYAxisValueFormatter implements YAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("##.##"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            // write your logic here
            // access the YAxis object to get more information
            return mFormat.format(value);// + " $"; // e.g. append a dollar-sign
        }
    }
}



