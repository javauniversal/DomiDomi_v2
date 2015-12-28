package com.appgestor.domidomi.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
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

import com.appgestor.domidomi.R;
import com.appgestor.domidomi.ViewPager.SlidingTabLayout;
import com.appgestor.domidomi.mockedFragments.FragMenu;

import static com.appgestor.domidomi.Entities.Empresas.getEmpresastatic;
import static com.appgestor.domidomi.Entities.Sede.getSedeStatic;


public class ActMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getSedeStatic().getDescripcion());
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new MyClasPagerAdapter(getSupportFragmentManager()));
        SlidingTabLayout mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(getResources().getColor(R.color.color_1));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.color_negro));
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.action_settings:
                finishAffinity();
                return true;
            case R.id.action_cart:
                Bundle bundle = new Bundle();
                bundle.putInt("compania", getEmpresastatic().getIdempresa());
                startActivity(new Intent(ActMenu.this, ActCar.class).putExtras(bundle));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

}
