/* GraphicsAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 */

package maximsblog.blogspot.com.jlatexmath.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import maximsblog.blogspot.com.formuladict.R;
import maximsblog.blogspot.com.jlatexmath.cache.JLaTeXMathCache;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

/**
 * An atom representing an atom containing a graphic.
 */
public class GraphicsAtom extends Atom {

	private Bitmap image = null;
	private Bitmap bimage;
	private int w, h;

	private Atom base;
	private boolean first = true;
	private int interp = -1;
	private String path;

	public GraphicsAtom(int index, String token, String path, String option) {
		super(index, token);
		this.path = path;
		// Read an SVG from the assets folder
		SVG svg;
		boolean ok = true;
		try {
			String source = readFromAssets(AjLatexMath.getContext(), "graphics"
					+ File.separator + path.substring(0, path.indexOf("."))
					+ ".svg");
			String c = AjLatexMath.getContext().getResources().getString(R.color.formula_graphics);
			if(c.length() != 7){
				c = "#" + c.substring(3, c.length());
			}
			if(!c.equals("#1F1A17"))
				source = source.replaceAll("#1F1A17", c);
			svg = SVG.getFromString(source);
			// Create a canvas to draw onto
			if (svg.getDocumentWidth() != -1) {
				Resources r = AjLatexMath.getContext().getResources();
				float w = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, svg.getDocumentWidth(),
						r.getDisplayMetrics());
				float h = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, svg.getDocumentHeight(),
						r.getDisplayMetrics());
				svg.setDocumentWidth(w);
				svg.setDocumentHeight(h);

				image = Bitmap.createBitmap(
						(int) Math.ceil(svg.getDocumentWidth()),
						(int) Math.ceil(svg.getDocumentHeight()),
						Bitmap.Config.ARGB_8888);
				Canvas bmcanvas = new Canvas(image);

				// Clear background to white
				//bmcanvas.drawRGB(255, 255, 255);

				// Render our document onto our canvas
				svg.renderToCanvas(bmcanvas);
				bmcanvas.save();
			}
		} catch (SVGParseException e) {
			ok = false;
		} 
		if(!ok){
			image = Bitmap.createBitmap(
					(int) 100,
					(int) 100,
					Bitmap.Config.ARGB_8888);
			Canvas c = new  Canvas(image);
			Paint p = new Paint();
			p.setColor(0xFF000000);
			Path path2 = new Path();
			path2.moveTo(0, 0);
			path2.lineTo(100, 0);
			path2.lineTo(100, 100);
			path2.lineTo(-100, 100);
			path2.lineTo(0, -100);
			c.drawPath(path2, p);
			p.setColor(0xFFFFFFFF);
			c.drawText(path, 0, path.length(), 5, 50, p);
		}
		draw();
		buildAtom(option);
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
	

	protected void buildAtom(String option) {
		base = this;
		Map<String, String> options = ParseOption.parseMap(option);
		if (options.containsKey("width") || options.containsKey("height")) {
			base = new ResizeAtom(index, token, base, options.get("width"),
					options.get("height"),
					options.containsKey("keepaspectratio"));
		}
		if (options.containsKey("scale")) {
			double scl = Double.parseDouble(options.get("scale"));
			base = new ScaleAtom(index, token, base, scl, scl);
		}
		if (options.containsKey("angle") || options.containsKey("origin")) {
			base = new RotateAtom(index, token, base, options.get("angle"),
					options.get("origin"));
		}
		if (options.containsKey("interpolation")) {
			String meth = options.get("interpolation");
			if (meth.equalsIgnoreCase("bilinear")) {
				interp = GraphicsBox.BILINEAR;
			} else if (meth.equalsIgnoreCase("bicubic")) {
				interp = GraphicsBox.BICUBIC;
			} else if (meth.equalsIgnoreCase("nearest_neighbor")) {
				interp = GraphicsBox.NEAREST_NEIGHBOR;
			}
		}
	}

	public void draw() {
		/*
		 * if (image != null) { w = image.getWidth(); h = image.getHeight();
		 * bimage =Bitmap.createBitmap(w, h, Config.ARGB_8888); Canvas g2d = new
		 * Canvas(bimage); g2d.drawBitmap(image, 0, 0, null); }
		 */
		w = image.getWidth();
		h = image.getHeight();
		bimage = image;
	}

	public Box createBox(TeXEnvironment env) {
		if (image != null) {
			if (first) {
				first = false;
				return base.createBox(env);
			} else {
				env.isColored = true;
				float width = w
						* SpaceAtom.getFactor(TeXConstants.UNIT_PIXEL, env);
				float height = h
						* SpaceAtom.getFactor(TeXConstants.UNIT_PIXEL, env);
				return new GraphicsBox(index, path, bimage, width, height,
						env.getSize(), interp);
			}
		}

		return new TeXFormula("\\text{ No such image file ! }").root
				.createBox(env);
	}
}