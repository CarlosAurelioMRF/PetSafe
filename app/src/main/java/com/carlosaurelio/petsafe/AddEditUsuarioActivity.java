package com.carlosaurelio.petsafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditUsuarioActivity extends AppCompatActivity {

    private UsuarioController mUsuarioController;
    private static int TYPE_ACTIVITY;
    private static String TITLE_ACTIVITY;
    private EditText edtNome, edtUsuario, edtSenha;
    private String nomePessoa, nomeUsuario;
    private int codigoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TYPE_ACTIVITY = bundle.getInt("TYPE_ACTIVITY");
        TITLE_ACTIVITY = bundle.getString("TITLE_ACTIVITY");
        getSupportActionBar().setTitle(TITLE_ACTIVITY);

        mUsuarioController = new UsuarioController(this);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtUsuario = (EditText) findViewById(R.id.edtUserName);
        edtSenha = (EditText) findViewById(R.id.edtPassword);

        // Editando
        if (TYPE_ACTIVITY == 1) {
            codigoUsuario = bundle.getInt("codigoUsuario");
            nomePessoa = bundle.getString("nomePessoa");
            nomeUsuario = bundle.getString("nomeUsuario");
            edtNome.setText(nomePessoa);
            edtUsuario.setText(nomeUsuario);
        }
    }

    public void salvarDados(View view) {
        if (edtNome.getText().toString().trim().equals("")) {
            TextInputLayout lNomeLayout = (TextInputLayout) findViewById(R.id.lNomeLayout);
            lNomeLayout.setErrorEnabled(true);
            lNomeLayout.setError("Preencha o campo nome.");
            edtNome.setError("Obrigatório");
            edtNome.requestFocus();
        } else if (edtUsuario.getText().toString().trim().equals("")) {
            TextInputLayout lNomeUsuarioLayout = (TextInputLayout) findViewById(R.id.lNomeUsuarioLayout);
            lNomeUsuarioLayout.setErrorEnabled(true);
            lNomeUsuarioLayout.setError("Preencha o campo nome de usuário.");
            edtUsuario.setError("Obrigatório");
            edtUsuario.requestFocus();
        } else if (edtSenha.getText().toString().trim().equals("")) {
            TextInputLayout lPasswordLayout = (TextInputLayout) findViewById(R.id.lPasswordLayout);
            lPasswordLayout.setErrorEnabled(true);
            lPasswordLayout.setError("Preencha o campo senha.");
            edtSenha.setError("Obrigatório");
            edtSenha.requestFocus();
        } else {
            Usuario usuario;
            switch (TYPE_ACTIVITY) {
                case 0: // Inserindo
                    usuario = new Usuario(0, edtNome.getText().toString().trim(), edtUsuario.getText().toString().trim(), edtSenha.getText().toString().trim());
                    try {
                        mUsuarioController.insert(usuario);
                        mUsuarioController.close();
                        Toast.makeText(getApplicationContext(), usuario.getNomePessoa() + " cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível inserir o usuário!").create().show();
                        e.printStackTrace();
                    }
                    break;
                case 1: // Editando
                    usuario = new Usuario(codigoUsuario, edtNome.getText().toString().trim(), edtUsuario.getText().toString().trim(), edtSenha.getText().toString().trim());
                    try {
                        mUsuarioController.update(usuario);
                        mUsuarioController.close();
                        Toast.makeText(getApplicationContext(), usuario.getNomePessoa() + " editado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível atualizar o usuário!").create().show();
                        e.printStackTrace();
                    }
                    break;
            }

            Intent intent = new Intent(AddEditUsuarioActivity.this, UsuariosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean someDataEntered() {
        if (edtNome.getText().toString().length() > 0 ||
                edtUsuario.getText().toString().length() > 0 ||
                edtSenha.getText().toString().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (someDataEntered()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Alerta").setMessage("Alterações não foram salvas deseja sair assim mesmo?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    supportFinishAfterTransition();
                    dialog.cancel();
                }
            });

            alert.setNegativeButton("Não", null).create().show();

        } else {
            super.onBackPressed();
        }
    }
}
