package technician.ifb.com.ifptecnician.fragment.dummy;

public class SpareModel {

 String  TechCode,itemtype,Spare,Acc,Add,Ticketno,CallType,product,ProductName,MachineStaus,CallBookDate,ClosedDate;

    public SpareModel(String techCode, String itemtype, String spare, String acc, String add, String ticketno, String callType, String product, String productName, String machineStaus, String callBookDate, String closedDate) {
        TechCode = techCode;
        this.itemtype = itemtype;
        Spare = spare;
        Acc = acc;
        Add = add;
        Ticketno = ticketno;
        CallType = callType;
        this.product = product;
        ProductName = productName;
        MachineStaus = machineStaus;
        CallBookDate = callBookDate;
        ClosedDate = closedDate;
    }

    public String getTechCode() {
        return TechCode;
    }

    public void setTechCode(String techCode) {
        TechCode = techCode;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getSpare() {
        return Spare;
    }

    public void setSpare(String spare) {
        Spare = spare;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }

    public String getAdd() {
        return Add;
    }

    public void setAdd(String add) {
        Add = add;
    }

    public String getTicketno() {
        return Ticketno;
    }

    public void setTicketno(String ticketno) {
        Ticketno = ticketno;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getMachineStaus() {
        return MachineStaus;
    }

    public void setMachineStaus(String machineStaus) {
        MachineStaus = machineStaus;
    }

    public String getCallBookDate() {
        return CallBookDate;
    }

    public void setCallBookDate(String callBookDate) {
        CallBookDate = callBookDate;
    }

    public String getClosedDate() {
        return ClosedDate;
    }

    public void setClosedDate(String closedDate) {
        ClosedDate = closedDate;
    }
}
