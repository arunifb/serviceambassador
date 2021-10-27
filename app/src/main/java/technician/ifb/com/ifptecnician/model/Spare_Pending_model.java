package technician.ifb.com.ifptecnician.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Spare_Pending_model {


    String name,id;

    public Spare_Pending_model(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Spare_Pending_model){
            Spare_Pending_model model=(Spare_Pending_model)obj;
            if (model.getName().equals(name) && model.getId().equals(id)){

                return true;
            }
        }
        return false;

    }
}
