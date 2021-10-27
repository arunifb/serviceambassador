package technician.ifb.com.ifptecnician.amc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContractTypeDetailsModel {


    String name,id;

    public ContractTypeDetailsModel(String name, String id) {
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
        if (obj instanceof ContractTypeDetailsModel){
            ContractTypeDetailsModel model=(ContractTypeDetailsModel)obj;
            if (model.getName().equals(name) && model.getId().equals(id)){

                return true;
            }
        }
        return false;

    }
}


