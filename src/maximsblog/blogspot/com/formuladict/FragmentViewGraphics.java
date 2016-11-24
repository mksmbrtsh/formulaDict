package maximsblog.blogspot.com.formuladict;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentViewGraphics extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_view_graphics,
				container, false);
		SVGImageView image = (SVGImageView) root.findViewById(R.id.svg);
		String path = getArguments().getString("path");
		String source = readFromAssets(getActivity(), "graphics"
				+ File.separator + path.substring(0, path.indexOf("."))
				+ ".svg");
		String c = getActivity().getResources().getString(R.color.formula_graphics);
		if(c.length() != 7){
			c = "#" + c.substring(3, c.length());
		}
		if(!c.equals("#1F1A17"))
			source = source.replaceAll("#1F1A17", c);
		try {
			image.setSVG(SVG.getFromString(source));
		} catch (SVGParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}

	public static String readFromAssets(Context context, String filename) {
		// do reading, usually loop until end of file reading
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					context.getAssets().open(filename)));

			String mLine;

			mLine = reader.readLine();

			while (mLine != null) {
				sb.append(mLine); // process line
				sb.append(System.getProperty("line.separator"));
				mLine = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
