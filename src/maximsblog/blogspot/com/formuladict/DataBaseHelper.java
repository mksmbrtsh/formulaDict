package maximsblog.blogspot.com.formuladict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseHelper extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "matheditor";
	private static final int DATABASE_VERSION = 1;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * Copies the database file at the specified location over the current
	 * internal application database.
	 * 
	 * @param file
	 * */
	public boolean exportDatabase(File file, String dbPath) throws IOException {

		// Close the SQLiteOpenHelper so it will commit the created empty
		// database to internal storage.
		close();
		String destPath = file.getPath();
		destPath = destPath.substring(0, destPath.lastIndexOf("/"))
				+ "/databases";
		String DB_FILEPATH = destPath + File.separator + DATABASE_NAME;
		File newDb = new File(DB_FILEPATH);
		File oldDb = new File(dbPath);
		if (newDb.exists()) {
			copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
			// Access the copied database so SQLiteHelper will cache it and mark
			// it as created.
			getWritableDatabase();
			return true;
		}
		return false;
	}

	private void copyFile(FileInputStream fromFile, FileOutputStream toFile)
			throws IOException {
		FileChannel fromChannel = null;
		FileChannel toChannel = null;
		try {
			fromChannel = fromFile.getChannel();
			toChannel = toFile.getChannel();
			fromChannel.transferTo(0, fromChannel.size(), toChannel);
		} finally {
			try {
				if (fromChannel != null) {
					fromChannel.close();
				}
			} finally {
				if (toChannel != null) {
					toChannel.close();
				}
			}
		}
	}
	

	private static int start = 1;

	public static final String ArticleView = "Articles";
	public static final int ArticleCodeView = start++;
	
	public static final String ArticleTagsView = "ArticleTags";
	public static final int ArticleTagsCodeView = start++;

	public static final String ChapterView = "Chapters";
	public static final int ChapterCodeView = start++;
	
	public static final String SubjectsView = "Subjects";
	public static final int SubjectsCodeView = start++;
	
	public static final String OpenedChapters = "OpenedChapters";
	public static final int OpenedChaptersCodeView = start++;
	
	public static final String OpenedSubjects = "OpenedSubjects";
	public static final int OpenedSubjectsCodeView = start++;
	
	public static final String SelectedArticles = "SelectedArticles";
	public static final int SelectedArticlesCodeView = start++;
	
	public static class Tables {
		public static class Subjects {
			public static final String TableName = "Subjects";

			public static class Column {
				public static final String SubjectID = "SubjectID";
				public static final String Title = "Title";
				public static final String LanguageID = "LanguageID";
			}
		}
		
		
		public static class Chapters {
			public static final String TableName = "Chapters";

			public static class Column {
				public static final String ChapterID = "ChapterID";
				public static final String Title = "Title";
				public static final String LanguageID = "LanguageID";
				public static final String IndexRow = "IndexRow";
				public static final String SubjectID = "SubjectID";
			}
		}
		
		public static class Articles {
			public static final String TableName = "Articles";

			public static class Column {
				public static final String ArticlesID = "ArticlesID";
				public static final String ChapterID = "ChapterID";
				public static final String Title = "Title";
				public static final String Description = "Description";
				public static final String Comment = "Comment";
				public static final String LanguageID = "LanguageID";
				public static final String IndexRow = "IndexRow";
			}
		}

		public static class ArticleTagConnection {
			public static final String TableName = "ArticleTagConnection";

			public static class Column {
				public static final String ArticlesID = "ArticlesID";
				public static final String TagID = "TagID";

			}
		}
		
		public static class ArticleTags {
			public static final String TableName = "ArticleTags";

			public static class Column {
				public static final String TagID = "TagID";
				public static final String Name = "Name";
				public static final String LanguageID = "LanguageID";

			}
		}
		
		public static class LaTeXCommands {
			public static final String TableName = "LaTeXCommands";

			public static class Column {
				public static final String CommandID = "CommandID";
				public static final String Command = "Command";
				public static final String Comment = "Comment";
				public static final String LanguageID = "LanguageID";
			}
		}
		
		public static class LaTexTagConnection {
			public static final String TableName = "LaTexTagConnection";

			public static class Column {
				public static final String CommandID = "CommandID";
				public static final String TagId = "TagId";
			}
		}
		
		public static class LaTexTags {
			public static final String TableName = "LaTexTags";

			public static class Column {
				public static final String TagID = "TagID";
				public static final String Name = "Name";
				public static final String LanguageID = "LanguageID";

			}
		}
		public static class OpenedChapters {
			public static final String TableName = "OpenedChapters";

			public static class Column {
				public static final String SubjectID = "SubjectID";
				public static final String ChapterID = "ChapterID";

			}
		}
		
		public static class OpenedSubjects {
			public static final String TableName = "OpenedSubjects";

			public static class Column {
				public static final String _id = "SubjectID";
				public static final String State = "State";
			}
		}
		
		public static class SelectedArticles {
			public static final String TableName = "SelectedArticles";

			public static class Column {
				public static final String SubjectID = "SubjectID";
				public static final String ChapterID = "ChapterID";
				public static final String ArticlesID = "ArticlesID";
			}
		}
		
		
	}

}
