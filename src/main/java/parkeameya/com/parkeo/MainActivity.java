package parkeameya.com.parkeo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import parkeameya.com.parkeo.Model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnRegister;
    RelativeLayout rootLayout;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //presionar control + o
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Before setContentView

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("fonts/Arkhip_font.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build());
        setContentView(R.layout.activity_main);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance();
        users= db.getReference("Users");

        //Init View

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        //Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    /*
    //que no se cierre la sesion
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); *
    }*/


    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Iniciar sesión");
        dialog.setMessage("porfavor ingrese su correo para iniciar sesión");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);


        dialog.setView(login_layout);

        //set button
        dialog.setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                        //set disable button sign in if is processing
                        btnSignIn.setEnabled(false);


                    //    AlertDialog waitingDialog = new  SpotsDialog(MainActivity.this);
                 //       waitingDialog.show();

                       // AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                       // waitingDialog.show();
                        //Check validation
                        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                            Snackbar.make(rootLayout, "porfavor ingrese su correo", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }


                        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                            Snackbar.make(rootLayout, "porfavor ingrese su clave", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (edtPassword.getText().toString().length() < 6) {
                            Snackbar.make(rootLayout, "Contraseña muy corta !!", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

///--------------                         //min 3:20 a 3:40
                        final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                        waitingDialog.show();

                        //Login
                        auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        waitingDialog.dismiss();
    //pasar a otra actividaddddddddddddddd                                    //pasar a otra actividad con Intent
                                        startActivity(new Intent(MainActivity.this, MapsActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Snackbar.make(rootLayout, "ha fallado" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();

                                //active button
                                btnSignIn.setEnabled(true);
                            }
                        });
                    }
                });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        dialog.show();
    }


    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTRAR");
        dialog.setMessage("porfavor ingrese su correo para registrarse");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        dialog.setView(register_layout);

        //set button
        dialog.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Check validation
                if(TextUtils.isEmpty(edtEmail.getText().toString()))
                {
                    Snackbar.make(rootLayout,"porfavor ingrese su correo",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(edtPhone.getText().toString()))
                {
                    Snackbar.make(rootLayout,"Porfavor ingrese su numero",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(edtPhone.getText().toString().length()<6)
                {
                    Snackbar.make(rootLayout,"Ingrese un numero real !!",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(edtPassword.getText().toString()))
                {
                    Snackbar.make(rootLayout,"Porfavor ingrese su clave",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(edtPassword.getText().toString().length()<6)
                {
                    Snackbar.make(rootLayout,"contraseña corta !!",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                //Register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Save user to db
                                User user = new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setPassword(edtPassword.getText().toString());

                                //Use email to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Registrado con exito!!",Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout,"ha fallado"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout,"ha fallado"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });

        dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

}
