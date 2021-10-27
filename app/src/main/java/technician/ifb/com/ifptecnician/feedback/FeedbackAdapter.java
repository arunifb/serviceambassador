package technician.ifb.com.ifptecnician.feedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;


public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private View.OnClickListener mOnItemClickListener;
    SharedPreferences prefdetails;
    private List<Feedbackmodel> dataSet;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;


    public FeedbackAdapter(Context context,List<Feedbackmodel> dataSet){
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
        view=inflater.inflate(R.layout.item_feedback, viewGroup, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, int i) {
        final Feedbackmodel dataSets=dataSet.get(i);
//
        holder.tv_title.setText(dataSets.Title);
        holder.tv_des.setText(dataSets.Description);

        holder.labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if (isOn){
                    dataSet.get(i).setQuestionId("1");

                }

                else {
                    dataSet.get(i).setQuestionId("0");
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_title,tv_des;
        LabeledSwitch labeledSwitch;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title=(TextView)view.findViewById(R.id.item_title);
            tv_des=view.findViewById(R.id.item_des);
            labeledSwitch=view.findViewById(R.id.item_switch);


        }

        @Override
        public void onClick(View v) {

//
//            if (v==item_ll_go){
//
////                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataSets.getRCNNo()));// Initiates the Intent
////                 context.startActivity(intent);
//                final Ecatalogmodel dataSets=dataSet.get(getAdapterPosition());
//                context. startActivity(new Intent(context, PdfView.class)
//                        .putExtra("url",dataSets.PageUrl)
//                        .putExtra("name",dataSets.Name));
//            }
        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<Feedbackmodel> list_task){
        this.dataSet=list_task;
        notifyItemRangeChanged(0,list_task.size());
    }
}

