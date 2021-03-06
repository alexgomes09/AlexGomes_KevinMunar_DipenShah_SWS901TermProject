package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/29/2014.
 */
public class AdminPanel extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    Intent intent;

    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);

        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        intent = getIntent();

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);

        // Add Drawer Item to dataList


        if(intent.getStringExtra(Login.USER_TYPE).equals("1")) {
            dataList.add(new DrawerItem("Enter Vital Sign", R.drawable.enter_vital_signs));
            dataList.add(new DrawerItem("Get Vital Sign", R.drawable.get_vital_sign));
            dataList.add(new DrawerItem("Get Patient Location", R.drawable.patient_location));
            dataList.add(new DrawerItem("Send SMS", R.drawable.patient_location));
        }

        if(intent.getStringExtra(Login.USER_TYPE).equals("0")){
            dataList.add(new DrawerItem("Enter Vital Sign", R.drawable.enter_vital_signs));
            dataList.add(new DrawerItem("Exercise Video", R.drawable.patient_location));
            dataList.add(new DrawerItem("Patient Symptom", R.drawable.symptom));
        }


        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close) {
        Toolbar mytoolbar = new Toolbar(AdminPanel.this);
        mDrawerToggle = new ActionBarDrawerToggle(AdminPanel.this, mDrawerLayout,mytoolbar , R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void SelectItem(int possition) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        if(intent.getStringExtra(Login.USER_TYPE).equals("1")) {
            switch (possition) {
                case 0:
                    fragment = new EnterVitalSignsFragment();
                    break;
                case 1:
                    fragment = new GetVitalSignsFragment();
                    break;
                case 2:
                    fragment = new GetPatientLocation();
                    break;
                case 3:
                    fragment = new SendSMS();
                    break;
                default:
                    break;
            }
        }else if(intent.getStringExtra(Login.USER_TYPE).equals("0")){
            switch (possition) {
                case 0:
                    fragment = new EnterVitalSignsFragment();
                    break;
                case 1:
                    fragment = new PatientVideo();
                    break;
                case 2:
                    fragment = new PaitentSymptom();
                    break;
                default:
                    break;
            }
        }

        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                .commit();

        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);
        }
    }
}
