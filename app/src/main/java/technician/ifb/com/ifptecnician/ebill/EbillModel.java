package technician.ifb.com.ifptecnician.ebill;

public class EbillModel {

    String name,code,qty,header,base_amount,tax_amount,gross_amount,dis_amount,dis_per,net_amount;

    public String getName() {
        return name;
    }



    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBase_amount() {
        return base_amount;
    }

    public void setBase_amount(String base_amount) {
        this.base_amount = base_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public String getDis_amount() {
        return dis_amount;
    }

    public void setDis_amount(String dis_amount) {
        this.dis_amount = dis_amount;
    }

    public String getDis_per() {
        return dis_per;
    }

    public void setDis_per(String dis_per) {
        this.dis_per = dis_per;
    }

    public String getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount = net_amount;
    }
}
