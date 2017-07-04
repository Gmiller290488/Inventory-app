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

            Cursor cursor = getContentResolver().query(
                    ProductContract.ProductEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);

             ListView productListView = (ListView) findViewById(R.id.list);

            View emptyView = findViewById(R.id.empty_view);
            productListView.setEmptyView(emptyView);

            ProductCursorAdapter adapter = new ProductCursorAdapter(this, cursor);

            productListView.setAdapter(adapter);
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
       int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from product database");

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
