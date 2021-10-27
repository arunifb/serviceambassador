package technician.ifb.com.ifptecnician.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.dummy.SalesModel;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.GetBomStock;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;

public class Pending_item_adapter extends BaseAdapter implements NumberPicker.OnValueChangeListener {

    private ArrayList<PendingSpare> dataSet;
    int[] numValues;
    Context mContext;
    LayoutInflater layoutInflater;
    private Add_item_adapter.ContactsFilter contactsFilter;
    List<Integer> hiddenItems = new ArrayList<>();
    String quentity="0";
    int pos;
    Dbhelper dbhelper;
    String partname;
    String pen;
    int sqty,current_qty,price;
    SessionManager sessionManager;
    String FrCode;


    public Pending_item_adapter(ArrayList<PendingSpare> dataSet, Context context) {
        // super(context, R.layout.item_leads_list, data);
        this.dataSet = dataSet;
        this.mContext=context;
        dbhelper=new Dbhelper(context);
        layoutInflater=LayoutInflater.from(this.mContext);
        sessionManager=new SessionManager(context);
        HashMap<String,String> user=sessionManager.getUserDetails();
        FrCode=user.get(SessionManager.KEY_FrCode);

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
    public View getView(final int position,View convertView, ViewGroup parent) {
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

        final PendingSpare dataSets=dataSet.get(position);

        holder.tv_partname.setText(dataSets.getPartName());
        holder.tv_partcode.setText(dataSets.getPartCode());
        holder.tv_quentity.setText(dataSets.getQty());
        holder.tv_spare_not_found.setText(dataSets.getPending_fla());


        if (dataSets.getPending_fla().equals("Yes")){

            holder.tv_part_price.setVisibility(View.GONE);
            holder.tv_price.setVisibility(View.GONE);

        }
        else {

            holder.tv_part_price.setVisibility(View.VISIBLE);
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_part_price.setText("\u20B9 "+dataSets.getTotalprice());
            holder.tv_price.setText("\u20B9 "+dataSets.getPrice()+ "*"+dataSets.getQty());
        }


        holder.tv_spare_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=position;
                current_qty=Integer.parseInt(dataSets.getQty());
                partname=dataSets.getPartName();
                pen=dataSets.getPending_fla();
                price=Integer.parseInt(dataSets.getPrice());
                openpopup();

            }
        });

        holder.tv_quentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pen=dataSets.getPending_fla();
                partname=dataSets.getPartName();
                pos=position;
                price=Integer.parseInt(dataSets.getPrice());
                showNumberPicker(v);
            }
        });

        if (dataSets.getFlags().equals("D")){

            holder.added_spare_card.setBackgroundColor(Color.LTGRAY);
            holder.btn_delete.setVisibility(View.INVISIBLE);
            holder.tv_quentity.setVisibility(View.INVISIBLE);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenItems.add(position);
                dataSets.setFlags("D");
                dataSets.setPending_fla("");
                dataSets.setChanges("yes");
                holder.btn_delete.setVisibility(View.INVISIBLE);
                holder.tv_spare_not_found.setVisibility(View.INVISIBLE);
                holder.tv_quentity.setVisibility(View.INVISIBLE);
               // holder.ll_main.setVisibility(View.GONE);
                holder.added_spare_card.setCardBackgroundColor(Color.LTGRAY);

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    private static class ViewHolder {
        TextView tv_partname,tv_partcode,tv_quentity,tv_spare_not_found,tv_part_price,tv_price;
        Button btn_delete;
        LinearLayout ll_main;
        CardView added_spare_card;
        public ViewHolder(View view){

            // tv_materialcategory=(TextView)view.findViewById(R.id.tv_materialcategory);

            tv_partname=(TextView)view.findViewById(R.id.tv_part_name);
            tv_partcode=(TextView)view.findViewById(R.id.tv_part_code);
            tv_quentity=(TextView)view.findViewById(R.id.tv_par_quentity);
            btn_delete=(Button)view.findViewById(R.id.btn_delete);
            tv_spare_not_found=view.findViewById(R.id.tv_spare_not_found);
            added_spare_card=view.findViewById(R.id.added_spare_card);
            ll_main=view.findViewById(R.id.item_pending_spare_ll_main);
            tv_part_price=view.findViewById(R.id.tv_part_price);
            tv_price=view.findViewById(R.id.tv_price);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        String partcode=dataSet.get(pos).getPartCode();
        System.out.println("partcode -->"+partcode);

        GetBomStock getBomStock=new GetBomStock();

        getBomStock.getstock(new GetBomStock.VolleyCallback() {
            @Override
            public void onSuccess(String result, String status) {

                System.out.println("result--> "+result);
                if (status.equals("true")){

                    try{

                        JSONArray jsonArray=new JSONArray(result);

                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            String good_stock=jsonObject.getString("good_stock");
                            String refurbished_stock=jsonObject.getString("refurbished_stock");

                            int goodstock=string_to_int(good_stock);
                            int refurbishedstock=string_to_int(refurbished_stock);

                            if (goodstock > 0) {

                                if (goodstock < refurbishedstock) {

                                    sqty = refurbishedstock;
                                } else {
                                    sqty = goodstock;
                                }


                            }
                            else {

                                if (goodstock < refurbishedstock) {

                                    sqty = refurbishedstock;

                                } else {
                                    sqty = goodstock;
                                }
                            }

                              if (picker.getValue() <= sqty && pen.equals("No")) {

                                  quentity = String.valueOf(picker.getValue());
                                  dataSet.get(pos).setQty(quentity);
                                  dataSet.get(pos).setFlags("U");
                                  dataSet.get(pos).setTotalprice(""+price*Integer.parseInt(quentity));
                                  notifyDataSetChanged();
                              }
                              else if (picker.getValue() > sqty && pen.equals("Yes")) {

                                  quentity = String.valueOf(picker.getValue());
                                  dataSet.get(pos).setQty(quentity);
                                  dataSet.get(pos).setFlags("U");
                                  notifyDataSetChanged();
                              }
                              else if (picker.getValue() > sqty) {

                                  quentity = String.valueOf(picker.getValue());
                                  dataSet.get(pos).setQty(quentity);
                                  dataSet.get(pos).setFlags("U");
                                  dataSet.get(pos).setPending_fla("Yes");
                                  notifyDataSetChanged();

                                  Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();
                              }
                              else if (picker.getValue() <= sqty) {

                                  quentity = String.valueOf(picker.getValue());
                                  dataSet.get(pos).setQty(quentity);
                                  dataSet.get(pos).setFlags("U");
                                  dataSet.get(pos).setPending_fla("No");
                                  dataSet.get(pos).setTotalprice(""+price*Integer.parseInt(quentity));
                                  notifyDataSetChanged();

                                  Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();

                              }
                              else {
                                  Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();
                              }


                        }
                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }
                else {

                    sqty=0;
                    if (picker.getValue() <= sqty && pen.equals("No")) {

                        quentity = String.valueOf(picker.getValue());
                        dataSet.get(pos).setQty(quentity);
                        dataSet.get(pos).setFlags("U");
                        notifyDataSetChanged();
                    }
                    else if (picker.getValue() > sqty && pen.equals("Yes")) {

                        quentity = String.valueOf(picker.getValue());
                        dataSet.get(pos).setQty(quentity);
                        dataSet.get(pos).setFlags("U");
                        notifyDataSetChanged();
                    }
                    else if (picker.getValue() > sqty) {

                        quentity = String.valueOf(picker.getValue());
                        dataSet.get(pos).setQty(quentity);
                        dataSet.get(pos).setFlags("U");
                        dataSet.get(pos).setPending_fla("Yes");
                        notifyDataSetChanged();

                        Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();
                    }
                    else if (picker.getValue() <= sqty) {

                        quentity = String.valueOf(picker.getValue());
                        dataSet.get(pos).setQty(quentity);
                        dataSet.get(pos).setFlags("U");
                        dataSet.get(pos).setPending_fla("No");
                        notifyDataSetChanged();

                        Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(mContext, "Stock Quentity " + sqty, Toast.LENGTH_SHORT).show();
                    }

                    // showerror("Spare stock  not found");
                    //  bom_qty=0;
                   // tv_bom_qty.setText("0");
                }

            }
        },mContext,FrCode,partcode);





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

        String partcode=dataSet.get(pos).getPartCode();
        GetBomStock getBomStock=new GetBomStock();
        final int[] goodstock = new int[1];
        final int[] refurbishedstock = new int[1];


        getBomStock.getstock(new GetBomStock.VolleyCallback() {
            @Override
            public void onSuccess(String result, String status) {


                System.out.println("result--> "+result);


                if (status.equals("true")){

                    try{

                        JSONArray jsonArray=new JSONArray(result);

                        for (int i=0;i<jsonArray.length();i++){

                             JSONObject jsonObject= jsonArray.getJSONObject(i);
                             String good_stock=jsonObject.getString("good_stock");
                             String refurbished_stock=jsonObject.getString("refurbished_stock");
                             goodstock[0] =string_to_int(good_stock);
                             refurbishedstock[0] =string_to_int(refurbished_stock);

                             if (goodstock[0] > 0) {

                                 if (goodstock[0] < refurbishedstock[0]) {

                                     sqty = refurbishedstock[0];

                                 } else {

                                     sqty = goodstock[0];
                                 }

                             }
                             else {

                                 if (goodstock[0] < refurbishedstock[0]) {

                                     sqty = refurbishedstock[0];

                                 } else {

                                     sqty = goodstock[0];
                                 }
                             }
                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }
                else {
                    sqty=0;

                }

            }
        },mContext,FrCode,partcode);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                                   if (  sqty < current_qty ){

                                        dataSet.get(pos).setFlags("U");
                                        dataSet.get(pos).setPending_fla("Yes");
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }

                                   else if (sqty == 0){

                                        dataSet.get(pos).setFlags("U");
                                        dataSet.get(pos).setPending_fla("Yes");
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                   }

                                    else {

                                        Toast.makeText(mContext, "Stock quantity "+sqty, Toast.LENGTH_SHORT).show();
                                    }


            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                                 if (sqty==current_qty || current_qty<sqty){

                                        dataSet.get(pos).setFlags("U");
                                        dataSet.get(pos).setPending_fla("No");
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }

                                 else
                                    {
                                        Toast.makeText(mContext, "Stock quantity "+sqty, Toast.LENGTH_SHORT).show();
                                    }

            }
        });

        dialog.show();

    }

    public int string_to_int(String Numbers){
        int no=0;
        no= Integer.parseInt(Numbers);
        return no;
    }

}


