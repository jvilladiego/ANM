package co.gov.anm.sgd.response;

import com.oracle.ucm.Field;

import java.io.InputStream;

import java.util.List;

public class DocumentResponse extends WCCResponse {
   
    private InputStream is;   

    public DocumentResponse(List<Field> fieldList) {
        super(fieldList);
    }

    public DocumentResponse() {
        super();
    }
    
    public void setInputStream(InputStream is) {
        this.is = is;
    }
    
    public InputStream getInputStream() {
        return is;
    }   
}
