package technician.ifb.com.ifptecnician.stock;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.MyViewHolder> implements Filterable{

    private List<ReturnModel> stocklist;
    private List<ReturnModel> stocklistFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private MystockAdapter.JobsAdapterListener listener;
    String searchString = "";
    boolean return_status,edit_mode;
    int pos;
    private MystockAdapter.Ruturnclick ruturnclick;
    String type="all";

    public ReturnAdapter(Context context, List<ReturnModel> stocklist) {
        this.context = context;
        this.stocklist = stocklist;
        this.stocklistFiltered=stocklist;

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
                .inflate(R.layout.item_return_stock, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ReturnModel dataSets=stocklist.get(position);

        holder.tv_name.setText(dataSets.getItemName());
        holder.tv_componemt.setText(dataSets.getItemId());
        holder.tv_qty.setText(dataSets.getQuantity());
        holder.tv_amount.setText(dataSets.getIssuedQty());
        holder.tv_item_remark.setText(dataSets.getRemark());
        holder.tv_item_reqdate.setText(dataSets.getReqDate());

        System.out.println(""+dataSets.getIssuedQty()+"   "+dataSets.getQuantity());

        String status=dataSets.getStatus();
        if (status.equals("ACCEPTED")){
            
            holder.tv_item_status.setTextColor(Color.GREEN);
        }
        else {
            holder.tv_item_status.setTextColor(Color.RED);

        }
        holder.tv_item_status.setText(dataSets.getStatus());

        String name =dataSets.getItemName().toLowerCase(Locale.getDefault());
        String Component=dataSets.getItemId().toLowerCase(Locale.getDefault());
        String qty=dataSets.getQuantity();//.toLowerCase(Locale.getDefault());
        String MaterialCategory=dataSets.getPrice().toLowerCase(Locale.getDefault());
//
        if (name.contains(searchString)) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_name.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_name.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_name.setText(dataSets.getItemName());
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
                holder.tv_componemt.setText(dataSets.getItemId());
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



    }

    @Override
    public int getItemCount() {
        return stocklist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView name, phone;
//        public ImageView thumbnail;

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }
        //tv_servicetype,
        TextView tv_name,tv_qty,tv_componemt,tv_category,tv_amount,tv_item_remark,tv_item_status,tv_item_reqdate;
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
            tv_item_remark=view.findViewById(R.id.tv_item_remark);
            tv_item_status=view.findViewById(R.id.tv_item_status);
            tv_item_reqdate=view.findViewById(R.id.tv_item_reqdate);
            checkBox=view.findViewById(R.id.item_check);

            ll_return=view.findViewById(R.id.ll_return);
            item_ll_check=view.findViewById(R.id.item_ll_check);

        }

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
                    List<ReturnModel> filteredList = new ArrayList<>();
                    for (ReturnModel row : stocklist) {

                        if (row.getItemName().toLowerCase().contains(charString.toLowerCase()) || row.getItemId().contains(charSequence) ||
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
                stocklistFiltered = (ArrayList<ReturnModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface Ruturnclick{

        void onReturnclick(int position,int number,String name);
    }

}
