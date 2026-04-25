package co.gov.anm.sgd.transfer;

import co.gov.anm.sgd.response.WCCResponse;


public class FolderComDTO extends WCCResponse {
  
    private String folderGuid;
    private String folderPath;

    public void setFolderGuid(String folderGuid) {
        this.folderGuid = folderGuid;
    }

    public String getFolderGuid() {
        return folderGuid;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }
}
