package pucp.telecom.moviles.incidencias.webRequests;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;

public class FireUser {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    /*
     * 1: Ingreso exitoso.
     * 0: Error en el ingreso. (FirebaseAuthInvalidUserException, FirebaseAuthInvalidCredentialsException)
     * */
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

    /*
     * 1: Usuario registrado.
     * 0: Error en el registro. (FirebaseAuthUserCollisionException, FirebaseAuthWeakPasswordException, FirebaseAuthInvalidCredentialsException)
     * */
    public void doRegister(String email, final String pucpCode, String password, Activity context, final CallbackInterface callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Actualizar al usuario con su codigo pucp y su rol.
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(pucpCode + "-U")
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            callback.onComplete(new DtoMessage("Usuario registrado", 1));
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        callback.onComplete(new DtoMessage("Error en el registro", 0));
                    }
                });

    }
}
