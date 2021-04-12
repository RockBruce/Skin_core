package cn.edsmall.skin_core;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.File;

import cn.edsmall.skin_library.SkinManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File f = new File(getFilesDir(), "skin.apk");
        SysUtils.writeResponseBodyToDisk(f,this);
        SkinManager.getInstance().loadSkin(f.getAbsolutePath());
    }
}