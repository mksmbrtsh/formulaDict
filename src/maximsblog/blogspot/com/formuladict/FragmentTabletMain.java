package maximsblog.blogspot.com.formuladict;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class FragmentTabletMain extends Fragment {
	private int mId;
	private View mRoot;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mId = getArguments().getInt("id");
		mRoot = inflater.inflate(R.layout.main_tablet_layout, container, false);
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int paddingTop = ((MainActivity) getActivity()).getStatusBarHeight();
		TypedValue tv = new TypedValue();
		getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true);
		paddingTop += TypedValue.complexToDimensionPixelSize(tv.data,
				getResources().getDisplayMetrics());
		View div = mRoot.findViewById(R.id.div);
		LinearLayout.LayoutParams layoutParams = (LayoutParams) div
				.getLayoutParams();
		layoutParams.setMargins(0, paddingTop, 0, 0);
		div.setLayoutParams(layoutParams);
		if (savedInstanceState == null) {
			FragmentManager fragmentManager = getFragmentManager();
			Bundle args = new Bundle();
			args.putInt("id", mId);
			FragmentViewFormuls mainViewFragment = (FragmentViewFormuls) fragmentManager
					.findFragmentByTag("mainViewFragTable");
			mainViewFragment = new FragmentViewFormuls();
			mainViewFragment.setArguments(args);
			fragmentManager
					.beginTransaction()
					.replace(R.id.main_content, mainViewFragment,
							"mainViewFragTable").commit();
			MainListFragment mainListFragment = new MainListFragment();
			mainListFragment.setArguments(args);
			fragmentManager.beginTransaction()
					.replace(R.id.menu2, mainListFragment, "mainListFragTable")
					.commit();
		}
	};

	public void setFilter(String query) {
		FragmentManager fragmentManager = getFragmentManager();
		((MainListFragment) fragmentManager
				.findFragmentByTag("mainListFragTable")).setFilter(query);
	}

	public void setSubject(int id) {
		FragmentManager fragmentManager = getFragmentManager();
		MainListFragment mainListFragment = (MainListFragment) fragmentManager
				.findFragmentByTag("mainListFragTable");
		mainListFragment.setSubject(id);
	}

}
