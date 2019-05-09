package com.zaf.econnecto.ui.interfaces;

import android.app.Activity;


public interface BackPressHandler {

    /**
     * Function to handle the back pressed event of the device hardware back button.
     * This function will be invoked at {@link Activity#onBackPressed()}
     *
     * @return true if the handler handle the event,
     * false to let the activity to handle the event
     */
    boolean onBackPressed();
}
