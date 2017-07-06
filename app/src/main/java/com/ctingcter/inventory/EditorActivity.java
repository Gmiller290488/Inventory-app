package com.ctingcter.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.ctingcter.inventory.data.ProductContract;

/**
 * Created by CTingCTer on 30/04/2017.
 */

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Button mDeleteButton;
    private EditText mProductEditText;
    private EditText mQuantityeditText;
    private EditText mSupplierEditText;
    private EditText mPriceEditText;
    private ImageView mProductImageView;
    private View mOrderMoreTV;
    private Uri mCurrentProductUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

         mOrderMoreTV = (View) findViewById(R.id.order_more_TV);
        // All of the declarations for the EditTexts
        mProductEditText = (EditText) findViewById(R.id.id_product_EV);
        mPriceEditText = (EditText) findViewById(R.id.id_price_ET);
        mQuantityeditText = (EditText) findViewById(R.id.id_quantity_ET);
        mSupplierEditText = (EditText) findViewById(R.id.id_supplier_ET);

        View ordering = (View) findViewById(R.id.order_more_TV);

        Button save = (Button) findViewById(R.id.id_save_btn);
        save.setOnClickListener(this);

        Button delete = (Button) findViewById(R.id.id_delete_btn);
        delete.setOnClickListener(this);

        Button decrementOne = (Button) findViewById(R.id.id_decrement_one_btn);
        decrementOne.setOnClickListener(this);

        Button decrementTen = (Button) findViewById(R.id.id_decrement_ten_btn);
        decrementTen.setOnClickListener(this);

        Button incrementOne = (Button) findViewById(R.id.id_increment_one_btn);
        incrementOne.setOnClickListener(this);

        Button incrementTen = (Button) findViewById(R.id.id_increment_ten_btn);
        decrementOne.setOnClickListener(this);

        Button order = (Button) findViewById(R.id.id_order_more_btn);
        order.setOnClickListener(this);

        Button picture = (Button) findViewById(R.id.id_picture_btn);
        picture.setOnClickListener(this);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            delete.setVisibility(View.GONE);
            ordering.setVisibility(View.GONE);
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

    }


    private void insertProduct() {
        String titleString = mProductEditText.getText().toString().trim();
        if (titleString.matches("")){
            Toast.makeText(this, "Please enter the product name", Toast.LENGTH_SHORT).show();
            return;
        }
          String quantityString = mQuantityeditText.getText().toString().trim();
        if (quantityString.matches("")){
            quantityString = "0";
        }
        int quantity = Integer.parseInt(quantityString);


        String supplierString = mSupplierEditText.getText().toString().trim();
        if (supplierString.matches("")){
            Toast.makeText(this, "Please enter the supplier name", Toast.LENGTH_SHORT).show();
            return;
        }

        String priceString = mPriceEditText.getText().toString().trim();
        if (priceString.matches("")){
            Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();
            return;
        }
        int price = Integer.parseInt(priceString);

         //Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, titleString);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, supplierString);

       // Insert new product into the provider and return the content URI for the new Product
        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

       //  Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
         //    If the newUri is null, then there was an error with insertion.
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
       //      Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
       }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.id_save_btn:
                //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                insertProduct();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRICE,
                ProductContract.ProductEntry.COLUMN_SUPPLIER,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PICTURE_ID };

        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,                       // Parent activity context
                mCurrentProductUri,                         // Provider content URI to query
                projection,                                 // Columns to include in cursor
                null,                                       // No selection cause
                null,                                       // No selection arguments
                null);                                      // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER);
            //int pictureColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PICTURE_ID);

            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            // Need to implmenet picture

            mProductEditText.setText(name);
            mPriceEditText.setText(price);
            mQuantityeditText.setText(quantity);
            mSupplierEditText.setText(supplier);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductEditText.setText("");
        mPriceEditText.setText("");
        mQuantityeditText.setText("");
        mSupplierEditText.setText("");
    }

}





