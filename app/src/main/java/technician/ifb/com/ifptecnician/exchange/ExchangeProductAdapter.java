package technician.ifb.com.ifptecnician.exchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;

public class ExchangeProductAdapter extends BaseAdapter {

        private ArrayList<ExchangeProductModel> dataSet;

        Context mContext;
        LayoutInflater layoutInflater;


    public ExchangeProductAdapter(ArrayList<ExchangeProductModel> dataSet, Context context) {
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
                convertView=layoutInflater.inflate(R.layout.item_exchange_product,parent,false);
                holder=new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder=(ViewHolder)convertView.getTag();
            }

            final ExchangeProductModel dataSets=dataSet.get(position);

            holder.tv_productname.setText(dataSets.getProduct());
            holder.tv_model_name.setText(dataSets.getModel());

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
            TextView tv_productname,tv_model_name;
            Button btn_delete;
            public ViewHolder(View view){


                tv_productname=(TextView)view.findViewById(R.id.item_ex_tv_product);
                tv_model_name=(TextView)view.findViewById(R.id.item_ex_tv_model);
                btn_delete=(Button)view.findViewById(R.id.item_ex_btn_delete);
            }
        }

    }

