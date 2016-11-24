package maximsblog.blogspot.com.formuladict;

import android.widget.MultiAutoCompleteTextView.Tokenizer;

public class SpaceTokenizer implements Tokenizer {

	public int findTokenStart(CharSequence text, int cursor) {
		int i = cursor;

		if(i>0){
			do {
				i--;
				
			} while (i > 0 && !(text.charAt(i) == '\\' || text.charAt(i) == '{' || text.charAt(i) == '}'));
		}

		return i;
	}

	public int findTokenEnd(CharSequence text, int cursor) {
		int i = cursor;
		int len = text.length();

		while (i < len) {
			if (text.charAt(i) == '\\' || text.charAt(i) == '{' || text.charAt(i) == '}') {
				return i;
			} else {
				i++;
			}
		}

		return len;
	}

	public CharSequence terminateToken(CharSequence text) {
		return text;
	}
}