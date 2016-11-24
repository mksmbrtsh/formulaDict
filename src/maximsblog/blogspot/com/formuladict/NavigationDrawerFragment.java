package maximsblog.blogspot.com.formuladict;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements OnClickListener {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;

	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;

	private View mRoot;

	private ListView mListMenu;

	private CustomSimpleCursorAdapter mAdapter;

	private TextView mVersionText;

	private Button mGetFullVersionButton;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
		mGetFullVersionButton.setOnClickListener(this);
		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		String title = "";
		Cursor c = getActivity().getContentResolver().query(
				DataBaseProvider.CONTENT_URI_Subjects, null, "AND State = 1",
				null, null);
		int indexSubjectID = c.getColumnIndex("_id");
		int indexIndexRow = c.getColumnIndex("IndexRow");
		int indexSubjectTitle = c
				.getColumnIndex(DataBaseHelper.Tables.Subjects.Column.Title);
		c.moveToFirst();
		title = c.getString(indexSubjectTitle);
		mCurrentSelectedPosition = c.getInt(indexIndexRow);
		int id = c.getInt(indexSubjectID);
		c.close();

		c = getActivity().getContentResolver().query(
				DataBaseProvider.CONTENT_URI_Subjects, null, "", null, null);
		mAdapter = new CustomSimpleCursorAdapter(getActivity(),
				R.layout.row_subject, c,
				new String[] { "IndexRow", DataBaseHelper.Tables.Subjects.Column.Title },
				new int[] { R.id.number, R.id.article_title });
		mListMenu.setAdapter(mAdapter);

		mAdapter.setSelectedItem(mCurrentSelectedPosition);
		mListMenu.invalidateViews();
		// Select either the default item (0) or the last selected item.
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(mCurrentSelectedPosition,
					title, id);
		}
		try {
			String nameversion = getString(R.string.version) + " " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			mVersionText.setText(nameversion);
		} catch (NameNotFoundException e) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_navigation_drawer_menu,
				container, false);
		mGetFullVersionButton = (Button)mRoot.findViewById(R.id.full_version_btn);
		mListMenu = (ListView) mRoot.findViewById(R.id.listMenu);

		mListMenu.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		mListMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if ((mCurrentSelectedPosition) != position) {
					selectItem(position);
					mAdapter.setSelectedItem(position);
					mListMenu.invalidateViews();
				}
			}
		});
		mVersionText = (TextView)mRoot.findViewById(R.id.version_text_nav);
		return mRoot;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 *
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Landscape
		} else {
			// Portrait
			int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.84);
			LayoutParams params = mFragmentContainerView.getLayoutParams();
			params.width = width;
			mFragmentContainerView.setLayoutParams(params);
		}

		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.menu_icon_actionbar, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}
				InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				// getActivity().invalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}
				View view = getActivity().getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				// getActivity().invalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};
		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		ContentValues[] values = new ContentValues[2];
		Cursor c = ((SimpleCursorAdapter) mListMenu.getAdapter()).getCursor();
		c.moveToPosition(position);
		int indexSubjectID = c.getColumnIndex("_id");
		int indexSubjectTitle = c
				.getColumnIndex(DataBaseHelper.Tables.Subjects.Column.Title);
		String title = c.getString(indexSubjectTitle);
		int id = c.getInt(indexSubjectID);
		ContentValues v = new ContentValues();
		Uri uri = DataBaseProvider.CONTENT_URI_OpenedSubjects;
		v.put("State", 1);
		v.put("SubjectID", id);
		values[0] = v;
		ContentValues v2 = new ContentValues();
		c.moveToPosition(mCurrentSelectedPosition);
		v2 = new ContentValues();
		v2.put("State", 0);
		v2.put("SubjectID", c.getInt(indexSubjectID));
		values[1] = v2;
		mCurrentSelectedPosition = position;
		getActivity().getContentResolver().bulkInsert(uri, values);
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position, title, id);

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.

		if (mDrawerLayout != null && isDrawerOpen()) {
			showGlobalContextActionBar();
			return;
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// actionBar.setTitle("  " + getString(R.string.app_name));
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position, String title, int id);
		void onClickGetFullVersion();
	}

	public void close() {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
	}
	
	private class CustomSimpleCursorAdapter extends SimpleCursorAdapter {

		private int mSelectedPosition;
		
		public CustomSimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to, SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);
		}
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			View v = view.findViewById(R.id.selected);
			if(mSelectedPosition == cursor.getPosition()){
				v.setVisibility(View.VISIBLE);
				view.setBackgroundColor(getResources().getColor(R.color.item_background_with_alpa));
			} else { 
				v.setVisibility(View.INVISIBLE);
				view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
			TextView tv = (TextView)view.findViewById(R.id.number);
			tv.setText("\u00A7" + String.valueOf((Integer.valueOf(tv.getText().toString()) + 1)));
			
		};

		public void setSelectedItem(int currentSelectedPosition) {
			mSelectedPosition = currentSelectedPosition;
		}
		
	}

	public void setPurchased(boolean purchased) {
		if(purchased)
			mGetFullVersionButton.setVisibility(View.GONE);
		else
			mGetFullVersionButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		mCallbacks.onClickGetFullVersion();
	}
}
