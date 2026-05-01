package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.bean.ComunicacionesSessionBeanLocal2;
import co.gov.anm.comunicaciones.bean.WCContentLocal2;
import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.Folders;
import co.gov.anm.comunicaciones.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.entity.Tramite;

import co.gov.anm.sgd.service.WccResponse;

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
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.bpel.services.workflow.task.model.AttachmentTypeImpl;
import oracle.bpel.services.workflow.worklist.adf.DocMgmtBean;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

import org.apache.commons.io.IOUtils;




@ManagedBean(name="CrearComunicacionRespuestaJSFBean")
@ViewScoped
public class CrearComunicacionRespuestaJSFBean extends CommonJSFBean{
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1380769935553860125L;

    @EJB
    private ComunicacionesSessionBeanLocal2 comunicacionEJB;
    
    @EJB
    private WCContentLocal2 wcContentEJB;
    
    private String user;
    private final static String PARENT_TAX_NAME = "ANM";
    private transient Logger log;
    
    //Formulario gestion comunicacion
    private List<SelectItem> lstTiposDocum = new ArrayList<SelectItem>();
    private List<TipoDocumentalTramite> listaTiposDocum = new ArrayList<TipoDocumentalTramite>();
    private List<SelectItem> lstTramites = new ArrayList<SelectItem>();
    private List<Tramite> listaTramites = new ArrayList<Tramite>();
    //Taxonomia
    private List<SelectItem> lstCategoria = new ArrayList<SelectItem>();
    private List<Folders> listaCategoria = new ArrayList<Folders>();
    private List<SelectItem> lstProceso = new ArrayList<SelectItem>();
    private List<Folders> listaProceso = new ArrayList<Folders>();
    private List<SelectItem> lstSerie = new ArrayList<SelectItem>();
    private List<Folders> listaSerie = new ArrayList<Folders>();
    private List<SelectItem> lstSubserie = new ArrayList<SelectItem>();
    private List<Folders> listaSubserie = new ArrayList<Folders>();
    private List<SelectItem> lstCuaderno = new ArrayList<SelectItem>();
    private List<Folders> listaCuaderno = new ArrayList<Folders>();
    
    private Collection<AnmPlantilla> plantillasSet = new ArrayList<>();
    private RichInputFile ifAnexo;


    private Collection<SelectItem> dependencias = new ArrayList<>();
    private List<SelectItem> listaAsignacion;
    private Collection<SelectItem> usuariosDependencia = new ArrayList<>();
    private RichInputText itPlaca;

    public CrearComunicacionRespuestaJSFBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN OperacionBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
            log.debug(user + " -> wcContentEJB: " + wcContentEJB);
        } catch (Exception e) {
            log.error(user + " -> Exception OperacionBean", e);
        }
        log.info("END OperacionBean");
    }


    @PostConstruct
    public void init() throws Exception {
        log.info("BEGIN init");
        try {
            log.debug(user+" -> carga plantillas");
            plantillasSet = comunicacionEJB.getAnmPlantillaFindAll();

            //Precargar taxonomia
            precargarTaxonomia();
            
            listaTramites = this.wcContentEJB.getTramiteFindAll();
            log.debug(user+" -> listaTramites size: "+listaTramites.size());
            lstTramites = new ArrayList<SelectItem>(listaTramites.size());
            for (Tramite f : listaTramites) {
                //log.debug(user+"Cargando Tramite: " + f.getIdtramite().toString());
                lstTramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
            }

            log.debug(user+" -> url documento anterior: " +
                      (String) super.getElObjectFromBinding("#{bindings.urlDocPpal.inputValue}"));
            
        } catch (Exception e) {
            log.error(user+"Exception init CrearComunicacionRespuestaJSFBean",e);
        }
        log.info("END init");
    }
    
    
    
    /****************************************************************
     * Creado por: jucjimenezmo - Cuando ya se selecciono la taxonomia
     * Actualizado por: Adrian Molina - adrimol@gmail.com
     * Fecha actualizacion: 21Abril2017
     * *************************************************************/
    private void precargarTaxonomia() {
        log.info("BEGIN precargarTaxonomia");
        try {
            String idCatProceso = null;
            String idProceso = null;
            String idSerie = null;
            String idSubSerie = null;
            
            //Inicializar taxonomia
            listaCategoria = this.wcContentEJB.getFoldersFindByFolderName(PARENT_TAX_NAME);
            if(listaCategoria.size()>0){
                Folders parent = listaCategoria.get(0);
                listaCategoria = this.wcContentEJB.getFoldersFindByParentGuid(parent.getFfolderguid());
                log.debug(user+" -> listaCategoria size: " + listaCategoria.size());
                for (Folders f : listaCategoria) {
                    lstCategoria.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
                }
                log.debug(user+" -> lstCategoria size: " + lstCategoria.size());
            }
            //Precargar los combos hijos si aplica
            if (getElObjectFromBinding("#{bindings.idCatProceso.inputValue}") != null) {
                idCatProceso = (String) getElObjectFromBinding("#{bindings.idCatProceso.inputValue}");
                log.debug(user+" -> precargarTaxonomia() - idCatProceso: " + idCatProceso);
                cargarProcesos(idCatProceso);
                if (getElObjectFromBinding("#{bindings.idProceso.inputValue}") != null) {
                    idProceso = (String) getElObjectFromBinding("#{bindings.idProceso.inputValue}");
                    log.debug(user+" -> precargarTaxonomia() - idProceso: " + idProceso);
                    cargarSeries(idProceso);
                    if (getElObjectFromBinding("#{bindings.idSerie.inputValue}") != null) {
                        idSerie = (String) getElObjectFromBinding("#{bindings.idSerie.inputValue}");
                        log.debug(user+" -> precargarTaxonomia() - idSerie: " + idSerie);
                        cargarSubseries(idSerie);
                        if (getElObjectFromBinding("#{bindings.idSubSerie.inputValue}") != null) {
                            idSubSerie = (String) getElObjectFromBinding("#{bindings.idSubSerie.inputValue}");
                            log.debug(user+" -> precargarTaxonomia() - idSubSerie: " + idSubSerie);
                            cargarCuadernos(idSubSerie);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(user + " -> Exception precargarTaxonomia", e);
        }
        log.info("END precargarTaxonomia");
      }
    
    
    private void cargarProcesos(String catProcesoSel) {
        log.info("BEGIN cargarProcesos");
        try {
           this.listaProceso = this.wcContentEJB.getFoldersFindByParentGuid(catProcesoSel);

           log.debug(user + " -> listaProceso size: " + listaProceso.size());
           for (Folders f : listaProceso) {
               lstProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
           }
           log.debug(user + " -> lstProceso size: " + lstProceso.size());

           //Setear nombre de la categoria
           for (Folders item : listaCategoria) {
               if (item.getFfolderguid().equals(catProcesoSel)) {
                   log.debug(user + " -> item.getFfolderguid()(): " + item.getFfolderguid());
                   setElObjectIntoBinding("#{bindings.catProceso.inputValue}", item.getFfoldername());
                   break;
               }
           }
       } catch (Exception e) {
            log.error(user + " -> Exception cargarProcesos", e);
        } 
        log.info("END cargarProcesos");
    }


    private void cargarSeries(String idProceso) {
        log.info("BEGIN cargarSeries");
        try {
            //Obtener lista de series
            listaSerie = wcContentEJB.getFoldersFindByParentGuid(idProceso);
            log.debug(user + " -> listaSerie size: " + listaSerie.size());
            for (Folders f : listaSerie) {
                lstSerie.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }
            log.debug(user + " -> lstSerie size: " + lstSerie.size());

            //Setear nombre del proceso
            for (Folders item : listaProceso) {
                if (item.getFfolderguid().equals(idProceso)) {
                    log.debug(user + " -> item.getFfolderguid()(): " + item.getFfolderguid());
                    setElObjectIntoBinding("#{bindings.proceso.inputValue}", item.getFfoldername());
                    break;
                }
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cargarSeries", e);
        }
        log.info("BEGIN cargarSeries");
    }


    private void cargarSubseries(String idSerie) {
        log.info("BEGIN cargarSubseries");
        try {
           //Setear nombre de la serie
           for (Folders item : listaSerie) {
               if (item.getFfolderguid().equals(idSerie)) {
                   log.debug(user + " -> item.getFfolderguid()(): " + item.getFfolderguid());
                   setElObjectIntoBinding("#{bindings.serie.inputValue}", item.getFfoldername());
                   break;
               }
           }
           //Obtener lista de subseries
           listaSubserie = wcContentEJB.getFoldersFindByParentGuid(idSerie);
           log.debug(user + " -> listaSubserie size: " + listaSubserie.size());
           for (Folders f : listaSubserie) {
               lstSubserie.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
           }
           log.debug(user + " -> lstSubserie size: " + lstSubserie.size());
       } catch (Exception e) {
            log.error(user + " -> Exception cargarSubseries", e);
        }
        log.info("END cargarSubseries");    
    }

    private void cargarCuadernos(String idSubSerie) {
        log.info("END cargarCuadernos");    
        try {
            //Obtener lista de expedientes
            listaCuaderno = wcContentEJB.getFoldersFindByParentGuid(idSubSerie);
            log.debug(user + " -> listaExpediente size: " + listaCuaderno.size());
            for (Folders f : listaCuaderno) {
                lstCuaderno.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }
            log.debug(user + " -> lstExpediente size: " + lstCuaderno.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cargarCuadernos", e);
        }
        log.info("END cargarCuadernos");  
    }
    
    


    public void cambiarCategoriaProceso ( ValueChangeEvent evt ){
        log.debug(user+" -> inicio cambiarCategoriaProceso(): "+evt.getNewValue());
        
        String catProcesoSel = (String)evt.getNewValue();
        listaProceso = this.wcContentEJB.getFoldersFindByParentGuid(catProcesoSel);
        lstProceso = new ArrayList<SelectItem>(this.listaProceso.size());
        
        
        for ( Folders f:listaProceso ){
            lstProceso.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
        }
        
        //setea los demas datos de la taxonomia        
        for ( Folders f:listaProceso ){
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
            lstSerie = new ArrayList<SelectItem>(0);
            listaSerie = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);    
        } else {
            String idProceso = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", idProceso);
            
            for ( Folders f:this.listaProceso ){
                if ( f.getFfolderguid().equals(idProceso) ){
                    super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", f.getFfoldername());        
                    break;
                }
            }
                                    
            listaSerie = this.wcContentEJB.getFoldersFindByParentGuid(idProceso);
            lstSerie = new ArrayList<SelectItem>(listaSerie.size());
            
            
            for ( Folders f:listaSerie ){
                lstSerie.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }            
        }        
        this.cambiarSerie(null);
    }    
    

    public void cambiarSerie ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio cambiarSerie()");
        if ( evt==null  || evt.getNewValue()==null  ){
            lstSubserie = new ArrayList<SelectItem>(0);
            listaSubserie = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);    
        }
        else{
            String idSerie = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", idSerie);
            
            for ( Folders f:listaSubserie ){
                if ( f.getFfolderguid().equals(idSerie) ){
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            listaSubserie = this.wcContentEJB.getFoldersFindByParentGuid(idSerie);
            lstSubserie = new ArrayList<SelectItem>(listaSubserie.size());
            for ( Folders f:listaSubserie ){
                lstSubserie.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
            }
        }        
        this.cambiarSubSerie(null);
    }    
    

    public void cambiarSubSerie ( ValueChangeEvent evt ){
        log.debug(user+" -> Inicio: cambiarSubSerie()");
        
        if ( evt==null || evt.getNewValue()==null  ){
            lstCuaderno = new ArrayList<SelectItem>(0);
            listaCuaderno = new ArrayList<Folders>(0);            
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);    
        } else {
            String idSubSerie = (String)evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", idSubSerie);
            
            for ( Folders f:listaCuaderno ){
                if ( f.getFfolderguid().equals(idSubSerie) ){
                    super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFfoldername());        
                    break;
                }
            }
            
            listaCuaderno = this.wcContentEJB.getFoldersFindByParentGuid(idSubSerie);
            lstCuaderno = new ArrayList<SelectItem>(listaCuaderno.size());
            
            
            for ( Folders f:listaCuaderno ){
                lstCuaderno.add(new SelectItem(f.getFfolderguid(),f.getFfoldername()));
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
            
            for ( Folders f:listaCuaderno ){
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
            listaTiposDocum = new ArrayList<TipoDocumentalTramite>();
            lstTiposDocum = new ArrayList<SelectItem>();
            return;
        }
        
        String tramiteSelected = (String)evt.getNewValue();
        for ( Tramite t:listaTramites ){
            if ( tramiteSelected.equals(t.getIdtramite().toString()) ){
                super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                break;
            }
        }
        
        listaTiposDocum = this.wcContentEJB.getTipoDocumentalTramiteTiposDocumentalesTramites(new BigDecimal(tramiteSelected));
        
        log.debug(user+" -> tipos documentales encontrados para el tramite: " + listaTiposDocum.size());
        
        lstTiposDocum = new ArrayList<SelectItem>(listaTiposDocum.size());
        for ( TipoDocumentalTramite f : listaTiposDocum ){
            lstTiposDocum.add(new SelectItem(f.getIdtipodtal().toString(),f.getNombretipodocumental()));
        }
    }

    public void cambiarTipoDocumento ( ValueChangeEvent evt ){          
        log.debug(user+" -> Inicio: cambiarTipoDocumento():"+evt.getNewValue());
        
        if (evt==null|| evt.getNewValue()==null){            
            super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);    
        } else {
            String tipoDocSele = (String)evt.getNewValue();
            
            for ( TipoDocumentalTramite t:listaTiposDocum ){
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
            log.debug(user+" -> es titulo minero: " + (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}"));
            log.debug(user+" -> id tramite: " + (String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}"));
            log.debug(user+" -> id tp doc: " + (String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}"));
            log.debug(user+" -> id sub serie: " + (String)super.getElObjectFromBinding("#{bindings.idSubSerie.inputValue}"));
            
            if (((Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) &&
                (((String)super.getElObjectFromBinding("#{bindings.idTramite.inputValue}")) == null &&
                    ((String)super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}")) == null)) {
                log.debug(user+" -> Error en validaci�n en tr�mite");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tr�mite y el tipo documental.");
                
                return;
            }            
            
            if (!((Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) &&
                ((String)super.getElObjectFromBinding("#{bindings.idSubSerie.inputValue}")) == null) {
                log.debug(user+" -> Error en validaci�n en taxonom�a");
                
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar la taxonom�a completa. Por favor verificar");
                
                return;
            }            
            
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
            
            try {
                log.debug(user+" -> requiereRespuesta: "+super.getElObjectFromBinding("#{bindings.requiereRespuesta.inputValue}"));
                log.debug(user+" -> responderComunicacion: "+super.getElObjectFromBinding("#{bindings.responderComunicacion.inputValue}"));
                
                super.setElObjectIntoBinding("#{bindings.requiereRespuesta.inputValue}", Boolean.TRUE);  
            } catch (Exception e) {
                log.error("error seteando valor de requiereRespuesta: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("error creando la comunicacion: " + e.getMessage(), e);
        }
        
        super.setOperation(evt);
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
                           log.debug(user+" nombre modificado: "+nombreModificado);
                           setElObjectIntoBinding("#{bindings.name.inputValue}", nombreModificado);
                           log.debug(user+" -> name: "+getElObjectFromBinding("#{bindings.name.inputValue}"));
                           //Setear demas variables del adjunto
                           setElObjectIntoBinding("#{bindings.mimeType.inputValue}", file.getContentType());
                           setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                           byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                           setElObjectIntoBinding("#{bindings.content.inputValue}", java.util.Base64.getEncoder().encodeToString(bytes));
                       }
                       log.debug(user + " -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name.inputValue}"));
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
           } catch (Exception e) {
                log.error(user + " -> Exception limpiarDocPpal", e);
            }
            log.info("END limpiarDocPpal");    
        }

    public void descargar(FacesContext fc, OutputStream os) {
            log.info("INICIO descargar");
            try {
                log.debug(user + " -> name: " + getElObjectFromBinding("#{bindings.name.inputValue}"));
                log.debug(user + " -> mimeType: " + getElObjectFromBinding("#{bindings.mimeType.inputValue}"));
                log.debug(user + " -> size: " + getElObjectFromBinding("#{bindings.size.inputValue}"));
                byte[] encodeFile = getElObjectFromBinding("#{bindings.content.inputValue}").toString().getBytes();
                log.debug(user + " -> length-e: " + encodeFile.length);
                //Decodificar archivo recibido en el payload
                byte[] decodeFile = java.util.Base64.getDecoder().decode(encodeFile);
                log.debug(user + " -> length-d: " + decodeFile.length);
                //Crear response para el navegador
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + getElObjectFromBinding("#{bindings.name.inputValue}"));
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
    
    
    public void cambiarEsExpMinero (ValueChangeEvent evt) {
        log.debug(user+" -> cambiarEsExpMinero():"+evt.getNewValue());
        Boolean selectedValue = (Boolean)evt.getNewValue();
        log.debug(user+" -> selectedValue: "+selectedValue);
        try {
          
        } catch (Exception e) {
            log.error("error cambiandoExpMinero: " + e.getMessage(), e);
        }
    }
    
    public void validarExpMinero (ValueChangeEvent event) {
        log.debug(user+" -> validarExpMinero():"+event.getNewValue());
        String expMinero = (String) event.getNewValue();
        log.debug(user+" -> expMinero: "+expMinero);
        try {   
           
        } catch(Exception e) {
            log.error("error validando exp minero: " + e.getMessage(), e);
        }
    }    
    
    

     /************************************************************
      * ACCESORES
      * ********************************************************/

   

    public void setPlantillasSet(Collection<AnmPlantilla> plantillasSet) {
        this.plantillasSet = plantillasSet;
    }

    public Collection<AnmPlantilla> getPlantillasSet() {
        return plantillasSet;
    }

    public void setLstCategoria(List<SelectItem> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public List<SelectItem> getLstCategoria() {
        return lstCategoria;
    }

    public void setListaCategoria(List<Folders> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<Folders> getListaCategoria() {
        return listaCategoria;
    }

    public void setLstProceso(List<SelectItem> lstProceso) {
        this.lstProceso = lstProceso;
    }

    public List<SelectItem> getLstProceso() {
        return lstProceso;
    }

    public void setListaProceso(List<Folders> listaProceso) {
        this.listaProceso = listaProceso;
    }

    public List<Folders> getListaProceso() {
        return listaProceso;
    }

    public void setLstSerie(List<SelectItem> lstSerie) {
        this.lstSerie = lstSerie;
    }

    public List<SelectItem> getLstSerie() {
        return lstSerie;
    }

    public void setListaSerie(List<Folders> listaSerie) {
        this.listaSerie = listaSerie;
    }

    public List<Folders> getListaSerie() {
        return listaSerie;
    }

    public void setLstSubserie(List<SelectItem> lstSubserie) {
        this.lstSubserie = lstSubserie;
    }

    public List<SelectItem> getLstSubserie() {
        return lstSubserie;
    }

    public void setListaSubserie(List<Folders> listaSubserie) {
        this.listaSubserie = listaSubserie;
    }

    public List<Folders> getListaSubserie() {
        return listaSubserie;
    }

    public void setLstCuaderno(List<SelectItem> lstCuaderno) {
        this.lstCuaderno = lstCuaderno;
    }

    public List<SelectItem> getLstCuaderno() {
        return lstCuaderno;
    }

    public void setListaCuaderno(List<Folders> listaCuaderno) {
        this.listaCuaderno = listaCuaderno;
    }

    public List<Folders> getListaCuaderno() {
        return listaCuaderno;
    }

    public void setLstTiposDocum(List<SelectItem> lstTiposDocum) {
        this.lstTiposDocum = lstTiposDocum;
    }

    public List<SelectItem> getLstTiposDocum() {
        return lstTiposDocum;
    }

    public void setListaTiposDocum(List<TipoDocumentalTramite> listaTiposDocum) {
        this.listaTiposDocum = listaTiposDocum;
    }

    public List<TipoDocumentalTramite> getListaTiposDocum() {
        return listaTiposDocum;
    }

    public void setLstTramites(List<SelectItem> lstTramites) {
        this.lstTramites = lstTramites;
    }

    public List<SelectItem> getLstTramites() {
        return lstTramites;
    }

    public void setListaTramites(List<Tramite> listaTramites) {
        this.listaTramites = listaTramites;
    }

    public List<Tramite> getListaTramites() {
        return listaTramites;
    }

    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }

    public void setListaAsignacion(List<SelectItem> listaAsignacion) {
        this.listaAsignacion = listaAsignacion;
    }

    public List<SelectItem> getListaAsignacion() {
        return listaAsignacion;
    }

    public void setDependencias(Collection<SelectItem> dependencias) {
        this.dependencias = dependencias;
    }

    public Collection<SelectItem> getDependencias() {
        return dependencias;
    }

    public void setUsuariosDependencia(Collection<SelectItem> usuariosDependencia) {
        this.usuariosDependencia = usuariosDependencia;
    }

    public Collection<SelectItem> getUsuariosDependencia() {
        return usuariosDependencia;
    }

    public void setItPlaca(RichInputText itPlaca) {
        this.itPlaca = itPlaca;
    }

    public RichInputText getItPlaca() {
        return itPlaca;
    }
}
