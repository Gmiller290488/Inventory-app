package com.ctingcter.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ctingcter.inventory.data.ProductContract;

import java.net.URI;

import static com.ctingcter.inventory.data.ProductContract.ProductEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity  {

    private TextView mEmptyStateTextView;

    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_title_text);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.id_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

        @Override
        protected void onStart() {
            super.onStart();
            displayDatabaseInfo();
        }

        private void displayDatabaseInfo() {


            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    ProductContract.ProductEntry._ID,
                    ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                    ProductContract.ProductEntry.COLUMN_QUANTITY,
                    ProductContract.ProductEntry.COLUMN_PRICE};

            // Perform a query on the pets table
//            Cursor cursor = db.query(
//                    TABLE_NAME,   // The table to query
//                    projection,            // The columns to return
//                    null,                  // The columns for the WHERE clause
//                    null,                  // The values for the WHERE clause
//                    null,                  // Don't group the rows
//                    null,                  // Don't filter by row groups
//                    null);                   // The sort order

            Cursor cursor = getContentResolver().query(
                    ProductContract.ProductEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);

            TextView displayView = (TextView) findViewById(R.id.text_view_product);

            try {
                // Create a header in the Text View that looks like this:
                //
                // The pets table contains <number of rows in Cursor> pets.
                // _id - name - breed - gender - weight
                //
                // In the while loop below, iterate through the rows of the cursor and display
                // the information from each column in this order.
                displayView.setText("The products table contains " + cursor.getCount() + " products.\n\n");
                displayView.append(ProductContract.ProductEntry._ID + " - " +
                        ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                        ProductContract.ProductEntry.COLUMN_QUANTITY + " - " +
                        ProductContract.ProductEntry.COLUMN_PRICE + "\n");

                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
                int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_QUANTITY);
                int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE);

                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentQuantity = cursor.getString(quantityColumnIndex);
                    String currentPrice = cursor.getString(priceColumnIndex);
                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            currentName + " - " +
                            currentQuantity + " - " +
                            currentPrice));
                }
            } finally {
                // this hides the empty table display But I need to change it as it doesnt work
                mEmptyStateTextView.setText("");
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                cursor.close();
            }
        }






    private void insertProduct() {

        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Sirloin Steak");
        values.put(ProductContract.ProductEntry.COLUMN_QUANTITY, "10");
        values.put(ProductContract.ProductEntry.COLUMN_PRICE, "£3.99");
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER, "British Premier Meats");
        values.put(ProductContract.ProductEntry.COLUMN_PICTURE_ID, "@drawable/placeholder");

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

    }

    private void deleteAllProducts() {
       // SQLiteDatabase db = mDbHelper.getWritableDatabase();
      //  db.delete(TABLE_NAME, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertProduct();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
