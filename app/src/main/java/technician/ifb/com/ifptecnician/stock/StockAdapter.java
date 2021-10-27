package technician.ifb.com.ifptecnician.stock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> implements Filterable {


    private List<StockModel> stocklist;

    private List<StockModel> stocklistFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    String searchString = "";

    private Assignclick assignclick;

    public StockAdapter(Context context, List<StockModel> stocklist,Assignclick assignclick) {
        this.context = context;
        this.stocklist = stocklist;
        this.assignclick=assignclick;
        stocklistFiltered=stocklist;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final StockModel dataSets=stocklistFiltered.get(position);

        holder.tv_name.setText(dataSets.getFGDescription());
        holder.tv_des.setText(dataSets.getComponentDescription());
        holder.tv_componemt.setText(dataSets.getComponent());
        holder.tv_product.setText(dataSets.getFGProduct());
        holder.tv_category.setText(dataSets.getMaterialCategory());
        holder.tv_qty.setText(dataSets.getGood_stock());
        holder.tv_item_refurbished_stock_qty.setText(dataSets.getRefurbished_stock());

        String name =dataSets.getFGDescription().toLowerCase(Locale.getDefault());
        String des =dataSets.getComponentDescription().toLowerCase(Locale.getDefault());
        String Component=dataSets.getComponent().toLowerCase(Locale.getDefault());
        String FGProduct=dataSets.getFGProduct().toLowerCase(Locale.getDefault());
        String qty=dataSets.getGood_stock().toLowerCase(Locale.getDefault());
        String MaterialCategory=dataSets.getMaterialCategory().toLowerCase(Locale.getDefault());



        if (des.contains(searchString)) {
            int startPos = des.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_des.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_des.setText(spanString);
            if (searchString.length() == 0) {
                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_des.setText(dataSets.getComponentDescription());
            }
        }

        if (name.contains(searchString)) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_name.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_name.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_name.setText(dataSets.getFGDescription());
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
                holder.tv_componemt.setText(dataSets.getComponent());
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
                holder.tv_qty.setText(dataSets.getGood_stock());
            }
        }

        if (MaterialCategory.contains(searchString)) {
            int startPos = MaterialCategory.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_category.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_category.setText(spanString);
            if (searchString.length() == 0) {
                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_category.setText(dataSets.getMaterialCategory());
            }
        }


        if (FGProduct.contains(searchString)) {
            int startPos = FGProduct.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_product.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_product.setText(spanString);
            if (searchString.length() == 0) {
                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_product.setText(dataSets.getMaterialCategory());
            }
        }




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
        TextView tv_name,tv_qty,tv_componemt,tv_category,tv_des,tv_product,tv_item_refurbished_stock_qty;


        ImageView iv_priority,iv_call;
        CardView lead_cv;
        LinearLayout ll_assign;

        public MyViewHolder(View view) {
            super(view);
            tv_name=view.findViewById(R.id.tv_item_stock_name);
            tv_des=view.findViewById(R.id.tv_item_stock_des);
            tv_qty=view.findViewById(R.id.tv_item_stock_qty);
            tv_componemt=view.findViewById(R.id.tv_item_stock_componemt);
            tv_category=view.findViewById(R.id.tv_item_stock_materialcategory);
            tv_product=view.findViewById(R.id.tv_item_stock_product);
            ll_assign=view.findViewById(R.id.ll_assign);

            tv_item_refurbished_stock_qty=view.findViewById(R.id.tv_item_refurbished_stock_qty);

            ll_assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    assignclick.onassignclick(getLayoutPosition(),Integer.parseInt(stocklist.get(getAdapterPosition()).good_stock),stocklist.get(getAdapterPosition()).ComponentDescription);
                }
            });





        }

    }

    //Setting the arraylist
//    public void setListContent(ArrayList<StockModel> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

    public interface JobsAdapterListener {
        void onTaskSelected(StockModel StockModel);

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
                    List<StockModel> filteredList = new ArrayList<>();
                    for (StockModel row : stocklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getComponent().toLowerCase().contains(charString.toLowerCase()) || row.getComponentDescription().contains(charSequence) ||
                                row.getFGDescription().toLowerCase().contains(charString.toLowerCase()) || row.getGood_stock().toLowerCase().contains(charString.toLowerCase())
                            || row.getFGProduct().toLowerCase().contains(charString.toLowerCase()))
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
                stocklistFiltered = (ArrayList<StockModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface Assignclick{

        void onassignclick(int position,int no,String name);
    }

}
