package technician.ifb.com.ifptecnician.adapter;

import android.app.Dialog;
import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;
import technician.ifb.com.ifptecnician.fragment.adapter.SalesAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.SalesModel;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;

public class Add_item_adapter extends BaseAdapter implements Filterable, NumberPicker.OnValueChangeListener{

    private ArrayList<Add_item_model> dataSet;
    private ArrayList<Add_item_model> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;
    private ContactsFilter contactsFilter;
    String quentity="0";
    String pen="";
    int pos,sqty,current_qty,price;
    private Deleteclick deleteclick;
    private QuentityClick quentityClick;


    public Add_item_adapter(ArrayList<Add_item_model> dataSet, Context context,Deleteclick deleteclick,QuentityClick quentityClick) {
        // super(context, R.layout.item_leads_list, data);
        this.dataSet = dataSet;
        this.contactsSetgets=dataSet;
        this.mContext=context;
        this.deleteclick=deleteclick;
        this.quentityClick=quentityClick;
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
            convertView=layoutInflater.inflate(R.layout.item_add_view,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

          final Add_item_model dataSets=dataSet.get(position);

          holder.tv_partname.setText(dataSets.getItemname());
          holder.tv_partcode.setText(dataSets.getDescription());
          holder.tv_quentity.setText(dataSets.getCount());
          holder.tv_part_date.setText(dataSets.getDate());



        if (dataSets.getCheck().equals("Yes")){

            holder.tv_part_price.setVisibility(View.GONE);
            holder.tv_price.setVisibility(View.GONE);

        }
        else {

            holder.tv_part_price.setVisibility(View.VISIBLE);
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_part_price.setText("\u20B9 "+dataSets.getTotal_price());
            holder.tv_price.setText("\u20B9 "+dataSets.getPrice()+ "*"+dataSets.getCount());
        }

          holder.tv_quentity.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  pen=dataSets.getCheck();
                  pos=position;
                  sqty=dataSets.getStockqty();
                  price=Integer.parseInt(dataSets.getPrice());
                  showNumberPicker(v);

              }
          });

          holder.tv_spare_not_found.setText(dataSets.getCheck());

        holder.tv_spare_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_qty=Integer.valueOf(dataSets.getCount());
                price=Integer.valueOf(dataSets.getPrice());
                sqty=dataSets.getStockqty();
                pos=position;
                openpopup();

            }
        });

          holder.btn_delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  deleteclick.onDeleteclick(dataSets.getDescription(),dataSets.getItemname(),dataSets.getCount(),dataSets.getCheck());
                  dataSet.remove(position);
                  notifyDataSetChanged();

              }
          });


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
        TextView tv_partname,tv_partcode,tv_quentity,tv_spare_not_found,tv_part_date,tv_part_price,tv_price;
        Button btn_delete;
        public ViewHolder(View view){

           // tv_materialcategory=(TextView)view.findViewById(R.id.tv_materialcategory);
            tv_partname=(TextView)view.findViewById(R.id.tv_part_name);
            tv_partcode=(TextView)view.findViewById(R.id.tv_part_code);
            tv_quentity=(TextView)view.findViewById(R.id.tv_par_quentity);
            btn_delete=(Button)view.findViewById(R.id.btn_delete);
            tv_spare_not_found=view.findViewById(R.id.tv_spare_not_found);
            tv_part_date=view.findViewById(R.id.tv_part_date);
            tv_part_price=view.findViewById(R.id.tv_part_price);
            tv_price=view.findViewById(R.id.tv_price);

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

    public class ContactsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults=new FilterResults();

            if(constraint!=null && constraint.length()>0)
            {
                ArrayList<SalesModel> templist=new ArrayList<>();

                for(Add_item_model contactsSetget: dataSet){

                    if(contactsSetget.getCount().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                       // templist.add(contactsSetget);
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

            contactsSetgets = (ArrayList<Add_item_model>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        if (picker.getValue() <= sqty && pen.equals("No")) {

            quentity = String.valueOf(picker.getValue());
            dataSet.get(pos).setCount(quentity);
            dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));
            notifyDataSetChanged();

          // quentityClick.onQuentityClick();

        }
        else if(picker.getValue() > sqty && pen.equals("Yes")){

            quentity = String.valueOf(picker.getValue());
            dataSet.get(pos).setCount(quentity);

            dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));
            notifyDataSetChanged();
        }

        else if(picker.getValue() > sqty){

              quentity = String.valueOf(picker.getValue());
              dataSet.get(pos).setCount(quentity);
              dataSet.get(pos).setFlag("I");
              dataSet.get(pos).setCheck("Yes");

            dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));

            notifyDataSetChanged();
//            dataSet.get(pos).setCount(quentity);
//            notifyDataSetChanged();
          //  Toast.makeText(mContext, "Stock Mismatch", Toast.LENGTH_SHORT).show();

        }

        else if (picker.getValue() <= sqty){

            quentity = String.valueOf(picker.getValue());
            dataSet.get(pos).setCount(quentity);
            dataSet.get(pos).setFlag("I");
            dataSet.get(pos).setCheck("No");
            dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));
            notifyDataSetChanged();
        }

    }

    public void showNumberPicker(View view) {
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show( ((AppCompatActivity)mContext).getSupportFragmentManager(), "time picker");
    }

    public void  openpopup(){

        final Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.pending_spare_popup);

        Button save=dialog.findViewById(R.id.pop_btn_save);
        Button cancel=dialog.findViewById(R.id.pop_btn_close);
        TextView tv_yes=dialog.findViewById(R.id.pop_tv_yes);
        TextView tv_no=dialog.findViewById(R.id.pop_tv_no);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( sqty < current_qty ){

                    dataSet.get(pos).setCheck("Yes");
                    dataSet.get(pos).setCount(quentity);
                    dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));
                    notifyDataSetChanged();
                    dialog.dismiss();
                }

                else {

                    Toast.makeText(mContext, "Spare Already in stock", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sqty==current_qty || current_qty<sqty){

                    dataSet.get (pos).setCheck("No");
                    dataSet.get(pos).setCount(quentity);
                    dataSet.get(pos).setTotal_price(""+price*Integer.parseInt(quentity));
                    notifyDataSetChanged();
                    dialog.dismiss();
                }

                else
                {
                    Toast.makeText(mContext, "Please check in stock", Toast.LENGTH_SHORT).show();
                }


            }
        });

        dialog.show();

    }


    public interface Deleteclick{

        void onDeleteclick(String code,String name,String qty,String status);
    }


    public interface QuentityClick{

        void onQuentityClick(String code,String name,String qty,String status);
    }


}


