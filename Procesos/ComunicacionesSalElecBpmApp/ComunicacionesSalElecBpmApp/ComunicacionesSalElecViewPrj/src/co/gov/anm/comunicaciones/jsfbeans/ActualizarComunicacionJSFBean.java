package co.gov.anm.comunicaciones.jsfbeans;

import co.gov.anm.comunicaciones.model.ejb.ModeloComunicacionesLocal3;
import co.gov.anm.comunicaciones.model.entity.SgdTipoIdentificacion;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

import org.apache.log4j.Logger;


@ManagedBean(name = "ActualizarComunicacionJSFBean")
@ViewScoped
public class ActualizarComunicacionJSFBean extends CommonJSFBean {
    
    private static Logger logger = Logger.getLogger(ActualizarComunicacionJSFBean.class.getSimpleName());

    @EJB
    private ModeloComunicacionesLocal3 comunicacionesBean;

    private String user;
    private Logger log;
    private Collection<SelectItem> tiposIdentificacionSet;
    
    public ActualizarComunicacionJSFBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN CrearComunicacionEntElecHT_jsfbean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error(user + " -> Exception CrearComunicacionEntElecHT_jsfbean", e);
        }
        log.info("END CrearComunicacionEntElecHT_jsfbean");
    }
    
    
    @PostConstruct
    public void inicializar() throws Exception{        
        log.info(this.user + " | BEGIN | inicializar()");

        Collection<SgdTipoIdentificacion> tiposIdentificacion =
            this.comunicacionesBean.getSgdTipoIdentificacionFindAll();
        this.tiposIdentificacionSet = new ArrayList<SelectItem>(tiposIdentificacion.size());
        for (SgdTipoIdentificacion tp : tiposIdentificacion) {
            this.tiposIdentificacionSet.add(new SelectItem(tp.getCodigo(), tp.getNombre()));
        }

        log.info(this.user + " | END | inicializar()");
    
    }
    
    /*******************************************************************
     ************     SETTERS AND GETTERS        *****************
     * *******************************************************/

    public void setTiposIdentificacionSet(Collection<SelectItem> tiposIdentificacionSet) {
        this.tiposIdentificacionSet = tiposIdentificacionSet;
    }

    public Collection<SelectItem> getTiposIdentificacionSet() {
        return tiposIdentificacionSet;
    }
}
