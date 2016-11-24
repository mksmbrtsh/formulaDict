package maximsblog.blogspot.com.formuladict;

import java.util.ArrayList;
import java.util.Map;

public class Result {
	public ArrayList<Map<String, Object>> chapterData;
	public ArrayList<ArrayList<Map<String, Object>>> articleData;

	public Result(ArrayList<Map<String, Object>> chapterData,
			ArrayList<ArrayList<Map<String, Object>>> articleData) {
		this.chapterData = chapterData;
		this.articleData = articleData;
	}
}
