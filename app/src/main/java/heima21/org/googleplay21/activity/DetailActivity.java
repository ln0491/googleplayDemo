package heima21.org.googleplay21.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.bean.DetailBean;
import heima21.org.googleplay21.holder.DetailDesHolder;
import heima21.org.googleplay21.holder.DetailDownloadHolder;
import heima21.org.googleplay21.holder.DetailInfoHolder;
import heima21.org.googleplay21.holder.DetailPicHolder;
import heima21.org.googleplay21.holder.DetailSafeHolder;
import heima21.org.googleplay21.manager.DownloadManager;
import heima21.org.googleplay21.protocol.DetailProtocol;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.LoadDataUi;

public class DetailActivity extends LoadDataActivity {

    private ActionBar mActionbar;
    private DetailBean mData;

    @Bind(value = R.id.detail_info_container)
    protected FrameLayout mInfoContainer;
    @Bind(value = R.id.detail_safe_container)
    protected FrameLayout mSafeContainer;

    @Bind(value = R.id.detail_pic_container)
    protected FrameLayout mPicContainer;

    @Bind(value = R.id.detail_des_container)
    protected FrameLayout mDesContainer;

    @Bind(value = R.id.detail_download_container)
    protected FrameLayout mDownloadContainer;
    private String mPackName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_detail);
        if(getIntent()!=null){
          mPackName =   getIntent().getStringExtra("pack");
        }

        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected View onInitSuccessView() {
        View detailView = View.inflate(UiUtil.getContext(),R.layout.content_detail,null);
        ButterKnife.bind(this, detailView);

        DetailInfoHolder infoHolder = new DetailInfoHolder();
        View infoView = infoHolder.inflateAndFindView();
        //添加每一块view
        mInfoContainer.addView(infoView);
        infoHolder.setData(mData);

        DetailSafeHolder safeHolder = new DetailSafeHolder();
        View safeView = safeHolder.inflateAndFindView();
        //添加每一块view
        mSafeContainer.addView(safeView);
        safeHolder.setData(mData);

        DetailPicHolder picHolder = new DetailPicHolder();
        View picView = picHolder.inflateAndFindView();
        //添加每一块view
        mPicContainer.addView(picView);
        picHolder.setData(mData);

        DetailDesHolder desHolder = new DetailDesHolder();
        View desView = desHolder.inflateAndFindView();
        //添加每一块view
        mDesContainer.addView(desView);
        desHolder.setData(mData);

        downloadHolder = new DetailDownloadHolder();
        View downloadView = downloadHolder.inflateAndFindView();
        //添加每一块view
        mDownloadContainer.addView(downloadView);
        downloadHolder.setData(mData);
        DownloadManager.getInstance().addOnDownloadListener(mData.packageName, downloadHolder);

        return detailView;
    }
    DetailDownloadHolder downloadHolder;
    @Override
    protected void onResume() {
        super.onResume();
        if(downloadHolder!=null) {
//            DownloadManager.getInstance().addOnDownloadListener(mData.packageName, downloadHolder);
            downloadHolder.checkState();
        }
    }

    @Override
    protected LoadDataUi.Result doInBackground() {
        DetailProtocol mProtocol = new DetailProtocol();
        Map<String,String> params = new HashMap<>();
        //需要接受一个包名
        params.put("packageName",mPackName);
        mProtocol.setParams(params);

        try {
            mData =  mProtocol.loadData();
            if(mData == null){
                return LoadDataUi.Result.EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LoadDataUi.Result.FAILED;
        }
        return LoadDataUi.Result.SUCESS;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
