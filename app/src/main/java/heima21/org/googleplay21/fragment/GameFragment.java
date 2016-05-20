package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.base.ListItemClickAdapter;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.base.SuperBaseAdapter;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.holder.HomeHolder;
import heima21.org.googleplay21.protocol.GameProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * Created by Administrator on 2016/4/29.
 */
public class GameFragment extends LoadDataFragment {

    private GameProtocol mProtocol;
    private List<HomeBean.ApkItem> mDatas;

    @Override
    protected View onInitSuccessView() {
        ListView mListView = new ListView(UiUtil.getContext());
        mListView.setAdapter(new GameAdapter(mDatas,mListView));
        mListView.setBackgroundColor(Color.parseColor("#cccccc"));
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        return mListView;
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        mProtocol = new GameProtocol();
        try {
            mDatas = mProtocol.loadPage("0");
            if(mDatas == null || mDatas.size() == 0){
                return LoadDataUi.Result.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.SUCESS;
    }

    private class GameAdapter extends ListItemClickAdapter {
        public GameAdapter(List<HomeBean.ApkItem> mDatas, ListView mListView) {
            super(mDatas, mListView);
        }

        @Override
        protected List<HomeBean.ApkItem> getLoadMoreData() throws Exception {
           return mProtocol.loadPage("" + mDatas.size());
        }
    }
}
