package com.iamscratches.scratchpad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner dbSelect;
    EditText etItem, etQuantity, etAmount, etDBName;
    DBManager db;
    String dbName;
    List<String> arr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        define();
        findNotePads();

        dbSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), arr.get(position), Toast.LENGTH_LONG).show();
                if(arr.get(position).equalsIgnoreCase("Add new Notepad"))
                    etDBName.setVisibility(View.VISIBLE);
                else{
                    etDBName.setVisibility(View.GONE);
                    dbName = arr.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        findNotePads();
    }

    private void define(){
        db = new DBManager(this);
        dbSelect = (Spinner)findViewById(R.id.dbSelect);
        etAmount = (EditText)findViewById(R.id.etAmount);
        etItem = (EditText)findViewById(R.id.etItem);
        etDBName = (EditText)findViewById(R.id.etDBName);
        etQuantity = (EditText)findViewById(R.id.etQuantity);
    }

    private void findNotePads(){
        arr.clear();
        arr.add("Add new Notepad");
        Cursor cursor = db.getNotepads();
        if(cursor.moveToFirst()) {
            do {
                arr.add(cursor.getString(cursor.getColumnIndex(db.tableName)));
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        dbSelect.setAdapter(arrayAdapter);

    }
    public void viewDatabase(View view) {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
//        finish();
    }

    public void addDatabase(View view) {
        if(etDBName.getVisibility()==View.VISIBLE){
            dbName = etDBName.getText().toString();
            dbName = dbName.replaceAll(" ","_");
        }
        if(dbName.equals("")){
            Toast.makeText(this, "Select a notepad first or create a new one", Toast.LENGTH_LONG).show();
            return;
        }

        String item = etItem.getText().toString();
        String quantity = etQuantity.getText().toString();
        float amount = 0;
        if(etAmount.getText().toString().equals("") || item.equals("")){
            Toast.makeText(this, "Item and amount field can't ve empty", Toast.LENGTH_LONG).show();
            return;
        }
        else
            amount = Float.parseFloat(etAmount.getText().toString());

        db.enterTableData(dbName, item, quantity, amount);
        Cursor cursor = db.query(dbName);
        String tableData = "";
        if(cursor.moveToFirst()){
            do{
                tableData += cursor.getString(cursor.getColumnIndex("ID")) + " " +
                        cursor.getString(cursor.getColumnIndex("dateTime")) + " " +
                        cursor.getString(cursor.getColumnIndex("item")) + " " +
                        cursor.getString(cursor.getColumnIndex("quantity")) +
                        cursor.getString(cursor.getColumnIndex("amount")) + "\n";
            }while (cursor.moveToNext());
        }
        cursor = db.query("Notepads");
        if(cursor.moveToFirst()){
            do{
                tableData += cursor.getString(cursor.getColumnIndex("ID")) + " " +
                        cursor.getString(cursor.getColumnIndex("tableName")) + "\n";
            }while (cursor.moveToNext());
        }
//        Toast.makeText(this, tableData, Toast.LENGTH_LONG).show();
        if(!arr.contains(dbName))
            arr.add(dbName);
    }

    public void freepadLayout(View view) {
        Intent intent = new Intent(this, FreeActivity.class);
        startActivity(intent);
    }
}