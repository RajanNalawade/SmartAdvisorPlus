package sbilife.com.pointofsale_bancaagency.needanalysis;

class SuggestedProdList {
    private String product_Code = null;
    private String product_UIN = null;
    private String product_name = null;
    private boolean selected = false;


    public SuggestedProdList(String product_Code, String product_UIN,
                             String product_name, boolean selected) {
        super();
        this.product_Code = product_Code;
        this.product_UIN = product_UIN;
        this.product_name = product_name;
        this.selected = selected;
    }


    public String getProduct_UIN() {
        return product_UIN;
    }


    public void setProduct_UIN(String product_UIN) {
        this.product_UIN = product_UIN;
    }


    public String getProduct_Code() {
        return product_Code;
    }

    public void setProduct_Code(String product_Code) {
        this.product_Code = product_Code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
