package com.example.laksh.victoryfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterFan extends AppCompatActivity {

    TextView textView;
    EditText fanEmail,fanUsername,fanPassword,fanCPasword;

    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_fan);

        fanEmail=(EditText)findViewById(R.id.txtFanEmail) ;
        fanUsername=(EditText)findViewById(R.id.txtFanUsername);
        fanPassword=(EditText)findViewById(R.id.txtFanPassword);
        fanCPasword=(EditText)findViewById(R.id.txtFanConfirmPassword);
        textView=(TextView)findViewById(R.id.txtAlertFan);

        getSupportActionBar().hide();
    }

    public void bnSignUpFanOnClick(View view) {
        RegisterFan.Send objSend= new RegisterFan.Send();
        objSend.execute("");
    }

    private class Send extends AsyncTask<String, String, String> {
        String msg="";
        String email=fanEmail.getText().toString();
        String un=fanUsername.getText().toString();
        String password=fanPassword.getText().toString();
        String cpassword=fanCPasword.getText().toString();

        @Override
        protected void onPreExecute() {
            textView.setText("Inserting Data");
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(dbUrl,dbUsername,dbPw);
                Statement st=conn.createStatement();
                Statement st2=conn.createStatement();
                ResultSet rs=st.executeQuery("SELECT `username` FROM `users` WHERE `username` LIKE '"+un+"'");
                ResultSet rs2=st2.executeQuery("SELECT `email` FROM `fans` WHERE `email` LIKE '"+email+"'");
                if(conn==null){
                    msg="Connection goes wrong..";
                }
                else if(email.isEmpty() || un.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
                    msg="Please fill all fields.";
                }
                else if(rs2.isBeforeFirst()){
                    msg="This email is already taken. Try again.";
                }
                else if(rs.isBeforeFirst()){
                    msg="Username is already taken. Try again.";
                }
                else if(!password.equals(cpassword)){
                    msg="Passwords don't match. Try again.";
                }
                else{
                    String querry1="INSERT INTO `users` (`username`, `user_type`, `pw`, `status`) VALUES ('"+un+"', 'Fan', '"+password+"', '1');";
                    String querry2="INSERT INTO `fans` (`username`, `email`, `pw`, `status`) VALUES ('"+un+"', '"+email+"', '"+password+"', '1');";
                    Statement stmt1=conn.createStatement();
                    Statement stmt2=conn.createStatement();
                    stmt1.executeUpdate(querry1);
                    stmt2.executeUpdate(querry2);
                    msg="Registration successful!";

                }
                conn.close();

            }catch (Exception ex){
                msg="Connection goes wrong..";
                ex.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            textView.setText(msg);
            if(msg.equals("Registration successful!"))
                startActivity(new Intent(RegisterFan.this, Login.class));
        }
    }
}