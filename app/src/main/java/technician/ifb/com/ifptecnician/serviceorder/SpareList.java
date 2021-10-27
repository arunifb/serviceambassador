package technician.ifb.com.ifptecnician.serviceorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpareList {


    String name,id;

    public SpareList(String name, String id) {
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
        if (obj instanceof SpareList){
            SpareList model=(SpareList)obj;
            if (model.getName().equals(name) && model.getId().equals(id)){

                return true;
            }
        }
        return false;

    }
}