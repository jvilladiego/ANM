package bean;

import java.io.Serializable;

public class SavePointBean implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 8180072674853854446L;

    public String savePointId;

    public SavePointBean() {
    }

    public void setSavePointId(String savePointId) {
        this.savePointId = savePointId;
    }

    public String getSavePointId() {
        return savePointId;
    }
}
