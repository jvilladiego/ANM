package co.gov.anm.comunicaciones.control;


import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal;
import co.gov.anm.comunicaciones.bean.WCContentLocal;
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
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;


@ManagedBean(name="ActualizarComunicacionJSFBean")
@ViewScoped
public class ActualizarComunicacionJSFBean extends CommonJSFBean{
    
    @EJB
    private ComunicacionesSessionBeanLocal comunicacionEJB;
    
    @EJB
    private WCContentLocal wcContentEJB;
    
    private ExpedienteMinero port = null;    
    private String user;
    
    /*
     * Listas de Metadatos
     */
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
    
    private Logger log;
    
    private RichInputFile ifAnexo;
    
    public ActualizarComunicacionJSFBean() {
        try {
            
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN ActualizarComunicacionJSFBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error(user + " -> Exception ActualizarComunicacionJSFBean", e);
        }
        log.info("END ActualizarComunicacionJSFBean");
    }
    
    
    
    private final static String PARENT_TAX_NAME = "ANM";    
    
    @PostConstruct
    public void init () throws Exception {
        log = Logger.getLogger(this.getClass().getSimpleName());
        
        //cargar dependencias        
        dependenciasSet = wcContentEJB.getUnidadAdministrativaFindAll();
        
        for (AnmUnidadadministrativaTb unidAdmin : dependenciasSet) {
            this.dependencias.add(new SelectItem(unidAdmin.getCodUnidadadministrativa().toString(), 
                        unidAdmin.getCodUnidadadministrativa() + " - " + unidAdmin.getNombreunidadadministrativa()));
        }
        //cargar categorias de la taxonomia        
        Collection<Folders> taxonomiaParent = this.wcContentEJB.getFoldersFindByFolderName(PARENT_TAX_NAME);
        String parentGuid = null;
        if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
            throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
        }

        for (Folders f : taxonomiaParent) {
            parentGuid = f.getFfolderguid();
            break;
        }
                
        //Limpiar taxonomia para solucionar issue 446
        log.debug("*** idCatProceso: "+getElObjectFromBinding("#{bindings.idCatProceso.inputValue}"));                
        log.debug("*** idProceso: "+getElObjectFromBinding("#{bindings.idProceso.inputValue}"));
        log.debug("*** idSerie: "+getElObjectFromBinding("#{bindings.idSerie.inputValue}"));
        log.debug("*** idSubSerie: "+getElObjectFromBinding("#{bindings.idSubSerie.inputValue}"));
        log.debug("*** idCuaderno: "+getElObjectFromBinding("#{bindings.idCuaderno.inputValue}"));
        log.debug("*** idTramite: "+getElObjectFromBinding("#{bindings.idTramite.inputValue}"));                
        log.debug("*** idTpDocumento: "+getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}"));      
        
        if(getElObjectFromBinding("#{bindings.idCatProceso.inputValue}")!=null){
            precargarTaxonomia((String)getElObjectFromBinding("#{bindings.idCatProceso.inputValue}"),parentGuid);
        }else{
            //Precargar listado tramites y tipos documentales    
            this.tramitesSet = this.wcContentEJB.getTramiteFindAll();
            log.debug("tramitesSet size: " + tramitesSet.size());
            this.tramites = new ArrayList<SelectItem>(this.tramitesSet.size());
            for (AnmTramiteTb f : tramitesSet) {
                //log.debug("Cargando Tramite: " + f.getIdtramite().intValue());
                this.tramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
            }
            log.debug("tramites size: " + tramites.size());
            this.tiposDocumentalesSet = this.wcContentEJB.getTipoDocumentalTramiteTiposDocumentalesTramites(
                            new BigDecimal( getElObjectFromBinding("#{bindings.idTramite.inputValue}").toString() ));
            log.debug("tiposDocumentalesSet size: " + tiposDocumentalesSet.size());
            this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
            for ( AnmTipodtalseguntramite f : this.tiposDocumentalesSet ){
                this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
            }
            log.debug("tiposDocumentales size: " + tiposDocumentales.size());
        }
        
        
        log.debug("*** codDepDestino: "+getElObjectFromBinding("#{bindings.codDepDestino.inputValue}"));
        log.debug("*** idUsuario: "+getElObjectFromBinding("#{bindings.idUsuario.inputValue}"));
                
