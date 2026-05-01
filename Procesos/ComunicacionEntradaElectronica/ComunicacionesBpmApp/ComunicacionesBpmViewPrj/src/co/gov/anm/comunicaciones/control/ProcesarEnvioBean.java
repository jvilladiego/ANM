package co.gov.anm.comunicaciones.control;

import javax.annotation.PostConstruct;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.output.RichSpacer;

import org.apache.log4j.Logger;

public class ProcesarEnvioBean extends CommonJSFBean{
    
    private Logger log;
    private String user;
    private RichSpacer spacer;

    public ProcesarEnvioBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN ProcesarEnvioBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error("Excepcion ProcesarEnvioBean",e);
        }
        log.info("END ProcesarEnvioBean");
    }


    @PostConstruct
    public void init (){
        log.info("BEGIN init");
        try {
            //Revision variables URL
            log.debug("nroRadicado: "+getElObjectFromBinding("#{bindings.nroRadicado.inputValue}"));
            log.debug("urlDocPpalBO: "+getElObjectFromBinding("#{bindings.urlDocPpalBO.inputValue}"));
            log.debug("urlDocPpal: "+getElObjectFromBinding("#{bindings.urlDocPpal.inputValue}"));
        } catch (Exception e) {
            log.error("Excepcion init",e);
        }
        log.info("END init");
    }

    


    public void setSpacer(RichSpacer spacer) {
        this.spacer = spacer;
    }

    public RichSpacer getSpacer() {
        return spacer;
    }
}
