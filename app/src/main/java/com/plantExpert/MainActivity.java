package com.plantExpert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.plantExpert.UI.AskExpertFragment;
import com.plantExpert.UI.DetectFragment;
import com.plantExpert.UI.ProfileFragment;
import com.plantExpert.UI.SolutionsFragment;
import com.plantExpert.Validation.Authentication;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Toolbar myToolBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolBar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setSupportActionBar(myToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container , new DetectFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.item1){
                Toast.makeText(this, "Detect Screen Invoked", Toast.LENGTH_SHORT).show();
                openFragment(new DetectFragment());
                return true;
            } else if (item.getItemId() == R.id.item2) {
                Toast.makeText(this, "Ask Expert Screen Invoked", Toast.LENGTH_SHORT).show();
                openFragment(new AskExpertFragment());
                return true;
            } else if (item.getItemId() == R.id.item3) {
                Toast.makeText(this, "Solutions Screen Invoked", Toast.LENGTH_SHORT).show();
                openFragment(new SolutionsFragment());
                return true;
                
            } else if (item.getItemId() == R.id.item4) {
                Toast.makeText(this, "Profile Screen Invoked", Toast.LENGTH_SHORT).show();
                openFragment(new ProfileFragment());
               return true;
            }
            return false;
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu ,menu);
        return true;
    }
    private void openFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container , fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id  = item.getItemId();
        if (id == R.id.logOut){
            Intent intent = new Intent(getApplicationContext(), Authentication.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}