package heima21.org.googleplay21.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.DetailBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * 详情页info部分
 */
public class DetailInfoHolder extends BaseHolder<DetailBean> {
    @Bind(value = R.id.app_detail_info_tv_downloadnum)
    protected TextView mTvDownloadNum;

    @Bind(value = R.id.app_detail_info_tv_size)
    protected TextView mTvSize;
    @Bind(value = R.id.app_detail_info_tv_name)
    protected TextView mTvName;
    @Bind(value = R.id.app_detail_info_tv_time)
    protected TextView mTvTime;
    @Bind(value = R.id.app_detail_info_tv_version)
    protected TextView mTvVersion;

    @Bind(value = R.id.app_detail_info_rb_star)
    protected RatingBar mRbStar;

    @Bind(value = R.id.app_detail_info_iv_icon)
    protected ImageView mIvIcon;

    @Override
    public View inflateAndFindView() {
        View infoView = View.inflate(UiUtil.getContext(), R.layout.item_app_detail_info,null);
        ButterKnife.bind(this, infoView);
        return infoView;
    }

    @Override
    public void setData(DetailBean data) {
//        mTvDownloadNum.setText("下载量" + data.downloadNum);
//        mTvDownloadNum.setText(UiUtil.getString(R.string.detail_info_downloadnum) + data.downloadNum);
        mTvDownloadNum.setText(UiUtil.getString(R.string.detail_info_downloadnum, data.downloadNum));

        mTvSize.setText("大小:" + Formatter.formatFileSize(UiUtil.getContext(), data.size));
        mTvName.setText(data.name);
        mTvTime.setText("日期:" + data.date);
        mTvVersion.setText(UiUtil.getString(R.string.detail_info_version, data.version));

        mRbStar.setRating(data.stars);

        String url = Constants.IMG_URL + data.iconUrl;
        Picasso.with(UiUtil.getContext()).load(url).
                error(R.drawable.ic_default).placeholder(R.drawable.ic_default).into(mIvIcon);
    }
}
