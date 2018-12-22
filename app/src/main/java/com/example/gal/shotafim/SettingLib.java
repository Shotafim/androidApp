package com.example.gal.shotafim;


/***    This class include all setting of images and other resources, quick getters.
 *      [*] NOTE: All values should be here STATIC.
 *
 * @author markG
 * */

public class SettingLib  {
    static int group_icn_btn = R.drawable.baseline_group_black_48;
    static int person_icn_btn = R.drawable.baseline_person_black_48;
    static int person_add_icn_btn = R.drawable.baseline_person_add_black_48;
    static int transaction_icn_btn = R.drawable.baseline_attach_money_black_48;

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

}
