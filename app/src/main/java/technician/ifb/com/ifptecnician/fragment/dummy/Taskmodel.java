package technician.ifb.com.ifptecnician.fragment.dummy;

public class Taskmodel {



   String TicketNo,Branch,Franchise,CallType,Status,
           Product,Model,SerialNo,MachinStatus,DOP,DOI,PendingReason,CallBookDate,AssignDate,ClosedDate, CancelledDate ,
           TechnicianCode,TechName,CustomerCode,CustomerName,PinCode,TelePhone,RCNNo,MobileNo,
           Street,City,State,Address,NO126,ServiceType,Email,Priority,Ageing;


    public Taskmodel(String ticketNo, String branch, String franchise, String callType, String status, String product, String model, String serialNo, String machinStatus, String DOP, String DOI, String pendingReason, String callBookDate, String assignDate, String closedDate, String cancelledDate, String technicianCode, String techName, String customerCode, String customerName, String pinCode, String telePhone, String RCNNo, String mobileNo, String street, String city, String state, String address, String NO126, String serviceType, String email, String priority, String ageing) {
        TicketNo = ticketNo;
        Branch = branch;
        Franchise = franchise;
        CallType = callType;
        Status = status;
        Product = product;
        Model = model;
        SerialNo = serialNo;
        MachinStatus = machinStatus;
        this.DOP = DOP;
        this.DOI = DOI;
        PendingReason = pendingReason;
        CallBookDate = callBookDate;
        AssignDate = assignDate;
        ClosedDate = closedDate;
        CancelledDate = cancelledDate;
        TechnicianCode = technicianCode;
        TechName = techName;
        CustomerCode = customerCode;
        CustomerName = customerName;
        PinCode = pinCode;
        TelePhone = telePhone;
        this.RCNNo = RCNNo;
        MobileNo = mobileNo;
        Street = street;
        City = city;
        State = state;
        Address = address;
        this.NO126 = NO126;
        ServiceType = serviceType;
        Email = email;
        Priority = priority;
        Ageing = ageing;
    }

    public String getAgeing() {
        return Ageing;
    }

    public void setAgeing(String ageing) {
        Ageing = ageing;
    }


    //    public Taskmodel(String ticketNo, String branch, String franchise, String callType, String status, String product, String model, String serialNo, String machinStatus, String DOP, String DOI, String pendingReason, String callBookDate, String assignDate, String closedDate, String cancelledDate, String technicianCode, String techName, String customerCode, String customerName, String pinCode, String telePhone, String RCNNo, String mobileNo, String street, String city, String state, String address, String NO126, String serviceType, String email, String priority) {
//        TicketNo = ticketNo;
//        Branch = branch;
//        Franchise = franchise;
//        CallType = callType;
//        Status = status;
//        Product = product;
//        Model = model;
//        SerialNo = serialNo;
//        MachinStatus = machinStatus;
//        this.DOP = DOP;
//        this.DOI = DOI;
//        PendingReason = pendingReason;
//        CallBookDate = callBookDate;
//        AssignDate = assignDate;
//        ClosedDate = closedDate;
//        CancelledDate = cancelledDate;
//        TechnicianCode = technicianCode;
//        TechName = techName;
//        CustomerCode = customerCode;
//        CustomerName = customerName;
//        PinCode = pinCode;
//        TelePhone = telePhone;
//        this.RCNNo = RCNNo;
//        MobileNo = mobileNo;
//        Street = street;
//        City = city;
//        State = state;
//        Address = address;
//        this.NO126 = NO126;
//        ServiceType = serviceType;
//        Email = email;
//        Priority = priority;
//    }

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getFranchise() {
        return Franchise;
    }

    public void setFranchise(String franchise) {
        Franchise = franchise;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getMachinStatus() {
        return MachinStatus;
    }

    public void setMachinStatus(String machinStatus) {
        MachinStatus = machinStatus;
    }

    public String getDOP() {
        return DOP;
    }

    public void setDOP(String DOP) {
        this.DOP = DOP;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public String getPendingReason() {
        return PendingReason;
    }

    public void setPendingReason(String pendingReason) {
        PendingReason = pendingReason;
    }

    public String getCallBookDate() {
        return CallBookDate;
    }

    public void setCallBookDate(String callBookDate) {
        CallBookDate = callBookDate;
    }

    public String getAssignDate() {
        return AssignDate;
    }

    public void setAssignDate(String assignDate) {
        AssignDate = assignDate;
    }

    public String getClosedDate() {
        return ClosedDate;
    }

    public void setClosedDate(String closedDate) {
        ClosedDate = closedDate;
    }

    public String getCancelledDate() {
        return CancelledDate;
    }

    public void setCancelledDate(String cancelledDate) {
        CancelledDate = cancelledDate;
    }

    public String getTechnicianCode() {
        return TechnicianCode;
    }

    public void setTechnicianCode(String technicianCode) {
        TechnicianCode = technicianCode;
    }

    public String getTechName() {
        return TechName;
    }

    public void setTechName(String techName) {
        TechName = techName;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getTelePhone() {
        return TelePhone;
    }

    public void setTelePhone(String telePhone) {
        TelePhone = telePhone;
    }

    public String getRCNNo() {
        return RCNNo;
    }

    public void setRCNNo(String RCNNo) {
        this.RCNNo = RCNNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNO126() {
        return NO126;
    }

    public void setNO126(String NO126) {
        this.NO126 = NO126;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }
}
