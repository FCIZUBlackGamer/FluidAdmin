package com.thetatechno.fluidadmin.ui.branches;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.ui.clientList.ClientListViewAdapter;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.BranchViewHolder> {
    private static final String ARG_BRANCH = "branch";
    private Context context;
   private List<Branch> branchList;
   private OnDeleteListener listener;
  private   NavController navController;

    public BranchesAdapter(NavController navController, Context context, @Nullable List<Branch> branchList) {

        this.context = context;
        this.branchList = branchList;
        this.navController = navController;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(context).inflate(R.layout.branch_layout_item, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BranchViewHolder holder, final int position) {
        holder.descriptionTxt.setText(branchList.get(position).getDescription());
        holder.addressTxt.setText(branchList.get(position).getAddress());
        holder.phoneTxt.setText(branchList.get(position).getMobileNumber());
        holder.mailTxt.setText(branchList.get(position).getEmail());
        holder.branchOptionMenuTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.branchOptionMenuTxtView);
                //inflating agents_menu from xml resource
                popup.inflate(R.menu.branch_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.editBranch:
                                //handle edit click
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ARG_BRANCH, (Serializable) branchList.get(position));
                                navController.navigate(R.id.action_branches_to_addOrUpdateBranch, bundle);
                                break;
                            case R.id.deleteBranch:
                                //handle delete click
                                if (listener != null)
                                    listener.onDeleteButtonClicked(branchList.get(position));
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (branchList.size() > 0)
            return branchList.size();
        else return 0;
    }


    public class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTxt, mailTxt, phoneTxt;
        TextView addressTxt;
        TextView branchOptionMenuTxtView;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTxt = itemView.findViewById(R.id.branch_description_txt);
            mailTxt = itemView.findViewById(R.id.branch_email_txt);
            phoneTxt = itemView.findViewById(R.id.branch_phone_txt);
            addressTxt = itemView.findViewById(R.id.branch_address_txt);
            branchOptionMenuTxtView = itemView.findViewById(R.id.branchTextOptionMenu);
        }
    }
}
