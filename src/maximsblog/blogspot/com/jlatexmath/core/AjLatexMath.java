package maximsblog.blogspot.com.jlatexmath.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import java.util.ArrayList;
import java.util.Arrays;

public class AjLatexMath {

	private static Context mContext;
	private static Paint st;
	public static float xbase = 0;
	public static float ybase = 0;
	public static float xscl = 1;
	public static float yscl = 1;

	public static void init(Context context) {
		mContext = context;
		st = new Paint();
		st.setStyle(Style.FILL_AND_STROKE);
		st.setColor(Color.BLACK);
		st.setStrokeWidth(1);
		new TeXFormula();
	}

	public static AssetManager getAssetManager() {
		AssetManager mng = mContext.getAssets();
		return mng;
	}

	public static Context getContext() {
		return mContext;
	}

	public static Paint getPaint() {
		return st;
	}
	
	public static float getLeading(float textSize) {
		st.setTextSize(textSize);
		return st.getFontSpacing();
	}

	public static ArrayList<String>getListAllCommands(){
		ArrayList<String> list = new ArrayList<String>(predefinedCommands.length + texSymbols.length + texFormuls.length);
		list.addAll(Arrays.asList(predefinedCommands));
		list.addAll(Arrays.asList(texSymbols));
		list.addAll(Arrays.asList(texFormuls));
		return list;
	}

	private static String[] predefinedCommands = new String[] {
"\\newcommand",
		"\\renewcommand",
		"\\rule",
		"\\hspace",
		"\\vspace",
		"\\llap",
		"\\rlap",
		"\\clap",
		"\\mathllap",
		"\\mathrlap",
		"\\mathclap",
		"\\includegraphics",
		"\\cfrac",
		"\\frac",
		"\\sfrac",
		"\\genfrac",
		"\\over",
		"\\overwithdelims",
		"\\atop",
		"\\atopwithdelims",
		"\\choose",
		"\\underscore",
		"\\mbox",
		"\\text",
		"\\intertext",
		"\\binom",
		"\\mathbf",
		"\\bf",
		"\\mathbb",
		"\\mathcal",
		"\\cal",
		"\\mathit",
		"\\it",
		"\\mathrm",
		"\\rm",
		"\\mathscr",
		"\\mathsf",
		"\\sf",
		"\\mathtt",
		"\\tt",
		"\\mathfrak",
		"\\mathds",
		"\\frak",
		"\\Bbb",
		"\\oldstylenums",
		"\\bold",
		"\\^",
		"\\'",
		"\\\"",
		"\\`",
		"\\=",
		"\\.",
		"\\~",
		"\\u",
		"\\v",
		"\\H",
		"\\r",
		"\\U",
		"\\T",
		"\\t",
		"\\accent",
		"\\grkaccent",
		"\\hat",
		"\\widehat",
		"\\tilde",
		"\\acute",
		"\\grave",
		"\\ddot",
		"\\cyrddot",
		"\\mathring",
		"\\bar",
		"\\breve",
		"\\check",
		"\\vec",
		"\\dot",
		"\\widetilde",
		"\\nbsp",
		"\\smallmatrix@@env",
		"\\matrix@@env",
		"\\overrightarrow",
		"\\overleftarrow",
		"\\overleftrightarrow",
		"\\underrightarrow",
		"\\underleftarrow",
		"\\underleftrightarrow",
		"\\xleftarrow",
		"\\xrightarrow",
		"\\underbrace",
		"\\overbrace",
		"\\underbrack",
		"\\overbrack",
		"\\underparen",
		"\\overparen",
		"\\sqrt",
		"\\sqrtsign",
		"\\overline",
		"\\underline",
		"\\mathop",
		"\\mathpunct",
		"\\mathord",
		"\\mathrel",
		"\\mathinner",
		"\\mathbin",
		"\\mathopen",
		"\\mathclose",
		"\\joinrel",
		"\\smash",
		"\\vdots",
		"\\ddots",
		"\\iddots",
		"\\nolimits",
		"\\limits",
		"\\normal",
		"\\(",
		"\\[",
		"\\left",
		"\\middle",
		"\\cr",
		"\\multicolumn",
		"\\hdotsfor",
		"\\array@@env",
		"\\align@@env",
		"\\aligned@@env",
		"\\flalign@@env",
		"\\alignat@@env",
		"\\alignedat@@env",
		"\\multline@@env",
		"\\gather@@env",
		"\\gathered@@env",
		"\\shoveright",
		"\\shoveleft",
		"\\\\\\",
		"\\newenvironment",
		"\\renewenvironment",
		"\\makeatletter",
		"\\makeatother",
		"\\fbox",
		"\\boxed",
		"\\stackrel",
		"\\stackbin",
		"\\accentset",
		"\\underaccent",
		"\\undertilde",
		"\\overset",
		"\\Braket",
		"\\Set",
		"\\underset",
		"\\boldsymbol",
		"\\LaTeX",
		"\\GeoGebra",
		"\\big",
		"\\Big",
		"\\bigg",
		"\\Bigg",
		"\\bigl",
		"\\Bigl",
		"\\biggl",
		"\\Biggl",
		"\\bigr",
		"\\Bigr",
		"\\biggr",
		"\\Biggr",
		"\\displaystyle",
		"\\textstyle",
		"\\scriptstyle",
		"\\scriptscriptstyle",
		"\\sideset",
		"\\prescript",
		"\\rotatebox",
		"\\reflectbox",
		"\\scalebox",
		"\\resizebox",
		"\\raisebox",
		"\\shadowbox",
		"\\ovalbox",
		"\\doublebox",
		"\\phantom",
		"\\hphantom",
		"\\vphantom",
		"\\sp@breve",
		"\\sp@hat",
		"\\definecolor",
		"\\textcolor",
		"\\fgcolor",
		"\\bgcolor",
		"\\colorbox",
		"\\fcolorbox",
		"\\c",
		"\\IJ",
		"\\ij",
		"\\TStroke",
		"\\tStroke",
		"\\Lcaron",
		"\\tcaron",
		"\\lcaron",
		"\\k",
		"\\cong",
		"\\doteq",
		"\\jlmDynamic",
		"\\jlmExternalFont",
		"\\jlmText",
		"\\jlmTextit",
		"\\jlmTextbf",
		"\\jlmTextitbf",
		"\\DeclareMathSizes",
		"\\magnification",
		"\\hline",
		"\\tiny",
		"\\scriptsize",
		"\\footnotesize",
		"\\small",
		"\\normalsize",
		"\\large",
		"\\Large",
		"\\LARGE",
		"\\huge",
		"\\Huge",
		"\\jlatexmathcumsup",
		"\\jlatexmathcumsub",
		"\\hstrok",
		"\\Hstrok",
		"\\dstrok",
		"\\Dstrok",
		"\\dotminus",
		"\\ratio",
		"\\smallfrowneq",
		"\\geoprop",
		"\\minuscolon",
		"\\minuscoloncolon",
		"\\simcolon",
		"\\simcoloncolon",
		"\\approxcolon",
		"\\approxcoloncolon",
		"\\coloncolon",
		"\\equalscolon",
		"\\equalscoloncolon",
		"\\colonminus",
		"\\coloncolonminus",
		"\\colonequals",
		"\\coloncolonequals",
		"\\colonsim",
		"\\coloncolonsim",
		"\\colonapprox",
		"\\coloncolonapprox",
		"\\kern",
		"\\char",
		"\\roman",
		"\\Roman",
		"\\textcircled",
		"\\textsc",
		"\\sc",
		"\\,",
		"\\:",
		"\\;",
		"\\thinspace",
		"\\medspace",
		"\\thickspace",
		"\\!",
		"\\negthinspace",
		"\\negmedspace",
		"\\negthickspace",
		"\\quad",
		"\\surd",
		"\\iint",
		"\\iiint",
		"\\iiiint",
		"\\idotsint",
		"\\int",
		"\\oint",
		"\\lmoustache",
		"\\rmoustache",
		"\\-",
		"\\jlmXML",
		"\\above",
		"\\abovewithdelims",
		"\\st",
		"\\fcscore"
	};

