package com.xeishawn.googleplusmini;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendProfile extends ActionBarActivity {
//    private static final String TAG = "gpm-friendProfile-activity";
    String friendDisplayName;
    String friendOrganizations = "";
    String friendAboutMe;
    String friendImage_url;
    String friendOccupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent activity = getIntent();
        friendAboutMe = activity.getExtras().getString("friendAboutMe");
        friendOrganizations = activity.getExtras().getString("friendOrganizations");
        friendDisplayName = activity.getExtras().getString("friendDisplayName");
        friendImage_url = activity.getExtras().getString("friendImage_url");
        friendImage_url = friendImage_url.substring(0,friendImage_url.indexOf("?")) + "?sz=300";
        friendOccupation = activity.getExtras().getString("friendOccupation");

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(getResources().getDrawable(R.drawable.ic_launcher))
                .showImageOnFail(getResources().getDrawable(R.drawable.ic_launcher))
                .showImageOnLoading(getResources().getDrawable(R.drawable.ic_launcher)).build();

        ImageView imageView = (ImageView) findViewById(R.id.icon);
        imageLoader.displayImage(friendImage_url, imageView, options);

        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        if (friendDisplayName != null) {
            profile_name.setText(friendDisplayName);
        }
        else{
            friendDisplayName = "User";
            profile_name.setText(friendDisplayName);
        }

        TextView profile_info = (TextView) findViewById(R.id.profile_info);
        if (friendOccupation != null) {
            profile_info.setText(friendOccupation + "\n");
        }
        else{
            profile_info.setText("\n");
        }

        if (friendOrganizations != null) {
            profile_info.append(friendOrganizations + "\n");
        }
        else{
            profile_info.append("\n");
        }

        if (friendAboutMe != null) {
            profile_info.append(friendAboutMe + "\n");
        }
        else{
            profile_info.append("\n");
        }

        Button email = (Button) findViewById(R.id.button_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email Client:"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_sign_out:
                Intent activity = new Intent(getApplicationContext(), LoginActivity.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activity);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
