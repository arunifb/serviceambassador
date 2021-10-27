package technician.ifb.com.ifptecnician.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.R;


public class AllLeadFragment extends Fragment {

    ExpandableListView expandableListView;



    public AllLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_all_lead,container,false);
        expandableListView=view.findViewById(R.id.lvexp);
        return view;
    }

}