        if ( getElObjectFromBinding("#{bindings.codDepDestino.inputValue}")!=null ){
            Long codDependencia = Long.valueOf((String)getElObjectFromBinding("#{bindings.codDepDestino.inputValue}"));
            this.cargarUsuarios(codDependencia);    
        }
        
        log.debug("Obtiene nombre de la dependencia remitente con codigo: " + 
                (String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}"));
        
        List<AnmUnidadadministrativaTb> unidAdminList = wcContentEJB.getUnidadAdministrativaFindByCod(
            new BigDecimal((String)super.getElObjectFromBinding("#{bindings.codigoDepRemitente.inputValue}")));
        
        super.setElObjectIntoBinding("#{bindings.nombreDepRemitente.inputValue}", unidAdminList.get(0).getNombreunidadadministrativa());
        
        try {
            log.debug("url wsdl: " + (String)super.getElObjectFromBinding("#{bindings.urlExpMineroWSDL.inputValue}"));
            //port = ServiceLocator.getInstance().getExpMineroPort((String)super.getElObjectFromBinding("#{bindings.urlExpMineroWSDL.inputValue}"));
        } catch (Exception e) {
            log.error("Exception init",e);
        }
    }    
    
    
    
    
    
    private void cargarUsuarios ( Long codDependencia ){
        log.debug("Filtra usuarios por codigo dependencia: " + codDependencia);
        log.debug("session bean " + this.comunicacionEJB);
        
        usuariosSet = this.comunicacionEJB.getSgdUsuarioFindByDependencia(codDependencia);
        
        usuariosDependencia = new ArrayList<SelectItem>();
        
        for (SgdUsuario usuario : usuariosSet) {
            this.usuariosDependencia.add(new SelectItem(usuario.getIdUsuario(), usuario.getNombreUsuario()));
        }
        
        log.debug("Usuarios retornados: " + this.usuariosDependencia.size());   
    }
    

    
    public void cambiarDependenciaDestino ( ValueChangeEvent evt ){
        log.debug("Inicio cambiarDependenciaDestino():"+evt.getOldValue());
        
        String newValue = (String)evt.getNewValue();
        
        try{
            if (newValue != null){                            
                for (AnmUnidadadministrativaTb depDestino : dependenciasSet) {
                    if (newValue.equals(depDestino.getCodUnidadadministrativa().toString())) {
                        super.setElObjectIntoBinding("#{bindings.codDepDestino.inputValue}", depDestino.getCodUnidadadministrativa().toString());
                        super.setElObjectIntoBinding("#{bindings.nombreDepDestino.inputValue}", depDestino.getNombreunidadadministrativa());
                        super.setElObjectIntoBinding("#{bindings.idDepDestino.inputValue}", depDestino.getIdUnidadadministrativa());
                        
                        Long codDependencia = Long.parseLong(depDestino.getCodUnidadadministrativa().toString());
                        
                        this.cargarUsuarios(codDependencia);
                        
                        break;
                    }
                }
            }    
        } catch(Exception e){
            e.printStackTrace();
        }        
        log.debug("Fin cambiarDependenciaDestino():"+evt.getNewValue());
    }
    
