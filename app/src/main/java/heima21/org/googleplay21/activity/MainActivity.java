package heima21.org.googleplay21.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.itheima.tabindicator.library.TabIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseActivity;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.fragment.AppFragment;
import heima21.org.googleplay21.fragment.HomeFragment;
import heima21.org.googleplay21.manager.FragmentFactory;
import heima21.org.googleplay21.util.UiUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    //需要使用protected,private 暴利反射
    @Bind(value = R.id.main_pager)
    protected ViewPager mPager;

    @Bind(value = R.id.indicator)
    protected TabIndicator mIndicator;

    private String[] mTitles;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //新actionbar的入口方法
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //类似toast,toast可以跨应用或者后台弹出，Snackbar只能在当前应用弹出
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        //5.0抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //抽屉的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();    //开关同步状态

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        initData();
    }

    private void initData() {
        mTitles = UiUtil.getStringArray(R.array.pagers);
        mPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        //indicator和viewpager建立关系
        mIndicator.setViewPager(mPager);
        mIndicator.addOnPageChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        if(mIndicator!=null){
            mIndicator.removeOnPageChangeListener(this);
        }
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 页面选中时调用此方法
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //强制进行加载，当前position对应的fragment
        LoadDataFragment fragment = FragmentFactory.getFragment(position);
        //只有我们封装的loadDataFragment才能调用我们封装的方法网络方法，
        fragment.loadData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //1.使用场景：单个页面非常大（资源、view)/页面数量特别多，不能使用FragmentPagerAdapter
    //2.实时性：证券的app,刷新1-5秒钟刷新，getItem重新调用，FragmentStatePagerAdapter
    //3.流量、电量的问题 http，全面考虑

    /**
     * FragmentStatePagerAdapter他的getItem()会重复调用
     */
    private class MainAdapter extends FragmentStatePagerAdapter{

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mTitles != null) {
                return mTitles[position];
            }
            return super.getPageTitle(position);
        }

        /**
         * 根据postion获取item,viewPager回收前面的页面以后，并没有触发此方法，内部缓存了之前的Fragment
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem:" + position);
            Fragment fragment = FragmentFactory.getFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mTitles != null) {
                return mTitles.length;
            }
            return 0;
        }
    }
//    private class MainAdapter extends FragmentPagerAdapter {
//
//        public MainAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (mTitles != null) {
//                return mTitles[position];
//            }
//            return super.getPageTitle(position);
//        }
//
//        /**
//         * 根据postion获取item,viewPager回收前面的页面以后，并没有触发此方法，内部缓存了之前的Fragment
//         *
//         * @param position
//         * @return
//         */
//        @Override
//        public Fragment getItem(int position) {
//            Log.e(TAG, "getItem:" + position);
//            switch (position){
//                case 0:
//                    return new HomeFragment();
//                case 1:
//                    return new AppFragment();
//                default:
//                    break;
//            }
//            return new HomeFragment();
//        }
//
//        @Override
//        public int getCount() {
//            if (mTitles != null) {
//                return mTitles.length;
//            }
//            return 0;
//        }
//    }

//    private class MainAdapter extends PagerAdapter{
//
//        /**
//         * 告诉Indicator显示什么内容
//         * @param position
//         * @return
//         */
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if(mTitles!=null){
//                return mTitles[position];
//            }
//            return super.getPageTitle(position);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            // ListView mListView; adpater//
//            TextView textView = new TextView(UiUtil.getContext());
//            textView.setTextColor(Color.BLACK);
//            textView.setText(mTitles[position]);
//            textView.setTextSize(25);
//            textView.setGravity(Gravity.CENTER);
//            container.addView(textView);
//            return textView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//
//        @Override
//        public int getCount() {
//            if(mTitles!=null){
//                return mTitles.length;
//            }
//            return 0;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }

    private void initView() {
        //使用查找view工具进行绑定，绑定到当前Activity上
        ButterKnife.bind(this);

        //test
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "gallery is clicked!", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
