package com.vidrieriachaloreyes.mysqlsycn;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.vidrieriachaloreyes.mysqlsycn.Retrofit.ApiCliente;
import com.vidrieriachaloreyes.mysqlsycn.Retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText Name;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Contact> arrayList = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;

    ApiInterface retrofitAPI;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        Name = findViewById(R.id.name);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        readFromLocalStorage();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();
            }
        };

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

    }

    public void submitName(View view) {
        String name = Name.getText().toString();
        // saveToAppServer(name);
        saveToAppServer2(name);
        Name.setText("");
    }


    private void saveToAppServer2(String name) {
        progressDialog.show();

        final String insertName = name;
        retrofitAPI = ApiCliente.getConexion().create(ApiInterface.class);
        Call<Contact> call = retrofitAPI.insert(insertName);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(@NonNull Call<Contact> call, @NonNull Response<Contact> response) {
                progressDialog.dismiss();
                Log.e("onResponse ", " response = " + response);

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    Log.e("onResponse ", " response = " + response.body().getMessage());
                    Log.e("onResponse ", " success = " + success);
                    Log.e("onResponse ", " response = " + response);

                    if (success) {
                        saveToLocalStorage(insertName, DbContract.SYNC_STATUS_OK);
                    } else {
                        saveToLocalStorage(insertName, DbContract.SYNC_STATUS_FAILIDE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                progressDialog.dismiss();
                //  Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("onFailed", " error --> " + t.getLocalizedMessage());
                saveToLocalStorage(insertName, DbContract.SYNC_STATUS_FAILIDE);

            }
        });
    }

    private void readFromLocalStorage() {
        arrayList.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.readFromLocalDatabase(database);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
            int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));
            arrayList.add(new Contact(name, sync_status));
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();
    }

    public boolean checkNetworkConnection() {
        Log.e("checkNetworkConnection", " 123123 ");
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    private void saveToLocalStorage(String name, int sync) {

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        dbHelper.saveToLocalDatabase(name, sync, database);
        readFromLocalStorage();
        dbHelper.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(DbContract.UI_UPDATE_BROADCAST));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
