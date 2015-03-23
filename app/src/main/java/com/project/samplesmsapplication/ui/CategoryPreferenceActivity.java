package com.project.samplesmsapplication.ui;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.samplesmsapplication.R;

import java.util.ArrayList;

public class CategoryPreferenceActivity extends ActionBarActivity {
    
    private Toolbar toolbar;
    private ListView listCategoryPreferences;
    private ArrayList<String> arrLists;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_preferences);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Category Preferences");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.btn_settings_normal);
        }

        listCategoryPreferences = (ListView)findViewById(R.id.listCategoryPreferences);
        
    }
    @Override
    protected void onResume() {
        super.onResume();

        arrLists = new ArrayList<>();
        arrLists.add("A Category");
        arrLists.add("B Category");
        arrLists.add("C Category");
        arrLists.add("D Category");
        arrLists.add("E Category");
        arrLists.add("F Category");
        arrLists.add("G Category");
        arrLists.add("H Category");
        arrLists.add("I Category");
        arrLists.add("J Category");
        arrLists.add("K Category");

        MyCustomAdapter adapter = new MyCustomAdapter(CategoryPreferenceActivity.this,arrLists);
        listCategoryPreferences.setAdapter(adapter);

    }

    public class MyCustomAdapter extends BaseAdapter {
        private final Activity context;
        private final ArrayList<String> programs;


        public class ViewHolder {
            public TextView text;
            public ImageView image;

        }

        public MyCustomAdapter(Activity context, ArrayList<String> programs) {
            this.context = context;
            this.programs = programs;
        }

        @Override
        public int getCount() {
            return programs.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.categorysettings_customlistview, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.tv_settings_catname);
                viewHolder.image = (ImageView) rowView.findViewById(R.id.iv_status);
                rowView.setTag(viewHolder);
            }
            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String s = programs.get(position);
            holder.text.setText(s);

            return rowView;
        }
    }

}
