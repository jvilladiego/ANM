package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal;
import co.gov.anm.comunicaciones.entity.SgdUsuario;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

@ManagedBean(name="TramitarComunicacionJSFBean")
@ViewScoped
public class TramitarComunicacionJSFBean extends CommonJSFBean {
    
    @EJB
    private ComunicacionesSessionBeanLocal comunicacionEJB;
    
    private Collection<SelectItem> usuariosDependencia = new ArrayList<>();
    private Collection<SgdUsuario> usuariosSet = new ArrayList<>();
    
    private Logger log;

    public TramitarComunicacionJSFBean() {
        super();
    }
    
    @PostConstruct
    public void init () {
        log = Logger.getLogger(this.getClass().getSimpleName());
        Long codDependencia = Long.parseLong(((String) super.getElObjectFromBinding("#{bindings.codDepDestino.inputValue}")));
        
        log.debug("Filtra usuarios por codigo dependencia: " + codDependencia);
        
        usuariosSet = comunicacionEJB.getSgdUsuarioFindByDependencia(codDependencia);
        
        log.debug("Usuarios retornados: " + usuariosSet.size());
        
        log.debug("Llena lista de usuarios para FYI");   
            
        if (usuariosSet != null && usuariosSet.size() > 0) {
            for (SgdUsuario usuario : usuariosSet) {
                usuariosDependencia.add(new SelectItem(usuario.getIdUsuario(), usuario.getNombreUsuario()));
            }
        }
    }
    
    public void cambiarUsuarioDestino(ValueChangeEvent evt) {
        log.debug("Inicio cambiarUsuarioDestino():"+evt.getOldValue());
        
        String newValue = (String)evt.getNewValue();
        
        try {
            if (newValue != null) {
                for (SgdUsuario usuario : usuariosSet) {
                    if (usuario.getIdUsuario().equals(newValue)) {
                        log.debug("setea el nuevo usuario asignado: " + newValue);
                        
                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}", usuario.getNombreUsuario());      
                        
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error cambiando de usuario: " + e.getMessage(), e);
        }
        
        log.debug("Fin cambiarUsuarioDestino():"+evt.getNewValue());
    }

    public void setUsuariosDependencia(Collection<SelectItem> usuariosDependencia) {
        this.usuariosDependencia = usuariosDependencia;
    }

    public Collection<SelectItem> getUsuariosDependencia() {
        return usuariosDependencia;
    }

    public void setUsuariosSet(Collection<SgdUsuario> usuariosSet) {
        this.usuariosSet = usuariosSet;
    }

    public Collection<SgdUsuario> getUsuariosSet() {
        return usuariosSet;
    }
}
