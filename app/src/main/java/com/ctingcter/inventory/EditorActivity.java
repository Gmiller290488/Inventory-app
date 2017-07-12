package com.ctingcter.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ctingcter.inventory.data.ProductContract;

import static com.ctingcter.inventory.R.id.id_decrement_one_btn;

/**
 * Created by CTingCTer on 30/04/2017.
 */

public class EditorActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;
    private Button mDeleteButton;
    private Button mDecrementOne;
    private Button mDecrementTen;
    private Button mIncrementTen;
    private Button mIncrementOne;
    private TextView mPhoneTextView;
    private EditText mPhoneEditText;
    private EditText mProductEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierEditText;
    private EditText mPriceEditText;
    private ImageView mProductImageView;
    private Uri mCurrentProductUri;
    private boolean mProductHasChanged = false;
    int quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        mDecrementOne = (Button) findViewById(R.id.id_decrement_one_btn);
        mDecrementTen = (Button) findViewById(R.id.id_decrement_ten_btn);
        mIncrementOne = (Button) findViewById(R.id.id_increment_one_btn);
        mIncrementTen = (Button) findViewById(R.id.id_increment_ten_btn);
        // All of the declarations for the EditTexts
        mPhoneEditText = (EditText) findViewById(R.id.id_phone_ET);
        mProductEditText = (EditText) findViewById(R.id.id_product_EV);
        mPriceEditText = (EditText) findViewById(R.id.id_price_ET);
        mQuantityEditText = (EditText) findViewById(R.id.id_quantity_ET);
        mSupplierEditText = (EditText) findViewById(R.id.id_supplier_ET);


        mPhoneTextView = (TextView) findViewById(R.id.id_call_supplier);

        mProductEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        View ordering = (View) findViewById(R.id.order_more_TV);

        Button save = (Button) findViewById(R.id.id_save_btn);
        save.setOnClickListener(this);

        Button delete = (Button) findViewById(R.id.id_delete_btn);
        delete.setOnClickListener(this);

        Button decrementOne = (Button) findViewById(id_decrement_one_btn);
        decrementOne.setOnClickListener(this);

        Button decrementTen = (Button) findViewById(R.id.id_decrement_ten_btn);
        decrementTen.setOnClickListener(this);

        Button incrementOne = (Button) findViewById(R.id.id_increment_one_btn);
        incrementOne.setOnClickListener(this);

        Button incrementTen = (Button) findViewById(R.id.id_increment_ten_btn);
        incrementTen.setOnClickListener(this);


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

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    private void saveProduct() {
        String titleString = mProductEditText.getText().toString().trim();
        if (titleString.matches("")) {
            Toast.makeText(this, "Please enter the product name", Toast.LENGTH_SHORT).show();
            return;
        }
        String quantityString = mQuantityEditText.getText().toString().trim();
        if (quantityString.matches("")) {
            quantityString = "0";
        }
        int quantity = Integer.parseInt(quantityString);


        String supplierString = mSupplierEditText.getText().toString().trim();
        if (supplierString.matches("")) {
            Toast.makeText(this, "Please enter the supplier name", Toast.LENGTH_SHORT).show();
            return;
        }

        String priceString = mPriceEditText.getText().toString().trim();
        if (priceString.matches("")) {
            Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();
            return;
        }
        int price = Integer.parseInt(priceString);

        String phoneString = mPhoneEditText.getText().toString().trim();
        if (phoneString.matches("")) {
            Toast.makeText(this, "Please enter the supplier phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        int phone = Integer.parseInt(phoneString);

        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(titleString) && TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(phoneString)) {

            return;
        }


        //Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, titleString);
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, price);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, supplierString);
        values.put(ProductContract.ProductEntry.COLUMN_PHONE, phoneString);

        if (mCurrentProductUri == null) {
            // New product so insert a new product into the provider
            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

            //  Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                //    If the newUri is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
            } else {
                //      Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
            }

        } else {
            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentProductUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProductUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Error with updating product",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Product updated",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_save_btn:
                saveProduct();
                return;

            case R.id.id_delete_btn:
                showDeleteConfirmationDialog();
                return;

            case R.id.id_decrement_one_btn:
                String quantityString = mQuantityEditText.getText().toString().trim();
                int quantity = Integer.parseInt(quantityString);
                if (quantity >= 1) {
                    quantity--;
                    if (quantity < 10) {
                        mDecrementTen.setVisibility(View.GONE);
                    }
                    quantityString = Integer.toString(quantity);
                    mQuantityEditText.setText(quantityString);
                } else {
                    mDecrementOne.setVisibility(View.GONE);
                }
                return;

            case R.id.id_decrement_ten_btn:
                quantityString = mQuantityEditText.getText().toString().trim();
                quantity = Integer.parseInt(quantityString);
                if (quantity >= 10) {
                    quantity = quantity - 10;
                    if (quantity < 10) {
                        mDecrementTen.setVisibility(View.GONE);
                    }
                    if (quantity < 1) {
                        mDecrementOne.setVisibility(View.GONE);
                    }
                    quantityString = Integer.toString(quantity);
                    mQuantityEditText.setText(quantityString);
                } else {
                    mDecrementTen.setVisibility(View.GONE);
                }
                return;

            case R.id.id_increment_one_btn:
                quantityString = mQuantityEditText.getText().toString().trim();
                quantity = Integer.parseInt(quantityString);
                quantity++;
                if (quantity > 9) {
                    mDecrementTen.setVisibility(View.VISIBLE);
                }
                mDecrementOne.setVisibility(View.VISIBLE);

                quantityString = Integer.toString(quantity);
                mQuantityEditText.setText(quantityString);
                return;

            case R.id.id_increment_ten_btn:
                quantityString = mQuantityEditText.getText().toString().trim();
                quantity = Integer.parseInt(quantityString);
                quantity = quantity + 10;
                mDecrementTen.setVisibility(View.VISIBLE);
                mDecrementOne.setVisibility(View.VISIBLE);
                quantityString = Integer.toString(quantity);
                mQuantityEditText.setText(quantityString);
                return;

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
                ProductContract.ProductEntry.COLUMN_PHONE,
                ProductContract.ProductEntry.COLUMN_PICTURE_ID};

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
            int pictureColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PICTURE_ID);
            int phoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PHONE);

            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);
            // Need to implmenet picture

            mProductEditText.setText(name);
            mPriceEditText.setText(price);
            mQuantityEditText.setText(quantity);
            mSupplierEditText.setText(supplier);
            mPhoneTextView.setText(phone);
            mPhoneEditText.setText(phone);

            if (Integer.parseInt(quantity) < 10) {
                mDecrementTen.setVisibility(View.GONE);
            }
            if (Integer.parseInt(quantity) == 0) {
                mDecrementOne.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");
        mPhoneTextView.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("There are unsaved changes, do you want to exit?");
        builder.setPositiveButton("Yes, exit without saving", discardButtonClickListener);
        builder.setNegativeButton("No, I'm not finished", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteProduct() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentProductUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, ("Failed to delete product"),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, ("Product deleted successfully"),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}





