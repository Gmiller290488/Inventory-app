package com.ctingcter.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ctingcter.inventory.data.ProductContract;

/**
 * Created by CTingCTer on 30/04/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in inventory_list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false);
    }


    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productTextView = (TextView) view.findViewById(R.id.id_product_TV);
        TextView quantityTextView = (TextView) view.findViewById(R.id.id_quantity_TV);
        TextView priceTextView = (TextView) view.findViewById(R.id.id_price_TV);


        // Find the columns of pet attributes that we're interested in
        int productColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);

        // Read the pet attributes from the Cursor for the current pet
        String productName = cursor.getString(productColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        String price = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes for the current pet
        productTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(price);

    }
}
