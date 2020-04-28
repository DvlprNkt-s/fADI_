package com.example.fadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fadi.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    private MaterialEditText edtNewUserName,edtNewPass,edtNewEmail;// for Sing up
    private MaterialEditText edtUser,edtPass;                      // for Sing in

    private Button btnSingIn;
    private TextView txtBntSingUp;


    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;

    ConstraintLayout constraintLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        //EditText
        edtUser=(MaterialEditText)findViewById(R.id.edtUserName);
        edtPass=(MaterialEditText)findViewById(R.id.edtPass);

        //View
        btnSingIn=(Button)findViewById(R.id.btn_sing_in);
        txtBntSingUp=(TextView)findViewById(R.id.txt_btn_singUp);

        constraintLayout=(ConstraintLayout)findViewById(R.id.mainA) ;


        txtBntSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });


        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn(edtUser.getText().toString(),edtPass.getText().toString());
            }
        });

    }

    private void singIn(final String user, final String pass) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists())
                {
                    if(!user.isEmpty())
                    {
                        User login =dataSnapshot.child(user).getValue(User.class);
                            if(login.getPassword().equals(pass)){
                                Intent intent = new Intent(MainActivity.this,Home.class);
                                startActivity(intent);}
                            else
                                Toast.makeText(MainActivity.this, "Login Wrong", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(MainActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(MainActivity.this, "Пользователя не существует", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAlertDialog() {
        //AlertDialog
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Регистрация");
        alertDialog.setMessage("Пожалуйста,заполните все поля!");


        //Inflater
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View sing_up_layout = layoutInflater.inflate(R.layout.sing_up_layout,null);

        //EditText
        edtNewUserName=(MaterialEditText)sing_up_layout.findViewById(R.id.edtNewUserName);
        edtNewPass=(MaterialEditText)sing_up_layout.findViewById(R.id.edtNewPass);
        edtNewEmail=(MaterialEditText)sing_up_layout.findViewById(R.id.edtNewEmail);

        alertDialog.setView(sing_up_layout);
        alertDialog.setIcon(R.drawable.user_name);




        //Alert Buttons
        alertDialog.setNegativeButton("ВЫЙТИ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("ЗАРЕГИСТРИРОВАТЬСЯ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User (edtNewUserName.getText().toString(),
                        edtNewPass.getText().toString(),
                        edtNewEmail.getText().toString());



                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         if(!validate()){
                             Toast.makeText(MainActivity.this, "Не успешная регистрация пользователя!!", Toast.LENGTH_SHORT).show();
                            return;
                            }

                        else
                        {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Успешная регистрация пользователя!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                dialog.dismiss();
            }

        });
        alertDialog.show();
        if( edtNewUserName.getText().toString().length() <= 3)
            edtNewUserName.setError("Введите  не меньше 3 символов");

        if (edtNewPass.getText().toString().length() < 4 || edtNewPass.getText().toString().length() > 10)
            edtNewPass.setError("Введите  не меньше 4 символов");
}
    public boolean validate() {
        boolean valid = true;

        if (  edtNewUserName.getText().toString().isEmpty() || edtNewUserName.getText().toString().length() < 3) {
            Toast.makeText(MainActivity.this, "Неправильно ввели пароль!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (edtNewEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(edtNewEmail.getText().toString()).matches()) {
            Toast.makeText(this, "Неверный email!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (edtNewPass.getText().toString().isEmpty() || edtNewPass.getText().toString().length() < 4 || edtNewPass.getText().toString().length() > 10) {
            Toast.makeText(this, "Неправильно ввели пароль!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }



}
