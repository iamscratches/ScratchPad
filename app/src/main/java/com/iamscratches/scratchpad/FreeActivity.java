package com.iamscratches.scratchpad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FreeActivity extends AppCompatActivity {

    LinearLayout llCreated, llModified;
    EditText etContents, etNew;
    TextView tvCreated, tvModified;
    Spinner dbSelect;
    ImageButton ibSave, ibDelete;
    String prevContents, freepadName, createdDate = null, modifiedDate;
    FreeDBManager db;
    List<String> arr;
    boolean newFreePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);
        define();

        findNotePads();

        dbSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), arr.get(position), Toast.LENGTH_LONG).show();
                if(arr.get(position).equalsIgnoreCase("Add new Freepad")) {

                    etNew.setVisibility(View.VISIBLE);
                    llCreated.setVisibility(View.GONE);
                    llModified.setVisibility(View.GONE);
                    ibDelete.setEnabled(false);
                    newFreePad = true;
                    etNew.setText("");
                    etContents.setText("");
                    createdDate = null;
                }
                else{
                    etNew.setVisibility(View.GONE);
                    llCreated.setVisibility(View.VISIBLE);
                    llModified.setVisibility(View.VISIBLE);
                    ibDelete.setEnabled(true);
                    getDetails(position);
                    setDetails();
                    newFreePad = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getDetails(int position){
        String selectionArgs[] = {arr.get(position)};
        String projectAll[] = {"*"};
        String selection = db.colFreepads + " like ?";
        Cursor cursor = db.query(projectAll,selection,selectionArgs);
        if(cursor.moveToFirst()) {
            freepadName = cursor.getString(cursor.getColumnIndex(db.colFreepads));
            prevContents = cursor.getString(cursor.getColumnIndex(db.colContents));
            createdDate = cursor.getString(cursor.getColumnIndex(db.colCreated));
            modifiedDate = cursor.getString(cursor.getColumnIndex(db.colModified));
        }
    }
    private void setDetails(){
        etContents.setText(prevContents);
        tvCreated.setText(createdDate);
        tvModified.setText(modifiedDate);
    }
    private void define(){
        llCreated = (LinearLayout)findViewById(R.id.llCreated);
        llModified = (LinearLayout)findViewById(R.id.llModified);
        etContents = (EditText)findViewById(R.id.etContents);
        etNew = (EditText)findViewById(R.id.etNew);
        tvCreated = (TextView) findViewById(R.id.tvCreated);
        tvModified = (TextView)findViewById(R.id.tvModified);
        ibSave = (ImageButton)findViewById(R.id.ibSave);
        ibDelete = (ImageButton)findViewById(R.id.ibDelete);
        dbSelect = (Spinner)findViewById(R.id.dbSelect);
        db = new FreeDBManager(this);
        arr = new ArrayList<String>();



    }

    private void findNotePads(){
        arr.clear();

        Cursor cursor = db.getNotepads();
        if(cursor.moveToFirst()) {
            do {
                arr.add(cursor.getString(cursor.getColumnIndex(db.colFreepads)));
            } while (cursor.moveToNext());
        }
        arr.add("Add new Freepad");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        dbSelect.setAdapter(arrayAdapter);

    }

    public void buDelete(View view) {
        db.Delete(freepadName);

        prevContents = "";
        freepadName = "";
        createdDate = null;
        modifiedDate = "";
        findNotePads();
    }

    public void buSave(View view) {
        String newContents = etContents.getText().toString();

        if(newContents.equals(prevContents)){
            Toast.makeText(this, "no changes are made to be saved", Toast.LENGTH_LONG).show();
            return;
        }
        String newModifiedDate = String.valueOf(new Date());
        if(createdDate== null){
            createdDate = newModifiedDate;
        }
        ContentValues values = new ContentValues();
        values.put(db.colCreated, createdDate);
        values.put(db.colModified, newModifiedDate);
        values.put(db.colContents, newContents);
        if(newFreePad){
            freepadName = etNew.getText().toString();
            values.put(db.colFreepads,freepadName);
            db.Insert(values);
            Toast.makeText(this, "FreePad saved", Toast.LENGTH_LONG).show();
            etNew.setText("");
            etContents.setText("");
        }
        else{
            String selection = db.colFreepads + " like ?";
            String selectionArgs[] = {freepadName};
            db.Update(values,selection, selectionArgs);
            Toast.makeText(this, "FreePad modified", Toast.LENGTH_LONG).show();
        }

        findNotePads();
        String temp = arr.get(0);
        int t = arr.indexOf(freepadName);
        arr.set(0, freepadName);
        arr.set(t, temp);
    }
}