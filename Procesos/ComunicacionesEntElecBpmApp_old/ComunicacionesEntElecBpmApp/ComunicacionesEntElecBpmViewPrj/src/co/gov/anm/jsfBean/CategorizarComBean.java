package co.gov.anm.jsfBean;

import co.gov.anm.cee.utils.JsfUtil;
import co.gov.anm.cee.utils.PropertiesKeys;
import co.gov.anm.cee.utils.PropertiesLoader;
import co.gov.anm.entity.AnmTipodtalseguntramite;
import co.gov.anm.entity.AnmTramiteTb;
import co.gov.anm.entity.Folderfolders;
import co.gov.anm.entity.SgdEtnia;
import co.gov.anm.entity.SgdPoblacionVulnerable;
import co.gov.anm.entity.SgdRangoEdad;
import co.gov.anm.session.com.ComunicacionesEntSessionEJB9;

import co.gov.anm.session.com.ComunicacionesEntSessionEJBLocal9;
import co.gov.anm.session.wcc.WCCSessionEJB9;
import co.gov.anm.session.wcc.WCCSessionEJBLocal9;

import co.gov.anm.view.FirmarPortClient;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.math.BigDecimal;

import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.model.AutoSuggestUIHints;

import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import org.apache.log4j.Logger;


@ManagedBean(name = "categorizarComBean")
@ViewScoped
public class CategorizarComBean {
    
    private final static String PARENT_TAX_NAME = "ANM"; //Directorio raiz de la taxonomia - libreria ANM
    
    @EJB
    private WCCSessionEJBLocal9 wccBean;
    @EJB
    private ComunicacionesEntSessionEJBLocal9 comBean;
    
    private Logger log;
    private String user;
    
    private Collection<SelectItem> listaDependencias;
    private List<SelectItem> listaRangoEdadSI = new ArrayList<SelectItem>();
    private List<SgdRangoEdad> listaRangoEdad = new ArrayList<SgdRangoEdad>();
    private List<SelectItem> listaPoblacionVulnerableSI = new ArrayList<SelectItem>();
    private List<SgdPoblacionVulnerable> listaPoblacionVulnerable = new ArrayList<SgdPoblacionVulnerable>();
    private List<SelectItem> listaEtniaSI = new ArrayList<SelectItem>();
    private List<SgdEtnia> listaEtnia = new ArrayList<SgdEtnia>();
    private Boolean marcaPqrs = false;
    private String marcaRequiereRespuesta = "N";
    
    //Gestion Documental
    private boolean renderedSubseries;
    private boolean renderedCuadernos;
    private List<SelectItem> tramites = new ArrayList<>();
    private List<AnmTramiteTb> tramitesSet = new ArrayList<>();
    private List<SelectItem> tiposDocumentales = new ArrayList<>();
    private List<AnmTipodtalseguntramite> tiposDocumentalesSet = new ArrayList<>();
    private Collection<SelectItem> categoriasProceso = new ArrayList<>();
    private Collection<Folderfolders> categoriasProcesoSet = new ArrayList<>();
    private Collection<SelectItem> procesos = new ArrayList<>();
    private Collection<Folderfolders> procesosSet = new ArrayList<>();
    private Collection<SelectItem> series = new ArrayList<>();
    private Collection<Folderfolders> seriesSet = new ArrayList<>();
    private Collection<SelectItem> subseries = new ArrayList<>();
    private Collection<Folderfolders> subseriesSet = new ArrayList<>();
    private Collection<SelectItem> cuadernos = new ArrayList<>();
    private Collection<Folderfolders> cuadernosSet = new ArrayList<>();
    private RichInputText itCuaderno;
    
    private String id = "";
    private String usuario = "";
    private String contrasenia = "";

