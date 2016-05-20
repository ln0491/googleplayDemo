package heima21.org.googleplay21.holder;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 加载更多的holder
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
    //去加载更多
    public static final int STATE_LOADING_MORE = 1;
    //加载更多失败了
    public static final int STATE_RETRY = 2;
    //都不显示，当前页面已经加载完所有数据、不支持加载更多
    public static final int STATE_NONE = 0;

    @Bind(value = R.id.item_loadmore_container_loading)
    protected LinearLayout mLoadMoreContainer;
    @Bind(value = R.id.item_loadmore_container_retry)
    protected LinearLayout mRetryContainer;

    @Override
    public View inflateAndFindView() {
        //haha
        //2.使用XML
        View convertView = View.inflate(UiUtil.getContext(), R.layout.item_load_more,null);
        ButterKnife.bind(this,convertView);

//        TextView tv = new TextView(UiUtil.getContext());
//        tv.setTextColor(Color.RED);
//        tv.setText("我是加载更多");
//        tv.setTextSize(30);
//        tv.setGravity(Gravity.CENTER);
        return convertView;
    }

    //添加一个方法控制他的状态,设置数据---》变成切换状态
    @Override
    public void setData(Integer state) {
        if(state == STATE_NONE){
            mLoadMoreContainer.setVisibility(View.GONE);
            mRetryContainer.setVisibility(View.GONE);
        }else if(state == STATE_LOADING_MORE){
            mLoadMoreContainer.setVisibility(View.VISIBLE);
            mRetryContainer.setVisibility(View.GONE);
        }else {
            mLoadMoreContainer.setVisibility(View.GONE);
            mRetryContainer.setVisibility(View.VISIBLE);
        }
    }
}
