package com.ctingcter.inventory.data;

/**
 * Created by CTingCTer on 28/04/2017.
 */


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  API Contract for the Inventory App
 */
public class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // five it an empty constructor
    private ProductContract() {}

    /* The CONTENT_AUTHORITY is often the package name of the app to ensure it
           is unique */
    public static final String CONTENT_AUTHORITY = "com.ctingcter.inventory";

    /*
     CONTENT_AUTHORITY is used to create the base of all URIs which the app will
     use to conract the content provider */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        // The content URI to access the product data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        // Name of databse table for products
        public final static String TABLE_NAME = "products";



        /**
         * Unique ID number for the product
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="product";

        /**
         * Quantity of product in stock
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY="quantity";

        /**
         * Price of product
         * Type: FLOAT
         */
        public final static String COLUMN_PRICE="price";

        /**
         * Picture of product
         * Type: STRING
         */
        public final static String COLUMN_PICTURE_ID="picture";

        /**
         * Supplier of product
         * Type: STRING
         */
        public final static String COLUMN_SUPPLIER ="supplier";


        // Returns where or not the price given is valid
        public static boolean isValidPrice(String price) {
            final String regExp = "[0-9]+([,.][0-9]{2})?";
            final Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(price);
            if (matcher.matches()) {
                return true;
            }
            return false;
        }
    }
}
