package technician.ifb.com.ifptecnician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import technician.ifb.com.ifptecnician.EssentialsDetailsActivity;
import technician.ifb.com.ifptecnician.R;

public class AddMoreEssentialAdapter extends RecyclerView.Adapter<AddMoreEssentialAdapter.ViewHolder>{

    private Context mContext;
    private List<String> addedlist;

    boolean clicked = false;


    public AddMoreEssentialAdapter(Context context, List<String> addedlist) {

        this.mContext = context;
        this.addedlist = addedlist;


    }

    @Override
    public AddMoreEssentialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.essential_add_more_items, parent, false);

        return new AddMoreEssentialAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMoreEssentialAdapter.ViewHolder holder, int position) {

        holder.essential_item.setText(addedlist.get(position));

        holder.img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clicked){

                    holder.img_select.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checkcircle));

                    clicked = false;

                }else{
                    clicked = true;
                    holder.img_select.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_un_check));

                }


            }
        });

        holder.add_essential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(holder.essential_count.getText().toString());

                count++;

                holder.essential_count.setText(String.valueOf(count));



            }
        });


        holder.decrease_essential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(holder.essential_count.getText().toString());

                if (count>1) {
                    count--;

                    holder.essential_count.setText(String.valueOf(count));

                }




            }
        });



    }

    @Override
    public int getItemCount() {
        return addedlist.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView essential_item,add_essential,essential_count,decrease_essential;
        ImageView img_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            essential_item = itemView.findViewById(R.id.essential_item);
            add_essential = itemView.findViewById(R.id.add_essential);
            essential_count = itemView.findViewById(R.id.essential_count);
            decrease_essential = itemView.findViewById(R.id.decrease_essential);
            img_select = itemView.findViewById(R.id.img_select);

        }


    }
}
