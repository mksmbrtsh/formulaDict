package maximsblog.blogspot.com.formuladict;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		OnQueryTextListener, BillingProcessor.IBillingHandler {

	private BillingProcessor bp;

	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private boolean mTablet;
	private Menu mMenu;

	private SearchView mSearchView;

	private int mID;

	private String mQuery;

	public boolean getTablet() {
		return mTablet;
	}

	private boolean purchased;

	public boolean getPurchased() {
		return purchased;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		purchased = false;
		if (!BillingProcessor.isIabServiceAvailable(this)) {
			Toast.makeText(this,
					getString(R.string.billing_service_not_enabled),
					Toast.LENGTH_LONG).show();
		} else {
			bp = new BillingProcessor(
					this,
					"key",
					"key", this);
		}
		mTablet = getResources().getBoolean(R.bool.is_landscape);
		FragmentManager fragmentManager = getFragmentManager();
		ActionBar bar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		bar.setBackgroundDrawable(d);
		bar.setIcon(android.R.color.transparent);
		setContentView(R.layout.activity_main);
		setStatusBarTranslucent(true);
		if (savedInstanceState != null)
			mQuery = savedInstanceState.getString("query");
		FragmentTransaction t = fragmentManager.beginTransaction();
		if (fragmentManager.findFragmentByTag("FragmentFormula") != null) {
			t.remove(fragmentManager.findFragmentByTag("FragmentFormula"));
		}

		if (fragmentManager.findFragmentByTag("mainListFrag") != null) {
			t.remove(fragmentManager.findFragmentByTag("mainListFrag"));
		}

		if (fragmentManager.findFragmentByTag("mainTableFrag") != null) {
			t.remove(fragmentManager.findFragmentByTag("mainTableFrag"));
		}

		if (fragmentManager.findFragmentByTag("mainListFragTable") != null) {
			t.remove(fragmentManager.findFragmentByTag("mainListFragTable"));
		}

		if (fragmentManager.findFragmentByTag("mainViewFragTable") != null) {
			t.remove(fragmentManager.findFragmentByTag("mainViewFragTable"));
		}
		t.commit();
		if (!mTablet) {
			moveDrawerToTop();
		} else {
			moveDrawerToTopTablet();
		}
	}

	private void moveDrawerToTop() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor,
				null); // "null" is important.

		// HACK: "steal" the first child of decor view
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		FrameLayout container = (FrameLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.navigation_drawer).setPadding(0,
				getStatusBarHeight(), 0, 0);

		// Make the drawer replace the first child
		decor.addView(drawer);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) drawer.findViewById(R.id.drawer_layout));
	}

	private void moveDrawerToTopTablet() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout drawer = (FrameLayout) inflater.inflate(
				R.layout.decor_tablet, null); // "null" is important.

		// HACK: "steal" the first child of decor view
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		FrameLayout container = (FrameLayout) drawer
				.findViewById(R.id.drawer_content); // This is the container we
													// defined just now.
		container.addView(child, 0);
		View v = drawer.findViewById(R.id.navigation_drawer);
		v.setPadding(0, getStatusBarHeight(), 0, 0);

		// Make the drawer replace the first child
		decor.addView(drawer);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setHasOptionsMenu(false);
		maximsblog.blogspot.com.formuladict.DrawerLayout drawerLayout = (maximsblog.blogspot.com.formuladict.DrawerLayout) drawer
				.findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
		drawerLayout.setScrimColor(getResources().getColor(
				android.R.color.transparent));
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerLayout.requestDisallowInterceptTouchEvent(true);
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
	public void onNavigationDrawerItemSelected(int position, String title,
			int id) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		mTitle = title;
		mID = id;
		if (!mTablet) {
			MainListFragment mainListFragment = new MainListFragment();
			Bundle args = new Bundle();
			args.putInt("id", id);
			mainListFragment.setArguments(args);
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.drawer_content, mainListFragment,
					"mainListFrag");
			transaction.commit();
		} else {
			FragmentTabletMain fragmentTabletMain = new FragmentTabletMain();
			Bundle args = new Bundle();
			args.putInt("id", id);
			fragmentTabletMain.setArguments(args);
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.drawer_content, fragmentTabletMain,
					"mainTableFrag");
			transaction.commit();
		}
		if (!mTablet)
			getActionBar().setTitle(mTitle);
		invalidateOptionsMenu();
	}

	public void onSectionAttached(int number) {
		/*
		 * switch (number) { case 0: mTitle = " 1 " ; break; case 1: mTitle =
		 * "  2"; break; } if (mTablet) { restoreActionBar();
		 * invalidateOptionsMenu(); }
		 */
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	public void setHelpArticleVisible(boolean visible) {
		mMenu.findItem(R.id.help_article).setVisible(visible);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean editable = prefs.getBoolean("edit_mod", true);
		mMenu.findItem(R.id.edit).setChecked(editable);
		if (mID == App.CUSTOM_FORMULS) {
			mMenu.findItem(R.id.edit).setVisible(true);
		} else
			mMenu.findItem(R.id.edit).setVisible(false);

		if (!mTablet && !mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			restoreActionBar();
		} else if (mTablet) {

		}
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		MenuItem s = menu.findItem(R.id.action_search);
		if (s != null) {
			mSearchView = (SearchView) s.getActionView();
			mSearchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			mSearchView.setIconifiedByDefault(true);
			mSearchView.setOnQueryTextListener(this);
			if (mQuery != null)
				mSearchView.setQuery(mQuery, true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mSearchView != null)
			outState.putString("query", mSearchView.getQuery().toString());
		super.onSaveInstanceState(outState);
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.about) {
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else if (id == R.id.preference) {
			Intent i = new Intent(this, CommonPreferencesActivity.class);
			startActivityForResult(i, 77);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else if (id == R.id.save) {
			DataBaseHelper o = new DataBaseHelper(getApplicationContext());
			String d = "";
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			File outputFile = null;
			File dir;
			dir = getCacheDir();
			File tsDir = new File(dir, "temp");
			boolean dirIscreate = false;
			if (!(dirIscreate = tsDir.exists())) {
				dirIscreate = tsDir.mkdir();
			}
			if (dirIscreate) {
				try {
					o.exportDatabase(getFilesDir(), d = tsDir.getAbsolutePath()
							+ File.separator + "math" + sdf.format(new Date())
							+ ".db");

				} catch (IOException e) {
					e.printStackTrace();
				}
				o.close();
				if (d.length() > 0) {
					Intent shareIntent = new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.setAction(Intent.ACTION_SEND);

					shareIntent.setType("text/plain");

					Uri contentUri = FileProvider.getUriForFile(this,
							"maximsblog.blogspot.com.formuladict.fileprovider",
							new File(d));
					shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
					shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							"db");
					shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"math");
					startActivity(Intent.createChooser(shareIntent, "share"));

				}
			}
		}
		if (id == R.id.share) {
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
			FragmentManager fm = getFragmentManager();
			FragmentViewFormuls fv = ((FragmentViewFormuls) fm
					.findFragmentByTag("mainViewFragTable"));
			Article a = fv.getCurrentArticle();
			int subjectID = fv.getCurrentSubjectID();
			intent.putExtra("a", a);
			intent.putExtra("s", subjectID);
			intent.putExtra("f", mQuery);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} else if (id == R.id.edit) {
			if (purchased) {
				item.setChecked(!item.isChecked());
				boolean editable = item.isChecked();
				Editor editor = PreferenceManager.getDefaultSharedPreferences(
						this).edit();
				editor.putBoolean("edit_mod", editable).commit();
				FragmentManager fm = getFragmentManager();
				MainListFragment mainListFragment = (MainListFragment) fm
						.findFragmentByTag("mainListFrag");
				if (mainListFragment != null)
					mainListFragment.refreshListMode(editable);
				if (mainListFragment == null)
					mainListFragment = (MainListFragment) fm
							.findFragmentByTag("mainListFragTable");
				if (mainListFragment != null)
					mainListFragment.refreshListMode(editable);
			} else {
				Toast.makeText(this,
						getString(R.string.please_get_full_version),
						Toast.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		FragmentManager fm = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fm
				.findFragmentByTag("mainListFrag");
		if (mainListFragment != null)
			mainListFragment.setFilter(newText);
		if (mainListFragment == null)
			mainListFragment = (MainListFragment) fm
					.findFragmentByTag("mainListFragTable");
		if (mainListFragment != null)
			mainListFragment.setFilter(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		FragmentManager fm = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fm
				.findFragmentByTag("mainListFrag");
		if (mainListFragment != null)
			mainListFragment.setFilter(query);
		if (mainListFragment == null)
			mainListFragment = (MainListFragment) fm
					.findFragmentByTag("mainListFragTable");
		if (mainListFragment != null)
			mainListFragment.setFilter(query);
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!bp.handleActivityResult(requestCode, resultCode, data)) {
			FragmentManager fragmentManager = getFragmentManager();
			if (requestCode == 77 && mTablet) {
				FragmentTabletMain fragmentTabletMain = (FragmentTabletMain) fragmentManager
						.findFragmentByTag("mainTableFrag");
				fragmentTabletMain.setSubject(mID);
				FragmentViewFormuls mainViewFragment = (FragmentViewFormuls) fragmentManager
						.findFragmentByTag("mainViewFragTable");
				mainViewFragment.setNewPrefs();
			} else
				super.onActivityResult(requestCode, resultCode, data);
		}
	}

	// IBillingHandler implementation

	@Override
	public void onBillingInitialized() {
		/*
		 * Called when BillingProcessor was initialized and it's ready to
		 * purchase
		 */
		purchased = true;// bp.isPurchased("formula_dict_full");
		TransactionDetails td = bp
				.getPurchaseTransactionDetails("formula_dict_full");
		if (td != null)
			purchased &= bp.isValidTransactionDetails(td);
		updateEnabledFealtures();
	}

	@Override
	public void onProductPurchased(String productId, TransactionDetails details) {
		/*
		 * Called when requested PRODUCT ID was successfully purchased
		 */
		purchased = bp.isPurchased("formula_dict_full");
		TransactionDetails td = bp
				.getPurchaseTransactionDetails("formula_dict_full");
		if (td != null)
			purchased &= bp.isValidTransactionDetails(td);
		updateEnabledFealtures();
	}

	@Override
	public void onBillingError(int errorCode, Throwable error) {
		/*
		 * Called when some error occurred. See Constants class for more details
		 * 
		 * Note - this includes handling the case where the user canceled the
		 * buy dialog: errorCode =
		 * Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
		 */
		if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
			String errorValue = "";
			switch (errorCode) {
			case Constants.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE:
				errorValue = getString(R.string.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE);
				break;
			case Constants.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE:
				errorValue = getString(R.string.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE);
				break;
			case Constants.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:
				errorValue = getString(R.string.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE);
				break;
			case Constants.BILLING_RESPONSE_RESULT_ERROR:
				errorValue = getString(R.string.BILLING_RESPONSE_RESULT_ERROR);
				break;
			case Constants.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
				errorValue = getString(R.string.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED);
				break;
			default:
				break;
			}
			Toast.makeText(this,
					getString(R.string.on_billing_error) + ": " + errorValue,
					Toast.LENGTH_LONG).show();
		}
		purchased = false;
		updateEnabledFealtures();
	}

	@Override
	public void onPurchaseHistoryRestored() {
		/*
		 * Called when purchase history was restored and the list of all owned
		 * PRODUCT ID's was loaded from Google Play
		 */
		purchased = bp.isPurchased("formula_dict_full");
		TransactionDetails td = bp
				.getPurchaseTransactionDetails("formula_dict_full");
		if (td != null)
			purchased &= bp.isValidTransactionDetails(td);
		updateEnabledFealtures();
	}

	private void updateEnabledFealtures() {
		mNavigationDrawerFragment.setPurchased(purchased);
		if (mMenu != null)
			mMenu.findItem(R.id.edit).setEnabled(purchased);
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		editor.putBoolean("edit_mod", purchased).commit();

		FragmentManager fm = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fm
				.findFragmentByTag("mainListFrag");
		if (mainListFragment != null)
			mainListFragment.setEmptyText(purchased);
		if (mainListFragment == null)
			mainListFragment = (MainListFragment) fm
					.findFragmentByTag("mainListFragTable");
		if (mainListFragment != null)
			mainListFragment.setEmptyText(purchased);
	}

	@Override
	public void onDestroy() {
		if (bp != null)
			bp.release();
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		if(mNavigationDrawerFragment.isVisible() && !mTablet){
			mNavigationDrawerFragment.close();
			return;
		}
		super.onBackPressed();
	};

	@Override
	public void onClickGetFullVersion() {
		bp.purchase(this, "formula_dict_full");
	}
}
