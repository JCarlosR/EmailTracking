package com.example.neyser.emailtracking.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.model.Link;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
    private ArrayList<Link> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLinkName, tvLinkId, tvLinkUrl, tvLinkType;
        public ViewHolder(View view) {
            super(view);

            tvLinkId = (TextView) view.findViewById(R.id.tvLinkId);
            tvLinkName = (TextView) view.findViewById(R.id.tvLinkName);
            tvLinkUrl = (TextView) view.findViewById(R.id.tvLinkUrl);
            tvLinkType = (TextView) view.findViewById(R.id.tvLinkType);
        }
    }

    public LinkAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setLinks(ArrayList<Link> myDataSet) {
        mDataSet = myDataSet;
        notifyDataSetChanged();
    }

    @Override
    public LinkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_link, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Link link = mDataSet.get(position);
        holder.tvLinkId.setText("Link " + link.getId());
        holder.tvLinkName.setText(link.getName());
        holder.tvLinkUrl.setText(link.getUrl());
        holder.tvLinkType.setText(link.getType());
        // link.getTypeId()
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}