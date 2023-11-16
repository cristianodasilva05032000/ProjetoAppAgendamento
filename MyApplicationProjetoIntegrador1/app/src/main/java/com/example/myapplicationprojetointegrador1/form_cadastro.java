package com.example.myapplicationprojetointegrador1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplicationprojetointegrador1.databinding.ActivityFormCadastroBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class form_cadastro extends AppCompatActivity {

    ActivityFormCadastroBinding binding;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormCadastroBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(

                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("686103582220-j5l6dmup1ln1dnfrsbi16q75avs32fd8.apps.googleusercontent.com")
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btGoogle.setOnClickListener(view1 -> {
            signIn();

        });
    }




    private void signIn (){
        googleSignInClient.signOut(); // Desloga qualquer conta previamente autenticada
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1);

    }

    private void loginComGoogle(String token){
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
        if (task.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Login Efetuado com Sucesso",Toast.LENGTH_LONG).show();
            abrePrincipal();
            finish();
        }else {

            Toast.makeText(getApplicationContext(),"Erro ao Efetuar Login",Toast.LENGTH_LONG).show();
        }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1 ){

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
        try {
            GoogleSignInAccount conta = task.getResult(ApiException.class);
            loginComGoogle(conta.getIdToken());
        } catch (ApiException exception){
            Toast.makeText(getApplicationContext(),"Erro ao Efetuar Login",Toast.LENGTH_LONG).show();
        }
        }
    }

    private void abrePrincipal() {

            Intent intent = new Intent(this, cadastro_user.class);
            startActivity(intent);
            finish();

    }






    private void loginEmailSenha(String email, String senha){

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Login Efetuado com Sucesso",Toast.LENGTH_LONG).show();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Erro ao Efetuar Login",Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
         }

}