package maximsblog.blogspot.com.formuladict;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class ViewGraphicsActivity extends BaseActivity {

	private AdView mAdView;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		ActionBar bar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		bar.setBackgroundDrawable(d);
		bar.setIcon(android.R.color.transparent);
		setContentView(R.layout.activity_view);
		setTitle(((Article) getIntent().getParcelableExtra("a")).Title);
		if (savedInstanceState == null) {
			FragmentManager fm = getFragmentManager();
			FragmentViewGraphics fv = new FragmentViewGraphics();
			String path = getIntent().getStringExtra("path");
			Bundle args = new Bundle();
			args.putString("path", path);
			fv.setArguments(args);
			fm.beginTransaction().add(R.id.area, fv, "fragmentView").commit();
		}
		if (!getIntent().getBooleanExtra("p", false)) {
			LinearLayout myLayout = (LinearLayout) findViewById(R.id.area);
			mAdView = new AdView(this);
			mAdView.setAdSize(AdSize.BANNER);
			mAdView.setAdUnitId("key");
			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
			mAdView.loadAd(adRequest);
			myLayout.addView(mAdView);
		}
	}

	@Override
	public void onPause() {
		if (!getIntent().getBooleanExtra("p", false))
			mAdView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (!getIntent().getBooleanExtra("p", false))
			mAdView.destroy();
		super.onDestroy();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
