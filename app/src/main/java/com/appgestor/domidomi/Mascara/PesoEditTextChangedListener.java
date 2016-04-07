package com.appgestor.domidomi.Mascara;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class PesoEditTextChangedListener implements TextWatcher {

    private String mMask;
    private EditText mEditText;
    private boolean isUpdating;
    private DecimalFormat formato;

    public PesoEditTextChangedListener(String mask, EditText editText) {
        mMask = mask;
        mEditText = editText;
        initSymbolMask();
    }

    private void initSymbolMask(){
        isUpdating = false;
        formato = new DecimalFormat(mMask);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        isUpdating = true;
        String str = s.toString();

        str = str.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");

        try {
            str = formato.format(Double.parseDouble(str));
            mEditText.setText(str);
            mEditText.setSelection(mEditText.getText().length());

        } catch (NumberFormatException e) {
            s = "";
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
