package com.zaf.econnecto.network_call;

public class VolleyUpload {

    private static final VolleyUpload ourInstance = new VolleyUpload();

    public static VolleyUpload getInstance() {
        return ourInstance;
    }

    private VolleyUpload() {
    }
}
