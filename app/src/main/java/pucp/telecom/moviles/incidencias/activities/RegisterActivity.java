package pucp.telecom.moviles.incidencias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doRegister(v);
            }
        });
    }

    public void doRegister(View view) {
        Log.d("msgxd", "1");
        String email = ((EditText) findViewById(R.id.editTextEmailPucpRegister)).getText().toString();
        String pucpCode = ((EditText) findViewById(R.id.editTextCodePucpRegister)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPasswordRegister)).getText().toString();
        fireUser = new FireUser();
        fireUser.doRegister(email, pucpCode, password, RegisterActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                Log.d("msgxd", "4");
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                switch (status) {
                    case 1:
                        Log.d("msgxd", "5ok");
                        // Redirigir el acceso a su respectiva pagina
                        //startActivity(new Intent(RegisterActivity.this, IncidenceActivity.class));
                        break;
                    case -1:
                    case -2:
                    case -3:
                        Log.d("msgxd", "5err");
                        // Mostrar mensaje de error.
                        break;
                }
                Log.d("msgxd", "6");
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();


            }
        });
    }
}