package maximsblog.blogspot.com.formuladict;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(savedInstanceState);
		setStatusBarTranslucent(true);
		decorTop(findViewById(R.id.area));
	}
	
	protected abstract void setContentView(Bundle savedInstanceState);
	
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
