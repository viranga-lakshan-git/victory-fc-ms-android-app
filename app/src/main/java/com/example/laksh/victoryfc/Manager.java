package com.example.laksh.victoryfc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Manager extends AppCompatActivity {
    private ImageView imageViewManager;
    private TextView textViewManagerName;
    private TextView textViewManagerDoB;
    private TextView textViewManagerCountry;
    private TextView textViewManagerQuote;
    private TextView textViewManagerMatches;
    private TextView textViewManagerWins;
    private TextView textViewManagerTrophies;
    private TextView textViewMessage;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        getSupportActionBar().hide();

        imageViewManager=findViewById(R.id.imgManager);
        textViewManagerName=findViewById(R.id.txtManagerName);
        textViewManagerDoB=findViewById(R.id.txtManagerDoB);
        textViewManagerCountry=findViewById(R.id.txtManagerCountry);
        textViewManagerQuote=findViewById(R.id.txtManagerQuote);
        textViewManagerMatches=findViewById(R.id.txtManagerMatches);
        textViewManagerWins=findViewById(R.id.txtManagerWins);
        textViewManagerTrophies=findViewById(R.id.txtManagerTrophies);
        textViewMessage=findViewById(R.id.txtMessageView);

        Manager.Get objGet= new Manager.Get();
        objGet.execute("");
    }

    private class Get extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            textViewMessage.setText("Loading..");
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(dbUrl,dbUsername,dbPw);
                if(conn==null){
                    textViewMessage.setText("Connection goes wrong..");
                }
                else{
                    String querry="SELECT `name`, `dob`, `country`, `quote`, `image`, `matches`, `wins`, `trophies` FROM `manager` WHERE `status` = 1 AND `id`=(SELECT MAX(id) FROM `manager`)";
                    Statement stmt=conn.createStatement();
                    ResultSet rs= stmt.executeQuery(querry);
                    if(rs==null) {
                        textViewMessage.setText("No Data");
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (rs.next()) {
                                        textViewManagerName.setText(rs.getString("name"));
                                        textViewManagerDoB.setText(rs.getString("dob"));
                                        textViewManagerCountry.setText(rs.getString("country"));
                                        textViewManagerQuote.setText("\"" + rs.getString("quote") + "\"");
                                        textViewManagerMatches.setText(rs.getString("matches"));
                                        textViewManagerWins.setText(rs.getString("wins"));
                                        textViewManagerTrophies.setText(rs.getString("trophies"));
                                        Blob blob = rs.getBlob("image");
                                        int blobLength = (int) blob.length();
                                        byte[] imgByte = blob.getBytes(1, blobLength);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                                        imageViewManager.setImageBitmap(bmp);
                                    }
                                }catch (Exception ex){
                                    System.out.println(ex);
                                }
                            }
                        });

                    }
                }
            }catch (Exception ex){

                ex.printStackTrace();
                Writer writer=new StringWriter();
                ex.printStackTrace(new PrintWriter(writer));
                textViewMessage.setText(writer.toString());

            }
            return "";
        }

        @Override
        protected void onPostExecute(String msg) {
            textViewMessage.setText("");
        }
    }
}