package com.magung.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.GridViewHolder> {

    private List<Customer> customers;
    private Context context;
    public CustomerAdapter(Context context, List<Customer> customers) {
        this.customers = customers;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_layout, parent, false);
        GridViewHolder viewHolder = new GridViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.GridViewHolder holder, int position) {
        final String id = customers.get(position).getId(),
                nama = customers.get(position).getNama(),
                telp = customers.get(position).getTelp();
        holder.tvId.setText(id);
        holder.tvName.setText(nama);
        holder.tvTelp.setText(telp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, nama, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvTelp;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_nama);
            tvTelp = (TextView) itemView.findViewById(R.id.tv_telp);
        }
    }
}
