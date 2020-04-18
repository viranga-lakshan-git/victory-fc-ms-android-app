package com.example.laksh.victoryfc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterPlayer extends AppCompatActivity {

    TextView textView; Button btnBrowseImage; InputStream encodedImage; ImageView imageViewPlayer;
    EditText playerUsername,playerName,playerDob,playerPosition,playerHeight,playerWeight,playerPassword,playerConfirmPassword;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final String dbUrl="jdbc:mysql://192.168.1.8/victory_fc";
    private static final String dbUsername="admin";
    private static final String dbPw="admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_player);

        playerUsername=(EditText) findViewById(R.id.txtFanUsername);
        playerName=(EditText)findViewById(R.id.txtName);
        playerDob=(EditText)findViewById(R.id.txtManagerDoB);
        playerPosition=(EditText)findViewById(R.id.txtPosition);
        playerHeight=(EditText)findViewById(R.id.txtHeight);
        playerWeight=(EditText)findViewById(R.id.txtWeight);
        playerPassword=(EditText)findViewById(R.id.txtPlayerPassword);
        playerConfirmPassword=(EditText)findViewById(R.id.txtPlayerConfirmPassword);
        textView=(TextView) findViewById(R.id.txtAlert);
        btnBrowseImage=(Button) findViewById(R.id.btnBrowsePlayerImage);
        imageViewPlayer=(ImageView)findViewById(R.id.imageViewPlayer);
        getSupportActionBar().hide();

        btnBrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && null!=data){
            Bitmap originBitmap=null;
            Uri selectedImage= data.getData();
            try {
                InputStream is=getContentResolver().openInputStream(selectedImage);
                encodedImage=is;
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream imageStream;
            try{
                imageStream=getContentResolver().openInputStream(selectedImage);
                originBitmap=BitmapFactory.decodeStream(imageStream);
            }catch(FileNotFoundException e){
                textView.setText(e.getMessage());
            }
            if(originBitmap!=null){
                Bitmap bMapScaled = Bitmap.createScaledBitmap(originBitmap, 85, 85, true);
                this.imageViewPlayer.setImageBitmap(bMapScaled);
            }
        }
    }

    public void btnRegisterPlayerOnclick(View view) {
        Send objSend= new Send();
        objSend.execute("");
    }

    private class Send extends AsyncTask<String, String, String>{
        String msg="";
        String un=playerUsername.getText().toString();
        String name=playerName.getText().toString();
        String dob=playerDob.getText().toString();
        String position=playerPosition.getText().toString();
        String height=playerHeight.getText().toString();
        String weight=playerWeight.getText().toString();
        String password=playerPassword.getText().toString();
        String cpasswword=playerConfirmPassword.getText().toString();
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
                ResultSet rs=st.executeQuery("SELECT `username` FROM `users` WHERE `username`='"+un+"'");
                if(conn==null){
                    msg="Connection goes wrong..";
                }
                else if(un.isEmpty() || name.isEmpty() || dob.isEmpty() || position.isEmpty() || height.isEmpty() || weight.isEmpty() || password.isEmpty() || cpasswword.isEmpty()){
                    msg="Please fill all fields.";
                }
                else if(rs.isBeforeFirst()){
                    msg="Username is already taken. Try again.";
                }
                else if(!password.equals(cpasswword)){
                    msg="Passwords don't match. Try again.";
                }
                else{
                    String querry1="INSERT INTO `users` (`username`, `user_type`, `pw`, `status`) VALUES ('"+un+"', 'Player', '"+password+"', '1');";
                    String querry2="INSERT INTO `players` (`id`, `username`, `pw`, `name`, `image`, `dob`, `weight`, `height`, `position`, `status`) VALUES (NULL,?,?,?,?,?,?,?,?, '1');";
                    Statement statement=conn.createStatement();
                    PreparedStatement preparedStatement=conn.prepareStatement(querry2);
                    statement.executeUpdate(querry1);
                    Float heightF=Float.parseFloat(height);
                    Float weightF=Float.parseFloat(weight);

                    preparedStatement.setString(1, un);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, name);
                    preparedStatement.setBlob(4, encodedImage);
                    preparedStatement.setString(5, dob);
                    preparedStatement.setFloat(6, weightF);
                    preparedStatement.setFloat(7, heightF);
                    preparedStatement.setString(8, position);

                    preparedStatement.executeUpdate();

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
                startActivity(new Intent(RegisterPlayer.this, Login.class));
        }
    }

}

