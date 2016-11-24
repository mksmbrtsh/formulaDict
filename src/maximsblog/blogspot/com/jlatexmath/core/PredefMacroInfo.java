/* PredefMacroInfo.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2011 DENIZET Calixte
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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import android.content.res.AssetManager;

/**
 * Class to load the predefined commands. Mainly wrote to avoid the use of the
 * Java reflection.
 */
class PredefMacroInfo extends MacroInfo {

	private int id;

	public PredefMacroInfo(int id, int nbArgs, int posOpts) {
		super(nbArgs, posOpts);
		this.id = id;
	}

	public PredefMacroInfo(int id, int nbArgs) {
		super(nbArgs);
		this.id = id;
	}

	public Object invoke(final TeXParser tp, final String[] args)
			throws ParseException {
		return invokeID(id, tp, args);
	}

	private static final Object invokeID(final int id, final TeXParser tp,
			final String[] args) throws ParseException {
		String token = tp.getCurrentTerm();
		int index = tp.getPos() - token.length();
		Atom a=null;
		try {

			switch (id) {
			case 0:
				a = PredefMacros.newcommand_macro(tp, args);
				break;
			case 1:
				a = PredefMacros.renewcommand_macro(tp, args);
				break;
			case 2:
				a = PredefMacros.rule_macro(tp, args);
				break;
			case 3:
				break;
			case 4:
				a = PredefMacros.hvspace_macro(tp, args);
				break;
			case 5:
			case 6:
			case 7:
				a = PredefMacros.clrlap_macro(tp, args);
				break;
			case 8:
			case 9:
			case 10:
				a = PredefMacros.mathclrlap_macro(tp, args);
				break;
			case 11:
				a = PredefMacros.includegraphics_macro(tp, args);
				break;
			case 12:
				a = PredefMacros.cfrac_macro(tp, args);
				break;
			case 13:
				a = PredefMacros.frac_macro(tp, args);
				break;
			case 14:
				a = PredefMacros.sfrac_macro(tp, args);
				break;
			case 15:
				a = PredefMacros.genfrac_macro(tp, args);
				break;
			case 16:
				a = PredefMacros.over_macro(tp, args);
				break;
			case 17:
				a = PredefMacros.overwithdelims_macro(tp, args);
				break;
			case 18:
				a = PredefMacros.atop_macro(tp, args);
				break;
			case 19:
				a = PredefMacros.atopwithdelims_macro(tp, args);
				break;
			case 20:
				a = PredefMacros.choose_macro(tp, args);
				break;
			case 21:
				a = PredefMacros.underscore_macro(tp, args);
				break;
			case 22:
				a = PredefMacros.mbox_macro(tp, args);
				break;
			case 23:
				a = PredefMacros.text_macro(tp, args);
				break;
			case 24:
				a = PredefMacros.intertext_macro(tp, args);
				break;
			case 25:
				a = PredefMacros.binom_macro(tp, args);
				break;
			case 26:
				a = PredefMacros.mathbf_macro(tp, args);
				break;
			case 27:
				a = PredefMacros.bf_macro(tp, args);
				break;
			case 28:
				a = PredefMacros.textstyle_macros(tp, args);
				break;
			case 29:
				a = PredefMacros.textstyle_macros(tp, args);
				break;
			case 30:
				a = PredefMacros.textstyle_macros(tp, args);
				break;
			case 31:
				a = PredefMacros.mathit_macro(tp, args);
				break;
			case 32:
				a = PredefMacros.it_macro(tp, args);
				break;
			case 33:
				a = PredefMacros.mathrm_macro(tp, args);
				break;
			case 34:
				a = PredefMacros.rm_macro(tp, args);
				break;
			case 35:
				a = PredefMacros.textstyle_macros(tp, args);
				break;
			case 36:
				a = PredefMacros.mathsf_macro(tp, args);
				break;
			case 37:
				a = PredefMacros.sf_macro(tp, args);
				break;
			case 38:
				a = PredefMacros.mathtt_macro(tp, args);
				break;
			case 39:
				a = PredefMacros.tt_macro(tp, args);
				break;
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
				a = PredefMacros.textstyle_macros(tp, args);
				break;
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
				a = PredefMacros.accentbis_macros(tp, args);
				break;
			case 58:
				a = PredefMacros.T_macro(tp, args);
				break;
			case 59:
				a = PredefMacros.accentbis_macros(tp, args);
				break;
			case 60:
				a = PredefMacros.accent_macro(tp, args);
				break;
			case 61:
				a = PredefMacros.grkaccent_macro(tp, args);
				break;
			case 62:
			case 63:
			case 64:
			case 65:
			case 66:
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
			case 75:
				a = PredefMacros.accent_macros(tp, args);
				break;
			case 76:
				a = PredefMacros.nbsp_macro(tp, args);
				break;
			case 77:
				a = PredefMacros.smallmatrixATATenv_macro(tp, args);
				break;
			case 78:
				a = PredefMacros.matrixATATenv_macro(tp, args);
				break;
			case 79:
				a = PredefMacros.overrightarrow_macro(tp, args);
				break;
			case 80:
				a = PredefMacros.overleftarrow_macro(tp, args);
				break;
			case 81:
				a = PredefMacros.overleftrightarrow_macro(tp, args);
				break;
			case 82:
				a = PredefMacros.underrightarrow_macro(tp, args);
				break;
			case 83:
				a = PredefMacros.underleftarrow_macro(tp, args);
				break;
			case 84:
				a = PredefMacros.underleftrightarrow_macro(tp, args);
				break;
			case 85:
				a = PredefMacros.xleftarrow_macro(tp, args);
				break;
			case 86:
				a = PredefMacros.xrightarrow_macro(tp, args);
				break;
			case 87:
				a = PredefMacros.underbrace_macro(tp, args);
				break;
			case 88:
				a = PredefMacros.overbrace_macro(tp, args);
				break;
			case 89:
				a = PredefMacros.underbrack_macro(tp, args);
				break;
			case 90:
				a = PredefMacros.overbrack_macro(tp, args);
				break;
			case 91:
				a = PredefMacros.underparen_macro(tp, args);
				break;
			case 92:
				a = PredefMacros.overparen_macro(tp, args);
				break;
			case 93:
			case 94:
				a = PredefMacros.sqrt_macro(tp, args);
				break;
			case 95:
				a = PredefMacros.overline_macro(tp, args);
				break;
			case 96:
				a = PredefMacros.underline_macro(tp, args);
				break;
			case 97:
				a = PredefMacros.mathop_macro(tp, args);
				break;
			case 98:
				a = PredefMacros.mathpunct_macro(tp, args);
				break;
			case 99:
				a = PredefMacros.mathord_macro(tp, args);
				break;
			case 100:
				a = PredefMacros.mathrel_macro(tp, args);
				break;
			case 101:
				a = PredefMacros.mathinner_macro(tp, args);
				break;
			case 102:
				a = PredefMacros.mathbin_macro(tp, args);
				break;
			case 103:
				a = PredefMacros.mathopen_macro(tp, args);
				break;
			case 104:
				a = PredefMacros.mathclose_macro(tp, args);
				break;
			case 105:
				a = PredefMacros.joinrel_macro(tp, args);
				break;
			case 106:
				a = PredefMacros.smash_macro(tp, args);
				break;
			case 107:
				a = PredefMacros.vdots_macro(tp, args);
				break;
			case 108:
				a = PredefMacros.ddots_macro(tp, args);
				break;
			case 109:
				a = PredefMacros.iddots_macro(tp, args);
				break;
			case 110:
				a = PredefMacros.nolimits_macro(tp, args);
				break;
			case 111:
				a = PredefMacros.limits_macro(tp, args);
				break;
			case 112:
				a = PredefMacros.normal_macro(tp, args);
				break;
			case 113:
				a = PredefMacros.leftparenthesis_macro(tp, args);
				break;
			case 114:
				a = PredefMacros.leftbracket_macro(tp, args);
				break;
			case 115:
				a = PredefMacros.left_macro(tp, args);
				break;
			case 116:
				a = PredefMacros.middle_macro(tp, args);
				break;
			case 117:
				a = PredefMacros.cr_macro(tp, args);
				break;
			case 118:
				a = PredefMacros.multicolumn_macro(tp, args);
				break;
			case 119:
				a = PredefMacros.hdotsfor_macro(tp, args);
				break;
			case 120:
				a = PredefMacros.arrayATATenv_macro(tp, args);
				break;
			case 121:
				a = PredefMacros.alignATATenv_macro(tp, args);
				break;
			case 122:
				a = PredefMacros.alignedATATenv_macro(tp, args);
				break;
			case 123:
				a = PredefMacros.flalignATATenv_macro(tp, args);
				break;
			case 124:
				a = PredefMacros.alignatATATenv_macro(tp, args);
				break;
			case 125:
				a = PredefMacros.alignedatATATenv_macro(tp, args);
				break;
			case 126:
				a = PredefMacros.multlineATATenv_macro(tp, args);
				break;
			case 127:
				a = PredefMacros.gatherATATenv_macro(tp, args);
				break;
			case 128:
				a = PredefMacros.gatheredATATenv_macro(tp, args);
				break;
			case 129:
				a = PredefMacros.shoveright_macro(tp, args);
				break;
			case 130:
				a = PredefMacros.shoveleft_macro(tp, args);
				break;
			case 131:
				a = PredefMacros.backslashcr_macro(tp, args);
				break;
			case 132:
				a = PredefMacros.newenvironment_macro(tp, args);
				break;
			case 133:
				a = PredefMacros.renewenvironment_macro(tp, args);
				break;
			case 134:
				a = PredefMacros.makeatletter_macro(tp, args);
				break;
			case 135:
				a = PredefMacros.makeatother_macro(tp, args);
				break;
			case 136:
			case 137:
				a = PredefMacros.fbox_macro(tp, args);
				break;
			case 138:
				a = PredefMacros.stackrel_macro(tp, args);
				break;
			case 139:
				a = PredefMacros.stackbin_macro(tp, args);
				break;
			case 140:
				a = PredefMacros.accentset_macro(tp, args);
				break;
			case 141:
				a = PredefMacros.underaccent_macro(tp, args);
				break;
			case 142:
				a = PredefMacros.undertilde_macro(tp, args);
				break;
			case 143:
				a = PredefMacros.overset_macro(tp, args);
				break;
			case 144:
				a = PredefMacros.Braket_macro(tp, args);
				break;
			case 145:
				a = PredefMacros.Set_macro(tp, args);
				break;
			case 146:
				a = PredefMacros.underset_macro(tp, args);
				break;
			case 147:
				a = PredefMacros.boldsymbol_macro(tp, args);
				break;
			case 148:
				a = PredefMacros.LaTeX_macro(tp, args);
				break;
			case 149:
				a = PredefMacros.GeoGebra_macro(tp, args);
				break;
			case 150:
				a = PredefMacros.big_macro(tp, args);
				break;
			case 151:
				a = PredefMacros.Big_macro(tp, args);
				break;
			case 152:
				a = PredefMacros.bigg_macro(tp, args);
				break;
			case 153:
				a = PredefMacros.Bigg_macro(tp, args);
				break;
			case 154:
				a = PredefMacros.bigl_macro(tp, args);
				break;
			case 155:
				a = PredefMacros.Bigl_macro(tp, args);
				break;
			case 156:
				a = PredefMacros.biggl_macro(tp, args);
				break;
			case 157:
				a = PredefMacros.Biggl_macro(tp, args);
				break;
			case 158:
				a = PredefMacros.bigr_macro(tp, args);
				break;
			case 159:
				a = PredefMacros.Bigr_macro(tp, args);
				break;
			case 160:
				a = PredefMacros.biggr_macro(tp, args);
				break;
			case 161:
				a = PredefMacros.Biggr_macro(tp, args);
				break;
			case 162:
				a = PredefMacros.displaystyle_macro(tp, args);
				break;
			case 163:
				a = PredefMacros.textstyle_macro(tp, args);
				break;
			case 164:
				a = PredefMacros.scriptstyle_macro(tp, args);
				break;
			case 165:
				a = PredefMacros.scriptscriptstyle_macro(tp, args);
				break;
			case 166:
				a = PredefMacros.sideset_macro(tp, args);
				break;
			case 167:
				a = PredefMacros.prescript_macro(tp, args);
				break;
			case 168:
				a = PredefMacros.rotatebox_macro(tp, args);
				break;
			case 169:
				a = PredefMacros.reflectbox_macro(tp, args);
				break;
			case 170:
				a = PredefMacros.scalebox_macro(tp, args);
				break;
			case 171:
				a = PredefMacros.resizebox_macro(tp, args);
				break;
			case 172:
				a = PredefMacros.raisebox_macro(tp, args);
				break;
			case 173:
				a = PredefMacros.shadowbox_macro(tp, args);
				break;
			case 174:
				a = PredefMacros.ovalbox_macro(tp, args);
				break;
			case 175:
				a = PredefMacros.doublebox_macro(tp, args);
				break;
			case 176:
				a = PredefMacros.phantom_macro(tp, args);
				break;
			case 177:
				a = PredefMacros.hphantom_macro(tp, args);
				break;
			case 178:
				a = PredefMacros.vphantom_macro(tp, args);
				break;
			case 179:
				a = PredefMacros.spATbreve_macro(tp, args);
				break;
			case 180:
				a = PredefMacros.spAThat_macro(tp, args);
				break;
			case 181:
				a = PredefMacros.definecolor_macro(tp, args);
				break;
			case 182:
				a = PredefMacros.textcolor_macro(tp, args);
				break;
			case 183:
				a = PredefMacros.fgcolor_macro(tp, args);
				break;
			case 184:
				a = PredefMacros.bgcolor_macro(tp, args);
				break;
			case 185:
				a = PredefMacros.colorbox_macro(tp, args);
				break;
			case 186:
				a = PredefMacros.fcolorbox_macro(tp, args);
				break;
			case 187:
				a = PredefMacros.cedilla_macro(tp, args);
				break;
			case 188:
				a = PredefMacros.IJ_macro(tp, args);
				break;
			case 189:
				a = PredefMacros.IJ_macro(tp, args);
				break;
			case 190:
				a = PredefMacros.TStroke_macro(tp, args);
				break;
			case 191:
				a = PredefMacros.TStroke_macro(tp, args);
				break;
			case 192:
				a = PredefMacros.LCaron_macro(tp, args);
				break;
			case 193:
				a = PredefMacros.tcaron_macro(tp, args);
				break;
			case 194:
				a = PredefMacros.LCaron_macro(tp, args);
				break;
			case 195:
				a = PredefMacros.ogonek_macro(tp, args);
				break;
			case 196:
				a = PredefMacros.cong_macro(tp, args);
				break;
			case 197:
				a = PredefMacros.doteq_macro(tp, args);
				break;
			case 198:
				a = PredefMacros.jlmDynamic_macro(tp, args);
				break;
			case 199:
				a = PredefMacros.jlmExternalFont_macro(tp, args);
				break;
			case 200:
				a = PredefMacros.jlmText_macro(tp, args);
				break;
			case 201:
				a = PredefMacros.jlmTextit_macro(tp, args);
				break;
			case 202:
				a = PredefMacros.jlmTextbf_macro(tp, args);
				break;
			case 203:
				a = PredefMacros.jlmTextitbf_macro(tp, args);
				break;
			case 204:
				a = PredefMacros.DeclareMathSizes_macro(tp, args);
				break;
			case 205:
				a = PredefMacros.magnification_macro(tp, args);
				break;
			case 206:
				a = PredefMacros.hline_macro(tp, args);
				break;
			case 207:
			case 208:
			case 209:
			case 210:
			case 211:
			case 212:
			case 213:
			case 214:
			case 215:
			case 216:
				a = PredefMacros.size_macros(tp, args);
				break;
			case 217:
				a = PredefMacros.jlatexmathcumsup_macro(tp, args);
				break;
			case 218:
				a = PredefMacros.jlatexmathcumsub_macro(tp, args);
				break;
			case 219:
				a = PredefMacros.hstrok_macro(tp, args);
				break;
			case 220:
				a = PredefMacros.Hstrok_macro(tp, args);
				break;
			case 221:
				a = PredefMacros.dstrok_macro(tp, args);
				break;
			case 222:
				a = PredefMacros.Dstrok_macro(tp, args);
				break;
			case 223:
				a = PredefMacros.dotminus_macro(tp, args);
				break;
			case 224:
				a = PredefMacros.ratio_macro(tp, args);
				break;
			case 225:
				a = PredefMacros.smallfrowneq_macro(tp, args);
				break;
			case 226:
				a = PredefMacros.geoprop_macro(tp, args);
				break;
			case 227:
				a = PredefMacros.minuscolon_macro(tp, args);
				break;
			case 228:
				a = PredefMacros.minuscoloncolon_macro(tp, args);
				break;
			case 229:
				a = PredefMacros.simcolon_macro(tp, args);
				break;
			case 230:
				a = PredefMacros.simcoloncolon_macro(tp, args);
				break;
			case 231:
				a = PredefMacros.approxcolon_macro(tp, args);
				break;
			case 232:
				a = PredefMacros.approxcoloncolon_macro(tp, args);
				break;
			case 233:
				a = PredefMacros.coloncolon_macro(tp, args);
				break;
			case 234:
				a = PredefMacros.equalscolon_macro(tp, args);
				break;
			case 235:
				a = PredefMacros.equalscoloncolon_macro(tp, args);
				break;
			case 236:
				a = PredefMacros.colonminus_macro(tp, args);
				break;
			case 237:
				a = PredefMacros.coloncolonminus_macro(tp, args);
				break;
			case 238:
				a = PredefMacros.colonequals_macro(tp, args);
				break;
			case 239:
				a = PredefMacros.coloncolonequals_macro(tp, args);
				break;
			case 240:
				a = PredefMacros.colonsim_macro(tp, args);
				break;
			case 241:
				a = PredefMacros.coloncolonsim_macro(tp, args);
				break;
			case 242:
				a = PredefMacros.colonapprox_macro(tp, args);
				break;
			case 243:
				a = PredefMacros.coloncolonapprox_macro(tp, args);
				break;
			case 244:
				a = PredefMacros.kern_macro(tp, args);
				break;
			case 245:
				a = PredefMacros.char_macro(tp, args);
				break;
			case 246:
			case 247:
				a = PredefMacros.romannumeral_macro(tp, args);
				break;
			case 248:
				a = PredefMacros.textcircled_macro(tp, args);
				break;
			case 249:
				a = PredefMacros.textsc_macro(tp, args);
				break;
			case 250:
				a = PredefMacros.sc_macro(tp, args);
				break;
			case 251:
			case 252:
			case 253:
			case 254:
			case 255:
			case 256:
			case 257:
			case 258:
			case 259:
			case 260:
				a = PredefMacros.muskip_macros(tp, args);
				break;
			case 261:
				a = PredefMacros.quad_macro(tp, args);
				break;
			case 262:
				a = PredefMacros.surd_macro(tp, args);
				break;
			case 263:
				a = PredefMacros.iint_macro(tp, args);
				break;
			case 264:
				a = PredefMacros.iiint_macro(tp, args);
				break;
			case 265:
				a = PredefMacros.iiiint_macro(tp, args);
				break;
			case 266:
				a = PredefMacros.idotsint_macro(tp, args);
				break;
			case 267:
				a = PredefMacros.int_macro(tp, args);
				break;
			case 268:
				a = PredefMacros.oint_macro(tp, args);
				break;
			case 269:
				a = PredefMacros.lmoustache_macro(tp, args);
				break;
			case 270:
				a = PredefMacros.rmoustache_macro(tp, args);
				break;
			case 271:
				a = PredefMacros.insertBreakMark_macro(tp, args);
				break;
			case 272:
				a = PredefMacros.jlmXML_macro(tp, args);
				break;
			case 273:
				a = PredefMacros.above_macro(tp, args);
				break;
			case 274:
				a = PredefMacros.abovewithdelims_macro(tp, args);
				break;
			case 275:
				a = PredefMacros.st_macro(tp, args);
				break;
			case 276:
				a = PredefMacros.fcscore_macro(tp, args);
				break;
			default:
				a = null;
				break;
			}
		} catch (Exception e) {
			throw new ParseException("Problem with command " + args[0]
					+ " at position " + tp.getLine() + ":" + tp.getCol() + "\n"
					+ e.getMessage());
		}
		if(a != null) {
			a.index = index;
			a.token = token;
		}
		return a;
	}
}
