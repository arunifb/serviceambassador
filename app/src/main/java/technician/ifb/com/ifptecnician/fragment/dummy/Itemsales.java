package technician.ifb.com.ifptecnician.fragment.dummy;

public class Itemsales {

    String MaterialCategory,FGProduct,Component,ComponentDescription,FGDescription;

    public Itemsales(String materialCategory, String FGProduct, String component, String componentDescription, String FGDescription) {
        MaterialCategory = materialCategory;
        this.FGProduct = FGProduct;
        Component = component;
        ComponentDescription = componentDescription;
        this.FGDescription = FGDescription;
    }

    public String getMaterialCategory() {
        return MaterialCategory;
    }

    public void setMaterialCategory(String materialCategory) {
        MaterialCategory = materialCategory;
    }

    public String getFGProduct() {
        return FGProduct;
    }

    public void setFGProduct(String FGProduct) {
        this.FGProduct = FGProduct;
    }

    public String getComponent() {
        return Component;
    }

    public void setComponent(String component) {
        Component = component;
    }

    public String getComponentDescription() {
        return ComponentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        ComponentDescription = componentDescription;
    }

    public String getFGDescription() {
        return FGDescription;
    }

    public void setFGDescription(String FGDescription) {
        this.FGDescription = FGDescription;
    }
}
