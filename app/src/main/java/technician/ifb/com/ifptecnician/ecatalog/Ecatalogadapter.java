package technician.ifb.com.ifptecnician.ecatalog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.exchange.ExchangeMain;
import technician.ifb.com.ifptecnician.exchange.Webview.Viewifbpoint;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;

public class Ecatalogadapter extends RecyclerView.Adapter<Ecatalogadapter.MyViewHolder> {

    private View.OnClickListener mOnItemClickListener;
    SharedPreferences prefdetails;

    private List<Ecatalogmodel> dataSet;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;


    public Ecatalogadapter(Context context,List<Ecatalogmodel> dataSet){
        this.context=context;
        this.dataSet=dataSet;
        inflater=LayoutInflater.from(context);
        prefdetails=context.getSharedPreferences("details",0);
    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=inflater.inflate(R.layout.item_ecatagory, viewGroup, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, int i) {
        final Ecatalogmodel dataSets=dataSet.get(i);
//
        holder.tv_name.setText(dataSets.Name);




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name;

        LinearLayout item_ll_go;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name=(TextView)view.findViewById(R.id.tv_name);
            item_ll_go=view.findViewById(R.id.item_ll_go);
            item_ll_go.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

//            if (v==task_cv){
//
//                System.out.println(getAdapterPosition());
//                final Ecatalogmodel dataSets=dataSet.get(getAdapterPosition());
//                System.out.println(dataSets);
//                Gson gson = new Gson();
//                String json = gson.toJson(dataSets);
//                System.out.println(json);
//                SharedPreferences.Editor editor=prefdetails.edit();
//                editor.putString("details",json);
//                editor.apply();
//                //  context.startActivity(new Intent(context,CustomerDetailsActivity.class));
//                context.startActivity(new Intent(context, NegativeResponseDetails.class));
//            }
//
            if (v==item_ll_go){

//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataSets.getRCNNo()));// Initiates the Intent
//                 context.startActivity(intent);
                final Ecatalogmodel dataSets=dataSet.get(getAdapterPosition());
                context. startActivity(new Intent(context, PdfView.class)
                        .putExtra("url",dataSets.PageUrl)
                        .putExtra("name",dataSets.Name));
            }
        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<Ecatalogmodel> list_task){
        this.dataSet=list_task;
        notifyItemRangeChanged(0,list_task.size());

    }
}
