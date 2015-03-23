package com.project.samplesmsapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.samplesmsapplication.R;
import com.project.samplesmsapplication.helpers.DatabaseHelper;
import com.project.samplesmsapplication.helpers.MyClipboardManager;
import com.project.samplesmsapplication.helpers.PrefUtils;
import com.project.samplesmsapplication.model.Msg;

import java.util.ArrayList;

public class GlobalMessageDetailsActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView txtMessageDetails;

    private int current_position;
    private int current_id;
    private String current_message;
    private int cat_id;
    private DatabaseHelper dbHelper;
    private ArrayList<Msg> msgs;
    private ImageView btnNextShowMessage;
    private ImageView btnPreviousShowMessage;

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TextView txtCurrentTotal;

    private ImageView imgShare;
    private ImageView imgMessages;
    private ImageView imgCopy;
    private ImageView imgFavourite;
    private ImageView globalImageview;
    private ImageView imgThumbsUp;
    private ImageView getImgThumbsDown;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        globalImageview = (ImageView) findViewById(R.id.globalImageView);
        imgThumbsUp = (ImageView)findViewById(R.id.imgThumbsUp);
        getImgThumbsDown = (ImageView)findViewById(R.id.imgThumbsDown);
        if (toolbar != null) {
            toolbar.setTitle("Category");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_icon_home);
        }

        init();
        Intent intent = getIntent();
        current_id = intent.getIntExtra("id", 0);
        current_message = intent.getStringExtra("msg");
        current_position = intent.getIntExtra("position", 0);
        cat_id = intent.getIntExtra("category_id", 0);


    }

    private void init() {

        //  txtMessageDetails = (TextView) findViewById(R.id.txtMessageDetails);
        btnNextShowMessage = (ImageView) findViewById(R.id.btnNextShowMessage);
        btnPreviousShowMessage = (ImageView) findViewById(R.id.btnPreviousShowMessage);
        btnPreviousShowMessage.setOnClickListener(previousClickListner);
        btnNextShowMessage.setOnClickListener(nextClickListner);
        viewPager = (ViewPager) findViewById(R.id.guideSlider);
        viewPager.setOnPageChangeListener(pageChangeListener);

        imgCopy = (ImageView) findViewById(R.id.imgCopy);
        imgMessages = (ImageView) findViewById(R.id.imgMessage);
        imgShare = (ImageView) findViewById(R.id.imgShare);

        imgCopy.setOnClickListener(this);
        imgMessages.setOnClickListener(this);
        imgShare.setOnClickListener(this);

        txtCurrentTotal = (TextView) findViewById(R.id.txtCurrentTotal);
        imgFavourite = (ImageView) findViewById(R.id.imgFavourite);
        imgFavourite.setOnClickListener(favouriteClick);

        new fetchCategories().execute();

    }

    private View.OnClickListener favouriteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            processSetFavourite();
        }
    };


    private void processSetFavourite() {

        Msg message = msgs.get(viewPager.getCurrentItem());
        boolean isFavourite = message.isFavourite;
        DatabaseHelper helper = new DatabaseHelper(GlobalMessageDetailsActivity.this);

        if (isFavourite == false) {

            helper.setFavourite(message._id, true);
            helper.close();
            msgs.get(viewPager.getCurrentItem()).isFavourite = true;
            imgFavourite.setImageResource(R.drawable.ic_toggle_star);
            Toast.makeText(GlobalMessageDetailsActivity.this,"Added To Favourite",Toast.LENGTH_SHORT).show();

        } else {

            helper.setFavourite(message._id, false);
            helper.close();
            msgs.get(viewPager.getCurrentItem()).isFavourite = false;
            imgFavourite.setImageResource(R.drawable.ic_toggle_star_outline);
            Toast.makeText(GlobalMessageDetailsActivity.this,"Removed From Favourite",Toast.LENGTH_SHORT).show();

        }


    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            txtCurrentTotal.setText(String.format("%d/%d", position + 1, msgs.size()));
            updateFavouriteStatus(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateFavouriteStatus(int position) {

        try {
            boolean isFav = msgs.get(position).isFavourite;


            if (isFav == true) {
                imgFavourite.setImageResource(R.drawable.ic_toggle_star);
            } else {
                imgFavourite.setImageResource(R.drawable.ic_toggle_star_outline);
            }
        } catch (Exception e) {

        }
    }


    private View.OnClickListener nextClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (viewPager.getCurrentItem() >= msgs.size()) {

            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }

        }
    };

    private View.OnClickListener previousClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (viewPager.getCurrentItem() == 0) {

            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        updatePreference();
    }

    private void updatePreference() {

        globalImageview.setBackgroundColor(Color.parseColor(PrefUtils.getBackGroudColor(GlobalMessageDetailsActivity.this)));
        imgFavourite.setColorFilter(Color.parseColor(PrefUtils.getBackGroudColorText(GlobalMessageDetailsActivity.this)));
        imgThumbsUp.setColorFilter(Color.parseColor(PrefUtils.getBackGroudColorText(GlobalMessageDetailsActivity.this)));
        getImgThumbsDown.setColorFilter(Color.parseColor(PrefUtils.getBackGroudColorText(GlobalMessageDetailsActivity.this)));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgCopy:

                processCopy();

                break;

            case R.id.imgMessage:

                processMessage();

                break;
            case R.id.imgShare:
                processShare();
                break;
        }
    }

    private void processMessage() {

        Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
        intentsms.putExtra("sms_body", msgs.get(viewPager.getCurrentItem()).msg_text);
        startActivity(intentsms);

    }

    private void processShare() {


        Intent iShare = new Intent(Intent.ACTION_SEND);
        iShare.setType("text/plain");
        iShare.putExtra(Intent.EXTRA_TEXT, msgs.get(viewPager.getCurrentItem()).msg_text);
        startActivity(Intent.createChooser(iShare, "Share with"));

    }

    private void processCopy() {

        MyClipboardManager manager = new MyClipboardManager();
        manager.copyToClipboard(GlobalMessageDetailsActivity.this, msgs.get(viewPager.getCurrentItem()).msg_text);
        Toast.makeText(this, "Copied To ClipBoard", Toast.LENGTH_SHORT).show();

    }

    private class fetchCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(CategoryHomeActivity.this,"Please wait","Fetching Categories",false,true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            dbHelper = new DatabaseHelper(GlobalMessageDetailsActivity.this);
            msgs = dbHelper.getMessages(cat_id);
            dbHelper.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pagerAdapter = new GuideAdapter(GlobalMessageDetailsActivity.this, msgs);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(current_position);


            updateFavouriteStatus(current_position);

        }
    }

    public class GuideAdapter extends PagerAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<Msg> guideImages;

        public GuideAdapter(Context context, ArrayList<Msg> guideImages) {
            this.context = context;
            this.guideImages = guideImages;
        }

        @Override
        public int getCount() {
            return guideImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((TextView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            TextView guideImage = new TextView(context);
            guideImage.setText(guideImages.get(position).msg_text);
            guideImage.setGravity(Gravity.CENTER);
            guideImage.setTextColor(Color.parseColor(PrefUtils.getBackGroudColorText(context)));
            guideImage.setTextSize(TypedValue.COMPLEX_UNIT_SP, PrefUtils.getMessageFontSize(GlobalMessageDetailsActivity.this));
            guideImage.setTypeface(PrefUtils.getTypeFace(GlobalMessageDetailsActivity.this));
            ((ViewPager) container).addView(guideImage, 0);

            return guideImage;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((TextView) object);
        }
    }



    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
