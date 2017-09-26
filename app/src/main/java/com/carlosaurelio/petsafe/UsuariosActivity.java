package com.carlosaurelio.petsafe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView txtEmptyView;

    private UsuarioController mUsuarioController;
    private List<Usuario> mUsuarioList;
    private boolean mPesquisando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdicionarUsuario);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication().getApplicationContext(), AddEditUsuarioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE_ACTIVITY", 0);
                bundle.putString("TITLE_ACTIVITY", "Usuário [Inserindo]");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        txtEmptyView = (TextView) findViewById(R.id.empty_view);

        mUsuarioController = new UsuarioController(this);
        mUsuarioList = mUsuarioController.findAll();

        Intent intent = getIntent();
        if (intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mUsuarioList = mUsuarioController.findUsuario(query);
            setUpRecyclerView(mUsuarioList);
        }

        if (mUsuarioList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            txtEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            txtEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        if (!mPesquisando) {
            setUpRecyclerView(null);
        }
        super.onResume();
    }

    private void setUpRecyclerView(List<Usuario> usuarios) {
        if (usuarios == null) {
            mRecyclerView.setAdapter(new UsuarioAdapter(this, null));
            mPesquisando = false;
        } else {
            mRecyclerView.setAdapter(new UsuarioAdapter(this, usuarios));
            mPesquisando = true;
        }
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

}
