package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.ui.addorupdatecode.CodeAddFragment;

import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

public class CodeListAdapter extends RecyclerView.Adapter<CodeListAdapter.CodeViewHolder>{

    Context context;
    FragmentManager fragmentManager;
    List<Code> codeList;
    OnDeleteListener listener;


    public CodeListAdapter(Context context, List<Code> codeList, FragmentManager fragmentManager) {
        this.codeList = codeList;
        this.context = context;
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
            holder.codeTypeTxt.setText(codeList.get(position).getCodeType());
//            holder.userCodeTxt.setText(context.getResources().getText(R.string.user_code_txt) + " "+codeList.get(position).getUserCode());
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
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.nav_host_fragment, CodeAddFragment.newInstance(codeList.get(position)),"CodeAddFragment")
                                            .commit();
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
        TextView codeTextViewOptions, codeTxt, codeTypeTxt, codeDescriptionTxt;


        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            codeTextViewOptions = itemView.findViewById(R.id.codeTxtViewOptions);
            codeTxt = itemView.findViewById(R.id.code_txt);
            codeTypeTxt = itemView.findViewById(R.id.codeType_txt);
//            userCodeTxt = itemView.findViewById(R.id.userCode_txt);
            codeDescriptionTxt = itemView.findViewById(R.id.codeDescription);

        }
    }
}
