package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal2;
import co.gov.anm.comunicaciones.entity.SgdUsuario;

import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.model.AutoSuggestUIHints;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.SelectionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

@ManagedBean(name="TramitarComunicacionJSFBean")
@ViewScoped
public class TramitarComunicacionJSFBean extends CommonJSFBean {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -7406761047352405314L;

    @EJB
    private ComunicacionesSessionBeanLocal2 comunicacionEJB;
    
    private List<SgdUsuario> listaUsuarioDependencia;
    private List<SelectItem> usuariosList;
    private List<SelectItem> usuariosFYI;
    private String textoBusqueda;
    
    private String idUsuario = null;
    private String nombreUsuario = null;
    private boolean usuarioSeleccionado = false;
    
    
    private static final Logger log = Logger.getLogger(TramitarComunicacionJSFBean.class);

    private String user;
    private SgdUsuario sgdUsuario;

    private List<SelectItem> lstDependencias2 = new ArrayList<SelectItem>();
    private List<SelectItem> lstUsuariosObj = new ArrayList<SelectItem>();
    
    private String dependenciaDestino;

    public TramitarComunicacionJSFBean() {
        super();
    }
    

    @PostConstruct
    public void init() {
        log.info("[TramitarComunicacionJSFBean] INICIO init()");

        try {
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();

            user = secCntx.getUserPrincipal().getName();

            log.info("[TramitarComunicacionJSFBean] Usuario logueado: " + user);

            List<SgdUsuario> lstUsuario = comunicacionEJB.getSgdUsuarioFindById(user);

            log.info("[TramitarComunicacionJSFBean] lstUsuario size: " +
                     (lstUsuario != null ? lstUsuario.size() : "NULL"));

            if (lstUsuario != null && lstUsuario.size() > 0) {
                sgdUsuario = lstUsuario.get(0);

                log.info("[TramitarComunicacionJSFBean] Usuario BD idUsuario: " + sgdUsuario.getIdUsuario());
                log.info("[TramitarComunicacionJSFBean] Usuario BD nombre: " + sgdUsuario.getNombreUsuario());
                log.info("[TramitarComunicacionJSFBean] Dependencia usuario logueado: " +
                         sgdUsuario.getCodigoDependencia());

                cargarUsuariosDependenciaUsuarioLogueado();
            } else {
                log.warn("[TramitarComunicacionJSFBean] No se encontró usuario en BD para: " + user);
                lstUsuariosObj.clear();
            }

            cargarDependencias();

            Object codDepDestinoObj = super.getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}");
            Object idAsignacionObj = super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}");

            log.debug("[TramitarComunicacionJSFBean] codDependenciaDestino: " + codDepDestinoObj);
            log.debug("[TramitarComunicacionJSFBean] idAsignacion: " + idAsignacionObj);

            if (codDepDestinoObj != null) {
                Long codDependenciaDestino = Long.parseLong(codDepDestinoObj.toString());

                log.info("[TramitarComunicacionJSFBean] Filtra usuarios FYI por codDependenciaDestino: " +
                         codDependenciaDestino);

                listaUsuarioDependencia =
                    comunicacionEJB.getSgdUsuarioFindByDependencia(codDependenciaDestino);

                log.info("[TramitarComunicacionJSFBean] Usuarios dependencia destino retornados: " +
                         (listaUsuarioDependencia != null ? listaUsuarioDependencia.size() : "NULL"));
            } else {
                listaUsuarioDependencia = new ArrayList<SgdUsuario>();
                log.warn("[TramitarComunicacionJSFBean] codDependenciaDestino viene NULL.");
            }

