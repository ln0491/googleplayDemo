package heima21.org.googleplay21.manager;

import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.fragment.AppFragment;
import heima21.org.googleplay21.fragment.CategoryFragment;
import heima21.org.googleplay21.fragment.GameFragment;
import heima21.org.googleplay21.fragment.HomeFragment;
import heima21.org.googleplay21.fragment.HotFragment;
import heima21.org.googleplay21.fragment.RecommendFragment;
import heima21.org.googleplay21.fragment.SubjectFragment;

/**
 * Created by Administrator on 2016/4/27.
 */
public class FragmentFactory {

    private static final String TAG = "FragmentFactory";
    static Map<Integer,LoadDataFragment> mCache = new HashMap<Integer,LoadDataFragment>();

    private static FragmentFactory mInstance;

    private FragmentFactory(){}

    public static FragmentFactory getInstance() {
        if(mInstance == null){
            mInstance = new FragmentFactory();
        }
        return mInstance;
    }


    public static LoadDataFragment getFragment(int position){
        //加上缓存功能,优先取缓存
        LoadDataFragment fragment = mCache.get(position);
        if(fragment!=null){
            Log.e(TAG,"取出缓存~");
            return fragment;
        }
        switch (position){
            case 0:
                fragment =  new HomeFragment();
                break;
            case 1:
                fragment =  new AppFragment();
                break;
            case 2:
                fragment =  new GameFragment();
                break;
            case 3:
                fragment =  new SubjectFragment();
                break;
            case 4:
                fragment =  new RecommendFragment();
                break;
            case 5:
                fragment =  new CategoryFragment();
                break;
            case 6:
                fragment =  new HotFragment();
                break;
            default:
                break;
        }
        mCache.put(position,fragment);
        Log.e(TAG, "保存缓存~");
        return fragment;
    }
}
