package dhilip.code.org.budgetbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DatabaseHelper helper = new DatabaseHelper(this);
    ListView listView;
    DetailList detailAdapter;
    Integer actionId;
    EditText searchText;
    Spinner spinner;
    List<SummaryDetails> summaryDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        List<String> list = new ArrayList<String>();
        list.add("All");
        list.add("Saving");
        list.add("Spent");

        spinner = (Spinner)findViewById(R.id.spinActionList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.summaryList);
        searchText=(EditText)findViewById(R.id.txtSearchText);
        initList();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                {
                    initList();
                }
                else {
                    //perform search
                    searchItem(s.toString());

                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                SummaryDetails newsData = (SummaryDetails) o;
                Intent i = new Intent(ListDetails.this, UpdateDetail.class);
                i.putExtra("actionId",newsData.ActionId);
                i.putExtra("detailId", newsData.Id);
                i.putExtra("amount", newsData.Amount);
                i.putExtra("description", newsData.Description);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RemoveItemFromList(position);

                return true;
            }
        });
    }

    private void initList()
    {
        summaryDetails = helper.getRecords();
        detailAdapter=new DetailList(this,summaryDetails);
        listView.setAdapter(detailAdapter);
    }

    protected void searchItem(String searchText)
    {
        List<SummaryDetails> temp = new ArrayList<SummaryDetails>();
        for (SummaryDetails detail:summaryDetails)
        {
            if (!searchText.isEmpty())
            {
                if (detail.Description.contains(searchText) && (detail.ActionId == actionId || actionId == 0))
                {
                    temp.add(detail);
                }
            }
            else
            {
                if (detail.ActionId == actionId || actionId == 0)
                {
                    temp.add(detail);
                }
            }

        }
        detailAdapter=new DetailList(this,temp);
        listView.setAdapter(detailAdapter);
    }
    protected void RemoveItemFromList(int position)
    {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(ListDetails.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<SummaryDetails> summaryDetails= helper.getRecords();
                helper.DeleteDetailById(deletePosition);
                detailAdapter.remove(deletePosition);
                detailAdapter.notifyDataSetChanged();
                detailAdapter.notifyDataSetInvalidated();
                List<SummaryDetails> summaryDetailss= helper.getRecords();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        actionId = (int)id;
        searchItem("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
