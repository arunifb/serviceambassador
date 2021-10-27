package technician.ifb.com.ifptecnician.adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.model.Shelflifemodel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;


public class ShelflifeAdapter extends BaseAdapter {

    private ArrayList<Shelflifemodel> dataSet;
    private ArrayList<Shelflifemodel> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;

    public ShelflifeAdapter(ArrayList<Shelflifemodel> dataSet, Context context) {
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
            convertView=layoutInflater.inflate(R.layout.item_shelflife,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Shelflifemodel dataSets=dataSet.get(position);


        holder.tv_esential.setText(dataSets.getName());
        holder.tv_qty.setText(dataSets.getQty());

        holder.tv_date.setText(dataSets.getShelflife());

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_esential,tv_qty,tv_date;

        public ViewHolder(View view){

            tv_esential=(TextView)view.findViewById(R.id.item_sl_essential);
            tv_qty=(TextView)view.findViewById(R.id.item_sl_qty);
            tv_date=(TextView)view.findViewById(R.id.item_sl_date);

        }
    }

}
