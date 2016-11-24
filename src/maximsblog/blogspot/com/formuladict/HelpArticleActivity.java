package maximsblog.blogspot.com.formuladict;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.JMathTeXException;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula.TeXIconBuilder;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.InflateException;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpArticleActivity extends BaseActivity {

	private Article mArticle;
	private TextView mErrorText;
	private ImageView mImageView;
	private Float mTextSize;
	private int subjectID;
	private View mRoot;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		BackgroundColorSpan mBackgroundColorSpan = new BackgroundColorSpan(getResources().getColor(R.color.background_find_text));
		ForegroundColorSpan mForegroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.formula_text));
		ActionBar bar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		bar.setBackgroundDrawable(d);
		View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_title, null);
		TextView mTitle = (TextView) mActionBarView.findViewById(R.id.action_title);
		TextView mSubTitle = (TextView) mActionBarView
				.findViewById(R.id.action_subtitle);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setCustomView(mActionBarView);
		setContentView(R.layout.activity_help_article);
		mRoot = findViewById(R.id.formula_area);
		mImageView = (ImageView) findViewById(R.id.logo);
		mErrorText = (TextView) findViewById(R.id.error);
		mArticle = getIntent().getParcelableExtra("a");
		subjectID = getIntent().getIntExtra("s", -1);
		String mFilter = getIntent().getStringExtra("f");
		mTextSize = Float
				.valueOf((PreferenceManager.getDefaultSharedPreferences(this)
						.getString("text_size", "24")));
		String title = Character.toString(mArticle.ChapterTitle.charAt(0))
				.toUpperCase() + mArticle.ChapterTitle.substring(1);
		mTitle.setText("\u00A7" + mArticle.ChapterNumber + ". " + title,
				TextView.BufferType.SPANNABLE);
		title = Character.toString(mArticle.Title.charAt(0)).toUpperCase()
				+ mArticle.Title.substring(1);
		mSubTitle.setText((mArticle.IndexRow + 1) + ". " + title,
				TextView.BufferType.SPANNABLE);
		if (mFilter != null && mFilter.length() > 0) {
			int intColor = getResources().getColor(
					R.color.background_find_text);
			int red = Color.red(intColor);
			int green = Color.green(intColor);
			int blue = Color.blue(intColor);

			mArticle.Comment = mArticle.Comment.replaceAll(mFilter,
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
		
		
		
		
		mImageView.post(new Runnable() {

			@Override
			public void run() {
				int w = App.getMaxTextureSize();
				int h = w;
				TeXFormula f;
				try {
					f = new TeXFormula(mArticle.Comment);
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
				TeXIcon mIcon = formula.new TeXIconBuilder()
						.setStyle(TeXConstants.STYLE_DISPLAY)
						.setSize(mTextSize)
						.setWidth(
								TeXConstants.UNIT_PIXEL,
								getWindowManager().getDefaultDisplay()
										.getWidth(), TeXConstants.ALIGN_LEFT)
						.setIsMaxWidth(true)
						.setTrueValues(true)
						.setInterLineSpacing(TeXConstants.UNIT_PIXEL,
								AjLatexMath.getLeading(mTextSize)).build();
				mIcon.setInsets(new Insets(0, 0, 0, 0));
				if (mIcon.getIconWidth() > w) {
					String text = "большая ширина";
					mErrorText.setText(text);
					mErrorText.setVisibility(View.VISIBLE);
					mImageView.setVisibility(View.GONE);
					return;
				} else if (mIcon.getIconHeight() > h) {
					String text = "большая высота";
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
					Bitmap image = Bitmap.createBitmap(bitmapWidht,
							bitmapHeight, Config.RGB_565);
					Canvas g2 = new Canvas(image);
					Paint p = new Paint();
					p.setShader(new BitmapShader(BitmapFactory.decodeResource(
							getResources(),
							R.drawable.gridpaperlightbluepattern),
							TileMode.REPEAT, TileMode.REPEAT));
					g2.drawRect(0, 0, bitmapWidht, bitmapHeight, p);
					mIcon.paintIcon(g2, 0, 0);
					Bitmap scaleImage = image;
					mImageView.setImageBitmap(scaleImage);
				}
			}
		});
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
