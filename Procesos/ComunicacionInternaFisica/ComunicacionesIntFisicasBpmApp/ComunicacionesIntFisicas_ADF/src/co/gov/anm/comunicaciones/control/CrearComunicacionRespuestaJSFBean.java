package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal;
import co.gov.anm.comunicaciones.bean.WCContentLocal;
import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.AnmTipodtalseguntramite;
import co.gov.anm.comunicaciones.entity.AnmTramiteTb;
import co.gov.anm.comunicaciones.entity.Folders;

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

@ManagedBean(name="CrearComunicacionRespuestaJSFBean")
@ViewScoped
public class CrearComunicacionRespuestaJSFBean extends CommonJSFBean{
    
    @EJB
    private WCContentLocal wcContentEJB;
    
    @EJB
    private ComunicacionesSessionBeanLocal comunicacionEJB;
    
    private Logger log;
    private String user;
    
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
    private Collection<SelectItem> cuentas = new ArrayList<>();
    private Collection<SelectItem> tramites = new ArrayList<>();
    private Collection<AnmTramiteTb> tramitesSet = new ArrayList<>();
    private Collection<SelectItem> tiposDocumentales = new ArrayList<>();
    private Collection<AnmTipodtalseguntramite> tiposDocumentalesSet = new ArrayList<>();
    private final static String PARENT_TAX_NAME = "ANM";
    private List<AnmPlantilla> lstPlantilla = new ArrayList<AnmPlantilla>();
    
    
    private RichInputFile ifAnexo;
    
    public CrearComunicacionRespuestaJSFBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN CrearComunicacionRespuestaJSFBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
            setElObjectIntoBinding("#{bindings.name1.inputValue}", "");
        } catch (Exception e) {
            log.error(user + " -> Exception CrearComunicacionRespuestaJSFBean", e);
        }
        log.info("END CrearComunicacionRespuestaJSFBean");
    }
    
    
    
    @PostConstruct
    public void init () throws Exception{
        log.info("BEGIN CrearComunicacionRespuestaJSFBean init");
        try {
            Collection<Folders> taxonomiaParent = this.wcContentEJB.getFoldersFindByFolderName(PARENT_TAX_NAME);
            String parentGuid = null;
            if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
                throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
            }
            
            //Cargar listado plantillas
            lstPlantilla = comunicacionEJB.getAnmPlantillaFindAll();
            log.debug(user+" -> lstPlantilla size: "+lstPlantilla.size());

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
            log.debug(user+" -> tramitesSet size: " + tramitesSet.size());
            for (AnmTramiteTb f : tramitesSet) {
                //log.debug(user+" -> Cargando Tramite: " + f.getIdtramite().toString());
                this.tramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
            }
            
            //Verificar url Recibida
            log.debug(user+" -> urlDocumRelacionadoBO: "+getElObjectFromBinding("#{bindings.urlDocumRelacionadoBO.inputValue}"));
            
            //Limpiar valores 'Gestion documental'
            log.debug(user+" -> idCatProceso: " + getElObjectFromBinding("#{bindings.idCatProceso.inputValue}"));
            log.debug(user+" -> idTramite: " + getElObjectFromBinding("#{bindings.idTramite.inputValue}"));
            setElObjectIntoBinding("#{bindings.idTramite.inputValue}", null);
            setElObjectIntoBinding("#{bindings.idCatProceso.inputValue}", null);
            log.debug(user+" -> idCatProceso: " + getElObjectFromBinding("#{bindings.idCatProceso.inputValue}"));
            log.debug(user+" -> idTramite: " + getElObjectFromBinding("#{bindings.idTramite.inputValue}"));            
        } catch (Exception e) {
            log.error("Excepcion CrearComunicacionRespuestaJSFBean init",e);
        }
        log.info("END CrearComunicacionRespuestaJSFBean init");
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
                super.setElObjectIntoBinding("#{bindings.catProceso.inputValue}", f.getFfoldername());        
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
        
        log.debug(user+" -> tipos documentales encontrados para el tramite: " + tiposDocumentalesSet.size());
        
        this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
        for ( AnmTipodtalseguntramite f : this.tiposDocumentalesSet ){
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(),f.getNombretipodocumental()));
        }
    }

    public void cambiarTipoDocumento ( ValueChangeEvent evt ){          
        log.debug(user+" -> Inicio: cambiarTipoDocumento():"+evt.getNewValue());
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);    
        } else {
            String tipoDocSele = (String)evt.getNewValue();
            
            for ( AnmTipodtalseguntramite t:this.tiposDocumentalesSet ){
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
    
    public void crearComunicacion ( ActionEvent evt ){
        try {
            log.debug((Integer)super.getElObjectFromBinding("#{bindings.nroFolios.inputValue}"));
            Boolean esTituloMinero = (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}");
            String idTramite = (String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}");
            String idTpDocumento = (String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}");
            String idSerie = (String)super.getElObjectFromBinding("#{bindings.idSerie.inputValue}");
            
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
                log.debug(user+" -> Error en validaci�n en taxonom�a");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonomía completa. Por favor verificar");
                
                return;
            }
            
            /*DocMgmtBean docMngr = (DocMgmtBean)super.getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            log.debug("Cantidad de attachements:"+docMngr.getAddedAttachmentList().size());
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
            String mimeType = (String)getElObjectFromBinding("#{bindings.mimeType.inputValue}");
            Integer size = (Integer)getElObjectFromBinding("#{bindings.size.inputValue}");
            //String content = getElObjectFromBinding("#{bindings.content.inputValue}");
            
            if (ifAnexo.getValue() == null) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "No se ha seleccionado algún documento principal. Por favor verificar.");                
                return;
            } 
        } catch (Exception e) {
            log.error("error creando comunicacion: " + e.getMessage(), e);
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
                           setElObjectIntoBinding("#{bindings.mimeType.inputValue}", file.getContentType());
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
            setElObjectIntoBinding("#{bindings.mimeType.inputValue}", null);
            setElObjectIntoBinding("#{bindings.size.inputValue}", null);
            setElObjectIntoBinding("#{bindings.content.inputValue}",null);*/
        } catch (Exception e) {
            log.error(user + " -> Exception limpiarDocPpal", e);
        }
        log.info("END limpiarDocPpal");    
    }



    

    public void setCategoriasProceso(Collection<SelectItem> categoriasProceso) {
        this.categoriasProceso = categoriasProceso;
    }

    public Collection<SelectItem> getCategoriasProceso() {
        return categoriasProceso;
    }

    public void setProcesos(Collection<SelectItem> procesos) {
        this.procesos = procesos;
    }

    public Collection<SelectItem> getProcesos() {
        return procesos;
    }

    public void setSeries(Collection<SelectItem> series) {
        this.series = series;
    }

    public Collection<SelectItem> getSeries() {
        return series;
    }

    public void setSubseries(Collection<SelectItem> subseries) {
        this.subseries = subseries;
    }

    public Collection<SelectItem> getSubseries() {
        return subseries;
    }

    public void setCuadernos(Collection<SelectItem> cuadernos) {
        this.cuadernos = cuadernos;
    }

    public Collection<SelectItem> getCuadernos() {
        return cuadernos;
    }

    public void setCuentas(Collection<SelectItem> cuentas) {
        this.cuentas = cuentas;
    }

    public Collection<SelectItem> getCuentas() {
        return cuentas;
    }

    public void setTramites(Collection<SelectItem> tramites) {
        this.tramites = tramites;
    }

    public Collection<SelectItem> getTramites() {
        return tramites;
    }

    public void setTiposDocumentales(Collection<SelectItem> tiposDocumentales) {
        this.tiposDocumentales = tiposDocumentales;
    }

    public Collection<SelectItem> getTiposDocumentales() {
        return tiposDocumentales;
    }

    public void setLstPlantilla(List<AnmPlantilla> lstPlantilla) {
        this.lstPlantilla = lstPlantilla;
    }

    public List<AnmPlantilla> getLstPlantilla() {
        return lstPlantilla;
    }
    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
