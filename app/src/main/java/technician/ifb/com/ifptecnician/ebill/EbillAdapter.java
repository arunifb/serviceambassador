package technician.ifb.com.ifptecnician.ebill;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;

public class EbillAdapter extends RecyclerView.Adapter<EbillAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<EbillModel> editModelArrayList;
    Context ctx;
    private TotalAdapterListener listener;

    public EbillAdapter(Context ctx, ArrayList<EbillModel> editModelArrayList,TotalAdapterListener listener){

        this.ctx=ctx;
        inflater = LayoutInflater.from(ctx);
        this.listener=listener;
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_ebill, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_productname.setText(editModelArrayList.get(position).getName());
        holder.tv_quentity.setText(editModelArrayList.get(position).getQty());
        if (editModelArrayList.get(position).getHeader().length()==0){
            holder.tv_header.setVisibility(View.GONE);
        }
        else{
            holder.tv_header.setText(editModelArrayList.get(position).getHeader());
        }
        holder.tv_item_code.setText(editModelArrayList.get(position).getCode());


        holder.tv_item_baseprice.setText(editModelArrayList.get(position).getBase_amount());

        holder.tv_item_net_amount.setText(editModelArrayList.get(position).getNet_amount());
        holder.tv_item_taxamount.setText(editModelArrayList.get(position).getTax_amount());
        holder.et_item_productrate.setText(editModelArrayList.get(position).getGross_amount());


    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView editText;
        TextView tv_productname,tv_quentity,tv_header,tv_item_code,tv_item_baseprice,tv_item_net_amount,
        tv_item_taxamount,et_item_productrate;


        public MyViewHolder(View itemView) {
            super(itemView);

            editText =  itemView.findViewById(R.id.et_item_productrate);
            tv_productname=  itemView.findViewById(R.id.tv_item_productname);
            tv_quentity= itemView.findViewById(R.id.tv_item_productunit);
            tv_header=itemView.findViewById(R.id.tv_header);
            tv_item_code=itemView.findViewById(R.id.tv_item_code);

            tv_item_baseprice=itemView.findViewById(R.id.tv_item_baseprice);

            tv_item_net_amount=itemView.findViewById(R.id.tv_item_net_amount);
            tv_item_taxamount=itemView.findViewById(R.id.tv_item_taxamount);
            et_item_productrate=itemView.findViewById(R.id.et_item_productrate);


//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    //  editModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    String price = editText.getText().toString();
////                    //  String qty = quantity.getText().toString();
////                    Intent intent = new Intent("custom-message");
////                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
////                    //  intent.putExtra("quantity",qty);
////                    intent.putExtra("item",ItemName);
////                    LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
//
//
//
//                    if (price.length() > 0){
//
//                        editModelArrayList.get(getAdapterPosition()).setAmount(price);
//                        listener.onContactSelected(editModelArrayList.get(getAdapterPosition()));
//
//                    }
//
//                    else {
//
//                        editModelArrayList.get(getAdapterPosition()).setAmount("0");
//                        listener.onContactSelected(editModelArrayList.get(getAdapterPosition()));
//                    }
//
//
//                }
//            });

        }

    }

    public interface TotalAdapterListener {
        void onContactSelected(EbillModel ebillModel);

    }

}