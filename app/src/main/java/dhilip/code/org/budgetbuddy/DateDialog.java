package dhilip.code.org.budgetbuddy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dhilip on 21-10-2015.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText txtDate;
    private String selectedDate;
    private String selectedTime;

    public String getSelectedDate()
    {
        return this.selectedDate;
    }
    public String getSelectedTime(){return this.selectedTime;}
    public void setTimeDialog(String time){ this.selectedTime = time;}
    public void setSelectedDate(String selectedDate)
    {
        this.selectedDate = selectedDate;
    }

    public View currentView;
    public DateDialog(View view)
    {
        this.currentView = view;
        txtDate = (EditText)view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+monthOfYear+"-"+dayOfMonth;
        this.setSelectedDate(date);
        txtDate.setText(date);

        TimePickerDialog timeDialog = new TimePickerDialog(this.currentView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        timeDialog.show(ft, "TimePicker");
    }
}
