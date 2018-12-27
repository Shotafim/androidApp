package com.example.gal.shotafim;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutocompleteItemAdapter extends ArrayAdapter<SearchBarItem> {

    private List<SearchBarItem> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<SearchBarItem> dataListAllItems;

    public AutocompleteItemAdapter(Context context, int resource, List<SearchBarItem> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }


    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.nameItem_autocomplete);
        strName.setText(getItem(position).getmName());

        ImageView imageView = view.findViewById(R.id.image_item_autocomplete);
        imageView.setImageResource(getItem(position).getmImage());


        return view;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public SearchBarItem getItem(int position) {
        Log.d("CustomListAdapter", ""+dataList.get(position));
        return dataList.get(position);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<SearchBarItem>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<SearchBarItem> matchValues = new ArrayList<SearchBarItem>();

                for (SearchBarItem dataItem : dataListAllItems) {
                    if (dataItem.getmName().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<SearchBarItem>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
