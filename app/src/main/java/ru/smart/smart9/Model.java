package ru.smart.smart9;

/**
 * Created by gts on 15.04.2016.
 */
public class Model {
    private static Model __instance;

    private Model() {
    }

    public static Model instance() {
        if (__instance == null) {
            __instance = new Model();
        }
        return __instance;
    }




    private Object server_inet = null;
    public void set_server_inet(Object server_inet) {
        this.server_inet = server_inet;
    }
    public Object get_server_inet() {
        return server_inet;
    }

    private Object server_local = null;
    public void set_server_local(Object server_local) {
        this.server_local = server_local;
    }
    public Object get_server_local() {
        return server_local;
    }

    private Object server_current = null;
    public void set_server_current(Object server_current) {
        this.server_current = server_current;
    }
    public Object get_server_current() {
        return server_current;
    }

    private Object username = null;
    public void set_username(Object username) {
        this.username = username;
    }
    public Object get_username() {
        return username;
    }

    private Object userpassword = null;
    public void set_userpassword(Object userpassword) {
        this.userpassword = userpassword;
    }
    public Object get_userpassword() {
        return userpassword;
    }


    private Object netmode = null;
    public void set_netmode(Object netmode) {
        this.netmode = netmode;
    }
    public Object get_netmode() {
        return netmode;
    }


}
