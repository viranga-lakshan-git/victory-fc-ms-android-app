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
import android.widget.ImageView;
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

public class Players extends AppCompatActivity {
    private ArrayList<ListPlayers> playersArrayList;
    private Players.MyAppAdapter myAppAdapter;
    private ListView playersListView;
    private boolean success = false;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        getSupportActionBar().hide();

        playersListView = (ListView) findViewById(R.id.playersListView); //ListView Declaration
        playersArrayList = new ArrayList<ListPlayers>();

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(Players.this, "Synchronising",
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
                    //SELECT p.`image`, s.`player_name`, s.`dob`, s.`position`, s.`height`, s.`weight`, s.`matches`, s.`goals`, s.`assists`, s.`red_cards`, s.`yellow_cards` FROM `statistics` s,`players` p WHERE p.`status`=1 AND s.`status`=1 AND p.`id`=s.`id`
                    //String query = "SELECT `player_name`, `dob`, `position`, `height`, `weight`, `matches`, `goals`, `assists`, `red_cards`, `yellow_cards` FROM `statistics` WHERE `status`=1";
                    String query = "SELECT p.`image`, s.`player_name`, s.`dob`, s.`position`, s.`height`, s.`weight`, s.`matches`, s.`goals`, s.`assists`, s.`red_cards`, s.`yellow_cards` FROM `statistics` s,`players` p WHERE p.`status`=1 AND s.`status`=1 AND p.`id`=s.`id` ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next()) {
                            try {
                                playersArrayList.add(new ListPlayers(rs.getBlob("image"),rs.getString("player_name"),
                                        rs.getString("dob"),rs.getString("position"),rs.getFloat("height"),
                                        rs.getFloat("weight"),rs.getInt("matches"),rs.getInt("goals"),
                                        rs.getInt("assists"),rs.getInt("red_cards"),rs.getInt("yellow_cards")));
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
            Toast.makeText(Players.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(playersArrayList, Players.this);
                    playersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    playersListView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            ImageView imageViewPlayer;
            TextView textViewName;
            TextView textViewBorn;
            TextView textViewPosition;
            TextView textViewHeight;
            TextView textViewWeight;
            TextView textViewMatches;
            TextView textViewGoals;
            TextView textViewAssists;
            TextView textViewRed;
            TextView textViewYellow;
        }

        public List<ListPlayers> parkingList;

        public Context context;
        ArrayList<ListPlayers> arraylist;

        private MyAppAdapter(List<ListPlayers> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ListPlayers>();
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
            ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_players, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageViewPlayer= (ImageView) rowView.findViewById(R.id.imgPlayer);
                viewHolder.textViewName= (TextView) rowView.findViewById(R.id.txtPlayerName);
                viewHolder.textViewBorn= (TextView) rowView.findViewById(R.id.txtPlayerDoB);
                viewHolder.textViewPosition= (TextView) rowView.findViewById(R.id.txtPlayerPosition);
                viewHolder.textViewHeight= (TextView) rowView.findViewById(R.id.txtPlayerHeight);
                viewHolder.textViewWeight= (TextView) rowView.findViewById(R.id.txtPlayerWeight);
                viewHolder.textViewMatches= (TextView) rowView.findViewById(R.id.txtPlayerMatches);
                viewHolder.textViewGoals= (TextView) rowView.findViewById(R.id.txtPlayerGoals);
                viewHolder.textViewAssists= (TextView) rowView.findViewById(R.id.txtPlayerAssists);
                viewHolder.textViewRed= (TextView) rowView.findViewById(R.id.txtPlayerRed);
                viewHolder.textViewYellow= (TextView) rowView.findViewById(R.id.txtPlayerYellow);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Blob blob=parkingList.get(position).getImage();
            int blobLength = 0;
            try {
                blobLength = (int) blob.length();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            byte[] imgByte = new byte[0];
            try {
                imgByte = blob.getBytes(1, blobLength);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
            viewHolder.imageViewPlayer.setImageBitmap(bmp);
            //viewHolder.imageViewPlayer.setImageIcon(parkingList.get(position).getImage());
            viewHolder.textViewName.setText(parkingList.get(position).getName());
            viewHolder.textViewBorn.setText(parkingList.get(position).getDob());
            viewHolder.textViewPosition.setText(parkingList.get(position).getPosition());
            viewHolder.textViewHeight.setText(parkingList.get(position).getHeight().toString());
            viewHolder.textViewWeight.setText(parkingList.get(position).getWeight().toString());
            viewHolder.textViewMatches.setText(Integer.toString(parkingList.get(position).getMatches()));
            viewHolder.textViewGoals.setText(Integer.toString(parkingList.get(position).getGoals()));
            viewHolder.textViewAssists.setText(Integer.toString(parkingList.get(position).getAssists()));
            viewHolder.textViewRed.setText(Integer.toString(parkingList.get(position).getRed()));
            viewHolder.textViewYellow.setText(Integer.toString(parkingList.get(position).getYellow()));
            return rowView;
        }
    }
}