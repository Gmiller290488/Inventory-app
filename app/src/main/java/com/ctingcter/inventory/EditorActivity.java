package com.ctingcter.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctingcter.inventory.data.ProductContract;
import com.ctingcter.inventory.data.ProductDbHelper;

/**
 * Created by CTingCTer on 30/04/2017.
 */

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private Uri mCurrentProducturi;
    private EditText mProductEditText;
    private EditText mQuantityeditText;
    private EditText mSupplierEditText;
    private EditText mPriceEditText;
    private ImageView mProductImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        // All of the declarations for the EditTexts
        mProductEditText = (EditText) findViewById(R.id.id_product_EV);
        mPriceEditText = (EditText) findViewById(R.id.id_price_ET);
        mQuantityeditText = (EditText) findViewById(R.id.id_quantity_ET);
        mSupplierEditText = (EditText) findViewById(R.id.id_supplier_ET);

        Button save = (Button) findViewById(R.id.id_save_btn);
        save.setOnClickListener(this);
        // Examine the intent that launched the activity
        // Figure if it's editing product or adding a new one
        Intent intent = getIntent();
        mCurrentProducturi = intent.getData();

        // If the intent DOESN'T contain a product URI, then we are adding a NEW product
        if (mCurrentProducturi == null) {
            // New product so changed title
            setTitle(getString(R.string.editor_activity_title_new_product));


        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));
        }
    }


    private void insertProduct() {
        String titleString = mProductEditText.getText().toString().trim();
        if (titleString.matches("")){
            Toast.makeText(this, "Please enter the product name", Toast.LENGTH_SHORT).show();
            return;
        }
        String supplierString = mSupplierEditText.getText().toString().trim();
        if (supplierString.matches("")){
            Toast.makeText(this, "Please enter the supplier name", Toast.LENGTH_SHORT).show();
            return;
        }
        String quantityString = mQuantityeditText.getText().toString().trim();
        if (quantityString.matches("")){
            quantityString = "0";
        }
        int quantity = Integer.parseInt(quantityString);
        String priceString = mPriceEditText.getText().toString().trim();
        if (titleString.matches("")){
            Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();
            return;
        }
        int price = Integer.parseInt(priceString);

        // Create Database helper
        ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

         //Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, titleString);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, supplierString);

       //  Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

       //  Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
         //    If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
       //      Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
       }

    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.id_save_btn:
                //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                insertProduct();
        }
    }
}
