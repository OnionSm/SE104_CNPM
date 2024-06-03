package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager
{
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_NAME = "LoginSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    public SessionManager(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String username)
    {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn()
    {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername()
    {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
    }
}
