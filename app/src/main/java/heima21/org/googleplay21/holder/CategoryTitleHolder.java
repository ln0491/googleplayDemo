package heima21.org.googleplay21.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import heima21.org.googleplay21.base.BaseHolder;
import heima21.org.googleplay21.bean.CategoryBean;
import heima21.org.googleplay21.util.UiUtil;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryBean>{

    private TextView textView;

    @Override
    public View inflateAndFindView() {
        textView = new TextView(UiUtil.getContext());
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        return textView;
    }

    @Override
    public void setData(CategoryBean data) {
        textView.setText(data.title);
    }
}
