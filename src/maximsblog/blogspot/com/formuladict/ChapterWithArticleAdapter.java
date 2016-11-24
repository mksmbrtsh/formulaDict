package maximsblog.blogspot.com.formuladict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChapterWithArticleAdapter extends BaseExpandableListAdapter {

	public Context mContext;
	public List<Map<String, Object>> chapterData;
	public List<List<Map<String, Object>>> articleData;
	LayoutInflater mLayoutInflater;

	private BackgroundColorSpan mBackgroundColorSpan;
	private ForegroundColorSpan mForegroundColorSpan;
	private String mQuery = "";
	private ArrayList<Map<String, Object>> mChapterData;
	private ArrayList<ArrayList<Map<String, Object>>> mArticleData;
	public int mCurrentGroup;
	public int mCurrentChild;
	private IEditFormulaDict mEditFormulaDictListener;
	
	public interface IEditFormulaDict {
		public void articleSetupClick(View v, int groupPosition, int childPosition);
		public void chapterSetupClick(View v, int groupPosition);
	}

	public ChapterWithArticleAdapter(Context context,
			ArrayList<Map<String, Object>> chapterData,
			ArrayList<ArrayList<Map<String, Object>>> articleData, IEditFormulaDict editFormulaDictListener) {
		mContext = context;
		mChapterData = chapterData;
		mArticleData = articleData;
		mEditFormulaDictListener = editFormulaDictListener;
		mCurrentGroup = chapterData.size() == 0 ? -1:0;
		if(mCurrentGroup == -1)
			mCurrentChild = -1;
		else {
			mCurrentChild = articleData.get(mCurrentGroup).size() == 0 ? -1:0;
		}
		mBackgroundColorSpan = new BackgroundColorSpan(
				mContext.getResources().getColor(R.color.background_find_text));
		mForegroundColorSpan = new ForegroundColorSpan(
				mContext.getResources().getColor(R.color.formula_text));
		

		this.chapterData = new ArrayList<Map<String, Object>>(
				chapterData.size());
		this.articleData = new ArrayList<List<Map<String, Object>>>(
				articleData.size());
		for (int i1 = 0, cnt1 = chapterData.size(); i1 < cnt1; i1++) {
			Map<String, Object> groupCursor = chapterData.get(i1);
			Map<String, Object> curGroupMap = new HashMap<String, Object>();
			this.chapterData.add(curGroupMap);
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.ChapterID,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.ChapterID));
			curGroupMap.put(DataBaseHelper.Tables.Chapters.Column.Title,
					groupCursor
							.get(DataBaseHelper.Tables.Chapters.Column.Title));
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.LanguageID,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.LanguageID));
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.IndexRow,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.IndexRow));
			curGroupMap.put("State", groupCursor.get("State"));
		}
		for (int i1 = 0, cnt = articleData.size(); i1 < cnt; i1++) {
			List<Map<String, Object>> childrenn = new ArrayList<Map<String, Object>>(
					articleData.get(i1).size());
			for (int i2 = 0, cnt2 = articleData.get(i1).size(); i2 < cnt2; i2++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.putAll(articleData.get(i1).get(i2));
				childrenn.add(m);
			}
			this.articleData.add(childrenn);
		}
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCurrentArticlesID() {
		return (int) articleData.get(mCurrentGroup).get(mCurrentChild)
				.get("ArticleID");
	}

	public int getCurrentChapterID() {
		return (int) chapterData.get(mCurrentGroup).get("ChapterID");
	}

	public void setCurrentPosition(Article a) {
		setCurrentPosition(a.ChapterID, a.ArticlesID);
	}

	public void setCurrentPosition(int chapterID, int articleID) {
		for (int i1 = 0, cnt1 = chapterData.size(); i1 < cnt1; i1++) {
			if ((int) chapterData.get(i1).get(
					DataBaseHelper.Tables.Chapters.Column.ChapterID) == chapterID) {
				for (int i2 = 0, cnt2 = articleData.get(i1).size(); i2 < cnt2; i2++) {
					if ((int) articleData.get(i1).get(i2).get("ArticleID") == articleID) {
						mCurrentChild = i2;
						mCurrentGroup = i1;
						return;
					}
				}
			}
		}
		mCurrentChild = 0;
		mCurrentGroup = 0;
	}

	public int getGroupCount() {
		return chapterData.size();
	}

	public int getChildrenCount(int groupPosition) {
		return articleData.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return chapterData.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return articleData.get(groupPosition).get(childPosition);
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public boolean hasStableIds() {
		return false;
	}

	// Создаем визуальный объект из XML описания
	private View newGroupView(Context context, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.chapter, parent, false);
	}

	public View getGroupView(int groupPosition, boolean b, View view,
			ViewGroup parent) {
		View v = null;
		GroupViewHolder holder;
		if (view != null) {
			// Если существует то, берем текущий объект
			v = view;
			holder = (GroupViewHolder) v.getTag();
		} else {
			// Не существует создаем новый
			v = newGroupView(mContext, parent);
			holder = new GroupViewHolder();
			holder.title = (TextView) v.findViewById(R.id.chapter_title);
			holder.number = (TextView) v.findViewById(R.id.number);
			holder.setup = (ImageButton)v.findViewById(R.id.edit_chapter_btn);
			v.setTag(holder);
		}
		// Отправляем на инициализацию визуальных компонентов
		bindGroupView(groupPosition, holder, v);
		// Отдаем созданное View списку
		return v;
	}

	private void bindGroupView(final int groupPosition, final GroupViewHolder holder,
			View v) {
		holder.title.setText(
				(String) chapterData.get(groupPosition).get("Title"),
				TextView.BufferType.SPANNABLE);

		Spannable str = (Spannable) holder.title.getText();
		int i = holder.title.getText().toString().toLowerCase()
				.indexOf(mQuery.toLowerCase());
		if (i != -1) {
			str.setSpan(mBackgroundColorSpan, i, i + mQuery.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			str.setSpan(mForegroundColorSpan, i, i + mQuery.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if(mEditFormulaDictListener != null) {
			holder.setup.setVisibility(View.VISIBLE);
			holder.setup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mEditFormulaDictListener.chapterSetupClick(holder.setup ,groupPosition);
				}
			});
		} else {
			holder.setup.setVisibility(View.GONE);
		}
	}

	private View newChildView(Context context, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.article, parent, false);
	}

	public View getChildView(int groupPosition, int childPosition, boolean b,
			View view, ViewGroup parent) {
		ChildViewHolder holder;
		View v = null;
		if (view != null) {
			// Если существует то, берем текущий объект
			v = view;
			holder = (ChildViewHolder) v.getTag();
		} else {
			// Не существует создаем новый
			v = newChildView(mContext, parent);
			holder = new ChildViewHolder();
			holder.title = (TextView) v.findViewById(R.id.article_title);
			holder.number = (TextView) v.findViewById(R.id.number);
			holder.selected = v.findViewById(R.id.selected);
			holder.setup = (ImageButton)v.findViewById(R.id.edit_article_btn);
			v.setTag(holder);
		}
		// Отправляем на инициализацию визуальных компонентов
		bindChildView(groupPosition, childPosition, holder, v);
		// Отдаем созданное View списку
		return v;
	}

	private void bindChildView(final int groupPosition, final int childPosition,
			final ChildViewHolder holder, View v) {
		holder.title.setText(
				(String) articleData.get(groupPosition).get(childPosition)
						.get("Title"), TextView.BufferType.SPANNABLE);

		Spannable str = (Spannable) holder.title.getText();
		int i = holder.title.getText().toString().toLowerCase()
				.indexOf(mQuery.toLowerCase());
		if (i != -1) {
			str.setSpan(mBackgroundColorSpan, i, i + mQuery.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			str.setSpan(mForegroundColorSpan, i, i + mQuery.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		holder.number.setText("\u00A7" + String.valueOf(groupPosition + 1)
				+ "." + (childPosition + 1));
		if (groupPosition == mCurrentGroup && childPosition == mCurrentChild) {
			holder.selected.setTag(true);
			holder.selected.setVisibility(View.VISIBLE);
			v.setBackgroundColor(mContext.getResources().getColor(R.color.item_background_with_alpa));
		} else {
			v.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
			holder.selected.setVisibility(View.INVISIBLE);
			holder.selected.setTag(false);
		}
		if(mEditFormulaDictListener != null) {
			holder.setup.setVisibility(View.VISIBLE);
			holder.setup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mEditFormulaDictListener.articleSetupClick(holder.setup, groupPosition, childPosition);
				}
			});
		} else {
			holder.setup.setVisibility(View.GONE);
		}
	}

	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	public void filter(String newText) {
		mQuery = newText;
		List<Map<String, Object>> chapterData = new ArrayList<Map<String, Object>>();
		List<List<Map<String, Object>>> articleData = new ArrayList<List<Map<String, Object>>>();

		for (int i1 = 0, cnt1 = mChapterData.size(); i1 < cnt1; i1++) {
			Map<String, Object> groupCursor = mChapterData.get(i1);
			Map<String, Object> curGroupMap = new HashMap<String, Object>();
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.ChapterID,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.ChapterID));
			curGroupMap.put(DataBaseHelper.Tables.Chapters.Column.Title,
					groupCursor
							.get(DataBaseHelper.Tables.Chapters.Column.Title));
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.LanguageID,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.LanguageID));
			curGroupMap
					.put(DataBaseHelper.Tables.Chapters.Column.IndexRow,
							groupCursor
									.get(DataBaseHelper.Tables.Chapters.Column.IndexRow));
			curGroupMap.put("State", groupCursor.get("State"));
			boolean groupAdded1 = false;
			boolean groupAdded2 = false;
			if (((String) groupCursor
					.get(DataBaseHelper.Tables.Chapters.Column.Title))
					.toLowerCase().contains(newText.toLowerCase())) {
				groupAdded1 = true;
			}
			List<Map<String, Object>> childrenn = new ArrayList<Map<String, Object>>(
					mArticleData.get(i1).size());
			for (int i2 = 0, cnt2 = mArticleData.get(i1).size(); i2 < cnt2; i2++) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.putAll(mArticleData.get(i1).get(i2));
				if (groupAdded1) {
					childrenn.add(m);
				} else if (((String) mArticleData.get(i1).get(i2).get("Title"))
						.toLowerCase().contains(newText.toLowerCase())
						|| ((String) mArticleData.get(i1).get(i2)
								.get("Description")).toLowerCase().contains(
								newText.toLowerCase())|| (mArticleData.get(i1).get(i2)
										.get("Comment"))!=null && ((String) mArticleData.get(i1).get(i2)
										.get("Comment")).toLowerCase().contains(
												newText.toLowerCase())) {
					childrenn.add(m);
					if (!groupAdded2) {
						groupAdded2 = true;
					}
				}
			}
			if (groupAdded1 || groupAdded2) {
				chapterData.add(curGroupMap);
				articleData.add(childrenn);
			}
		}
		this.chapterData = chapterData;
		this.articleData = articleData;

		this.notifyDataSetChanged();
	}

	public String getFilter() {
		return mQuery;
	}

	private static class GroupViewHolder {
		public TextView title;
		public TextView number;
		public ImageButton setup;
	}

	public static class ChildViewHolder {
		public TextView title;
		public TextView number;
		public View selected;
		public ImageButton setup;
	}

	public void setEditFormulaDictListener(IEditFormulaDict editFormulaDictListener) {
		mEditFormulaDictListener = editFormulaDictListener;
	}
}