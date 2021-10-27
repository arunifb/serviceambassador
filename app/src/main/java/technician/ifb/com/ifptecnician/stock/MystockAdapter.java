package technician.ifb.com.ifptecnician.stock;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;


public class MystockAdapter extends RecyclerView.Adapter<MystockAdapter.MyViewHolder> implements Filterable,
        NumberPicker.OnValueChangeListener{

    private List<MystockModel> stocklist;
    private List<MystockModel> stocklistFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    String searchString = "";
    boolean return_status,edit_mode;
    int pos;
    private Ruturnclick ruturnclick;
    String type="all";

    public MystockAdapter(Context context, List<MystockModel> stocklist, List<MystockModel> stocklistFiltered,boolean return_status,Ruturnclick ruturnclick) {
        this.context = context;
        this.stocklist = stocklist;
        this.stocklistFiltered=stocklistFiltered;
        this.return_status=return_status;
        this.ruturnclick=ruturnclick;
        inflater=LayoutInflater.from(context);

    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MystockModel dataSets=stocklist.get(position);

        holder.tv_name.setText(dataSets.getSpareDes());
        holder.tv_componemt.setText(dataSets.getSpareCode());
        holder.tv_qty.setText(dataSets.getQuantity());
        holder.tv_amount.setText(dataSets.getAmount());


        String name =dataSets.getSpareDes().toLowerCase(Locale.getDefault());
        String Component=dataSets.getSpareCode().toLowerCase(Locale.getDefault());
        String qty=dataSets.getQuantity();//.toLowerCase(Locale.getDefault());
        String MaterialCategory=dataSets.getAmount().toLowerCase(Locale.getDefault());
//
        if (name.contains(searchString)) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_name.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_name.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_name.setText(dataSets.getSpareDes());
            }
        }

        if (Component.contains(searchString)) {
            int startPos = Component.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_componemt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_componemt.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_componemt.setText(dataSets.getSpareCode());
            }
        }

        if (qty.contains(searchString)) {
            int startPos = qty.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_qty.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_qty.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_qty.setText(dataSets.getQuantity());
            }
        }

        if (dataSets.isIscheck()){
            holder.checkBox.setChecked(true);
        }
        else{

            holder.checkBox.setChecked(false);
        }



        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                final boolean ischeck = holder.checkBox.isChecked();

                type="multi";
                for (int i=0; i<stocklist.size();i++) {
                    if (isChecked) {

                        stocklist.get(position).setIscheck(true);

                    } else {

                        stocklist.get(position).setIscheck(false);

                    }
                }

            }
        });


        holder.tv_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int number = Integer.parseInt(stocklistFiltered.get(position).getReturnqty());
                showNumberPicker(v,number);
                pos=position;
            }
        });

        if (return_status){



                holder.item_ll_check.setVisibility(View.VISIBLE);

                holder.tv_qty.setClickable(true);
                holder.ll_return.setVisibility(View.GONE);

        }


        else{

            holder.item_ll_check.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
            holder.tv_qty.setClickable(false);
            holder.ll_return.setVisibility(View.VISIBLE);

        }


        holder.ll_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int number = Integer.parseInt(stocklist.get(position).getQuantity());
                String name=stocklist.get(position).SpareDes;
                ruturnclick.onReturnclick(position,number,name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stocklistFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView name, phone;
//        public ImageView thumbnail;

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }
        //tv_servicetype,
        TextView tv_name,tv_qty,tv_componemt,tv_category,tv_amount;
        LinearLayout ll_return;
        LinearLayout item_ll_check;

        ImageView iv_priority,iv_call;
        CardView lead_cv;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            tv_name=view.findViewById(R.id.tv_item_stock_name);
            tv_qty=view.findViewById(R.id.tv_item_stock_qty);
            tv_componemt=view.findViewById(R.id.tv_item_stock_componemt);
            tv_category=view.findViewById(R.id.tv_item_stock_materialcategory);
            tv_amount=view.findViewById(R.id.tv_item_stock_amount);
            checkBox=view.findViewById(R.id.item_check);

            ll_return=view.findViewById(R.id.ll_return);
            item_ll_check=view.findViewById(R.id.item_ll_check);

        }

    }

    //Setting the arraylist
//    public void setListContent(ArrayList<MystockModel> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

    public void showNumberPicker(View view,int qty) {
        SelectQuntity newFragment = new SelectQuntity();
        newFragment.setValueChangeListener(this,qty);
        newFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), "Quantity Picker");
    }

//    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
//        this.valueChangeListener = valueChangeListener;
//    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        stocklist.get(pos).setQuantity(""+newVal);
        stocklist.get(pos).setIscheck(true);
        notifyDataSetChanged();

    }


    public interface JobsAdapterListener {
        void onTaskSelected(MystockModel MystockModel);

    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    stocklistFiltered = stocklist;

                } else {
                    //searchString=charString;
                    List<MystockModel> filteredList = new ArrayList<>();
                    for (MystockModel row : stocklist) {

                        if (row.getSpareDes().toLowerCase().contains(charString.toLowerCase()) || row.getSpareCode().contains(charSequence) ||
                                row.getQuantity().toLowerCase().contains(charString.toLowerCase()) || row.getQuantity().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }

                    stocklistFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stocklistFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stocklistFiltered = (ArrayList<MystockModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public void selectAll(){
        return_status=true;

        for (int i=0;i<stocklist.size();i++){

            stocklist.get(i).setIscheck(true);

        }

        notifyDataSetChanged();

    }
    public void unselectall(){
        return_status=false;
        notifyDataSetChanged();
    }


    public interface Ruturnclick{

        void onReturnclick(int position,int number,String name);
    }

}

