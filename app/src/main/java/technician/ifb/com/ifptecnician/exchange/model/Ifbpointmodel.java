package technician.ifb.com.ifptecnician.exchange.model;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import technician.ifb.com.ifptecnician.troublesehoot.model.ModelsModel;

public class Ifbpointmodel {
    String name,code;


    public Ifbpointmodel(String name, String code) {
        this.name = name;
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Ifbpointmodel){
            Ifbpointmodel model=(Ifbpointmodel)obj;
            if (model.getName().equals(name) && model.getCode().equals(code)){

                return true;
            }
        }
        return false;

    }
}
