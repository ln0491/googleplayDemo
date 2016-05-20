package heima21.org.googleplay21.base;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import heima21.org.googleplay21.activity.DetailActivity;
import heima21.org.googleplay21.bean.HomeBean;
import heima21.org.googleplay21.holder.HomeHolder;
import heima21.org.googleplay21.manager.DownloadManager;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 支持3个apk列表页面点击的Adapter基类
 */
public abstract class ListItemClickAdapter extends SuperBaseAdapter<HomeBean.ApkItem> {

    private final List<HomeBean.ApkItem> mDatas;

    @Override
    protected BaseHolder getItemHolder(int position) {
        HomeHolder homeHolder = new HomeHolder();
        String packageName = mDatas.get(position).packageName;
        DownloadManager.getInstance().addOnDownloadListener(packageName, homeHolder);
        return homeHolder;
    }

    @Override
    protected boolean isSupportLoadMore() {
        return true;
    }

    private final ListView mListView;

    public ListItemClickAdapter(final List<HomeBean.ApkItem> mDatas, ListView listView) {
        super(mDatas);
        this.mDatas = mDatas;
        this.mListView = listView;
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UiUtil.getContext(), DetailActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                UiUtil.getContext().startActivity(intent);
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= mListView.getHeaderViewsCount();
                Intent intent = new Intent(UiUtil.getContext(), DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String packName = mDatas.get(position).packageName;
                intent.putExtra("pack", packName);
                UiUtil.getContext().startActivity(intent);
            }
        });
    }
}
