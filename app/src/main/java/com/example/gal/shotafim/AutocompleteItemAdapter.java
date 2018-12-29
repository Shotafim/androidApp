//package com.example.gal.shotafim;
//
//import android.app.Activity;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class AutocompleteItemAdapter extends ArrayAdapter<SearchBarItem> {
//
//    public AutocompleteItemAdapter(Context context,  ArrayList<SearchBarItem> items) {
//        super(context, 0,items);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
////        return super.getView(position, convertView, parent);
//
//        View listItemView = convertView;
//
//        if (convertView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_autocomplete, parent, false);
//        }
//
//        SearchBarItem item = getItem(position);
//        ImageView image_item = listItemView.findViewById(R.id.image_item_autocomplete);
//        TextView text_item = listItemView.findViewById(R.id.nameItem_autocomplete);
//
//        image_item.setImageResource(item.getmImage());
//        text_item.setText(item.getmName());
//
//        return listItemView;
//
//    }
//
//    private Filter categories = new Filter(){
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            FilterResults results = new FilterResults();
//            return null;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//        }
//    };
//
////    @Nullable
////    @Override
////    public SearchBarItem getItem(int position) {
////        return super.getItem(position);
////    }
//
//    //    @Nullable
////    @Override
////    public Fruit getItem(int position) {
////        return items.get(position);
////    }
////
////    @Override
////    public int getCount() {
////        return items.size();
////    }
////
////    @Override
////    public long getItemId(int position) {
////        return position;
////    }
////
////    @NonNull
////    @Override
////    public Filter getFilter() {
////        return fruitFilter;
////    }
////
//    private Filter fruitFilter = new Filter() {
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            SearchBarItem item = (SearchBarItem) resultValue;
//            return item.getmName();
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            if (charSequence != null) {
//                suggestions.clear();
//                for (Fruit fruit : tempItems) {
//                    if (fruit.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
//                        suggestions.add(fruit);
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = suggestions;
//                filterResults.count = suggestions.size();
//                return filterResults;
//            } else {
//                return new FilterResults();
//            }
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            ArrayList<Fruit> tempValues = (ArrayList<Fruit>) filterResults.values;
//            if (filterResults != null && filterResults.count > 0) {
//                clear();
//                for (Fruit fruitObj : tempValues) {
//                    add(fruitObj);
//                    notifyDataSetChanged();
//                }
//            } else {
//                clear();
//                notifyDataSetChanged();
//            }
//        }
//    };
////}
//}
