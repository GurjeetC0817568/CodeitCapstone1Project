package com.gurjeet.codeitcapstone1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.nav);
        navigationView.setOnItemSelectedListener(nav);
      //TODO: either sell or profile part add here
        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.profile);
            fragmentManager = getSupportFragmentManager();
            ProfileFragment profileFragment = new ProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentMainPart,profileFragment).commit();
        }

    }




    private BottomNavigationView.OnItemSelectedListener nav =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.sale:
                            fragment = new SellFragment();
                            break;

                        case R.id.home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.profile:
                              fragment = new ProfileFragment();
                            break;
                        case R.id.chat:
                            fragment = new ChatFragment();
                            break;
                        case R.id.call:
                            fragment = new ProfileFragment();//fragment = new Call();// for now to not crash the app
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMainPart,fragment).commit();
                    return true;

                }

            };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //on back press send to activity main
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}