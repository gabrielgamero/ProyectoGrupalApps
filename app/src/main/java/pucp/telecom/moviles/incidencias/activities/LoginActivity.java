package pucp.telecom.moviles.incidencias.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;
import pucp.telecom.moviles.incidencias.webRequests.FireUser;

public class LoginActivity extends AppCompatActivity {
    /*
    // Login fields
    EditText email;
    EditText password;
    // Login button
    Button login;

    // Firebase Authentication
    FireUser fireUser;

    // Dtos
    DtoMessage dtoMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activityLogin); // activityLoginActivity
    }

    public void doLogin(View view) {
        String email = ((EditText) findViewById(R.id.edittextEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.edittextPassword)).getText().toString();
        fireUser = new FireUser();
        fireUser.doLogin(email, password, LoginActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                switch (status) {
                    case 1:
                        // Redirigir el acceso a su respectiva pagina

                        break;
                    case 2:
                        // Mostrar mensaje de error.

                        break;
                    default:
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }
*/
}


