package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal;
import co.gov.anm.comunicaciones.entity.SgdUsuario;

import co.gov.anm.comunicaciones.bean.WCContentLocal;
import co.gov.anm.comunicaciones.entity.AnmUnidadadministrativaTb;

import java.util.List;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

@ManagedBean(name = "TramitarComunicacionJSFBean")
@ViewScoped
public class TramitarComunicacionJSFBean extends CommonJSFBean {

    @EJB
    private ComunicacionesSessionBeanLocal comunicacionEJB;

    @EJB
    private WCContentLocal wcContentEJB;

    private Collection<SelectItem> usuariosDependencia = new ArrayList<>();
    private Collection<SgdUsuario> usuariosSet = new ArrayList<>();
    
    private String tipoAsignacionDO;
    private String dependenciaSeleccionadaDO;
    private String dependenciaOriginalCodigo;
    private Object dependenciaOriginalId;
    private String dependenciaOriginalNombre;

    private List<SelectItem> dependencias = new ArrayList<>();
    private Collection<AnmUnidadadministrativaTb> dependenciasSet = new ArrayList<>();

    private Logger log;

    public TramitarComunicacionJSFBean() {
        super();
    }

    @PostConstruct
    public void init() {
        log = Logger.getLogger(this.getClass().getSimpleName());

       //Req05
        Object codigoOriginalObj =
            super.getElObjectFromBinding(
                "#{bindings.codDepDestino.inputValue}"
            );

        dependenciaOriginalCodigo =
            codigoOriginalObj != null
            ? codigoOriginalObj.toString()
            : null;

        dependenciaOriginalId =
            super.getElObjectFromBinding(
                "#{bindings.id4.inputValue}"
            );

        Object nombreOriginalObj =
            super.getElObjectFromBinding(
                "#{bindings.nombre6.inputValue}"
            );

        dependenciaOriginalNombre =
            nombreOriginalObj != null
            ? nombreOriginalObj.toString()
            : null;
        
        // Req05
        dependenciasSet = wcContentEJB.getUnidadAdministrativaFindAll();

        dependencias.clear();
        dependencias.add(new SelectItem(null, "Seleccione"));

        if (dependenciasSet != null) {
            for (AnmUnidadadministrativaTb dependencia : dependenciasSet) {
                dependencias.add(new SelectItem(dependencia.getCodUnidadadministrativa().toString(),
                                                dependencia.getCodUnidadadministrativa() + " - " +
                                                dependencia.getNombreunidadadministrativa()));
            }
        }

        log.debug("Dependencias cargadas: " + dependencias.size());

        Long codDependencia =
            Long.parseLong(((String) super.getElObjectFromBinding("#{bindings.codDepDestino.inputValue}")));

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
        log.debug("Inicio cambiarUsuarioDestino():" + evt.getOldValue());

        String newValue = (String) evt.getNewValue();

        try {
            if (newValue != null) {
                for (SgdUsuario usuario : usuariosSet) {
                    if (usuario.getIdUsuario().equals(newValue)) {
                        log.debug("setea el nuevo usuario asignado: " + newValue);

                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}",
                                                     usuario.getNombreUsuario());

                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error cambiando de usuario: " + e.getMessage(), e);
        }

        log.debug("Fin cambiarUsuarioDestino():" + evt.getNewValue());
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

//Req05
    public void cambiarAsignacion(ValueChangeEvent event) {
        try {
            tipoAsignacionDO = event.getNewValue() != null ? event.getNewValue().toString() : null;

            log.debug("Tipo de asignacion seleccionado: " + tipoAsignacionDO);

            super.setElObjectIntoBinding("#{bindings.tipoAsignacion.inputValue}", tipoAsignacionDO);

            if ("D".equals(tipoAsignacionDO)) {
                /*
                 * Obliga a seleccionar explícitamente una dependencia
                 * y elimina cualquier usuario elegido anteriormente.
                 */
                dependenciaSeleccionadaDO = null;

                super.setElObjectIntoBinding(
                    "#{bindings.idUsuario.inputValue}",
                    null
                );

            } else if ("U".equals(tipoAsignacionDO)) {
                /*
                 * Si se regresa a asignación por usuario, restaura
                 * la dependencia original de la comunicación.
                 */
                dependenciaSeleccionadaDO = null;

                super.setElObjectIntoBinding(
                    "#{bindings.codDepDestino.inputValue}",
                    dependenciaOriginalCodigo
                );

                super.setElObjectIntoBinding(
                    "#{bindings.id4.inputValue}",
                    dependenciaOriginalId
                );

                super.setElObjectIntoBinding(
                    "#{bindings.nombre6.inputValue}",
                    dependenciaOriginalNombre
                );
            }

        } catch (Exception e) {
            log.error("Error cambiando el tipo de asignacion", e);
        }
    }
    
    //Req05
    public void cambiarDependencia(ValueChangeEvent event) {
        try {
            if (event.getNewValue() == null) {
                return;
            }

            String codigoSeleccionado =
                event.getNewValue().toString();

            for (AnmUnidadadministrativaTb dependencia : dependenciasSet) {
                if (codigoSeleccionado.equals(
                        dependencia
                            .getCodUnidadadministrativa()
                            .toString())) {

                    super.setElObjectIntoBinding(
                        "#{bindings.codDepDestino.inputValue}",
                        dependencia
                            .getCodUnidadadministrativa()
                            .toString()
                    );

                    super.setElObjectIntoBinding(
                        "#{bindings.id4.inputValue}",
                        dependencia.getIdUnidadadministrativa()
                    );

                    super.setElObjectIntoBinding(
                        "#{bindings.nombre6.inputValue}",
                        dependencia
                            .getNombreunidadadministrativa()
                    );

                    /*
                     * Al reasignar por dependencia, no debe
                     * conservarse un usuario seleccionado.
                     */
                    super.setElObjectIntoBinding(
                        "#{bindings.idUsuario.inputValue}",
                        null
                    );

                    log.debug(
                        "Dependencia seleccionada: "
                        + codigoSeleccionado
                    );

                    break;
                }
            }

        } catch (Exception e) {
            log.error(
                "Error cambiando la dependencia destino",
                e
            );
        }
    }

    public String getTipoAsignacionDO() {
        return tipoAsignacionDO;
    }

    public void setTipoAsignacionDO(String tipoAsignacionDO) {
        this.tipoAsignacionDO = tipoAsignacionDO;
    }

    public List<SelectItem> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<SelectItem> dependencias) {
        this.dependencias = dependencias;
    }

    public Collection<AnmUnidadadministrativaTb> getDependenciasSet() {
        return dependenciasSet;
    }

    public void setDependenciasSet(Collection<AnmUnidadadministrativaTb> dependenciasSet) {
        this.dependenciasSet = dependenciasSet;
    }
    
    public String getDependenciaSeleccionadaDO() {
        return dependenciaSeleccionadaDO;
    }

    public void setDependenciaSeleccionadaDO(
        String dependenciaSeleccionadaDO
    ) {
        this.dependenciaSeleccionadaDO = dependenciaSeleccionadaDO;
    }
}
