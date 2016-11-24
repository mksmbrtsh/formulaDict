package maximsblog.blogspot.com.formuladict;

import com.melnykov.fab.ObservableScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class CustomHorizontalScrollView extends HorizontalScrollView  {

    public ObservableScrollView.OnScrollChangedListener mOnScrollChangedListener;

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedListener != null)
        	mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollChangedListener(ObservableScrollView.OnScrollChangedListener onScrollChangedListener){
        this.mOnScrollChangedListener = onScrollChangedListener;
    }

}
