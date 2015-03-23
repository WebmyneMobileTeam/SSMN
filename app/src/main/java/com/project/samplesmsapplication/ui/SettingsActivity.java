package com.project.samplesmsapplication.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.audiofx.BassBoost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.samplesmsapplication.R;
import com.project.samplesmsapplication.helpers.Prefs;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener{

    private View itemSettingTheme;
    private View itemSettingFontSize;
    private View itemSettingFontType;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Settings");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_icon_home);
        }

        itemSettingTheme = (View)findViewById(R.id.itemSettingTheme);
        itemSettingTheme.setOnClickListener(this);

        itemSettingFontSize = (View)findViewById(R.id.itemSettingFontSize);
        itemSettingFontSize.setOnClickListener(this);

        itemSettingFontType = (View)findViewById(R.id.itemSettingFontType);
        itemSettingFontType.setOnClickListener(this);

        setupTheme();
        setupTextFontSize();
        setupTextFontType();
    }

    private void setupTheme() {

        TextView title = (TextView)itemSettingTheme.findViewById(R.id.txtTitleSettingItem);
        TextView subTitle = (TextView)itemSettingTheme.findViewById(R.id.txtSubTitleSettingItem);
        title.setText("Theme Style");
        subTitle.setText("Change Application Theme");

    }

    private void setupTextFontSize() {

        TextView title = (TextView)itemSettingFontSize.findViewById(R.id.txtTitleSettingItem);
        TextView subTitle = (TextView)itemSettingFontSize.findViewById(R.id.txtSubTitleSettingItem);
        title.setText("Font Size");
        subTitle.setText("Change text message font size");

    }

    private void setupTextFontType() {

        TextView title = (TextView)itemSettingFontType.findViewById(R.id.txtTitleSettingItem);
        TextView subTitle = (TextView)itemSettingFontType.findViewById(R.id.txtSubTitleSettingItem);
        title.setText("Font Type");
        subTitle.setText("Change text message font family");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.itemSettingFontType:
                displaySettingsOptionFontType();
                break;
            case R.id.itemSettingFontSize:
                displaySettingsOptionFontSize();
                break;

            case R.id.itemSettingTheme:
                displaySettingsOption();
                break;
        }
    }

    private void displaySettingsOption() {

        String[] items = {"Light (Day Mode)", "Dark (Night Mode)"};
        int selectedLanguage = Prefs.with(SettingsActivity.this).getInt("selected_theme", 0);
        AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingsActivity.this);

        builder3.setTitle("Pick your choice").setSingleChoiceItems(items, selectedLanguage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Prefs.with(SettingsActivity.this).save("selected_theme", which);

            }

        });

        builder3.show();
    }


    private void displaySettingsOptionFontSize() {

      final  String[] items = {"8","10","12","14","16","18","20","22","24","26","28","30"};
        ArrayList iTemp = new ArrayList(Arrays.asList(items));

        String selectedLanguage = Prefs.with(SettingsActivity.this).getString("selected_font_size", "18");

        AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingsActivity.this);

        builder3.setTitle("Pick your choice").setSingleChoiceItems(items,iTemp.indexOf(""+selectedLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Prefs.with(SettingsActivity.this).save("selected_font_size", items[which]);

            }

        });

        builder3.show();
    }

    private void displaySettingsOptionFontType() {

        final  String[] items = {"Default","Century Gothic","Trebuchet MS","Tw Cen MT"};
        ArrayList iTemp = new ArrayList(Arrays.asList(items));

        String selectedLanguage = Prefs.with(SettingsActivity.this).getString("selected_font_type", "Default");

        AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingsActivity.this);

        builder3.setTitle("Pick your choice").setSingleChoiceItems(items,iTemp.indexOf(""+selectedLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Prefs.with(SettingsActivity.this).save("selected_font_type",items[which]);

            }

        });

        builder3.show();
    }



}
