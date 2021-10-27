package technician.ifb.com.ifptecnician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.essentialdetails.EssentialItemClick;
import technician.ifb.com.ifptecnician.essentiallead.essentialmodel.EssentialList;

public class EssentialListAdapter extends RecyclerView.Adapter<EssentialListAdapter.ViewHolder>{

    private Context mContext;
    private List<EssentialList>  essentialList;
    private EssentialItemClick ItemClick;


    public EssentialListAdapter(Context context, List<EssentialList> essentialLists, EssentialItemClick essentialItemClick) {

         this.mContext = context;
         this.essentialList = essentialLists;
         this.ItemClick = essentialItemClick;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.essential_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EssentialList essentialItem = essentialList.get(position);

        holder.tv_custname.setText(essentialItem.getSoldToPartyList());
        holder.tv_calltype.setText(essentialItem.getProcessTypeTxt());
        holder.tv_address.setText(essentialItem.getAddress());
        holder.tv_customercode.setText(essentialItem.getSoldToParty());
        holder.tv_customer_no.setText(essentialItem.getCallerNo());
        holder.tv_status.setText(essentialItem.getConcatstatuser());
        holder.tv_zzmatgrp.setText(essentialItem.getZzmatGrp());
        holder.tv_zzproduct_desc.setText(essentialItem.getZzproductDesc());

        if (essentialItem.geteMail()!= null || !essentialItem.geteMail().equals("")) {

            holder.tv_email.setVisibility(View.VISIBLE);
            holder.tv_email.setText(essentialItem.geteMail());

        }else {

            holder.tv_email.setVisibility(View.GONE);
        }

        holder.task_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ItemClick!= null){

                    ItemClick.OnEssentialItemClick(view, position);
                }


            }
        });




    }

    @Override
    public int getItemCount() {
        return essentialList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_calltype,tv_customercode,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,
                tv_customer_no,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt, tv_email,tv_zzmatgrp,tv_zzproduct_desc;
        ImageView iv_call,iv_rcn,iv_alt,iv_thum,iv_calender;
        LinearLayout ll_alt,item_task_ll_medium;
        CardView task_cv;

        public ViewHolder(@NonNull View view) {
            super(view);

            tv_calltype=(TextView)view.findViewById(R.id.tv_calltype);
            tv_customercode=(TextView)view.findViewById(R.id.tv_customercode);
            tv_address=(TextView)view.findViewById(R.id.tv_address);
            tv_callbook=(TextView)view.findViewById(R.id.tv_callbookdate);
            tv_status=(TextView)view.findViewById(R.id.tv_status);
            tv_customer_no=(TextView)view.findViewById(R.id.tv_customer_no);
            tv_servicetype=(TextView)view.findViewById(R.id.tv_servicetype);
            tv_custname=(TextView)view.findViewById(R.id.tv_custname);
            iv_call=(ImageView)view.findViewById(R.id.iv_call);
            task_cv=(CardView)view.findViewById(R.id.task_cv);
            tv_ageing=view.findViewById(R.id.tv_ageing);
            tv_escaltion=view.findViewById(R.id.tv_escaltion);
            tv_problemdescription=view.findViewById(R.id.tv_problemdescription);
            iv_alt=view.findViewById(R.id.alt_iv_message);
            tv_alt=view.findViewById(R.id.tv_altno);
            iv_rcn=view.findViewById(R.id.rcn_iv_message);
            ll_alt=view.findViewById(R.id.ll_altview);
            iv_thum=view.findViewById(R.id.iv_thum);
            tv_scfeedbackcatvalue=view.findViewById(R.id.tv_scfeedbackcatvalue);

            item_task_ll_medium=view.findViewById(R.id.item_task_ll_medium);

            iv_calender=view.findViewById(R.id.iv_calender);
            tv_email = view.findViewById(R.id.tv_email);
            tv_zzmatgrp = view.findViewById(R.id.tv_zzmatgrp);
            tv_zzproduct_desc = view.findViewById(R.id.tv_zzproduct_desc);



        }


    }
}
