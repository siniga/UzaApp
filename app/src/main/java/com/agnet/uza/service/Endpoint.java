package com.agnet.uza.service;

/**
 * Created by Peter Khamis on 5/23/2017.
 */

/**
 * Created by Peter Khamis on 5/22/2017.
 */

public class Endpoint {

    //  private static String root = "http://lete.aggreyapps.com/api/public/index.php/api/auth/";
    // private static String storage = "http://lete.aggreyapps.com/api/storage/app/";

    private static String root = "https://7065fda925bf.ngrok.io/uza_app/api/public/index.php/api/";
    private static String storage = "https://7065fda925bf.ngrok.io /uza_app/api/public/index.php/api/storage/app/";

    private static String completeUrl;

    public static void setUrl(String url) {
        completeUrl = root + url;
    }

    public static String getUrl() {
        return completeUrl;
    }

    public static void setStorageUrl(String storageUrl) {
        completeUrl = storage + storageUrl;
    }

    public static String getStorageUrl() {
        return completeUrl;
    }
}

