package com.example.chopinxbp.eyestrackdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements Camera2BasicFragment.FragmentInteraction{

    private int eyesLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //人眼跟踪
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

    }

    @Override
    public void process(int location) {
        eyesLocation = location;
        Log.e("tag", "eyesLocation: " + eyesLocation);
    }
}
