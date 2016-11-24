package maximsblog.blogspot.com.formuladict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

public class ListLoader extends AsyncTaskLoader<Result> {

	private int mSubjectID;

	public ListLoader(Context context, int subjectID) {
		super(context);
		mSubjectID = subjectID;
		// run only once
		onContentChanged();
	}

	@Override
	protected void onStartLoading() {
		// That's how we start every AsyncTaskLoader...
		// - code snippet from android.content.CursorLoader (method
		// onStartLoading)
		if (takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	public Result loadInBackground() {
		Result result = new Result(new ArrayList<Map<String, Object>>(),
				new ArrayList<ArrayList<Map<String, Object>>>());

		Cursor groupCursor = getContext().getContentResolver().query(
				DataBaseProvider.CONTENT_URI_Chapters, null,
				" AND c.SubjectID = ? ",
				new String[] { String.valueOf(mSubjectID) }, null);
		if (groupCursor.moveToFirst()) {
			int indexChapterID = groupCursor
					.getColumnIndex(DataBaseHelper.Tables.Chapters.Column.ChapterID);
			int indexTitle = groupCursor
					.getColumnIndex(DataBaseHelper.Tables.Chapters.Column.Title);
			int indexLanguageID = groupCursor
					.getColumnIndex(DataBaseHelper.Tables.Chapters.Column.LanguageID);
			int indexRow = groupCursor
					.getColumnIndex(DataBaseHelper.Tables.Chapters.Column.IndexRow);
			int stateRow = groupCursor.getColumnIndex("State");
			do {
				Map<String, Object> curGroupMap = new HashMap<String, Object>();
				result.chapterData.add(curGroupMap);
				curGroupMap.put(
						DataBaseHelper.Tables.Chapters.Column.ChapterID,
						groupCursor.getInt(indexChapterID));
				String title = groupCursor.getString(indexTitle);
				title = Character.toString(title.charAt(0)).toUpperCase()
						+ title.substring(1);
				curGroupMap.put(DataBaseHelper.Tables.Chapters.Column.Title,
						title);
				curGroupMap.put(
						DataBaseHelper.Tables.Chapters.Column.LanguageID,
						groupCursor.getString(indexLanguageID));
				curGroupMap.put(DataBaseHelper.Tables.Chapters.Column.IndexRow,
						groupCursor.getInt(indexRow));
				curGroupMap.put("State", groupCursor.getInt(stateRow));
				Cursor childrenCursor = getContext().getContentResolver()
						.query(DataBaseProvider.CONTENT_URI_Articles,
								null,
								" AND a.ChapterID = ?",
								new String[] { String.valueOf(groupCursor
										.getInt(indexChapterID)) }, null);

				ArrayList<Map<String, Object>> childrenn = new ArrayList<Map<String, Object>>();
				if (childrenCursor.moveToFirst()) {
					do {
						Map<String, Object> curChildMap = new HashMap<String, Object>();
						childrenn.add(curChildMap);
						curChildMap.put("ArticleID", childrenCursor
								.getInt(childrenCursor
										.getColumnIndex("ArticlesID")));
						curChildMap.put("ChapterID", childrenCursor
								.getInt(childrenCursor
										.getColumnIndex("ChapterID")));
						title = childrenCursor.getString(childrenCursor
								.getColumnIndex("ArticleTitle"));
						title = Character.toString(title.charAt(0))
								.toUpperCase() + title.substring(1);

						curChildMap.put("Title", title);
						curChildMap.put("Description", childrenCursor
								.getString(childrenCursor
										.getColumnIndex("Description")));
						curChildMap.put("Comment", childrenCursor
								.getString(childrenCursor
										.getColumnIndex("Comment")));
						curChildMap.put("LanguageID", childrenCursor
								.getInt(childrenCursor
										.getColumnIndex("LanguageID")));
						curChildMap.put("IndexRow", childrenCursor
								.getInt(childrenCursor
										.getColumnIndex("IndexRow")));
						title = childrenCursor.getString(childrenCursor
								.getColumnIndex("ChapterTitle"));
						title = Character.toString(title.charAt(0))
								.toUpperCase() + title.substring(1);
						curChildMap.put("ChapterTitle", title);
						curChildMap.put("ChapterNumber", childrenCursor
								.getInt(childrenCursor
										.getColumnIndex("ChapterNumber")));
					} while (childrenCursor.moveToNext());
				}
				childrenCursor.close();
				result.articleData.add(childrenn);
			} while (groupCursor.moveToNext());
		}
		groupCursor.close();

		return result;
	}
}
