package maximsblog.blogspot.com.formuladict;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

	public class CommonPreferencesActivity extends PreferenceActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        ActionBar bar = getActionBar();
			Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
			bar.setBackgroundDrawable(d);
			bar.setIcon(android.R.color.transparent);
			View v = findViewById(android.R.id.content);
			v.setBackgroundDrawable(d);
			setStatusBarTranslucent(true);
	        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	        setResult(RESULT_OK);
	        setStatusBarTranslucent(true);
			decorTop(v);
	    }

	    public static class MyPreferenceFragment extends PreferenceFragment
	    {
	        @Override
	        public void onCreate(final Bundle savedInstanceState)
	        {
	            super.onCreate(savedInstanceState);
	            addPreferencesFromResource(R.xml.preferences);
	        }
	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	            View view = super.onCreateView(inflater, container, savedInstanceState);
	            view.setBackgroundColor(getResources().getColor(android.R.color.white));

	            return view;
	        }
	        
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
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if(id ==  android.R.id.home){
				onBackPressed();
	            return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		@Override
		public void onBackPressed() {
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	    
	}
