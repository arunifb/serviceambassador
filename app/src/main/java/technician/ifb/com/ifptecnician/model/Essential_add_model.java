package technician.ifb.com.ifptecnician.model;

import java.io.Serializable;


public class Essential_add_model  {

    String ename,ecode,equentity,itemtype,flag,date;

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getEquentity() {
        return equentity;
    }

    public void setEquentity(String equentity) {
        this.equentity = equentity;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "Essential : {ename:"
                + ename
                + ", ecode:"
                + ecode
                + ", equentity:"
                + equentity
                +", itemtype:"
                + itemtype
                +", Employees:"
                +flag
               + "}";
    }
}
