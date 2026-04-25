package co.gov.anm.sgd.transfer;

import java.io.File;

public class FileTransfer {
    
    private File file;
    private String filename;
    private String contentType;
    private long size;
    
    private Credenciales credenciales;

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}
