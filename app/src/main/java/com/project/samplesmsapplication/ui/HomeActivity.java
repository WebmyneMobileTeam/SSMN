package com.project.samplesmsapplication.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.samplesmsapplication.R;
import com.project.samplesmsapplication.helpers.Prefs;
import com.project.samplesmsapplication.ui.wigets.HalfHeightLayout;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    private HalfHeightLayout btnMessages;
    private RelativeLayout rl_language;
    private HalfHeightLayout btnFavourites;
    private RelativeLayout rl_exit;
    private HalfHeightLayout btnSettings;
    private RelativeLayout rl_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnMessages = (HalfHeightLayout) findViewById(R.id.btnMessages);
        btnMessages.setOnClickListener(msgClickListner);
        rl_language = (RelativeLayout) findViewById(R.id.rl_language);
        rl_language.setOnClickListener(this);
        btnFavourites = (HalfHeightLayout) findViewById(R.id.btnFavourites);
        btnFavourites.setOnClickListener(this);
        rl_exit = (RelativeLayout)findViewById(R.id.rl_exit);
        rl_exit.setOnClickListener(this);
        btnSettings = (HalfHeightLayout)findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);
        rl_search = (RelativeLayout)findViewById(R.id.rl_search);
        rl_search.setOnClickListener(this);


    }

    private View.OnClickListener msgClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i = new Intent(HomeActivity.this, CategoryHomeActivity.class);
            startActivity(i);

        }
    };


    private void ShowLanguageDialog() {

        String[] items = {"English", "Arabic"};

        int selectedLanguage = Prefs.with(HomeActivity.this).getInt("selected_language", 0);

        AlertDialog.Builder builder3 = new AlertDialog.Builder(HomeActivity.this);

        builder3.setTitle("Pick your choice").setSingleChoiceItems(items, selectedLanguage, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Prefs.with(HomeActivity.this).save("selected_language", which);
                Toast.makeText(HomeActivity.this, "" + which, Toast.LENGTH_SHORT).show();

            }
        });

        builder3.show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_search:
                Intent iSearch = new Intent(HomeActivity.this, SearchMainActivity.class);
                startActivity(iSearch);
                break;

            case R.id.btnSettings:
                Intent iSettings = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(iSettings);
                break;


            case R.id.rl_language:
                ShowLanguageDialog();
                break;

            case R.id.btnFavourites:

                Intent iFavourite = new Intent(HomeActivity.this, FavouriteMessageListActivity.class);
                startActivity(iFavourite);

                break;

            case R.id.rl_exit:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Exit");
                alert.setMessage("Are you sure you want to close the app?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();



                break;

        }

    }
}
