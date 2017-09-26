package com.carlosaurelio.petsafe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class AnimalActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView txtEmptyView;

    private List<Animal> mAnimalList;
    private AnimalController mAnimalController;
    private List<Cliente> mClienteList;
    private ClienteController mClienteController;

    private boolean mPesquisando;

    private static int COD_DONO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        txtEmptyView = (TextView) findViewById(R.id.empty_view);

        mAnimalController = new AnimalController(this);
        mClienteController = new ClienteController(this);
        mClienteList = mClienteController.findAll();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            COD_DONO = bundle.getInt("COD_DONO");
            mAnimalList = mAnimalController.findPorCliente(COD_DONO);
        } else {
            mAnimalList = mAnimalController.findAll();
        }

        if (intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mAnimalList = mAnimalController.findAnimal(query);
            setUpRecyclerView(true);
        }

        if (mAnimalList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            txtEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            txtEmptyView.setVisibility(View.GONE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdicionarAnimal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClienteList.isEmpty()) {
                    Snackbar.make(v, "Necessário cadastrar um cliente antes.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Intent intent = new Intent(getApplication().getApplicationContext(), AddEditAnimalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("TYPE_ACTIVITY", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualiza, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.btnSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        if (!mPesquisando) {
            setUpRecyclerView(false);
        }
        super.onResume();
    }

    private void setUpRecyclerView(boolean pesquisando) {
        mRecyclerView.setAdapter(new AnimalAdapter(this, mAnimalList));
        mPesquisando = pesquisando;
    }
}
