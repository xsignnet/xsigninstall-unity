package com.projectdemo.xsigninstall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.xsigninstallsdk.XSignInstall;
import com.project.xsigninstallsdk.inter.ConfigCallBack;
import com.unity3d.player.UnityPlayer;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Xsigninstall
{
    private static Context _context = null;
    private static Activity _activity = null;
    public static final String xSigninstallCallbackObject = "XSignInstall";
    public static final String xSignInstallCallbackMethod = "_installCallback";
    public static final String xSignWakeUpCallbackMethod = "_wakeupCallback";
    public static final int REQ_CODE_WRITE = 1;
    public static final int REQ_CODE_READ = 2;

    public static void init(Context context, Activity activity)
    {
        _context = context;
        _activity = activity;
    }

    public static void register()
    {
        new XSignInstall(_context, "register");
    }

    public static void installation() {
        checkNeedPermission();
    }

    public static void checkNeedPermission()
    {
        int checkWritePermission = ContextCompat.checkSelfPermission(_context, "android.permission.WRITE_EXTERNAL_STORAGE");
        int checkReadPermission = ContextCompat.checkSelfPermission(_context, "android.permission.READ_EXTERNAL_STORAGE");
        if (checkWritePermission != 0) {
            ActivityCompat.requestPermissions(_activity, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, 1);
        }
        if (checkReadPermission != 0) {
            ActivityCompat.requestPermissions(_activity, new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 2);
        }
        if ((checkWritePermission == 0) && (checkReadPermission == 0))
        {
            Log.e("Xsigninstall", "Installation_with_parameters run");
            new XSignInstall(_context, "Installation_with_parameters");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                whenBack(grantResults);
                break;
            case 2:
                whenBack(grantResults);
        }
    }

    private void whenBack(@NonNull int[] grantResults)
    {
        if ((grantResults.length > 0) && (grantResults[0] == 0))
        {
            Log.e("Xsigninstall", "Installation_with_parameters run");
            new XSignInstall(_context, "Installation_with_parameters");
        }
    }

    public static void getInstall()
    {
        Log.e("getInstall", "getInstall run");
        XSignInstall get_config = new XSignInstall(_context, "Get_config", new ConfigCallBack()
        {
            public void OnSuccess(Map configinfo)
            {
                String json = new JSONObject(configinfo).toString();
                UnityPlayer.UnitySendMessage(xSigninstallCallbackObject, xSignInstallCallbackMethod, json);
            }

            public void RouseCallBack(Map rouse) {}
        });
        get_config = null;
    }

    public static void wakeup()
    {
        Uri uri = _activity.getIntent().getData();
        Log.e("wakeup", "wakeupCALL");
        if (uri != null)
        {
            String uriString = uri.toString();
            Log.e("wakeup", "uriString: " + uriString);
            if (_context == null) {
                Log.e("wakeup", "_context is null");
            }
            XSignInstall localXSignInstall = new XSignInstall(_context, uriString, new ConfigCallBack()
            {
                public void OnSuccess(Map map) {}

                public void RouseCallBack(Map map)
                {
                    String json = new JSONObject(map).toString();
                    Log.e("RouseCallBack", "json: " + json);



                    new Handler().postDelayed(new Runnable(){
                        public void run(){
                            //execute the task
                            UnityPlayer.UnitySendMessage(xSigninstallCallbackObject, xSignWakeUpCallbackMethod, json);
                        }
                    },500);


                }
            });
        }
    }

    public static String changeToJson(String arg1, String... arg2)
    {
        JSONObject object = new JSONObject();
        try
        {
            if (arg2 == null)
            {
                object.put("rouse", arg1);
            }
            else
            {
                object.put("installId", arg1);

                object.put("pbData", arg2);
            }
        }
        catch (JSONException localJSONException) {}
        return object.toString();
    }
}
