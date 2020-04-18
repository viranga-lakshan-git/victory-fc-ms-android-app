package com.example.laksh.victoryfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    private TextView textView;
    private EditText loginUsername,loginPassword;


    //192.168.137.231 -lakshlumia
    //192.168.1.7 -wifi ar
    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textView=(TextView)findViewById(R.id.txtAlertLogin);
        loginUsername=(EditText)findViewById(R.id.txtUsername);
        loginPassword=(EditText)findViewById(R.id.txtPassword);

        getSupportActionBar().hide();
    }

    public void btnRegisterOnclick(View view) {
        Intent promptRegister = new Intent(this, RegisterPrompt.class);
        startActivity(promptRegister);
    }

    public void btnLoginOnClick(View view) {
        Login.Send objSend= new Login.Send();
        objSend.execute("");
    }

    private class Send extends AsyncTask<String, String, Boolean> {
        boolean valid;

        String username=loginUsername.getText().toString();
        String password=loginPassword.getText().toString();


        @Override
        protected void onPreExecute() {
            textView.setText("Validating..");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(dbUrl,dbUsername,dbPw);
                if(conn==null){
                    valid=false;
                    textView.setText("Connection goes wrong..");
                }
                else if(username.isEmpty() && !password.isEmpty()){
                    valid=false;
                    textView.setText("Enter Username.");
                }
                else if(!username.isEmpty() && password.isEmpty()){
                    valid=false;
                    textView.setText("Enter Password.");
                }
                else if(username.isEmpty() && password.isEmpty()){
                    valid=false;
                    textView.setText("Username and Password are Empty.");
                }
                else{
                    String querry="SELECT `username` FROM `users` WHERE `username` = '"+username+"' AND `pw` = '"+password+"' AND `status` = 1;";
                    Statement stmt=conn.createStatement();
                    ResultSet rs= stmt.executeQuery(querry);
                    if(!rs.next()) {
                        valid=false;
                        textView.setText("Invalid Username or Password");
                    }
                    else{
                        valid=true;
                    }
                }
            }catch (Exception ex){
                valid=false;
                textView.setText("Connection goes wrong..");
                ex.printStackTrace();
            }
            return valid;
        }

        @Override
        protected void onPostExecute(Boolean valid) {
            if(valid){
                startActivity(new Intent(Login.this, MainMenu.class));
            }
        }
    }
}

