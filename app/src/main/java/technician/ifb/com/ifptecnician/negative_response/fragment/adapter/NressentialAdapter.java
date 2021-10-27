package technician.ifb.com.ifptecnician.negative_response.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.negative_response.fragment.model.NressentialModel;

public class NressentialAdapter extends BaseAdapter  {

    private ArrayList<NressentialModel> dataSet;
    private ArrayList<NressentialModel> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;

   

    public NressentialAdapter(ArrayList<NressentialModel> dataSet, Context context) {
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

        final NressentialModel dataSets=dataSet.get(position);

        holder.tv_ename.setText(dataSets.getEname());
        holder.tv_ecode.setText(dataSets.getEcode());
        holder.tv_equentity.setText(dataSets.getEquentity());
        holder.tv_itemtype.setText(dataSets.getItemtype());



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
            btn_delete.setVisibility(View.GONE);
            tv_itemtype=view.findViewById(R.id.tv_itemtype);

        }
    }

}

