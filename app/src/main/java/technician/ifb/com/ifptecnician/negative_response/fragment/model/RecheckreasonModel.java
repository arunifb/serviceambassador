package technician.ifb.com.ifptecnician.negative_response.fragment.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecheckreasonModel {

  private  String Pname,Pid;


    public RecheckreasonModel(String pname, String pid) {
        Pname = pname;
        Pid = pid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    @NonNull
    @Override
    public String toString() {
        return Pname;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj instanceof RecheckreasonModel){
            RecheckreasonModel model=(RecheckreasonModel)obj;
            if (model.getPname().equals(Pname) && model.getPid().equals(Pid)){

                return true;
            }
        }
        return false;
    }
}
