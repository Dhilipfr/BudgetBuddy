package dhilip.code.org.budgetbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.StatementEvent;

/**
 * Created by Dhilip on 13-10-2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BudgetBuddy.db";
    private static final String TABLE_NAME = "Summary";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_ACTIONID = "ActionId";
    private static final String COLUMN_CREATEDON = "CreatedOn";
    private static final String COLUMN_UPDATEDON = "UpdatedOn";
    private static final String COLUMN_AMOUNT = "Amount";
    private static final String COLUMN_DESCRIPTION = "Description";

    SQLiteDatabase db;

    private static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY NOT NULL,"+
            ""+COLUMN_ACTIONID+" INTEGER NOT NULL,"+COLUMN_CREATEDON+" DATE NOT NULL,"+COLUMN_UPDATEDON+" DATE NOT NULL, "+COLUMN_AMOUNT+" "+
            "REAL NOT NULL,"+COLUMN_DESCRIPTION+" TEXT)";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS"+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public String SearchData(String description)
    {
        db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_DESCRIPTION+","+COLUMN_AMOUNT+" FROM "+TABLE_NAME+" WHERE "+COLUMN_AMOUNT+"=100";
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b = "Not Fount";
        if (cursor.moveToFirst())
        {
            do{
                a = cursor.getString(0);

                if (a.equals(description))
                {
                    b = cursor.getString(1);
                    break;
                }
            }while (cursor.moveToNext());
        }
        db.close();
        return b;
    }

    public void InsertDetails(SummaryDetails details)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "SELECT * FROM "+TABLE_NAME+"";
        Cursor c = db.rawQuery(query,null);
        int count = c.getCount();

        values.put(COLUMN_ID,count);
        values.put(COLUMN_ACTIONID,details.ActionId);
        values.put(COLUMN_CREATEDON,getStringDateTime(details.CreatedOn));
        values.put(COLUMN_UPDATEDON,getStringDateTime(details.UpdatedOn));
        values.put(COLUMN_AMOUNT,details.Amount);
        values.put(COLUMN_DESCRIPTION,details.Description);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<SummaryDetails> getRecords()
    {
        db = this.getReadableDatabase();

        List<SummaryDetails> list = new ArrayList<SummaryDetails>();
        String selectQuery = "SELECT * FROM "+TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst())
        {
            do {
                SummaryDetails details = new SummaryDetails();
                details.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                details.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                details.setActionId(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIONID)));
                details.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                details.setCreatedOn(DateToString(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDON))));
                details.setUpdatedOn(DateToString(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATEDON))));
                list.add(details);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public List<SummaryDetails> getRecordsByDate(String startDate,String endDate)
    {
        db = this.getReadableDatabase();

        List<SummaryDetails> list = new ArrayList<SummaryDetails>();
        String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_CREATEDON+" BETWEEN '"+DateToString(startDate)+"' AND '"+DateToString(endDate)+"'";

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst())
        {
            do {
                SummaryDetails details = new SummaryDetails();
                details.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                details.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                details.setActionId(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIONID)));
                details.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                details.setCreatedOn(DateToString(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDON))));
                details.setUpdatedOn(DateToString(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATEDON))));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                //details.setCreatedOn(df.parse(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDON))));
                list.add(details);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public SummaryDetails getDetailByActionId(int actionId)
    {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ACTIONID+"="+actionId+"";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor!=null)
            cursor.moveToFirst();

        SummaryDetails detail = new SummaryDetails();
        detail.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        detail.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
        detail.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        detail.setActionId(cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIONID)));
        return detail;
    }

    /*Get single data*/
    public SummaryDetails getDetailByDescription(String description)
    {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_DESCRIPTION+"='"+description+"'";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor!=null)
            cursor.moveToFirst();

        SummaryDetails detail = new SummaryDetails();
        detail.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        detail.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
        detail.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        return detail;
    }

    public int UpdateDetailById(SummaryDetails detail)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_AMOUNT, detail.Amount);
        values.put(COLUMN_DESCRIPTION, detail.Description);
        values.put(COLUMN_UPDATEDON, getStringDateTime(detail.UpdatedOn));

        int id = db.update(TABLE_NAME,values,"Id=?",new String[]{String .valueOf(detail.getId())});
        db.close();
        return id;
    }

    public void DeleteDetailById(int id)
    {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "Id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void DropTable()
    {
        db.delete(TABLE_NAME,null,null);
    }

    /**
     * get datetime
     * */
    public String getStringDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String d;
        d = dateFormat.format(date);
        return d;
    }

    public Date DateToString(String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateInString = "01/01/2015";
        Date dates = new Date();

        try {
            dates = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }
}
