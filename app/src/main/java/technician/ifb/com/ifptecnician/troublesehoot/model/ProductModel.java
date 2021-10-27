package technician.ifb.com.ifptecnician.troublesehoot.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductModel {

    String name,code;


    public ProductModel(String name, String code) {
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
        if (obj instanceof ProductModel){
            ProductModel model=(ProductModel)obj;
            if (model.getName().equals(name) && model.getCode().equals(code)){

                return true;
            }
        }
        return false;

    }
}


