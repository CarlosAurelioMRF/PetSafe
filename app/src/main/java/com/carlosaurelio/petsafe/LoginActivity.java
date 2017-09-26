package com.carlosaurelio.petsafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Carlos Aurelio on 31/03/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;
    private UsuarioController mUsuarioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsuarioController = new UsuarioController(this);

        setContentView(R.layout.activity_login);
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

    }

    public void validar(View view) {
        if (edtUsuario.getText().toString().trim().equals("")) {
            TextInputLayout lNomeUsuarioLayout = (TextInputLayout) findViewById(R.id.lNomeUsuarioLayout);
            lNomeUsuarioLayout.setErrorEnabled(true);
            lNomeUsuarioLayout.setError("Preencha o campo usuário.");
            edtUsuario.setError("Obrigatório");
            edtUsuario.requestFocus();
        } else if (edtSenha.getText().toString().trim().equals("")) {
            TextInputLayout lPasswordLayout = (TextInputLayout) findViewById(R.id.lPasswordLayout);
            lPasswordLayout.setErrorEnabled(true);
            lPasswordLayout.setError("Preencha o campo senha.");
            edtSenha.setError("Obrigatório");
            edtSenha.requestFocus();
        } else {
            String usuario = edtUsuario.getText().toString();
            String senha = edtSenha.getText().toString();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("OK", null);
            try {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Autenticando...");
                progressDialog.show();
                boolean isValid = mUsuarioController.validaLogin(usuario, senha);
                if (isValid) {
                    SaveSharedPreference.setUserName(LoginActivity.this, usuario);
                    Intent intent = getIntent().setClass(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("pessoa", mUsuarioController.findUsuarioNome(usuario));
                    bundle.putString("usuario", usuario);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    alert.setMessage("Verifique usuário e senha!");
                    alert.create().show();
                }
            } catch (Exception e) {
                alert.setMessage("Erro validando usuario e senha");
                alert.create().show();
                e.printStackTrace();
            }
        }
    }
}
