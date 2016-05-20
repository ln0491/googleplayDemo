package heima21.org.googleplay21.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.DetailBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;
import heima21.org.googleplay21.view.RatioLayout;

/**
 * 详情页info部分
 */
public class DetailPicHolder extends BaseHolder<DetailBean> {
    @Bind(value = R.id.app_detail_pic_iv_container)
    protected LinearLayout mContainer;


    @Override
    public View inflateAndFindView() {
        View infoView = View.inflate(UiUtil.getContext(), R.layout.item_app_detail_pic,null);
        ButterKnife.bind(this, infoView);
        return infoView;
    }

    @Override
    public void setData(DetailBean data) {
        List<String> pics  = data.screen;
        for(int i=0;i<pics.size();i++){
            String url = Constants.IMG_URL + pics.get(i);
            ImageView iv = new ImageView(UiUtil.getContext());
            Picasso.with(UiUtil.getContext()).load(url).into(iv);

            RatioLayout ratioLayout = new RatioLayout(UiUtil.getContext());
            ratioLayout.setRatio(1.5f);
            ratioLayout.setRelative(RatioLayout.RELATIVE_HEIGHT);
            ratioLayout.setPadding(8,8,8,8);
            ratioLayout.addView(iv);
            mContainer.addView(ratioLayout);
        }
    }
}
