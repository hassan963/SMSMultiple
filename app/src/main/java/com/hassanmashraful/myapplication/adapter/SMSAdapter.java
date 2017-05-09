package com.hassanmashraful.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hassanmashraful.myapplication.R;
import com.hassanmashraful.myapplication.content.UserInfo;

import java.util.ArrayList;

/**
 * Created by Hassan M.Ashraful on 4/12/2017.
 */

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> implements Filterable{

    private ArrayList<UserInfo> arrayList;
    private Context mcontext;
    private ArrayList<UserInfo> mFilteredList;

    public SMSAdapter(Context context, ArrayList<UserInfo> userInfos){

        this.arrayList = userInfos;
        this.mFilteredList = userInfos;
        this.mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_each_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //UserInfo userInfo = mFilteredList.get(position);
        holder.smsTXT.setText(mFilteredList.get(position).getSms());
        holder.phnTXT.setText(mFilteredList.get(position).getNumber());

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = arrayList;
                } else {

                    ArrayList<UserInfo> filteredList = new ArrayList<>();

                    for (UserInfo userInfo : arrayList) {

                        if (userInfo.getNumber().toLowerCase().contains(charString) || userInfo.getSms().toLowerCase().contains(charString)) {

                            filteredList.add(userInfo);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mFilteredList = (ArrayList<UserInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView smsTXT, phnTXT;
        private ImageView smsIMG, phnIMG;

        public ViewHolder(View itemView) {
            super(itemView);
            smsIMG = (ImageView) itemView.findViewById(R.id.smsIMG);
            phnIMG = (ImageView) itemView.findViewById(R.id.phnIMG);

            phnTXT = (TextView) itemView.findViewById(R.id.phnTXT);
            smsTXT = (TextView) itemView.findViewById(R.id.smsTXT);

        }
    }
}
