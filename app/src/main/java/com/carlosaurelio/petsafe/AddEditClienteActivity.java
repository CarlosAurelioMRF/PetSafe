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

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class AddEditClienteActivity extends AppCompatActivity {

    private static int TYPE_ACTIVITY;
    private static String TITLE_ACTIVITY;
    private ClienteController mClienteController;

    private EditText edtNome, edtSegundoNome, edtCpf, edtEndereco, edtTelefone, edtEmail, edtDataNascimento, edtInfo;
    private int codigoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TYPE_ACTIVITY = bundle.getInt("TYPE_ACTIVITY");
        TITLE_ACTIVITY = bundle.getString("TITLE_ACTIVITY");
        getSupportActionBar().setTitle(TITLE_ACTIVITY);

        mClienteController = new ClienteController(this);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSegundoNome = (EditText) findViewById(R.id.edtSegundoNome);
        edtCpf = (EditText) findViewById(R.id.edtCpf);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDataNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        edtInfo = (EditText) findViewById(R.id.edtInfo);

        MaskEditTextChangedListener maskCPF = new MaskEditTextChangedListener("###.###.###-##", edtCpf);
        MaskEditTextChangedListener maskTEL = new MaskEditTextChangedListener("(##)####-####", edtTelefone);
        MaskEditTextChangedListener maskEND = new MaskEditTextChangedListener("##/##/####", edtDataNascimento);

        edtCpf.addTextChangedListener(maskCPF);
        edtTelefone.addTextChangedListener(maskTEL);
        edtDataNascimento.addTextChangedListener(maskEND);

        // Editando
        if (TYPE_ACTIVITY == 1) {
            codigoCliente = bundle.getInt("codigoCliente");
            edtNome.setText(bundle.getString("nomePessoa"));
            edtSegundoNome.setText(bundle.getString("segundoNome"));
            edtCpf.setText(bundle.getString("cpfCliente"));
            edtEndereco.setText(bundle.getString("enderecoCliente"));
            edtTelefone.setText(bundle.getString("telefoneCliente"));
            edtEmail.setText(bundle.getString("emailCliente"));
            edtDataNascimento.setText(bundle.getString("dataNascimento"));
            edtInfo.setText(bundle.getString("informacoes"));
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean someDataEntered() {
        if (edtNome.getText().toString().length() > 0 ||
                edtSegundoNome.getText().toString().length() > 0 ||
                edtCpf.getText().toString().length() > 0 ||
                edtEndereco.getText().toString().length() > 0 ||
                edtTelefone.getText().toString().length() > 0 ||
                edtEmail.getText().toString().length() > 0 ||
                edtDataNascimento.getText().toString().length() > 0 ||
                edtInfo.getText().toString().length() > 0) {
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

    public void salvarDadosCliente(View view) {
        if (edtNome.getText().toString().trim().equals("")) {
            TextInputLayout lNomeLayout = (TextInputLayout) findViewById(R.id.lNomeLayout);
            lNomeLayout.setErrorEnabled(true);
            lNomeLayout.setError("Preencha o campo nome.");
            edtNome.setError("Obrigatório");
            edtNome.requestFocus();
        } else if (edtCpf.getText().toString().trim().equals("")) {
            TextInputLayout lCpfLayout = (TextInputLayout) findViewById(R.id.lCpfLayout);
            lCpfLayout.setErrorEnabled(true);
            lCpfLayout.setError("Preencha o campo CPF.");
            edtCpf.setError("Obrigatório");
            edtCpf.requestFocus();
        } else if (edtTelefone.getText().toString().trim().equals("")) {
            TextInputLayout lTelefoneLayout = (TextInputLayout) findViewById(R.id.lTelefoneLayout);
            lTelefoneLayout.setErrorEnabled(true);
            lTelefoneLayout.setError("Preencha o campo telefone.");
            edtTelefone.setError("Obrigatório");
            edtTelefone.requestFocus();
        } else {
            Cliente cliente;
            switch (TYPE_ACTIVITY) {
                case 0: // Inserindo
                    cliente = new Cliente(0, edtNome.getText().toString().trim(),
                            edtSegundoNome.getText().toString().trim(),
                            edtCpf.getText().toString().trim(),
                            edtEndereco.getText().toString().trim(),
                            edtTelefone.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtDataNascimento.getText().toString().trim(),
                            edtInfo.getText().toString().trim());
                    try {
                        mClienteController.insert(cliente);
                        mClienteController.close();
                        Toast.makeText(getApplicationContext(), cliente.getNomeCliente() + " cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível inserir o cliente!").create().show();
                        e.printStackTrace();
                    }
                    break;
                case 1: // Editando
                    cliente = new Cliente(codigoCliente, edtNome.getText().toString().trim(),
                            edtSegundoNome.getText().toString().trim(),
                            edtCpf.getText().toString().trim(),
                            edtEndereco.getText().toString().trim(),
                            edtTelefone.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            edtDataNascimento.getText().toString().trim(),
                            edtInfo.getText().toString().trim());
                    try {
                        mClienteController.update(cliente);
                        mClienteController.close();
                        Toast.makeText(getApplicationContext(), cliente.getNomeCliente() + " editado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível atualizar o cliente!").create().show();
                        e.printStackTrace();
                    }
                    break;
            }

            Intent intent = new Intent(AddEditClienteActivity.this, ClientesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
