package ru.smart.smart9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//import ru.smart.smart9.R;


public class MainActivity extends AppCompatActivity {
    TextView InetStatus,ServerInetStatus,ServerLocalStatus,NetStatus;
    SharedPreferences pref;
    Button checkbtn;
    public static final String MyPREFERENCES = "MyPrefs" ;
    LinearLayout layout1;
    int respcode;
    String server_inet_stored;
    String server_local_stored;
    String server_current_stored;
    String username_stored;
    String userpassword_stored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InetStatus          =( TextView )findViewById(R.id.inet_status);
        ServerInetStatus    =( TextView )findViewById(R.id.server_inet_status);
        ServerLocalStatus   =( TextView )findViewById(R.id.server_local_status);
        NetStatus           =( TextView )findViewById(R.id.net_status);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        server_inet_stored = pref.getString("Server_inet", "No name defined");
        server_local_stored = pref.getString("Server_local", "No name defined");
        server_current_stored = pref.getString("Server_current", server_inet_stored);
        username_stored = pref.getString("Username", "No name defined");
        userpassword_stored = pref.getString("Password", "No name defined");

        Model.instance().set_username(username_stored);
        Model.instance().set_userpassword(userpassword_stored);
        Model.instance().set_server_inet(server_inet_stored);
        Model.instance().set_server_local(server_local_stored);
        Model.instance().set_server_current(server_current_stored);
        Model.instance().set_netmode("inet");


        InetStatus.setText("Доступ к интернет: проверяется...");
        new MyAsyncTask("inet").execute("http://www.google.com");
        new MyAsyncTask("server_inet").execute(server_inet_stored);
        new MyAsyncTask("server_local").execute(server_local_stored);


        checkbtn = (Button) findViewById(R.id.button_check_connect);
        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                server_inet_stored = pref.getString("Server_inet", "No name defined");
                server_local_stored = pref.getString("Server_local", "No name defined");
                username_stored = pref.getString("Username", "No name defined");
                userpassword_stored = pref.getString("Password", "No name defined");

                Model.instance().set_username(username_stored);
                Model.instance().set_userpassword(userpassword_stored);
                Model.instance().set_server_inet(server_inet_stored);
                Model.instance().set_server_local(server_local_stored);
                Model.instance().set_server_current(server_current_stored);

                InetStatus.setText("Доступ к интернет: проверяется...");
                new MyAsyncTask("inet").execute("http://www.google.com");
                new MyAsyncTask("server_inet").execute(server_inet_stored);
                new MyAsyncTask("server_local").execute(server_local_stored);
            }
        });


    }

    public void showbuttons( View view)
    {
        String button_test;
        button_test =((Button) view).getText().toString();
        if (button_test.equals("Состояние датчиков"))
        {
            Intent intent1= new Intent(this,ViewSensors.class);
            startActivity(intent1 );
        }

        else if (button_test.equals("Переключатели"))
        {
            Intent intent= new Intent(this,SwitchRelays.class);
            startActivity(intent );
        }
        else if (button_test.equals("Климат"))
        {
            Intent intent= new Intent(this,Climate.class);
            startActivity(intent );
        }
        else if (button_test.equals("Журнал переключений"))
        {
            Intent intent= new Intent(this,Journal_switches.class);
            startActivity(intent );
        }
        else if (button_test.equals("Журнал датчиков"))
        {
            Intent intent= new Intent(this,Journal_sensors.class);
            startActivity(intent );
        }
        else if (button_test.equals("Журнал снимков"))
        {
            Intent intent= new Intent(this,Journal_images.class);
            startActivity(intent );
        }
        else if (button_test.equals("Видеокамеры"))
        {
            Intent intent= new Intent(this,cameras.class);
            startActivity(intent );
        }
        else if (button_test.equals("Электроэнергия"))
        {
            Intent intent= new Intent(this,electro.class);
            startActivity(intent );
        }
        else if (button_test.equals("Настройки"))
        {
            Intent intent= new Intent(this,settings.class);
            startActivity(intent );
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        String servertype;
      MyAsyncTask(String servertype) {
            this.servertype = servertype;
        }
        @Override
        protected void onPreExecute()
        {
            respcode = 0;
        }
        protected Double doInBackground(String... params) {

            if (servertype.equalsIgnoreCase("inet") ) {
                CheckInet gg = new CheckInet();
                respcode = gg.GetHTTPResponse(params[0]);
            }
            if ( (servertype.equalsIgnoreCase("server_inet")) ||(servertype.equalsIgnoreCase("server_local")) ) {
                CheckServer gg = new CheckServer();
                respcode = gg.GetHTTPResponse(params[0]);
            }


                return null;

        }
        protected void onPostExecute(Double result){
            if ((respcode == 200 ) & servertype.equalsIgnoreCase("inet")) {
                InetStatus.setText("Доступ к интернет: Есть");
                InetStatus.setTextColor(Color.GREEN);
            }
            if ((respcode != 200 ) & servertype.equalsIgnoreCase("inet")) {
                InetStatus.setText("Доступ к интернет: Нет");
                InetStatus.setTextColor(Color.RED);
            }

            if ((respcode == 200 ) & servertype.equalsIgnoreCase("server_inet")) {
                ServerInetStatus.setText("Доступ к серверу через интернет: Есть");
                ServerInetStatus.setTextColor(Color.GREEN);
            }
            if ((respcode != 200 ) & servertype.equalsIgnoreCase("server_inet")) {
                ServerInetStatus.setText("Доступ к серверу через интернет: Нет");
                ServerInetStatus.setTextColor(Color.RED);
            }

            if ((respcode == 200 ) & servertype.equalsIgnoreCase("server_local")) {
                Model.instance().set_server_current(server_local_stored);
                Model.instance().set_netmode("local");
                NetStatus.setText("Режим подключения: ЛВС");
                ServerLocalStatus.setText("Доступ к серверу через ЛВС: Есть");
                ServerLocalStatus.setTextColor(Color.GREEN);
            }
            if ((respcode != 200 ) & servertype.equalsIgnoreCase("server_local")) {
                Model.instance().set_server_current(server_inet_stored);
                Model.instance().set_netmode("inet");
                NetStatus.setText("Режим подключения: Интернет");
                ServerLocalStatus.setText("Доступ к северу через ЛВС: Нет");
                ServerLocalStatus.setTextColor(Color.RED);
            }

        }
        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
        }
    }
}