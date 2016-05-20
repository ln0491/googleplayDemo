package heima21.org.googleplay21.base;

import android.view.View;

/**
 * 支持多个页面的holder,放置view的引用
 */
public abstract class BaseHolder<T> {

    /**
     * 由holder控制不同页面的item长相
     * @return
     */
    public abstract View inflateAndFindView();

    public abstract void setData(T data);
}
