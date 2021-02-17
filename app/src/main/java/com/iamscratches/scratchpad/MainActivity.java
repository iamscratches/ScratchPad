package com.iamscratches.scratchpad;

import androidx.appcompat.app.AppCompatActivity;

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
    EditText etItem, etAmount, etDBName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbSelect = (Spinner)findViewById(R.id.dbSelect);
        etAmount = (EditText)findViewById(R.id.etAmount);
        etItem = (EditText)findViewById(R.id.etItem);
        etDBName = (EditText)findViewById(R.id.etDBName);

        List<String> arr = new ArrayList<String>();
        arr.add("Admin");
        arr.add("Manager");
        arr.add("Tester");
        arr.add("Developer");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        dbSelect.setAdapter(arrayAdapter);
        dbSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), arr.get(position), Toast.LENGTH_LONG).show();
                if(arr.get(position).equalsIgnoreCase("Admin"))
                    etDBName.setVisibility(View.VISIBLE);
                else
                    etDBName.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void viewDatabase(View view) {
    }

    public void addDatabase(View view) {
    }
}