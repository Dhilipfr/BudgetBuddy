package dhilip.code.org.budgetbuddy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Dhilip on 22-10-2015.
 */
public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    EditText txtTimePicker;
    private String selectedTime;

    public void setSelectedTime(String time)
    {
        this.selectedTime = time;
    }
    public String getSelectedTime(){
        return this.selectedTime;
    }

    public TimePickerDialog(View view)
    {
        txtTimePicker = (EditText)view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR);
        int mm = calendar.get(calendar.MINUTE);
        int ss = calendar.get(calendar.SECOND);

        return new android.app.TimePickerDialog(getActivity(),this,hh,mm,true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay+":"+minute;
        String date = txtTimePicker.getText().toString();
        txtTimePicker.setText(date+" "+time);
        this.setSelectedTime(time);
    }
}