            if (idAsignacionObj != null && idAsignacionObj.toString().equals("3")) {
                log.info("[TramitarComunicacionJSFBean] Llena lista de usuarios para FYI");

                usuariosList = new ArrayList<SelectItem>();

                if (listaUsuarioDependencia != null && listaUsuarioDependencia.size() > 0) {
                    for (SgdUsuario usuario : listaUsuarioDependencia) {
                        usuariosList.add(new SelectItem(usuario, usuario.getNombreUsuario()));

                        log.debug("[TramitarComunicacionJSFBean] Usuario FYI agregado: " +
                                  usuario.getIdUsuario() + " - " + usuario.getNombreUsuario());
                    }
                }

                log.info("[TramitarComunicacionJSFBean] usuariosList FYI size: " + usuariosList.size());
            }

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR init()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN init()");
    }

    public List<SelectItem> listarUsuariosDependencia (FacesContext facesContext, AutoSuggestUIHints hints) {
        List<SelectItem> suggestItems = new ArrayList<SelectItem>();
        String submittedValue = hints.getSubmittedValue().toUpperCase(Locale.ENGLISH);
        
        if (hints.getSubmittedValue().length() < 3) {
            return suggestItems;
        }
        
        try {
            String prog = null;
            
            for (SgdUsuario usuario : listaUsuarioDependencia) {
                prog = usuario.getNombreUsuario().toUpperCase();
                
                if (prog.contains(submittedValue) && suggestItems.size() < 10) {
                    suggestItems.add(new SelectItem(usuario.getIdUsuario(), usuario.getNombreUsuario()));   
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggestItems;
    }
    
    public void seleccionarUsuariosDestino ( ValueChangeEvent evt ){
        System.out.println("Inicio seleccionarUsuariosDestino():"+evt.getOldValue());
        
        List<SgdUsuario> usuariosSeleccionados = (ArrayList<SgdUsuario>) evt.getNewValue();
        String usuariosFYI = "";
        
        try {
            if (usuariosSeleccionados != null ){    
                System.out.println("Usuario seleccionados: " + usuariosSeleccionados.size());
                
                for (SgdUsuario usuario : usuariosSeleccionados) {
                    System.out.println("usuario seleccionado: " + usuario.getIdUsuario());
                    
                    usuariosFYI = usuariosFYI + usuario.getIdUsuario() + ";";                    
                }
                
                super.setElObjectIntoBinding("#{bindings.usuariosDestinoFYI.inputValue}", usuariosFYI);
                System.out.println(super.getElObjectFromBinding("#{bindings.usuariosDestinoFYI.inputValue}"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }        
        
        System.out.println("Fin seleccionarUsuariosDestino():"+evt.getNewValue());
    }
    
    public void cambiarUsuarioDestino(ValueChangeEvent evt) {
        System.out.println("Inicio cambiarUsuarioDestino():"+evt.getOldValue());
        
        String newValue = (String)evt.getNewValue();
        
        try {
            if (newValue != null) {
                for (SgdUsuario usuario : listaUsuarioDependencia) {
                    if (usuario.getIdUsuario().equals(newValue)) {
                        System.out.println("setea el nuevo usuario asignado: " + newValue);
                        
                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioAsignado.inputValue}", usuario.getNombreUsuario());      
                        
                        usuarioSeleccionado = true;
                        
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Fin cambiarUsuarioDestino():"+evt.getNewValue());
    }

    public void asignarComunicacion(ActionEvent evt) {
        try {
            if (((Boolean) super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}"))) {
                log.info("[TramitarComunicacionJSFBean] La comunicación debe responderse");
                super.setElObjectIntoBinding("#{bindings.requiereRespuesta.inputValue}", Boolean.TRUE);
                super.setElObjectIntoBinding("#{bindings.nroFolios.inputValue}", 0);
            }

            /*
             * Guarda en el payload de la HT el tipo de asignación seleccionado:
             * U = Asignar Usuario del Área
             * D = Remitir a otra dependencia
             */
            try {
                super.setElObjectIntoBinding("#{bindings.tipoAsignacion.inputValue}", tipoAsignacionDO);

                log.info("[TramitarComunicacionJSFBean] ACEPTAR - tipoAsignacion seteado: " + tipoAsignacionDO);
                log.info("[TramitarComunicacionJSFBean] ACEPTAR - tipoAsignacion binding final: " +
                         super.getElObjectFromBinding("#{bindings.tipoAsignacion.inputValue}"));
            } catch (Exception exTipoAsignacion) {
                log.error("[TramitarComunicacionJSFBean] ERROR seteando tipoAsignacion", exTipoAsignacion);
                super.showMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar el tipo de asignación.");
                return;
            }

            Object idAsignacionObj = super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}");
            Object idUsuarioObj = super.getElObjectFromBinding("#{bindings.idUsuario.inputValue}");

            Object codDependenciaDestinoObj = null;
            try {
                codDependenciaDestinoObj = super.getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}");
            } catch (Exception exCodDepDestino) {
                log.warn("[TramitarComunicacionJSFBean] No se pudo leer codDependenciaDestino", exCodDepDestino);
            }

            log.info("[TramitarComunicacionJSFBean] ACEPTAR - idAsignacion: " + idAsignacionObj);
            log.info("[TramitarComunicacionJSFBean] ACEPTAR - tipoAsignacionDO: " + tipoAsignacionDO);
            log.info("[TramitarComunicacionJSFBean] ACEPTAR - idUsuario final: " + idUsuarioObj);
            log.info("[TramitarComunicacionJSFBean] ACEPTAR - codDependenciaDestino final: " + codDependenciaDestinoObj);

            if ("U".equals(tipoAsignacionDO) &&
                (idUsuarioObj == null || idUsuarioObj.toString().trim().equals(""))) {

                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar un usuario.");
                return;
            }

            if ("D".equals(tipoAsignacionDO) &&
                (codDependenciaDestinoObj == null || codDependenciaDestinoObj.toString().trim().equals(""))) {

                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar una dependencia destino.");
                return;
            }

            if (idAsignacionObj != null &&
                idAsignacionObj.toString().equals("3") &&
                (super.getElObjectFromBinding("#{bindings.usuariosDestinoFYI.inputValue}") == null ||
                 super.getElObjectFromBinding("#{bindings.usuariosDestinoFYI.inputValue}").toString().trim().equals(""))) {

                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar al menos un usuario.");
                return;
            }

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR asignarComunicacion()", e);
            super.showMessage(FacesMessage.SEVERITY_ERROR, "Ocurrió un error al asignar la comunicación.");
            return;
        }

        super.setOperation(evt);
    }

    public void setUsuariosFYI(List<SelectItem> usuariosFYI) {
        this.usuariosFYI = usuariosFYI;   
    }

    public List<SelectItem> getUsuariosFYI() {
        return usuariosFYI;
    }

    public void setUsuariosList(List<SelectItem> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public List<SelectItem> getUsuariosList() {
        return usuariosList;
    }    

    public void setListaUsuarioDependencia(List<SgdUsuario> listaUsuarioDependencia) {
        this.listaUsuarioDependencia = listaUsuarioDependencia;
    }

    public List<SgdUsuario> getListaUsuarioDependencia() {
        return listaUsuarioDependencia;
    }
    
    public void setTextoBusqueda(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda;
    }

    public String getTextoBusqueda() {
        return textoBusqueda;
    }    
    
    
    //METODOS NUEVOS
    
    
    private String tipoAsignacionDO;

    public String getTipoAsignacionDO() {
        return tipoAsignacionDO;
    }

    public void setTipoAsignacionDO(String tipoAsignacionDO) {
        this.tipoAsignacionDO = tipoAsignacionDO;
    }
    
    public void cambiarAsignacion(ValueChangeEvent vce) {
        log.info("==================================================");
        log.info("[TramitarComunicacionJSFBean] INICIO cambiarAsignacion()");
        log.info("[TramitarComunicacionJSFBean] oldValue: " + vce.getOldValue());
        log.info("[TramitarComunicacionJSFBean] newValue: " + vce.getNewValue());

        try {
            tipoAsignacionDO = vce.getNewValue() != null ? vce.getNewValue().toString() : null;
            log.info("[TramitarComunicacionJSFBean] tipoAsignacionDO: " + tipoAsignacionDO);

            log.debug("[TramitarComunicacionJSFBean] idUsuario ANTES limpiar: " +
                      super.getElObjectFromBinding("#{bindings.idUsuario.inputValue}"));

            try {
                super.setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", null);
                log.debug("[TramitarComunicacionJSFBean] idUsuario limpiado.");
            } catch (Exception exIdUsuario) {
                log.warn("[TramitarComunicacionJSFBean] No se pudo limpiar idUsuario", exIdUsuario);
            }

            if ("U".equals(tipoAsignacionDO)) {
                log.info("[TramitarComunicacionJSFBean] Seleccionó U: cargar usuarios del área del usuario logueado.");
                cargarUsuariosDependenciaUsuarioLogueado();
            } else if ("D".equals(tipoAsignacionDO)) {
                log.info("[TramitarComunicacionJSFBean] Seleccionó D: se usará lista de dependencias.");
            } else {
                log.warn("[TramitarComunicacionJSFBean] Valor no esperado tipoAsignacionDO: " + tipoAsignacionDO);
            }

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cambiarAsignacion()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cambiarAsignacion()");
        log.info("==================================================");
    }
    
    public void cambiarDependencia(ValueChangeEvent vce) {
        log.info("==================================================");
        log.info("[TramitarComunicacionJSFBean] INICIO cambiarDependencia()");
        log.info("[TramitarComunicacionJSFBean] oldValue: " + vce.getOldValue());
        log.info("[TramitarComunicacionJSFBean] newValue: " + vce.getNewValue());

        try {
            if (vce.getNewValue() == null) {
                log.warn("[TramitarComunicacionJSFBean] Dependencia seleccionada viene NULL.");
                return;
            }

            log.debug("[TramitarComunicacionJSFBean] Clase newValue: " + vce.getNewValue().getClass());

            Long codDependencia = Long.parseLong(vce.getNewValue().toString());

            log.info("[TramitarComunicacionJSFBean] codDependencia seleccionado: " + codDependencia);

            /*
             * Este es el binding correcto en esta HT.
             * En el PageDef corresponde a dependenciaDestino.codigo.
             */
            try {
                super.setElObjectIntoBinding("#{bindings.codDependenciaDestino.inputValue}", codDependencia);

                log.info("[TramitarComunicacionJSFBean] codDependenciaDestino seteado en payload: " +
                         super.getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}"));

            } catch (Exception exCodDepDestino) {
                log.error("[TramitarComunicacionJSFBean] No se pudo setear codDependenciaDestino.",
                          exCodDepDestino);
                super.showMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar la dependencia destino.");
                return;
            }

            /*
             * Si escoge dependencia, no debe quedar usuario manual seleccionado.
             * El BPM buscará el jefe de esa dependencia.
             */
            try {
                super.setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", null);

                log.info("[TramitarComunicacionJSFBean] idUsuario limpiado por selección de dependencia.");

            } catch (Exception exIdUsuario) {
                log.warn("[TramitarComunicacionJSFBean] No se pudo limpiar idUsuario.", exIdUsuario);
            }

            try {
                log.info("[TramitarComunicacionJSFBean] idUsuario payload actual: " +
                         super.getElObjectFromBinding("#{bindings.idUsuario.inputValue}"));
            } catch (Exception exGetIdUsuario) {
                log.warn("[TramitarComunicacionJSFBean] No se pudo leer idUsuario.", exGetIdUsuario);
            }

            log.info("[TramitarComunicacionJSFBean] Dependencia seleccionada procesada correctamente.");

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cambiarDependencia()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cambiarDependencia()");
        log.info("==================================================");
    }
    
    public void cambiarUsuario(ValueChangeEvent evt) {
        log.info("==================================================");
        log.info("[TramitarComunicacionJSFBean] INICIO cambiarUsuario()");
        log.info("[TramitarComunicacionJSFBean] oldValue: " + evt.getOldValue());
        log.info("[TramitarComunicacionJSFBean] newValue: " + evt.getNewValue());

        try {
            if (evt.getNewValue() == null) {
                log.warn("[TramitarComunicacionJSFBean] Usuario seleccionado viene NULL.");
                return;
            }

            log.debug("[TramitarComunicacionJSFBean] Clase newValue: " + evt.getNewValue().getClass());

            SgdUsuario usuarioSelected = (SgdUsuario) evt.getNewValue();

            log.info("[TramitarComunicacionJSFBean] usuarioSelected.idUsuario: " + usuarioSelected.getIdUsuario());
            log.info("[TramitarComunicacionJSFBean] usuarioSelected.nombre: " + usuarioSelected.getNombreUsuario());
            log.info("[TramitarComunicacionJSFBean] usuarioSelected.codDependencia: " + usuarioSelected.getCodigoDependencia());
            log.info("[TramitarComunicacionJSFBean] usuarioSelected.email: " + usuarioSelected.getEmail());

            try {
                super.setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuarioSelected.getIdUsuario());
                log.info("[TramitarComunicacionJSFBean] idUsuario seteado en payload: " +
                         super.getElObjectFromBinding("#{bindings.idUsuario.inputValue}"));
            } catch (Exception exIdUsuario) {
                log.error("[TramitarComunicacionJSFBean] ERROR seteando idUsuario. Este binding es obligatorio.", exIdUsuario);
                super.showMessage(FacesMessage.SEVERITY_ERROR, "No se pudo asignar el usuario seleccionado.");
                return;
            }

           /* try {
                super.setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", usuarioSelected.getCodigoDependencia());
                log.info("[TramitarComunicacionJSFBean] codDependencia seteado en payload: " +
                         super.getElObjectFromBinding("#{bindings.codDependencia.inputValue}"));
            } catch (Exception exCodDep) {
                log.warn("[TramitarComunicacionJSFBean] No existe binding codDependencia o no se pudo setear.", exCodDep);
            }*/

            try {
                super.setElObjectIntoBinding("#{bindings.nombre.inputValue}", usuarioSelected.getNombreUsuario());
                log.info("[TramitarComunicacionJSFBean] nombre seteado en payload.");
            } catch (Exception exNombre) {
                log.warn("[TramitarComunicacionJSFBean] No existe binding nombre o no se pudo setear.", exNombre);
            }

            try {
                super.setElObjectIntoBinding("#{bindings.nombreUsuarioAsignado.inputValue}", usuarioSelected.getNombreUsuario());
                log.info("[TramitarComunicacionJSFBean] nombreUsuarioAsignado seteado en payload.");
            } catch (Exception exNombreAsignado) {
                log.warn("[TramitarComunicacionJSFBean] No existe binding nombreUsuarioAsignado o no se pudo setear.", exNombreAsignado);
            }

           /* try {
                super.setElObjectIntoBinding("#{bindings.email.inputValue}", usuarioSelected.getEmail());
                log.info("[TramitarComunicacionJSFBean] email seteado en payload.");
            } catch (Exception exEmail) {
                log.warn("[TramitarComunicacionJSFBean] No existe binding email o no se pudo setear.", exEmail);
            }*/

            usuarioSeleccionado = true;

            log.info("[TramitarComunicacionJSFBean] Usuario seleccionado procesado correctamente.");

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cambiarUsuario()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cambiarUsuario()");
        log.info("==================================================");
    }
    
    private void cargarUsuariosDependencia(Long codDependencia) {
        log.info("--------------------------------------------------");
        log.info("[TramitarComunicacionJSFBean] INICIO cargarUsuariosDependencia()");
        log.info("[TramitarComunicacionJSFBean] codDependencia: " + codDependencia);

        try {
            lstUsuariosObj.clear();

            log.debug("[TramitarComunicacionJSFBean] lstUsuariosObj limpiada.");

            List<SgdUsuario> listaUsuarios = comunicacionEJB.getSgdUsuarioFindByDependencia(codDependencia);

            if (listaUsuarios == null) {
                log.warn("[TramitarComunicacionJSFBean] listaUsuarios viene NULL.");
                return;
            }

            log.info("[TramitarComunicacionJSFBean] listaUsuarios size: " + listaUsuarios.size());

            for (SgdUsuario usuario : listaUsuarios) {
                log.debug("[TramitarComunicacionJSFBean] Usuario agregado combo: "
                          + usuario.getIdUsuario() + " - "
                          + usuario.getNombreUsuario() + " - dep "
                          + usuario.getCodigoDependencia());

                lstUsuariosObj.add(new SelectItem(usuario, usuario.getNombreUsuario()));
            }

            log.info("[TramitarComunicacionJSFBean] lstUsuariosObj final size: " + lstUsuariosObj.size());

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cargarUsuariosDependencia()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cargarUsuariosDependencia()");
        log.info("--------------------------------------------------");
    }
    
    public List<SelectItem> getLstUsuariosObj() {
        log.debug("[TramitarComunicacionJSFBean] getLstUsuariosObj() size: " +
                  (lstUsuariosObj != null ? lstUsuariosObj.size() : "NULL"));
        return lstUsuariosObj;
    }

    public void setLstUsuariosObj(List<SelectItem> lstUsuariosObj) {
        this.lstUsuariosObj = lstUsuariosObj;
    }

    public List<SelectItem> getLstDependencias2() {
        log.debug("[TramitarComunicacionJSFBean] getLstDependencias2() size: " +
                  (lstDependencias2 != null ? lstDependencias2.size() : "NULL"));
        return lstDependencias2;
    }

    public void setLstDependencias2(List<SelectItem> lstDependencias2) {
        this.lstDependencias2 = lstDependencias2;
    }
    
    private void cargarUsuariosDependenciaUsuarioLogueado() {
        log.info("[TramitarComunicacionJSFBean] INICIO cargarUsuariosDependenciaUsuarioLogueado()");

        try {
            lstUsuariosObj.clear();

            if (sgdUsuario == null) {
                log.warn("[TramitarComunicacionJSFBean] sgdUsuario es NULL. No se cargan usuarios.");
                return;
            }

            if (sgdUsuario.getCodigoDependencia() == null) {
                log.warn("[TramitarComunicacionJSFBean] codigoDependencia del usuario logueado es NULL.");
                return;
            }

            Long codDependenciaUsuario = sgdUsuario.getCodigoDependencia();

            log.info("[TramitarComunicacionJSFBean] Cargando usuarios de la dependencia del logueado: " +
                     codDependenciaUsuario);

            List<SgdUsuario> listaUsuarios =
                comunicacionEJB.getSgdUsuarioFindByDependencia(codDependenciaUsuario);

            log.info("[TramitarComunicacionJSFBean] listaUsuarios dependencia logueado size: " +
                     (listaUsuarios != null ? listaUsuarios.size() : "NULL"));

            if (listaUsuarios != null) {
                for (SgdUsuario usuario : listaUsuarios) {
                    log.debug("[TramitarComunicacionJSFBean] Usuario combo área: "
                              + usuario.getIdUsuario() + " - "
                              + usuario.getNombreUsuario() + " - dep "
                              + usuario.getCodigoDependencia());

                    lstUsuariosObj.add(new SelectItem(usuario, usuario.getNombreUsuario()));
                }
            }

            log.info("[TramitarComunicacionJSFBean] lstUsuariosObj final size: " + lstUsuariosObj.size());

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cargarUsuariosDependenciaUsuarioLogueado()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cargarUsuariosDependenciaUsuarioLogueado()");
    }
    
    private void cargarDependencias() {
        log.info("[TramitarComunicacionJSFBean] INICIO cargarDependencias()");

        try {
            lstDependencias2.clear();

            List<UnidadAdministrativa> dependencias =
                comunicacionEJB.getUnidadAdministrativaFindAll();

            log.info("[TramitarComunicacionJSFBean] dependencias size: " +
                     (dependencias != null ? dependencias.size() : "NULL"));

            if (dependencias != null) {
                for (UnidadAdministrativa unidad : dependencias) {
                    lstDependencias2.add(new SelectItem(
                        unidad.getCodUnidadadministrativa().longValue(),
                        unidad.getCodUnidadadministrativa() + " - " + unidad.getNombreunidadadministrativa()
                    ));

                    log.debug("[TramitarComunicacionJSFBean] Dependencia agregada: " +
                              unidad.getCodUnidadadministrativa() + " - " +
                              unidad.getNombreunidadadministrativa());
                }
            }

            log.info("[TramitarComunicacionJSFBean] lstDependencias2 final size: " + lstDependencias2.size());

        } catch (Exception e) {
            log.error("[TramitarComunicacionJSFBean] ERROR cargarDependencias()", e);
        }

        log.info("[TramitarComunicacionJSFBean] FIN cargarDependencias()");
    }

    

}
