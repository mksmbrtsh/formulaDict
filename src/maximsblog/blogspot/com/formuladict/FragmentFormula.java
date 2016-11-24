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

public class FragmentFormula extends Fragment {
	private ImageView mImageView;
	private ObservableScrollView mScroll;
	private TextView mErrorText;
	private Article mArticle;
	private float mTextSize;
	private int mSubjectID;
	private TextView mTitle;
	private TextView mSubTitle;
	private String mFilter;
	private String mOriginalDescription;

	private BackgroundColorSpan mBackgroundColorSpan;
	private ForegroundColorSpan mForegroundColorSpan;
	private TeXIcon mIcon;
	private CustomHorizontalScrollView mHScroll;
	private View mRoot;
	private View mActionBarView;
	private FloatingActionButton mFabLeft;
	private FloatingActionButton mFabRight;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mBackgroundColorSpan = new BackgroundColorSpan(getActivity()
				.getResources().getColor(R.color.background_find_text));
		mForegroundColorSpan = new ForegroundColorSpan(getActivity()
				.getResources().getColor(R.color.formula_text));

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

		mActionBarView = inflater.inflate(R.layout.actionbar_title, null);
		mRoot = inflater.inflate(R.layout.fragment_formula, container, false);
		mImageView = (ImageView) mRoot.findViewById(R.id.logo);
		mImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x = event.getX();
				float y = event.getY();
				switch (event.getAction()) {
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_UP:
					if (mIcon != null) {
						Box b = hitTest(mIcon.getBox(), x, y);
						if (b != null) {
							Intent intent;
							if (getActivity() instanceof MainActivity) {
								intent = new Intent(getActivity(),
										ViewGraphicsActivity.class);
								intent.putExtra("p", ((MainActivity)getActivity()).getPurchased());
							} else {
								intent = new Intent(getActivity(),
										ViewGraphicsActivity2.class);
								intent.putExtra("p", ((ViewFormulaActivity)getActivity()).getPurchased());
							}
							intent.putExtra("path", b.token);
							intent.putExtra("a", mArticle);
							
							startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
							return false;
						}
					}
					break;
				}
				return true;
			}
		});
		mScroll = (ObservableScrollView) mRoot.findViewById(R.id.scroll);
		mHScroll = (CustomHorizontalScrollView) mRoot
				.findViewById(R.id.hscroll);
		mErrorText = (TextView) mRoot.findViewById(R.id.error);
		if (mFabLeft != null) {
			ScrollViewScrollDetectorImpl l1 = mFabLeft
					.attachToScrollView(mScroll);
			ScrollViewScrollDetectorImpl l2 = mFabRight
					.attachToScrollView(mScroll);
			mScroll.setOnScrollChangedListener(new ScrollChangedListener(l1, l2));
			l1 = mFabLeft.attachToHScrollView(mHScroll);
			l2 = mFabRight.attachToHScrollView(mHScroll);
			mHScroll.setOnScrollChangedListener(new ScrollChangedListener(l1,
					l2));
		}
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);

		mTitle = (TextView) mActionBarView.findViewById(R.id.action_title);
		mSubTitle = (TextView) mActionBarView
				.findViewById(R.id.action_subtitle);

		mTextSize = Float.valueOf((PreferenceManager
				.getDefaultSharedPreferences(getActivity()).getString(
				"text_size", "24")));

		if (getActivity() instanceof ViewFormulaActivity
				|| ((MainActivity) getActivity()).getTablet()) {
			getActivity().getActionBar().setDisplayShowCustomEnabled(true);
			getActivity().getActionBar().setDisplayShowTitleEnabled(false);
			getActivity().getActionBar().setCustomView(mActionBarView);
		}

		if (getActivity() instanceof MainActivity
				&& ((MainActivity) getActivity()).getTablet()) {
			mActionBarView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							ViewFormulaActivity.class);
					intent.putExtra("a", mArticle);
					intent.putExtra("s", mSubjectID);
					intent.putExtra("f", mFilter);
					startActivityForResult(intent, 1);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				}
			});
		} else {
			mActionBarView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().onBackPressed();
				}
			});
		}
		ViewTreeObserver vto = mImageView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
	};

	private OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

		@SuppressLint("NewApi")
		@Override
		public void onGlobalLayout() {
			if (Build.VERSION.SDK_INT < 16) {
				mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(
						onGlobalLayoutListener);
			} else {
				mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(
						onGlobalLayoutListener);
			}
			if (mArticle != null && getActivity() != null)
				initUI();
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

	private void initUI() {
		if (getActivity() instanceof ViewFormulaActivity
				|| ((MainActivity) getActivity()).getTablet()) {
			String title = Character.toString(mArticle.ChapterTitle.charAt(0))
					.toUpperCase() + mArticle.ChapterTitle.substring(1);
			mTitle.setText("\u00A7" + mArticle.ChapterNumber + ". " + title,
					TextView.BufferType.SPANNABLE);
			title = Character.toString(mArticle.Title.charAt(0)).toUpperCase()
					+ mArticle.Title.substring(1);
			mSubTitle.setText((mArticle.IndexRow + 1) + ". " + title,
					TextView.BufferType.SPANNABLE);
			if (mFilter.length() > 0) {
				int intColor = getResources().getColor(
						R.color.background_find_text);
				int red = Color.red(intColor);
				int green = Color.green(intColor);
				int blue = Color.blue(intColor);

				mArticle.Description = mArticle.Description.replaceAll(mFilter,
						"\\\\bgcolor{" + red + "," + green + "," + blue + "}{"
								+ mFilter + "}");

				Spannable str = (Spannable) mTitle.getText();
				int i = mTitle.getText().toString().toLowerCase()
						.indexOf(mFilter.toLowerCase());
				if (i != -1) {
					str.setSpan(mBackgroundColorSpan, i, i + mFilter.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					str.setSpan(mForegroundColorSpan, i, i + mFilter.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				str = (Spannable) mSubTitle.getText();
				i = mSubTitle.getText().toString().toLowerCase()
						.indexOf(mFilter.toLowerCase());
				if (i != -1) {
					str.setSpan(mBackgroundColorSpan, i, i + mFilter.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					str.setSpan(mForegroundColorSpan, i, i + mFilter.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		setFormula();
	}

	private class ScrollChangedListener implements
			ObservableScrollView.OnScrollChangedListener {

		private ScrollViewScrollDetectorImpl l1;
		private ScrollViewScrollDetectorImpl l2;

		public ScrollChangedListener(ScrollViewScrollDetectorImpl l1,
				ScrollViewScrollDetectorImpl l2) {
			this.l1 = l1;
			this.l2 = l2;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			l1.onScrollChanged(l, t, oldl, oldt);
			l2.onScrollChanged(l, t, oldl, oldt);
		}

	}

	private void setFormula() {
		int w = App.getMaxTextureSize();
		int h = w;
		TeXFormula f;
		try {
			f = new TeXFormula(mArticle.Description);
		} catch (JMathTeXException e) {
			String text = e.toString();
			mErrorText.setText(text);
			mErrorText.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
			return;
		}
		mErrorText.setVisibility(View.GONE);
		mImageView.setVisibility(View.VISIBLE);
		TeXFormula formula = f;
		mIcon = formula.new TeXIconBuilder()
				.setStyle(TeXConstants.STYLE_DISPLAY)
				.setSize(mTextSize)
				.setWidth(
						TeXConstants.UNIT_PIXEL,
						getActivity().getWindowManager().getDefaultDisplay()
								.getWidth(), TeXConstants.ALIGN_LEFT)
				.setIsMaxWidth(true)
				.setTrueValues(true)
				.setInterLineSpacing(TeXConstants.UNIT_PIXEL,
						AjLatexMath.getLeading(mTextSize)).build();
		mIcon.setInsets(new Insets(0, 0, 0, 0));
		if (mIcon.getIconWidth() > w) {
			String text = getString(R.string.big_width);
			mErrorText.setText(text);
			mErrorText.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
			return;
		} else if (mIcon.getIconHeight() > h) {
			String text = getString(R.string.big_heigth);
			mErrorText.setText(text);
			mErrorText.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
			return;
		} else {
			int bitmapWidht;
			bitmapWidht = mRoot.getWidth();
			if (mIcon.getIconWidth() > bitmapWidht) {
				bitmapWidht = mIcon.getIconWidth();
			}
			int bitmapHeight;
			bitmapHeight = mRoot.getHeight();
			if (mIcon.getIconHeight() > bitmapHeight) {
				bitmapHeight = mIcon.getIconHeight();
			}
			Bitmap image = Bitmap.createBitmap(bitmapWidht, bitmapHeight,
					Config.RGB_565);
			Canvas g2 = new Canvas(image);
			Paint p = new Paint();
			p.setShader(new BitmapShader(BitmapFactory.decodeResource(getResources(), R.drawable.gridpaperlightbluepattern), TileMode.REPEAT, TileMode.REPEAT));
			g2.drawRect(0, 0, bitmapWidht,bitmapHeight,p);
			mIcon.paintIcon(g2, 0, 0);
			Bitmap scaleImage = image;
			mImageView.setImageBitmap(scaleImage);
		}
	}

	public Bitmap scaleBitmapAndKeepRation(Bitmap targetBmp,
			int reqHeightInPixels, int reqWidthInPixels) {
		Bitmap bitmap = Bitmap.createBitmap(reqWidthInPixels,
				reqHeightInPixels, Config.ARGB_8888);
		Canvas g = new Canvas(bitmap);
		g.drawBitmap(targetBmp, 0, 0, null);
		targetBmp.recycle();
		return bitmap;
	}

	private Box hitTest(Box box, float x, float y) {
		Box out = box;
		for (Box c : box.getChildrens()) {
			out = hitTest(c, x, y);
			if (out != null && out instanceof IHintTest) {
				if (((IHintTest) out).hitTest(x, y, mTextSize))
					return out;
				else
					return null;
			}
		}
		if (out != null && out instanceof IHintTest) {
			if (((IHintTest) out).hitTest(x, y, mTextSize))
				return out;
			else
				return null;
		}
		return null;
	}

	public void deleteFilters() {
		mArticle.Description = mOriginalDescription;
		mFilter = "";
		if (getActivity() instanceof ViewFormulaActivity
				|| ((MainActivity) getActivity()).getTablet()) {
			Spannable str = (Spannable) mTitle.getText();
			mTitle.setText(str.toString());
			str = (Spannable) mSubTitle.getText();
			mSubTitle.setText(str.toString());
		}
		setFormula();
	}

	private void setIntent(Intent intent) {
		mArticle = intent.getParcelableExtra("a");
		mOriginalDescription = new String(mArticle.Description);
		mSubjectID = intent.getIntExtra("s", -1);
		mFilter = intent.getStringExtra("f");
		initUI();
	}

	public Article getCurrentArticle() {
		return mArticle;
	}

	public String getCurrentFilter() {
		return mFilter;
	}

	public void setNewPrefs() {
		mTextSize = Float.valueOf((PreferenceManager
				.getDefaultSharedPreferences(getActivity()).getString(
				"text_size", "32")));
		setFormula();
	}

	public int getCurrentSubjectID() {
		return mSubjectID;
	}

	public void setFabs(FloatingActionButton fabLeft,
			FloatingActionButton fabRight) {
		mFabLeft = fabLeft;
		mFabRight = fabRight;
	}

	public void setEmpty() {
		mErrorText.setVisibility(View.GONE);
		mImageView.setVisibility(View.VISIBLE);
		mImageView.setImageResource(android.R.color.transparent);
	}
}
