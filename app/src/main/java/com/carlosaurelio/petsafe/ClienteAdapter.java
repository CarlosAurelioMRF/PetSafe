package com.carlosaurelio.petsafe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos Aurelio on 22/03/2016.
 */
public class ClienteAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private ClienteController mClienteController;
    private List<Cliente> mClientes;
    Context mContext;

    public ClienteAdapter(Context context, List<Cliente> clientes) {
        mClienteController = new ClienteController(context);
        if (clientes == null) {
            mClientes = mClienteController.findAll();
        } else {
            mClientes = clientes;
        }
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClienteViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ClienteViewHolder viewHolder = (ClienteViewHolder) holder;

        viewHolder.txtTitulo.setText(mClientes.get(position).getNomeCliente());
        viewHolder.txtSubtitulo.setText(mClientes.get(position).getEmailCliente());

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.swipeEsquerda));
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.swipeDireita));

//        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, " Faz algo ao clicar : " + mClientes.get(position).getNomeCliente()
//                        + " \n" + mClientes.get(position).getTelefoneCliente(), Toast.LENGTH_SHORT).show();
//            }
//        });

        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=" + mClientes.get(position).getEnderecoCliente());
                Intent intentCall = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intentCall);
            }
        });

        viewHolder.btnContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float scale = mContext.getResources().getDisplayMetrics().density;
                final int positionSelected = position;

                List<ContextMenuItem> items = new ArrayList<>();
                items.add(new ContextMenuItem(R.drawable.ic_visible, "Visualizar"));
                items.add(new ContextMenuItem(R.drawable.ic_animais, "Ver Animais"));
                items.add(new ContextMenuItem(R.drawable.ic_phone, "Ligar"));

                ContextMenuAdapter adapter = new ContextMenuAdapter(mContext, items, 0);

                ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);
                listPopupWindow.setAdapter(adapter);
                listPopupWindow.setAnchorView(viewHolder.btnContext);
                listPopupWindow.setWidth((int) (240 * scale + 0.5f));
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        Bundle bundle;
                        switch (position) {
                            case 0:
                                intent = new Intent(mContext, AddEditClienteActivity.class);
                                bundle = new Bundle();
                                bundle.putInt("codigoCliente", mClientes.get(positionSelected).getCodigoCliente());
                                bundle.putString("nomePessoa", mClientes.get(positionSelected).getNomeCliente());
                                bundle.putString("segundoNome", mClientes.get(positionSelected).getSegundoNomeCliente());
                                bundle.putString("cpfCliente", mClientes.get(positionSelected).getCpfCliente());
                                bundle.putString("enderecoCliente", mClientes.get(positionSelected).getEnderecoCliente());
                                bundle.putString("telefoneCliente", mClientes.get(positionSelected).getTelefoneCliente());
                                bundle.putString("emailCliente", mClientes.get(positionSelected).getEmailCliente());
                                bundle.putString("dataNascimento", mClientes.get(positionSelected).getDataNascimentoCliente());
                                bundle.putString("informacoes", mClientes.get(positionSelected).getObservacaoCliente());
                                bundle.putInt("TYPE_ACTIVITY", 1);
                                bundle.putString("TITLE_ACTIVITY", "Cliente [Editando]");
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(mContext, AnimalActivity.class);
                                bundle = new Bundle();
                                bundle.putInt("COD_DONO", mClientes.get(positionSelected).getCodigoCliente());
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                break;
                            case 2:
                                Uri uri = Uri.parse("tel:" + mClientes.get(positionSelected).getTelefoneCliente());
                                Intent intentCall = new Intent(Intent.ACTION_DIAL, uri);
                                mContext.startActivity(intentCall);
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
                notifyItemRangeChanged(position, mClientes.size());
                mItemManger.closeAllItems();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClientes.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void remove(int position, ClienteViewHolder viewHolder) {
        String nomeCliente = mClientes.get(position).getNomeCliente();
        try {
            if (mClienteController.delete(mClientes.get(position).getCodigoCliente())) {
                mClientes.remove(position);
                notifyItemRemoved(position);
                Snackbar.make(viewHolder.itemView, nomeCliente + " foi deletado com sucesso.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (Exception e) {
            Log.d("UsuarioAdapter", "Não foi possível deletar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayAdapter<Cliente> executeAutoComplete() {
        return new ClienteAdapterAutoComplete(mContext, R.layout.content_add_edit_animal, R.id.lbl_name, mClientes);
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        SwipeLayout swipeLayout;
        Button btnDelete;
        ImageButton btnContext, btnLocation;
        TextView txtTitulo, txtSubtitulo;

        public ClienteViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_view, parent, false));
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            btnDelete = (Button) itemView.findViewById(R.id.btnDeletar);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtSubtitulo = (TextView) itemView.findViewById(R.id.txtSubtitulo);
            btnContext = (ImageButton) itemView.findViewById(R.id.btnContext);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);

        }
    }

    public class ClienteAdapterAutoComplete extends ArrayAdapter<Cliente> {

        private Context context;
        private int resource, textViewResourceId;
        private List<Cliente> mClientes, tempItems, suggestions;

        public ClienteAdapterAutoComplete(Context context, int resource, int textViewResourceId, List<Cliente> clientes) {
            super(context, resource, textViewResourceId, clientes);
            this.context = context;
            this.resource = resource;
            this.textViewResourceId = textViewResourceId;
            this.mClientes = clientes;
            tempItems = new ArrayList<Cliente>(clientes);
            suggestions = new ArrayList<Cliente>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_people, parent, false);
            }
            Cliente cliente = mClientes.get(position);
            if (cliente != null) {
                TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
                if (lblName != null)
                    lblName.setText(cliente.getNomeCliente() + " " + cliente.getSegundoNomeCliente());
            }
            return view;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((Cliente) resultValue).getNomeCliente() + " " + ((Cliente) resultValue).getSegundoNomeCliente();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (Cliente cliente : tempItems) {
                        if (cliente.getNomeCliente().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                cliente.getSegundoNomeCliente().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(cliente);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Cliente> filterList = (ArrayList<Cliente>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Cliente cliente : filterList) {
                        add(cliente);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }
}
