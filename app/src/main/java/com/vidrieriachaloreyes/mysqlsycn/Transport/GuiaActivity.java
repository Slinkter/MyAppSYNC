package com.vidrieriachaloreyes.mysqlsycn.Transport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vidrieriachaloreyes.mysqlsycn.R;

public class GuiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_guia);
    }
}
