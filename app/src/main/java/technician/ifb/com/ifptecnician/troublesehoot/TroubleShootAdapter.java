package technician.ifb.com.ifptecnician.troublesehoot;

import android.content.Context;
import android.graphics.Color;

import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.repsly.library.timelineview.LineType;
import com.repsly.library.timelineview.TimelineView;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.troublesehoot.model.TroubleShootModel;

public class TroubleShootAdapter extends RecyclerView.Adapter<TroubleShootAdapter.MyViewHolder>

        implements Filterable {

    private List<TroubleShootModel> tasklist;
    private List<TroubleShootModel> taskListFiltered;
    private final LayoutInflater inflater;

    View view;
    Tasklist_Adapter.MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    private Yesclick yesclick;
    private Noclick noclick;
    String searchString = "";
    ErrorDetails errorDetails;
    String dbversion="";

    public TroubleShootAdapter(Context context, List<TroubleShootModel> tasklist, JobsAdapterListener listener, Yesclick yesclick, Noclick noclick) {
        this.context = context;
        this.listener = listener;
        this.yesclick=yesclick;
        this.noclick=noclick;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_troubleshoot, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final TroubleShootModel dataSets=taskListFiltered.get(position);

        holder.tv_step.setText(dataSets.getStep());

        holder.timelineView.setLineType(getLineType(position));



        // Make first and last markers stroked, others filled
        if (position == 0 || position + 1 == getItemCount()) {
            holder.timelineView.setFillMarker(false);
        } else {
          //  holder.timelineView.setFillMarker(true);

        }


        if (dataSets.getRunning().equals("complete")){


            holder.tv_action.setText(dataSets.getAction());
            holder.tv_check.setText(dataSets.getCheck());
            holder.timelineView.setDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_alarm_on_black_24dp));
            holder.timelineView.setFillMarker(false);
            holder.btn_no.setVisibility(View.VISIBLE);
            holder.btn_yes.setVisibility(View.VISIBLE);
            holder.btn_yes.setBackgroundColor(Color.parseColor("#139E91"));
            holder.btn_yes.setTextColor(Color.WHITE);
            holder.tv_line.setText("");
            holder.iv_imege.setImageDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_check_box_black_24dp));
            holder.ll_yes.setBackgroundResource(R.drawable.yes_btn_trouble);

        }
       else  if (dataSets.getRunning().equals("running")){

            holder.tv_action.setText(dataSets.getAction());
            holder.tv_check.setText(dataSets.getCheck());
            holder.timelineView.setActive(true);
            holder.timelineView.setFillMarker(false);
            holder.btn_no.setVisibility(View.VISIBLE);
            holder.btn_yes.setVisibility(View.VISIBLE);
            holder.iv_imege.setImageDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_check_box_black_24dp));




        }
       else if(dataSets.getRunning().equals("cancel")){
            holder.btn_no.setVisibility(View.VISIBLE);
            holder.btn_yes.setVisibility(View.VISIBLE);

           // holder.tv_status.setBackgroundResource(R.drawable.redcircule);
            holder.timelineView.setFillMarker(false);
            holder.timelineView.setDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_cancel_icon));
            holder.tv_action.setText(dataSets.getAction());
            holder.tv_check.setText(dataSets.getCheck());
            holder.btn_no.setBackgroundColor(Color.GRAY);
            holder.btn_no.setTextColor(Color.BLACK);
            holder.iv_imege.setImageDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_check_box_outline_blank_black_24dp));

            holder.ll_yes.setBackgroundColor(Color.parseColor("#00000000"));
            holder.ll_no.setBackgroundResource(R.drawable.no_btn_troblue);


            holder.iv_imege.setImageDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_cancel_black_24dp));

            // holder.tv_line.setBackgroundColor(Color.parseColor("#00000000"));
            holder.tv_line.setBackgroundColor(Color.parseColor("#b71c1c"));
        }
       else {
          // holder.tv_status.setBackgroundColor(Color.LTGRAY);
          // holder.ll_main.setBackgroundColor(Color.LTGRAY);
            holder.btn_no.setVisibility(View.GONE);
            holder.btn_yes.setVisibility(View.GONE);
           holder.btn_yes.setEnabled(false);
           holder.btn_no.setEnabled(false);
            holder.iv_imege.setImageDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_check_box_outline_blank_black_24dp));

           // holder.tv_line.setBackgroundColor(Color.parseColor("#00000000"));
            holder.tv_line.setBackgroundColor(Color.parseColor("#A9A9A9"));
        }

    }

    @Override
    public int getItemCount() {
        return taskListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }

        TextView tv_action,tv_step,tv_check,tv_status,tv_line;
        ImageView iv_imege;
        TextView btn_yes,btn_no;
        LinearLayout ll_main,ll_yes,ll_no;
        TimelineView timelineView;

        public MyViewHolder(View view) {
            super(view);
            tv_action=view.findViewById(R.id.item_troubleshoot_action);
            tv_step=view.findViewById(R.id.item_troubleshoot_step);
            tv_check=view.findViewById(R.id.item_troubleshoot_check);
            btn_yes=view.findViewById(R.id.item_troubleshoot_btn_yes);
            btn_no=view.findViewById(R.id.item_troubleshoot_btn_no);
            tv_status=view.findViewById(R.id.item_troubleshoot_status);
            ll_main=view.findViewById(R.id.item_troubleshoot_main);
            timelineView=view.findViewById(R.id.timeline);
            iv_imege=view.findViewById(R.id.item_troubleshoot_image);
            tv_line=view.findViewById(R.id.item_troubleshoot_tv_line);
            ll_yes=view.findViewById(R.id.item_troubleshoot_ll_yes);
            ll_no=view.findViewById(R.id.item_troubleshoot_ll_no);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final TroubleShootModel dataSets=taskListFiltered.get(getAdapterPosition());
                    String status=dataSets.getStatus();
                    System.out.println("Status"+status);

                }
            });

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TroubleShootModel tasklistModel=taskListFiltered.get(getAdapterPosition());

                    yesclick.onYesclick(getAdapterPosition());
//                  //  rcnclick.onRcnclick(ref,"",rcn,ticketno);

                }
            });

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    TroubleShootModel tasklistModel=taskListFiltered.get(getAdapterPosition());

                    noclick.onNoclick(getAdapterPosition());

                }
            });


        }
    }


    public interface JobsAdapterListener {
        void onTaskSelected(TasklistModel tasklistModel);

    }

    public interface Yesclick{

        void onYesclick(int pos);
    }

    public interface Noclick{

        void onNoclick(int pos);
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    taskListFiltered = tasklist;

                }
                else {
                    //searchString=charString;
                    List<TroubleShootModel> filteredList = new ArrayList<>();
                    for (TroubleShootModel row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAction().toLowerCase().contains(charString.toLowerCase()) || row.getCheck().contains(charSequence)
                                || row.getStatus().toLowerCase().contains(charString.toLowerCase()) || row.getStep().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }

                    taskListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = taskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                taskListFiltered = (ArrayList<TroubleShootModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private LineType getLineType(int position) {
        if (getItemCount() == 1) {
            return LineType.ONLYONE;

        } else {
            if (position == 0) {
                return LineType.BEGIN;

            } else if (position == getItemCount() - 1) {
                return LineType.END;

            } else {
                return LineType.NORMAL;
            }
        }
    }
}
