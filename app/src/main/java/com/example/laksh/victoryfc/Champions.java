package com.example.laksh.victoryfc;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Champions extends AppCompatActivity {

    private ArrayList<ListChampions> championsArrayList;
    private Champions.MyAppAdapter myAppAdapter;
    private ListView championsListView;
    private boolean success = false;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.champions);
        getSupportActionBar().hide();

        championsListView = (ListView) findViewById(R.id.listViewChampions); //ListView Declaration
        championsArrayList = new ArrayList<ListChampions>();

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(Champions.this, "Synchronising",
                    "ListView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPw); //Connection Object
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    String query = "SELECT `year`, `description` FROM `achivements` WHERE `place` LIKE 'Champions' AND `status` = 1 ORDER BY `year` ASC";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                championsArrayList.add(new ListChampions(rs.getInt("year"),rs.getString("description")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Loaded..";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my ListView
        {
            progress.dismiss();
            Toast.makeText(Champions.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new Champions.MyAppAdapter(championsArrayList, Champions.this);
                    championsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    championsListView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            TextView textViewYear;
            TextView textViewDescription;
        }

        public List<ListChampions> parkingList;

        public Context context;
        ArrayList<ListChampions> arraylist;

        private MyAppAdapter(List<ListChampions> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ListChampions>();
            arraylist.addAll(parkingList);
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
        {

            View rowView = convertView;
            Champions.MyAppAdapter.ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_champions, parent, false);
                viewHolder = new Champions.MyAppAdapter.ViewHolder();
                viewHolder.textViewYear = (TextView) rowView.findViewById(R.id.txtViewChampionsYear);
                viewHolder.textViewDescription = (TextView) rowView.findViewById(R.id.txtViewChampionsDescription);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Champions.MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textViewYear.setText(Integer.toString(parkingList.get(position).getYear()));
            viewHolder.textViewDescription.setText(parkingList.get(position).getDescription()+ "");
            return rowView;
        }
    }
}