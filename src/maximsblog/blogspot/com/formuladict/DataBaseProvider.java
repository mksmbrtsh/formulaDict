package maximsblog.blogspot.com.formuladict;

import java.util.Date;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataBaseProvider extends ContentProvider {

	public static final String AUTHORITY = "maximsblog.blogspot.com.formuladict.db";

	private static final UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, DataBaseHelper.ArticleView,
				DataBaseHelper.ArticleCodeView);
		sUriMatcher.addURI(AUTHORITY, DataBaseHelper.ArticleTagsView,
				DataBaseHelper.ArticleTagsCodeView);
		sUriMatcher.addURI(AUTHORITY, DataBaseHelper.ChapterView,
				DataBaseHelper.ChapterCodeView);
		sUriMatcher.addURI(AUTHORITY, DataBaseHelper.SubjectsView,
				DataBaseHelper.SubjectsCodeView);
		sUriMatcher.addURI(AUTHORITY, DataBaseHelper.SelectedArticles,
				DataBaseHelper.SelectedArticlesCodeView);
	}

	public static final Uri CONTENT_URI_Articles = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.ArticleView);
	public static final Uri CONTENT_URI_ArticleTags = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.ArticleTagsView);
	public static final Uri CONTENT_URI_Chapters = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.ChapterView);
	public static final Uri CONTENT_URI_Subjects = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.SubjectsView);
	public static final Uri CONTENT_URI_OpenedChapters = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.OpenedChapters);
	public static final Uri CONTENT_URI_OpenedSubjects = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.OpenedSubjects);
	public static final Uri CONTENT_URI_SelectedArticles = Uri.parse("content://"
			+ AUTHORITY + "/" + DataBaseHelper.SelectedArticles);
	
	private SQLiteDatabase mDB;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DataBaseHelper openHelper = new DataBaseHelper(context);
		mDB = openHelper.getReadableDatabase();
		return (mDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		int i = sUriMatcher.match(uri);
		String langID = App.getLanguageID();
		String sql="";
		if (i == DataBaseHelper.ArticleCodeView) {
			 sql = "SELECT a.IndexRow, a.ChapterID, a.ArticlesID, a.Title AS ArticleTitle, a.Description, a.Comment, a.LanguageID, c.Title AS ChapterTitle, c.IndexRow AS ChapterNumber FROM Articles a LEFT JOIN Chapters c ON c.ChapterID = a.ChapterID WHERE a.LanguageID = " + langID + " AND c.LanguageID=" + langID + " " + selection +" ORDER BY ChapterNumber, a.IndexRow ASC";
		} else if(i == DataBaseHelper.ArticleTagsCodeView) {
			sql = "SELECT at.Name, at.TagID AS _id, atc.ArticlesID FROM ArticleTags at JOIN ArticleTagConnection atc ON atc.TagID = at.TagID WHERE atc.ArticlesID = 0 AND at.LanguageID = " + langID;
		} else if (i == DataBaseHelper.ChapterCodeView){
			sql = "SELECT c.IndexRow, c.ChapterID, c.Title, c.LanguageID, c.SubjectID, case when oc.State ISNULL then 0 else oc.State end AS State FROM Chapters c LEFT JOIN OpenedChapters oc ON oc.ChapterID = c.ChapterID WHERE LanguageID = " + langID + " " + selection + " ORDER BY c.IndexRow ASC";
		} else if(i == DataBaseHelper.SubjectsCodeView){
			sql = "SELECT s.SubjectID AS _id, s.Title, s.LanguageID, s.IndexRow AS IndexRow, case when os.State ISNULL then 0 else os.State end AS State FROM Subjects s LEFT JOIN OpenedSubjects os ON os.SubjectID = s.SubjectID WHERE LanguageID = " + langID + " " + selection + " ORDER BY s.SubjectID";
		} else if(i == DataBaseHelper.SelectedArticlesCodeView){
			sql = "SELECT sa.SubjectID, sa.ArticlesID, sa.ChapterID FROM SelectedArticles sa WHERE " + selection + " ORDER BY sa.SubjectID";
		}
		c = mDB.rawQuery(sql, selectionArgs);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId;
		rowId = mDB.insertWithOnConflict(uri.getLastPathSegment(), null,
				values, SQLiteDatabase.CONFLICT_REPLACE);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if (values.length == 0)
			return 0;
		int insertCount = 0;
		try {
			mDB.beginTransaction();
			for (ContentValues value : values) {
				long id = mDB.insertWithOnConflict(uri.getLastPathSegment(),
						null, value, SQLiteDatabase.CONFLICT_REPLACE);
				if (id > 0)
					insertCount++;
			}
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			// Your error handling
			e.printStackTrace();
		} finally {
			mDB.endTransaction();
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return insertCount;
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		return mDB.delete(uri.getLastPathSegment(), whereClause, whereArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return mDB.update(uri.getLastPathSegment(), values, selection, selectionArgs);
		/*int i = sUriMatcher.match(uri);
		if (i == DataBaseHelper.ArticleCodeView) {
			return mDB.update(DataBaseHelper.Tables.Articles.TableName, values, selection,
					selectionArgs);
		}
		return -1;*/
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

}