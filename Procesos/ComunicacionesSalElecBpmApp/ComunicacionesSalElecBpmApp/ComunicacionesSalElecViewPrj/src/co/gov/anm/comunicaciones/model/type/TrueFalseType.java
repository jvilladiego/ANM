package co.gov.anm.comunicaciones.model.type;

/**
 * 
 */
public enum TrueFalseType {
    
    TRUE("SI",true),
    FALSE("NO",false);
    
    
    private String value;
    private boolean booleanValue;
    
    TrueFalseType(String value, boolean booleanValue){
        this.value = value;
        this.booleanValue = booleanValue;
    }


    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

}
