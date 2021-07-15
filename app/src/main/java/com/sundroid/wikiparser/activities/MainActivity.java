package com.sundroid.wikiparser.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.adapters.InfinitePagerAdapter;
import com.sundroid.wikiparser.fragments.ArticleFragment;
import com.sundroid.wikiparser.fragments.CategoryFragment;
import com.sundroid.wikiparser.fragments.FeaturedFragment;
import com.sundroid.wikiparser.utils.ActivityNavigator;
import com.sundroid.wikiparser.utils.InfiniteViewPager;
import com.sundroid.wikiparser.utils.SharedPrefManager;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private String[] slides;
    @BindView(R.id.slide_pager) InfiniteViewPager slide_pager;
    @BindView(R.id.slider_shimmer) ShimmerFrameLayout slider_shimmer;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.nav_view) NavigationView nav_view;
    @BindView(R.id.drawer_layout) DrawerLayout drawer_layout;
    @BindView(R.id.drawer_toggle) RelativeLayout drawer_toggle;
    @BindView(R.id.toggle_icon) ImageView toggle_icon;

    SectionsPagerAdapter mSectionsPagerAdapter;
    SharedPrefManager sharedPrefManager;
    int checkpos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        sharedPrefManager = new SharedPrefManager(this);

        drawer_toggle.setOnClickListener(v -> drawer_layout.openDrawer(GravityCompat.START));

        nav_view.setNavigationItemSelectedListener(item -> {
            drawer_layout.closeDrawer(GravityCompat.START, false);
            if(item.getItemId() != R.id.nav_switch){
                new ActivityNavigator().setNavigate(MainActivity.this, item.getItemId());
            }
            return true;
        });

        View switchLayout = nav_view.getMenu().findItem(R.id.nav_switch).getActionView();
        ToggleSwitch mode_switch = switchLayout.findViewById(R.id.mode_switch);

        if(sharedPrefManager.getDisplayMode().equals("LIGHT")){
            checkpos = 0;
            mode_switch.setCheckedTogglePosition(checkpos);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            toggle_icon.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        }else{
            checkpos = 1;
            mode_switch.setCheckedTogglePosition(checkpos);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            toggle_icon.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        mode_switch.setOnToggleSwitchChangeListener((position, isChecked) -> {
            if(mode_switch.getCheckedTogglePosition() == 1){
                checkpos = 1;
                sharedPrefManager.saveSPString(SharedPrefManager.DISPLAY_MODE, "DARK");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                toggle_icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }else{
                checkpos = 0;
                sharedPrefManager.saveSPString(SharedPrefManager.DISPLAY_MODE, "LIGHT");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                toggle_icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });

        View headerView = nav_view.inflateHeaderView(R.layout.nav_header);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        slides = new String[]{
                "https://i.pinimg.com/originals/2a/a3/5e/2aa35e40cd89dbf94801ecedaa429b6e.jpg",
                "https://www.teahub.io/photos/full/308-3080271_nature-wallpaper-banner.jpg",
                "https://3.bp.blogspot.com/-kyb40VpBd9w/XLHduxsIBWI/AAAAAAAAAZU/hxqnhzCUCrw9JrPO9o6oCuE8iI3gpqTNgCLcBGAs/s1600/Facebook%2BCover%25234.jpg"
        };
        getBanners();

        slider_shimmer.startShimmer();
    }

    Handler mHandler;

    private LayoutInflater layoutInflater;
    CircularProgressDrawable circularProgressDrawable;
    private void getBanners(){
        circularProgressDrawable = new CircularProgressDrawable(MainActivity.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        slider_shimmer.stopShimmer();
        slider_shimmer.setVisibility(View.GONE);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return slides.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view == obj;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                container.removeView(view);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.home_slider_adapter, container, false);
                ImageView slide_img = view.findViewById(R.id.slider_img);
                try{
                    slide_img.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(MainActivity.this)
                            .load(slides[position])
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_sync)
                            .into(slide_img);
                }catch (Exception e){
                    Glide.with(MainActivity.this)
                            .load(slides[position])
                            .error(R.drawable.ic_sync)
                            .into(slide_img);
                    e.printStackTrace();
                }
                container.addView(view);
                return view;
            }
        };
        InfinitePagerAdapter wrappedAdapter = new InfinitePagerAdapter(pagerAdapter);
        slide_pager.clearAnimation();
        slide_pager.setAdapter(wrappedAdapter);

        mHandler = new Handler();
        slide_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(mHandler!=null){
                    mHandler.removeCallbacksAndMessages(null);
                }
                mHandler.postDelayed(() -> slide_pager.setCurrentItem(position+1), 3000);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ArticleFragment();
                case 1:
                    return new FeaturedFragment();
                case 2:
                    return new CategoryFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }
    }
}

