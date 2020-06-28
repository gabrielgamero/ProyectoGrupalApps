package pucp.telecom.moviles.incidencias.webRequests;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;

public class FireUser {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public void doLogin(String email, String password, Activity context, final CallbackInterface callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        callback.onComplete(new DtoMessage("Ingreso exitoso", 1));
                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(new DtoMessage("Error en el ingreso", 0));
                    }
                });

    }
}
