package com.project.samplesmsapplication.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.Path;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.caverock.androidsvg.SVG;
import com.eftimoff.androipathview.PathView;
import com.project.samplesmsapplication.R;


public class Launcher extends ActionBarActivity {
    
    PathView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        
       pathView = (PathView)findViewById(R.id.pathView);
        
       float length = 150;
       float height = 100;

        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(length, 0.0f);
        path.lineTo(length,height);
        path.lineTo(0.0f,height);
        path.lineTo(0.0f, 0.0f);
        
        path.lineTo(length/2.0f,length/3.0f);
        path.lineTo(length, 0.0f);

        path.close();
        
        pathView.setPath(path);
        pathView.getPathAnimator()
                .delay(100)
                .duration(2500)
                .listenerStart(null)
                .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                    @Override
                    public void onAnimationEnd() {
                        Intent i = new Intent(Launcher.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();
        
    }

}
