package heima21.org.googleplay21.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.base.LoadDataFragment;
import heima21.org.googleplay21.base.SuperBaseAdapter;
import heima21.org.googleplay21.bean.CategoryBean;
import heima21.org.googleplay21.holder.CategoryNormalHolder;
import heima21.org.googleplay21.holder.CategoryTitleHolder;
import heima21.org.googleplay21.vo.CategoryVo;
import heima21.org.googleplay21.protocol.CategoryProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CategoryFragment extends LoadDataFragment {
    private List<CategoryBean> mDatas;

    @Override
    protected View onInitSuccessView() {
        ListView mListView = new ListView(UiUtil.getContext());
        mListView.setBackgroundColor(Color.parseColor("#cccccc"));

        mListView.setAdapter(new CategoryAdapter(mDatas));
        return mListView;
    }

    private class CategoryAdapter extends SuperBaseAdapter<CategoryBean> {

        public CategoryAdapter(List<CategoryBean> mDatas) {
            super(mDatas);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        protected int getItemNormalType(int position) {
            CategoryBean bean = mDatas.get(position);
            if(bean.type == CategoryBean.TYPE_TITLE){
                return super.getItemNormalType(position) + 1;
            }else {
                return super.getItemNormalType(position);
            }

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        /**
         * 需要两种UI效果的holder支持
         * @return
         */
        @Override
        protected BaseHolder getItemHolder(int position) {
            //当前postion位置categorybean的类型
            CategoryBean bean = mDatas.get(position);
            if(bean.type == CategoryBean.TYPE_TITLE){
                return new CategoryTitleHolder();
            }else {
                return new CategoryNormalHolder();
            }
        }

        @Override
        protected List<CategoryBean> getLoadMoreData() throws Exception {
            return null;
        }
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        CategoryProtocol  mProtocol = new CategoryProtocol();
        Map<String,String> params = new HashMap<>();
        params.put("index", "0");
        mProtocol.setParams(params);

        try {
            mDatas  = mProtocol.loadData();
            if(mDatas == null){
                return LoadDataUi.Result.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.SUCESS;
    }


}
