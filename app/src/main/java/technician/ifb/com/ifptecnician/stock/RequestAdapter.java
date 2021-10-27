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

public class RequestAdapter
        extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> implements Filterable{

    private List<RequestModel> stocklist;
    private List<RequestModel> stocklistFiltered;
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
    private Assignclick assignclick;

    public RequestAdapter(Context context, List<RequestModel> stocklist,Assignclick assignclick) {
        this.context = context;
        this.stocklist = stocklist;
        this.stocklistFiltered=stocklist;
        this.assignclick=assignclick;
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
                .inflate(R.layout.item_request_stock, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final RequestModel dataSets=stocklist.get(position);

        holder.tv_name.setText(dataSets.getItemName());
        holder.tv_componemt.setText(dataSets.getItemId());
        holder.tv_qty.setText(dataSets.getQuantity());
//        holder.tv_amount.setText(dataSets.getPrice());

        holder.tv_item_remark.setText(dataSets.getRemark());
        holder.tv_item_status.setText(dataSets.getStatus());
        String status=dataSets.getStatus();
        holder.tv_issue_qty.setText(dataSets.getIssuedQty());
        holder.tv_item_date.setText(dataSets.getReqDate());

        if (status.equals("ACCEPTED")){

            holder.tv_item_status.setTextColor(Color.GREEN);
            holder.tv_item_accept.setVisibility(View.GONE);
        }
        else if(status.equals("ALLOWED")){

            holder.tv_item_status.setTextColor(Color.BLUE);
            holder.tv_item_accept.setVisibility(View.VISIBLE);
        }
        else {
            holder.tv_item_status.setTextColor(Color.RED);
            holder.tv_item_accept.setVisibility(View.GONE);

        }
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
        TextView tv_name,tv_qty,tv_componemt,tv_category,tv_amount,tv_item_remark,tv_item_status,
                tv_item_accept,tv_item_date,tv_issue_qty;
        LinearLayout ll_return;
        LinearLayout item_ll_check;


        CardView lead_cv;


        public MyViewHolder(View view) {
            super(view);
            tv_name=view.findViewById(R.id.tv_item_stock_name);
            tv_qty=view.findViewById(R.id.tv_item_stock_qty);
            tv_componemt=view.findViewById(R.id.tv_item_stock_componemt);
            tv_category=view.findViewById(R.id.tv_item_stock_materialcategory);
            tv_amount=view.findViewById(R.id.tv_item_stock_amount);
            tv_item_remark=view.findViewById(R.id.tv_item_remark);
            tv_item_status=view.findViewById(R.id.tv_item_status);
            tv_item_accept=view.findViewById(R.id.tv_item_accept);

            tv_item_date=view.findViewById(R.id.tv_item_date);
            tv_issue_qty=view.findViewById(R.id.tv_issue_qty);


            tv_item_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assignclick.onassignclick(getLayoutPosition());
                }
            });
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
                    List<RequestModel> filteredList = new ArrayList<>();
                    for (RequestModel row : stocklist) {

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
                stocklistFiltered = (ArrayList<RequestModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface Ruturnclick{

        void onReturnclick(int position,int number,String name);
    }

    public interface Assignclick{

        void onassignclick(int position);
    }

}
