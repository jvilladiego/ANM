package co.gov.anm.sgd.response;


public class FolderResponse {
    
    private String id;
    private String parentID;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
