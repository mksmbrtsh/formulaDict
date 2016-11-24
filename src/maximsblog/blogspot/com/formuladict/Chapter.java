package maximsblog.blogspot.com.formuladict;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Chapter implements Parcelable {
	public int SubjectID;
	public int ChapterID;
	public String Title;
	public int LanguageID;
	public int Order;

	public Chapter(int ChapterID, String Title, int LanguageID, int Order, int SubjectID ) {
		this.ChapterID = ChapterID;
		this.Title = Title;
		this.LanguageID = LanguageID;
		this.Order = Order;
		this.SubjectID = SubjectID;
	}
	public Chapter(Parcel in) {
		this.ChapterID = in.readInt();
		this.Title = in.readString();
		this.LanguageID = in.readInt();
		this.Order = in.readInt();
		this.SubjectID = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ChapterID);
		dest.writeString(Title);
		dest.writeInt(LanguageID);
		dest.writeInt(Order);
		dest.writeInt(SubjectID);
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