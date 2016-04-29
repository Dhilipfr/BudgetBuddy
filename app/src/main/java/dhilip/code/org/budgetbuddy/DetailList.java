package dhilip.code.org.budgetbuddy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dhilip on 17-10-2015.
 */
public class DetailList extends BaseAdapter {
    private List<SummaryDetails> detailsArrayList;
    private LayoutInflater inflater;
    public DetailList(Context aContext,List<SummaryDetails> details)
    {
     this.detailsArrayList = details;
        inflater = LayoutInflater.from(aContext);
    }
    @Override
    public int getCount() {
        return detailsArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return detailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void remove(int id)
    {
        detailsArrayList.remove(id);
    }

    public class Holder
    {
        TextView tvAction;
        TextView tvAmount;
        TextView tvDescription;
        TextView tvUpdatedDate;
        ImageView imgView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.detail_list,null,true);
            holder = new Holder();
            holder.tvAmount = (TextView)convertView.findViewById(R.id.lblAmount);
            holder.tvDescription = (TextView)convertView.findViewById(R.id.lblDescription);
            holder.tvUpdatedDate = (TextView)convertView.findViewById(R.id.lblUpdatedDate);
            holder.imgView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }

        if(detailsArrayList.get(position).getDescription().length() >= 29)
            holder.tvDescription.setText(detailsArrayList.get(position).getDescription().substring(0,29)+"...");
        else
            holder.tvDescription.setText(detailsArrayList.get(position).getDescription());

        holder.tvAmount.setText("₹"+detailsArrayList.get(position).getAmount().toString());
        holder.tvUpdatedDate.setText(getStringDateTime(detailsArrayList.get(position).getUpdatedOn()));
        if (detailsArrayList.get(position).ActionId == 1)
            holder.imgView.setImageResource(R.drawable.ic_savings_icon);
        else if (detailsArrayList.get(position).ActionId == 2)
            holder.imgView.setImageResource(R.drawable.ic_salary_512);


        return convertView;
    }

    public String getStringDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String d;
        d = dateFormat.format(date);
        return d;
    }
}
