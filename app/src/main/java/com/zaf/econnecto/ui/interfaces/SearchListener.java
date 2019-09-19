package com.zaf.econnecto.ui.interfaces;

import android.text.Editable;

public interface SearchListener {

    void beforeTextChanged(CharSequence s, int start, int count, int after);

    void onTextChanged(CharSequence s, int start, int before, int count);

    void afterTextChanged(Editable s);

}
