package pucp.telecom.moviles.incidencias.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;
import pucp.telecom.moviles.incidencias.webRequests.FireUser;

public class LoginActivity extends AppCompatActivity {
    // Login fields
    EditText email;
    EditText password;
    // Button
    Button btnLogin;
    TextView registerUser;

    // Firebase Authentication
    FireUser fireUser;

    // Dtos
    DtoMessage dtoMessage;


    int LAUNCH_CREATE_INCIDENCE_ACTIVITY = 1;
    int LAUNCH_VIEW_INCIDENCE_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // activityLoginActivity

        registerUser = findViewById(R.id.textViewRegistrar);
        registerUser.setText(Html.fromHtml("<a href='#'>No tienes cuenta? Registrate aquí</a>")); // Darle estilo a registrar
        registerUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("mgsxd", "estoy acaa");
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), LAUNCH_VIEW_INCIDENCE_ACTIVITY);
            }
        });

        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doLogin(v);
            }
        });


    }

    public void doLogin(View view) {
        //Log.d("msgxd", "1");
        String email = ((EditText) findViewById(R.id.editTextEmailPucpLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPasswordLogin)).getText().toString();
        fireUser = new FireUser();
        fireUser.doLogin(email, password, LoginActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                //  Log.d("msgxd", "4");
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                //Log.d("msgxd", "status:" + status);
                switch (status) {
                    case 1:
                        // Redirigir el acceso a su respectiva pagina
                        //Log.d("msgxd", "5ok");
                        FirebaseAuth mAuth = (FirebaseAuth) dtoMessage.getObject();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser.isEmailVerified()) {


                            Intent intent = new Intent(LoginActivity.this, ListIncidencesActivity.class);
                            //Log.d("msgxd", "UseName: " + firebaseUser.getDisplayName());
                            String[] nombreUsuario_rol = firebaseUser.getDisplayName().split("-");
                            //Log.d("msgxd", nombreUsuario_rol.toString());
                            String nombreUsuario = nombreUsuario_rol[0];
                            String rol = nombreUsuario_rol[1];

                            intent.putExtra("rol", rol);
                            intent.putExtra("nombreUsuario", nombreUsuario);
                            intent.putExtra("userId", firebaseUser.getUid());

                            Log.d("msgxd", "aea" + rol + nombreUsuario + firebaseUser.getUid());
                            startActivity(intent);
                        } else {

                            mAuth.signOut();
                            message = "Debe confirmar su correo.";
                        }

                        break;
                    case -1:
                    case -2:
                  //      Log.d("msgxd", "5err");
                        // Mostrar mensaje de error.
                        break;
                }
                //Log.d("msgxd", "6to");
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });


    }


    // Al regresar del Register
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CREATE_INCIDENCE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "onActivityResult RESULT_CANCELED", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == LAUNCH_VIEW_INCIDENCE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Activar su cuenta via correo", Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "onActivityResult RESULT_CANCELED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
