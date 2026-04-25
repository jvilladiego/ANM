package co.gov.anm.sgd.response;

import co.gov.anm.sgd.util.PropertiesLoader;

import com.oracle.ucm.Field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;

public class WCCResponse {
    
    private String codigoEstado = null;
    private String mensajeRsp = null;
    private Map<String, String> metaDatosList = null;
    
    public WCCResponse () {
        super();
    }
    
    public WCCResponse(String statusCode, String statusMessage) {
        this.codigoEstado = statusCode;
        this.mensajeRsp = statusMessage;
    }
    
    public WCCResponse(String statusCode) {
        this.codigoEstado = statusCode;
        this.mensajeRsp = PropertiesLoader.getErrorProperty(statusCode);
    }
    
    public WCCResponse(List<Field> flist) {
        metaDatosList = new HashMap<String, String>();
        
        for (Field curfield : flist) {
            String name = curfield.getName();
            String value = curfield.getValue();
            
            if ((name == null) || (name.length()==0) || (value == null) || (value.length()==0)) {
                continue;
            }
            
            if ("StatusCode".equals(name)) {
               codigoEstado = value;                
            } else if ("StatusMessage".equals(name)) {
                mensajeRsp = value;    
            } else  {
                metaDatosList.put(name, value);
            }
        } 
        
        if (codigoEstado == null) {
            codigoEstado = "0";
            mensajeRsp = "Success";
        }
    }
    
    public WCCResponse(DataBinder binder) {
        metaDatosList = new HashMap<String, String>();
        DataObject ldo = binder.getLocalData();
        
        Set dbkeys =  ldo.keySet();
        Iterator kit = dbkeys.iterator();
        
        while (kit.hasNext()) {
            String cname = (String)kit.next();
            String cval = (String)ldo.get(cname);
            if ((cname == null) || (cname.length()==0) || (cval == null) || (cval.length()==0))
                continue;
            
            if ("StatusCode".equals(cname)) {
                codigoEstado = cval;
            } else if ("StatusMessage".equals(cname)) {
                mensajeRsp = cval;    
            } else {
                metaDatosList.put(cname, cval);
            }   
        } 
        
        if (codigoEstado == null) {
            codigoEstado = "0";
            mensajeRsp = "Success";
        }
    }
    
    public String getStatusCode() {
        return codigoEstado;
    }
    
    public String getStatusMessage() {
        return mensajeRsp;
    }
    
    public Map<String, String> getMetaDataFields() {
        return metaDatosList;
    }
    
    public void setStatusCode(String code) {
        codigoEstado = code;
    }

    public void setStatusMessage(String mensajeRsp) {
        this.mensajeRsp = mensajeRsp;
    }

    public void setMetaDataFields(Map<String, String> metaDatos) {
        this.metaDatosList = metaDatos;
    }
    
    public void cleanMetaData () {
        this.metaDatosList = null;
    }
}
