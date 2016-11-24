package maximsblog.blogspot.com.formuladict;

import java.util.ArrayList;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.Box;
import maximsblog.blogspot.com.jlatexmath.core.IHintTest;
import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.JMathTeXException;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.MetricAffectingSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RawFragment extends Fragment implements OnClickListener,
		TextView.OnEditorActionListener, TextWatcher,
		OnGesturePerformedListener, OnLongClickListener,
		OnActionItemClickListener, OnCheckedChangeListener {

	private float mTextSize;

	public static Fragment newInstance() {
		RawFragment fragment = new RawFragment();
		return fragment;
	}

	private ImageView mImageView;

	private MultiAutoCompleteTextView mFormulaText;
	TeXFormula formula;
	TeXIcon icon;
	private TextView mErrorText;
	private AutoCompleteFormulaAdapter mAdapter;
	private ArrayList<String> mTerms;
	private ArrayList<View> mAlphabetKeys;
	private GestureLibrary mGestureLib;
	private Article mArticle;

	private ScrollView mScroll;

	private Switch mSwitch;

	private LinearLayout layout;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("a", mArticle);
		super.onSaveInstanceState(outState);
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mArticle = savedInstanceState.getParcelable("a");
		} else {
			mArticle = getArguments().getParcelable("a");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mTextSize = ((EditorActivity) getActivity()).getTextSize();
		layout = (LinearLayout) inflater.inflate(R.layout.fragment_example,
				container, false);
		mImageView = (ImageView) layout.findViewById(R.id.logo);
		mFormulaText = (MultiAutoCompleteTextView) layout
				.findViewById(R.id.formulaEdit);
		mSwitch = (Switch) layout.findViewById(R.id.editor_switch);
		mScroll = (ScrollView) layout.findViewById(R.id.scroll);
		mFormulaText.requestFocus();
		mErrorText = (TextView) layout.findViewById(R.id.error);
		mFormulaText.setText(mArticle.Description);

		mGestureLib = GestureLibraries.fromRawResource(getActivity(),
				R.raw.gesture);
		mGestureLib.load();

		GestureOverlayView gestures = (GestureOverlayView) layout
				.findViewById(R.id.gestures);
		gestures.addOnGesturePerformedListener(this);

		setFormula();
		mFormulaText.addTextChangedListener(this);
		mSwitch.setOnCheckedChangeListener(this);
		mTerms = AjLatexMath.getListAllCommands();
		mAdapter = new AutoCompleteFormulaAdapter(getActivity(), mTerms);
		mFormulaText.setTokenizer(new SpaceTokenizer());
		mFormulaText.setThreshold(1);
		mFormulaText.setAdapter(mAdapter);
		mFormulaText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		mFormulaText.setOnEditorActionListener(this);
		mFormulaText.setRawInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		mFormulaText.setVerticalFadingEdgeEnabled(true);
		mFormulaText.setVerticalScrollBarEnabled(true);
		mFormulaText.setSingleLine(false);
		mFormulaText.setMinLines(4);
		mFormulaText.setMaxLines(4);
		layout.findViewById(R.id.btn0).setOnClickListener(this);
		layout.findViewById(R.id.btn1).setOnClickListener(this);
		layout.findViewById(R.id.btn2).setOnClickListener(this);
		layout.findViewById(R.id.btn3).setOnClickListener(this);
		layout.findViewById(R.id.btn4).setOnClickListener(this);
		layout.findViewById(R.id.btn5).setOnClickListener(this);
		layout.findViewById(R.id.btn6).setOnClickListener(this);
		layout.findViewById(R.id.btn7).setOnClickListener(this);
		layout.findViewById(R.id.btn8).setOnClickListener(this);
		layout.findViewById(R.id.btn9).setOnClickListener(this);

		mAlphabetKeys = new ArrayList<View>();
		mAlphabetKeys.add(layout.findViewById(R.id.q));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.w));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.e));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.r));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.t));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.y));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.u));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.i));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.o));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.p));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);

		mAlphabetKeys.add(layout.findViewById(R.id.a));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.s));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.d));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.f));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.g));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.h));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.j));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.k));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.l));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		layout.findViewById(R.id.btn_slash).setOnClickListener(this);

		layout.findViewById(R.id.caps).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.z));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.x));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.c));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.v));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.b));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.n));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		mAlphabetKeys.add(layout.findViewById(R.id.m));
		mAlphabetKeys.get(mAlphabetKeys.size() - 1).setOnClickListener(this);
		layout.findViewById(R.id.space).setOnClickListener(this);
		layout.findViewById(R.id.del).setOnClickListener(this);
		layout.findViewById(R.id.Button02).setOnClickListener(this);
		RelativeLayout curlyBrackets = (RelativeLayout) layout
				.findViewById(R.id.curly_brackets);
		curlyBrackets.setOnClickListener(this);
		curlyBrackets.setOnLongClickListener(this);
		layout.findViewById(R.id.plus_btn).setOnClickListener(this);
		layout.findViewById(R.id.minus_btn).setOnClickListener(this);
		layout.findViewById(R.id.div_button).setOnClickListener(this);
		layout.findViewById(R.id.sep_button).setOnClickListener(this);
		RelativeLayout supersubscript = (RelativeLayout) layout
				.findViewById(R.id.supersubscript);
		supersubscript.setOnClickListener(this);
		RelativeLayout commons = (RelativeLayout) layout
				.findViewById(R.id.commons);
		commons.setOnClickListener(this);
		RelativeLayout dots = (RelativeLayout) layout.findViewById(R.id.dots);
		dots.setOnClickListener(this);
		dots.setOnLongClickListener(this);
		supersubscript.setOnLongClickListener(this);
		commons.setOnLongClickListener(this);
		RelativeLayout equals = (RelativeLayout) layout
				.findViewById(R.id.equals);
		equals.setOnClickListener(this);
		equals.setOnLongClickListener(this);
		return layout;
	}

	private void setFormula() {
		int w = App.getMaxTextureSize();
		int h = w;
		MultiAutoCompleteTextView mactv;
		if (mSwitch.isChecked())
			mArticle.Description = mFormulaText.getText().toString();
		else
			mArticle.Comment = mFormulaText.getText().toString();

		TeXFormula f;
		try {
			if (mSwitch.isChecked())
				f = new TeXFormula(mArticle.Description);
			else
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
		formula = f;
		icon = formula.new TeXIconBuilder()
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
		icon.setInsets(new Insets(0, 0, 0, 0));
		if (icon.getIconWidth() > w) {
			String text = "большая ширина";
			mErrorText.setText(text);
			mErrorText.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
			return;
		} else if (icon.getIconHeight() > h) {
			String text = "большая высота";
			mErrorText.setText(text);
			mErrorText.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
			return;
		} else {
			if (icon.getIconHeight() == 0 && icon.getIconWidth() == 0)
				return;
			Bitmap image = Bitmap.createBitmap(icon.getIconWidth(),
					icon.getIconHeight(), Config.ARGB_8888);

			Canvas g2 = new Canvas(image);
			g2.drawColor(Color.WHITE);
			icon.paintIcon(g2, 0, 0);
			Bitmap scaleImage = image;// scaleBitmapAndKeepRation(image, h, w);
			mImageView.setImageBitmap(scaleImage);
			mScroll.fullScroll(ScrollView.FOCUS_DOWN);
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

	@Override
	public void onClick(View v) {
		int start;
		int stop;
		String replacement;
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		start = mactv.getSelectionStart();
		stop = mactv.getSelectionEnd();
		switch (v.getId()) {
		case R.id.caps:
			Button caps = (Button) v;
			boolean toUpper = false;
			if (caps.getText().equals("\u2191")) {
				caps.setText("\u2193");
				toUpper = true;
			} else {
				caps.setText("\u2191");
				toUpper = false;
			}
			for (View key : mAlphabetKeys) {
				Button keyBtn = (Button) key;
				keyBtn.setText(toUpper ? keyBtn.getText().toString()
						.toUpperCase() : keyBtn.getText().toString()
						.toLowerCase());
			}
			break;
		case R.id.curly_brackets:
			if (mactv.getText().toString().lastIndexOf("\\mbox") == start
					- "\\mbox".length()) {
				View view = getActivity().getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(view, 0);
				}
			}
			insetbrackets("{}");
			break;
		case R.id.dots:
			insertString(start, stop, "\\cdot ");
			break;
		case R.id.equals:
			insertString(start, stop, "=");
			break;
		case R.id.commons:
			insertString(start, stop, ".");
			break;
		case R.id.supersubscript:
			insertString(start, stop, "^");
			break;
		default:
			replacement = ((Button) v).getText().toString();
			switch (replacement) {
			case "\\":
				int startSelection = insertString(start, stop, "\\");
				showDropDown(mactv.getText().toString(), startSelection);
				break;
			case "\u21b2":// enter
				insertString(start, stop, "\n");
				break;
			case "\u2190":// del
				delete(start, stop);
				break;
			case "\u2423":// space
				insertString(start, stop, " ");
				break;
			default:
				StringBuilder sb = new StringBuilder(mactv.getText().toString());
				sb.replace(start, stop, replacement);
				startSelection = start + replacement.length();
				showDropDown(sb.toString(), startSelection);
				break;
			}
			setFormula();
			break;
		}
	}

	private void delete(int start, int stop) {
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		StringBuilder sb = new StringBuilder(mactv.getText().toString());
		if (start == stop && start > 0)
			start--;
		if (sb.substring(start, stop).equals("{")) {
			if (stop < sb.length()) {
				if (sb.charAt(stop) == '}')
					stop++;
			}
		}
		if (sb.substring(start, stop).equals("[")) {
			if (stop < sb.length()) {
				if (sb.charAt(stop) == '}')
					stop++;
			}
		}
		if (sb.substring(start, stop).equals("<")) {
			if (stop < sb.length()) {
				if (sb.charAt(stop) == '>')
					stop++;
			}
		}
		if (sb.substring(start, stop).equals("|")) {
			if (stop < sb.length()) {
				if (sb.charAt(stop) == '|')
					stop++;
			}
		}
		if (sb.substring(start, stop).equals("(")) {
			if (stop < sb.length()) {
				if (sb.charAt(stop) == ')')
					stop++;
			}
		}
		int s = start;
		boolean ok = false;
		boolean stopSearch = false;
		while (sb.length() > 0 && !ok && !stopSearch && s >= 0) {
			char c = sb.charAt(s);
			if (c == '\\') {
				ok = true;
				break;
			} else {
				if (!Character.isLetter(c))
					stopSearch = true;
			}
			s--;
		}
		if (ok) {
			String command = sb.substring(s, stop);
			for (String t : mTerms) {
				if (t.equals(command)) {
					start = s;
					break;
				}
			}
		}
		sb.replace(start, stop, "");
		int startSelection = start;
		mactv.setText(sb.toString());
		mactv.setSelection(startSelection);
	}

	private int insertString(int start, int stop, String string) {
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		int startSelection;
		StringBuilder sb = new StringBuilder(mactv.getText().toString());
		sb.replace(start, stop, string);
		startSelection = start + string.length();
		mactv.setText(sb.toString());
		mactv.setSelection(startSelection);
		return startSelection;
	}

	private void insetbrackets(String brackets) {
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		int start;
		int stop;
		String replacement;
		int startSelection;
		StringBuilder sb = new StringBuilder(mactv.getText().toString());
		start = mactv.getSelectionStart();
		stop = mactv.getSelectionEnd();
		sb.replace(start, stop, replacement = brackets);
		startSelection = start + replacement.length() - 1;
		mactv.setText(sb.toString());
		mactv.setSelection(startSelection);
	}

	private void showDropDown(String result, int cursor) {
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		mactv.setText(result);
		SpaceTokenizer ss = new SpaceTokenizer();
		mAdapter.getFilter().filter(
				result.substring(ss.findTokenStart(result, cursor), cursor));
		mactv.setSelection(cursor);
		mactv.showDropDown();
	}

	@Override
	public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
		setFormula();
		return true;
	}

	public void setTextSize() {
		mTextSize = ((EditorActivity) getActivity()).getTextSize();
		setFormula();
	}

	public String getLatex() {
		return mArticle.Description;
	}

	public void setLatex(String latex) {
		mFormulaText.setText(mArticle.Description = latex);
		setFormula();
	}

	public class TypefaceSpan extends MetricAffectingSpan {
		private Typeface mTypeface;

		public TypefaceSpan(Typeface typeface) {
			mTypeface = typeface;
		}

		@Override
		public void updateMeasureState(TextPaint p) {
			p.setTypeface(mTypeface);
			p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.setTypeface(mTypeface);
			tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		setFormula();
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				int start = mactv.getSelectionStart();
				if (prediction.name.equals("prev")) {
					if (start - 1 >= 0)
						start--;

				} else if (prediction.name.equals("next")) {
					if (start + 1 <= mactv.getText().length())
						start++;
				}
				mactv.setSelection(start);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		final QuickAction quickAction = new QuickAction(getActivity());
		switch (v.getId()) {
		case R.id.curly_brackets:
			quickAction.setGroupId(R.id.curly_brackets);
			quickAction.addActionItem(new ActionItem(R.id.parentheses, "()"));
			quickAction
					.addActionItem(new ActionItem(R.id.square_brackets, "[]"));
			quickAction.addActionItem(new ActionItem(R.id.vertical_brackets,
					"||"));
			quickAction.addActionItem(new ActionItem(R.id.inequality_signs,
					"<>"));
			break;
		case R.id.dots:
			quickAction.setGroupId(R.id.dots);
			quickAction.addActionItem(new ActionItem(R.id.times, "\u00D7"));
			quickAction.addActionItem(new ActionItem(R.id.ast, "*"));
			break;
		case R.id.equals:
			quickAction.setGroupId(R.id.equals);
			quickAction.addActionItem(new ActionItem(R.id.more, ">"));
			quickAction.addActionItem(new ActionItem(R.id.less, "<"));
			quickAction.addActionItem(new ActionItem(R.id.leqslant, "\u2A7D"));
			quickAction.addActionItem(new ActionItem(R.id.geqslant, "\u2a7e"));
			quickAction.addActionItem(new ActionItem(R.id.equiv, "\uef72"));
			break;
		case R.id.commons:
			quickAction.setGroupId(R.id.commons);
			quickAction.addActionItem(new ActionItem(R.id.comma, ","));
			quickAction.addActionItem(new ActionItem(R.id.dot, ","));
			break;
		case R.id.supersubscript:
			quickAction.setGroupId(R.id.supersubscript);
			quickAction.addActionItem(new ActionItem(R.id.superscript, "^"));
			quickAction.addActionItem(new ActionItem(R.id.subscript, "_"));
			break;
		}
		quickAction.setOnActionItemClickListener(this);
		quickAction.show(v);
		return false;
	}

	@Override
	public void onItemClick(QuickAction quickAction, int pos, int actionId,
			int groupId) {
		ActionItem action = quickAction.getActionItem(pos);
		String replacement = "";
		switch (groupId) {
		case R.id.curly_brackets:
			insetbrackets(action.getTitle());
			return;
		case R.id.dots:
			if (action.getActionId() == R.id.times)
				replacement = "\\times ";
			if (action.getActionId() == R.id.ast)
				replacement = "*";
			break;
		case R.id.equals:
			if (action.getActionId() == R.id.more)
				replacement = ">";
			else if (action.getActionId() == R.id.less)
				replacement = "<";
			else if (action.getActionId() == R.id.leqslant)
				replacement = "\\leqslant ";
			else if (action.getActionId() == R.id.geqslant)
				replacement = "\\geqslant ";
			else if (action.getActionId() == R.id.equiv)
				replacement = "\\equiv ";
			break;
		case R.id.commons:
			if (action.getActionId() == R.id.dot)
				replacement = ".";
			else if (action.getActionId() == R.id.comma)
				replacement = ",";
			break;
		case R.id.supersubscript:
			if (action.getActionId() == R.id.superscript)
				replacement = "^";
			else if (action.getActionId() == R.id.subscript)
				replacement = "_";
			break;
		}
		int start;
		int stop;
		int startSelection;
		MultiAutoCompleteTextView mactv;
		mactv = mFormulaText;
		StringBuilder sb = new StringBuilder(mactv.getText().toString());
		start = mactv.getSelectionStart();
		stop = mactv.getSelectionEnd();
		sb.replace(start, stop, replacement);
		startSelection = start + replacement.length();
		mactv.setText(sb.toString());
		mactv.setSelection(startSelection);
	}

	public void saveArticle(String title) {
		ContentValues values = new ContentValues();
		if (mArticle.ArticlesID != -1)
			values.put(DataBaseHelper.Tables.Articles.Column.ArticlesID,
					mArticle.ArticlesID);
		values.put(DataBaseHelper.Tables.Articles.Column.ChapterID,
				mArticle.ChapterID);
		values.put(DataBaseHelper.Tables.Articles.Column.Description,
				mArticle.Description);
		values.put(DataBaseHelper.Tables.Articles.Column.Title, title);
		values.put(DataBaseHelper.Tables.Articles.Column.Comment,
				mArticle.Comment);
		values.put(DataBaseHelper.Tables.Articles.Column.LanguageID,
				mArticle.LanguageID);
		values.put(DataBaseHelper.Tables.Articles.Column.IndexRow,
				mArticle.IndexRow);
		getActivity().getContentResolver().insert(
				DataBaseProvider.CONTENT_URI_Articles, values);
	}

	public void remove() {
		getActivity().getContentResolver().delete(
				DataBaseProvider.CONTENT_URI_Articles, "ArticlesID = ?",
				new String[] { String.valueOf(mArticle.ArticlesID) });
	}

	public Article getCurrentArticle() {
		return mArticle;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked)
			mFormulaText.setText(mArticle.Description);
		else
			mFormulaText.setText(mArticle.Comment);
		setFormula();
	}

}
