package technician.ifb.com.ifptecnician.essentialdetails.essentialsdetailsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EssentialItemList{

    @SerializedName("ItemType")
    @Expose
    private String itemType;
    @SerializedName("Component")
    @Expose
    private String component;
    @SerializedName("ComponentDescription")
    @Expose
    private String componentDescription;
    @SerializedName("accessories_stock")
    @Expose
    private String accessoriesStock;
    @SerializedName("additives_stock")
    @Expose
    private String additivesStock;
    @SerializedName("FrCode")
    @Expose
    private String frCode;
    @SerializedName("ShelfLife")
    @Expose
    private String shelfLife;
    @SerializedName("stock_in_transit")
    @Expose
    private String stockInTransit;

    boolean selected;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    public String getAccessoriesStock() {
        return accessoriesStock;
    }

    public void setAccessoriesStock(String accessoriesStock) {
        this.accessoriesStock = accessoriesStock;
    }

    public String getAdditivesStock() {
        return additivesStock;
    }

    public void setAdditivesStock(String additivesStock) {
        this.additivesStock = additivesStock;
    }

    public String getFrCode() {
        return frCode;
    }

    public void setFrCode(String frCode) {
        this.frCode = frCode;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStockInTransit() {
        return stockInTransit;
    }

    public void setStockInTransit(String stockInTransit) {
        this.stockInTransit = stockInTransit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
