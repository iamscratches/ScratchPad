package com.iamscratches.scratchpad;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    DBManager db;
    Spinner dbSelect;
    String dbName;
    ListView lvList;
    long total = 0;
    long items = 0;

    TextView tvTotalAmount, tvTotalItems;
    List<String> arr = new ArrayList<String>();
    EditText etSearch;
    ImageButton ibHideSearch, ibSearch, ibShowSearch;

    ArrayList<AdapterItems> listnewsData = new ArrayList<AdapterItems>();
    MyCustomAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        define();
        findNotePads();
        setNotepadClickListener();
    }

    private void define(){
        db = new DBManager(DBManager.context);
        dbSelect = (Spinner)findViewById(R.id.dbSelect);
        lvList = (ListView)findViewById(R.id.lvList);
        tvTotalAmount = (TextView)findViewById(R.id.tvTotalAmount);
        tvTotalItems = (TextView)findViewById(R.id.tvTotalItems);
        etSearch = (EditText)findViewById(R.id.etSearch);
        ibSearch = (ImageButton)findViewById(R.id.ibSearch);
        ibHideSearch = (ImageButton)findViewById(R.id.ibHideSearch);
        ibShowSearch = (ImageButton)findViewById(R.id.ibShowSearch);

    }
    private void setNotepadClickListener() {
        dbSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), arr.get(position), Toast.LENGTH_LONG).show();
                dbName = arr.get(position);
                total = 0;
                items = 0;
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void findNotePads(){
        Cursor cursor = db.getNotepads();
        arr.clear();
        if(cursor.moveToFirst()) {
            do {
                arr.add(cursor.getString(cursor.getColumnIndex(db.tableName)));
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        dbSelect.setAdapter(arrayAdapter);

    }

    public void delNotepad(View view) {
        if(arr.size()>1) {
            db.drop(dbName);
            dbName = arr.get(0);
            findNotePads();
        }
        else{
            Toast.makeText(this, "Must have atleast one notepad", Toast.LENGTH_LONG).show();
        }
    }

    public void hideSearch(View view) {
        ibShowSearch.setVisibility(View.VISIBLE);
        ibHideSearch.setVisibility(View.GONE);
        ibSearch.setVisibility(View.GONE);
        etSearch.setVisibility(View.GONE);
    }

    public void search(View view) {
        String str = "%" + etSearch.getText().toString() + "%";
        String selectionArgs[] = {str};
        updateListView(db.query(dbName, " " + " item like ? ",selectionArgs));
        Toast.makeText(getApplicationContext()," Filtered all " +  str  + " items", Toast.LENGTH_SHORT).show();

    }

    public void showSearch(View view) {
        ibShowSearch.setVisibility(View.GONE);
        ibHideSearch.setVisibility(View.VISIBLE);
        ibSearch.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.VISIBLE);
    }

    //display news list
    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<AdapterItems>  listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }

        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final   AdapterItems s = listnewsDataAdpater.get(position);

            TextView tvID=( TextView)myView.findViewById(R.id.tvID);
            tvID.setText( String.valueOf(s.ID));

            TextView tvDate=( TextView)myView.findViewById(R.id.tvDate);
            String str = "" + s.date;
            str = str.substring(8,10) + "/" + str.substring(4,7) + "/" + str.substring(str.length()-2);
            tvDate.setText(str);

            TextView tvItem=( TextView)myView.findViewById(R.id.tvItem);
            tvItem.setText(s.item);

            TextView tvQuantity=( TextView)myView.findViewById(R.id.tvQuantity);
            tvQuantity.setText(String.valueOf(s.quantity));

            TextView tvAmount=( TextView)myView.findViewById(R.id.tvAmount);
            tvAmount.setText(String.valueOf(s.amount));

            ImageView iv = (ImageView)myView.findViewById(R.id.ivDelete);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] selectionArgs = {String.valueOf(s.ID)};
                    db.Delete(dbName, "ID like ?", selectionArgs);
                    Toast.makeText(getApplicationContext(), s.item + " deleted", Toast.LENGTH_SHORT).show();
                    updateListView();
                }
            });

            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectionArgs[] = {s.item};
                    updateListView(db.query(dbName, " " + " item like ? ",selectionArgs));
                    Toast.makeText(getApplicationContext()," Filtered all " +  s.item  + " items", Toast.LENGTH_SHORT).show();
                }
            });

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), s.date, Toast.LENGTH_SHORT).show();
                }
            });

            return myView;
        }

    }

    private void updateListView(){
        Cursor cursor = db.query(dbName);
        total = 0;
        items = 0;
        listnewsData.clear();
        if(cursor.moveToFirst()){
            do{
                listnewsData.add(new AdapterItems(
                                    cursor.getInt(cursor.getColumnIndex("ID")),
                                    cursor.getString(cursor.getColumnIndex("dateTime")),
                                    cursor.getString(cursor.getColumnIndex("item")),
                                    cursor.getString(cursor.getColumnIndex("quantity")),
                                    cursor.getFloat(cursor.getColumnIndex("amount"))
                                )
                );
                total += cursor.getFloat(cursor.getColumnIndex("amount"));
                items++;
            }while (cursor.moveToNext());
        }
        myadapter=new MyCustomAdapter(listnewsData);
        lvList.setAdapter(myadapter);//initialize with data
        tvTotalAmount.setText(String.valueOf(total));
        tvTotalItems.setText(String.valueOf(items));
    }
    private void updateListView(Cursor cursor){
        total = 0;
        items = 0;
        listnewsData.clear();
        if(cursor.moveToFirst()){
            do{
                listnewsData.add(new AdapterItems(
                                cursor.getInt(cursor.getColumnIndex("ID")),
                                cursor.getString(cursor.getColumnIndex("dateTime")),
                                cursor.getString(cursor.getColumnIndex("item")),
                                cursor.getString(cursor.getColumnIndex("quantity")),
                                cursor.getFloat(cursor.getColumnIndex("amount"))
                        )
                );
                total += cursor.getFloat(cursor.getColumnIndex("amount"));
                items++;
            }while (cursor.moveToNext());
        }
//        myadapter=new MyCustomAdapter(listnewsData);
//        lvList.setAdapter(myadapter);//initialize with data
        myadapter.notifyDataSetChanged();
        tvTotalAmount.setText(String.valueOf(total));
        tvTotalItems.setText(String.valueOf(items));
    }
}