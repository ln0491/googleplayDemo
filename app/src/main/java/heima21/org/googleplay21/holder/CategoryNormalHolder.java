package heima21.org.googleplay21.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import heima21.org.googleplay21.R;
import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.CategoryBean;
import heima21.org.googleplay21.util.Constants;
import heima21.org.googleplay21.util.UiUtil;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CategoryNormalHolder extends BaseHolder<CategoryBean> {
    @Bind(value = R.id.item_category_icon_1)
    protected ImageView mIvIcon1;
    @Bind(value = R.id.item_category_icon_2)
    protected ImageView mIvIcon2;
    @Bind(value = R.id.item_category_icon_3)
    protected ImageView mIvIcon3;

    @Bind(value = R.id.item_category_name_1)
    protected TextView mTvName1;
    @Bind(value = R.id.item_category_name_2)
    protected TextView mTvName2;
    @Bind(value = R.id.item_category_name_3)
    protected TextView mTvName3;

    @Bind(value = R.id.item_category_item_1)
    protected LinearLayout mContainer1;
    @Bind(value = R.id.item_category_item_2)
    protected LinearLayout mContainer2;
    @Bind(value = R.id.item_category_item_3)
    protected LinearLayout mContainer3;


    @Override
    public View inflateAndFindView() {
        View categoryView = View.inflate(UiUtil.getContext(), R.layout.item_category,null);
        ButterKnife.bind(this, categoryView);
        return categoryView;
    }

    @Override
    public void setData(CategoryBean data) {
        mTvName1.setText(data.name1);
        mTvName2.setText(data.name2);
        mTvName3.setText(data.name3);

        loadPic(data.url1, mIvIcon1,mContainer1);
        loadPic(data.url2, mIvIcon2,mContainer2);
        loadPic(data.url3, mIvIcon3,mContainer3);
    }

    private void loadPic(String url1, ImageView mIvIcon1, LinearLayout mContainer1) {
        //没有图片的情况
        if(TextUtils.isEmpty(url1)){
            mContainer1.setVisibility(View.INVISIBLE);
        }else {
            mContainer1.setVisibility(View.VISIBLE);
            String url = Constants.IMG_URL + url1;
            Picasso.with(UiUtil.getContext()).load(url).
                    error(R.drawable.ic_default).placeholder(R.drawable.ic_default).into(mIvIcon1);
        }
    }
}
