package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal2;
import co.gov.anm.comunicaciones.bean.WCContentLocal2;
import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.Folders;
import co.gov.anm.comunicaciones.entity.SgdUsuario;
import co.gov.anm.comunicaciones.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.entity.Tramite;
import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;
import co.gov.anm.model.view.AsignacionComunicacionVOImpl;
import co.gov.anm.model.view.TrueFalseVOImpl;
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

import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.bpel.services.workflow.task.model.AttachmentTypeImpl;
import oracle.bpel.services.workflow.worklist.adf.DocMgmtBean;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

@ManagedBean(name="CrearComunicacionJSFBean")
@ViewScoped
public class CrearComunicacionJSFBean extends CommonJSFBean {

    private Logger log;
    private String user;
    
    @EJB
    private ComunicacionesSessionBeanLocal2 comunicacionEJB;
    
    @EJB
    private WCContentLocal2 wcContentEJB;
    
    private List<SelectItem> listaAsignacion;
    private List<SelectItem> listaRequiereResp;
    
    private Collection<SelectItem> dependencias = new ArrayList<>();
    private Collection<UnidadAdministrativa> dependenciasSet = new ArrayList<>();
    private Collection<SelectItem> usuariosDependencia = new ArrayList<>();
    private Collection<SgdUsuario> usuariosSet = new ArrayList<>();
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
    private Collection<Tramite> tramitesSet = new ArrayList<>();
    private Collection<SelectItem> tiposDocumentales = new ArrayList<>();
    private Collection<TipoDocumentalTramite> tiposDocumentalesSet = new ArrayList<>();
    private Collection<AnmPlantilla> plantillasSet = new ArrayList<>();
    private ExpedienteMinero proxy = new SGDWebServiceLocator().getExpedienteMineroProxy();

    
    private final static String PARENT_TAX_NAME = "ANM";
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
        } catch (Exception e) {
            log.error("Excepcion CrearComunicacionJSFBean",e);
        }
        log.info("END CrearComunicacionJSFBean");
    }
    
    
    
    @PostConstruct
    public void init () throws Exception {
        //cargar dependencias        
        dependenciasSet = wcContentEJB.getUnidadAdministrativaFindAll();
        
        for (UnidadAdministrativa unidAdmin : dependenciasSet) {
            this.dependencias.add(new SelectItem(unidAdmin.getCodUnidadadministrativa(), 
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
        for (Tramite f : tramitesSet) {
            System.out.println("Cargando Tramite: " + f.getIdtramite());
            this.tramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
        }
        
        //Pobla lista de tipos de asignacion
        listaAsignacion = new ArrayList<SelectItem>();
        
        AsignacionComunicacionVOImpl asignacion = (AsignacionComunicacionVOImpl) super.getViewObjectComunicaciones("AsignacionComunicacionVO1");
        asignacion.executeQuery();
        RowSetIterator iterA = asignacion.createRowSetIterator(null);
        Row rowA = null;
           
        while ( iterA.hasNext() ){                
            rowA = iterA.next();   
            listaAsignacion.add(new SelectItem(rowA.getAttribute("Cod"), (String)rowA.getAttribute("Valor")));
        } 
        
        //Pobla lista "requiere respuesta"
        listaRequiereResp = new ArrayList<SelectItem>();
        
        TrueFalseVOImpl requiereR = (TrueFalseVOImpl) super.getViewObjectComunicaciones("TrueFalseVO1");
        requiereR.executeQuery();
        RowSetIterator iterB = requiereR.createRowSetIterator(null);
        Row rowB = null;
           
        while ( iterB.hasNext() ){                
            rowB = iterB.next();    
            listaRequiereResp.add(new SelectItem(rowB.getAttribute("id"), (String)rowB.getAttribute("valor")));
        } 
        
        //inicializa check boxes
        super.setElObjectIntoBinding("#{bindings.responderComunicacion.inputValue}", Boolean.FALSE);   
        super.setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);   
        
        log.debug(user+" -> Obtiene nombre de la dependencia remitente con codigo: " + 
                (String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}"));
        
        List<UnidadAdministrativa> unidAdminList = wcContentEJB.getUnidadAdministrativaFindByCod(
            new BigDecimal((String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}")));
        
        super.setElObjectIntoBinding("#{bindings.nombreDepRemitente.inputValue}", unidAdminList.get(0).getNombreunidadadministrativa());
        
        log.debug(user+" -> carga plantillas");
        plantillasSet = comunicacionEJB.getAnmPlantillaFindAll();   
    }
        
    public void cambiarAsignacion (ValueChangeEvent evt) {
        log.debug(user+" -> Inicio cambiarAsignacion():"+evt.getOldValue());
        
        String selectedValue = (String)evt.getNewValue();
        
        try {
           if (selectedValue != null){
               
               AsignacionComunicacionVOImpl vo = (AsignacionComunicacionVOImpl) super.getViewObjectComunicaciones("AsignacionComunicacionVO1");
               vo.executeQuery();
               RowSetIterator iter = vo.createRowSetIterator(null);
               Row row = null;
               
               while ( iter.hasNext() ){                
                   row = iter.next();                
                   
                   log.debug(row.getAttribute("Cod"));
                   log.debug(row.getAttribute("Valor"));
                   if ( selectedValue.equals(row.getAttribute("Cod")) ){  
                       super.setElObjectIntoBinding("#{bindings.idAsignacion.inputValue}", row.getAttribute("Cod"));
                       super.setElObjectIntoBinding("#{bindings.nombreAsignacion.inputValue}", row.getAttribute("Valor"));
                       
                       if (selectedValue.equals("2") || selectedValue.equals("3")) {
                           super.setElObjectIntoBinding("#{bindings.responderComunicacion.inputValue}", Boolean.FALSE); 
                           super.setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 0);  
                           
                           super.setElObjectIntoBinding("#{bindings.idUsuarioCom.inputValue}", null);
                           super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}", null);
                           
                           if (selectedValue.equals("3")) {
                               super.setElObjectIntoBinding("#{bindings.codDependenciaDestino.inputValue}", null);
                               super.setElObjectIntoBinding("#{bindings.nombreDependenciaDestino.inputValue}", null);                               
                           }                        
                       } 
                   }
               }
           }               
        } catch (Exception e) {
            log.error("error cambiando asignacion: " + e.getMessage(), e);
        }
        log.debug(user+" -> Fin cambiarAsignacion():"+evt.getNewValue());
    }
    
    
    
    
    public void cambiarRequiereRespuesta (ValueChangeEvent evt) {
        log.debug(user+" -> Inicio cambiarRequiereRespuesta():"+evt.getOldValue());
        
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
            log.error("error cambiando requiereRespuesta: " + e.getMessage(), e);
        }
        log.debug(user+" -> Fin cambiarRequiereRespuesta():"+evt.getNewValue());
    }
    
    
    
    
    public void cambiarDependenciaDestino ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio cambiarDependenciaDestino():"+evt.getOldValue());
        
        BigDecimal newValue = (BigDecimal)evt.getNewValue();
        
        try{
            if (newValue != null){                            
                for (UnidadAdministrativa depDestino : dependenciasSet) {
                    if (newValue.equals(depDestino.getCodUnidadadministrativa())) {
                        super.setElObjectIntoBinding("#{bindings.codDependenciaDestino.inputValue}", depDestino.getCodUnidadadministrativa());
                        super.setElObjectIntoBinding("#{bindings.nombreDependenciaDestino.inputValue}", depDestino.getNombreunidadadministrativa());
                        super.setElObjectIntoBinding("#{bindings.idDependenciaDestino.inputValue}", depDestino.getIdUnidadadministrativa());
                        
                        if (((String)super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}")).equals("1")) {
                            Long codDependencia = Long.parseLong(depDestino.getCodUnidadadministrativa().toString());
                            
                            log.debug(user+" -> Filtra usuarios por codigo dependencia: " + codDependencia);                            
                            
                            usuariosSet = this.comunicacionEJB.getSgdUsuarioFindByDependencia(codDependencia);
                            
                            this.usuariosDependencia = new ArrayList<SelectItem>();
                            
                            for (SgdUsuario usuario : usuariosSet) {
                                this.usuariosDependencia.add(new SelectItem(usuario.getIdUsuario(), usuario.getNombreUsuario()));
                            }
                            
                            log.debug(user+" -> Usuarios retornados: " + usuariosDependencia.size());   
                        }                        
                        break;
                    }
                }
            }    
        } catch(Exception e){
            log.error("error cambiando dependencia destino: " + e.getMessage(), e);
        }        
        log.debug(user+" -> Fin cambiarDependenciaDestino():"+evt.getNewValue());
    }
    
    public void cambiarUsuarioDestino (ValueChangeEvent evt) {
        log.debug(user+" -> cambiarUsuarioDestino():"+evt.getNewValue());
        String selectedValue = (String)evt.getNewValue();
        
        try {
            if (selectedValue != null) {
                
                for (SgdUsuario usuario : usuariosSet) {
                    if (selectedValue.equals(usuario.getIdUsuario())) {
                        super.setElObjectIntoBinding("#{bindings.idUsuarioCom.inputValue}", usuario.getIdUsuario());
                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}", usuario.getNombreUsuario());
                        
                        log.debug(user+" -> id usuario seleccionado: " + usuario.getIdUsuario());
                        log.debug(user+" -> nombre usuario seleccionado: " + usuario.getNombreUsuario());
                        
                        break;
                    }
                }
                
                log.debug(user+" -> id asignado: " + super.getElObjectFromBinding("#{bindings.idUsuarioCom.inputValue}"));
                log.debug(user+" -> usuario asignado: " + super.getElObjectFromBinding("#{bindings.nombreUsuarioCom.inputValue}"));
            }
        } catch (Exception e) {
            log.error("error cambiando usuario: " + e.getMessage(), e);
        }
    }
    
    public void cambiarEsExpMinero (ValueChangeEvent evt) {
        log.debug(user+" -> cambiarEsExpMinero():"+evt.getNewValue());
        Boolean selectedValue = (Boolean)evt.getNewValue();
        
        try {
            if (selectedValue != null) {                
                if (selectedValue.equals(Boolean.FALSE)) {
                    System.out.println("exp minero false");
                    
                    super.setElObjectIntoBinding("#{bindings.nroPlaca.inputValue}", "");   
                    
                    super.setElObjectIntoBinding("#{bindings.idTramite.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
                } else {
                    System.out.println("exp minero true");
                    
                    super.setElObjectIntoBinding("#{bindings.idCatProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.catProceso1.inputValue}", null);
                                                                    
                    super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.proceso1.inputValue}", null);
                    
                    super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);    

                    super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreSubserie.inputValue}", null);
                }
            }
        } catch (Exception e) {
            log.error("error cambiandoExpMinero: " + e.getMessage(), e);
        }
    }
    
    public void cambiarCategoriaProceso ( ValueChangeEvent evt ){
        log.debug(user+" -> inicio cambiarCategoriaProceso(): "+evt.getNewValue());
        
        String catProcesoSel = (String)evt.getNewValue();
        this.procesosSet = this.wcContentEJB.getFoldersFindByParentGuid(catProcesoSel);
        this.procesos = new ArrayList<SelectItem>(this.procesosSet.size());
        
        
        for ( Folders f:procesosSet ){
            this.procesos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }
        
        //setea los demas datos de la taxonomia        
        for ( Folders f:this.categoriasProcesoSet ){
            if ( f.getFfolderguid().equals(catProcesoSel) ){
                super.setElObjectIntoBinding("#{bindings.catProceso1.inputValue}", f.getFfoldername());        
                break;
            }
        }
        
        this.cambiarProceso(null);
        log.debug(user+" -> Fin: cambiarCategoriaProceso()");        
    }
    
    public void cambiarProceso ( ValueChangeEvent evt ){
        log.debug(user+" -> inicio cambiarProceso(): "+(evt!=null&&evt.getNewValue()!=null?evt.getNewValue():"vacio"));
        
        if ( evt==null || evt.getNewValue()==null ){
            this.series = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.proceso1.inputValue}", null);    
        } else {
            String idProceso = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", idProceso);
            
            for ( Folders f:this.procesosSet ){
                if ( f.getFfolderguid().equals(idProceso) ){
                    super.setElObjectIntoBinding("#{bindings.proceso1.inputValue}", f.getFfoldername());        
                    break;
                }
            }
                                    
            this.seriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idProceso);
            this.series = new ArrayList<SelectItem>(this.seriesSet.size());            
            
            for ( Folders f:seriesSet ){
                this.series.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }            
        }        
        this.cambiarSerie(null);
    }

    public void cambiarSerie ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio cambiarSerie()");
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
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            this.subseriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idSerie);
            this.subseries = new ArrayList<SelectItem>(this.subseriesSet.size());
            for ( Folders f:subseriesSet ){
                this.subseries.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarSubSerie(null);
    }    
    
    public void cambiarSubSerie ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio: cambiarSubSerie()");
        
        if ( evt==null || evt.getNewValue()==null  ){
            this.cuadernos = new ArrayList<SelectItem>(0);
            this.cuadernosSet = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.nombreSubserie.inputValue}", null);    
        } else {
            String idSubSerie = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", idSubSerie);
            
            for ( Folders f:this.subseriesSet ){
                if ( f.getFfolderguid().equals(idSubSerie) ){
                    super.setElObjectIntoBinding("#{bindings.nombreSubserie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            this.cuadernosSet = this.wcContentEJB.getFoldersFindByParentGuid(idSubSerie);
            this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());            
            
            for ( Folders f:cuadernosSet ){
                this.cuadernos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarCuaderno(null);
    }
    
    public void cambiarCuaderno ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio: cambiarCuaderno()");
        
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
        log.debug(user+" -> Inicio: cambiarTramite(): "+evt.getNewValue());
        
        if (evt==null || evt.getNewValue()==null){
            super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);
            this.tiposDocumentalesSet = new ArrayList<TipoDocumentalTramite>();
            this.tiposDocumentales = new ArrayList<SelectItem>();
            return;
        }
        
        String tramiteSelected = (String)evt.getNewValue();
        for ( Tramite t:this.tramitesSet ){
            if ( tramiteSelected.equals(t.getIdtramite().toString()) ){
                super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                break;
            }
        }
        
        this.tiposDocumentalesSet = this.wcContentEJB.getTipoDocumentalTramiteTiposDocumentalesTramites(new BigDecimal(tramiteSelected));
        
        log.debug(user+" -> tipos documentales encontrados para el tramite: " + tiposDocumentalesSet.size());
        
        this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
        for ( TipoDocumentalTramite f : this.tiposDocumentalesSet ){
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
        }
    }
    
    public void cambiarTipoDocumental ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio: cambiarTipoDocumento():"+evt.getNewValue());
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);    
        } else {
            String tipoDocSele = (String)evt.getNewValue();
            
            for ( TipoDocumentalTramite t:this.tiposDocumentalesSet ){
                if ( tipoDocSele.equals(t.getIdtipodtal().toString()) ){
                    log.debug(user+" -> encuentra tipo documental");                                        
                    
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", t.getIdtipodtal());
                    
                    log.debug(user+" -> asigna tipo documental");
                    break;
                }
            }
        }
    }
    
    public void validarExpMinero (ValueChangeEvent event) {
        log.debug(user+" -> validarExpMinero():"+event.getNewValue());
        WccResponse response = null;
        
        String expMinero = (String) event.getNewValue();
        
        try {	
            response = proxy.validarExpedienteMinero(expMinero);
            
            if (response.getStatusCode().equals("ERWCC03")) {
                log.debug(user+" -> Error de validacion");  
                
                event.getComponent().processUpdates(FacesContext.getCurrentInstance());
                super.setElObjectIntoBinding("#{bindings.nroPlaca.inputValue}", "");
                super.showMessage(FacesMessage.SEVERITY_ERROR, "El expediente minero no existe. Por favor validar.");
            } else {
                log.debug(user+" -> Expediente minero validado");
            }
        } catch(Exception e) {
            log.error("error validando exp minero: " + e.getMessage(), e);
        }
    }    
    
    public void crearComunicacion ( ActionEvent evt ){        
        log.info("INICIO crearComunicacion");
        log.debug(user+" -> responder com: " + (Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}"));
        log.debug(user+" -> tiempo resp: " + (Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}"));
        
        try {
            /*
             * Actualizado por: Adrian Molina (adrimol@gmail.com) el 29Marzo2017
             * Motivo: Soluci�n a inconveniente de atributo requerido en los componentes relacionados
             */
            //Validar destino del envio para verificar campos "dependencia destino" y "usuario destino"
            log.debug(user+" -> idAsignacion: " + (String)super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}"));
            log.debug(user+" -> codDependenciaDestino: " + (String)super.getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}"));
            log.debug(user+" -> idUsuarioCom: " + (String)super.getElObjectFromBinding("#{bindings.idUsuarioCom.inputValue}"));
            if(getElObjectFromBinding("#{bindings.idAsignacion.inputValue}").equals("1")){
                if(getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}")==null){
                    super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la dependencia destino.");
                    return;
                }
                if(getElObjectFromBinding("#{bindings.idUsuarioCom.inputValue}")==null){
                    super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el usuario destino.");
                    return;
                }
            }
            if(getElObjectFromBinding("#{bindings.idAsignacion.inputValue}").equals("2")){
                if(getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}")==null){
                    super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la dependencia destino.");
                    return;
                }
            }
            log.debug(user+" -> esTituloMinero?: " + (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}"));
            log.debug(user+" -> idTramite: " + (String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}"));
            log.debug(user+" -> idTpDocumento: " + (String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}"));
            log.debug(user+" -> idCatProceso: " + (String)super.getElObjectFromBinding("#{bindings.idCatProceso.inputValue}"));
            log.debug(user+" -> idProceso: " + (String)super.getElObjectFromBinding("#{bindings.idProceso.inputValue}"));
            log.debug(user+" -> idSerie: " + (String)super.getElObjectFromBinding("#{bindings.idSerie.inputValue}"));
            if ((Boolean) super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) {
                if ((((String) super.getElObjectFromBinding("#{bindings.idTramite.inputValue}")) == null ||
                     ((String) super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}")) == null)) {
                    log.debug(user+" -> Error en validacion en tr�mite");
                    super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tr�mite y el tipo documental.");
                    return;
                }
            } else {
                if (((String) super.getElObjectFromBinding("#{bindings.idCatProceso.inputValue}")) == null ||
                     ((String) super.getElObjectFromBinding("#{bindings.idProceso.inputValue}")) == null ||
                     ((String) super.getElObjectFromBinding("#{bindings.idSerie.inputValue}")) == null ){
                    log.debug(user+" -> Error en validacion de taxonomia");
                    super.showMessage(FacesMessage.SEVERITY_WARN, "Debe diligenciar los campos requeridos en 'Gesti�n documental'.");
                    return;
                }
            }

            /*
             * Actualizado por: Adrian Molina el 29Marzo2017
             * Motivo: Se deshabilita el siguiente bloque de codigo para dar cumplimiento a las condiciones
             *  del negocio
             */
            /*
            if (!((Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) &&
                ((String)super.getElObjectFromBinding("#{bindings.idSubSerie.inputValue}")) == null) {
                System.out.println("Error en validaci�n en taxonom�a");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonom�a completa. Por favor verificar");
                
                return;
            }
            */
            
            if (((Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) && 
                    (super.getElObjectFromBinding("#{bindings.nroPlaca.inputValue}") == null || 
                    ((String)super.getElObjectFromBinding("#{bindings.nroPlaca.inputValue}")).trim().equals(""))) {
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe escribir un n�mero de placa para el expediente minero");
                
                return;
            }
            
            if (((Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}")) && 
                ((Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}")) <= 0) {
                System.out.println("error de validacion en responder comunicaci�n");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "El tiempo de respuesta debe ser mayor a cero (0). Por favor verificar.");
                
                return;
            }
            
            if (((String)super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}")).equals("3")) {
                log.debug(user+" -> quita valores de dependencia y usuario");
                
                super.setElObjectIntoBinding("#{bindings.dependenciaDestinoCom.inputValue}", null);
                super.setElObjectIntoBinding("#{bindings.idUsuarioCom.inputValue}", null);
            } else if (((String)super.getElObjectFromBinding("#{bindings.idAsignacion.inputValue}")).equals("2")) {
                log.debug(user+" -> quita valores de dependencia");
                
                super.setElObjectIntoBinding("#{bindings.idUsuarioCom.inputValue}", null);
            }
            /*
             * Bloque comentariado para evitar validacion obsoleta (ANM-801)
            DocMgmtBean docMngr = (DocMgmtBean)super.getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            log.debug(user+" -> Cantidad de attachements:"+docMngr.getAddedAttachmentList().size());
            int cantidadAttachments = docMngr.getAddedAttachmentList().size();
                        
            if (cantidadAttachments == 0) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado alg�n documento. Por favor verificar.");
                
                return;
            } else {                
                for (AttachmentTypeImpl attachment : docMngr.getAddedAttachmentList()) {
                    log.debug(attachment.getMimeType());
                }
                
                if (!docMngr.getAddedAttachmentList().get(0).getMimeType().equals("application/vnd.oasis.opendocument.text")) {
                    super.showMessage(FacesMessage.SEVERITY_WARN, "La extensi�n del documento debe ser .odt. Por favor verificar.");
                    
                    return; 
                }
            }
            */
            
            try {
                System.out.println("No es la respuesta de una comunicaci�n");
                super.setElObjectIntoBinding("#{bindings.requiereRespuesta.inputValue}", Boolean.FALSE);  
            } catch (Exception e) {
                log.error("error seteando requiereRespuesta: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("error creando comunicacion: " + e.getMessage(), e);
        }
        
        super.setOperation(evt);
        log.info("FIN crearComunicacion");
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
                   if(file.getContentType().contains("vnd.oasis.opendocument.text")){
                   // if(file.getContentType().contains("vnd.openxmlformats-officedocument.wordprocessingml.document")){
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
                           log.debug(user+" -> name1: "+getElObjectFromBinding("#{bindings.name1.inputValue}"));
                           //Setear demas variables del adjunto
                           setElObjectIntoBinding("#{bindings.mimeType1.inputValue}", file.getContentType());
                           setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                           byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                           setElObjectIntoBinding("#{bindings.content1.inputValue}", java.util.Base64.getEncoder().encodeToString(bytes));
                       }
                       log.debug(user + " -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
                   }else{
                       mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                      "La extensi�n del archivo es correcta pero el contenido invalido, por favor actualice el archivo.");
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
               setElObjectIntoBinding("#{bindings.mimeType1.inputValue}", null);
               setElObjectIntoBinding("#{bindings.size.inputValue}", null);
               setElObjectIntoBinding("#{bindings.content1.inputValue}",null);*/
           } catch (Exception e) {
                log.error(user + " -> Exception limpiarDocPpal", e);
            }
            log.info("END limpiarDocPpal");    
        }

    public void descargar(FacesContext fc, OutputStream os) {
            log.info("INICIO descargar");
            try {
                log.debug(user + " -> name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
                log.debug(user + " -> mimeType2: " + getElObjectFromBinding("#{bindings.mimeType2.inputValue}"));
                log.debug(user + " -> size: " + getElObjectFromBinding("#{bindings.size.inputValue}"));
                //log.debug(user + " -> content1: " + getElObjectFromBinding("#{bindings.content1.inputValue}"));
                byte[] encodeFile = getElObjectFromBinding("#{bindings.content1.inputValue}").toString().getBytes();
                log.debug(user + " -> length-e: " + encodeFile.length);
                //Decodificar archivo recibido en el payload
                byte[] decodeFile = java.util.Base64.getDecoder().decode(encodeFile);
                log.debug(user + " -> length-d: " + decodeFile.length);
                //Crear response para el navegador
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + getElObjectFromBinding("#{bindings.name1.inputValue}"));
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

    
    
    
    
    
    
    
    public void setListaAsignacion(List<SelectItem> listaAsignacion) {
        this.listaAsignacion = listaAsignacion;
    }

    public List<SelectItem> getListaAsignacion() {
        return listaAsignacion;
    }    
    
    public void setListaRequiereResp(List<SelectItem> listaRequiereResp) {
        this.listaRequiereResp = listaRequiereResp;
    }

    public List<SelectItem> getListaRequiereResp() {
        return listaRequiereResp;
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

    public void setTramitesSet(Collection<Tramite> tramitesSet) {
        this.tramitesSet = tramitesSet;
    }

    public Collection<Tramite> getTramitesSet() {
        return tramitesSet;
    }

    public void setTiposDocumentales(Collection<SelectItem> tiposDocumentales) {
        this.tiposDocumentales = tiposDocumentales;
    }

    public Collection<SelectItem> getTiposDocumentales() {
        return tiposDocumentales;
    }

    public void setTiposDocumentalesSet(Collection<TipoDocumentalTramite> tiposDocumentalesSet) {
        this.tiposDocumentalesSet = tiposDocumentalesSet;
    }

    public Collection<TipoDocumentalTramite> getTiposDocumentalesSet() {
        return tiposDocumentalesSet;
    }


    public void setDependencias(Collection<SelectItem> dependencias) {
        this.dependencias = dependencias;
    }

    public Collection<SelectItem> getDependencias() {
        return dependencias;
    }

    public void setDependenciasSet(Collection<UnidadAdministrativa> dependenciasSet) {
        this.dependenciasSet = dependenciasSet;
    }

    public Collection<UnidadAdministrativa> getDependenciasSet() {
        return dependenciasSet;
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

    public void setPlantillasSet(Collection<AnmPlantilla> plantillasSet) {
        this.plantillasSet = plantillasSet;
    }

    public Collection<AnmPlantilla> getPlantillasSet() {
        return plantillasSet;
    }

    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
