package com.appgestor.domidomi.mockedActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.R;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Exit();

    }

    public void Exit(){
        new MaterialDialog.Builder(this)
                .title(R.string.title)
                .content(R.string.content)
                .positiveText(R.string.agree)
                .backgroundColor(getResources().getColor(R.color.color_gris))
                .positiveColor(getResources().getColor(R.color.color_negro))
                .negativeColor(getResources().getColor(R.color.color_negro))
                .negativeText(R.string.disagree)
                .callback(new MaterialDialog.ButtonCallback() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        finishAffinity();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
}
