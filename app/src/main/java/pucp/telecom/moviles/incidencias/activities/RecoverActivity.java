package pucp.telecom.moviles.incidencias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;
import pucp.telecom.moviles.incidencias.webRequests.FireUser;

public class RecoverActivity extends AppCompatActivity {
    // Login fields
    EditText email;
    // Button
    Button sendMail;
    // Firebase Authentication
    FireUser fireUser;
    // Dtos
    DtoMessage dtoMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);


        sendMail = findViewById(R.id.buttonRecoverPassword);
        sendMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMail(v);
            }
        });
    }

    public void sendMail(View view) {
        String email = ((EditText) findViewById(R.id.editTextEmailPucpForgot)).getText().toString();
        fireUser = new FireUser();
        fireUser.sendEmailRecoverPassword(email, RecoverActivity.this, new CallbackInterface() {
            @Override
            public void onComplete(Object result) {
                dtoMessage = (DtoMessage) result;
                int status = dtoMessage.getStatus();
                String message = dtoMessage.getMessage();
                switch (status) {
                    case 1:
                        startActivity(new Intent(RecoverActivity.this, LoginActivity.class));
                        break;
                    case -1:
                        break;
                }
                Toast.makeText(RecoverActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}