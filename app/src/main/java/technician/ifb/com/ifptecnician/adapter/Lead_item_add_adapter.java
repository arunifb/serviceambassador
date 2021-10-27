package technician.ifb.com.ifptecnician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.model.Lead_item_add_model;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;


public class Lead_item_add_adapter extends BaseAdapter implements Filterable ,
        NumberPicker.OnValueChangeListener{

    private ArrayList<Lead_item_add_model> dataSet;
    private ArrayList<Lead_item_add_model> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;
    private ContactsFilter contactsFilter;

    int pos,sqty,current_qty;

    public Lead_item_add_adapter(ArrayList<Lead_item_add_model> dataSet, Context context) {
        // super(context, R.layout.item_leads_list, data);
        this.dataSet = dataSet;
        this.contactsSetgets=dataSet;
        this.mContext=context;
        layoutInflater=LayoutInflater.from(this.mContext);

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.item_add_essential,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Lead_item_add_model dataSets=dataSet.get(position);

        holder.tv_ename.setText(dataSets.getEname());
        holder.tv_ecode.setText(dataSets.getEcode());
        holder.tv_equentity.setText(dataSets.getEquentity());
        holder.tv_itemtype.setText(dataSets.getItemtype());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.tv_equentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                sqty=dataSets.getSqty();
                showNumberPicker(v);
            }
        });

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_ename,tv_ecode,tv_equentity,tv_itemtype;
        Button btn_delete;
        public ViewHolder(View view){

            tv_ename=(TextView)view.findViewById(R.id.tv_ess_comdes);
            tv_ecode=(TextView)view.findViewById(R.id.tv_ess_com);
            tv_equentity=(TextView)view.findViewById(R.id.tv_ess_quentity);
            btn_delete=(Button) view.findViewById(R.id.btn_ess_delete);
            tv_itemtype=view.findViewById(R.id.tv_itemtype);

        }



    }

    @Override
    public Filter getFilter() {

        if ( contactsFilter == null)
        {
            contactsFilter=new ContactsFilter();
        }
        return contactsFilter;
    }

    public class ContactsFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults=new FilterResults();

            if(constraint!=null && constraint.length()>0)
            {
                ArrayList<Lead_item_add_model> templist=new ArrayList<>();

                for(Lead_item_add_model contactsSetget: dataSet){

                    if(contactsSetget.getEname().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        templist.add(contactsSetget);
                    }
                }

                filterResults.count=templist.size();
                filterResults.values=templist;
            }
            else {

                filterResults.count=dataSet.size();
                filterResults.values=dataSet;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            contactsSetgets = (ArrayList<Lead_item_add_model>) results.values;
            notifyDataSetChanged();
        }
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        if (picker.getValue() <= sqty) {


           String  quentity = String.valueOf(picker.getValue());
            dataSet.get(pos).setEquentity(quentity);
            notifyDataSetChanged();

        }

        else {

           Alert alert=new Alert();

           alert.showDialog(mContext,"Stock Alert !!","Total Stock :"+sqty);
        }


    }

    public void showNumberPicker(View view) {
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show( ((AppCompatActivity)mContext).getSupportFragmentManager(), "time picker");
    }

}


