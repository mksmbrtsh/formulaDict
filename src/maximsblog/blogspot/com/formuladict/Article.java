package maximsblog.blogspot.com.formuladict;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Article implements Parcelable {
	
	public int ArticlesID;
	public int ChapterID;
	public String Title;
	public String Description;
	public String Comment;
	public int LanguageID;
	public int IndexRow;
	public String ChapterTitle;
	public int ChapterNumber;

	public Article(int ArticlesID, int ChapterID, String Title, String Description, String Comment, int LanguageID, int IndexRow, String ChapterTitle, int ChapterNumber ) {
		this.ArticlesID = ArticlesID;
		this.ChapterID = ChapterID;
		this.Title = Title;
		this.Description = Description;
		this.Comment = Comment;
		this.LanguageID = LanguageID;
		this.IndexRow = IndexRow;
		this.ChapterTitle = ChapterTitle;
		this.ChapterNumber = ChapterNumber;
	}
	public Article(Parcel in) {
		this.ArticlesID = in.readInt();
		this.ChapterID = in.readInt();
		this.Title = in.readString();
		this.Description = in.readString();
		this.Comment = in.readString();
		this.LanguageID = in.readInt();
		this.IndexRow = in.readInt();
		this.ChapterTitle = in.readString();
		this.ChapterNumber = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ArticlesID);
		dest.writeInt(ChapterID);
		dest.writeString(Title);
		dest.writeString(Description);
		dest.writeString(Comment);
		dest.writeInt(LanguageID);
		dest.writeInt(IndexRow);
		dest.writeString(ChapterTitle);
		dest.writeInt(ChapterNumber);
	}
	
	public static final Creator<Article> CREATOR = new Creator<Article>() {
		public Article createFromParcel(Parcel in) {
			return new Article(in);
		}

		public Article[] newArray(int size) {
			return new Article[size];
		}
	};
}
