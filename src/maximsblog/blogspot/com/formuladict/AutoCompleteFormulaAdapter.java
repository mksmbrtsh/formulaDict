package maximsblog.blogspot.com.formuladict;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;


/**
 * Created by mksmbrtsh on 04.08.15.
 */
public class AutoCompleteFormulaAdapter extends BaseAdapter implements Filterable {

    private final LayoutInflater mLayoutInflater;
    private ArrayList<String> mList;
    private ArrayList<String> mListOriginalValues;
    private TeXFormula mFormula;
    private Context mContext;

    public AutoCompleteFormulaAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
        mFormula = new TeXFormula();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = null;
        ViewHolder holder;
        if(view == null) {
            v = mLayoutInflater.inflate(R.layout.autocomplete_text, viewGroup, false);
            holder = new ViewHolder();
            holder.f = (ImageView)v.findViewById(R.id.formula);
            holder.name = (TextView) v.findViewById(R.id.hint);
            holder.help = (Button) v.findViewById(R.id.help);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ViewHolder) v.getTag();
        }
        holder.name.setText(mList.get(i));
        holder.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://en.wikibooks.org/wiki/Special:Search?search=%s&prefix=LaTeX%%2F&fulltext=Search+this+book&fulltext=Search", URLEncoder.encode(mList.get(i), "utf-8"))));
                    mContext.startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        return v;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mList = (ArrayList<String>) results.values; // has

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                // results of a
                // filtering
                // operation in
                // values
                // List<String> FilteredArrList = new ArrayList<String>();
                List<String> FilteredArrList = new ArrayList<String>();

                if (mListOriginalValues == null) {
                    mListOriginalValues = new ArrayList<String>(mList); // saves
                }

                /********
                 *
                 * If constraint(CharSequence that is received) is null returns
                 * the mListOriginalValues(Original) values else does the Filtering
                 * and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mListOriginalValues.size();
                    results.values = mListOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mListOriginalValues.size(); i++) {
                        String data = mListOriginalValues.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    private static class ViewHolder {
        public TextView name;
        public Button help;
        public ImageView f;
    }


}
