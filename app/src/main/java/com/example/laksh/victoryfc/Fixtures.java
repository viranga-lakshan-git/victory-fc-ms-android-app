package com.example.laksh.victoryfc;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Fixtures extends AppCompatActivity {

    private ArrayList<ListFixtures> fixturesArrayList;
    private Fixtures.MyAppAdapter myAppAdapter;
    private ListView fixturesListView;
    private boolean success = false;

    private TextView textViewOpponetTeam;
    private TextView textViewictoryGround;
    private TextView textViewOpponetGround;
    private TextView textViewVenue;
    private TextView textViewDate;
    private TextView textViewTime;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixtures);
        getSupportActionBar().hide();



        fixturesListView = (ListView) findViewById(R.id.listViewFixtures); //ListView Declaration
        fixturesArrayList = new ArrayList<ListFixtures>();

        textViewOpponetTeam=findViewById(R.id.txtViewOpponetTeam);
        textViewictoryGround=findViewById(R.id.txtViewVitoryGround);
        textViewOpponetGround=findViewById(R.id.txtViewOpponetGround);
        textViewVenue=findViewById(R.id.txtViewVenue);
        textViewDate=findViewById(R.id.txtViewDate);
        textViewTime=findViewById(R.id.txtViewTime);

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(Fixtures.this, "Synchronising",
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
                    String query = "SELECT `date`, `time`, `venue`, `team`, `ground` FROM `fixtures` WHERE `status`=1 ORDER BY `fixtures`.`match_id` ASC";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String query2 = "SELECT `date`, `time`, `venue`, `team`, `ground` FROM `fixtures` WHERE `status`=1 AND `next`=1";
                    Statement stmt2 = conn.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query2);
                    if (rs != null && rs2!=null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                fixturesArrayList.add(new ListFixtures(rs.getString("date"),rs.getString("time"),rs.getString("venue"),rs.getString("team"),rs.getString("ground")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (rs2.next()) {
                                        try {
                                            textViewOpponetTeam.setText(rs2.getString("team"));
                                            textViewictoryGround.setText(rs2.getString("ground"));
                                            if (rs2.getString("ground").equals("Home"))
                                                textViewOpponetGround.setText("Away");
                                            else if (rs2.getString("ground").equals("Away"))
                                                textViewOpponetGround.setText("Home");
                                            textViewVenue.setText(rs2.getString("venue"));
                                            textViewDate.setText(rs2.getString("date"));
                                            textViewTime.setText(rs2.getString("time"));
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }
                            });

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
            Toast.makeText(Fixtures.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new Fixtures.MyAppAdapter(fixturesArrayList, Fixtures.this);
                    fixturesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    fixturesListView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            TextView textViewVersus;
            TextView textViewGround;
            TextView textViewVenue;
            TextView textViewDate;
            TextView textViewTime;
        }

        public List<ListFixtures> parkingList;

        public Context context;
        ArrayList<ListFixtures> arraylist;

        private MyAppAdapter(List<ListFixtures> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ListFixtures>();
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
            Fixtures.MyAppAdapter.ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_fixtures, parent, false);
                viewHolder = new Fixtures.MyAppAdapter.ViewHolder();
                viewHolder.textViewVersus = (TextView) rowView.findViewById(R.id.txtViewVersus);
                viewHolder.textViewGround = (TextView) rowView.findViewById(R.id.txtViewGround);
                viewHolder.textViewVenue = (TextView) rowView.findViewById(R.id.txtViewVenue);
                viewHolder.textViewDate = (TextView) rowView.findViewById(R.id.txtViewDate);
                viewHolder.textViewTime = (TextView) rowView.findViewById(R.id.txtViewTime);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Fixtures.MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textViewVersus.setText(parkingList.get(position).getVersus());

            String versus=parkingList.get(position).getGround();
            if(versus.equalsIgnoreCase("Home"))
                viewHolder.textViewGround.setText("Away");
            else if(versus.equalsIgnoreCase("Away"))
                viewHolder.textViewGround.setText("Home");
            else
                viewHolder.textViewGround.setText("");

            viewHolder.textViewVenue.setText(parkingList.get(position).getVenue());
            viewHolder.textViewDate.setText(parkingList.get(position).getDate());
            viewHolder.textViewTime.setText(parkingList.get(position).getTime());
            return rowView;
        }
    }
}
