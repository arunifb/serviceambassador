package technician.ifb.com.ifptecnician.troublesehoot.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProblemModel {

    String name,code;


    public ProblemModel(String name, String code) {
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
        if (obj instanceof ProblemModel){
            ProblemModel model=(ProblemModel)obj;
            if (model.getName().equals(name) && model.getCode().equals(code)){

                return true;
            }
        }
        return false;

    }
}


