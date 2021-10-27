//package technician.ifb.com.ifptecnician.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.net.Uri;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.text.Spannable;
//import android.text.style.ForegroundColorSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
//import technician.ifb.com.ifptecnician.R;
//import technician.ifb.com.ifptecnician.model.Add_Product_Model;
//import technician.ifb.com.ifptecnician.model.TasklistModel;
//
//public class Add_Product_Adapter extends RecyclerView.Adapter<Add_Product_Adapter.MyViewHolder> {
//
//
//    SharedPreferences prefdetails;
////    private ArrayList<TasklistModel> dataSet=new ArrayList<>();
//
//    private List<Add_Product_Model> product_models;
//
//    private final LayoutInflater inflater;
//    View view;
//    MyViewHolder holder;
//    private Context context;
//    private JobsAdapterListener listener;
//    String searchString = "";
//
//    public Add_Product_Adapter(Context context, List<Add_Product_Model> product_models) {
//        this.context = context;
//        this.product_models = product_models;
//        inflater=LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_product, parent, false);
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//        final Add_Product_Model dataSets=product_models.get(position);
//
//
//        holder.tv_productname.setText(dataSets.getProduct());
//        holder.tv_machine_name.setText(dataSets.getMachine());
//        holder.tv_year.setText(dataSets.getYear());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return product_models.size();
//    }
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
////        public TextView name, phone;
////        public ImageView thumbnail;
//
//        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
//
//        public ForegroundColorSpan getForegroundColorSpan(){
//            return foregroundColorSpan;
//        }
//
//        TextView tv_productname,tv_machine_name,tv_year;
//
//
//        public MyViewHolder(View view) {
//            super(view);
//            tv_productname=(TextView)view.findViewById(R.id.tv_productname);
//            tv_machine_name=(TextView)view.findViewById(R.id.tv_machine_name);
//            tv_year=(TextView)view.findViewById(R.id.tv_year);
//
//        }
//    }
//
//    public interface JobsAdapterListener {
//        void onTaskSelected(TasklistModel tasklistModel);
//    }
//
//
//}
//
