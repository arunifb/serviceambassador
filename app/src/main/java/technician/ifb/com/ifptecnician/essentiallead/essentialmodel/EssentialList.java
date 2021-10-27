
package technician.ifb.com.ifptecnician.essentiallead.essentialmodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EssentialList implements Serializable {

    @SerializedName("object_id")
    @Expose
    private String objectId;
    @SerializedName("concatstatuser")
    @Expose
    private String concatstatuser;
    @SerializedName("frcode")
    @Expose
    private String frcode;
    @SerializedName("franchise")
    @Expose
    private String franchise;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("brcode")
    @Expose
    private String brcode;
    @SerializedName("process_type_txt")
    @Expose
    private String processTypeTxt;
    @SerializedName("lead_category")
    @Expose
    private String leadCategory;
    @SerializedName("zzmat_grp")
    @Expose
    private String zzmatGrp;
    @SerializedName("zzproduct_desc")
    @Expose
    private String zzproductDesc;
    @SerializedName("zzserial_numb")
    @Expose
    private String zzserialNumb;
    @SerializedName("sold_to_party")
    @Expose
    private String soldToParty;
    @SerializedName("sold_to_party_list")
    @Expose
    private String soldToPartyList;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("postl_cod1")
    @Expose
    private String postlCod1;
    @SerializedName("tel_no")
    @Expose
    private String telNo;
    @SerializedName("caller_no")
    @Expose
    private String callerNo;
    @SerializedName("e_mail")
    @Expose
    private String eMail;
    @SerializedName("technician")
    @Expose
    private String technician;
    @SerializedName("posting_date")
    @Expose
    private Object postingDate;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("changed_time")
    @Expose
    private Object changedTime;
    @SerializedName("technician_code")
    @Expose
    private String technicianCode;
    @SerializedName("branchexecode")
    @Expose
    private Object branchexecode;
    @SerializedName("branchexename")
    @Expose
    private Object branchexename;
    @SerializedName("createdby")
    @Expose
    private Object createdby;
    @SerializedName("covid_lead")
    @Expose
    private Object covidLead;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("productid")
    @Expose
    private String productid;
    @SerializedName("zzreltyp")
    @Expose
    private Object zzreltyp;
    @SerializedName("zzbranch")
    @Expose
    private Object zzbranch;
    @SerializedName("zzbse")
    @Expose
    private Object zzbse;
    @SerializedName("zzregion")
    @Expose
    private Object zzregion;
    @SerializedName("zzifb_point")
    @Expose
    private Object zzifbPoint;
    @SerializedName("zzname1")
    @Expose
    private Object zzname1;
    @SerializedName("Product")
    @Expose
    private Object product;
    @SerializedName("zzhouse_no")
    @Expose
    private Object zzhouseNo;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getConcatstatuser() {
        return concatstatuser;
    }

    public void setConcatstatuser(String concatstatuser) {
        this.concatstatuser = concatstatuser;
    }

    public String getFrcode() {
        return frcode;
    }

    public void setFrcode(String frcode) {
        this.frcode = frcode;
    }

    public String getFranchise() {
        return franchise;
    }

    public void setFranchise(String franchise) {
        this.franchise = franchise;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBrcode() {
        return brcode;
    }

    public void setBrcode(String brcode) {
        this.brcode = brcode;
    }

    public String getProcessTypeTxt() {
        return processTypeTxt;
    }

    public void setProcessTypeTxt(String processTypeTxt) {
        this.processTypeTxt = processTypeTxt;
    }

    public String getLeadCategory() {
        return leadCategory;
    }

    public void setLeadCategory(String leadCategory) {
        this.leadCategory = leadCategory;
    }

    public String getZzmatGrp() {
        return zzmatGrp;
    }

    public void setZzmatGrp(String zzmatGrp) {
        this.zzmatGrp = zzmatGrp;
    }

    public String getZzproductDesc() {
        return zzproductDesc;
    }

    public void setZzproductDesc(String zzproductDesc) {
        this.zzproductDesc = zzproductDesc;
    }

    public String getZzserialNumb() {
        return zzserialNumb;
    }

    public void setZzserialNumb(String zzserialNumb) {
        this.zzserialNumb = zzserialNumb;
    }

    public String getSoldToParty() {
        return soldToParty;
    }

    public void setSoldToParty(String soldToParty) {
        this.soldToParty = soldToParty;
    }

    public String getSoldToPartyList() {
        return soldToPartyList;
    }

    public void setSoldToPartyList(String soldToPartyList) {
        this.soldToPartyList = soldToPartyList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostlCod1() {
        return postlCod1;
    }

    public void setPostlCod1(String postlCod1) {
        this.postlCod1 = postlCod1;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getCallerNo() {
        return callerNo;
    }

    public void setCallerNo(String callerNo) {
        this.callerNo = callerNo;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public Object getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Object postingDate) {
        this.postingDate = postingDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Object getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(Object changedTime) {
        this.changedTime = changedTime;
    }

    public String getTechnicianCode() {
        return technicianCode;
    }

    public void setTechnicianCode(String technicianCode) {
        this.technicianCode = technicianCode;
    }

    public Object getBranchexecode() {
        return branchexecode;
    }

    public void setBranchexecode(Object branchexecode) {
        this.branchexecode = branchexecode;
    }

    public Object getBranchexename() {
        return branchexename;
    }

    public void setBranchexename(Object branchexename) {
        this.branchexename = branchexename;
    }

    public Object getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Object createdby) {
        this.createdby = createdby;
    }

    public Object getCovidLead() {
        return covidLead;
    }

    public void setCovidLead(Object covidLead) {
        this.covidLead = covidLead;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public Object getZzreltyp() {
        return zzreltyp;
    }

    public void setZzreltyp(Object zzreltyp) {
        this.zzreltyp = zzreltyp;
    }

    public Object getZzbranch() {
        return zzbranch;
    }

    public void setZzbranch(Object zzbranch) {
        this.zzbranch = zzbranch;
    }

    public Object getZzbse() {
        return zzbse;
    }

    public void setZzbse(Object zzbse) {
        this.zzbse = zzbse;
    }

    public Object getZzregion() {
        return zzregion;
    }

    public void setZzregion(Object zzregion) {
        this.zzregion = zzregion;
    }

    public Object getZzifbPoint() {
        return zzifbPoint;
    }

    public void setZzifbPoint(Object zzifbPoint) {
        this.zzifbPoint = zzifbPoint;
    }

    public Object getZzname1() {
        return zzname1;
    }

    public void setZzname1(Object zzname1) {
        this.zzname1 = zzname1;
    }

    public Object getProduct() {
        return product;
    }

    public void setProduct(Object product) {
        this.product = product;
    }

    public Object getZzhouseNo() {
        return zzhouseNo;
    }

    public void setZzhouseNo(Object zzhouseNo) {
        this.zzhouseNo = zzhouseNo;
    }

}
