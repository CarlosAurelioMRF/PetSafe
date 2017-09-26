package com.carlosaurelio.petsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private UsuarioController mUsuarioController;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsuarioController = new UsuarioController(this);

        try {
            testaInicializacao();
        } catch (Exception e) {
            exibeDialogo("Erro na inicialização do banco de dados.");
            e.printStackTrace();
        }

        if (SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String nomePessoa = bundle.getString("pessoa");
            String usuario = bundle.getString("usuario");
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " " + nomePessoa + "\n " + usuario,
                    Snackbar.LENGTH_LONG).setAction("Action", null);
            View snackbarLayout = snackbar.getView();
            TextView textView = (TextView)snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_user, 0, 0, 0);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnCadastroUsuario) {
            Intent intent = new Intent(this, UsuariosActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.btnLogout) {
            SaveSharedPreference.clearUserName(MainActivity.this);
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCliente(View view) {
        Intent intent = new Intent(this, ClientesActivity.class);
        startActivity(intent);
    }

    public void onClickAnimal(View view) {
        Intent intent = new Intent(this, AnimalActivity.class);
        startActivity(intent);
    }

    public void testaInicializacao() {
        if (mUsuarioController.findAll().isEmpty()) {
            Usuario usuario = new Usuario(0, "Administrador Supremo", "admin", "admin");
            mUsuarioController.insert(usuario);
        }
    }

    public void exibeDialogo(String mensagem) {
        alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("OK", null);
        alert.setMessage(mensagem);
        alert.create().show();
    }
}
