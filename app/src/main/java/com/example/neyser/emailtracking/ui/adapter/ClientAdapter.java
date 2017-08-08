package com.example.neyser.emailtracking.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.model.Client;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {
    private ArrayList<Client> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvClientId, tvClientFirstName, tvClientLastName, tvClientEmail;
        public ViewHolder(View view) {
            super(view);

            tvClientId = (TextView) view.findViewById(R.id.tvClientId);
            tvClientFirstName = (TextView) view.findViewById(R.id.tvFirstName);
            tvClientLastName = (TextView) view.findViewById(R.id.tvLastName);
            tvClientEmail = (TextView) view.findViewById(R.id.tvClientEmail);
        }
    }

    public ClientAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void addCategories(ArrayList<Client> myDataSet) {
        mDataSet.addAll(myDataSet);
        notifyDataSetChanged();
    }

    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_client, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Client client = mDataSet.get(position);
        holder.tvClientId.setText("Cliente " + client.getId());
        holder.tvClientFirstName.setText(client.getFirstName());
        holder.tvClientLastName.setText(client.getLastName());
        holder.tvClientEmail.setText(client.getEmail());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}