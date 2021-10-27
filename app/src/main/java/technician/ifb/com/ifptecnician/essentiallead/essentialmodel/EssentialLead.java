
package technician.ifb.com.ifptecnician.essentiallead.essentialmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class EssentialLead implements Serializable {

    @SerializedName("Frcode")
    @Expose
    private Object frcode;
    @SerializedName("lead_category")
    @Expose
    private Object leadCategory;
    @SerializedName("Contactno")
    @Expose
    private Object contactno;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Data")
    @Expose
    private List<EssentialList> data = null;

    public Object getFrcode() {
        return frcode;
    }

    public void setFrcode(Object frcode) {
        this.frcode = frcode;
    }

    public Object getLeadCategory() {
        return leadCategory;
    }

    public void setLeadCategory(Object leadCategory) {
        this.leadCategory = leadCategory;
    }

    public Object getContactno() {
        return contactno;
    }

    public void setContactno(Object contactno) {
        this.contactno = contactno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<EssentialList> getData() {
        return data;
    }

    public void setData(List<EssentialList> data) {
        this.data = data;
    }

}
