package pucp.telecom.moviles.incidencias.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;
import pucp.telecom.moviles.incidencias.webRequests.FireUser;

public class LoginActivity extends AppCompatActivity {
    // Login fields
    EditText email;
    EditText password;
    // Login button
    Button btnLogin;

    // Firebase Authentication
    FireUser fireUser;

    // Dtos
    DtoMessage dtoMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login); // activityLoginActivity
        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doLogin(v);
            }
        });

    }

    public void doLogin(View view) {
        Log.d("msgxd", "1");
        String email = ((EditText) findViewById(R.id.editTextEmailPucpLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPasswordLogin)).getText().toString();
        fireUser = new FireUser();
        fireUser.doLogin(email, password, LoginActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                Log.d("msgxd", "4");
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                Log.d("msgxd", "status:" + status);
                switch (status) {
                    case 1:
                        // Redirigir el acceso a su respectiva pagina
                        Log.d("msgxd", "5ok");
                        //startActivity(new Intent(LoginActivity.this, IncidenceActivity.class));
                        break;
                    case -1:
                    case -2:
                        Log.d("msgxd", "5err");
                        // Mostrar mensaje de error.
                        break;
                }
                Log.d("msgxd", "6to");
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });


    }

}
