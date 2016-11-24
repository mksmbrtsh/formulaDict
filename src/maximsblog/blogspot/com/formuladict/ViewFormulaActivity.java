package maximsblog.blogspot.com.formuladict;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class ViewFormulaActivity extends BaseActivity {

	private Menu mMenu;
	private String mFilter = "";
	private FragmentManager fm;
	private AdView mAdView;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		ActionBar bar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		bar.setBackgroundDrawable(d);
		bar.setIcon(android.R.color.transparent);
		setContentView(R.layout.activity_view);
		fm = getFragmentManager();
		if (savedInstanceState == null) {
			FragmentViewFormuls fv = new FragmentViewFormuls();
			Article article = getIntent().getParcelableExtra("a");
			int subjectID = getIntent().getIntExtra("s", -1);
			String filter = getIntent().getStringExtra("f");
			Bundle args = new Bundle();
			args.putParcelable("a", article);
			args.putInt("s", subjectID);
			args.putString("f", filter);
			fv.setArguments(args);
			fm.beginTransaction().add(R.id.container, fv, "mainViewFragTable")
					.commit();
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
	
	public boolean getPurchased(){
		return getIntent().getBooleanExtra("p", false);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		getMenuInflater().inflate(R.menu.view_menu, menu);
		FragmentViewFormuls fv = ((FragmentViewFormuls) fm
				.findFragmentByTag("mainViewFragTable"));
		Article a = fv.getCurrentArticle();
		if (a.Comment != null && a.Comment.length() > 0) {
			mMenu.findItem(R.id.help_article).setVisible(true);
		} else
			mMenu.findItem(R.id.help_article).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {

		FragmentViewFormuls fv = ((FragmentViewFormuls) fm
				.findFragmentByTag("mainViewFragTable"));
		Article a = fv.getCurrentArticle();
		String f = fv.getCurrentFilter();
		int subjectID = fv.getCurrentSubjectID();
		Intent data = new Intent();
		data.putExtra("a", a);
		setResult(RESULT_OK, data);
		savePositions(subjectID, a);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void savePositions(int subjectID, Article a) {
		ContentValues values = new ContentValues();
		values.put("SubjectID", subjectID);
		values.put("ChapterID", a.ChapterID);
		values.put("ArticlesID", a.ArticlesID);
		getContentResolver().insert(
				DataBaseProvider.CONTENT_URI_SelectedArticles, values);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.question) {
			FragmentViewFormuls fv = ((FragmentViewFormuls) fm
					.findFragmentByTag("mainViewFragTable"));
			Article a = fv.getCurrentArticle();

			try {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)
						+ ": " + a.ChapterTitle + ", " + a.Title);
				i.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "mksmbrtsh@gmail.com" });
				i.putExtra(Intent.EXTRA_TEXT,
						getString(R.string.enter_your_offer));

				if (i.resolveActivity(getPackageManager()) != null) {
					startActivity(i);
				}
			} catch (Exception e) {

			}
		} else if (id == R.id.about) {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else if (id == R.id.preference) {
			Intent i = new Intent(this, CommonPreferencesActivity2.class);
			startActivityForResult(i, 77);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			return true;
		} else if (id == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (id == R.id.share) {
			try {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
				i.putExtra(
						Intent.EXTRA_TEXT,
						"https://play.google.com/store/apps/details?id=maximsblog.blogspot.com.formuladict");
				startActivity(Intent
						.createChooser(i, getString(R.string.share)));
			} catch (Exception e) {

			}
		} else if (id == R.id.help_article) {
			Intent intent = new Intent(this, HelpArticleActivity.class);
			FragmentViewFormuls fv = ((FragmentViewFormuls) fm
					.findFragmentByTag("mainViewFragTable"));
			Article a = fv.getCurrentArticle();
			int subjectID = fv.getCurrentSubjectID();
			intent.putExtra("a", a);
			intent.putExtra("s", subjectID);
			intent.putExtra("f", fv.getCurrentFilter());
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 77) {
			FragmentViewFormuls fv = new FragmentViewFormuls();
			Article article = getIntent().getParcelableExtra("a");
			int subjectID = getIntent().getIntExtra("s", -1);
			String filter = getIntent().getStringExtra("f");
			Bundle args = new Bundle();
			args.putParcelable("a", article);
			args.putInt("s", subjectID);
			args.putString("f", filter);
			fv.setArguments(args);
			fm.beginTransaction().replace(R.id.area, fv, "mainViewFragTable")
					.commit();
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}
}
