package com.ctingcter.inventory.data;

/**
 * Created by CTingCTer on 28/04/2017.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *  API Contract for the Inventory App
 */
public class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // five it an empty constructor
    private ProductContract() {}
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.ctingcter.inventory.data";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content:// + CONTENT_AUTHORITY");

    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

        // The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

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

    }
}
