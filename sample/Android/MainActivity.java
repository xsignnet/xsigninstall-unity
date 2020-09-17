package com.projectdemo.xsigninstall;

import android.os.Bundle;
import android.util.Log;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity
        extends UnityPlayerActivity
{
    public static final int REQ_CODE_WRITE = 1;
    public static final int REQ_CODE_READ = 2;

    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e("MainActivity", "thie activity is run");
        super.onCreate(savedInstanceState);
        Xsigninstall.init(this, this);

        Xsigninstall.wakeup();
    }

    protected void onStart()
    {
        super.onStart();
    }

    protected void onResume()
    {
        super.onResume();
        Xsigninstall.installation();
    }
}
