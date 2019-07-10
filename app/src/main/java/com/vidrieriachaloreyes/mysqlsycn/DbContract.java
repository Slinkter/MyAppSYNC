package com.vidrieriachaloreyes.mysqlsycn;

public class DbContract {

    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILIDE = 1;

    public static final String SERVER_URL = "https://vidrieriachaloreyes.com/php/syncinfo.php";
    public static final String UI_UPDATE_BROADCAST = "com.vidrieriachaloreyes.synctest.uiupdatebroadcast";

    public static final String DATABASE_NAME = "contactdb";
    public static final String TABLE_NAME = "contactsinfo";
    public static final String NAME = "name";
    public static final String SYNC_STATUS = "syncstatus";

}
