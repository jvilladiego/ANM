package co.gov.anm.comunicaciones.control;

import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@ManagedBean(name="AprobarComunicacionJSFBean")
@ViewScoped
public class AprobarComunicacionJSFBean extends CommonJSFBean{
    
    private Logger log;
    
    public AprobarComunicacionJSFBean() {
        super();
        log = Logger.getLogger(this.getClass().getSimpleName());
        log.info("BEGIN CrearComunicacionJSFBean");        
    }
    
    //Metodos para manejo del documento principal
    public void descargar(FacesContext fc, OutputStream os) {
        log.info("INICIO descargar");
        try {
            log.debug(" -> name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            log.debug(" -> mimeType: " + getElObjectFromBinding("#{bindings.mimetype.inputValue}"));
            log.debug(" -> size: " + getElObjectFromBinding("#{bindings.size.inputValue}"));
            byte[] encodeFile = getElObjectFromBinding("#{bindings.content1.inputValue}").toString().getBytes();
            log.debug(" -> length-e: " + encodeFile.length);
            //Decodificar archivo recibido en el payload
            byte[] decodeFile = java.util.Base64.getDecoder().decode(encodeFile);
            log.debug(" -> length-d: " + decodeFile.length);
             //Crear response para el navegador
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            response.setContentType((String)getElObjectFromBinding("#{bindings.mimetype.inputValue}"));
            response.setContentLength(decodeFile.length);
            response.getOutputStream().write(decodeFile);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            log.error(" Exception descargar", e);
        }
       log.info("FIN descargar");
    }
}
