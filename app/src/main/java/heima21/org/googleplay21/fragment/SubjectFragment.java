package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.base.SuperBaseAdapter;
import heima21.org.googleplay21.bean.SubjectBean;
import heima21.org.googleplay21.holder.SubjectHolder;
import heima21.org.googleplay21.protocol.SubjectProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SubjectFragment extends LoadDataFragment {
    private List<SubjectBean> mDatas;

    @Override
    protected View onInitSuccessView() {
        ListView mListView = new ListView(UiUtil.getContext());
        mListView.setAdapter(new SubjectAdapter(mDatas));
        mListView.setBackgroundColor(Color.parseColor("#cccccc"));
        return mListView;
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        SubjectProtocol mProtocol = new SubjectProtocol();
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

    private class SubjectAdapter extends SuperBaseAdapter<SubjectBean> {
        public SubjectAdapter(List<SubjectBean> mDatas) {
            super(mDatas);
        }

        @Override
        protected BaseHolder getItemHolder(int position) {
            return new SubjectHolder();
        }

        @Override
        protected List<SubjectBean> getLoadMoreData() throws Exception {
            return null;
        }
    }
}
