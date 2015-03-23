package com.project.samplesmsapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.samplesmsapplication.R;
import com.project.samplesmsapplication.helpers.DatabaseHelper;
import com.project.samplesmsapplication.helpers.PrefUtils;
import com.project.samplesmsapplication.model.Category;
import com.project.samplesmsapplication.model.Msg;

import java.util.ArrayList;
import java.util.List;

public class SearchMainActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView globalImageview;
    private EditText edSearch;
    private Button btnSearch;
    private ArrayList<Category> categories;
    private DatabaseHelper dbHelper;
    private int selectedCategory = 0;
    private ArrayList<Msg> messages;
    private ListView listMessages;
    private Spinner spinnerSections;
    ArrayList<ArrayList<Msg>> parts;
    private ImageView btnBackMSGLIST;
    private ImageView btnNextMSGLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        listMessages = (ListView) findViewById(R.id.listMessages);
        globalImageview = (ImageView) findViewById(R.id.globalImageView);
        spinnerSections = (Spinner) findViewById(R.id.spinnerSections);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Search");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_icon_home);
        }

        edSearch = (EditText) findViewById(R.id.edSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnBackMSGLIST = (ImageView) findViewById(R.id.btnBackMSGLIST);
        btnNextMSGLIST = (ImageView) findViewById(R.id.btnNextMSGLIST);

        btnNextMSGLIST.setOnClickListener(this);
        btnBackMSGLIST.setOnClickListener(this);

        new fetchCategories().execute();


    }

    private class fetchCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(CategoryHomeActivity.this,"Please wait","Fetching Categories",false,true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            dbHelper = new DatabaseHelper(SearchMainActivity.this);
            categories = dbHelper.getAllCategories();
            dbHelper.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  progressDialog.dismiss();
            setupSpinnerItems();
        }
    }

    static <T> ArrayList<ArrayList<T>> chopped(ArrayList<T> list, final int L) {
        ArrayList<ArrayList<T>> parts = new ArrayList<ArrayList<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                            list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    public class MyCustomAdapter extends BaseAdapter {

        private final Activity context;
        private final ArrayList<Msg> programs;
        private int selectedInt;

        public class ViewHolder {

            public TextView text;
            public TextView txtMessageCountCategory;

        }

        public MyCustomAdapter(Activity context, ArrayList<Msg> programs) {
            this.context = context;
            this.programs = programs;
            View v = spinnerSections.getSelectedView();
            TextView tv = (TextView) v.findViewById(R.id.txtItemSpinner);
            selectedInt = Integer.parseInt(tv.getText().toString().split("-")[0].toString().trim());
            Log.e("Selected String -----", "" + selectedInt);
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
                rowView = inflater.inflate(R.layout.item_message_list, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.txtMessageListTitle);
                viewHolder.txtMessageCountCategory = (TextView) rowView.findViewById(R.id.txtMessageListCount);
                rowView.setTag(viewHolder);
            }
            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            String s = programs.get(position).msg_text;
            holder.text.setText(s);

            if (position == 0) {
                holder.txtMessageCountCategory.setText(String.format("%d", selectedInt));
            } else {
                holder.txtMessageCountCategory.setText(String.format("%d", selectedInt + position));
            }

            holder.text.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(context)));
            holder.txtMessageCountCategory.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(context)));


            return rowView;
        }
    }


    private void processSections() {
/*
        List<Integer> numbers = new ArrayList<Integer>(
                Arrays.asList(5, 3, 1, 2, 9, 5, 0, 7,8,9,10,11,12,13,14,15,16,17,12)
        );
        List<List<Integer>> parts = chopped(numbers, 3);
        Log.e("Arr - - - - -",""+parts);

*/

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                parts = chopped(messages, 20);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                MySecAdapter adapter = new MySecAdapter(parts, SearchMainActivity.this);
                spinnerSections.setAdapter(adapter);
                spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        displayMessages(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.execute();

    }


    private void displayMessages(int position) {

        MyCustomAdapter adapter = new MyCustomAdapter(SearchMainActivity.this, parts.get(position));
        listMessages.setAdapter(adapter);
    }

    public class MySecAdapter extends BaseAdapter {


        private ArrayList<ArrayList<Msg>> temp_parts;
        private Context ctx;

        public MySecAdapter(ArrayList<ArrayList<Msg>> temp_parts, Context ctx) {
            this.temp_parts = temp_parts;
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return temp_parts.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent,
                    false);
            TextView make = (TextView) row.findViewById(R.id.txtItemSpinner);
            make.setText(String.format("%d - %d", messages.indexOf(temp_parts.get(position).get(0)) + 1, messages.indexOf(temp_parts.get(position).get(temp_parts.get(position).size() - 1)) + 1));
            return row;
        }


        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.item_spinner, parent,
                    false);
            TextView make = (TextView) row.findViewById(R.id.txtItemSpinner);
            make.setPadding(16, 8, 16, 8);
            make.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            make.setText(String.format("%d - %d", messages.indexOf(temp_parts.get(position).get(0)) + 1, messages.indexOf(temp_parts.get(position).get(temp_parts.get(position).size() - 1)) + 1));
            return row;
        }
    }


    private class fetchMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(CategoryHomeActivity.this,"Please wait","Fetching Categories",false,true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            dbHelper = new DatabaseHelper(SearchMainActivity.this);
            messages = dbHelper.getSearchResult(selectedCategory, edSearch.getText().toString());
            dbHelper.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(messages.isEmpty()){
                listMessages.setAdapter(null);
                edSearch.setText("");
                spinnerSections.setAdapter(null);
                Toast.makeText(SearchMainActivity.this,"No Results",Toast.LENGTH_SHORT).show();

            }else{
                processSections();
            }




        }
    }


    private void setupSpinnerItems() {

        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        toolbar.addView(spinnerContainer, lp);

        YourObjectSpinnerAdapter spinnerAdapter = new YourObjectSpinnerAdapter();
        spinnerAdapter.addItems(categories);

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCategory = categories.get(position)._id;
                listMessages.setAdapter(null);
                edSearch.setText("");
                spinnerSections.setAdapter(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePreference();
    }

    private void updatePreference() {

        globalImageview.setBackgroundColor(Color.parseColor(PrefUtils.getBackGroudColor(SearchMainActivity.this)));
        edSearch.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(SearchMainActivity.this)));
        btnSearch.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(SearchMainActivity.this)));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearch:

                if (edSearch.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SearchMainActivity.this, "Please Enter keyword", Toast.LENGTH_SHORT).show();

                } else {

                    processSearch();
                }
                break;


            case R.id.btnBackMSGLIST:

                if(spinnerSections.getSelectedItemPosition() == 0){

                }else{
                    spinnerSections.setSelection(spinnerSections.getSelectedItemPosition()-1);
                }

                break;

            case R.id.btnNextMSGLIST:
                if(spinnerSections.getSelectedItemPosition() == parts.size()-1){

                }else{
                    spinnerSections.setSelection(spinnerSections.getSelectedItemPosition() + 1);
                }




        }
    }

    private void processSearch() {

        new fetchMessages().execute();

    }


    private class YourObjectSpinnerAdapter extends BaseAdapter {
        private List<Category> mItems = new ArrayList<>();

        public void clear() {
            mItems.clear();
        }

        public void addItem(Category yourObject) {
            mItems.add(yourObject);
        }

        public void addItems(List<Category> yourObjectList) {
            mItems.addAll(yourObjectList);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.
                        toolbar_spinner_item_actionbar, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position).category_name : "";
        }
    }


}
