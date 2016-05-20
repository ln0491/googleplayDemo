package heima21.org.googleplay21.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import heima21.org.googleplay21.holder.LoadMoreHolder;
import heima21.org.googleplay21.manager.ThreadManager;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 支持多个页面的Adapter基类
 * 1.提升开发速度
 * 2.代码分流（adapter---->holder,行为已经由此类进行复写，在holder中复写不同的内容，添加模块、重构代码、排除bug
 */
public abstract class SuperBaseAdapter<T> extends BaseAdapter {
    public static final int TYPE_LOADMORE = 0;
    public static final int TYPE_NORMAL = 1;
    private List<T> mDatas;

    public SuperBaseAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * 不同item的类型，必须是自然排列，0,1,2
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position == getCount() -1){
            //type loadmore
            return TYPE_LOADMORE;
        }else{
//            return TYPE_NORMAL;
            return getItemNormalType(position);
            //category页面normal有两种，item、title,
            //解决方案：默认返回type_normal,如果子页面需要更多的逻辑，复写一个方法
        }
    }

    protected int getItemNormalType(int position) {
        return TYPE_NORMAL;
    }

    /**
     * 把加载更多的这个条目，当成普通的条目处理，不是使用addFootView()
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getCount() {
        if(mDatas!=null){
//            return mDatas.size();
            //需要支持加载更多，需要多一个条目
            return mDatas.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 1.把加载更多的逻辑写在这里，支持多个页面
     * 2.当最后一个，也就是getCount-1的时候（刚刚露出一点）,显示加载更多，就可以去请求数据了
     * 3.拿到数据以后，让adapter先添加更多的数据moreData，更新数据
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder=null;
        if(convertView == null){
            //1.创建一个holder（内部是view的引用，contoller控制器) TODO:不同页面holder不一样
            //支持2种holder--->UI
            if(position == getCount() -1){
                //type loadmore
                holder = new LoadMoreHolder();
            }else{
                //normal type
                holder = getItemHolder(position);
            }
            //2.加载布局   TODO:不同页面是不一样的

//            convertView = View.inflate(UiUtil.getContext(), R.layout.home_temp_item,null);
            convertView = holder.inflateAndFindView();
            //3.findviewbyId 查找布局子view TODO:不同页面是不一样的
//            holder.textView = (TextView) convertView.findViewById(R.id.home_list_item_title);
            //4.绑定holder
            convertView.setTag(holder);
        }else{
            holder = (BaseHolder) convertView.getTag();
        }

        //set Data
        if(position == getCount() -1){
            //type loadmore
//            holder = new LoadMoreHolder();
            //TODO: 加载更多，请求服务器数据,
            // 2.初始化Ui先设置一次空状态
//            holder.setData(LoadMoreHolder.STATE_NONE);
            //还要加一个开关，当前是基类，考虑实现类是否支持分页加载
            if(isSupportLoadMore()) {
                if (holder instanceof LoadMoreHolder) {
                    onloadMore((LoadMoreHolder) holder);
                }
            }else{
                //不支持加载更多
                holder.setData(LoadMoreHolder.STATE_NONE);
            }
        }else{
            //normal type
            T data = mDatas.get(position);
            //5.设置数据 TODO:不同页面是不一样的
//        holder.textView.setText(data);
            holder.setData(data);
        }


        return convertView;
    }

    /**
     * 是否支持加载更多，不用定义成抽象类，
     * @return
     */
    protected boolean isSupportLoadMore() {
        return false;
    }
    private  boolean isLoadingMore;
    private void onloadMore(LoadMoreHolder holder) {
        //如果是正在加载，拒绝掉此次请求
        if(isLoadingMore){
            return;
        }
        isLoadingMore = true;
        //从服务器取数据，转圈，设置一次状态
        holder.setData(LoadMoreHolder.STATE_LOADING_MORE);
        ThreadManager.getNormalPool().execute(new LoadMoreTask(holder));
    }

//    protected abstract View inflateConvertView();

    /**
     * 不同页面内部view引用不同，因此做成抽象
     * @return
     */
    protected abstract BaseHolder getItemHolder(int position);


    class ViewHolder{
        TextView textView;
    }

    private class LoadMoreTask implements Runnable {

        private final LoadMoreHolder holder;

        public LoadMoreTask(LoadMoreHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            // 1.拿服务器数据的行为，还是基类，使用抽象方法
            int state = LoadMoreHolder.STATE_LOADING_MORE;
            List<T> moreData = null;
            try {
                Thread.sleep(2000);
                moreData = getLoadMoreData();
                if(moreData == null){
                    //b.没有更多数据了
                    state = LoadMoreHolder.STATE_NONE;
                }else{
                    //c.返回的数据小于页面大小 50 20 20 10，没有更多了
                    if(moreData.size()< Constants.PAGE_SIZE){
                        state = LoadMoreHolder.STATE_NONE;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                //a.错误的情形
                state = LoadMoreHolder.STATE_RETRY;
//                holder.setData(LoadMoreHolder.STATE_RETRY);
            }
            //2.添加more数据
            mDatas.addAll(moreData);
            //3.刷新UI,
            final int finalState = state;
            UiUtil.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                    //有可能需要显示错误的UI
                    holder.setData(finalState);
                    isLoadingMore = false;
                }
            });
        }
    }

    //去服务器取
    protected abstract List<T> getLoadMoreData() throws Exception;

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
}
