package technician.ifb.com.ifptecnician.stock;

public class MystockModel {


    boolean Ischeck;
    String TechPartnerId,SpareCode,SpareDes,Amount,Quantity,returnqty;
//      "ItemId": "8903287028700",
//              "ItemName": "CABLE HARNESS-C4 W/OUT TURBO- 32004652",
//              "Quantity": 1,
//              "Price": 0,
//              "OrderId": null,
//              "TechCode": null,
//              "stock_type": "REQUEST",
//              "Status": "Pending",
//              "Remark": "Request is pending"

    public String getTechPartnerId() {
        return TechPartnerId;
    }

    public String getSpareCode() {
        return SpareCode;
    }

    public String getSpareDes() {
        return SpareDes;
    }

    public String getAmount() {
        return Amount;
    }

    public String getQuantity() {
        return Quantity;
    }



    public boolean isIscheck() {
        return Ischeck;
    }

    public void setIscheck(boolean ischeck) {
        Ischeck = ischeck;
    }

    public void setTechPartnerId(String techPartnerId) {
        TechPartnerId = techPartnerId;
    }

    public void setSpareCode(String spareCode) {
        SpareCode = spareCode;
    }

    public void setSpareDes(String spareDes) {
        SpareDes = spareDes;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }


    public String getReturnqty() {
        return returnqty;
    }

    public void setReturnqty(String returnqty) {
        this.returnqty = returnqty;
    }
}
