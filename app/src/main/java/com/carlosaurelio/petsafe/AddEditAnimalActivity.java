package com.carlosaurelio.petsafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddEditAnimalActivity extends AppCompatActivity {

    private static int TYPE_ACTIVITY;
    private static String TITLE_ACTIVITY;
    private static Cliente mClientes;

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static String mCurrentPhotoPath;
    private static Bitmap mBitmap;

    private static int targetW;
    private static int targetH;

    private AnimalController mAnimalController;
    private ClienteController mClienteController;

    private Spinner spnTipo;
    private AutoCompleteTextView edtDono;
    private EditText edtNomeAnimal, edtIdade, edtPeso;
    private ImageView imgPhoto;
    private FloatingActionButton fabDeletar;
    private static int codigoAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_animal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TYPE_ACTIVITY = bundle.getInt("TYPE_ACTIVITY");

        mAnimalController = new AnimalController(this);
        mClienteController = new ClienteController(this);

        edtDono = (AutoCompleteTextView) findViewById(R.id.edtDono);
        edtDono.setThreshold(1);
        ClienteAdapter customerAdapter = new ClienteAdapter(this, mClienteController.findAll());
        edtDono.setAdapter(customerAdapter.executeAutoComplete());
        edtDono.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClientes = (Cliente) parent.getItemAtPosition(position);
            }
        });

        spnTipo = (Spinner) findViewById(R.id.spnTipo);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.tipoanimal, android.R.layout.simple_spinner_item);
        spnTipo.setAdapter(adapter);

        edtNomeAnimal = (EditText) findViewById(R.id.edtNomeAnimal);
        edtIdade = (EditText) findViewById(R.id.edtIdade);
        edtPeso = (EditText) findViewById(R.id.edtPeso);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        fabDeletar = (FloatingActionButton) findViewById(R.id.btnDelFoto);

        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.mAppBarLayout);
                    mAppBarLayout.setExpanded(false, true);
                }
            }
        };

        edtDono.setOnFocusChangeListener(listener);
        edtNomeAnimal.setOnFocusChangeListener(listener);
        edtIdade.setOnFocusChangeListener(listener);
        edtPeso.setOnFocusChangeListener(listener);

        imgPhoto.measure(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.MATCH_PARENT);
        targetH = imgPhoto.getMeasuredWidth();
        targetW = imgPhoto.getMeasuredHeight();

        // Verificando se está voltando da foto
        if (savedInstanceState == null) {
            mCurrentPhotoPath = null;
            mBitmap = null;
            mClientes = null;

            // Editando
            if (TYPE_ACTIVITY == 1) {
                codigoAnimal = bundle.getInt("codigoAnimal");
                List<Cliente> clienteList = mClienteController.findDono(bundle.getInt("codigoCliente"));
                mClientes = clienteList.get(0);
                edtDono.setText(mClientes.getNomeCliente() + " " + mClientes.getSegundoNomeCliente());
                edtNomeAnimal.setText(bundle.getString("nomeAnimal"));
                getSupportActionBar().setTitle(bundle.getString("nomeAnimal"));
                spnTipo.setSelection(bundle.getInt("tipoAnimal"));
                edtIdade.setText(bundle.getString("idadeAnimal"));
                edtPeso.setText(bundle.getString("pesoAnimal"));
                mCurrentPhotoPath = bundle.getString("photoAnimal");
                if (mCurrentPhotoPath != null && !mCurrentPhotoPath.isEmpty()) {
                    mBitmap = carregarImagem();
                    if (mBitmap != null) {
                        imgPhoto.setImageBitmap(mBitmap);
                    }
                }
            }
        }

        setupCollapsingToolbar();
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean someDataEntered() {
        if (edtNomeAnimal.getText().toString().length() > 0 ||
                edtIdade.getText().toString().length() > 0 ||
                edtPeso.getText().toString().length() > 0) {
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

    private File createImageFile(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imagesFolder = new File(storageDir, "Pet Safe");
        imagesFolder.mkdirs();
        if (!imagesFolder.exists()) {
            imagesFolder.createNewFile();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                imagesFolder /* directory */
        );
        FileOutputStream fo = new FileOutputStream(image);
        fo.write(bytes.toByteArray());

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        galleryAddPic();
        return image;
    }

    private Bitmap carregarImagem() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = null;
                if (data.getExtras().get("data") == null) {
                    inputStream = getContentResolver().openInputStream(data.getData());
                } else {
                    // Imagem vem da camera TO DO deletar imagem salva na pasta camera
                    inputStream = getContentResolver().openInputStream(data.getData());
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                createImageFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mBitmap = carregarImagem();
    }

    @Override
    protected void onResume() {
        if (mBitmap != null) {
            imgPhoto.setImageBitmap(mBitmap);
            fabDeletar.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void buscarPhoto(View view) {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Selecione uma Foto";
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

        startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
    }


    public void deletarFoto(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alerta").setMessage("Tem certeza que deseja deletar essa foto?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mCurrentPhotoPath = null;
                mBitmap = null;
                imgPhoto.setImageResource(R.drawable.ic_photo);
                fabDeletar.setVisibility(View.GONE);
                dialog.cancel();
            }
        });

        alert.setNegativeButton("Não", null).create().show();
    }

    public void salvarDadosAnimal(View view) {
        if (edtNomeAnimal.getText().toString().trim().equals("")) {
            TextInputLayout lNomeLayout = (TextInputLayout) findViewById(R.id.lNomeAnimalLayout);
            lNomeLayout.setErrorEnabled(true);
            lNomeLayout.setError("Preencha o campo nome.");
            edtNomeAnimal.setError("Obrigatório");
            edtNomeAnimal.requestFocus();
        } else if (mClientes == null) {
            edtDono.requestFocus();
            Snackbar.make(view, "Selecione um cliente válido.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        } else {
            Animal animal;
            switch (TYPE_ACTIVITY) {
                case 0: // Inserindo
                    animal = new Animal(0, mClientes.getCodigoCliente(),
                            edtNomeAnimal.getText().toString().trim(),
                            spnTipo.getSelectedItemPosition(),
                            edtIdade.getText().toString(),
                            edtPeso.getText().toString(),
                            mCurrentPhotoPath);
                    try {
                        mAnimalController.insert(animal);
                        mAnimalController.close();
                        Toast.makeText(getApplicationContext(), animal.getNomeAnimal() + " cadastrado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível inserir o animal!").create().show();
                        e.printStackTrace();
                    }
                    break;
                case 1: // Editando
                    animal = new Animal(codigoAnimal, mClientes.getCodigoCliente(),
                            edtNomeAnimal.getText().toString(),
                            spnTipo.getSelectedItemPosition(),
                            edtIdade.getText().toString(),
                            edtPeso.getText().toString(),
                            mCurrentPhotoPath);
                    try {
                        mAnimalController.update(animal);
                        mAnimalController.close();
                        Toast.makeText(getApplicationContext(), animal.getNomeAnimal() + " editado com sucesso.", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setPositiveButton("OK", null).setMessage("Não possível atualizar o animal!").create().show();
                        e.printStackTrace();
                    }
                    break;
            }

            Intent intent = new Intent(AddEditAnimalActivity.this, AnimalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}