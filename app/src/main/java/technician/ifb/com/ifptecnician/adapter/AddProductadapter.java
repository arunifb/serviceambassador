package technician.ifb.com.ifptecnician.adapter;

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
import technician.ifb.com.ifptecnician.fragment.dummy.SalesModel;
import technician.ifb.com.ifptecnician.model.Add_Product_Model;
import technician.ifb.com.ifptecnician.model.Add_item_model;

public class AddProductadapter extends BaseAdapter  {

    private ArrayList<Add_Product_Model> dataSet;

    Context mContext;
    LayoutInflater layoutInflater;


    public AddProductadapter(ArrayList<Add_Product_Model> dataSet, Context context) {
        // super(context, R.layout.item_leads_list, data);
        this.dataSet = dataSet;
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
            convertView=layoutInflater.inflate(R.layout.item_product,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Add_Product_Model dataSets=dataSet.get(position);
        System.out.println(dataSets.getMachine());

        holder.tv_productname.setText(dataSets.getProduct());
        holder.tv_machine_name.setText(dataSets.getMachine());
        holder.tv_year.setText(dataSets.getYear());


        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataSet.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_productname,tv_machine_name,tv_year;
        Button btn_delete;
        public ViewHolder(View view){

            // tv_materialcategory=(TextView)view.findViewById(R.id.tv_materialcategory);

            tv_productname=(TextView)view.findViewById(R.id.itemproduct_tv_product_name);
            tv_machine_name=(TextView)view.findViewById(R.id.itemproduct_tv_machine_name);
            tv_year=(TextView)view.findViewById(R.id.itemproduct_tv_year);
            btn_delete=(Button)view.findViewById(R.id.itemproduct_btn_delete);
        }
    }

}


