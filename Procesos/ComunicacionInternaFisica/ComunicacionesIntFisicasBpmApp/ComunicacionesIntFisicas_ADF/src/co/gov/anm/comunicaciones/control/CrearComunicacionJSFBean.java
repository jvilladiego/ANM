package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal;
import co.gov.anm.comunicaciones.bean.WCContentLocal;
import co.gov.anm.comunicaciones.entity.Anexo;
import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.AnmTipodtalseguntramite;
import co.gov.anm.comunicaciones.entity.AnmTramiteTb;
import co.gov.anm.comunicaciones.entity.AnmUnidadadministrativaTb;
import co.gov.anm.comunicaciones.entity.Folders;
import co.gov.anm.comunicaciones.entity.SgdUsuario;
import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.WccResponse;
import co.gov.anm.sgd.util.SGDWebServiceLocator;

import java.io.OutputStream;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;


@ManagedBean(name="CrearComunicacionJSFBean")
@ViewScoped
public class CrearComunicacionJSFBean extends CommonJSFBean {


    private Logger log;
    private String user;
    private List<AnmPlantilla> lstPlantilla = new ArrayList<AnmPlantilla>();
    
    @EJB
    private ComunicacionesSessionBeanLocal comunicacionEJB;
    
    @EJB
    private WCContentLocal wcContentEJB;
    
    private Collection<SelectItem> categoriasProceso = new ArrayList<>();
    private Collection<Folders> categoriasProcesoSet = new ArrayList<>();
    private Collection<SelectItem> procesos = new ArrayList<>();
    private Collection<Folders> procesosSet = new ArrayList<>();
    private Collection<SelectItem> series = new ArrayList<>();
    private Collection<Folders> seriesSet = new ArrayList<>();
    private Collection<SelectItem> subseries = new ArrayList<>();
    private Collection<Folders> subseriesSet = new ArrayList<>();
    private Collection<SelectItem> cuadernos = new ArrayList<>();
    private Collection<Folders> cuadernosSet = new ArrayList<>();
    private Collection<SelectItem> tramites = new ArrayList<>();
    private Collection<AnmTramiteTb> tramitesSet = new ArrayList<>();
    private Collection<SelectItem> tiposDocumentales = new ArrayList<>();
    private Collection<AnmTipodtalseguntramite> tiposDocumentalesSet = new ArrayList<>();
    private Collection<SelectItem> dependencias = new ArrayList<>();
    private Collection<AnmUnidadadministrativaTb> dependenciasSet = new ArrayList<>();
    private Collection<SelectItem> usuariosDependencia = new ArrayList<>();
    private Collection<SgdUsuario> usuariosSet = new ArrayList<>();
    
    private ExpedienteMinero port = new SGDWebServiceLocator().getExpedienteMineroProxy();
    
    private final static String PARENT_TAX_NAME = "ANM";    
    
    //Tabla de anexos no digitalizables
    private List<Anexo> lstAnexo = new ArrayList<Anexo>();
    private Anexo selectedAnexo;
    private Anexo nuevoAnexo = new Anexo();
    //Componentes
    private RichTable tanexo;
    
    private RichInputFile ifAnexo;



