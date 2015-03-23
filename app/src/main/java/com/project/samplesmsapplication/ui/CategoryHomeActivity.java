package com.project.samplesmsapplication.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.samplesmsapplication.R;
import com.project.samplesmsapplication.helpers.DatabaseHelper;
import com.project.samplesmsapplication.helpers.PrefUtils;
import com.project.samplesmsapplication.model.Category;

import java.util.ArrayList;

public class CategoryHomeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView listCategory;
    private ArrayList<Category> categories;
    private int selectedItem;
    private DatabaseHelper dbHelper;
    public ProgressDialog progressDialog;
    private ImageView globalImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_home);
        globalImageview = (ImageView) findViewById(R.id.globalImageView);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Category");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_icon_home);

        }
        listCategory = (ListView)findViewById(R.id.listCategory);
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent iMessages = new Intent(CategoryHomeActivity.this,MessageListActivity.class);
                iMessages.putExtra("selected_category",categories.get(position)._id);
                startActivity(iMessages);
            }
        });

        new fetchCategories().execute();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_category_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(CategoryHomeActivity.this, CategoryPreferenceActivity.class);
                startActivity(i);

                break;

            case R.id.action_sorting:

                openPreferences();

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void openPreferences() {

        final CharSequence[] digitList = {"Newest", "Oldest", "Smallest", "Largest", "High Rating", "Low Rating", "Most Liked", "Most Hated"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setIcon(R.drawable.ic_launcher);
        alt_bld.setTitle("Sort Messages By");
        selectedItem = -1;

        alt_bld.setSingleChoiceItems(digitList, 1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        selectedItem = item;
                    }
                });
        alt_bld.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "Selected digit:" + digitList[selectedItem],
                        Toast.LENGTH_SHORT).show();

            }
        });
        alt_bld.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = alt_bld.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        updatePreference();

    }

    private void updatePreference() {
        globalImageview.setBackgroundColor(Color.parseColor(PrefUtils.getBackGroudColor(CategoryHomeActivity.this)));
    }

    private class fetchCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog = ProgressDialog.show(CategoryHomeActivity.this,"Please wait","Fetching Categories",false,true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            dbHelper = new DatabaseHelper(CategoryHomeActivity.this);
            categories = dbHelper.getAllCategories();
            dbHelper.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  progressDialog.dismiss();
            fillCategories();
        }
    }

    private void fillCategories() {

        MyCustomAdapter adapter = new MyCustomAdapter(CategoryHomeActivity.this, categories);
        listCategory.setAdapter(adapter);
    }

    public class MyCustomAdapter extends BaseAdapter {

        private final Activity context;
        private final ArrayList<Category> programs;

        public class ViewHolder {

            public TextView text;
            public ImageView image;
            public TextView txtMessageCountCategory;
            public ImageView imgBesideText;

        }

        public MyCustomAdapter(Activity context, ArrayList<Category> programs) {
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

                int layout = 0;
                if(PrefUtils.isEnglish(CategoryHomeActivity.this)){
                    layout = R.layout.item_category_list_eng;
                }else{
                    layout = R.layout.item_category_list;
                }
                rowView = inflater.inflate(layout, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.tcCategoryName);
                viewHolder.image = (ImageView) rowView.findViewById(R.id.ivCategory);
                viewHolder.txtMessageCountCategory = (TextView)rowView.findViewById(R.id.txtMessageCountCategory);
                viewHolder.imgBesideText = (ImageView)rowView.findViewById(R.id.imgBesideText);

                rowView.setTag(viewHolder);
            }
            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String s = programs.get(position).category_name;
            holder.text.setText(s);
            holder.txtMessageCountCategory.setText(String.format("%d Messages",programs.get(position).count));
            holder.text.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(context)));
            holder.txtMessageCountCategory.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(context)));
            holder.imgBesideText.setColorFilter(Color.parseColor(PrefUtils.getBackGroudColorText(context)));

            return rowView;
        }
    }
}
