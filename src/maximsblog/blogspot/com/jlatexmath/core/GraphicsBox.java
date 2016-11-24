/* GraphicsBox.java
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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

/**
 * A box representing a box containing a graphics.
 */
public class GraphicsBox extends Box implements IHintTest {

	public final static int BILINEAR = 0;
	public final static int NEAREST_NEIGHBOR = 1;
	public final static int BICUBIC = 2;

	private Bitmap image;
	private float scl;
	private Object interp;
	private Paint paint;
	private float xbase = 0;
	private float ybase = 0;
	private float xscl = 1;
	private float yscl = 1;
	protected RectF r;
	
	public GraphicsBox(int index, String token, Bitmap image, float width, float height, float size,
			int interpolation) {
		super(index, token);
		this.image = image;
		this.width = width;
		this.height = height;
		this.scl = 1 / size;
		depth = 0;
		shift = 0;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setFilterBitmap(true);
		/*
		 * switch (interpolation) { case BILINEAR : interp =
		 * RenderingHints.VALUE_INTERPOLATION_BILINEAR; break; case
		 * NEAREST_NEIGHBOR : interp =
		 * RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR; break; case
		 * BICUBIC : interp = RenderingHints.VALUE_INTERPOLATION_BICUBIC; break;
		 * default : interp = null; }
		 */
	}
	


	public void draw(Canvas g2, float x, float y) {
		if(AjLatexMath.xbase != 0 || AjLatexMath.ybase != 0){
			this.xbase = AjLatexMath.xbase;
			this.ybase = AjLatexMath.ybase;
		}
		if(AjLatexMath.xscl != 1 || AjLatexMath.yscl != 1){
			this.xscl = AjLatexMath.xscl;
			this.yscl = AjLatexMath.yscl;
		}
		r = new RectF(x *xscl + xbase, (y - height) * yscl + ybase, (x + width) *xscl +xbase, (y + depth) * yscl + ybase);

		g2.save();
		// Object oldKey = null;
		// if (interp != null) {
		// oldKey = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
		// g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interp);
		// }
		g2.translate(x, y - height);
		g2.scale(scl, scl);
		g2.drawBitmap(image, 0, 0, paint);
		// image.recycle();
		// g2.setTransform(oldAt);
		// if (oldKey != null) {
		// g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldKey);
		// }
		g2.restore();
	}

	public int getLastFontId() {
		return 0;
	}

	@Override
	public boolean hitTest(float x, float y, float s) {
		float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, s / TeXFormula.PIXELS_PER_POINT, AjLatexMath.getContext().getResources().getDisplayMetrics());
		float xx = x / f;
		float yy = y / f;
		if(r != null) {
			return r.contains(xx, yy);
		} else return false;
	}
}