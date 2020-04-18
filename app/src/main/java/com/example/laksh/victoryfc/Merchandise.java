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

public class Merchandise extends AppCompatActivity {
    private ArrayList<ListMerchandise> merchandisesArrayList;
    private Merchandise.MyAppAdapter myAppAdapter;
    private ListView merchandiseListView;
    private boolean success = false;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchandise);
        getSupportActionBar().hide();

        merchandiseListView = (ListView) findViewById(R.id.listViewMerchandise); //ListView Declaration
        merchandisesArrayList = new ArrayList<ListMerchandise>();

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Merchandise.this, "Synchronising",
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
                    String query = "SELECT `item_id`, `image`, `item_name`, `item_price` FROM `merchandise` WHERE `status`=1";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next()) {
                            try {
                                merchandisesArrayList.add(new ListMerchandise(rs.getInt("item_id"),rs.getBlob("image"),rs.getString("item_name"),rs.getFloat("item_price")));
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
            Toast.makeText(Merchandise.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new Merchandise.MyAppAdapter(merchandisesArrayList, Merchandise.this);
                    merchandiseListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    merchandiseListView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder {
            TextView textViewItemID;
            ImageView imageViewImage;
            TextView textViewItemName;
            TextView textViewItemPrice;
        }

        public List<ListMerchandise> parkingList;

        public Context context;
        ArrayList<ListMerchandise> arraylist;

        private MyAppAdapter(List<ListMerchandise> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ListMerchandise>();
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
            Merchandise.MyAppAdapter.ViewHolder viewHolder = null;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_view_merchandise, parent, false);
                viewHolder = new Merchandise.MyAppAdapter.ViewHolder();
                viewHolder.textViewItemID = (TextView) rowView.findViewById(R.id.txtViewItemID);
                viewHolder.imageViewImage = (ImageView) rowView.findViewById(R.id.imgViewItem);
                viewHolder.textViewItemName = (TextView) rowView.findViewById(R.id.txtViewItemName);
                viewHolder.textViewItemPrice = (TextView) rowView.findViewById(R.id.txtViewPrice);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (Merchandise.MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textViewItemID.setText(parkingList.get(position).getItemID()+ "");
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
            viewHolder.imageViewImage.setImageBitmap(bmp);
            viewHolder.textViewItemName.setText(parkingList.get(position).getItemName()+ "");
            viewHolder.textViewItemPrice.setText(parkingList.get(position).getItemPrice()+ "");
            return rowView;
        }
    }
}
