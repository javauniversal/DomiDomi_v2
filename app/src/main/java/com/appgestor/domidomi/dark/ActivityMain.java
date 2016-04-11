package com.appgestor.domidomi.dark;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.appgestor.domidomi.Activities.ActEstadoPedido;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.Services.MyService;
import com.appgestor.domidomi.mockedFragments.FragListEmpresas;
import com.appgestor.domidomi.mockedFragments.FragmentCarrito;
import com.appgestor.domidomi.mockedFragments.FragmentPeril;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLatitudStatic;
import static com.appgestor.domidomi.Entities.UbicacionPreferen.setLongitudStatic;


public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private FragListEmpresas establecimientoF;
    private FragmentPeril perfilF;
    private FragmentCarrito carritoF;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        toolbar = (Toolbar) findViewById(R.id.toolbarhome);
        toolbar.setTitle("Buscando Establecimientos");
        setSupportActionBar(toolbar);

        FragmentManager fManager = getFragmentManager();
        establecimientoF = new FragListEmpresas();

        fManager.beginTransaction().replace(R.id.contentPanel, establecimientoF.newInstance(toolbar)).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Pulse otra vez para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fManager = getFragmentManager();

        if (id == R.id.nav_establecimiento) {
            toolbar.setTitle("Buscando Establecimientos");
            if (establecimientoF == null)
                establecimientoF = new FragListEmpresas();

            fManager.beginTransaction().replace(R.id.contentPanel, establecimientoF.newInstance(toolbar)).commit();
        } else if (id == R.id.nav_seguimiento) {
            startActivity(new Intent(this, ActEstadoPedido.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.nav_carrito) {
            toolbar.setTitle("Carrito");
            if (carritoF == null)
                carritoF = new FragmentCarrito();

            fManager.beginTransaction().replace(R.id.contentPanel, carritoF).commit();
        } else if (id == R.id.nav_perfil) {
            toolbar.setTitle("Perfiles");
            if (perfilF == null)
                perfilF = new FragmentPeril();

            fManager.beginTransaction().replace(R.id.contentPanel, perfilF).commit();
        } else if (id == R.id.nav_share) {
            shareTextUrl();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Descarga la mejor aplicación de domicilios");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.appgestor.domidomi&hl=es");

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public void onResume(){
        super.onResume();
        MyService gps = new MyService(this);
        if (gps.canGetLocation()){
            setLatitudStatic(gps.getLatitude());
            setLongitudStatic(gps.getLongitude());
        }
    }

}
