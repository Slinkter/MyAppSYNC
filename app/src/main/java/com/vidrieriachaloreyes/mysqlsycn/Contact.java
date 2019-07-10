package com.vidrieriachaloreyes.mysqlsycn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("status")
    private int Sync_status;

    // archivo php -->respuesta  despues del insert mysqli_query($con,$query)
    @Expose
    @SerializedName("success")
    private Boolean success;
    @Expose
    @SerializedName("message")
    private String message;
    //
    public Contact() {
    }

    public Contact(String name, int sync_status) {
        this.name = name;
        Sync_status = sync_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
