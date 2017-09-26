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
 * Created by Carlos Aurelio on 27/03/2016.
 */
public class AnimalAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private AnimalController mAnimalController;
    private List<Animal> mAnimals;
    Context mContext;

    public AnimalAdapter(Context context, List<Animal> animais) {
        mAnimalController = new AnimalController(context);
        this.mAnimals = animais;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnimalViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AnimalViewHolder viewHolder = (AnimalViewHolder) holder;

        ClienteController mClienteController = new ClienteController(mContext);

        viewHolder.txtTitulo.setText(mAnimals.get(position).getNomeAnimal());
        viewHolder.txtSubtitulo.setText(mClienteController.findById(mAnimals.get(position).getCodigoCliente()));

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.swipeDireita));

//        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, " Fazer algo ao clicar : " + mAnimals.get(position).getNomeAnimal()
//                        + " \n" + mAnimals.get(position).getTipoAnimal(), Toast.LENGTH_SHORT).show();
//            }
//        });

        viewHolder.btnContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float scale = mContext.getResources().getDisplayMetrics().density;
                final int positionSelected = position;

                List<ContextMenuItem> items = new ArrayList<>();
                items.add(new ContextMenuItem(R.drawable.ic_visible, "Visualizar"));
                items.add(new ContextMenuItem(R.drawable.ic_dono, "Ver Dono"));

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
                                Intent intent = new Intent(mContext, AddEditAnimalActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("codigoAnimal", mAnimals.get(positionSelected).getCodigoAnimal());
                                bundle.putInt("codigoCliente", mAnimals.get(positionSelected).getCodigoCliente());
                                bundle.putString("nomeAnimal", mAnimals.get(positionSelected).getNomeAnimal());
                                bundle.putInt("tipoAnimal", mAnimals.get(positionSelected).getTipoAnimal());
                                bundle.putString("idadeAnimal", mAnimals.get(positionSelected).getIdadeAnimal());
                                bundle.putString("pesoAnimal", mAnimals.get(positionSelected).getPesoAnimal());
                                bundle.putString("photoAnimal", mAnimals.get(positionSelected).getPhotoAnimal());
                                bundle.putInt("TYPE_ACTIVITY", 1);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(mContext, ClientesActivity.class);
                                bundle = new Bundle();
                                bundle.putInt("COD_DONO", mAnimals.get(positionSelected).getCodigoCliente());
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
                notifyItemRangeChanged(position, mAnimals.size());
                mItemManger.closeAllItems();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public String remove(int position, AnimalViewHolder viewHolder) {
        String nomeAnimal = mAnimals.get(position).getNomeAnimal();
        try {
            if (mAnimalController.delete(mAnimals.get(position).getCodigoAnimal())) {
                mAnimals.remove(position);
                notifyItemRemoved(position);
                Snackbar.make(viewHolder.itemView, nomeAnimal + " foi deletado com sucesso.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (Exception e) {
            Log.d("AnimalAdapter", "Não foi possível deletar: " + e.getMessage());
            e.printStackTrace();
        }

        return nomeAnimal;
    }

    public class AnimalViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        Button btnDelete;
        ImageButton btnContext;
        TextView txtTitulo, txtSubtitulo;

        public AnimalViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_view, parent, false));
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeletar);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtSubtitulo = (TextView) itemView.findViewById(R.id.txtSubtitulo);
            btnContext = (ImageButton) itemView.findViewById(R.id.btnContext);
        }
    }
}