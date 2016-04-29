package dhilip.code.org.budgetbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class UpdateDetail extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    EditText txtAmount, txtDescription;
    TextView lblAction;
    Date date = new Date();
    Integer detailId,actionId;
    Double amount;
    String decription;
    SummaryDetails details = new SummaryDetails();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        lblAction = (TextView)findViewById(R.id.lblActionId);
        Intent intent = getIntent();


        actionId = intent.getIntExtra("actionId", -1);
        detailId = intent.getIntExtra("detailId", -1);
        amount = intent.getDoubleExtra("amount", 0.0);
        decription = intent.getStringExtra("description");

        if (actionId == 1)
            lblAction.setText("SAVING :");
        else if(actionId ==2)
            lblAction.setText("SPENT :");
        txtAmount.setText(amount.toString());
        txtDescription.setText(decription);

    }
    public void btnOnClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnBudget: {
                List<SummaryDetails> summaryDetailsa = helper.getRecords();

                if (txtAmount.getText().toString().isEmpty()){
                    txtAmount.setError("Invalid Amount");
                    break;
                }
                if(txtDescription.getText().toString().isEmpty()){
                    txtDescription.setError("Invalid Description");
                    break;
                }

                amount = Double.parseDouble(txtAmount.getText().toString());
                decription = txtDescription.getText().toString();
                details.setId(detailId);
                details.setAmount(amount);
                details.setDescription(decription);
                details.UpdatedOn = date;

                Integer updatedId =  helper.UpdateDetailById(details);
                List<SummaryDetails> summaryDetailsa1 = helper.getRecords();
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(UpdateDetail.this,ListDetails.class);
                startActivity(i);
            }
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:{
                AlertDialog.Builder alert = new AlertDialog.Builder(UpdateDetail.this);
                alert.setTitle("Delete");
                alert.setMessage("Do you want delete this item?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.DeleteDetailById(detailId);
                        Intent i = new Intent(UpdateDetail.this, ListDetails.class);
                        startActivity(i);
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
