package pucp.telecom.moviles.incidencias.webRequests;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.telecom.moviles.incidencias.CallbackInterface;
import pucp.telecom.moviles.incidencias.entities.DtoMessage;

public class FireUser {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    /*
     * 1:  Ingreso exitoso.
     * -1: Usuario no existe.  FirebaseAuthInvalidUserException
     * -2: Password invalida.  FirebaseAuthInvalidCredentialsException
     * */
    public void doLogin(String email, String password, Activity context, final CallbackInterface callback) {
        Log.d("msgxd", "2");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("msgxd", "3succ");
                        Log.d("msgxd", mAuth.getCurrentUser().getEmail());

                        callback.onComplete(new DtoMessage("Ingreso exitoso", 1, (FirebaseAuth) mAuth ));
                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msgxd", "3fail");
                        Log.d("msgxd", e.toString());
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            callback.onComplete(new DtoMessage("Usuario no existe", -1));
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            callback.onComplete(new DtoMessage("Password inválida", -2));
                        }

                    }
                });

    }

    /*
     *  1: Usuario registrado.
     * -1: Este correo ya esta usado. FirebaseAuthUserCollisionException.
     * -2: Password minima de 6 caracteres. FirebaseAuthWeakPasswordException
     * -3: Correo inválido. FirebaseAuthInvalidCredentialsException
     * */
    public void doRegister(String email, final String pucpCode, String password, Activity context, final CallbackInterface callback) {
        Log.d("msgxd", "2");
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(context, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Actualizar al usuario con su codigo pucp y su rol.
                        Log.d("msgxd", "3succ");
                        final FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(pucpCode + "-U")
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("msgxd", "estoyaca!!!!");
                                            Log.d("msgxd", mAuth.getCurrentUser().getDisplayName());
                                            callback.onComplete(new DtoMessage("Usuario registrado", 1,(FirebaseAuth) mAuth));
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msgxd", "3fail");
                        Log.d("msgxd", e.toString());

                        if (e instanceof FirebaseAuthUserCollisionException) {
                            callback.onComplete(new DtoMessage("Este correo ya está usado", -1));
                        } else if (e instanceof FirebaseAuthWeakPasswordException) {
                            callback.onComplete(new DtoMessage("Password minima de 6 caracteres", -2));
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            callback.onComplete(new DtoMessage("Correo inválido", -3));
                        }


                        callback.onComplete(new DtoMessage("Error en el registro", 0));
                    }
                });

    }

    /*
     *  1: Enviando correo.
     * -1: Correo inválido.
     * */

    public void sendEmailRecoverPassword(String email, Activity context, final CallbackInterface callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("msgxd", task.toString());
                        if (task.isSuccessful()) {
                            callback.onComplete(new DtoMessage("Enviando correo", 1));
                        } else {
                            callback.onComplete(new DtoMessage("Correo inválido", -1));
                        }
                    }
                });

    }
}
