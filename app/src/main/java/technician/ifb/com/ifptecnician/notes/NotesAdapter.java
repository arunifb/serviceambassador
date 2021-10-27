package technician.ifb.com.ifptecnician.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.appointment.Create_Appointment;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> implements Filterable {




    String Ticktno="";
    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
    private List<NotesModel> notesModels;
    private List<NotesModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    private Altclick altclick;
    private Rcnclick rcnclick;

    String searchString = "";
    ErrorDetails errorDetails;
    String dbversion="";

    // call to customer
    private Tasklist_Adapter.Calltocustomer calltocustomer;


    public NotesAdapter(Context context, List<NotesModel> notesModels) {
        this.context = context;
        this.notesModels = notesModels;
        taskListFiltered=notesModels;
        inflater=LayoutInflater.from(context);
        dbhelper=new Dbhelper(context);
        prefdetails=context.getSharedPreferences("details",0);
        errorDetails=new ErrorDetails();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final NotesModel dataSets=taskListFiltered.get(position);

        holder.tv_notes.setText(dataSets.getRemark());
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


        TextView tv_notes;

        public MyViewHolder(View view) {
            super(view);


            tv_notes=view.findViewById(R.id.item_tv_notes);

        }
    }


    public interface Calltocustomer{

        void Onclickcall(String mobile,String ticketno);

    }


    public interface JobsAdapterListener {
        void onTaskSelected(TasklistModel tasklistModel);

    }

    public interface Altclick{

        void onAltclick(String refno,String smstype,String altno,String ticketno);
    }

    public interface Call{

        void onAltclick(String refno,String smstype,String altno,String ticketno);
    }


    public interface Rcnclick{

        void onRcnclick(String refno,String smstype,String altno,String ticketno);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    taskListFiltered = notesModels;

                } else {
                    //searchString=charString;
                    List<NotesModel> filteredList = new ArrayList<>();
                    for (NotesModel row : notesModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRemark().toLowerCase().contains(charString.toLowerCase()))
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
                taskListFiltered = (ArrayList<NotesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}





