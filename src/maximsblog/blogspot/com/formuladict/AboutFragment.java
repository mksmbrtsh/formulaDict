package maximsblog.blogspot.com.formuladict;

import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AboutFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_about,container, false);
		TextView t = (TextView) view.findViewById(R.id.note_text);
		t.setText(getResources().getText(R.string.about_text));
		Linkify.addLinks(t, Linkify.ALL);
		t.setMovementMethod(LinkMovementMethod.getInstance());
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			String nameversion = getString(R.string.version) + " " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			getActivity().getActionBar().setTitle(getString(R.string.app_name) + " " + nameversion);
		} catch (NameNotFoundException e) {

		}
	}
}