    public CategorizarComBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN CategorizarComBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error(user + " -> Exception CategorizarComBean", e);
        }
        log.info("END CategorizarComBean");
    }
    
    @PostConstruct
    public void inicializar() {
        log.info("Inicio CategorizarComBean - inicializar");
     
        // capturas de marcaPqrs y Requiere respuesta
        Object isPrsObj = getElObjectFromBinding("#{bindings.isPQRSDO.inputValue}");
        if(isPrsObj != null){
            this.marcaPqrs = (Boolean)isPrsObj;
        }
        Object requiereRespuestaObj = getElObjectFromBinding("#{bindings.requiereRespuestaDO.inputValue}");
        if(requiereRespuestaObj != null){
            this.marcaRequiereRespuesta = requiereRespuestaObj.toString();
        }
        
        log.debug("se recibe isPQRS :"+isPrsObj);
        log.debug("se recibe requiereRespuestaDO :"+requiereRespuestaObj);
     
        try{
            String docPrincipal = (String)getElObjectFromBinding("#{bindings.urlDocumentoPrincipalDO.inputValue}");
            log.debug(user+" -> isRenderDocPrincipal: "+docPrincipal);
            
            String docAdjunto = (String)getElObjectFromBinding("#{bindings.urlArchivoAdjuntoDO.inputValue}");
            log.debug(user+" -> isRenderAdjunto: "+docAdjunto);
            
        }catch (Exception e) {
            log.error("Error obteniendo urls documentos. Excepcion: " + e);
        }
        
        
        //Generacion de listado rangos de edad
        try {
            Object idRangoEdad = getElObjectFromBinding("#{bindings.idRangoEdad.inputValue}");
            log.info("Valor actual idRangoEdad: "+idRangoEdad);
            log.info("Tipo dato idRangoEdad: "+idRangoEdad.getClass());
            log.info("Inicio generar listaRagoEdad");
            this.listaRangoEdad = this.comBean.getSgdRangoEdadFindAll();
            log.debug(user+" -> lista tamano listaRangoEdad : " + listaRangoEdad.size());
            for (SgdRangoEdad rangoEdad : listaRangoEdad) {
                this.listaRangoEdadSI.add(new SelectItem(rangoEdad.getIdRangoEdad(), rangoEdad.getDescripcion()));
            }
            ;
        } catch (Exception e) {
            log.error("Error generando listaRangoEdad " + e);
        }

        //Generacion de listado poblacion vulnerable
        try {
            Object idPoblacionVulnerable = getElObjectFromBinding("#{bindings.idPoblacionVulnerable.inputValue}");
            log.info("Valor actual idPoblacionVulnerable: "+idPoblacionVulnerable);
            log.info("Tipo dato idPoblacionVulnerable: "+idPoblacionVulnerable.getClass());
            log.info("Inicio generar listaPoblacionVulnerable");
            this.listaPoblacionVulnerable = this.comBean.getSgdPoblacionVulnerableFindAll();
            log.debug(user+" -> lista tamano listaPoblacionVulnerable : " + listaPoblacionVulnerable.size());
            for (SgdPoblacionVulnerable pobvul : listaPoblacionVulnerable) {
                this.listaPoblacionVulnerableSI.add(new SelectItem(pobvul.getIdPoblacionVulnerable(),
                                                                   pobvul.getNombre()));
            }

        } catch (Exception e) {
            log.error("Error generando listaPoblacionVulnerable " + e);
        }

        //Generacion de listado etnia
        try {
            Object idEtnia = getElObjectFromBinding("#{bindings.idEtnia.inputValue}");
            log.info("Valor actual idEtnia: "+idEtnia);
            log.info("Tipo dato idEtnia: "+idEtnia.getClass());
            log.info("Inicio generar listaEtnia");
            this.listaEtnia = this.comBean.getSgdEtniaFindAll();
            log.debug(user+" -> lista tamano listaEtnia : " + listaEtnia.size());
            for (SgdEtnia etnia : listaEtnia) {
                this.listaEtniaSI.add(new SelectItem(etnia.getIdEtnia(), etnia.getNombre()));
            }

        } catch (Exception e) {
            log.error("Error generando listaEtnia " + e);
        }   
        
        try{
            //llenar tramites
            this.tramitesSet = this.wccBean.getTramiteFindAll();
            this.tramites = new ArrayList<SelectItem>(this.tramitesSet.size());
            for (AnmTramiteTb f : tramitesSet) {
                this.tramites.add(new SelectItem(f.getIdtramite().intValue(), f.getTramite()));
            }
            log.debug(user+" -> Tramites cargados");
        } catch (Exception e) {
            log.error("Error cargando tramites " + e);
        }   
        
        //Configuracion de la taxonomia
        try {

            Collection<Folderfolders> taxonomiaParent =
                this.wccBean.getFolderFoldersFindByFolderName(PARENT_TAX_NAME);
            String parentGuid = null;
            if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
                log.error("Se debe configurar el nodo principal de la taxonomia 'ANM'");
                throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
            }
            for (Folderfolders f : taxonomiaParent) {
                parentGuid = f.getFfolderguid();
                break;
            }
            
            this.categoriasProcesoSet = this.wccBean.getFolderFoldersFindByParentGuid(parentGuid);
            this.categoriasProceso = new ArrayList<SelectItem>(categoriasProcesoSet.size());
            for (Folderfolders f : categoriasProcesoSet) {
                this.categoriasProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }

        } catch (Exception e) {
            log.error("Failed configuracion de la taxonomia" + e);
        }
        
        
        //Configuracion usuario y contraseña para firma documento
        try{
            id = PropertiesLoader.getInstance().getProperty(PropertiesKeys.SGD_FIRMAR_ID);
            log.info("Identificacion firma documento parametrizado: "+id);
            usuario = PropertiesLoader.getInstance().getProperty(PropertiesKeys.SGD_FIRMAR_USR);
            log.info("Usuario firma documento parametrizado: "+usuario);
            contrasenia = PropertiesLoader.getInstance().getProperty(PropertiesKeys.SGD_FIRMAR_PWD);
            log.info("Credenciales obtenidas exitosamente!");
            setElObjectIntoBinding("#{bindings.idFirma.inputValue}", id);
            log.debug(user+" -> idFirma seteado correctamente");
            setElObjectIntoBinding("#{bindings.userFirma.inputValue}", usuario);
            log.debug(user+" -> userFirma seteado correctamente");
            Object usuarioFirmante = getElObjectFromBinding("#{bindings.userFirma.inputValue}");
            log.debug(user+" -> usuarioFirmante: "+usuarioFirmante);
            setElObjectIntoBinding("#{bindings.password.inputValue}", contrasenia);
            log.info("Credenciales seteadas exitosamente!");
            
        }catch (Exception e) {
            log.error("Falló la configuración de credenciales para Firmar documento: " + e);
        }
        
        log.info("Fin CategorizarComBean - inicializar");
    }
    
    public void enviarComunicado(ActionEvent action) {
        boolean formCompleto = true;
        
        /*
        log.info("Invocando servicio de firma juridica");
        Object taskNumber = this.getElObjectFromBinding("#{bindings.taskNumber.inputValue}");
        //Object usuario = this.getElObjectFromBinding("#{bindings.id.inputValue}");
        Object usuario = ADFContext.getCurrent().getSecurityContext().getUserName();
        Object numeroRadicado = this.getElObjectFromBinding("#{bindings.nroRadicado.inputValue}");
        Object contrasenaEncriptada = this.getElObjectFromBinding("#{bindings.password.inputValue}");
        
        if(Objects.nonNull(taskNumber) 
            && Objects.nonNull(usuario) 
            && Objects.nonNull(numeroRadicado) 
            && Objects.nonNull(contrasenaEncriptada) 
            && !usuario.toString().isEmpty()
            && !numeroRadicado.toString().isEmpty()
            && !contrasenaEncriptada.toString().isEmpty()){
            try {
                String wsdlUrl = PropertiesLoader.getInstance().getProperty(PropertiesKeys.SGD_FIRMA_WS_URL);
                log.debug("wsdlUrl :"+wsdlUrl);
                FirmarPortClient.firmarPDF(numeroRadicado.toString(), Integer.valueOf(taskNumber.toString()),
                                           usuario.toString(), usuario.toString(), contrasenaEncriptada.toString(),
                                           wsdlUrl);
                log.info("Servicio invocado exitosamente ...");
            } catch (MalformedURLException e) {
                log.error("Error al invocar el servicio");
                formCompleto= false;
            }
        }else{
            log.error("Datos para firma incompletos ");
            log.error("taskNumber :"+taskNumber);
            log.error("usuario :"+usuario);
            log.error("numeroRadicado :"+numeroRadicado);
            log.error("contrasenaEncriptada :"+contrasenaEncriptada);
        }*/
         
        if(formCompleto){
            this.setOperation(action);
        }
    }
    
    /**
     *Metodo que se encarga de asignar la operacion correspondiente a un boton.
     *Este metodo llama al metodo setOperation de la clase InvokeActionBean.
     * @param action
     */
    protected void setOperation(ActionEvent action) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        InvokeActionBean invokeActionBean =
            (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
        invokeActionBean.setOperation(action);
    }
    
    
    public boolean isRenderDocPrincipal(){
        try{
            String docPrincipal = (String)getElObjectFromBinding("#{bindings.urlDocumentoPrincipalDO.inputValue}");
            log.debug(user+" -> isRenderDocPrincipal: "+docPrincipal);
            
            return docPrincipal!=null && docPrincipal.length()>1;
        }catch(Exception e){
            log.error("Error isRenderDocPrincipal: " + e);
        }
        return false;
    }
    
    public boolean isRenderAdjunto(){
        try{
            String docAdjunto = (String)getElObjectFromBinding("#{bindings.urlArchivoAdjuntoDO.inputValue}");
            log.debug(user+" -> isRenderAdjunto: "+docAdjunto);
            
            return docAdjunto!=null && docAdjunto.length()>1;
        }catch(Exception e){
            log.error("Error isRenderAdjunto: " + e);
        }
        return false;
    }
        
    public void setElObjectIntoBinding(String expr, Object valor) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
        ve.setValue(elContext, valor);
    }

    protected Object getElObjectFromBinding(String expr) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
        Object res = ve.getValue(facesCtx.getELContext());
        return res;
    }

    
    public void cambiarTramite(ValueChangeEvent vce) {
        this.tiposDocumentales.clear();
        log.debug(user+" -> Inicio: cambiarTramite(): " + vce.getNewValue());
        //log.debug(user+" -> Inicio: cambiarTramite(): " + vce.getNewValue().getClass());

        Integer tramiteSelected = Integer.parseInt(vce.getNewValue().toString());

        for (AnmTramiteTb t : this.tramitesSet) {
            if (tramiteSelected.equals(t.getIdtramite().intValue())) {
                log.debug(user+" -> t.getTramite(): " + t.getTramite());
                setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                break;
            }
        }
        cargarTiposDocumentales(tramiteSelected);
    }
    
    private void cargarTiposDocumentales ( Integer tramiteSelected ){

        this.tiposDocumentalesSet =
            this.wccBean.getTipoDocumentalTramiteFindByTramite(BigDecimal.valueOf(tramiteSelected));
        this.tiposDocumentales = new ArrayList<SelectItem>();
        for (AnmTipodtalseguntramite f : tiposDocumentalesSet) {
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().intValue(), f.getNombretipodocumental()));
        }        
    }

    public void cambiarTipoDocumento(ValueChangeEvent vce) {
        log.debug(user+" -> Inicio: cambiarTipoDocumento():" + vce.getNewValue());
        //log.debug(user+" -> Inicio: cambiarTipoDocumento():" + vce.getNewValue().getClass());

        Integer tipoDocSele = (Integer) vce.getNewValue();
        for (AnmTipodtalseguntramite t : this.tiposDocumentalesSet) {
            if (tipoDocSele.equals(t.getIdtipodtal().intValue())) {
                setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                break;
            }
        }
    }

    public void cambiarCategoriaProceso(ValueChangeEvent vce) {
        log.debug(user+" -> inicio cambiarCategoriaProceso(): " + vce.getNewValue());

        String catProcesoSel = (String) vce.getNewValue();
        cargarProcesos(catProcesoSel);
        this.cambiarProceso(null);
        log.debug(user+" -> Fin: cambiarCategoriaProceso()");
    }
    
    private void cargarProcesos ( String catProcesoSel ){
        this.procesosSet = this.wccBean.getFolderFoldersFindByParentGuid(catProcesoSel);
        this.procesos = new ArrayList<SelectItem>(this.procesosSet.size());

        log.debug(user+" -> cargarProcesos(): Encontro: " + this.procesosSet.size() + " Procesos");

        for (Folderfolders f : procesosSet) {
            this.procesos.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }
        //setea los demas datos de la taxonomia
        for (Folderfolders f : this.categoriasProcesoSet) {
            if (f.getFfolderguid().equals(catProcesoSel)) {
                log.debug(user+" -> cambiarCategoriaProceso(): Define la categoria de proceso: " + f.getFfoldername());
                setElObjectIntoBinding("#{bindings.catProceso.inputValue}", f.getFfoldername());
                break;
            }
        }        
    }
    
    public void cambiarProceso(ValueChangeEvent vce) {
        log.debug(user+" -> inicio cambiarProceso(): " +
                  (vce != null && vce.getNewValue() != null ? vce.getNewValue() : "vacio"));

        if (vce == null || vce.getNewValue() == null) {
            this.series = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folderfolders>(0);
            setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);
        } else {
            String idProceso = (String) vce.getNewValue();
            setElObjectIntoBinding("#{bindings.idProceso.inputValue}", idProceso);
            cargarSeries(idProceso);
        }

        this.cambiarSerie(null);
    }
    
    private void cargarSeries ( String idProceso ){

        for (Folderfolders f : this.procesosSet) {
            if (f.getFfolderguid().equals(idProceso)) {
                setElObjectIntoBinding("#{bindings.proceso.inputValue}", f.getFfoldername());
                break;
            }
        }

        this.seriesSet = this.wccBean.getFolderFoldersFindByParentGuid(idProceso);
        this.series = new ArrayList<SelectItem>(this.seriesSet.size());

        log.debug(user+" -> cargarSeries(): Encontro: " + this.seriesSet.size() + " Series");

        for (Folderfolders f : seriesSet) {
            this.series.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }
    }

    public void cambiarSerie(ValueChangeEvent vce) {
        log.debug(user+" -> Inicio cambiarSerie()");
        if (vce == null || vce.getNewValue() == null) {
            this.subseries = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folderfolders>(0);
            setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
            setElObjectIntoBinding("#{bindings.serie.inputValue}", null);
        } else {
            String idSerie = (String) vce.getNewValue();
            setElObjectIntoBinding("#{bindings.idSerie.inputValue}", idSerie);

            for (Folderfolders f : this.seriesSet) {
                if (f.getFfolderguid().equals(idSerie)) {
                    setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFfoldername());
                    if(f.getFfoldername().contains("-")){
                        String[] serieArray = f.getFfoldername().split("-");
                        setElObjectIntoBinding("#{bindings.codigoSerie.inputValue}", serieArray[0]);
                        setElObjectIntoBinding("#{bindings.nombreSerie.inputValue}", serieArray[1]);
                    }else{
                        setElObjectIntoBinding("#{bindings.codigoSerie.inputValue}", f.getFfoldername());
                        setElObjectIntoBinding("#{bindings.nombreSerie.inputValue}", f.getFfoldername());
                    }
                    break;
                }
            }
            cargarSubseries(idSerie);

        }

        this.cambiarSubSerie(null);
    }
    
    private void cargarSubseries ( String idSerie ){        

        this.subseriesSet = this.wccBean.getFolderFoldersFindByParentGuid(idSerie);
        this.subseries = new ArrayList<SelectItem>(this.subseriesSet.size());
        log.debug(user+" -> cambiarSerie(): Encontro: " + this.subseries.size() + " Subseries");

        for (Folderfolders f : subseriesSet) {
            this.subseries.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }

        //validacion para pintar el selecOneChoice de subserie
        if (this.subseries.isEmpty()) {
            this.renderedSubseries = false;
            log.debug(user+" -> No hay subseries documentales para esta serie");
        } else {
            this.renderedSubseries = true;
            log.debug(user+" -> S� hay subseries documentales para esta serie");

        }
    }
    
    public void cambiarSubSerie(ValueChangeEvent vce) {
        try {
           log.debug(user+" -> Inicio: cambiarSubSerie()");

           if (vce == null || vce.getNewValue() == null) {
               this.cuadernos = new ArrayList<SelectItem>(0);
               this.cuadernosSet = new ArrayList<Folderfolders>(0);
               setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
               setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);
           } else {
               String idSubSerie = (String) vce.getNewValue();
               setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", idSubSerie);

               for (Folderfolders f : this.subseriesSet) {
                   if (f.getFfolderguid().equals(idSubSerie)) {
                       setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFfoldername());
                       if(f.getFfoldername().contains("-")){
                           String[] serieArray = f.getFfoldername().split("-");
                           setElObjectIntoBinding("#{bindings.codigoSubSerie.inputValue}", serieArray[0]);
                           setElObjectIntoBinding("#{bindings.nombreSubSerie.inputValue}", serieArray[1]);
                       }else{
                           setElObjectIntoBinding("#{bindings.codigoSubSerie.inputValue}", f.getFfoldername());
                           setElObjectIntoBinding("#{bindings.nombreSubSerie.inputValue}", f.getFfoldername());
                       }
                       break;
                   }
               }
               cargarCuadernos(idSubSerie);
               
               // Valida si es PQRS
               log.info("Valida si es PQRS  codigoSerie "+ getElObjectFromBinding("#{bindings.codigoSerie.inputValue}"));
               
               if ( getElObjectFromBinding("#{bindings.codigoSerie.inputValue}").equals("SD47")){
                    setElObjectIntoBinding("#{bindings.isPQRS.inputValue}", true);
                   setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", "S");
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", 'S');
                   
                    log.info("Valida si es PQRS  isPQRS "+ getElObjectFromBinding("#{bindings.isPQRS.inputValue}"));
               }else {
                    //En caso de que no sea tipificada se asigna como el usuario la marco
                    setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", this.marcaPqrs? "S" : "N");
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", this.marcaRequiereRespuesta);
                }
           }
           this.cambiarCuaderno(null);
        } catch (Exception e) {
            log.error("Exception cambiarSubSerie",e);
        }
    }
    
    private void cargarCuadernos ( String idSubSerie ){

        this.cuadernosSet = this.wccBean.getFolderFoldersFindByParentGuid(idSubSerie);
        this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());

        log.debug(user+" -> cambiarSubSerie(): Encontro: " + this.cuadernos.size() + " cuadernos");

        for (Folderfolders f : cuadernosSet) {
            this.cuadernos.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }

        //validacion para pintar el selecOneChoice de cuaderno (expediente)
        if (this.cuadernos.isEmpty()) {
            this.renderedCuadernos = false;
            log.debug(user+" -> No hay subseries documentales para esta serie");
        } else {
            this.renderedCuadernos = true;
            log.debug(user+" -> S� hay subseries documentales para esta serie");
        }
    }
    
    public List getCuadernos(FacesContext facesContext, AutoSuggestUIHints autoSuggestUIHints) {
        log.debug(user+" -> Inicio getCuadernos()");
        
        String value = autoSuggestUIHints.getSubmittedValue();
        log.debug(user+" -> value: "+value);
        List<SelectItem> items = new ArrayList<SelectItem>();
        
        items = cuadernos.stream()
                 .filter(cuaderno -> cuaderno.getLabel().toUpperCase().contains(value.toUpperCase()))
                 .collect(Collectors.toList());
            
        log.debug(user+" -> items size: "+items.size());
        log.debug(user+" -> Fin getCuadernos()");
        
        return items;
    }

    public void cambiarCuaderno(ValueChangeEvent vce) {
        try {
            log.debug(user+" -> Inicio: cambiarCuaderno");

            if (vce == null || vce.getNewValue() == null) {
                setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", null);
                setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", null);
            } else {
                String cuadernoSel = (String) vce.getNewValue();
                setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", cuadernoSel);

                for (Folderfolders f : this.cuadernosSet) {
                    if (f.getFfolderguid().equals(cuadernoSel)) {
                        setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", f.getFfoldername());
                        break;
                    }
                }
            }
            log.debug(user+" -> Id Cuaderno: "+getElObjectFromBinding("#{bindings.idCuaderno.inputValue}"));
            Object cuaderno = getElObjectFromBinding("#{bindings.cuaderno.inputValue}");
            log.debug(user+" -> cuaderno: "+cuaderno);
            if(Objects.nonNull(cuaderno)){
                itCuaderno.setValue(cuaderno);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itCuaderno);
            }
        } catch (Exception e) {
            log.error("Exception cambiarCuaderno",e);
        }
        log.debug(user+" -> Fin: cambiarCuaderno");
    }
    
    /****
     * Fecha: 29Enero2024
     * Metodo deprecado por cambio en la funcionalidad, a partir de la fecha, el usuario y contraseña para
     * firmar el documento se leera desde archivo properties
     * ****/
    /*
    public void cambiarPassword(ValueChangeEvent vce) {
        try{
            String encodedPass = Base64.getEncoder().encodeToString(vce.getNewValue().toString().getBytes());
            log.debug(user+" -> encodedPass: "+encodedPass);
            setElObjectIntoBinding("#{bindings.password.inputValue}", encodedPass);
            Object newPassword = getElObjectFromBinding("#{bindings.password.inputValue}");
            log.debug(user+" -> newPassword: "+newPassword);
            
            Object taskId = getElObjectFromBinding("#{bindings.taskNumber.inputValue}");
            if(Objects.nonNull(taskId))
                setElObjectIntoBinding("#{bindings.taskNumber1.inputValue}",Integer.valueOf(taskId.toString()));
        }catch(Exception e){
            log.error(user+" -> cambiarPassword -> Exception: "+e.getMessage(), e);
        }
    }
    */

    public void setListaDependencias(Collection<SelectItem> listaDependencias) {
        this.listaDependencias = listaDependencias;
    }

    public Collection<SelectItem> getListaDependencias() {
        return listaDependencias;
    }

    public void setListaRangoEdadSI(List<SelectItem> listaRangoEdadSI) {
        this.listaRangoEdadSI = listaRangoEdadSI;
    }

    public List<SelectItem> getListaRangoEdadSI() {
        return listaRangoEdadSI;
    }

    public void setListaPoblacionVulnerableSI(List<SelectItem> listaPoblacionVulnerableSI) {
        this.listaPoblacionVulnerableSI = listaPoblacionVulnerableSI;
    }

    public List<SelectItem> getListaPoblacionVulnerableSI() {
        return listaPoblacionVulnerableSI;
    }

    public void setListaEtniaSI(List<SelectItem> listaEtniaSI) {
        this.listaEtniaSI = listaEtniaSI;
    }

    public List<SelectItem> getListaEtniaSI() {
        return listaEtniaSI;
    }

    public void setTramites(List<SelectItem> tramites) {
        this.tramites = tramites;
    }

    public List<SelectItem> getTramites() {
        return tramites;
    }

    public void setTiposDocumentales(List<SelectItem> tiposDocumentales) {
        this.tiposDocumentales = tiposDocumentales;
    }

    public List<SelectItem> getTiposDocumentales() {
        return tiposDocumentales;
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

    public void setRenderedSubseries(boolean renderedSubseries) {
        this.renderedSubseries = renderedSubseries;
    }

    public boolean isRenderedSubseries() {
        return renderedSubseries;
    }

    public void setRenderedCuadernos(boolean renderedCuadernos) {
        this.renderedCuadernos = renderedCuadernos;
    }

    public boolean isRenderedCuadernos() {
        return renderedCuadernos;
    }

    public void setTramitesSet(List<AnmTramiteTb> tramitesSet) {
        this.tramitesSet = tramitesSet;
    }

    public List<AnmTramiteTb> getTramitesSet() {
        return tramitesSet;
    }

    public void setTiposDocumentalesSet(List<AnmTipodtalseguntramite> tiposDocumentalesSet) {
        this.tiposDocumentalesSet = tiposDocumentalesSet;
    }

    public List<AnmTipodtalseguntramite> getTiposDocumentalesSet() {
        return tiposDocumentalesSet;
    }

    public void setCategoriasProcesoSet(Collection<Folderfolders> categoriasProcesoSet) {
        this.categoriasProcesoSet = categoriasProcesoSet;
    }

    public Collection<Folderfolders> getCategoriasProcesoSet() {
        return categoriasProcesoSet;
    }

    public void setProcesosSet(Collection<Folderfolders> procesosSet) {
        this.procesosSet = procesosSet;
    }

    public Collection<Folderfolders> getProcesosSet() {
        return procesosSet;
    }

    public void setSeriesSet(Collection<Folderfolders> seriesSet) {
        this.seriesSet = seriesSet;
    }

    public Collection<Folderfolders> getSeriesSet() {
        return seriesSet;
    }

    public void setSubseriesSet(Collection<Folderfolders> subseriesSet) {
        this.subseriesSet = subseriesSet;
    }

    public Collection<Folderfolders> getSubseriesSet() {
        return subseriesSet;
    }

    public void setCuadernosSet(Collection<Folderfolders> cuadernosSet) {
        this.cuadernosSet = cuadernosSet;
    }

    public Collection<Folderfolders> getCuadernosSet() {
        return cuadernosSet;
    }


    public void setItCuaderno(RichInputText itCuaderno) {
        this.itCuaderno = itCuaderno;
    }

    public RichInputText getItCuaderno() {
        return itCuaderno;
    }

    
}

