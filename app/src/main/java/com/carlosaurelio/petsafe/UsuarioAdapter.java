package com.carlosaurelio.petsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Aurelio on 19/03/2016.
 */
public class UsuarioAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private UsuarioController mUsuarioController;
    private List<Usuario> mUsuarios;
    Context mContext;

    public UsuarioAdapter(Context context, List<Usuario> usuarios) {
        mUsuarioController = new UsuarioController(context);
        if (usuarios == null) {
            mUsuarios = mUsuarioController.findAll();
        } else {
            mUsuarios = usuarios;
        }

        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsuarioViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final UsuarioViewHolder viewHolder = (UsuarioViewHolder) holder;

        viewHolder.txtTitulo.setText(mUsuarios.get(position).getNomePessoa());
        viewHolder.txtSubtitulo.setText(mUsuarios.get(position).getNomeUsuario());

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.swipeDireita));

//        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, " Fazer algo ao clicar : " + mUsuarios.get(position).getNomePessoa()
//                        + " \n" + mUsuarios.get(position).getNomeUsuario(), Toast.LENGTH_SHORT).show();
//            }
//        });

        viewHolder.btnContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float scale = mContext.getResources().getDisplayMetrics().density;
                final int positionSelected = position;

                List<ContextMenuItem> items = new ArrayList<>();
                items.add(new ContextMenuItem(R.drawable.ic_visible, "Visualizar"));

                ContextMenuAdapter adapter = new ContextMenuAdapter(mContext, items, 1);

                ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);
                listPopupWindow.setAdapter(adapter);
                listPopupWindow.setAnchorView(viewHolder.btnContext);
                listPopupWindow.setWidth((int) (240 * scale + 0.5f));
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(mContext, AddEditUsuarioActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("codigoUsuario", mUsuarios.get(positionSelected).getIdUsuario());
                                bundle.putString("nomePessoa", mUsuarios.get(positionSelected).getNomePessoa());
                                bundle.putString("nomeUsuario", mUsuarios.get(positionSelected).getNomeUsuario());
                                bundle.putInt("TYPE_ACTIVITY", 1);
                                bundle.putString("TITLE_ACTIVITY", "Usuário [Editando]");
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                });
                listPopupWindow.setModal(true);
                listPopupWindow.getBackground().setAlpha(0);
                listPopupWindow.show();
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position, viewHolder);
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mUsuarios.size());
                mItemManger.closeAllItems();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsuarios.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void remove(int position, UsuarioViewHolder viewHolder) {
        String nomePessoa = mUsuarios.get(position).getNomePessoa();
        try {
            if (mUsuarioController.delete(mUsuarios.get(position).getIdUsuario())) {
                mUsuarios.remove(position);
                notifyItemRemoved(position);
                Snackbar.make(viewHolder.itemView, nomePessoa + " foi deletado com sucesso.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (Exception e) {
            Log.d("UsuarioAdapter", "Não foi possível deletar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button btnDelete;
        ImageButton btnContext;
        TextView txtTitulo, txtSubtitulo;

        public UsuarioViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_view, parent, false));
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeletar);
            btnContext = (ImageButton) itemView.findViewById(R.id.btnContext);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtSubtitulo = (TextView) itemView.findViewById(R.id.txtSubtitulo);
        }
    }

}
