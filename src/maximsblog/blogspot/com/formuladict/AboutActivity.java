package maximsblog.blogspot.com.formuladict;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class AboutActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		bar.setBackgroundDrawable(d);
		setStatusBarTranslucent(true);
		View v = findViewById(android.R.id.content);
		v.setBackgroundDrawable(d);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new AboutFragment()).commit();
		decorTop(v);
    }
    
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (makeTranslucent) {
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			} else {
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
	}
    
    @Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public void decorTop(View rootView) {
		int paddingTop = getStatusBarHeight();
		TypedValue tv = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
		paddingTop += TypedValue.complexToDimensionPixelSize(tv.data,
				getResources().getDisplayMetrics());
		rootView.setPadding(0, paddingTop, 0, 0);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}