    public void cambiarUsuarioDestino (ValueChangeEvent evt) {
        log.debug("cambiarUsuarioDestino():"+evt.getNewValue());
        String selectedValue = (String)evt.getNewValue();
        
        try {
            if (selectedValue != null) {
                
                for (SgdUsuario usuario : usuariosSet) {
                    if (selectedValue.equals(usuario.getIdUsuario())) {
                        super.setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuario.getIdUsuario());
                        super.setElObjectIntoBinding("#{bindings.nombreUsuarioCom.inputValue}", usuario.getNombreUsuario());
                        
                        log.debug("id usuario seleccionado: " + usuario.getIdUsuario());
                        log.debug("nombre usuario seleccionado: " + usuario.getNombreUsuario());
                        
                        break;
                    }
                }
                
                log.debug("id asignado: " + super.getElObjectFromBinding("#{bindings.idUsuario.inputValue}"));
                log.debug("usuario asignado: " + super.getElObjectFromBinding("#{bindings.nombreUsuarioCom.inputValue}"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cambiarRequiereRespuesta (ValueChangeEvent evt) {
        log.debug("Inicio cambiarRequiereRespuesta():"+evt.getOldValue());
        
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
            e.printStackTrace();
        }
        log.debug("Fin cambiarRequiereRespuesta():"+evt.getNewValue());
    }    
    
    public void cambiarEsExpMinero (ValueChangeEvent evt) {
        log.debug("cambiarEsExpMinero():"+evt.getNewValue());
        Boolean selectedValue = (Boolean)evt.getNewValue();
        
        try {
            if (selectedValue != null) {                
                if (selectedValue.equals(Boolean.FALSE)) {
                    log.debug("exp minero false");
                    
                    super.setElObjectIntoBinding("#{bindings.nroPlaca.inputValue}", "");   
                    
                    super.setElObjectIntoBinding("#{bindings.idTramite.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
                } else {
                    log.debug("exp minero true");
                    
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
            e.printStackTrace();
        }
    }    
    
    
    
    private void precargarTaxonomia ( String idCategoriaSel,String parentGuid ){  
        if ( idCategoriaSel==null ){
            return;
        }
        log.debug("precargarTaxonomia()->parentGuid: "+parentGuid);
        this.categoriasProcesoSet = this.wcContentEJB.getFoldersFindByParentGuid(parentGuid);
        this.categoriasProceso = new ArrayList<SelectItem>(categoriasProcesoSet.size());
        for (Folders f : categoriasProcesoSet) {
            this.categoriasProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }
        
        log.debug("precargarTaxonomia()->idCategoriaSel: "+idCategoriaSel);
        if ( idCategoriaSel==null ){
            return;
        }
        this.procesosSet = this.wcContentEJB.getFoldersFindByParentGuid(idCategoriaSel);
        this.procesos = new ArrayList<SelectItem>(this.procesosSet.size());
        
        
        for ( Folders f:procesosSet ){
            this.procesos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }
        
        String idProcesoSel = (String)getElObjectFromBinding("#{bindings.idProceso.inputValue}");
        if ( idProcesoSel==null ){
            return;
        }
        log.debug("precargarTaxonomia()->idProcesoSel: "+idProcesoSel);
        this.seriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idProcesoSel);
        this.series = new ArrayList<SelectItem>(this.seriesSet.size());            
        
        for ( Folders f:seriesSet ){
            this.series.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }  
        
        String idSerieSel = (String)getElObjectFromBinding("#{bindings.idSerie.inputValue}");
        if ( idSerieSel==null ){
            return;
        }
        log.debug("precargarTaxonomia()->idSerieSel: "+idSerieSel);
        
        this.subseriesSet = this.wcContentEJB.getFoldersFindByParentGuid(idSerieSel);
        this.subseries = new ArrayList<SelectItem>(this.subseriesSet.size());
        for ( Folders f:subseriesSet ){
            this.subseries.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }
        
        String idSubSerieSel = (String)getElObjectFromBinding("#{bindings.idSubSerie.inputValue}");
        if ( idSubSerieSel==null ){
            return;
        }
        log.debug("precargarTaxonomia()->idSubSerieSel: "+idSubSerieSel);
        this.cuadernosSet = this.wcContentEJB.getFoldersFindByParentGuid(idSubSerieSel);
        this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());            
        
        for ( Folders f:cuadernosSet ){
            this.cuadernos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }
        
    }
    
    
    public void cambiarCategoriaProceso ( ValueChangeEvent evt ){
        log.debug("inicio cambiarCategoriaProceso(): "+evt.getNewValue());
        
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
        log.debug("Fin: cambiarCategoriaProceso()");        
    }
    
    public void cambiarProceso ( ValueChangeEvent evt ){
        log.debug("inicio cambiarProceso(): "+(evt!=null&&evt.getNewValue()!=null?evt.getNewValue():"vacio"));
        
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
            this.series = new ArrayList<SelectItem>(this.seriesSet.size());            
            
            for ( Folders f:seriesSet ){
                this.series.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }            
        }        
        this.cambiarSerie(null);
    }

    public void cambiarSerie ( ValueChangeEvent evt ){
        log.debug("Inicio cambiarSerie()");
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
        log.debug("Inicio: cambiarSubSerie()");
        
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
                    super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            this.cuadernosSet = this.wcContentEJB.getFoldersFindByParentGuid(idSubSerie);
            this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());            
            
            for ( Folders f:cuadernosSet ){
                this.cuadernos.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarExpediente(null);
    }
    
    public void cambiarExpediente ( ValueChangeEvent evt ){
        log.debug("Inicio: cambiarExpediente()");
        
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
        log.debug("Inicio: cambiarTramite(): "+evt.getNewValue());
        
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
        
        log.debug("tipos documentales encontrados para el tramite: " + tiposDocumentalesSet.size());
        
        this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
        for ( AnmTipodtalseguntramite f : this.tiposDocumentalesSet ){
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
        }
    }
    
    public void cambiarTipoDocumental ( ValueChangeEvent evt ){
        log.debug("Inicio: cambiarTipoDocumento():"+evt.getNewValue());
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);    
        } else {
            String tipoDocSele = (String)evt.getNewValue();
            
            for ( AnmTipodtalseguntramite t:this.tiposDocumentalesSet ){
                if ( tipoDocSele.equals(t.getIdtipodtal().toString()) ){
                    log.debug("encuentra tipo documental");                                        
                    
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", t.getIdtipodtal());
                    
                    log.debug("asigna tipo documental");
                    break;
                }
            }
        }
    }
    
    public void validarExpMinero (ValueChangeEvent event) {
        log.debug("validarExpMinero():"+event.getNewValue());
        WccResponse response = null;
        
        String expMinero = (String) event.getNewValue();
        
        try {   
            ExpedienteMinero proxy = new SGDWebServiceLocator().getExpedienteMineroProxy();
            response = proxy.validarExpedienteMinero(expMinero);
            
            if (response.getStatusCode().equals("ERWCC03")) {
                log.debug("Error de validacion");
                super.showMessage(FacesMessage.SEVERITY_WARN, "El expediente minero no existe. Por favor validar.");
            } else {
                log.debug("Expediente minero validado");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }        
    
    public void actualizarComunicacion ( ActionEvent evt ){
        log.debug("responder com: " + (Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}"));
        log.debug("tiempo resp: " + (Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}"));
        log.debug("nro folios: " + (Integer)super.getElObjectFromBinding("#{bindings.nroFolios.inputValue}"));
        
        Boolean esTituloMinero = (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}");
        String idTramite = (String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}");
        String idTpDocumento = (String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}");
        String idSerie = (String)super.getElObjectFromBinding("#{bindings.idSerie.inputValue}");
        String nroPlaca = (String)super.getElObjectFromBinding("#{bindings.nroPlaca.inputValue}");
        
        log.debug("id tramite: " + idTramite);
        log.debug("id tp doc: " + idTpDocumento);
        log.debug("id serie: " + idSerie);
        
        
        
        try {
            if ((esTituloMinero) && (nroPlaca == null || nroPlaca.trim().equals(""))) {
                log.debug(" ->Error en validaci�n en tr�mite");
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe digitar 'Número de placa' para continuar.");
                return;
            }
                
            if (esTituloMinero&&
                ( (idTramite == null || idTramite.trim().equals("") || idTpDocumento == null || idTpDocumento.trim().equals("") ))) {
                log.debug(" -> Error en validacion en tramite");
                        
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tramite y el tipo documental.");
                        
                return;
            }
                
            log.debug("esTituloMinero: "+esTituloMinero);
            log.debug("idTramite: "+idTramite);
            log.debug("idTpDocumento: "+idTpDocumento);
            log.debug("idSerie: "+idSerie);
                
            if (!esTituloMinero && (idSerie == null || idSerie.trim().equals(""))) {
                log.debug(" ->Error en validación en trámite");
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonomía completa. Por favor verificar");
                    
                return;
            }
            
            if (((Boolean)super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}")) && 
                ((Integer)super.getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}")) <= 0) {
                log.debug("error de validacion en responder comunicación");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "El tiempo de respuesta debe ser mayor a cero (0). Por favor verificar.");
                
                return;
            }
            
            if (((Integer)super.getElObjectFromBinding("#{bindings.nroFolios.inputValue}")) <= 0) {
                log.debug("error de validacion en numero de folios");
                super.showMessage(FacesMessage.SEVERITY_WARN, "El número de folios debe ser mayor a cero (0). Por favor verificar.");
                
                return;
            }
            
            /*DocMgmtBean docMngr = (DocMgmtBean)super.getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            log.debug("Cantidad de attachements:"+docMngr.getAddedAttachmentList().size());
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
            }*/
            
            String docName = (String)getElObjectFromBinding("#{bindings.name1.inputValue}");
            String mimeType = (String)getElObjectFromBinding("#{bindings.mimetype.inputValue}");
            Integer size = (Integer)getElObjectFromBinding("#{bindings.size.inputValue}");
            //String content = getElObjectFromBinding("#{bindings.content.inputValue}");
            
            //if (docName == null || docName.trim().equals("")) {
            if (ifAnexo.getValue() == null) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado alg�n documento principal. Por favor verificar.");                
                return;
            } 
            
            try {
                log.debug("No es la respuesta de una comunicaci�n");
                super.setElObjectIntoBinding("#{bindings.requiereRespuesta.inputValue}", Boolean.FALSE);  
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        super.setOperation(evt);
    }

    //Metodos para manejo del documento principal
    public void descargar(FacesContext fc, OutputStream os) {
        log.info("INICIO descargar");
        try {
            log.debug(" -> name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            log.debug(" -> mimeType2: " + getElObjectFromBinding("#{bindings.mimetype.inputValue}"));
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

    public void cambiarDocPpal(ValueChangeEvent vce){
            log.info("BEGIN cambiarDocPpal");
            try {
               //UploadedFile file = getFile();
               UploadedFile file = (UploadedFile) vce.getNewValue();
               log.debug(" -> file: " + file);

               log.debug(" -> fileName: " + file.getFilename());
               log.debug(" -> fileType: " + file.getContentType());
               log.debug(" -> opaqueData: " + file.getOpaqueData());
               log.debug(" -> inputStream: " + file.getInputStream());
               String fileExtn = getFileExtn(file.getFilename());
               log.debug(" -> fileExtn: " + fileExtn);

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
                       log.debug(" -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
                   }else{
                       mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                      "La extensión del archivo es correcta pero el contenido invalido, por favor actualice el archivo.");
                       limpiarDocPpal();
                   }
               }
           } catch (Exception e) {
                log.error(" -> Exception cambiarDocPpal", e);
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
            log.error(" -> Exception mostrarMensaje", e);
        }
    }

    public void limpiarDocPpal(){
        log.info("BEGIN limpiarDocPpal");    
        try {
            ifAnexo.resetValue();
            AdfFacesContext.getCurrentInstance().addPartialTarget(ifAnexo);
            /*setElObjectIntoBinding("#{bindings.name1.inputValue}", null);
            setElObjectIntoBinding("#{bindings.mimeType.inputValue}", null);
            setElObjectIntoBinding("#{bindings.size.inputValue}", null);
            setElObjectIntoBinding("#{bindings.content.inputValue}",null);*/
        } catch (Exception e) {
            log.error(" -> Exception limpiarDocPpal", e);
        }
        log.info("END limpiarDocPpal");    
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

    public void setDependencias(Collection<SelectItem> dependencias) {
        this.dependencias = dependencias;
    }

    public Collection<SelectItem> getDependencias() {
        return dependencias;
    }

    public void setDependenciasSet(Collection<AnmUnidadadministrativaTb> dependenciasSet) {
        this.dependenciasSet = dependenciasSet;
    }

    public Collection<AnmUnidadadministrativaTb> getDependenciasSet() {
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

    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
