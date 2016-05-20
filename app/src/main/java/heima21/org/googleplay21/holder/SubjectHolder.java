package heima21.org.googleplay21.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.SubjectBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * Created by Administrator on 2016/4/29.
 */
public class SubjectHolder extends BaseHolder<SubjectBean> {
    @Bind(value = R.id.item_subject_iv_icon)
    protected ImageView mIvIcon;
    @Bind(value = R.id.item_subject_tv_title)
    protected TextView mTvTitle;
    @Override
    public View inflateAndFindView() {
        View subjectView = View.inflate(UiUtil.getContext(), R.layout.item_subject,null);
        ButterKnife.bind(this,subjectView);
        return subjectView;
    }

    @Override
    public void setData(SubjectBean data) {
        mTvTitle.setText(data.des);
        String iconUrl = Constants.IMG_URL + data.url;
        Picasso.with(UiUtil.getContext()).load(iconUrl).
                error(R.drawable.ic_default).placeholder(R.drawable.ic_default).into(mIvIcon);
    }
}
