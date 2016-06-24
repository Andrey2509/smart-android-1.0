package ru.smart.smart9;

import android.app.Application;

import org.json.JSONArray;

/**
 * Created by gts on 24.03.2016.
 */
public class global extends Application {
    private JSONArray name;
    public JSONArray getName() {return name;
    };
    public void setName (JSONArray aName) {

        name = aName;

    }

}
