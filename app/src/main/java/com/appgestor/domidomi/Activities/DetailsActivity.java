package com.appgestor.domidomi.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.appgestor.domidomi.R;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    Toolbar activityToolbar;

    ProgressActivity progressActivity;

    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(DetailsActivity.this, YourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        activityToolbar = (Toolbar) findViewById(R.id.activityToolbar);
        progressActivity = (ProgressActivity) findViewById(R.id.progress);

        setToolbar();

        Drawable emptyDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_shopping_basket)
                .colorRes(android.R.color.white);

        Drawable errorDrawable = new IconDrawable(this, Iconify.IconValue.zmdi_wifi_off)
                .colorRes(android.R.color.white);

        Drawable DrawableData = new IconDrawable(this, Iconify.IconValue.zmdi_alert_triangle)
                .colorRes(android.R.color.white);


        //Add which views you don't want to hide. In this case don't hide the toolbar
        List<Integer> skipIds = new ArrayList<>();
        skipIds.add(R.id.activityToolbar);

        String state = getIntent().getStringExtra("STATE");
        switch (state) {
            case "LOADING":
                progressActivity.showLoading(skipIds);
                setTitle("Loading");
                break;
            case "EMPTY":
                progressActivity.showEmpty(DrawableData,
                        "No hay datos para mostrar",
                        "Tenemos problemas en recuperar la información inténtelo más tarde.", skipIds);
                setTitle("No hay datos");
                activityToolbar.setBackgroundColor(getResources().getColor(R.color.colorSinDatos));
                break;
            case "ERROR":
                progressActivity.showEmpty(errorDrawable,
                        "Sin conexión",
                        "No hemos podido establecer una conexión con nuestros servidores. Por favor, inténtalo de nuevo cuando esté conectado a Internet.", skipIds);
                //"Inténtalo de nuevo", errorClickListener, skipIds);
                setTitle("Error");
                activityToolbar.setBackgroundColor(getResources().getColor(R.color.colorSinDatos));
                break;
        }
    }

    private void setToolbar() {
        setSupportActionBar(activityToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Progress Activity");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
