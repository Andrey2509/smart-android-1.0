package ru.smart.smart9;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

//import ru.smart.smart9.R;

public class settings extends Activity {
    EditText ed1,ed2,ed3,ed4;
    LinearLayout layout1;
    Button btn ;
    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        layout1 = (LinearLayout) findViewById(R.id.linear_settings);
        btn = (Button) layout1.findViewById(R.id.button_save_settings);


        ed1 = (EditText) findViewById(R.id.server_inet_edit);
        ed2 = (EditText) findViewById(R.id.server_local_edit);
        ed3 = (EditText) findViewById(R.id.username_edit);
        ed4 = (EditText) findViewById(R.id.userpassword_edit);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String server_inet_stored    = pref.getString("Server_inet", "No name defined");
        String server_local_stored   = pref.getString("Server_local", "No name defined");
        String username_stored       = pref.getString("Username", "No name defined");
        String userpassword_stored   = pref.getString("Password", "No name defined");

        ed1.setText(server_inet_stored);
        ed2.setText(server_local_stored);
        ed3.setText(username_stored);
        ed4.setText(userpassword_stored);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String server_inet_editc  = ed1.getText().toString();
                String server_local_editc = ed2.getText().toString();
                String username_editc     = ed3.getText().toString();
                String userpassword_editc = ed4.getText().toString();

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("Server_inet",server_inet_editc);
                editor.putString("Server_local",server_local_editc);
                editor.putString("Username",username_editc);
                editor.putString("Password",userpassword_editc);
                //editor.apply();
                editor.commit();

            }
        });



    }





}



