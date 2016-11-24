package maximsblog.blogspot.com.formuladict;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EditorActivity extends BaseActivity implements
		AdapterView.OnItemSelectedListener {

	private static final int SAVE_LATEX = 1;
	private static final int LOAD_LATEX = 2;
	private float mTextSize;
	private String actualLatexFormula;
	private EditText mTitle;
	private Spinner mTextSizeSpinner;

	@Override
	protected void setContentView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_editor);
		if (savedInstanceState != null) {
			mTextSize = savedInstanceState.getFloat("textsize");
		} else {
			mTextSize = Float.valueOf((PreferenceManager
					.getDefaultSharedPreferences(this).getString("text_size",
					"24")));
		}
		setActionBar();

		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentByTag("myFragmentTag");

		if (fragment == null) {
			FragmentTransaction ft = fm.beginTransaction();
			fragment = RawFragment.newInstance();
			Bundle b = new Bundle();
			b.putParcelable("a", getIntent().getParcelableExtra("a"));
			fragment.setArguments(b);
			ft.add(R.id.container, fragment, "myFragmentTag");
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editor_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			FileDialogSelect(SAVE_LATEX);
			break;
		case R.id.load:
			FileDialogSelect(LOAD_LATEX);
			break;
		case R.id.remove:
			FragmentManager fm = getFragmentManager();
			RawFragment fragment = (RawFragment) fm
					.findFragmentByTag("myFragmentTag");
			fragment.remove();
			setResult(RESULT_OK);
			super.onBackPressed();
			break;
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void FileDialogSelect(int requestCode) {
		Intent intent = new Intent(this, FileDialog.class);
		intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { ".latex",
				".tex" });
		int mod = SelectionMode.MODE_OPEN;
		switch (requestCode) {
		case SAVE_LATEX:
			mod = SelectionMode.MODE_CREATE;
			break;
		case LOAD_LATEX:
			mod = SelectionMode.MODE_OPEN;
			break;
		}

		intent.putExtra(FileDialog.SELECTION_MODE, mod);
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String path = data.getStringExtra(FileDialog.RESULT_PATH);
			File file;
			switch (requestCode) {
			case SAVE_LATEX:
				if (!path.endsWith(".latex"))
					path += ".latex";
				file = new File(path);
				if (file.exists()) {
					Toast.makeText(this, getString(R.string.error_cr_file),
							Toast.LENGTH_LONG).show();
				} else {
					try {
						if (file.createNewFile()) {
							FileWriter fStream = new FileWriter(file);
							BufferedWriter out = new BufferedWriter(fStream);
							String latex = getActualLatexFormula();
							out.write(latex, 0, latex.length());
							out.flush();
							out.close();
							fStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(this,
								getString(R.string.error_write_file),
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case LOAD_LATEX:
				file = new File(path);
				try {
					FileReader fStream = new FileReader(file);
					BufferedReader in = new BufferedReader(fStream);
					StringBuilder sb = new StringBuilder();
					String line;
					do {
						line = in.readLine();
						if (line != null)
							sb.append(line);
					} while (line != null);
					in.close();
					fStream.close();
					String latex = sb.toString();
					setActualLatex(latex);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Toast.makeText(this, getString(R.string.error_open_file),
							Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this, getString(R.string.error_open_file),
							Toast.LENGTH_LONG).show();
				}

				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putFloat("textsize", mTextSize);
		super.onSaveInstanceState(outState);
	};

	private void setActionBar() {
		ActionBar actionBar = getActionBar();
		LayoutInflater inflater;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.rawactiontitle, null);
		mTextSizeSpinner = (Spinner) layout.findViewById(R.id.textsize);
		mTitle = (EditText) layout.findViewById(R.id.title);
		mTitle.setText(((Article) getIntent().getParcelableExtra("a")).Title);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.title_spinner_textview, android.R.id.text1,
				getResources().getStringArray(R.array.text_size_array));
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		mTextSizeSpinner.setAdapter(adapter);
		mTextSizeSpinner.setSelection((int) mTextSize - 10);
		mTextSizeSpinner.setOnItemSelectedListener(this);
		LayoutParams lp = new LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT
						| Gravity.CENTER_VERTICAL);
		actionBar.setCustomView(layout, lp);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		Drawable d = getResources().getDrawable(R.drawable.main_fill_gradient);
		actionBar.setBackgroundDrawable(d);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		FragmentManager fm = getFragmentManager();
		RawFragment fragment = (RawFragment) fm
				.findFragmentByTag("myFragmentTag");
		mTextSize = 10 + position;
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		editor.putString("text_size", String.valueOf((int) mTextSize));
		editor.commit();
		fragment.setTextSize();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	public float getTextSize() {
		return mTextSize;
	}

	public String getActualLatexFormula() {
		FragmentManager fm = getFragmentManager();
		RawFragment fragment = (RawFragment) fm
				.findFragmentByTag("myFragmentTag");
		actualLatexFormula = fragment.getLatex();
		return actualLatexFormula;
	}

	public void setActualLatex(String actualLatex) {
		FragmentManager fm = getFragmentManager();
		RawFragment fragment = (RawFragment) fm
				.findFragmentByTag("myFragmentTag");
		fragment.setLatex(actualLatex);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		RawFragment fragment = (RawFragment) fm
				.findFragmentByTag("myFragmentTag");
		String title = mTitle.getText().toString();
		Article a = fragment.getCurrentArticle();
		StringBuilder sb = new StringBuilder();
		boolean flagNotCreate1 = false;
		boolean flagNotCreate2 = false;
		if (title == null || title.length() == 0) {
			sb.append(getString(R.string.title_not_set));
			flagNotCreate1 = true;
		}
		if (a.Description == null || a.Description.length() == 0) {
			if (flagNotCreate1)
				sb.append("\n");
			sb.append(getString(R.string.description_not_set));
			flagNotCreate2 = true;
		}
		if ((a.Comment != null && a.Comment.length() != 0) && (flagNotCreate1 && flagNotCreate2)) {
			ErrorCreateDialogFragment errorCreateDialogFragment = new ErrorCreateDialogFragment();
			Bundle args = new Bundle();
			args.putString("msg", getString(R.string.only_comment_not_save));
			errorCreateDialogFragment.setArguments(args);
			errorCreateDialogFragment.show(getFragmentManager(), "errorCreateDialog");
			return;
		}
		if (flagNotCreate1 && flagNotCreate2 && (a.Comment == null || a.Comment.length() == 0)) {
			setResult(RESULT_CANCELED);
			EditorActivity.super.onBackPressed();
			finish();
			overridePendingTransition(
					R.anim.slide_in_left,
					R.anim.slide_out_right);
		} else if (flagNotCreate1 || flagNotCreate2 ) {
			ErrorCreateDialogFragment errorCreateDialogFragment = new ErrorCreateDialogFragment();
			Bundle args = new Bundle();
			args.putString("msg", sb.toString());
			errorCreateDialogFragment.setArguments(args);
			errorCreateDialogFragment.show(getFragmentManager(), "errorCreateDialog");
		} else {
			fragment.saveArticle(title);
			Intent data = new Intent();
			data.putExtra("a", a);
			setResult(RESULT_OK, data);
			super.onBackPressed();
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}
	
	private class ErrorCreateDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String msg = getArguments().getString("msg");
			Dialog d = new AlertDialog.Builder(getActivity())
					.setTitle(R.string.error_create_row_dialog)
					.setMessage(msg)
					.setNegativeButton(getString(android.R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ErrorCreateDialogFragment.this.dismiss();
								}
							})
					.setPositiveButton(getString(R.string.close_not_save),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ErrorCreateDialogFragment.this.dismiss();
									setResult(RESULT_CANCELED);
									EditorActivity.super.onBackPressed();
									finish();
									overridePendingTransition(
											R.anim.slide_in_left,
											R.anim.slide_out_right);
								}
							}).create();
			d.getWindow().setBackgroundDrawableResource(R.drawable.main_fill_gradient);
			d.setOnShowListener(new DialogInterface.OnShowListener() {
				
				@Override
				public void onShow(DialogInterface dialog) {
					Dialog d = getDialog();
					Resources res = getResources();
					// Title divider
					final int titleDividerId = res.getIdentifier("titleDivider", "id",
							"android");
					final View titleDivider = d.findViewById(titleDividerId);
					if (titleDivider != null) {
						titleDivider.setBackgroundColor(res
								.getColor(R.color.dark_main_gradient));
					}
					int titleId = res.getIdentifier("alertTitle", "id", "android");
					if (titleId > 0) {
						TextView mTitle = (TextView) d.findViewById(titleId);
						mTitle.setTextColor(Color.WHITE);
					}
					TextView messageView = (TextView)d.findViewById(android.R.id.message);
					//messageView.setTextColor(Color.B);
					((ViewGroup)messageView.getParent()).setBackgroundResource(android.R.color.white);
				}
			});
			
			return d;
		}
	}

}
