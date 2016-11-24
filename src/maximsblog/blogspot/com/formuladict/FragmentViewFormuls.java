package maximsblog.blogspot.com.formuladict;

import java.util.Map;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.Box;
import maximsblog.blogspot.com.jlatexmath.core.IHintTest;
import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.JMathTeXException;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula.TeXIconBuilder;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.melnykov.fab.FloatingActionButton.ScrollViewScrollDetectorImpl;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.TextUtils.TruncateAt;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class FragmentViewFormuls extends Fragment implements OnClickListener,
		OnTouchListener {
	private Article mArticle;
	private FloatingActionButton mFabLeft;
	private FloatingActionButton mFabRight;
	private int mSubjectID;
	private String mFilter;
	private String mOriginalDescription;
	private View mRoot;

	private enum ImageAnim {
		none, left, right
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			if (getArguments().getParcelable("a") != null) {
				mArticle = getArguments().getParcelable("a");
				mOriginalDescription = new String(mArticle.Description);
				mSubjectID = getArguments().getInt("s", -1);
				mFilter = getArguments().getString("f");
			}
		} else {
			mArticle = savedInstanceState.getParcelable("a");
			mOriginalDescription = savedInstanceState.getString("d");
			mSubjectID = savedInstanceState.getInt("s", -1);
			mFilter = savedInstanceState.getString("f");
		}
		mRoot = inflater.inflate(R.layout.fragment_view_formuls, container,
				false);
		mFabLeft = (FloatingActionButton) mRoot.findViewById(R.id.left);
		mFabRight = (FloatingActionButton) mRoot.findViewById(R.id.right);

		mFabLeft.setOnClickListener(this);
		mFabRight.setOnClickListener(this);
		mRoot.findViewById(R.id.triple_tap_detector_layout).setOnTouchListener(
				this);
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity() instanceof MainActivity
				&& ((MainActivity) getActivity()).getTablet()) {
			decorTop(mRoot);
		}
		if (savedInstanceState == null && mArticle != null) {
			mFabLeft.setVisibility(View.VISIBLE);
			mFabRight.setVisibility(View.VISIBLE);
			mRoot.setClickable(true);
			setFormulaFragment(ImageAnim.none);
		} else {
			mFabLeft.setVisibility(View.GONE);
			mFabRight.setVisibility(View.GONE);
			mRoot.setClickable(false);
		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("a", mArticle);
		outState.putString("f", mFilter);
		outState.putInt("s", mSubjectID);
		outState.putString("d", mOriginalDescription);
		super.onSaveInstanceState(outState);
	};

	public void decorTop(View rootView) {
		int paddingTop = ((MainActivity) getActivity()).getStatusBarHeight();
		TypedValue tv = new TypedValue();
		getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true);
		paddingTop += TypedValue.complexToDimensionPixelSize(tv.data,
				getResources().getDisplayMetrics());
		rootView.setPadding(0, paddingTop, 0, 0);
	}

	@Override
	public void onClick(View view) {
		Cursor childrenCursor = getActivity().getContentResolver()
				.query(DataBaseProvider.CONTENT_URI_Articles,
						null,
						" AND c.SubjectID = " + mSubjectID
								+ " AND (a.Description LIKE '%" + mFilter
								+ "%' OR a.Title LIKE '%" + mFilter
								+ "%' OR c.Title LIKE '%" + mFilter + "%')",
						null, null);
		boolean find = false;
		for (int i1 = 0, cnt1 = childrenCursor.getCount(); i1 < cnt1; i1++) {
			childrenCursor.moveToPosition(i1);
			int id = childrenCursor.getInt(childrenCursor
					.getColumnIndex("ArticlesID"));
			if (id == mArticle.ArticlesID) {
				find = true;
				break;
			}
		}

		if (find) {
			if (view.getId() == R.id.left) {
				if (childrenCursor.moveToPrevious()) {
					setPrevArticleMainList();
					setArticle(childrenCursor, ImageAnim.left);
					if(getActivity() instanceof MainActivity){
						((MainActivity)getActivity()).setHelpArticleVisible(mArticle.Comment != null && mArticle.Comment.length() > 0);
					}
					return;
				} else if (childrenCursor.moveToLast()) {
					setPrevArticleMainList();
					setArticle(childrenCursor, ImageAnim.left);
					if(getActivity() instanceof MainActivity){
						((MainActivity)getActivity()).setHelpArticleVisible(mArticle.Comment != null && mArticle.Comment.length() > 0);
					}
					return;
				}
			} else {
				if (childrenCursor.moveToNext()) {
					setNextArticleMainList();
					setArticle(childrenCursor, ImageAnim.right);
					if(getActivity() instanceof MainActivity){
						((MainActivity)getActivity()).setHelpArticleVisible(mArticle.Comment != null && mArticle.Comment.length() > 0);
					}
					return;
				} else if (childrenCursor.moveToFirst()) {
					setNextArticleMainList();
					setArticle(childrenCursor, ImageAnim.right);
					if(getActivity() instanceof MainActivity){
						((MainActivity)getActivity()).setHelpArticleVisible(mArticle.Comment != null && mArticle.Comment.length() > 0);
					}
					return;
				}
			}
		}
		childrenCursor.close();
	}

	private void setNextArticleMainList() {
		FragmentManager fm = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fm
				.findFragmentByTag("mainListFrag");
		if (mainListFragment != null)
			mainListFragment.NextArticle();
		else {
			mainListFragment = (MainListFragment) fm
					.findFragmentByTag("mainListFragTable");
			if (mainListFragment != null)
				mainListFragment.NextArticle();
		}
		
		//getActivity().invalidateOptionsMenu();
	}

	private void setPrevArticleMainList() {
		FragmentManager fm = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fm
				.findFragmentByTag("mainListFrag");
		if (mainListFragment != null)
			mainListFragment.PrevArticle();
		else {
			mainListFragment = (MainListFragment) fm
					.findFragmentByTag("mainListFragTable");
			if (mainListFragment != null)
				mainListFragment.PrevArticle();
		}
		//getActivity().invalidateOptionsMenu();
	}

	private void setArticle(Cursor childrenCursor, ImageAnim imageAnim) {

		Article a = new Article(
				childrenCursor.getInt(childrenCursor
						.getColumnIndex("ArticlesID")),
				childrenCursor.getInt(childrenCursor
						.getColumnIndex("ChapterID")),
				childrenCursor.getString(childrenCursor
						.getColumnIndex("ArticleTitle")),
				childrenCursor.getString(childrenCursor
						.getColumnIndex("Description")),
				childrenCursor.getString(childrenCursor
						.getColumnIndex("Comment")),
				childrenCursor.getInt(childrenCursor
						.getColumnIndex("LanguageID")),
				childrenCursor.getInt(childrenCursor.getColumnIndex("IndexRow")),
				childrenCursor.getString(childrenCursor
						.getColumnIndex("ChapterTitle")), childrenCursor
						.getInt(childrenCursor.getColumnIndex("ChapterNumber")));
		mArticle = a;
		setFormulaFragment(imageAnim);
	}

	// Set the tap delay in milliseconds
	protected static final long TAP_MAX_DELAY = 500L;
	// Radius to capture tap within bound
	private final static int RADIUS = 30;
	private RectF mTapArea;

	private int mLastTapCount = 0;

	TapCounter mTapCounter = new TapCounter(TAP_MAX_DELAY, TAP_MAX_DELAY);
	private Menu mMenu;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTapCounter.resetCounter();
			if (mTapArea != null) {
				if (mTapArea.contains(x, y)) {
					if (mLastTapCount < 1) {
						mLastTapCount++;
					} else {
						if (mFabLeft.isVisible()) {
							mFabLeft.hide(true);
							mFabRight.hide(true);
						} else {
							mFabLeft.show(true);
							mFabRight.show(true);
						}
						mLastTapCount = 1;
						return true;
					}
				} else {
					mLastTapCount = 1;
					mTapArea = new RectF(x - RADIUS, y - RADIUS, x + RADIUS, y
							+ RADIUS);
				}
			} else {
				mLastTapCount = 1;
				mTapArea = new RectF(x - RADIUS, y - RADIUS, x + RADIUS, y
						+ RADIUS);
			}

		}

		return false;
	}

	class TapCounter extends CountDownTimer {

		public TapCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			if (mTapArea != null) {
				mLastTapCount = 0;
				mTapArea = null;
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}

		public void resetCounter() {
			start();
		}
	}

	public void deleteFilters() {
		mArticle.Description = mOriginalDescription;
		mFilter = "";
		setFormulaFragment(ImageAnim.none);
	}

	public void setIntent(Intent intent) {
		mArticle = intent.getParcelableExtra("a");
		mOriginalDescription = new String(mArticle.Description);
		mSubjectID = intent.getIntExtra("s", -1);
		mFilter = intent.getStringExtra("f");
		setFormulaFragment(ImageAnim.none);
		if(getActivity() instanceof MainActivity){
			((MainActivity)getActivity()).setHelpArticleVisible(mArticle.Comment != null && mArticle.Comment.length() > 0);
		}
	}

	

	public Article getCurrentArticle() {
		return mArticle;
	}

	public String getCurrentFilter() {
		return mFilter;
	}

	public int getCurrentSubjectID() {
		return mSubjectID;
	}

	private void setFormulaFragment(ImageAnim anim) {
		FragmentFormula fragment = new FragmentFormula();
		Bundle args = new Bundle();
		args.putParcelable("a", mArticle);
		args.putString("d", mOriginalDescription);
		args.putString("f", mFilter);
		args.putInt("s", mSubjectID);
		fragment.setArguments(args);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		if (anim == ImageAnim.left)
			t.setCustomAnimations(R.animator.slide_in_left,
					R.animator.slide_out_right);
		else if (anim == ImageAnim.right)
			t.setCustomAnimations(R.animator.slide_in_right,
					R.animator.slide_out_left);
		t.replace(R.id.container_formula, fragment, "FragmentFormula");
		mFabLeft.setVisibility(View.VISIBLE);
		mFabRight.setVisibility(View.VISIBLE);
		mRoot.setClickable(true);
		fragment.setFabs(mFabLeft, mFabRight);
		t.commit();
	}

	public void setNewPrefs() {
		FragmentManager fm = getFragmentManager();
		FragmentFormula fragment = (FragmentFormula) fm
				.findFragmentByTag("FragmentFormula");
		fragment.setNewPrefs();
	}

	public void setEmpty() {
		mFabLeft.setVisibility(View.GONE);
		mFabRight.setVisibility(View.GONE);
		mRoot.setClickable(false);
		FragmentManager fm = getFragmentManager();
		FragmentFormula fragment = (FragmentFormula) fm
				.findFragmentByTag("FragmentFormula");
		if(fragment != null)
			fragment.setEmpty();
	}

}
