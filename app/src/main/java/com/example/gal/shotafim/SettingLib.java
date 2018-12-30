package com.example.gal.shotafim;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/***    This class include all setting of images and other resources, quick getters.
 *      [*] NOTE: All values should be here STATIC.
 *
 * @author markG
 * */

public class SettingLib {
    static int group_icn_btn = R.drawable.baseline_group_black_48;
    static int person_icn_btn = R.drawable.baseline_person_black_48;
    static int person_add_icn_btn = R.drawable.baseline_person_add_black_48;
    static int transaction_icn_btn = R.drawable.baseline_attach_money_black_48;

    static int icon_list_tv = R.drawable.icons8_tv_40;
    static int icon_list_arnona = R.drawable.icons8_arnona_40;
    static int icon_list_water = R.drawable.icons8_water_48;
    static int icon_list_electricity = R.drawable.icon8_light_40;
    static int icon_list_internet = R.drawable.icons8_internet_48;

    /**
     *  Categories for Transaction, Payment
     **/
    static final String TV = "TV";
    static final String INTERNET = "Internet";
    static final String ARNONA = "Arnona";
    static final String HOUSE_PAYMENT = "House vaad";
    static final String ELECTRICITY = "Electricity";
    static final String[] CATEGORIES_TRANSACTION_PAYMENT = {TV, INTERNET, ARNONA, HOUSE_PAYMENT, ELECTRICITY};
    static final int TRANSFER = 0;
    static final int PAYMENT = 1;
    static final String TRANSFER_STR = "TRANSFER";
    static final String PAYMENT_STR = "PAYMENT";






    /**
     * Firebase
     * */
    // Their is a typo in Firebase name branch. should be Transaction not Transcation.
    static final String FB_TRANSACTION = "Transaction";  //
    static final String FB_PAYMENTS = "Payments";

    /**
     *  User Info
     * */
    static String mAuthUserName;
    public static void setmAuthUserName(String name){ mAuthUserName = name;}
    public static String getmAuthUserName(){return mAuthUserName;}


//    /***
//     *  ArrayList<SearchBarItem> is for each element in the AutoCompleteView in the Payment Window
//     */
//    public static ArrayList<SearchBarItem> getSearchBarAutoCompleteItems(){
//        ArrayList<SearchBarItem> searchBar_items = new ArrayList<>();
//        searchBar_items.add(new SearchBarItem("TV", group_icn_btn));
//        return searchBar_items;
//    }

    /**
     *  ATTENTION! this method delete all data from given child in the Firebase. Use wisely
     * */
    public static void DELETE_FIREBASE_CHILD_DATA(String path){
        DatabaseReference firebaseDataRef = FirebaseDatabase.getInstance().getReference();
        firebaseDataRef.child(path).removeValue();
    }


}
