package com.thetatechno.fluidadmin.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;

public class CodeListAdapter extends RecyclerView.Adapter<CodeListAdapter.CodeViewHolder>{

    Context context;
    FragmentManager fragmentManager;
    List<Code> codeList;
    OnDeleteListener listener;

    NavController navController;
    Bundle bundle;


    public CodeListAdapter(NavController navControlle, Context context, List<Code> codeList, FragmentManager fragmentManager) {
        this.codeList = codeList;
        this.context = context;
        navController = navControlle;
        bundle = new Bundle();
        this.fragmentManager = fragmentManager;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;
    }

    @NonNull
    @Override
    public CodeListAdapter.CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.code_list_item, parent, false);

        return new CodeListAdapter.CodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CodeListAdapter.CodeViewHolder holder, final int position) {

        try {
            holder.codeTxt.setText(codeList.get(position).getCode());
//            holder.codeTypeTxt.setText(codeList.get(position).getCodeType());
            holder.codeDescriptionTxt.setText(codeList.get(position).getDescription());

            holder.codeTextViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    PopupMenu popup = new PopupMenu(context, holder.codeTextViewOptions);
                    popup.inflate(R.menu.default_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    bundle.putSerializable(ARG_CODE, (Serializable) codeList.get(position));
                                    bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Code);
                                    bundle.putSerializable("codeList", (Serializable) codeList);
                                    navController.navigate(R.id.codeAddFragment, bundle);

                                    break;
                                case R.id.delete:
                                    //handle delete click
                                    if(listener !=null)
                                        listener.onDeleteButtonClicked(codeList.get(position));

                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

        }catch (Exception e){
            Sentry.capture(e);
        }

    }

    @Override
    public int getItemCount() {
        Log.e("Size Count", codeList.size()+"");
        return codeList.size();
    }

    public class CodeViewHolder extends RecyclerView.ViewHolder {
        TextView codeTextViewOptions, codeTxt, codeDescriptionTxt;


        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            codeTextViewOptions = itemView.findViewById(R.id.codeTxtViewOptions);
            codeTxt = itemView.findViewById(R.id.code_txt);
//            codeTypeTxt = itemView.findViewById(R.id.codeType_txt);
//            userCodeTxt = itemView.findViewById(R.id.userCode_txt);
            codeDescriptionTxt = itemView.findViewById(R.id.codeDescription);

        }
    }
}
