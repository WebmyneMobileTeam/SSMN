package com.project.samplesmsapplication.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    public static boolean isEnglish(Context ctx){
        boolean isEnglish = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_language",0);
        if(selectedLanguage == 0){
            isEnglish = true;
        }else{
            isEnglish = false;
        }
        return isEnglish;
    }

    public static boolean isLightTheme(Context ctx){
        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }
        return isLight;
    }

    public static String getBackGroudColor(Context ctx){

        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }

        String background = "";

        if(isLight == true){

            background = "#ffffff";
        }else{
            background = "#252525";
        }

        return background;

    }

    public static String getBackGroudColorText(Context ctx){

        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }

        String background = "";

        if(isLight == true){

            background = "#252525";
        }else{
            background = "#ffffff";
        }

        return background;

    }

    public static int getMessageFontSize(Context ctx){
        String selectedFontSize = Prefs.with(ctx).getString("selected_font_size", "18");
        int size = Integer.parseInt(selectedFontSize);
        return size;
    }

    public static Typeface getTypeFace(Context ctx){

        Typeface typeface = null;
        String selectedFontSize = Prefs.with(ctx).getString("selected_font_type", "Default");

        if(selectedFontSize.equalsIgnoreCase("Default")){
            typeface = Typeface.create("sans-serif",Typeface.NORMAL);
        }else if(selectedFontSize.equalsIgnoreCase("Century Gothic")){
            typeface = Typeface.createFromAsset(ctx.getAssets(),"century_gothic.TTF");
        }else if(selectedFontSize.equalsIgnoreCase("Trebuchet MS")){
            typeface = Typeface.createFromAsset(ctx.getAssets(),"trebuchet_ms.TTF");
        }else if(selectedFontSize.equalsIgnoreCase("Tw Cen MT")){
            typeface = Typeface.createFromAsset(ctx.getAssets(),"twcent.TTF");

        }

        return  typeface;

    }




}
