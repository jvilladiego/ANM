package co.gov.anm.sgd.response;

import com.oracle.ucm.Field;

import java.util.List;

public class BrowseResponse extends WCCResponse {
    
    private List<FolderResponse> folderList;
    private List<FileResponse> fileList;
    
    public BrowseResponse () {
        super();
    }
    
    public BrowseResponse (String errorCode) {
        super(errorCode);
    }
    
    public BrowseResponse (String errorCode, String message) {
        super(errorCode, message);
    }
    
    public BrowseResponse (List<Field> flist) {
        super(flist);
    }

    public List<FolderResponse> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<FolderResponse> folderList) {
        this.folderList = folderList;
    }

    public void setFileList(List<FileResponse> fileList) {
        this.fileList = fileList;
    }

    public List<FileResponse> getFileList() {
        return fileList;
    }
}
