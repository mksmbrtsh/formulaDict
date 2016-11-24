package maximsblog.blogspot.com.formuladict;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public void decorTop(View rootView){
		int paddingTop = ((MainActivity)getActivity()).getStatusBarHeight();
		TypedValue tv = new TypedValue();
		getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
				true);
		paddingTop += TypedValue.complexToDimensionPixelSize(tv.data,
				getResources().getDisplayMetrics());
		rootView.setPadding(0, paddingTop, 0, 0);
	}
}
