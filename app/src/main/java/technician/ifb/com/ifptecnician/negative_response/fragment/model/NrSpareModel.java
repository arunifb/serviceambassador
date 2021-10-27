package technician.ifb.com.ifptecnician.negative_response.fragment.model;

public class NrSpareModel {

    String Ticketno, ItemType, Status, PartName, PartCode, EssName, EssCode, AccName, AccCode, ETA, Qty, itemno, spare_serno,flags,pending_fla;

    public String getTicketno() {
        return Ticketno;
    }

    public void setTicketno(String ticketno) {
        Ticketno = ticketno;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public String getPartCode() {
        return PartCode;
    }

    public void setPartCode(String partCode) {
        PartCode = partCode;
    }

    public String getEssName() {
        return EssName;
    }

    public void setEssName(String essName) {
        EssName = essName;
    }

    public String getEssCode() {
        return EssCode;
    }

    public void setEssCode(String essCode) {
        EssCode = essCode;
    }

    public String getAccName() {
        return AccName;
    }

    public void setAccName(String accName) {
        AccName = accName;
    }

    public String getAccCode() {
        return AccCode;
    }

    public void setAccCode(String accCode) {
        AccCode = accCode;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getSpare_serno() {
        return spare_serno;
    }

    public void setSpare_serno(String spare_serno) {
        this.spare_serno = spare_serno;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getPending_fla() {
        return pending_fla;
    }

    public void setPending_fla(String pending_fla) {
        this.pending_fla = pending_fla;
    }
}
