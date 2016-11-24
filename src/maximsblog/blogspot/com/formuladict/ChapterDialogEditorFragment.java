package maximsblog.blogspot.com.formuladict;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChapterDialogEditorFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final EditText editor = new EditText(getActivity());
		editor.setText(getArguments().getString("title"));
		editor.setTag(getArguments().getInt("id"));
		final Dialog d = new AlertDialog.Builder(getActivity())
				.setTitle(
						getArguments().getInt("id") == -1 ? getString(R.string.new_chapter)
								: getString(R.string.edit))
				.setView(editor)
				.setCancelable(true)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (editor.getText().length() == 0) {
									Toast.makeText(
											getActivity(),
											getString(R.string.title_chapter_not_set),
											Toast.LENGTH_LONG).show();
									return;
								}
								Intent intent = new Intent();
								intent.putExtra("name", editor.getText()
										.toString());
								if (editor.getTag() != null)
									intent.putExtra("id",
											(int) editor.getTag());
								getTargetFragment().onActivityResult(3,
										Activity.RESULT_OK, intent);
							}
						}).create();
		d.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Resources res = getActivity().getResources();
				int titleId = res.getIdentifier("alertTitle", "id",
						"android");
				if (titleId > 0) {
					TextView mTitle = (TextView) d.findViewById(titleId);
					mTitle.setTextColor(Color.WHITE);
				}
			}
		});
		d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		return d;
	}
}
