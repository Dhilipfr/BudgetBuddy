package dhilip.code.org.budgetbuddy;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
1-save
2-spent
*/
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper helper = new DatabaseHelper(this);
    Common common = new Common();

    Spinner spinner;
    Integer actionId;
    Double amount,total,spent,save,balance;
    String decription;
    EditText txtAmount, txtDescription,txtDate;
    TextView objTotal,objSave,objSpent;
    SummaryDetails details = new SummaryDetails();
    Date date = new Date();

    @Override
    protected void onStart() {
        super.onStart();
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DPicker");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Menu*/
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_account_balance_wallet_white_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        /*End Menu*/

        List<String> list = new ArrayList<String>();
        list.add("<select>");
        list.add("Saving");
        list.add("Spent");

        spinner = (Spinner)findViewById(R.id.spinActionList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        List<SummaryDetails> summaryDetailsa1= helper.getRecords();
        setNoteValue(summaryDetailsa1);

        /*
        Date date1 = new Date();
        Date date2 = new Date();


        details.ActionId = 1;
        details.Amount = 100.0;
        details.Description = "Test";
        */
        //helper.UpdateDetailById(details);
        //helper.getDetailByDescription(details.Description);
        //
        //List<SummaryDetails> summaryDetailsa= helper.getRecords();
        //String s1 ="";
        //String s2="";
        //List<SummaryDetails> recordBydate= helper.getRecordsByDate(s1,s2);
        //helper.DeleteDetailById(details.ActionId);

        //helper.SearchData(details.Description);
    }

    private void ResetElement()
    {
        txtDescription.setText("");
        txtAmount.setText("");
    }

    private void ResetNote()
    {
        objSpent.setText("0.00");
        objSave.setText("0.00");
        objTotal.setText("0.00");
    }

    public void btnOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBudget:{
                txtAmount = (EditText)findViewById(R.id.txtAmount);
                txtDescription = (EditText)findViewById(R.id.txtDescription);
                txtDate = (EditText)findViewById(R.id.txtDate);

                if (txtAmount.getText().toString().isEmpty()){
                    txtAmount.setError("Invalid Amount");
                    break;
                }
                if(txtDescription.getText().toString().isEmpty()){
                    txtDescription.setError("Invalid Description");
                    break;
                }
                if(details.ActionId.equals(0)){
                    Toast.makeText(MainActivity.this,"Invalid Action",Toast.LENGTH_LONG).show();
                    break;
                }

                amount =  Double.parseDouble(txtAmount.getText().toString());
                decription = txtDescription.getText().toString();

                Date selecteddate = helper.DateToString(txtDate.getText().toString()+":00");

                details.setAmount(common.RoundofDecimal(amount));
                details.setDescription(decription);
                details.CreatedOn = selecteddate;
                details.UpdatedOn = selecteddate;

                helper.InsertDetails(details);
                List<SummaryDetails> summaryDetailsa1= helper.getRecords();
                Toast.makeText(this, "Added", Toast.LENGTH_LONG).show();

                setNoteValue(summaryDetailsa1);
                ResetElement();
            }break;
        }
    }

    public void setNoteValue(List<SummaryDetails> summaryDetailsa1)
    {

        total=0.00;save=0.00;spent=0.00;

        for (SummaryDetails detail:summaryDetailsa1)
        {
            if (detail.ActionId == 1)
                save = save + detail.Amount;
            else if (detail.ActionId == 2)
                spent = spent + detail.Amount;
        }
        save = common.RoundofDecimal(save);
        spent = common.RoundofDecimal(spent);
        balance = save - spent;
        total =common.RoundofDecimal( save + spent );
        objTotal = (TextView)findViewById(R.id.lblTotal);
        objSave = (TextView)findViewById(R.id.lblSave);
        objSpent = (TextView)findViewById(R.id.lblSpent);
        objSpent.setText(spent.toString());
        objSave.setText(save.toString());
        objTotal.setText(balance.toString());
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        actionId = (int)id;
        details.setActionId(actionId);
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume(){
        super.onResume();
        txtAmount = (EditText)findViewById(R.id.txtAmount);
        txtDescription = (EditText)findViewById(R.id.txtDescription);
        txtAmount.setText("");
        txtDescription.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_summary:{
                Intent i = new Intent(MainActivity.this,ListDetails.class);
                startActivity(i);
                return true;
            }
            case R.id.action_new_budget:
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Creating new Budget will remove the old Budget");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.DropTable();
                        ResetNote();
                        Toast.makeText(getApplicationContext(), "Start with new Budget", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