	private static String[] texSymbols = new String[] {
	"\\ae",
	"\\AE",
	"\\OE",
	"\\oe",
	"\\ss",
	"\\o",
	"\\O",
	"\\i",
	"\\j",
	"\\jlmEuler",
	"\\textapos",
	"\\jlatexmathlapos",
	"\\jlatexmathcedilla",
	"\\ogonek",
	"\\polishlcross",
	"\\lq",
	"\\rq",
	"\\textdbend",
	"\\dbend",
	"\\shortleftarrow",
	"\\shortrightarrow",
	"\\shortuparrow",
	"\\shortdownarrow",
	"\\Yup",
	"\\Ydown",
	"\\Yleft",
	"\\Yright",
	"\\varcurlyvee",
	"\\varcurlywedge",
	"\\minuso",
	"\\baro",
	"\\sslash",
	"\\bbslash",
	"\\moo",
	"\\varotimes",
	"\\varoast",
	"\\varobar",
	"\\varodot",
	"\\varoslash",
	"\\varobslash",
	"\\varocircle",
	"\\varoplus",
	"\\varominus",
	"\\boxast",
	"\\boxbar",
	"\\boxdot",
	"\\boxslash",
	"\\boxbslash",
	"\\boxcircle",
	"\\boxbox",
	"\\boxempty",
	"\\lightning",
	"\\merge",
	"\\vartimes",
	"\\fatsemi",
	"\\sswarrow",
	"\\ssearrow",
	"\\curlywedgeuparrow",
	"\\curlywedgedownarrow",
	"\\fatslash",
	"\\fatbslash",
	"\\lbag",
	"\\rbag",
	"\\varbigcirc",
	"\\leftrightarroweq",
	"\\curlyveedownarrow",
	"\\curlyveeuparrow",
	"\\nnwarrow",
	"\\nnearrow",
	"\\leftslice",
	"\\rightslice",
	"\\varolessthan",
	"\\varogreaterthan",
	"\\varovee",
	"\\varowedge",
	"\\talloblong",
	"\\interleave",
	"\\obar",
	"\\obslash",
	"\\olessthan",
	"\\ogreaterthan",
	"\\ovee",
	"\\owedge",
	"\\oblong",
	"\\inplus",
	"\\niplus",
	"\\nplus",
	"\\subsetplus",
	"\\supsetplus",
	"\\subsetpluseq",
	"\\supsetpluseq",
	"\\Lbag",
	"\\Rbag",
	"\\llbracket",
	"\\rrbracket",
	"\\llparenthesis",
	"\\rrparenthesis",
	"\\binampersand",
	"\\bindnasrepma",
	"\\trianglelefteqslant",
	"\\trianglerighteqslant",
	"\\ntrianglelefteqslant",
	"\\ntrianglerighteqslant",
	"\\llfloor",
	"\\rrfloor",
	"\\llceil",
	"\\rrceil",
	"\\arrownot",
	"\\Arrownot",
	"\\Mapstochar",
	"\\mapsfromchar",
	"\\Mapsfromchar",
	"\\leftrightarrowtriangle",
	"\\leftarrowtriangle",
	"\\rightarrowtriangle",
	"\\bigtriangledown",
	"\\bigtriangleup",
	"\\bigcurlyvee",
	"\\bigcurlywedge",
	"\\bigsqcap",
	"\\bigbox",
	"\\bigparallel",
	"\\biginterleave",
	"\\bignplus",
	"\\ulcorner",
	"\\urcorner",
	"\\llcorner",
	"\\lrcorner",
	"\\maltese",
	"\\textregistered",
	"\\lhook",
	"\\rhook",
	"\\mapstochar",
	"\\angle",
	"\\hbar",
	"\\sqsubset",
	"\\sqsupset",
	"\\mho",
	"\\square",
	"\\lozenge",
	"\\vartriangleright",
	"\\vartriangleleft",
	"\\trianglerighteq",
	"\\trianglelefteq",
	"\\rightsquigarrow",
	"\\lhd",
	"\\unlhd",
	"\\rhd",
	"\\unrhd",
	"\\Box",
	"\\boxdot",
	"\\boxplus",
	"\\boxtimes",
	"\\square",
	"\\blacksquare",
	"\\centerdot",
	"\\lozenge",
	"\\blacklozenge",
	"\\circlearrowright",
	"\\circlearrowleft",
	"\\leftrightharpoons",
	"\\rightleftharpoons",
	"\\boxminus",
	"\\Vdash",
	"\\Vvdash",
	"\\vDash",
	"\\twoheadrightarrow",
	"\\twoheadleftarrow",
	"\\leftleftarrows",
	"\\rightrightarrows",
	"\\upuparrows",
	"\\downdownarrows",
	"\\upharpoonright",
	"\\downharpoonright",
	"\\upharpoonleft",
	"\\downharpoonleft",
	"\\rightarrowtail",
	"\\leftarrowtail",
	"\\leftrightarrows",
	"\\rightleftarrows",
	"\\Lsh",
	"\\Rsh",
	"\\rightsquigarrow",
	"\\leftrightsquigarrow",
	"\\looparrowleft",
	"\\looparrowright",
	"\\circeq",
	"\\succsim",
	"\\gtrsim",
	"\\gtrapprox",
	"\\multimap",
	"\\therefore",
	"\\because",
	"\\doteqdot",
	"\\triangleq",
	"\\precsim",
	"\\lesssim",
	"\\lessapprox",
	"\\eqslantless",
	"\\eqslantgtr",
	"\\curlyeqprec",
	"\\curlyeqsucc",
	"\\preccurlyeq",
	"\\leqq",
	"\\leqslant",
	"\\lessgtr",
	"\\backprime",
	"\\risingdotseq",
	"\\fallingdotseq",
	"\\succcurlyeq",
	"\\geqq",
	"\\geqslant",
	"\\gtrless",
	"\\vartriangleright",
	"\\vartriangleleft",
	"\\trianglerighteq",
	"\\trianglelefteq",
	"\\bigstar",
	"\\between",
	"\\blacktriangledown",
	"\\blacktriangleright",
	"\\blacktriangleleft",
	"\\vartriangle",
	"\\blacktriangle",
	"\\triangledown",
	"\\eqcirc",
	"\\lesseqgtr",
	"\\gtreqless",
	"\\lesseqqgtr",
	"\\gtreqqless",
	"\\Rrightarrow",
	"\\Lleftarrow",
	"\\veebar",
	"\\barwedge",
	"\\doublebarwedge",
	"\\measuredangle",
	"\\sphericalangle",
	"\\varpropto",
	"\\smallsmile",
	"\\smallfrown",
	"\\Subset",
	"\\Supset",
	"\\Cup",
	"\\Cap",
	"\\curlywedge",
	"\\curlyvee",
	"\\leftthreetimes",
	"\\rightthreetimes",
	"\\subseteqq",
	"\\supseteqq",
	"\\bumpeq",
	"\\Bumpeq",
	"\\lll",
	"\\ggg",
	"\\circledS",
	"\\pitchfork",
	"\\dotplus",
	"\\backsim",
	"\\backsimeq",
	"\\complement",
	"\\intercal",
	"\\circledcirc",
	"\\circledast",
	"\\circleddash",
	"\\lvertneqq",
	"\\gvertneqq",
	"\\nleq",
	"\\ngeq",
	"\\nless",
	"\\ngtr",
	"\\nprec",
	"\\nsucc",
	"\\lneqq",
	"\\gneqq",
	"\\nleqslant",
	"\\ngeqslant",
	"\\lneq",
	"\\gneq",
	"\\npreceq",
	"\\nsucceq",
	"\\precnsim",
	"\\succnsim",
	"\\lnsim",
	"\\gnsim",
	"\\nleqq",
	"\\ngeqq",
	"\\precneqq",
	"\\succneqq",
	"\\precnapprox",
	"\\succnapprox",
	"\\lnapprox",
	"\\gnapprox",
	"\\nsim",
	"\\ncong",
	"\\diagup",
	"\\diagdown",
	"\\varsubsetneq",
	"\\varsupsetneq",
	"\\nsubseteqq",
	"\\nsupseteqq",
	"\\subsetneqq",
	"\\supsetneqq",
	"\\varsubsetneqq",
	"\\varsupsetneqq",
	"\\subsetneq",
	"\\supsetneq",
	"\\nsubseteq",
	"\\nsupseteq",
	"\\nparallel",
	"\\nmid",
	"\\nshortmid",
	"\\nshortparallel",
	"\\nvdash",
	"\\nVdash",
	"\\nvDash",
	"\\nVDash",
	"\\ntrianglerighteq",
	"\\ntrianglelefteq",
	"\\ntriangleleft",
	"\\ntriangleright",
	"\\nleftarrow",
	"\\nrightarrow",
	"\\nLeftarrow",
	"\\nRightarrow",
	"\\nLeftrightarrow",
	"\\nleftrightarrow",
	"\\divideontimes",
	"\\varnothing",
	"\\nexists",
	"\\Finv",
	"\\Game",
	"\\eth",
	"\\eqsim",
	"\\beth",
	"\\gimel",
	"\\daleth",
	"\\lessdot",
	"\\gtrdot",
	"\\ltimes",
	"\\rtimes",
	"\\shortmid",
	"\\shortparallel",
	"\\smallsetminus",
	"\\thicksim",
	"\\thickapprox",
	"\\approxeq",
	"\\succapprox",
	"\\precapprox",
	"\\curvearrowleft",
	"\\curvearrowright",
	"\\digamma",
	"\\varkappa",
	"\\Bbbk",
	"\\hslash",
	"\\backepsilon",
	"\\Diamond",
	"\\leadsto",
	"\\guillemotleft",
	"\\guillemotright",
	"\\guilsinglleft",
	"\\guilsinglright",
	"\\fg",
	"\\og",
	"\\textperthousand",
	"\\textpertenthousand",
	"\\textminus",
	"\\textendash",
	"\\textemdash",
	"\\S",
	"\\P",
	"\\comma",
	"\\ldotp",
	"\\cdotp",
	"\\normaldot",
	"\\textnormaldot",
	"\\slash",
	"\\semicolon",
	"\\faculty",
	"\\question",
	"\\questiondown",
	"\\jlatexmathsharp",
	"\\textdollar",
	"\\textpercent",
	"\\textampersand",
	"\\textfractionsolidus",
	"\\jlatexmatharobase",
	"\\underscore",
	"\\checkmark",
	"\\mathsterling",
	"\\yen",
	"\\acute",
	"\\grave",
	"\\ddot",
	"\\doubleacute",
	"\\tilde",
	"\\jlatexmathring",
	"\\mathring",
	"\\bar",
	"\\breve",
	"\\check",
	"\\hat",
	"\\vec",
	"\\dot",
	"\\widehat",
	"\\widetilde",
	"\\tie",
	"\\lbrace",
	"\\rbrace",
	"\\lbrack",
	"\\rbrack",
	"\\rsqbrack",
	"\\lsqbrack",
	"\\langle",
	"\\rangle",
	"\\lfloor",
	"\\rfloor",
	"\\lceil",
	"\\rceil",
	"\\uparrow",
	"\\Uparrow",
	"\\downarrow",
	"\\Downarrow",
	"\\updownarrow",
	"\\Updownarrow",
	"\\vert",
	"\\Vert",
	"\\slashdel",
	"\\Relbar",
	"\\lgroup",
	"\\rgroup",
	"\\bracevert",
	"\\lmoustache",
	"\\rmoustache",
	"\\alpha",
	"\\beta",
	"\\gamma",
	"\\delta",
	"\\epsilon",
	"\\varepsilon",
	"\\zeta",
	"\\eta",
	"\\theta",
	"\\vartheta",
	"\\iota",
	"\\kappa",
	"\\lambda",
	"\\mu",
	"\\nu",
	"\\xi",
	"\\omicron",
	"\\pi",
	"\\varpi",
	"\\rho",
	"\\varrho",
	"\\sigma",
	"\\varsigma",
	"\\tau",
	"\\upsilon",
	"\\phi",
	"\\varphi",
	"\\chi",
	"\\psi",
	"\\omega",
	"\\Gamma",
	"\\Delta",
	"\\Theta",
	"\\Lambda",
	"\\Xi",
	"\\Pi",
	"\\Sigma",
	"\\Upsilon",
	"\\Phi",
	"\\Psi",
	"\\Omega",
	"\\varGamma",
	"\\varDelta",
	"\\varTheta",
	"\\varLambda",
	"\\varXi",
	"\\varPi",
	"\\varSigma",
	"\\varUpsilon",
	"\\varPhi",
	"\\varPsi",
	"\\varOmega",
	"\\aleph",
	"\\imath",
	"\\jmath",
	"\\ell",
	"\\wp",
	"\\Re",
	"\\Im",
	"\\partial",
	"\\infty",
	"\\prime",
	"\\emptyset",
	"\\nabla",
	"\\surdsign",
	"\\top",
	"\\bot",
	"\\|",
	"\\triangle",
	"\\backslash",
	"\\forall",
	"\\exists",
	"\\neg",
	"\\lnot",
	"\\flat",
	"\\natural",
	"\\sharp",
	"\\clubsuit",
	"\\diamondsuit",
	"\\heartsuit",
	"\\spadesuit",
	"\\lacc",
	"\\racc",
	"\\bigcap",
	"\\bigcup",
	"\\bigodot",
	"\\bigoplus",
	"\\bigotimes",
	"\\bigsqcup",
	"\\biguplus",
	"\\bigvee",
	"\\bigwedge",
	"\\coprod",
	"\\int",
	"\\oint",
	"\\sum",
	"\\prod",
	"\\smallint",
	"\\minus",
	"\\plus",
	"\\pm",
	"\\mp",
	"\\setminus",
	"\\cdot",
	"\\times",
	"\\ast",
	"\\star",
	"\\diamond",
	"\\circ",
	"\\bullet",
	"\\div",
	"\\cap",
	"\\cup",
	"\\uplus",
	"\\sqcap",
	"\\sqcup",
	"\\triangleleft",
	"\\triangleright",
	"\\wr",
	"\\bigcirc",
	"\\vee",
	"\\lor",
	"\\land",
	"\\wedge",
	"\\oplus",
	"\\ominus",
	"\\otimes",
	"\\oslash",
	"\\odot",
	"\\dagger",
	"\\ddagger",
	"\\amalg",
	"\\equals",
	"\\gt",
	"\\lt",
	"\\leq",
	"\\le",
	"\\prec",
	"\\preceq",
	"\\ll",
	"\\subset",
	"\\subseteq",
	"\\sqsubseteq",
	"\\in",
	"\\vdash",
	"\\smile",
	"\\frown",
	"\\geq",
	"\\ge",
	"\\succ",
	"\\succeq",
	"\\gg",
	"\\supset",
	"\\supseteq",
	"\\sqsupseteq",
	"\\ni",
	"\\owns",
	"\\dashv",
	"\\mid",
	"\\parallel",
	"\\equiv",
	"\\sim",
	"\\simeq",
	"\\asymp",
	"\\approx",
	"\\propto",
	"\\perp",
	"\\not",
	"\\colon",
	"\\nearrow",
	"\\searrow",
	"\\swarrow",
	"\\nwarrow",
	"\\leftarrow",
	"\\gets",
	"\\Leftarrow",
	"\\rightarrow",
	"\\to",
	"\\Rightarrow",
	"\\leftrightarrow",
	"\\Leftrightarrow",
	"\\leftharpoonup",
	"\\leftharpoondown",
	"\\rightharpoonup",
	"\\rightharpoondown",
	"\\textmu",
	"\\texteuro",
	"\\euro"
	};
	
