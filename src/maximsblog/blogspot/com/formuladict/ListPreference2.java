package maximsblog.blogspot.com.formuladict;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ListPreference2 extends ListPreference {

	public ListPreference2(Context context) {
		super(context);
	}

	public ListPreference2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	public ListPreference2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public ListPreference2(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void showDialog(android.os.Bundle state) {
		super.showDialog(state);
		Dialog d = super.getDialog();
		Resources res = getContext().getResources();
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
	};
}
