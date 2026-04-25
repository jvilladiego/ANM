package co.gov.anm.sgd.exception;

/**
 * La clase <code>WSException</code> representa excepciones de la
 * invocaci&oacute;n a servicios web.
 * 
 * @author Jainer Eduardo Quiceno Rojas [jaine.quiceno@oracle.com]
 *
 */
public class WSException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String code;
    private String message;

    public WSException(String code, Throwable e) {
        super(code, e);
        
        this.code = code;
    }

    public WSException(String code, String msg) {
        super(code + msg);
        
        this.code = code;
        this.message = msg;
    }
    
    public WSException(String code, String msg, Throwable e) {
        super(code + msg, e);
        
        this.code = code;
        this.message = msg;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
