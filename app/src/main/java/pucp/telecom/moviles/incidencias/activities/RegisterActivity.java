package pucp.telecom.moviles.incidencias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;
import pucp.telecom.moviles.incidencias.webRequests.FireUser;

public class RegisterActivity extends AppCompatActivity {
    // Register fields
    EditText email;
    EditText code;
    EditText password;
    // Register button
    Button btnRegister;

    // Firebase Authentication
    FireUser fireUser;
    // Dtos
    DtoMessage dtoMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void doRegister() {
        String email = ((EditText) findViewById(R.id.editTextEmailPucpRegister)).getText().toString();
        String pucpCode = ((EditText) findViewById(R.id.editTextCodePucpRegister)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPasswordRegister)).getText().toString();
        fireUser = new FireUser();
        fireUser.doRegister(email, pucpCode, password, RegisterActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                switch (status) {
                    case 1:
                        // Redirigir el acceso a su respectiva pagina
                        //startActivity(new Intent(RegisterActivity.this, IncidenceActivity.class));
                        break;
                    case 2:
                        // Mostrar mensaje de error.
                        break;
                    default:
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }
}