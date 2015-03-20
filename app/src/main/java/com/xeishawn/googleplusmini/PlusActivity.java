package com.xeishawn.googleplusmini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.plusDomains.model.Person;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;
import java.util.Locale;


public class PlusActivity extends ActionBarActivity implements ActionBar.TabListener  {
//    private static final String TAG = "gpm-plus-activity";
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private static String accessToken;
    private static String displayName;
    private static String organizations;
    private static String aboutMe;
    private static String image_url;
    private static String occupation;
    private static String[] circle_list;
    private static String[][] circle_children_list;
    private static Person[][] circle_children_people;
    private static ImageLoader imageLoader;
    private static DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(getResources().getDrawable(R.drawable.ic_launcher))
                .showImageOnFail(getResources().getDrawable(R.drawable.ic_launcher))
                .showImageOnLoading(getResources().getDrawable(R.drawable.ic_launcher)).build();

        Intent activity = getIntent();
        accessToken = activity.getExtras().getString("accessToken");

        aboutMe = activity.getExtras().getString("aboutMe");
        occupation = activity.getExtras().getString("occupation");
        organizations = activity.getExtras().getString("organizations");
        displayName = activity.getExtras().getString("displayName");
        image_url = activity.getExtras().getString("image_url");
        image_url = image_url.substring(0,image_url.indexOf("?")) + "?sz=300";

        circle_list = activity.getStringArrayExtra("circle_list");
        circle_children_list = GetUsernameTask.getArray();
        circle_children_people = GetUsernameTask.getCircle_children_people();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent activity = new Intent(getApplicationContext(), LoginActivity.class);
        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            Intent activity = new Intent(getApplicationContext(), LoginActivity.class);
            activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MyProfile.newInstance(0, "Profile");
                case 1:
                    return MyCircles.newInstance(1, "Circles");
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    public static class MyProfile extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TITLE = "Profile";

        public static MyProfile newInstance(int sectionNumber, String title) {
            MyProfile fragment = new MyProfile();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.icon);
            imageLoader.displayImage(image_url, imageView, options);
            TextView profile_name = (TextView) rootView.findViewById(R.id.profile_name);
            TextView profile_info = (TextView) rootView.findViewById(R.id.profile_info);

            if (displayName != null) {
                profile_name.setText(displayName);
            }
            else{
                displayName = "User";
                profile_name.setText(displayName);
            }

            if (occupation != null) {
                profile_info.setText(occupation + "\n");
            }
            else{
                profile_info.setText("\n");
            }

            if (organizations != null) {
                profile_info.append(organizations + "\n");
            }
            else{
                profile_info.append("\n");
            }

            if (aboutMe != null) {
                profile_info.append(aboutMe + "\n");
            }
            else{
                profile_info.append("\n");
            }

            Button email = (Button) rootView.findViewById(R.id.button_email);
            email.setVisibility(View.INVISIBLE);
            return rootView;
        }
    }

    public static class MyCircles extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TITLE = "Circles";

        public static MyCircles newInstance(int sectionNumber, String title) {
            MyCircles fragment = new MyCircles();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.circle_main, container, false);
            ExpandableListView list = (ExpandableListView) rootView.findViewById(R.id.listView);
            list.setAdapter(new FriendListAdapter(this.getActivity()));
            list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                //Set child listener. clicking on friend under a circle will open the friend's profile
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    String friendDisplayName;
                    String friendOrganizations = "";
                    String friendAboutMe;
                    String friendImage_url;
                    String friendOccupation;

                    Intent activity = new Intent(parent.getContext(), FriendProfile.class);
                    activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Person friend = circle_children_people[groupPosition][childPosition];
//                    Log.i(TAG, friend.toString());
                    friendDisplayName = friend.getDisplayName();
                    activity.putExtra("friendDisplayName", friendDisplayName);
                    friendAboutMe = friend.getAboutMe();
                    activity.putExtra("friendAboutMe", friendAboutMe);
                    friendImage_url = friend.getImage().getUrl();
                    activity.putExtra("friendImage_url", friendImage_url);
                    friendOccupation = friend.getOccupation();
                    activity.putExtra("friendOccupation", friendOccupation);

                    if(friend.getOrganizations() != null) {
                        List<Person.Organizations> tmp = friend.getOrganizations();
                        for (Person.Organizations o: tmp){
                            friendOrganizations = organizations + " " + o.getName() + ",";
                        }
                        friendOrganizations = friendOrganizations.substring(0, friendOrganizations.length()-1);
                        activity.putExtra("friendOrganizations", friendOrganizations);
                    }

                    startActivity(activity);
                    return true;
                }
            });
            return rootView;
        }

        public class FriendListAdapter extends BaseExpandableListAdapter {
            public LayoutInflater inflater;
            public Activity activity;
            private String[] circles = circle_list;
            private String[][] circle_children = (String[][])circle_children_list;

            public FriendListAdapter (Activity activity) {
                this.activity = activity;
                inflater = activity.getLayoutInflater();
            }

            @Override
            public int getGroupCount() {
                return circles.length;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return circle_children[groupPosition].length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return circles[groupPosition];
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return circle_children[groupPosition][childPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.circle_list, null);
                }

                ((CheckedTextView) convertView).setText(getGroup(groupPosition).toString());
                ((CheckedTextView) convertView).setChecked(isExpanded);
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.entry, null);
                }

                convertView.setClickable(false);
                TextView textView = (TextView) convertView.findViewById(R.id.entry);
                textView.setText(getChild(groupPosition, childPosition).toString());

                Person friend = circle_children_people[groupPosition][childPosition];
                ImageView icon = (ImageView) convertView.findViewById(R.id.mini_icon);
                imageLoader.displayImage(friend.getImage().getUrl(), icon, options);
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        }
    }
}