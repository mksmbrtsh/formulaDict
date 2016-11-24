/* CharBox.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 * 
 * Copyright (C) 2004-2007 Universiteit Gent
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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.ArrayList;

/**
 * A box representing a single character.
 */
public class CharBox extends Box {

	private final CharFont cf;
	private final float size;

	private final char[] arr = new char[1];

	protected RectF r;
	/**
	 * Create a new CharBox that will represent the character defined by the
	 * given Char-object.
	 * 
	 * @param c
	 *            a Char-object containing the character's font information.
	 */
	public CharBox(int index, String token, Char c) {
		super(index, token);
		cf = c.getCharFont();
		size = c.getMetrics().getSize();
		width = c.getWidth();
		height = c.getHeight();
		depth = c.getDepth();
	}


	private float xbase = 0;
	private float ybase = 0;
	private float xscl = 1;
	private float yscl = 1;

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
		
		drawDebug(g2, x, y);
		g2.save();
		g2.translate(x, y);
		Typeface font = FontInfo.getFont(cf.fontId);
		if (size != 1) {
			g2.scale(size, size);
		}
		Paint st = AjLatexMath.getPaint();
		st.setTextSize(TeXFormula.PIXELS_PER_POINT);
		st.setTypeface(font);
		st.setStyle(Style.FILL);
		st.setAntiAlias(true);
		st.setStrokeWidth(0);
		arr[0] = cf.c;
		g2.drawText(arr, 0, 1, 0, 0, st);
		g2.restore();
	}

	public int getLastFontId() {
		return cf.fontId;
	}

	public String toString() {
		return super.toString() + "=" + cf.c;
	}
	
	public char getChar(){
		return arr[0];
	}

	public Typeface getFont(){
		return FontInfo.getFont(cf.fontId);
	}
}
