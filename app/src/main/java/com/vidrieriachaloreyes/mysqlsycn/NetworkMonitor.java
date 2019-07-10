package com.vidrieriachaloreyes.mysqlsycn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vidrieriachaloreyes.mysqlsycn.Retrofit.ApiCliente;
import com.vidrieriachaloreyes.mysqlsycn.Retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class NetworkMonitor extends BroadcastReceiver {

    ApiInterface retrofitAPI;


    @Override
    public void onReceive(final Context context, Intent intent) {

        if (checkNetworkConnection(context)) {
            final DbHelper dbHelper = new DbHelper(context);
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = dbHelper.readFromLocalDatabase(database);
            while (cursor.moveToNext()) {
                int sync_status = cursor.getInt(cursor.getColumnIndex(DbContract.SYNC_STATUS));
                if (sync_status == DbContract.SYNC_STATUS_FAILIDE) {
                    final String Name = cursor.getString(cursor.getColumnIndex(DbContract.NAME));
                    //
                    retrofitAPI = ApiCliente.getConexion().create(ApiInterface.class);
                    Call<Contact> call = retrofitAPI.insert(Name);
                    call.enqueue(new Callback<Contact>() {
                        @Override
                        public void onResponse(@NonNull Call<Contact> call, @NonNull retrofit2.Response<Contact> response) {
                            Log.e("onResponse ", " response = " + response);
                            if (response.isSuccessful() && response.body() != null) {
                                Boolean success = response.body().getSuccess();
                                if (success) {
                                    dbHelper.updateLocalDatabase(Name, DbContract.SYNC_STATUS_OK, database);
                                    context.sendBroadcast(new Intent(DbContract.UI_UPDATE_BROADCAST));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Contact> call, Throwable t) {
                            Log.e("onFailed", " error --> " + t.getLocalizedMessage());
                        }
                    });

                }
            }
           // dbHelper.close(); funciono en //
        }
    }


    public boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }


}