	private static String[] texFormuls = new String[] {
	"\\plus",
	"\\minus",
	"\\slash",
	"\\ast" ,
	"\\lbrack",
	"\\normaldot",
	"\\semicolon",
	"\\gt",
	"\\lbrace",
	"\\faculty",
	"\\rbrack",
	"\\comma",
	"\\lt",
	"\\question",
	"\\rsqbrack",
	"\\vert",
	"\\colon",
	"\\equals",
	"\\lsqbrack",
	"\\rbrace",
	"\\textapos",
	"\\jlatexmathlapos",
	"\\jlatexmathsharp",
	"\\mathsterling",
	"\\yen",
	"\\S",
	"\\guillemotleft",
	"\\textregistered",
	"\\textmu",
	"\\P",
	"\\guillemotright",
	"\\questiondown",
	"\\alpha",
	"\\beta",
	"\\gamma",
	"\\delta",
	"\\varepsilon",
	"\\zeta",
	"\\eta",
	"\\theta",
	"\\iota",
	"\\kappa",
	"\\lambda",
	"\\mu",
	"\\nu",
	"\\xi",
	"\\omicron",
	"\\pi",
	"\\rho",
	"\\varsigma",
	"\\sigma",
	"\\tau",
	"\\upsilon",
	"\\varphi",
	"\\chi",
	"\\psi",
	"\\omega",
	"\\vartheta",
	"\\phi",
	"\\varpi",
	"\\varkappa",
	"\\varrho",
            "\\ ",
            "!`",
            "\\copyright",
            "\\`A",
            "\\'A",
            "\\^A",
            "\\~A",
            "\\&quot;A",
            "\\r A",
            "\\AE",
            "\\c C",
            "\\`E",
            "\\'E",
            "\\^E",
            "\\&quot;E",
            "\\`I",
            "\\'I",
            "\\^I",
            "\\&quot;I",
            "\\~N",
            "\\`O",
            "\\'O",
            "\\^O",
            "\\~O",
            "\\&quot;O",
            "\\O",
            "\\`U",
            "\\'U",
            "\\^U",
            "\\&quot;U",
            "\\'Y",
            "\\ss",
            "\\`a",
            "\\'a",
            "\\^a",
            "\\~a",
            "\\&quot;a",
            "\\aa",
            "\\ae",
            "\\c c",
            "\\`e",
            "\\'e",
            "\\^e",
            "\\&quot;e",
            "\\`\\i",
            "\\'\\i",
            "\\^\\i",
            "\\&quot;\\i",
            "\\eth",
            "\\~n",
            "\\`o",
            "\\'o",
            "\\^o",
            "\\~o",
            "\\&quot;o",
            "\\div",
            "\\o",
            "\\`u",
            "\\'u",
            "\\^u",
            "\\&quot;u",
            "\\'y",
            "\\&quot;y",
            "\\=A",
            "\\=a",
            "\\u A",
            "\\u a",
            "\\k A",
            "\\k a",
            "\\'C",
            "\\'c",
            "\\^C",
            "\\^c",
            "\\.C",
            "\\.c",
            "\\v C",
            "\\v c",
            "\\v D",
            "d\\text{'}",
            "\\Dstrok",
            "\\dstrok",
            "\\=E",
            "\\=e",
            "\\u E",
            "\\u e",
            "\\.E",
            "\\.e",
            "\\k E",
            "\\k e",
            "\\v E",
            "\\v e",
            "\\^G",
            "\\^g",
            "\\u G",
            "\\u g",
            "\\.G",
            "\\.g",
            "\\underaccent{,}G",
            "\\'g",
            "\\^H",
            "\\^h",
            "\\Hstrok",
            "\\hstrok",
            "\\~I",
            "\\~\\i",
            "\\=I",
            "\\=\\i",
            "\\u I",
            "\\u \\i",
            "\\k I",
            "\\k i",
            "\\.I",
            "\\i",
            "\\IJ",
            "\\ij",
            "\\^J",
            "\\^\\j",
            "\\underaccent{,}K",
            "\\underaccent{,}k",
            "\\'L",
            "\\'l",
            "\\underaccent{,}L",
            "\\underaccent{,}l",
            "\\Lcaron",
            "\\lcaron",
            "L\\cdot",
            "l\\cdot",
            "\\L",
            "\\l",
            "\\'N",
            "\\'n",
            "\\underaccent{,}N",
            "\\underaccent{,}n",
            "\\v N",
            "\\v n",
            "\\text{'}n",
            "\\=O",
            "\\=o",
            "\\u O",
            "\\u o",
            "\\H O",
            "\\H o",
            "\\OE",
            "\\oe",
            "\\'R",
            "\\'r",
            "\\underaccent{,}R",
            "\\underaccent{,}r",
            "\\v R",
            "\\v r",
            "\\'S",
            "\\'s",
            "\\^S",
            "\\^s",
            "\\c S",
            "\\c s",
            "\\v S",
            "\\v s",
            "\\c T",
            "\\c t",
            "\\v T",
            "\\tcaron",
            "\\TStroke",
            "\\tStroke",
            "\\~U",
            "\\~u",
            "\\=U",
            "\\=u",
            "\\u U",
            "\\u u",
            "\\r U",
            "\\r u",
            "\\H U",
            "\\H u",
            "\\k U",
            "\\k u",
            "\\^W",
            "\\^w",
            "\\^Y",
            "\\^y",
            "\\&quot;Y",
            "\\'Z",
            "\\'z",
            "\\.Z",
            "\\.z",
            "\\v Z",
            "\\v z",
            "\\Alpha",
            "\\Beta",
            "\\Gamma",
            "\\Delta",
            "\\Epsilon",
            "\\Zeta",
            "\\Eta",
            "\\Theta",
            "\\Iota",
            "\\Kappa",
            "\\Lambda",
            "\\Mu",
            "\\Nu",
            "\\Xi",
            "\\Omicron",
            "\\Pi",
            "\\Rho",
            "\\Sigma",
            "\\Tau",
            "\\Upsilon",
            "\\Phi",
            "\\Chi",
            "\\Psi",
            "\\Omega",
            "\\nshortparallel",
            "\\leftrightharpoons",
            "\\Rrightarrow",
            "\\supsetneqq",
            "\\ge",
            "\\rhd",
            "\\leqslant",
            "\\downdownarrows",
            "\\gtrsim",
            "\\lessgtr",
            "\\longrightarrow",
            "\\Leftrightarrow",
            "\\leftarrow",
            "\\ominus",
            "\\!",
            "\\otimes",
            "\\lceil",
            "\\partial",
            "\\smallfrown",
            "\\,",
            "\\gg",
            "\\mathbin{\\rlap{&gt;}\\!&lt;",
            "\\mathbin{&gt;&lt;}",
            "\\leftslice",
            "\\rightslice",
            "\\;",
            "\\:",
            "\\hbar",
            "\\swarrow",
            "\\gneqq",
            "\\neq",
            "\\times",
            "\\lnot",
            "\\nsubseteqq",
            "\\uparrow",
            "\\curlyvee",
            "\\setminus",
            "{}_\\ast",
            "\\supsetneq",
            "\\nleftarrow",
            "\\nLeftrightarrow",
            "\\blacktriangledown",
            "\\downharpoonright",
            "\\subseteqq",
            "\\exists",
            "\\emptyset",
            "\\ggg",
            "\\sim",
            "\\lvertneqq",
            "\\|",
            "\\vee",
            "\\bullet",
            "\\subsetneqq",
            "\\triangledown",
            "\\simeq",
            "\\rightrightarrows",
            "\\bigtriangledown",
            "\\nleqslant",
            "\\Rsh",
            "\\searrow",
            "\\blacktriangleleft",
            "\\geq",
            "\\Vert",
            "\\ntrianglerighteq",
            "\\leftrightarrow",
            "\\barwedge",
            "\\ntriangleright",
            "\\rtimes",
            "\\iff",
            "\\circleddash",
            "\\Cup",
            "\\upuparrows",
            "\\ngtr",
            "\\succnsim",
            "\\coloncolonequals",
            "\\lnapprox",
            "\\gtrdot",
            "\\ldots",
            "\\approx",
            "\\lessapprox",
            "\\curlywedge",
            "\\gvertneqq",
            "\\ncong",
            "\\bumpeq",
            "\\Longleftarrow",
            "\\parallel",
            "\\thinspace",
            "\\rangle",
            "\\bigcap",
            "\\clubsuit",
            "\\supset",
            "\\not\\subset",
            "\\not\\supset",
            "\\oslash",
            "\\therefore",
            "\\ratio",
            "\\mathbin{\\ratio\\ratio}",
            "\\succapprox",
            "\\bot",
            "\\gtreqless",
            "\\Bumpeq",
            "\\Updownarrow",
            "\\backepsilon",
            "\\cup",
            "\\Longleftrightarrow",
            "\\preceq",
            "\\doublecup",
            "\\lVert",
            "\\supseteq",
            "\\ngeqslant",
            "\\dagger",
            "\\nsucceq",
            "\\amalg",
            "\\lesseqqgtr",
            "\\leqq",
            "\\circeq",
            "\\smallfrowneq",
            "\\stackrel{\\wedge}{=}",
            "\\stackrel{\\vee}{=}",
            "\\stackrel{\\scalebox{0.8}{\\bigstar}}{=}",
            "\\triangleq",
            "\\stackrel{\\scalebox{0.75}{\\mathrm{def}}}{=}",
            "\\stackrel{\\scalebox{0.75}{\\mathrm{m}}}{=}",
            "\\stackrel{\\scalebox{0.75}{\\mathrm{?}}}{=}",
            "\\leftharpoonup",
            "\\circledcirc",
            "\\nwarrow",
            "\\lnsim",
            "\\smallsmile",
            "\\odot",
            "\\nmid",
            "\\lessdot",
            "\\prime",
            "\\pm",
            "\\sqsupseteq",
            "\\doteq",
            "\\ll",
            "\\geqq",
            "\\heartsuit",
            "\\upharpoonright",
            "\\leftrightarrows",
            "\\rightthreetimes",
            "\\mp",
            "\\npreceq",
            "\\vdots",
            "\\ntriangleleft",
            "\\varpropto",
            "\\star",
            "\\minuscolon",
            "\\geoprop",
            "\\thicksim",
            "\\vartriangleleft",
            "\\blacksquare",
            "\\marker",
            "\\triangle",
            "\\biguplus",
            "\\succnapprox",
            "\\gnapprox",
            "\\ne",
            "\\square",
            "\\ni",
            "\\iddots",
            "\\ddots",
            "\\inplus",
            "\\nvdash",
            "\\coprod",
            "\\shortmid",
            "\\hookleftarrow",
            "\\nRightarrow",
            "\\upharpoonleft",
            "\\boxdot",
            "\\nsupseteq",
            "\\in",
            "\\notin",
            "\\ltimes",
            "\\succ",
            "\\minus",
            "\\succeq",
            "\\restriction",
            "\\int",
            "\\flat",
            "\\Game",
            "\\parr",
            "\\succcurlyeq",
            "\\langle",
            "\\cdot",
            "\\sqcup",
            "\\gimel",
            "\\circledast",
            "\\eqslantless",
            "\\le",
            "\\gtrless",
            "\\propto",
            "\\triangleleft",
            "\\blacktriangle",
            "\\Im",
            "\\bigoplus",
            "\\iiiint",
            "\\backsim",
            "\\infty",
            "\\subset",
            "\\Cap",
            "\\diamond",
            "\\nVdash",
            "\\fallingdotseq",
            "\\colonequals",
            "\\equalscolon",
            "\\between",
            "\\downarrow",
            "\\gets",
            "\\curlyeqsucc",
            "\\uplus",
            "\\in",
            "\\nsubseteq",
            "\\divideontimes",
            "\\lneqq",
            "\\precnsim",
            "\\niplus",
            "\\medspace",
            "\\geqslant",
            "\\asymp",
            "\\doublebarwedge",
            "\\sqsubseteq",
            "\\lmoustache",
            "\\nsim",
            "\\sqcap",
            "\\supseteqq",
            "\\leftthreetimes",
            "\\shortparallel",
            "\\vdash",
            "\\measuredangle",
            "\\subseteq",
            "\\llless",
            "\\lor",
            "\\Leftarrow",
            "\\wp",
            "\\wr",
            "\\bigotimes",
            "\\ngeqq",
            "\\models",
            "\\boxplus",
            "\\rightarrowtail",
            "\\boxminus",
            "\\smallsetminus",
            "\\gtrapprox",
            "\\prec",
            "\\downharpoonleft",
            "\\leadsto",
            "\\owns",
            "\\eqslantgtr",
            "\\cong",
            "\\looparrowleft",
            "\\beth",
            "\\iint",
            "\\vartriangle",
            "\\perp",
            "\\nLeftarrow",
            "\\bigsqcup",
            "\\nabla",
            "\\trianglelefteq",
            "\\sqsubset",
            "\\nexists",
            "\\blacktriangleright",
            "\\wedge",
            "\\angle",
            "\\&apos;",
            "\\bigodot",
            "\\quad",
            "\\circledS",
            "\\Vvdash",
            "\\preccurlyeq",
            "\\triangleright",
            "\\approxeq",
            "\\to",
            "\\veebar",
            "\\Downarrow",
            "\\nvDash",
            "\\lesssim",
            "\\precsim",
            "\\oint",
            "\\subsetneq",
            "\\Vdash",
            "\\diagup",
            "\\sphericalangle",
            "\\diamondsuit",
            "\\dashv",
            "\\nearrow",
            "\\nrightarrow",
            "\\gtreqqless",
            "\\bowtie",
            "\\pitchfork",
            "\\natural",
            "\\succneqq",
            "\\varsupsetneq",
            "\\leftrightsquigarrow",
            "\\cap",
            "\\rmoustache",
            "\\land",
            "\\Supset",
            "\\sum",
            "\\multimap",
            "\\rfloor",
            "\\nleftrightarrow",
            "\\lozenge",
            "\\leftleftarrows",
            "\\rightsquigarrow",
            "\\boxtimes",
            "\\rightleftarrows",
            "\\ntrianglelefteq",
            "\\iiint",
            "\\top",
            "\\rightarrow",
            "\\Re",
            "\\sqsupset",
            "\\twoheadrightarrow",
            "\\blacksquare",
            "\\varsupsetneqq",
            "\\curvearrowleft",
            "\\daleth",
            "\\thickspace",
            "\\gggtr",
            "\\eqcirc",
            "\\varsubsetneqq",
            "\\vert",
            "\\succsim",
            "\\Uparrow",
            "\\nleqq",
            "\\mho",
            "\\text{\\AA}",
            "\\eqsim",
            "\\looparrowright",
            "\\rightharpoonup",
            "\\equiv",
            "\\not\\equiv",
            "\\leftarrowtail",
            "\\lhd",
            "\\bigstar",
            "\\gnsim",
            "\\vDash",
            "\\blacklozenge",
            "\\cdot",
            "\\nless",
            "\\ddagger",
            "\\risingdotseq",
            "\\ell",
            "\\doteqdot",
            "\\lneq",
            "\\Rightarrow",
            "\\Longrightarrow",
            "\\curvearrowright",
            "\\hookrightarrow",
            "\\rVert",
            "\\longleftrightarrow",
            "\\curlyeqprec",
            "\\longleftarrow",
            "\\rightharpoondown",
            "\\hslash",
            "\\doublecap",
            "\\prod",
            "\\updownarrow",
            "\\Lsh",
            "\\nsucc",
            "\\slash",
            "\\leftharpoondown",
            "\\leq",
            "\\mathit{o}",
            "\\aleph",
            "\\nVDash",
            "\\lfloor",
            "\\circ",
            "\\bigcup",
            "\\nshortmid",
            "\\precapprox",
            "\\trianglerighteq",
            "\\mid",
            "\\dotplus",
            "\\nprec",
            "\\cdots",
            "\\Lleftarrow",
            "\\precneqq",
            "e",
            "\\Finv",
            "\\intercal",
            "\\surd",
            "\\bigvee",
            "\\gneq",
            "\\forall",
            "\\precnapprox",
            "\\spadesuit",
            "\\longmapsto",
            "\\llbracket",
            "\\rrbracket",
            "\\langle",
            "\\rangle",
            "\\bigcirc",
            "\\backsimeq",
            "\\div",
            "\\rbrace",
            "\\complement",
            "\\because",
            "\\dotminus",
            "\\bigtriangleup",
            "\\twoheadleftarrow",
            "\\epsilon",
            "\\Arrowvert",
            "\\Diamond",
            "\\varsubsetneq",
            "\\arrowvert",
            "\\sharp",
            "\\bigwedge",
            "\\varnothing",
            "\\diagdown",
            "\\mapsto",
            "\\vartriangleright",
            "\\oplus",
            "\\thickapprox",
            "\\rceil",
            "\\Subset",
            "\\rightleftharpoons",
            "\\acute",
            "\\grave",
            "\\ddot",
            "\\doubleacute",
            "\\tilde",
            "\\jlatexmathring",
            "\\bar",
            "\\breve",
            "\\check",
            "\\hat",
            "\\vec",
            "\\dot",
            "\\widehat",
            "\\widetilde",
            "\\mathbb{C}",
            "\\sideset{^\\circ}{}\\text{C}",
            "\\jlmEuler",
            "\\sideset{^\\circ}{}\\text{F}",
            "\\mathbb{H}",
            "\\mathbb{N}",
            "\\mathbb{P}",
            "\\mathbb{Q}",
            "\\mathbb{R}",
            "\\mathbb{Z}",
            "\\mathfrak{Z}",
            "\\mathscr{H}",
            "\\mathfrak{H}",
            "\\mathscr{I}",
            "\\mathscr{L}",
            "\\mathscr{R}",
            "\\mathscr{B}",
            "\\mathfrak{C}",
            "\\mathscr{E}",
            "\\mathscr{F}",
            "\\mathscr{M}",
            "ff",
            "fi",
            "fj",
            "fl",
            "ffi",
            "ffl",
            "\\mathbin{\\rlap{&lt;}\\;(}",
            "\\mathbin{\\rlap{&gt;}\\,)}",
            "\\minuso",
            "\\varocircle",
            "\\olessthan",
            "\\ogreaterthan",
            "\\boxslash",
            "\\boxbslash",
            "\\maltese",
            "\\checkmark",
            "\\mathpunct{\\={\\ }}",
            "\\hybull",
            "\\textperthousand",
            "\\textpertenthousand",
            "\\guilsinglleft",
            "\\guilsinglright",
            "\\not\\simeq",
            "&apos;&apos;&apos;",
            "&apos;&apos;",
            "`",
            "``",
            "\\textapos",
            ",",
            "\\textapos\\textapos",
            ",,",
            " ",
            " ",
            "\\euro",
            "\\textminus",
            "\\textendash",
            "\\textemdash",
            "\\sfrac{a}{c}",
            "\\sfrac{a}{s}",
            "\\sfrac{c}{o}",
            "\\sfrac{c}{u}",
            "{}^{\\text{TM}}",
            "\\ulcorner",
            "\\urcorner",
            "\\llcorner",
            "\\lrcorner",
            "\\text{\\sfrac13}",
            "\\text{sfrac23}",
            "\\text{\\sfrac15}",
            "\\text{\\sfrac25}",
            "\\text{\\sfrac35}",
            "\\text{\\sfrac45}",
            "\\text{\\sfrac16}",
            "\\text{\\sfrac56}",
            "\\text{\\sfrac12}",
            "\\text{\\sfrac14}",
            "\\text{\\sfrac34}",
            "\\text{\\sfrac18}",
            "\\text{\\sfrac38}",
            "\\text{\\sfrac58}",
            "\\text{\\sfrac78}",
            "\\text{\\sfrac{1}{\\ }}",
            "\\text{I}",
            "\\text{II}",
            "\\text{III}",
            "\\text{IV}",
            "\\text{V}",
            "\\text{VI}",
            "\\text{VII}",
            "\\text{VIII}",
            "\\text{IX}",
            "\\text{X}",
            "\\text{XI}",
            "\\text{XII}",
            "\\text{L}",
            "\\text{C}",
            "\\text{D}",
            "\\text{M}",
            "\\text{i}",
            "\\text{ii}",
            "\\text{iii}",
            "\\text{iv}",
            "\\text{v}",
            "\\text{vi}",
            "\\text{vii}",
            "\\text{viii}",
            "\\text{ix}",
            "\\text{x}",
            "\\text{xi}",
            "\\text{xii}",
            "\\text{l}",
            "\\text{c}",
            "\\text{d}",
            "\\text{m}",
            "\\uhblk",
            "\\lhblk",
            "\\block",
            "\\fgcolor{bfbfbf}{\\block}",
            "\\fgcolor{808080}{\\block}",
            "\\fgcolor{404040}{\\block}",
            "\\textcircled{\\texttt 1}",
            "\\textcircled{\\texttt 2}",
            "\\textcircled{\\texttt 3}",
            "\\textcircled{\\texttt 4}",
            "\\textcircled{\\texttt 5}",
            "\\textcircled{\\texttt 6}",
            "\\textcircled{\\texttt 7}",
            "\\textcircled{\\texttt 8}",
            "\\textcircled{\\texttt 9}",
            "\\textcircled{\\texttt A}",
            "\\textcircled{\\texttt B}",
            "\\textcircled{\\texttt C}",
            "\\textcircled{\\texttt D}",
            "\\textcircled{\\texttt E}",
            "\\textcircled{\\texttt F}",
            "\\textcircled{\\texttt G}",
            "\\textcircled{\\texttt H}",
            "\\textcircled{\\texttt I}",
            "\\textcircled{\\texttt J}",
            "\\textcircled{\\texttt K}",
            "\\textcircled{\\texttt L}",
            "\\textcircled{\\texttt M}",
            "\\textcircled{\\texttt N}",
            "\\textcircled{\\texttt O}",
            "\\textcircled{\\texttt P}",
            "\\textcircled{\\texttt Q}",
            "\\textcircled{\\texttt R}",
            "\\textcircled{\\texttt S}",
            "\\textcircled{\\texttt T}",
            "\\textcircled{\\texttt U}",
            "\\textcircled{\\texttt V}",
            "\\textcircled{\\texttt W}",
            "\\textcircled{\\texttt X}",
            "\\textcircled{\\texttt Y}",
            "\\textcircled{\\texttt Z}",
            "\\textcircled{\\texttt a}",
            "\\textcircled{\\texttt b}",
            "\\textcircled{\\texttt c}",
            "\\textcircled{\\texttt d}",
            "\\textcircled{\\texttt e}",
            "\\textcircled{\\texttt f}",
            "\\textcircled{\\texttt g}",
            "\\textcircled{\\texttt h}",
            "\\textcircled{\\texttt i}",
            "\\textcircled{\\texttt j}",
            "\\textcircled{\\texttt k}",
            "\\textcircled{\\texttt l}",
            "\\textcircled{\\texttt m}",
            "\\textcircled{\\texttt n}",
            "\\textcircled{\\texttt o}",
            "\\textcircled{\\texttt p}",
            "\\textcircled{\\texttt q}",
            "\\textcircled{\\texttt r}",
            "\\textcircled{\\texttt s}",
            "\\textcircled{\\texttt t}",
            "\\textcircled{\\texttt u}",
            "\\textcircled{\\texttt v}",
            "\\textcircled{\\texttt w}",
            "\\textcircled{\\texttt x}",
            "\\textcircled{\\texttt y}",
            "\\textcircled{\\texttt z}",
    };
	public static Integer foreground = Color.BLACK;
	public static Integer background = Color.TRANSPARENT;
}

