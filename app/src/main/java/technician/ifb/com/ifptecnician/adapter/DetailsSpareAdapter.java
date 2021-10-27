package technician.ifb.com.ifptecnician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.model.Details_spare_model;

public class DetailsSpareAdapter extends BaseAdapter {

    private ArrayList<Details_spare_model> dataSet;
    private ArrayList<Details_spare_model> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;


    public DetailsSpareAdapter(ArrayList<Details_spare_model> dataSet, Context context) {
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
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
            convertView=layoutInflater.inflate(R.layout.item_details_sparelist,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Details_spare_model dataSets=dataSet.get(position);

        holder.tv_partname.setText(dataSets.getItemname());
        holder.tv_partcode.setText(dataSets.getDescription());
        holder.tv_quentity.setText(dataSets.getCount());
        holder.tv_spare_not_found.setText(dataSets.getFlag());



//        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (holder.aSwitch.isChecked()){
//                    dataSet.get(position).setCheck("");
//                    dataSets.setFlag("");
//                    notifyDataSetChanged();
//                }
//                else{
//
//                    dataSet.get(position).setCheck("1");
//                    dataSets.setFlag("I");
//                    notifyDataSetChanged();
//                }
//            }
//        });

//        System.out.println("checkvalue-->"+dataSets.getCheck());
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_partname,tv_partcode,tv_quentity,tv_spare_not_found;

        public ViewHolder(View view){

            // tv_materialcategory=(TextView)view.findViewById(R.id.tv_materialcategory);
            tv_partname=(TextView)view.findViewById(R.id.tv_part_name);
            tv_partcode=(TextView)view.findViewById(R.id.tv_part_code);
            tv_quentity=(TextView)view.findViewById(R.id.tv_par_quentity);
            tv_spare_not_found=view.findViewById(R.id.tv_spare_not_found);

        }
    }


}


