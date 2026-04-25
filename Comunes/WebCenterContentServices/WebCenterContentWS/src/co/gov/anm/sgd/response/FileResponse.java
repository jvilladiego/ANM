package co.gov.anm.sgd.response;


public class FileResponse {
    
    private  String dID;
    private String docName;
    private String parentID;
    private String name;
    private String fileGUID;

    public void setFileGUID(String fileGUID) {
        this.fileGUID = fileGUID;
    }

    public String getFileGUID() {
        return fileGUID;
    }

    public void setDID(String dID) {
        this.dID = dID;
    }

    public String getDID() {
        return dID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setDocName(String id) {
        this.docName = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