    public CrearComunicacionJSFBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN CrearComunicacionJSFBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
            //setElObjectIntoBinding("#{bindings.name1.inputValue}", "");
        } catch (Exception e) {
            log.error(user + " -> Exception CrearComunicacionJSFBean", e);
        }
        log.info("END CrearComunicacionJSFBean");
    }
    
    
    
    @PostConstruct
    public void init () throws Exception {
        try {
           //cargar dependencias        
           dependenciasSet = wcContentEJB.getUnidadAdministrativaFindAll();
           
           for (AnmUnidadadministrativaTb unidAdmin : dependenciasSet) {
               this.dependencias.add(new SelectItem(unidAdmin.getCodUnidadadministrativa().toString(), 
                           unidAdmin.getCodUnidadadministrativa() + " - " + unidAdmin.getNombreunidadadministrativa()));
           }
           
           Collection<Folders> taxonomiaParent = this.wcContentEJB.getFoldersFindByFolderName(PARENT_TAX_NAME);
           String parentGuid = null;
           if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
               throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
           }

           for (Folders f : taxonomiaParent) {
               parentGuid = f.getFfolderguid();
               break;
           }
           this.categoriasProcesoSet = this.wcContentEJB.getFoldersFindByParentGuid(parentGuid);
           this.categoriasProceso = new ArrayList<SelectItem>(categoriasProcesoSet.size());
           for (Folders f : categoriasProcesoSet) {
               this.categoriasProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
           }
           
           this.tramitesSet = this.wcContentEJB.getTramiteFindAll();
           this.tramites = new ArrayList<SelectItem>(this.tramitesSet.size());
           for (AnmTramiteTb f : tramitesSet) {
               //log.debug(user+" -> Cargando Tramite: " + f.getIdtramite());
               this.tramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
           }
           
           //inicializa check boxes
           super.setElObjectIntoBinding("#{bindings.responderComunicacion.inputValue}", Boolean.FALSE);   
           super.setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);   
           
           log.debug(user+" ->Obtiene nombre de la dependencia remitente con codigo: " + 
                   (String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}"));
            if(getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}")!=null){
                List<AnmUnidadadministrativaTb> unidAdminList = wcContentEJB.getUnidadAdministrativaFindByCod(
                    new BigDecimal((String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}")));
                
                super.setElObjectIntoBinding("#{bindings.nombreDepRemitente.inputValue}", unidAdminList.get(0).getNombreunidadadministrativa());
            }
           /*try {
               log.debug(user+" ->url wsdl: " + (String)super.getElObjectFromBinding("#{bindings.urlExpMineroWSDL.inputValue}"));
               port = ServiceLocator.getInstance().getExpMineroPort((String)super.getElObjectFromBinding("#{bindings.urlExpMineroWSDL.inputValue}"));
           } catch (Exception e) {
               log.error();
           }*/
            //Cargar listado plantillas
            lstPlantilla = comunicacionEJB.getAnmPlantillaFindAll();
            log.debug(user+" -> lstPlantilla size: "+lstPlantilla.size());
       } catch (Exception e) {
            log.error("Exception init",e);
        }
    }
    
    
    
    public void cambiarDependenciaDestino ( ValueChangeEvent evt ){
        try{
            log.debug(user+" ->Inicio cambiarDependenciaDestino():"+evt.getOldValue());
            String newValue = (String)evt.getNewValue();
            if (newValue != null){                            
                for (AnmUnidadadministrativaTb depDestino : dependenciasSet) {
                    if (newValue.equals(depDestino.getCodUnidadadministrativa().toString())) {
                        super.setElObjectIntoBinding("#{bindings.codDepDestino.inputValue}", depDestino.getCodUnidadadministrativa().toString());
                        super.setElObjectIntoBinding("#{bindings.nombreDepDestino.inputValue}", depDestino.getNombreunidadadministrativa());
                        super.setElObjectIntoBinding("#{bindings.idDepDestino.inputValue}", depDestino.getIdUnidadadministrativa());
                        
                        Long codDependencia = Long.parseLong(depDestino.getCodUnidadadministrativa().toString());
                        
                        log.debug(user+" ->Filtra usuarios por codigo dependencia: " + codDependencia);
                        
                        usuariosSet = this.comunicacionEJB.getSgdUsuarioFindByDependencia(codDependencia);
                        
                        usuariosDependencia = new ArrayList<SelectItem>();
                        
                        for (SgdUsuario usuario : usuariosSet) {
                            this.usuariosDependencia.add(new SelectItem(usuario.getIdUsuario(), usuario.getNombreUsuario()));
                        }
                        
                        log.debug(user+" ->Usuarios retornados: " + this.usuariosDependencia.size());   
                        
                        break;
                    }
                }
            }    
        } catch(Exception e){
            log.error("Exception cambiarDependenciaDestino",e);
        }        
        log.debug(user+" ->Fin cambiarDependenciaDestino():"+evt.getNewValue());
    }
    
    
    
    public void cambiarUsuarioDestino (ValueChangeEvent evt) {
        log.debug(user+" ->cambiarUsuarioDestino():"+evt.getNewValue());
        String selectedValue = (String)evt.getNewValue();
        
        try {
            if (selectedValue != null) {
                
                for (SgdUsuario usuario : usuariosSet) {
                    if (selectedValue.equals(usuario.getIdUsuario())) {
                        super.setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuario.getIdUsuario());
                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}", usuario.getNombreUsuario());
                        super.setElObjectIntoBinding("#{bindings.email.inputValue}", usuario.getEmail());
                        
                        log.debug(user+" ->id usuario seleccionado: " + usuario.getIdUsuario());
                        log.debug(user+" ->nombre usuario seleccionado: " + usuario.getNombreUsuario());
                        log.debug(user+" ->email usuario seleccionado: " + usuario.getEmail());
                        
                        break;
                    }
                }
                
                log.debug(user+" ->id asignado: " + super.getElObjectFromBinding("#{bindings.idUsuarioCom.inputValue}"));
                log.debug(user+" ->usuario asignado: " + super.getElObjectFromBinding("#{bindings.nombreUsuarioCom.inputValue}"));
            }
        } catch (Exception e) {
            log.error("Exception cambiarUsuarioDestino",e);
        }
    }   
    
    public void cambiarRequiereRespuesta (ValueChangeEvent evt) {
        log.debug(user+" ->Inicio cambiarRequiereRespuesta():"+evt.getOldValue());
        
        Boolean selectedValue = (Boolean)evt.getNewValue();
        
        try {
            if (selectedValue != null) {                   
                if (selectedValue.booleanValue()) {                     
                    super.setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 1);                                            
                } else {
                    super.setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 0);                               
                }
           }               
        } catch (Exception e) {
            log.error("Exception cambiarRequiereRespuesta",e);
        }
        log.debug(user+" ->Fin cambiarRequiereRespuesta():"+evt.getNewValue());
    }    
    
    
    
    public void cambiarEsExpMinero (ValueChangeEvent evt) {
        log.debug(user+" ->cambiarEsExpMinero():"+evt.getNewValue());
        Boolean selectedValue = (Boolean)evt.getNewValue();
        
        try {
            if (selectedValue != null) {                
                if (selectedValue.equals(Boolean.FALSE)) {
                    log.debug(user+" ->exp minero false");
                    
                    super.setElObjectIntoBinding("#{bindings.nroPlaca.inputValue}", "");   
                    
                    super.setElObjectIntoBinding("#{bindings.idTramite.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
                } else {
                    log.debug(user+" ->exp minero true");
                    
                    super.setElObjectIntoBinding("#{bindings.idCatProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.catProceso.inputValue}", null);
                                                                    
                    super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);
                    
                    super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);    

                    super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreSubserie.inputValue}", null);
                }
            }
        } catch (Exception e) {
            log.error("Exception cambiarEsExpMinero",e);
        }
    }


    /**
     * @actualizado por: Adrian Molina
     * @fecha 27Abril2017
     * @param evt
     */
    public void cambiarCategoriaProceso ( ValueChangeEvent evt ){
        try {
            log.debug(user+" ->inicio cambiarCategoriaProceso(): "+evt.getNewValue());
            
            String catProcesoSel = (String)evt.getNewValue();
            this.procesosSet = this.wcContentEJB.getFoldersFindByParentGuid(catProcesoSel);
            this.procesos = new ArrayList<SelectItem>(this.procesosSet.size());
            
            
            for ( Folders f:procesosSet ){
                this.procesos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
            
            //setea los demas datos de la taxonomia        
            for ( Folders f:this.categoriasProcesoSet ){
                if ( f.getFfolderguid().equals(catProcesoSel) ){
                    super.setElObjectIntoBinding("#{bindings.catProceso.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            this.cambiarProceso(null);
            log.debug(user+" ->Fin: cambiarCategoriaProceso()");        
        } catch (Exception e) {
            log.error("Exception cambiarCategoriaProceso",e);
        }
        
    }
    
    public void cambiarProceso ( ValueChangeEvent evt ){
        log.debug(user+" ->inicio cambiarProceso(): "+(evt!=null&&evt.getNewValue()!=null?evt.getNewValue():"vacio"));
        
        if ( evt==null || evt.getNewValue()==null ){
            this.series = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);    
        } else {
            String idProceso = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", idProceso);
            
            for ( Folders f:this.procesosSet ){
                if ( f.getFfolderguid().equals(idProceso) ){
                    super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", f.getFfoldername());        
                    break;
                }
            }
                                    
            this.seriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idProceso);
            log.debug(user+" ->series consultadas: " + seriesSet.size());
            this.series = new ArrayList<SelectItem>(this.seriesSet.size());            
            
            for ( Folders f:seriesSet ){
                this.series.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }            
        }        
        this.cambiarSerie(null);
    }

    public void cambiarSerie ( ValueChangeEvent evt ){
        log.debug(user+" ->Inicio cambiarSerie()");
        if ( evt==null  || evt.getNewValue()==null  ){
            this.subseries = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);    
        }
        else{
            String idSerie = (String)evt.getNewValue();            
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", idSerie);
            
            for ( Folders f:this.seriesSet ){
                if ( f.getFfolderguid().equals(idSerie) ){
                    log.debug(user+" ->serie seleccionada: " + idSerie + ", " + f.getFfoldername());
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            log.debug(super.getElObjectFromBinding("#{bindings.idSerie.inputValue}" + 
                                                            super.getElObjectFromBinding("#{bindings.serie.inputValue}")));
            
            this.subseriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idSerie);
            log.debug(user+" ->subseries consultadas: " + subseriesSet.size());
            this.subseries = new ArrayList<SelectItem>(this.subseriesSet.size());
            for ( Folders f:subseriesSet ){
                this.subseries.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarSubSerie(null);
    }    
    
    public void cambiarSubSerie ( ValueChangeEvent evt ){
        log.debug(user+" ->Inicio: cambiarSubSerie()");
        
        if ( evt==null || evt.getNewValue()==null  ){
            this.cuadernos = new ArrayList<SelectItem>(0);
            this.cuadernosSet = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);    
        } else {
            String idSubSerie = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", idSubSerie);
            
            for ( Folders f:this.subseriesSet ){
                if ( f.getFfolderguid().equals(idSubSerie) ){
                    log.debug(user+" ->serie seleccionada: " + idSubSerie + ", " + f.getFfoldername());
                    super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            log.debug(super.getElObjectFromBinding("#{bindings.idSubSerie.inputValue}" + 
                                                            super.getElObjectFromBinding("#{bindings.subserie.inputValue}")));
            
            this.cuadernosSet = this.wcContentEJB.getFoldersFindByParentGuid(idSubSerie);
            this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());            
            
            for ( Folders f:cuadernosSet ){
                this.cuadernos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarExpediente(null);
    }
    
    public void cambiarExpediente ( ValueChangeEvent evt ){
        log.debug(user+" ->Inicio: cambiarExpediente()");
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", null);    
        } else {
            String cuadernoSel = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", cuadernoSel);
            
            for ( Folders f:this.cuadernosSet ){
                if ( f.getFfolderguid().equals(cuadernoSel) ){
                    super.setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", f.getFfoldername());        
                    break;
                }
            }
        }        
    }    
    
    public void cambiarTramite ( ValueChangeEvent evt ){
        log.debug(user+" ->Inicio: cambiarTramite(): "+evt.getNewValue());
        
        if (evt==null || evt.getNewValue()==null){
            super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);
            this.tiposDocumentalesSet = new ArrayList<AnmTipodtalseguntramite>();
            this.tiposDocumentales = new ArrayList<SelectItem>();
            return;
        }
        
        String tramiteSelected = (String)evt.getNewValue();
        for ( AnmTramiteTb t:this.tramitesSet ){
            if ( tramiteSelected.equals(t.getIdtramite().toString()) ){
                super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                break;
            }
        }
        
        this.tiposDocumentalesSet = this.wcContentEJB.getTipoDocumentalTramiteTiposDocumentalesTramites(new BigDecimal(tramiteSelected));
        
        log.debug(user+" ->tipos documentales encontrados para el tramite: " + tiposDocumentalesSet.size());
        
        this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
        for ( AnmTipodtalseguntramite f : this.tiposDocumentalesSet ){
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
        }
    }
    
    public void cambiarTipoDocumental ( ValueChangeEvent evt ){
        log.debug(user+" ->Inicio: cambiarTipoDocumento():"+evt.getNewValue());
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);    
        } else {
            String tipoDocSele = (String)evt.getNewValue();
            
            for ( AnmTipodtalseguntramite t:this.tiposDocumentalesSet ){
                if ( tipoDocSele.equals(t.getIdtipodtal().toString()) ){
                    log.debug(user+" ->encuentra tipo documental");                                        
                    
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", t.getIdtipodtal());
                    
                    log.debug(user+" ->asigna tipo documental");
                    break;
                }
            }
        }
    }
    
    public void validarExpMinero (ValueChangeEvent event) {
        log.debug(user+" ->validarExpMinero():"+event.getNewValue());
        WccResponse response = null;
        
        String expMinero = (String) event.getNewValue();
        
        try {	
            response = port.validarExpedienteMinero(expMinero);
            
            if (response.getStatusCode().equals("ERWCC03")) {
                log.debug(user+" ->Error de validacion");
                super.showMessage(FacesMessage.SEVERITY_WARN, "El expediente minero no existe. Por favor validar.");
            } else {
                log.debug(user+" ->Expediente minero validado");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }    
    
    public void crearComunicacion ( ActionEvent evt ){
        log.debug(user+" ->responder com: " + (Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}"));
        log.debug(user+" ->tiempo resp: " + (Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}"));
        log.debug(user+" ->nro folios: " + (Integer)super.getElObjectFromBinding("#{bindings.nroFolios.inputValue}"));
                
        Boolean esTituloMinero = (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}");
        String idTramite = (String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}");
        String idTpDocumento = (String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}");
        String idSerie = (String)super.getElObjectFromBinding("#{bindings.idSerie.inputValue}");
        String nroPlaca = (String)super.getElObjectFromBinding("#{bindings.nroPlaca.inputValue}");
        
        log.debug(user+" ->id tramite: " + idTramite);
        log.debug(user+" ->id tp doc: " + idTpDocumento);
        log.debug(user+" ->id serie: " + idSerie);
        
        try {
            if ((esTituloMinero) && (nroPlaca == null || nroPlaca.trim().equals(""))) {
                log.debug(user+" ->Error en validaci�n en tr�mite");
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe digitar 'Número de placa' para continuar.");
                return;
            }
            
            if (esTituloMinero&&
                ( (idTramite == null || idTramite.trim().equals("") || idTpDocumento == null || idTpDocumento.trim().equals("") ))) {
                    log.debug(user+" -> Error en validacion en tramite");
                    
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tramite y el tipo documental.");
                    
                return;
            }
            
            log.debug("esTituloMinero: "+esTituloMinero);
            log.debug("idTramite: "+idTramite);
            log.debug("idTpDocumento: "+idTpDocumento);
            log.debug("idSerie: "+idSerie);
            
            if (!esTituloMinero && (idSerie == null || idSerie.trim().equals(""))) {
                log.debug(user+" ->Error en validación en trámite");
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonomía completa. Por favor verificar");
                
                return;
            }
            
            
            /*
             * Actualizado por: Adrian Molina el 29Marzo2017
             * Motivo: Se deshabilita el siguiente bloque de codigo para dar cumplimiento a las condiciones
             *  del negocio
             */
            /*
            if (!((Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) &&
                ((String)super.getElObjectFromBinding("#{bindings.idSubSerie.inputValue}")) == null) {
                log.debug(user+" ->Error en validaci�n en taxonom�a");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonom�a completa. Por favor verificar");
                
                return;
            }
            */
            
            if (((Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}")) && 
                ((Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}")) <= 0) {
                log.debug(user+" ->error de validacion en responder comunicación");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "El tiempo de respuesta debe ser mayor a cero (0). Por favor verificar.");
                
                return;
            }
            
            if (((Integer)super.getElObjectFromBinding("#{bindings.nroFolios.inputValue}")) <= 0) {
                log.debug(user+" ->error de validacion en numero de folios");
                super.showMessage(FacesMessage.SEVERITY_WARN, "El número de folios debe ser mayor a cero (0). Por favor verificar.");
                
                return;
            }
            
            /*DocMgmtBean docMngr = (DocMgmtBean)super.getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            log.debug(user+" ->Cantidad de attachements:"+docMngr.getAddedAttachmentList().size());
            int cantidadAttachments = docMngr.getAddedAttachmentList().size();
            
            if (cantidadAttachments == 0) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado algún documento. Por favor verificar.");
                
                return;
            } else {                
                for (AttachmentTypeImpl attachment : docMngr.getAddedAttachmentList()) {
                    log.debug(attachment.getMimeType());
                }
                
                if (!docMngr.getAddedAttachmentList().get(0).getMimeType().equals("application/vnd.oasis.opendocument.text")) {
                    super.showMessage(FacesMessage.SEVERITY_WARN, "La extensión del documento debe ser .odt. Por favor verificar.");
                    
                    return; 
                }
            }*/
            String docName = (String)getElObjectFromBinding("#{bindings.name1.inputValue}");
            String mimeType = (String)getElObjectFromBinding("#{bindings.mimetype.inputValue}");
            Integer size = (Integer)getElObjectFromBinding("#{bindings.size.inputValue}");
            //String content = getElObjectFromBinding("#{bindings.content.inputValue}");
            
            if (ifAnexo.getValue() == null) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado alg�n documento principal. Por favor verificar.");                
                return;
            } 
            
            try {
                log.debug(user+" ->No es la respuesta de una comunicación");
                super.setElObjectIntoBinding("#{bindings.requiereRespuesta.inputValue}", Boolean.FALSE);  
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("error creando la comunicacion: " + e.getMessage(), e);
        }
        
        super.setOperation(evt);
    }
    
    public void validarDocumentoPrincipal (ValueChangeEvent valueChangeEvent) {
        
    }
    
    public void selectAnexo(SelectionEvent selEvent){
        log.info(user + " -> BEGIN selectAnexo");
        try {
            //Get selection source
            RichTable rtDest = (RichTable) selEvent.getSource();
            //Get selected row
            selectedAnexo = (Anexo) rtDest.getSelectedRowData();
            log.info(user + " -> selectedAnexo: "+selectedAnexo.getDescripcion());
            
        } catch (Exception e) {
            log.error(user+" -> Exception selectAnexo",e);
        }
        log.info(user + " -> FIN selectAnexo");
    }
    
    
    public void agregarAnexo(DialogEvent de){
        log.info(user + " -> BEGIN agregarAnexo");
        try {
            lstAnexo.add(nuevoAnexo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tanexo);
            nuevoAnexo = new Anexo();
        } catch (Exception e) {
            log.error(user+" -> Exception agregarAnexo",e);
        }
        log.info(user + " -> FIN agregarAnexo");
    }
    
    
    public void borrarRegistro(ActionEvent ae){
        log.info(user + " -> BEGIN borrarRegistro");
        try {
            lstAnexo.remove(selectedAnexo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tanexo);
        } catch (Exception e) {
            log.error(user+" -> Exception borrarRegistro",e);
        }
        log.info(user + " -> FIN borrarRegistro");
    }
    
    //Metodos para manejo del documento principal
    public void descargar(FacesContext fc, OutputStream os) {
        log.info("INICIO descargar");
        try {
            log.debug(user + " -> name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            log.debug(user + " -> mimeType2: " + getElObjectFromBinding("#{bindings.mimetype.inputValue}"));
            log.debug(user + " -> size: " + getElObjectFromBinding("#{bindings.size.inputValue}"));
            byte[] encodeFile = getElObjectFromBinding("#{bindings.content1.inputValue}").toString().getBytes();
            log.debug(user + " -> length-e: " + encodeFile.length);
            //Decodificar archivo recibido en el payload
            byte[] decodeFile = java.util.Base64.getDecoder().decode(encodeFile);
            log.debug(user + " -> length-d: " + decodeFile.length);
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
            log.error(user + " Exception descargar", e);
        }
       log.info("FIN descargar");
    }

    public void cambiarDocPpal(ValueChangeEvent vce){
            log.info("BEGIN cambiarDocPpal");
            try {
               //UploadedFile file = getFile();
               UploadedFile file = (UploadedFile) vce.getNewValue();
               log.debug(user + " -> file: " + file);

               log.debug(user + " -> fileName: " + file.getFilename());
               log.debug(user + " -> fileType: " + file.getContentType());
               log.debug(user + " -> opaqueData: " + file.getOpaqueData());
               log.debug(user + " -> inputStream: " + file.getInputStream());
               String fileExtn = getFileExtn(file.getFilename());
               log.debug(user + " -> fileExtn: " + fileExtn);

               if (!esValido(fileExtn)) {
                   mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                  "Solo se permiten archivos 'Odt' por favor actualice el archivo.");
                   limpiarDocPpal();
               } else {
                   //Revisar content Type del archivo
                   //10 Octubre de 2017 -> Se restringe a archivos ODT unicamente
                   //if(file.getContentType().contains("vnd.oasis.opendocument.text")||
                    if(file.getContentType().contains("vnd.oasis.opendocument.text")
                        ||file.getContentType().contains("vnd.openxmlformats-officedocument.wordprocessingml.document")){
                       //Maximo 10 Mb
                       if (file.getLength() > 10485760) {
                           mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                          "El archivo debe ser menor a 10Mb, por favor seleccione otro para continuar.");
                           limpiarDocPpal();
                       } else {
                           //Modificar el nombre del archivo Doc.Principal para agregar el numero de la instancia
                           String nombreDoc = file.getFilename();
                           String parts[] = nombreDoc.split("\\.(?=[^\\.]+$)");
                           String nombreModificado = parts[0]+"_"+getElObjectFromBinding("#{bindings.instanceId.inputValue}").toString()+"."+parts[1];
                           log.debug(user+" -> nombre modificado: "+nombreModificado);
                           setElObjectIntoBinding("#{bindings.name1.inputValue}", nombreModificado);
                           setElObjectIntoBinding("#{bindings.mimetype.inputValue}", file.getContentType());
                           setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                           byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                           //setElObjectIntoBinding("#{bindings.content1.inputValue}", file.getInputStream().);
                           setElObjectIntoBinding("#{bindings.content1.inputValue}", java.util.Base64.getEncoder().encodeToString(bytes));
                       }
                       log.debug(user + " -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
                   }else{
                       mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                      "La extensión del archivo es correcta pero el contenido invalido, por favor actualice el archivo.");
                       limpiarDocPpal();
                   }
               }
           } catch (Exception e) {
                log.error(user + " -> Exception cambiarDocPpal", e);
            }
            log.info("END cambiarDocPpal");
    }
    
    private String getFileExtn(String filename) {
        String parts[] = filename.split("\\.(?=[^\\.]+$)");
        return parts[1].toLowerCase();
    }

    
    
    private boolean esValido(String fileExtn) {
        //Pdf,Doc,Xls,Ppt
        //10 Octubre de 2017 -> Se restringe a archivos ODT unicamente
        if (fileExtn.equals("odt"))
            return true;
        else
            return false;
    }

    protected void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        try {
            FacesMessage message = new FacesMessage(severity, mensaje, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            log.error(user + " -> Exception mostrarMensaje", e);
        }
    }

    public void limpiarDocPpal(){
        log.info("BEGIN limpiarDocPpal");    
        try {
            ifAnexo.resetValue();
            AdfFacesContext.getCurrentInstance().addPartialTarget(ifAnexo);
            /*setElObjectIntoBinding("#{bindings.name1.inputValue}", null);
            setElObjectIntoBinding("#{bindings.mimetype.inputValue}", null);
            setElObjectIntoBinding("#{bindings.size.inputValue}", null);
            setElObjectIntoBinding("#{bindings.content.inputValue}",null);*/
        } catch (Exception e) {
            log.error(user + " -> Exception limpiarDocPpal", e);
        }
        log.info("END limpiarDocPpal");    
    }



    
    
    /*****************************************************************************************
     * ACCESSORS
     * ***************************************************************************************/
    
    public void setListaUsuarioDependencia(Collection<SelectItem> listaUsuarioDependencia) {
        this.usuariosDependencia = listaUsuarioDependencia;
    }

    public Collection<SelectItem> getListaUsuarioDependencia() {
        return usuariosDependencia;
    }    

    public void setCategoriasProceso(Collection<SelectItem> categoriasProceso) {
        this.categoriasProceso = categoriasProceso;
    }

    public Collection<SelectItem> getCategoriasProceso() {
        return categoriasProceso;
    }

    public void setCategoriasProcesoSet(Collection<Folders> categoriasProcesoSet) {
        this.categoriasProcesoSet = categoriasProcesoSet;
    }

    public Collection<Folders> getCategoriasProcesoSet() {
        return categoriasProcesoSet;
    }

    public void setProcesos(Collection<SelectItem> procesos) {
        this.procesos = procesos;
    }

    public Collection<SelectItem> getProcesos() {
        return procesos;
    }

    public void setProcesosSet(Collection<Folders> procesosSet) {
        this.procesosSet = procesosSet;
    }

    public Collection<Folders> getProcesosSet() {
        return procesosSet;
    }

    public void setSeries(Collection<SelectItem> series) {
        this.series = series;
    }

    public Collection<SelectItem> getSeries() {
        return series;
    }

    public void setSeriesSet(Collection<Folders> seriesSet) {
        this.seriesSet = seriesSet;
    }

    public Collection<Folders> getSeriesSet() {
        return seriesSet;
    }

    public void setSubseries(Collection<SelectItem> subseries) {
        this.subseries = subseries;
    }

    public Collection<SelectItem> getSubseries() {
        return subseries;
    }

    public void setSubseriesSet(Collection<Folders> subseriesSet) {
        this.subseriesSet = subseriesSet;
    }

    public Collection<Folders> getSubseriesSet() {
        return subseriesSet;
    }

    public void setCuadernos(Collection<SelectItem> cuadernos) {
        this.cuadernos = cuadernos;
    }

    public Collection<SelectItem> getCuadernos() {
        return cuadernos;
    }

    public void setCuadernosSet(Collection<Folders> cuadernosSet) {
        this.cuadernosSet = cuadernosSet;
    }

    public Collection<Folders> getCuadernosSet() {
        return cuadernosSet;
    }

    public void setTramites(Collection<SelectItem> tramites) {
        this.tramites = tramites;
    }

    public Collection<SelectItem> getTramites() {
        return tramites;
    }

    public void setTramitesSet(Collection<AnmTramiteTb> tramitesSet) {
        this.tramitesSet = tramitesSet;
    }

    public Collection<AnmTramiteTb> getTramitesSet() {
        return tramitesSet;
    }

    public void setTiposDocumentales(Collection<SelectItem> tiposDocumentales) {
        this.tiposDocumentales = tiposDocumentales;
    }

    public Collection<SelectItem> getTiposDocumentales() {
        return tiposDocumentales;
    }

    public void setTiposDocumentalesSet(Collection<AnmTipodtalseguntramite> tiposDocumentalesSet) {
        this.tiposDocumentalesSet = tiposDocumentalesSet;
    }

    public Collection<AnmTipodtalseguntramite> getTiposDocumentalesSet() {
        return tiposDocumentalesSet;
    }

    public void setListaDependencias(List<SelectItem> listaDependencias) {
        this.dependencias = listaDependencias;
    }

    public Collection<SelectItem> getListaDependencias() {
        return dependencias;
    }


    public void setDependenciasSet(Collection<AnmUnidadadministrativaTb> dependenciasSet) {
        this.dependenciasSet = dependenciasSet;
    }

    public Collection<AnmUnidadadministrativaTb> getDependenciasSet() {
        return dependenciasSet;
    }

    public void setUsuariosSet(Collection<SgdUsuario> usuariosSet) {
        this.usuariosSet = usuariosSet;
    }

    public Collection<SgdUsuario> getUsuariosSet() {
        return usuariosSet;
    }

    public void setLstPlantilla(List<AnmPlantilla> lstPlantilla) {
        this.lstPlantilla = lstPlantilla;
    }

    public List<AnmPlantilla> getLstPlantilla() {
        return lstPlantilla;
    }

    public void setTanexo(RichTable tanexo) {
        this.tanexo = tanexo;
    }

    public RichTable getTanexo() {
        return tanexo;
    }

    public void setLstAnexo(List<Anexo> lstAnexo) {
        this.lstAnexo = lstAnexo;
    }

    public List<Anexo> getLstAnexo() {
        return lstAnexo;
    }

    public void setNuevoAnexo(Anexo nuevoAnexo) {
        this.nuevoAnexo = nuevoAnexo;
    }

    public Anexo getNuevoAnexo() {
        return nuevoAnexo;
    }


    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
