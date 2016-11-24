package maximsblog.blogspot.com.formuladict;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import maximsblog.blogspot.com.formuladict.ChapterWithArticleAdapter.IEditFormulaDict;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.melnykov.fab.FloatingActionButton;

public class MainListFragment extends BaseFragment implements
		OnChildClickListener, OnClickListener, LoaderCallbacks<Result>,
		IEditFormulaDict, OnActionItemClickListener {

	private ExpandableListView mList;
	private ChapterWithArticleAdapter mAdapter;
	private int mSubjectID;
	
	private View mProgressLoader;
	private RelativeLayout mLayout;
	private String mQuery;
	private InterstitialAd mInterstitial;
	private FloatingActionButton mAddButton;

	private int groupPositionQDialog;
	private int childPositionQDialog;
	private TextView mEmptyText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSubjectID = getArguments().getInt("id");
		mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_main,
				container, false);
		super.decorTop(mLayout);
		mEmptyText = (TextView) mLayout.findViewById(R.id.empty_list);
		mEmptyText.setVisibility(View.GONE);
		setEmptyText(((MainActivity)getActivity()).getPurchased());
		mAddButton = (FloatingActionButton) mLayout.findViewById(R.id.add);
		mAddButton.setOnClickListener(this);
		mProgressLoader = mLayout.findViewById(R.id.progress_loader);
		mList = (ExpandableListView) mLayout.findViewById(R.id.list);
		mList.setOnChildClickListener(this);
		mList.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				saveGroupState(groupPosition, false);
			}
		});
		mList.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				saveGroupState(groupPosition, true);
			}
		});
		return mLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState == null) {
			Bundle b = new Bundle();
			b.putInt("subjectID", mSubjectID);
			getLoaderManager().initLoader(1, b, this);
		}
		decorTop(mLayout);
		if (!((MainActivity) getActivity()).getPurchased()) {
			// Create the interstitial.
			mInterstitial = new InterstitialAd(getActivity());
			mInterstitial.setAdUnitId("key");

			// Create ad request.
			AdRequest adRequest = new AdRequest.Builder().build();

			// Begin loading your interstitial.
			mInterstitial.loadAd(adRequest);
			// Begin listening to interstitial & show ads.
			mInterstitial.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {

				}

				@Override
				public void onAdClosed() {
					AdRequest adRequest = new AdRequest.Builder()
							.build();
					// Begin loading your interstitial.
					mInterstitial.loadAd(adRequest);
				}
			});
		}
	};

	private void setLoading(boolean loading) {
		if (loading) {
			mProgressLoader.setVisibility(View.VISIBLE);
			mList.setVisibility(View.INVISIBLE);
			mEmptyText.setVisibility(View.GONE);
		} else {
			mProgressLoader.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
			if (mAdapter.chapterData.size() == 0) {
				mEmptyText.setVisibility(View.VISIBLE);
			} else
				mEmptyText.setVisibility(View.GONE);
		}
	}

	private void setCurrentPositions() {
		Cursor c = getActivity().getContentResolver().query(
				DataBaseProvider.CONTENT_URI_SelectedArticles, null,
				"SubjectID = " + mSubjectID, null, null);
		int indexChapterID = c.getColumnIndex("ChapterID");
		int indexArticleID = c.getColumnIndex("ArticlesID");
		if (c.moveToFirst()) {
			int chapterID = c.getInt(indexChapterID);
			int articleID = c.getInt(indexArticleID);
			mAdapter.setCurrentPosition(chapterID, articleID);
			mList.invalidateViews();
		} else {
			mAdapter.mCurrentGroup = mAdapter.chapterData.size() == 0 ? -1 : 0;
			if (mAdapter.mCurrentGroup == -1)
				mAdapter.mCurrentChild = -1;
			else {
				mAdapter.mCurrentChild = mAdapter.articleData.get(
						mAdapter.mCurrentGroup).size() == 0 ? -1 : 0;
			}
		}
		c.close();
	}

	private void saveGroupState(int groupPosition, boolean b) {
		ContentValues value = new ContentValues();
		value.put("ChapterID", (int) mAdapter.chapterData.get(groupPosition)
				.get(DataBaseHelper.Tables.Chapters.Column.ChapterID));
		value.put("State", b ? 1 : 0);
		getActivity().getContentResolver().insert(
				DataBaseProvider.CONTENT_URI_OpenedChapters, value);
		mAdapter.notifyDataSetChanged();
	}

	private void setExpandedArticles() {
		for (int i1 = 0, cnt1 = mAdapter.chapterData.size(); i1 < cnt1; i1++) {
			if ((int) mAdapter.chapterData.get(i1).get("State") == 1) {
				mList.expandGroup(i1);
			}
		}
	}

	private void setSelectArticle() {
		if (mAdapter.mCurrentGroup == -1 || mAdapter.mCurrentChild == -1)
			return;
		if (mAdapter.mCurrentGroup < mAdapter.getGroupCount()) {
			mList.expandGroup(mAdapter.mCurrentGroup);
			saveGroupState(mAdapter.mCurrentGroup, true);
		}
		mList.invalidateViews();
		mList.setSelectedChild(mAdapter.mCurrentGroup, mAdapter.mCurrentChild,
				true);
		if (((MainActivity) getActivity()).getTablet()) {
			handler.sendEmptyMessage(2);
		}
	}

	private Handler handler = new Handler() { // handler for commiting fragment
		// after data is loaded
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 2 && getActivity() != null) {
				FragmentManager fm = getFragmentManager();
				FragmentViewFormuls fv = (FragmentViewFormuls) fm
						.findFragmentByTag("mainViewFragTable");

				if (mAdapter.articleData.size() > 0
						&& mAdapter.articleData.get(mAdapter.mCurrentGroup)
								.size() > 0) {
					Map<String, Object> c = mAdapter.articleData.get(
							mAdapter.mCurrentGroup).get(mAdapter.mCurrentChild);
					Intent intent = new Intent(getActivity(),
							ViewFormulaActivity.class);
					Article a = new Article((Integer) c.get("ArticleID"),
							(Integer) c.get("ChapterID"),
							(String) c.get("Title"),
							(String) c.get("Description"),
							(String) c.get("Comment"),
							(Integer) c.get("LanguageID"),
							(Integer) c.get("IndexRow"),
							(String) c.get("ChapterTitle"),
							(int) c.get("ChapterNumber"));
					intent.putExtra("a", a);
					intent.putExtra("s", mSubjectID);
					intent.putExtra("f", mAdapter.getFilter());
					if (fv != null)
						fv.setIntent(intent);
				} else {
					if (fv != null)
						fv.setEmpty();
					((MainActivity) getActivity()).setHelpArticleVisible(false);
				}
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(mSubjectID);
	}

	public void decorTop(View rootView) {
		int paddingTop = ((MainActivity) getActivity()).getStatusBarHeight();
		TypedValue tv = new TypedValue();
		getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true);
		paddingTop += TypedValue.complexToDimensionPixelSize(tv.data,
				getResources().getDisplayMetrics());
		rootView.setPadding(0, paddingTop, 0, 0);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Map<String, Object> c = mAdapter.articleData.get(groupPosition).get(
				childPosition);
		Intent intent = new Intent(getActivity(), ViewFormulaActivity.class);
		Article a = new Article((Integer) c.get("ArticleID"),
				(Integer) c.get("ChapterID"), (String) c.get("Title"),
				(String) c.get("Description"), (String) c.get("Comment"),
				(Integer) c.get("LanguageID"), (Integer) c.get("IndexRow"),
				(String) c.get("ChapterTitle"), (int) c.get("ChapterNumber"));
		intent.putExtra("a", a);
		intent.putExtra("s", mSubjectID);
		intent.putExtra("f", mAdapter.getFilter());
		intent.putExtra("p", ((MainActivity)getActivity()).getPurchased());
		if (((MainActivity) getActivity()).getTablet()) {
			FragmentManager fm = getFragmentManager();
			FragmentViewFormuls fv = (FragmentViewFormuls) fm
					.findFragmentByTag("mainViewFragTable");
			fv.setIntent(intent);
		} else {
			startActivityForResult(intent, 1);
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
		mAdapter.mCurrentGroup = groupPosition;
		mAdapter.mCurrentChild = childPosition;
		setSelectedChild();
		savePositions();
		if (!((MainActivity) getActivity()).getPurchased()) {
			mInterstitial.show();
		}
		return false;
	}

	private void setSelectedChild() {
		// save index and top position
		int index = mList.getFirstVisiblePosition();
		View vv = mList.getChildAt(0);
		int top = (vv == null) ? 0 : (vv.getTop() - mList.getPaddingTop());
		mList.invalidateViews();
		mList.setSelectedChild(mAdapter.mCurrentGroup, mAdapter.mCurrentChild,
				true);
		mList.setSelectionFromTop(index, top);
	}

	private void savePositions() {
		ContentValues values = new ContentValues();
		values.put("SubjectID", mSubjectID);
		values.put("ChapterID", mAdapter.getCurrentChapterID());
		values.put("ArticlesID", mAdapter.getCurrentArticlesID());
		getActivity().getContentResolver().insert(
				DataBaseProvider.CONTENT_URI_SelectedArticles, values);
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2 && resultCode == Activity.RESULT_CANCELED)
			return;
		if (requestCode == 3) {
			if (data.getIntExtra("id", -1) == -1) {
				ContentValues values = new ContentValues();
				values.put("Title", data.getStringExtra("name"));
				values.put("LanguageID", Integer.valueOf(App.getLanguageID()));
				values.put("SubjectID", App.CUSTOM_FORMULS);
				values.put("IndexRow", mAdapter.chapterData.size() + 1);
				getActivity().getContentResolver().insert(
						DataBaseProvider.CONTENT_URI_Chapters, values);
			} else {
				ContentValues values = new ContentValues();
				values.put("ChapterID", data.getIntExtra("id", -1));
				values.put("Title", data.getStringExtra("name"));
				values.put("LanguageID", Integer.valueOf(App.getLanguageID()));
				values.put("SubjectID", App.CUSTOM_FORMULS);
				values.put("IndexRow", mAdapter.chapterData.size());
				getActivity().getContentResolver().update(
						DataBaseProvider.CONTENT_URI_Chapters,
						values,
						DataBaseHelper.Tables.Chapters.Column.ChapterID + "=?",
						new String[] { String.valueOf(data
								.getIntExtra("id", -1)) });
			}
			Bundle b = new Bundle();
			b.putInt("subjectID", mSubjectID);
			getLoaderManager().restartLoader(1, b, this);
			return;
		}

		if (requestCode == 2) {
			if (data != null) {
				Article a = data.getParcelableExtra("a");
				if (a.ArticlesID != -1) {
					if (mAdapter != null
							&& (int) mAdapter.articleData
									.get(mAdapter.mCurrentGroup)
									.get(mAdapter.mCurrentChild)
									.get("ArticleID") != a.ArticlesID) {
						mAdapter.setCurrentPosition(a);
						setSelectArticle();
					}
					savePositions();
				}
			} else {
				getActivity().getContentResolver().delete(
						DataBaseProvider.CONTENT_URI_SelectedArticles,
						"ArticlesID = ?",
						new String[] { String.valueOf(mAdapter
								.getCurrentArticlesID()) });
			}
			Bundle b = new Bundle();
			b.putInt("subjectID", mSubjectID);
			getLoaderManager().restartLoader(1, b, this);
		} else {
			Article a = data.getParcelableExtra("a");
			if (mAdapter != null
					&& (int) mAdapter.articleData.get(mAdapter.mCurrentGroup)
							.get(mAdapter.mCurrentChild).get("ArticleID") != a.ArticlesID) {
				mAdapter.setCurrentPosition(a);
				setSelectArticle();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.add) {
			ChapterDialogEditorFragment newChapterDialog = new ChapterDialogEditorFragment();
			newChapterDialog.setTargetFragment(this, 3);
			Bundle b = new Bundle();
			b.putInt("id", -1);
			newChapterDialog.setArguments(b);
			newChapterDialog.show(getFragmentManager(), "newChapter");
		}
	}

	public void setFilter(String query) {
		if (mAdapter != null
				&& ((mAdapter.getFilter() != null && !mAdapter.getFilter()
						.equals(query)) || (mAdapter.getFilter() == null && query != null))) {
			mAdapter.filter(query);
			setCurrentPositions();
			setSelectArticle();
		} else {
			mQuery = query;
		}
	}

	public void NextArticle() {
		int child = mAdapter.mCurrentChild + 1;
		int group = mAdapter.mCurrentGroup;
		if (child >= mAdapter.getChildrenCount(mAdapter.mCurrentGroup)) {
			do {
				group++;
				if (group >= mAdapter.getGroupCount()) {
					group = 0;
				}
			} while (mAdapter.getChildrenCount(group) <= 0);
			child = 0;
		}
		mAdapter.mCurrentChild = child;
		mAdapter.mCurrentGroup = group;
		mList.expandGroup(mAdapter.mCurrentGroup);
		mList.smoothScrollToPosition(mList
				.getFlatListPosition(ExpandableListView
						.getPackedPositionForChild(mAdapter.mCurrentGroup,
								mAdapter.mCurrentChild)));
		savePositions();
	}

	public void PrevArticle() {
		int child = mAdapter.mCurrentChild - 1;
		int group = mAdapter.mCurrentGroup;
		if (child < 0) {
			do {
				group--;
				if (group < 0) {
					group = mAdapter.getGroupCount() - 1;
				}
			} while (mAdapter.getChildrenCount(group) <= 0);
			child = mAdapter.getChildrenCount(group) - 1;
		}
		mAdapter.mCurrentChild = child;
		mAdapter.mCurrentGroup = group;
		mList.expandGroup(mAdapter.mCurrentGroup);
		mList.smoothScrollToPosition(mList
				.getFlatListPosition(ExpandableListView
						.getPackedPositionForChild(mAdapter.mCurrentGroup,
								mAdapter.mCurrentChild)));
		savePositions();
	}

	@Override
	public Loader<Result> onCreateLoader(int id, Bundle args) {
		int subjectID = args.getInt("subjectID");
		setLoading(true);
		return new ListLoader(getActivity(), subjectID);
	}

	@Override
	public void onDestroyView() {
		if (mAdapter != null)
			mAdapter.mContext = null;
		getLoaderManager().destroyLoader(1);
		super.onDestroyView();

	}

	@Override
	public void onLoadFinished(Loader<Result> arg0, Result result) {
		ArrayList<Map<String, Object>> chapterData = result.chapterData;;
		ArrayList<ArrayList<Map<String, Object>>> articleData = result.articleData;;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		boolean editable = prefs.getBoolean("edit_mod", true);
		ChapterWithArticleAdapter.IEditFormulaDict editFormulaDict = (mSubjectID == App.CUSTOM_FORMULS && editable) ? this
				: null;
		if (editFormulaDict != null) {
			mAddButton.setVisibility(View.VISIBLE);
		} else
			mAddButton.setVisibility(View.GONE);

		mAdapter = new ChapterWithArticleAdapter(getActivity(), chapterData,
				articleData, editFormulaDict);
		mList.setAdapter(mAdapter);
		setLoading(false);
		if (mQuery != null) {
			mAdapter.filter(mQuery);
		}
		setExpandedArticles();
		setCurrentPositions();
		setSelectArticle();
		getActivity().invalidateOptionsMenu();

	}

	@Override
	public void onLoaderReset(Loader<Result> arg0) {
		// TODO Auto-generated method stub

	}

	public void setSubject(int id) {
		mSubjectID = id;
		Bundle b = new Bundle();
		b.putInt("subjectID", mSubjectID);
		if (getLoaderManager().getLoader(1) != null)
			getLoaderManager().restartLoader(1, b, this);
		else
			getLoaderManager().initLoader(1, b, this);
	}

	@Override
	public void articleSetupClick(View v, int groupPosition, int childPosition) {
		groupPositionQDialog = groupPosition;
		childPositionQDialog = childPosition;
		final QuickAction quickAction = new QuickAction(getActivity());
		quickAction.addActionItem(new ActionItem(R.id.article_edit,
				getResources().getDrawable(R.drawable.ic_edit_black)));
		quickAction.addActionItem(new ActionItem(R.id.article_delete,
				getResources().getDrawable(R.drawable.ic_del_black)));
		quickAction.setOnActionItemClickListener(this);
		quickAction.show(v);
	};

	@Override
	public void chapterSetupClick(View v, int groupPosition) {
		groupPositionQDialog = groupPosition;
		final QuickAction quickAction = new QuickAction(getActivity());
		quickAction.addActionItem(new ActionItem(R.id.chapter_edit,
				getResources().getDrawable(R.drawable.ic_edit_black)));
		if (mAdapter.articleData.get(groupPosition).size() == 0)
			quickAction.addActionItem(new ActionItem(R.id.chapter_delete,
					getResources().getDrawable(R.drawable.ic_del_black)));
		quickAction.addActionItem(new ActionItem(R.id.chapter_add,
				getResources().getDrawable(R.drawable.ic_plus_black)));
		quickAction.setOnActionItemClickListener(this);
		quickAction.show(v);
	};

	@Override
	public void onItemClick(QuickAction source, int pos, int actionId,
			int groupId) {
		switch (actionId) {
		case R.id.article_edit:
			articleEditClick(groupPositionQDialog, childPositionQDialog);
			break;
		case R.id.article_delete:
			articleDelClick(groupPositionQDialog, childPositionQDialog);
			break;
		case R.id.chapter_edit:
			chapterEditClick(groupPositionQDialog);
			break;
		case R.id.chapter_delete:
			chapterDelClick(groupPositionQDialog);
			break;
		case R.id.chapter_add:
			articleAddClick(groupPositionQDialog);
			break;
		default:
			break;
		}
	}

	public void articleEditClick(int currentGroup, int currentChild) {
		Map<String, Object> c = mAdapter.articleData.get(currentGroup).get(
				currentChild);
		Intent intent = new Intent(getActivity(), EditorActivity.class);
		Article a = new Article((Integer) c.get("ArticleID"),
				(Integer) c.get("ChapterID"),
				((String) c.get("Title")).toLowerCase(),
				(String) c.get("Description"), (String) c.get("Comment"),
				(Integer) c.get("LanguageID"), (Integer) c.get("IndexRow"),
				(String) c.get("ChapterTitle"), (int) c.get("ChapterNumber"));
		intent.putExtra("a", a);
		startActivityForResult(intent, 2);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	public void articleAddClick(int currentGroup) {
		Intent intent = new Intent(getActivity(), EditorActivity.class);
		Article a = new Article(-1, (int) mAdapter.chapterData
				.get(currentGroup).get("ChapterID"), "", "", "",
				Integer.valueOf(App.getLanguageID()),
				mAdapter.getChildrenCount(currentGroup),
				(String) mAdapter.chapterData.get(currentGroup).get("Title"),
				(int) mAdapter.chapterData.get(currentGroup).get("IndexRow"));
		intent.putExtra("a", a);
		startActivityForResult(intent, 2);
	}

	public void articleDelClick(int groupPosition, int childPosition) {
		int articlesID = (int) mAdapter.articleData.get(groupPosition)
				.get(childPosition).get("ArticleID");
		getActivity().getContentResolver().delete(
				DataBaseProvider.CONTENT_URI_Articles, "ArticlesID = ?",
				new String[] { String.valueOf(articlesID) });
		Bundle b = new Bundle();
		b.putInt("subjectID", mSubjectID);
		getLoaderManager().restartLoader(1, b, this);
	}

	public void chapterDelClick(int currentGroup) {
		int id = (int) mAdapter.chapterData.get(currentGroup).get(
				DataBaseHelper.Tables.Chapters.Column.ChapterID);

		Cursor c = getActivity().getContentResolver().query(
				DataBaseProvider.CONTENT_URI_Articles, null,
				" AND a.ChapterID = ?", new String[] { String.valueOf(id) },
				null);
		boolean delete_accept = c.getCount() == 0;
		c.close();
		if (delete_accept) {
			getActivity().getContentResolver().delete(
					DataBaseProvider.CONTENT_URI_Chapters, "ChapterID = ?",
					new String[] { String.valueOf(id) });
			Bundle b = new Bundle();
			b.putInt("subjectID", mSubjectID);
			getLoaderManager().restartLoader(1, b, this);
		} else {
			Toast.makeText(getActivity(),
					getString(R.string.err_del_not_empty_group),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void chapterEditClick(int groupPosition) {
		ChapterDialogEditorFragment editorChapterDialog = new ChapterDialogEditorFragment();
		editorChapterDialog.setTargetFragment(this, 3);
		Bundle args = new Bundle();
		args.putString(
				"title",
				(String) mAdapter.chapterData.get(groupPosition).get(
						DataBaseHelper.Tables.Chapters.Column.Title));
		args.putInt(
				"id",
				(int) mAdapter.chapterData.get(groupPosition).get(
						DataBaseHelper.Tables.Chapters.Column.ChapterID));
		editorChapterDialog.setArguments(args);
		editorChapterDialog.show(getFragmentManager(), "newChapter");
	}

	public void refreshListMode(boolean editable) {
		mAdapter.setEditFormulaDictListener(editable ? this : null);
		mList.invalidateViews();
		if (editable) {
			mAddButton.setVisibility(View.VISIBLE);
		} else
			mAddButton.setVisibility(View.GONE);
	}

	public void setEmptyText(boolean purchased) {
		mEmptyText.setText(purchased ? getString(R.string.add_new_formuls_in_edit_mod) : getString(R.string.please_get_full_version));
	}

}
