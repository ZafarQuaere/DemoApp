
package com.zaf.econnecto.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public final class KeyboardUtils {

    private KeyboardUtils() {
        // This utility class is not publicly instantiable
    }


    public static void hideKeyboard(Context context) {
        showKeyboard(context, false);
    }

    public static void showKeyboard(Context context) {
        showKeyboard(context, true);
    }

    private static void showKeyboard(Context context, boolean show) {
        if (!(context instanceof Activity)) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        View currentFocus = ((Activity) context).getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        if (show) {
            inputManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT);
        } else {
            inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
