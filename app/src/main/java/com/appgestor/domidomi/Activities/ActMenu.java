package com.appgestor.domidomi.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.android.volley.RequestQueue;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.R;
import com.appgestor.domidomi.ViewPager.SlidingTabLayout;
import com.appgestor.domidomi.dark.ActivityMain;
import com.appgestor.domidomi.mockedFragments.FragMenu;


import static com.appgestor.domidomi.Entities.Sede.getSedeStaticNew;

public class ActMenu extends AppCompatActivity {

    private DBHelper mydb;
    private LayerDrawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getSedeStaticNew().getDescripcion());
        setSupportActionBar(toolbar);

        mydb = new DBHelper(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActMenu.this, ActivityMain.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new MyClasPagerAdapter(getSupportFragmentManager()));
        SlidingTabLayout mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_menu, menu);


        MenuItem item = menu.findItem(R.id.action_shop);

        // Obtener drawable del item
        icon = (LayerDrawable) item.getIcon();

        // Actualizar el contador
        Utils.setBadgeCount(this, icon, mydb.getCantidadProducto(getSedeStaticNew().getIdempresa(), getSedeStaticNew().getIdsedes()));

        return true;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_shop:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ActMenu.this, ActCar.class);
                bundle.putInt("sede", getSedeStaticNew().getIdsedes());
                bundle.putInt("empresa", getSedeStaticNew().getIdempresa());
                bundle.putString("paginacion", "sin_menu");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MyClasPagerAdapter extends FragmentPagerAdapter {

        String[] tabas;

        public MyClasPagerAdapter(FragmentManager fm) {
            super(fm);
            tabas = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabas[position];
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            arguments.putInt("position", position);
            FragMenu myFragment;
            myFragment = FragMenu.newInstance(arguments);

            return myFragment;
        }

        @Override
        public int getCount() {
            return tabas.length;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActMenu.this, ActivityMain.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Utils.setBadgeCount(this, icon, mydb.getCantidadProducto(getSedeStaticNew().getIdempresa(), getSedeStaticNew().getIdsedes()));
    }

}
