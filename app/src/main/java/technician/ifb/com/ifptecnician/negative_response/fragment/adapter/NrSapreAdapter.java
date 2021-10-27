package technician.ifb.com.ifptecnician.negative_response.fragment.adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Add_item_adapter;
import technician.ifb.com.ifptecnician.negative_response.fragment.model.NrSpareModel;


public class NrSapreAdapter extends BaseAdapter {

    private ArrayList<NrSpareModel> dataSet;
    int[] numValues;
    Context mContext;
    LayoutInflater layoutInflater;
    private Add_item_adapter.ContactsFilter contactsFilter;


    public NrSapreAdapter(ArrayList<NrSpareModel> dataSet, Context context) {
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
            convertView=layoutInflater.inflate(R.layout.item_add_view,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final NrSpareModel dataSets=dataSet.get(position);

        holder.tv_partname.setText(dataSets.getPartName());
        holder.tv_partcode.setText(dataSets.getPartCode());
        holder.tv_quentity.setText(dataSets.getQty());
        holder.tv_spare_not_found.setText(dataSets.getPending_fla());

        if (dataSets.getFlags().equals("D")){

            holder.added_spare_card.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tv_partname,tv_partcode,tv_quentity,tv_spare_not_found;
        Button btn_delete;
        LinearLayout ll_main;
        CardView added_spare_card;
        public ViewHolder(View view){

            // tv_materialcategory=(TextView)view.findViewById(R.id.tv_materialcategory);

            tv_partname=(TextView)view.findViewById(R.id.tv_part_name);
            tv_partcode=(TextView)view.findViewById(R.id.tv_part_code);
            tv_quentity=(TextView)view.findViewById(R.id.tv_par_quentity);
            btn_delete=(Button)view.findViewById(R.id.btn_delete);
            btn_delete.setVisibility(View.GONE);
            tv_spare_not_found=view.findViewById(R.id.tv_spare_not_found);
            added_spare_card=view.findViewById(R.id.added_spare_card);
            ll_main=view.findViewById(R.id.item_pending_spare_ll_main);
        }
    }

    public int string_to_int(String Numbers){
        int no=0;
        no= Integer.parseInt(Numbers);
        return no;
    }

}




