package co.gov.anm.jsfBean;

import co.gov.anm.cee.utils.ADFUtils;
import co.gov.anm.cee.utils.PropertiesKeys;
import co.gov.anm.cee.utils.PropertiesLoader;
import co.gov.anm.entity.AnmTipodtalseguntramite;
import co.gov.anm.entity.AnmTramiteTb;
import co.gov.anm.entity.AnmUnidadadministrativaTb;
import co.gov.anm.entity.Folderfolders;
import co.gov.anm.entity.SgdDepartamento;
import co.gov.anm.entity.SgdEtnia;
import co.gov.anm.entity.TipoSolicitante;
import co.gov.anm.entity.SgdMunicipio;
import co.gov.anm.entity.SgdOcupacion;
import co.gov.anm.entity.SgdPais;
import co.gov.anm.entity.SgdPoblacionVulnerable;
import co.gov.anm.entity.SgdRangoEdad;
import co.gov.anm.entity.SgdSubCategoria;
import co.gov.anm.entity.SgdTipoIdentificacion;
import co.gov.anm.entity.SgdTipoSolicitud;
import co.gov.anm.entity.SgdUsuario;
import co.gov.anm.java.Interesado;
import co.gov.anm.session.com.ComunicacionesEntSessionEJBLocal9;
import co.gov.anm.session.wcc.WCCSessionEJBLocal9;
import co.gov.anm.sgd.service.Comunicaciones;
import co.gov.anm.sgd.service.DependenciaRol;
import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.FechaVencimientoRequest;
import co.gov.anm.sgd.service.FechaVencimientoResponse;
import co.gov.anm.sgd.service.UsuarioResponse;
import co.gov.anm.sgd.service.WccResponse;

import co.gov.anm.sgd.util.SGDWebServiceLocator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.model.AutoSuggestUIHints;

import oracle.bpel.services.workflow.task.model.AttachmentTypeImpl;
import oracle.bpel.services.workflow.worklist.adf.DocMgmtBean;
import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

@ManagedBean(name = "CrearComunicacionEntElecHT_jsfbean")
@ViewScoped
public class CrearComunicacionEntElecHT_jsfbean {

    @EJB
    private WCCSessionEJBLocal9 comunicacionesBean;
    @EJB
    private ComunicacionesEntSessionEJBLocal9 comBean;

    private Logger log;
    private String dependenciaDestino;
    private Collection<AnmUnidadadministrativaTb> dependenciasSet;
    private Collection<SelectItem> listaDependencias;
    private Timestamp fechaActual;
    private String user;
    private String dependencia;
    private SgdUsuario sgdUsuario;
    private List<SgdUsuario> listUsuarios = new ArrayList<SgdUsuario>();
    private List<SelectItem> listUsuariosSI = new ArrayList<SelectItem>();
    private Interesado interesado = new Interesado();
    private String comReasignada = "U";
    private Boolean isPQRS = false;
    private String esPQRS = "S";
    private Collection<SgdTipoSolicitud> listaTipoSolicitud = new ArrayList<SgdTipoSolicitud>();
    private List<SelectItem> listTipoPQRSSI = new ArrayList<SelectItem>();
    private String tipoPQRS;
    private String requiereRespuesta = "S";
    private String requiereGestionCompartida = "S";
    private Long codDependencia;
    private AnmUnidadadministrativaTb dependenciaSelected = new AnmUnidadadministrativaTb();
    private Integer tiempoRespuestaPQRS;
    private Long tipoSolicitudSelected = 0L;
    private Timestamp fechaVencimiento;
    private List<SgdTipoIdentificacion> listaTipoIdentificacion = new ArrayList<SgdTipoIdentificacion>();
    private List<SelectItem> listaTipoIdentificacionSI = new ArrayList<SelectItem>();
    private List<SgdRangoEdad> listaRangoEdad = new ArrayList<SgdRangoEdad>();
    private List<SelectItem> listaRangoEdadSI = new ArrayList<SelectItem>();
    private List<SgdPoblacionVulnerable> listaPoblacionVulnerable = new ArrayList<SgdPoblacionVulnerable>();
    private List<SelectItem> listaPoblacionVulnerableSI = new ArrayList<SelectItem>();
    private List<SgdEtnia> listaEtnia = new ArrayList<SgdEtnia>();
    private List<SelectItem> listaEtniaSI = new ArrayList<SelectItem>();
    private List<SgdPais> listaPais = new ArrayList<SgdPais>();
    private List<SelectItem> listaPaisSI = new ArrayList<SelectItem>();
    private List<SgdDepartamento> listaDepartamento = new ArrayList<SgdDepartamento>();
    private List<SelectItem> listaDepartamentoSI = new ArrayList<SelectItem>();
    private List<SgdMunicipio> listaMunicipio = new ArrayList<SgdMunicipio>();
    private List<SelectItem> listaMunicipioSI = new ArrayList<SelectItem>();
    private Boolean marcaPqrs = false;
    private String marcaRequiereRespuesta = "N";
   // Atributos para controlar tipo solicitud
    private ArrayList<SelectItem> tiposSolicitantes = null;
    private Collection<TipoSolicitante> tiposSolicitanteSet = null;
    private int departamentoSelected;
    //jairo
    private List<SelectItem> tiposDocumentales = new ArrayList<>();
    private List<AnmTipodtalseguntramite> tiposDocumentalesSet = new ArrayList<>();
    private List<SelectItem> tramites = new ArrayList<>();
    private List<AnmTramiteTb> tramitesSet = new ArrayList<>();
    @SuppressWarnings("oracle.jdeveloper.java.boolean-constructor")
    private boolean tieneReferencia = new Boolean(false);

    //Taxonomia
    private final static String PARENT_TAX_NAME = "ANM"; //Directorio raiz de la taxonomia - libreria ANM
    
    /**
     * Variables para adjunto
     */
    private String filePath;
    private String fileUrl;
    private UploadedFile file;
    private byte[] fileContent;
    private String fileFormat;
    private String fileBase64;
    private String fileName;
    private InputStream fileInputStream;
    
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

    private boolean renderedSubseries;
    private boolean renderedCuadernos;

    //Comunicaciones proxy
    SGDWebServiceLocator locator = new SGDWebServiceLocator();
    Comunicaciones comunicaciones = locator.getComunicacionesProxy();

    //Expediente Minero proxy    
    ExpedienteMinero expedienteMinero = locator.getExpedienteMineroProxy();
    private RichInputText itPlaca;
    private RichSelectOneChoice socTipoPQRS;
    
    //message para pop-up - use --> validar forms
    String message = "";

	private ArrayList<SelectItem> ocupaciones = new ArrayList<>();
    private Collection<SgdOcupacion> ocupacionSet;
    
    //Tipo solicitud y subcategoria
    private Long idTpSolicitud;
    private ArrayList<SelectItem> tiposSolicitud = null;
    private Collection<SgdTipoSolicitud> tiposSolicitudSet = null;
    private Long idSubcategoria;
    private ArrayList<SelectItem> subcategorias = null;
    private Collection<SgdSubCategoria> subcategoriaSet = null;
    private RichInputText itCuaderno;


    public CrearComunicacionEntElecHT_jsfbean() {
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
    public void inicializar() throws Exception {
        log.info("Inicio metodo @PostConstruct : inicializar()");
        try {
            
            // capturas de marcaPqrs y Requiere respuesta
            Object isPrsObj = getElObjectFromBinding("#{bindings.isPQRS.inputValue}");
            if(isPrsObj != null){
                this.marcaPqrs = (Boolean)isPrsObj;
            }
            Object requiereRespuestaObj = getElObjectFromBinding("#{bindings.requiereRespuestaDO.inputValue}");
            if(requiereRespuestaObj != null){
                this.marcaRequiereRespuesta = requiereRespuestaObj.toString();
            }
            
            log.debug("se recibe isPQRS :"+isPrsObj);
           log.debug("se recibe requiereRespuestaDO :"+requiereRespuestaObj);
           // Listado de Dependencias
           this.dependenciasSet = this.comunicacionesBean.getAnmUnidadadministrativaTbFindAll();
           log.debug(user + " -> dependenciasSet size: " + dependenciasSet.size());
           this.listaDependencias = new ArrayList<SelectItem>(dependenciasSet.size());
           for (AnmUnidadadministrativaTb undAdmin : dependenciasSet) {
               this.listaDependencias.add(new SelectItem(undAdmin.getCodUnidadadministrativa(),
                                                         undAdmin.getCodUnidadadministrativa() + " - " +
                                                         undAdmin.getNombreunidadadministrativa()));
           }
           log.debug(user + " -> listaDependencias size: " + listaDependencias.size());
		   // Ocupaciones
           this.ocupacionSet = this.comBean.getOcupacionFindAll();
           log.debug(user + " -> ocupacionSet size: " + ocupacionSet.size());
           this.ocupaciones = new ArrayList<SelectItem>(ocupacionSet.size());
           for (SgdOcupacion o : ocupacionSet) {
               getOcupaciones().add(new SelectItem(o.getNombre(), o.getNombre()));
           }
           log.debug(user + " -> ocupaciones size: " + listaDependencias.size());
           //Buscar la dependencia del usuario loggeado
           if (!this.user.isEmpty()) {
               this.sgdUsuario = this.comBean.getSgdUsuarioById(user);
               log.debug(user + " -> Cod.Dependencia: " + sgdUsuario.getCodigoDependencia());
               //Cargar dependencia
               this.codDependencia = sgdUsuario.getCodigoDependencia().longValue();
               log.debug(user + " -> depend: " + codDependencia);
               //Setear codigo dependencia
               dependencia =
                   comunicacionesBean.getAnmUnidadadministrativaByCod(codDependencia).getNombreunidadadministrativa();
               log.debug(user + " -> la dependencia es : " + dependencia);

           }else {
               log.debug(user + " -> No se encontro el usuario.");
           }
       } catch (Exception e) {
            log.error("Excepcion en Postconstruct", e);
        }
        

        // Asignar al binding el valor de la codigo
        try {
            setElObjectIntoBinding("#{bindings.codigo.inputValue}",
                                   this.codDependencia); //codigo de comunicaciones-> dependencia es la dependencia destino
            log.debug(user+" -> codigo1: "+getElObjectFromBinding("#{bindings.codigo1.inputValue}"));
        } catch (Exception e) {
            log.error("No fue posible asignar la dependencia del usuario loggueado", e);
        }

        //Listado de Usuarios por Dependencia
        
        this.listUsuarios = this.comBean.getSgdUsuarioByCodDependencia(Long.valueOf(codDependencia));
        log.debug(user+" -> lista tamano listUsuarios : " + listUsuarios.size());
        for (SgdUsuario usuario : listUsuarios) {
            this.listUsuariosSI.add(new SelectItem(usuario, usuario.getNombreUsuario()));
        }
        log.debug(user+" -> lista tamano listUsuariosSI : " + listUsuariosSI.size());

        //Generacion de listado de tipos de solicitud (PQRS)
        log.info("Inicio generar listaTipoSolicitud");
        this.listaTipoSolicitud = this.comBean.getSgdTipoSolicitudFindAll();
        log.debug(user+" -> lista tamano listaTipoSolicitud : " + listaTipoSolicitud.size());
        for (SgdTipoSolicitud tpqrs : listaTipoSolicitud) {
            this.listTipoPQRSSI.add(new SelectItem(tpqrs.getIdTipoSolicitud().longValue(), tpqrs.getNombre()));
        }

        //llenar tramites
        this.tramitesSet = this.comunicacionesBean.getTramiteFindAll();
        this.tramites = new ArrayList<SelectItem>(this.tramitesSet.size());
        for (AnmTramiteTb f : tramitesSet) {
            this.tramites.add(new SelectItem(f.getIdtramite().intValue(), f.getTramite()));
        }
        log.debug(user+" -> Tramites cargados");

        log.info("Fin metodo @PostConstruct : inicializar()");

        try {
            log.debug(user+" -> Tipo de pqrs selected " + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
            log.debug(user+" -> Tipo de pqrs selected" + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}").getClass());
        } catch (Exception e) {
            log.debug(user+" -> error al imprimir el tipo de pqrs " + e);
        }

        //Configuracion de la taxonomia
        try {

            Collection<Folderfolders> taxonomiaParent =
                this.comunicacionesBean.getFolderFoldersFindByFolderName(PARENT_TAX_NAME);
            String parentGuid = null;
            if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
                log.error("Se debe configurar el nodo principal de la taxonomia 'ANM'");
                throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
            }
            for (Folderfolders f : taxonomiaParent) {
                parentGuid = f.getFfolderguid();
                break;
            }
            this.categoriasProcesoSet = this.comunicacionesBean.getFolderFoldersFindByParentGuid(parentGuid);
            this.categoriasProceso = new ArrayList<SelectItem>(categoriasProcesoSet.size());
            for (Folderfolders f : categoriasProcesoSet) {
                this.categoriasProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }

        } catch (Exception e) {
            log.error("Failed configuracion de la taxonomia" + e);
        }
        
        // Generacion de listado de tipos de solicitud
        tiposSolicitanteSet = this.comBean.getTipoSolicitanteFindAll();
        setTiposSolicitantes(new ArrayList<SelectItem>(tiposSolicitanteSet.size()));
        for (TipoSolicitante tp : tiposSolicitanteSet) {
            getTiposSolicitantes().add(new SelectItem(tp.getIdTipoSolicitante().intValue(), tp.getNombre()));
        }

        //Generacion de listado de tipos de identificacion
        try {
            log.info("user: " + user + " -> Inicio generar listaTipoIdentificacion");
            this.listaTipoIdentificacion = this.comBean.getSgdTipoIdentificacionFindAll();
            log.debug(user+" -> user: " + user + " -> listaTipoIdentificacion.size() : " + this.listaTipoIdentificacion.size());
            for (SgdTipoIdentificacion tidentificacion : this.listaTipoIdentificacion) {
                this.listaTipoIdentificacionSI.add(new SelectItem(tidentificacion.getCodigo(),
                                                                  tidentificacion.getCodigo() + " - " +
                                                                  tidentificacion.getNombre()));
            }
            ;
        } catch (Exception e) {
            log.error("No fue posible generar listaTipoIdentificacion", e);
        }

        //Generacion de listado rangos de edad
        try {
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
            log.info("Inicio generar listaEtnia");
            this.listaEtnia = this.comBean.getSgdEtniaFindAll();
            log.debug(user+" -> lista tamano listaEtnia : " + listaEtnia.size());
            for (SgdEtnia etnia : listaEtnia) {
                this.listaEtniaSI.add(new SelectItem(etnia.getIdEtnia(), etnia.getNombre()));
            }

        } catch (Exception e) {
            log.error("Error generando listaEtnia " + e);
        }

        //Generacion de listado pais
        try {
            log.info("Inicio generar listaPais");
            this.listaPais = this.comBean.getSgdPaisFindAll();
            log.debug(user+" -> lista tamano listaPais : " + listaPais.size());
            for (SgdPais pais : listaPais) {
                this.listaPaisSI.add(new SelectItem(pais.getId(), pais.getNombre()));
            }
        } catch (Exception e) {
            log.error("Error generando listaPais " + e);
        }

        //Generacion de listado departamento
        try {
            log.info("Inicio generar listaDepartamento");
            this.listaDepartamento = this.comBean.getSgdDepartamentoFindAll();
            log.debug(user+" -> lista tamano listaDepartamento : " + listaDepartamento.size());
            for (SgdDepartamento departamento : listaDepartamento) {
                this.listaDepartamentoSI.add(new SelectItem(departamento.getIdDepartamento(),
                                                            departamento.getNombre()));
            }
        } catch (Exception e) {
            log.error("Error generando listaDepartamento " + e);
        }

        //Generacion de listado municipio-ciudad
        try {
            log.info("Inicio generar listaMunicipio");
            this.listaMunicipio = this.comBean.getSgdMunicipioFindAll();
            log.debug(user+" -> lista tamano listaMunicipio : " + listaMunicipio.size());
            for (SgdMunicipio municipio : listaMunicipio) {
                this.listaMunicipioSI.add(new SelectItem(municipio.getIdMunicipio(), municipio.getNombre()));
            }
        } catch (Exception e) {
            log.error("Error generando listaDepartamento " + e);
        }


        //JGUTIERREZ: Cuando ya se selecciono la taxonomia
        this.precargarTaxonomia();
        
        if(getElObjectFromBinding("#{bindings.idTramite.inputValue}")!=null){
            Integer tramiteSelected = (Integer)getElObjectFromBinding("#{bindings.idTramite.inputValue}");
            this.cargarTiposDocumentales(tramiteSelected);
        }
        
        try{
            tiposSolicitudSet = comBean.getSgdTipoSolicitudFindAll();
            tiposSolicitud = new ArrayList<SelectItem>(tiposSolicitudSet.size());
            for (SgdTipoSolicitud tp : tiposSolicitudSet) {
                tiposSolicitud.add(new SelectItem(tp.getIdTipoSolicitud(), tp.getNombre()));
            }
        }catch(Exception e){
            log.error("Error cargando Tipos de solicitud. Error: "+e.getMessage(), e);
        }

    }
    
    
    /*************************************
     * METODOS INTERACCIONES
     * **********************************/
    public void cambiarTramite(ValueChangeEvent evt) {

        this.tiposDocumentales.clear();
        log.debug(user+" -> Inicio: cambiarTramite(): " + evt.getNewValue());
        log.debug(user+" -> Inicio: cambiarTramite(): " + evt.getNewValue().getClass());

        Integer tramiteSelected = Integer.parseInt(evt.getNewValue().toString());

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
            this.comunicacionesBean.getTipoDocumentalTramiteFindByTramite(BigDecimal.valueOf(tramiteSelected));
        this.tiposDocumentales = new ArrayList<SelectItem>();
        for (AnmTipodtalseguntramite f : tiposDocumentalesSet) {
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().intValue(), f.getNombretipodocumental()));
        }        
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

    public void cambiarTipoDocumento(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio: cambiarTipoDocumento():" + evt.getNewValue());
        log.debug(user+" -> Inicio: cambiarTipoDocumento():" + evt.getNewValue().getClass());

        Integer tipoDocSele = (Integer) evt.getNewValue();
        for (AnmTipodtalseguntramite t : this.tiposDocumentalesSet) {
            if (tipoDocSele.equals(t.getIdtipodtal().intValue())) {
                setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                break;
            }
        }
    }

    //Calcular el usuario Jefe de la dependencia
    public void cambiarDependencia(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio cambiarDependencia(): " + evt.getNewValue());
        log.debug(user+" -> Inicio cambiarDependencia(): " + evt.getNewValue().getClass());

        Long dependenciaSelect = Long.parseLong(evt.getNewValue().toString());
        log.debug(user+" ->  dependenciaSelect: " + dependenciaSelect);

        try {
            setElObjectIntoBinding("#{bindings.codigo1.inputValue}",
                                   dependenciaSelect); //codigo de comunicaciones-> dependencia es la dependencia destino
            log.debug(user+" -> codigo1: "+getElObjectFromBinding("#{bindings.codigo1.inputValue}"));
        } catch (Exception e) {
            log.error(user+" -> Exception cambiarDependencia", e);
        }
        //
        //        //Obtener nombre de la dependencia
        //        for (AnmUnidadAdministrativaTb reg : lstAnmUnidad) {
        //            //log.debug(user+" -> codDepen: " +codDepen+" -- reg.getCodUnidadAdministrativa: " +reg.getCodUnidadAdministrativa() );
        //            log.debug(user+" -> comparacion: " + reg.getCodUnidadAdministrativa()
        //                                           .toString()
        //                                           .equals(codDepen.toString()));
        //            if (reg.getCodUnidadAdministrativa()
        //                   .toString()
        //                   .equals(codDepen.toString())) {
        //                setElObjectIntoBinding("#{bindings.nombre2.inputValue}", reg.getNombreUnidadadministrativa());
        //                break;
        //            }
        //        }
        //        log.debug(user+" -> nombre2: " + getElObjectFromBindings("#{bindings.nombre2.inputValue}"));


        DependenciaRol dprol = new DependenciaRol();
        dprol.setIdRol(2L);
        dprol.setCodigoDependencia(dependenciaSelect);

        String usuarioJefe = null;
        UsuarioResponse response = comunicaciones.buscarUsuariosPorDependenciaRol(dprol);
        if (response.getStatusCode().equals("0")) {
            usuarioJefe = response.getUsuarios()
                                  .get(0)
                                  .getIdUsuario();
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_WARN,
                           "La dependencia seleccionada no tiene jefe asignado, por favor informe a TI.");
        }
        
        log.debug(user+" -> usuarioJefe: " + usuarioJefe);
        if (usuarioJefe != null) {
            try {
                setElObjectIntoBinding("#{bindings.jefeAsignadoDO.inputValue}", usuarioJefe);
                log.debug(user+" -> jefeAsignadoDO: " + getElObjectFromBinding("#{bindings.jefeAsignadoDO.inputValue}"));
            } catch (Exception e) {
                log.error(user+" -> Exception cambiarDependencia usuarioJefe", e);
            }
        }
    }
    


    //Calcular el usuario Jefe de la dependencia
    public void cambiarUsuario(ValueChangeEvent evt) {

        log.info(this.user + " | BEGIN | cambiarUsuario()");
        log.debug(user+" ->  cambiarUsuario(): " + evt.getNewValue());
        log.debug(user+" ->  cambiarUsuario(): " + evt.getNewValue().getClass());

        try {

            SgdUsuario usuarioSelected = (SgdUsuario) evt.getNewValue();

            // set id usuario
            setElObjectIntoBinding("#{bindings.usuario.inputValue}", usuarioSelected.getIdUsuario());
            // set dependencia usuario
            setElObjectIntoBinding("#{bindings.codigo1.inputValue}", usuarioSelected.getCodigoDependencia());
            // set nombre usuario
            setElObjectIntoBinding("#{bindings.nombreAD.inputValue}", usuarioSelected.getNombreUsuario());
            // set email usuario
            setElObjectIntoBinding("#{bindings.emailAD.inputValue}", usuarioSelected.getEmail());

       } catch (Exception e) {
            log.error(this.user+" | Exception | cambiarUsuario()",e);
        }

        log.info(this.user + " | END | cambiarUsuario()");
    }

    public void cambiarUsuarioGestionCompartida(ValueChangeEvent evt) {

        log.info(this.user + " | BEGIN | cambiarUsuarioGestionCompartida()");
        log.debug(user+" ->  | cambiarUsuarioGestionCompartida() | value : " + evt.getNewValue());
        log.debug(user+" ->  | cambiarUsuarioGestionCompartida() | class : " + evt.getNewValue().getClass());

        try {

            SgdUsuario usuarioSelected = (SgdUsuario) evt.getNewValue();

            // set id usuario
            setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuarioSelected.getIdUsuario());
            // set dependencia usuario
            setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", usuarioSelected.getCodigoDependencia());
            // set nombre usuario
            setElObjectIntoBinding("#{bindings.nombreUsuario.inputValue}", usuarioSelected.getNombreUsuario());
            // set email usuario
            setElObjectIntoBinding("#{bindings.emailUsuario.inputValue}", usuarioSelected.getEmail());

        } catch (Exception e) {
            log.error(this.user + " | Exception | cambiarUsuarioGestionCompartida()", e);
        }

        log.info(this.user + " | END | cambiarUsuarioGestionCompartida()");
    }


    //pop-up de mensaje al usuario
    protected void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        StringBuilder mensajeStringBuilder = new StringBuilder();
        mensajeStringBuilder.append(mensaje);
        try {
            FacesMessage message = new FacesMessage(severity, mensajeStringBuilder.toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            log.error(user + " -> Exception mostrarMensaje", e);
        }
    }

    public void writeDependenciaSelected(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio: writeDependenciaSelected(): " + evt.getNewValue());
        log.debug(user+" -> Inicio: writeDependenciaSelected(): " + evt.getNewValue().getClass());
        
        Long dependenciaSelect = Long.parseLong(evt.getNewValue().toString());
        
        DependenciaRol dprol = new DependenciaRol();
        dprol.setIdRol(2L);
        dprol.setCodigoDependencia(dependenciaSelect);

        String usuarioJefe = null;
        UsuarioResponse response = comunicaciones.buscarUsuariosPorDependenciaRol(dprol);
        if (response.getStatusCode().equals("0")) {
            usuarioJefe = response.getUsuarios()
                                  .get(0)
                                  .getIdUsuario();
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_WARN,
                           "La dependencia seleccionada no tiene jefe asignado, por favor informe a TI.");
        }
        log.debug(user+" -> usuarioJefe: " + usuarioJefe);
        

        
        log.debug(user+" ->  cambiarDependencia() dependencia: " + dependenciaSelect);

        try {
            setElObjectIntoBinding("#{bindings.codDepGestionCompartida.inputValue}",
                                   dependenciaSelect); //codigo de comunicaciones-> dependencia es la dependencia destino
            log.debug(getElObjectFromBinding("#{bindings.codDepGestionCompartida.inputValue}"));
        } catch (Exception e) {
            log.error("Me daï¿½ï¿½", e);
        }
    }


    //Cambiar tipo PQRS
    // mETODO ASIGNA EL TIEMPO DE RESPUESTA, con base en pqrs
    //asigna requiereRespuesta="S"
    // asigna el string tipo PQRS
    //asigna la recha de vencimiento
    public void cambiarTipoPQRS(ValueChangeEvent evt) {
        try {

            if (evt == null || evt.getNewValue() == null) {

                log.info("cambiarTipoPQRS() --> null");

            } else {

                log.debug(user+" -> Inicio: cambiarTipoPQRS():" + evt.getNewValue());
                log.debug(user+" -> Inicio: cambiarTipoPQRS():" + evt.getNewValue().getClass());

                //get el tipo de PQRS
                tipoSolicitudSelected = (Long) evt.getNewValue();
                log.debug(user+" -> cambiarTipoPQRS() --> tipoSolicitudSelected " + tipoSolicitudSelected);

                //setear el tipo de PQRS en el binding
                setElObjectIntoBinding("#{bindings.idTipoPQRS.inputValue}", tipoSolicitudSelected);
                log.debug(user+" -> Tipo de pqrs selected " + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));

                // setear el tiempo de respuesta segun el tipo de PQRS
                tiempoRespuestaPQRS = this.comBean
                                          .getTipoSolicitudById(tipoSolicitudSelected)
                                          .getTiempoRespuesta();
                log.debug(user+" ->  cambiarTipoPQRS() tiempo: " + tiempoRespuestaPQRS);
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", tiempoRespuestaPQRS);
                
                // setear el tipo de pqrs (string)
                this.tipoPQRS = this.comBean.getTipoSolicitudById(tipoSolicitudSelected).getNombre();
                log.debug(this.user+" | cambiarTipoPQRS() | tipoPQRS: " + this.tipoPQRS);
                setElObjectIntoBinding("#{bindings.tipoPQRSDO.inputValue}", this.tipoPQRS);
                
                //calcula fecha de vencimiento y setea el valor en el binding
               this.calcularFechaVencimiento(tiempoRespuestaPQRS);

            }

        } catch (Exception e) {
            log.error("No fue posible encontrar el tiempo de respuesta para este tipo de solicitud", e);
        }
    }
    
    
    public void cambiarTiempoRespuesta(ValueChangeEvent evt) {
        try {
            if (evt == null || evt.getNewValue() == null) {
                log.info(this.user+" | cambiarTiempoRespuesta() --> null");
            } else {
                log.debug(user+" -> Inicio: cambiarTiempoRespuesta():" + evt.getNewValue());
                log.debug(user+" -> Inicio: cambiarTiempoRespuesta():" + evt.getNewValue().getClass());

                this.tiempoRespuestaPQRS = ((BigInteger) getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}")).intValue();
                log.debug(this.user+" | cambiarTiempoRespuesta() --> "+this.tiempoRespuestaPQRS);
                
                //calcula fecha de vencimiento y setea el valor en el binding
               this.calcularFechaVencimiento(this.tiempoRespuestaPQRS);
            }

        } catch (Exception e) {
            log.error(this.user+" | cambiarTiempoRespuesta()", e);
        }
    }


    protected void calcularFechaVencimiento(Integer dias) {

        log.info(this.user + " | Inicio caculateFechaVencimiento()");
        try {
            // Calcular la fecha de vencimiento service proxy
            log.debug(user + " -> Init calcular fecha vencimiento");
            FechaVencimientoRequest fvrequest = new FechaVencimientoRequest();
            fvrequest.setDiasRespuesta(dias);
            log.debug(user + " -> Init cast fecha");
            Timestamp ts = (Timestamp) getElObjectFromBinding("#{bindings.fecRadicacion.inputValue}");
            log.debug(user + " -> cast fecha - timestamp " + ts.toString());
            Date date = new Date(ts.getTime());
            log.debug(user + " -> cast fecha - date " + date.toString());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(date);
            log.debug(user + " -> cast fecha gcalendar " + c.getTime().toString());
            XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            log.debug(user + " -> cast fecha xmlgcalendar " + cal.toString());
            fvrequest.setFechaRadicacion(cal);
            log.debug(user + " -> fecha de radicacion " + fvrequest.getFechaRadicacion().toString());
            FechaVencimientoResponse fvresponse = comunicaciones.calcularFechaVencimiento(fvrequest);
            XMLGregorianCalendar calfv = fvresponse.getFechaVencimiento();
            log.debug(user + " -> Fecha de vencimiento " + calfv.toString());
            log.debug(user + " -> end calcular fecha vencimiento");
            log.debug(user + " -> cast to timestamp fecha vencimiento");
            Timestamp tsfv = new Timestamp(calfv.toGregorianCalendar().getTime().getTime());

            //Setear en el binding la fecha de vencimiento del tipo timeStamp
            setElObjectIntoBinding("#{bindings.fechaVencimiento.inputValue}", tsfv);
            log.debug(user + " -> set fecha vencimiento into binding: " + tsfv);
            log.debug(user + " -> get fecha vencimiento into binding :" +
                      getElObjectFromBinding("#{bindings.fechaVencimiento.inputValue}"));

        } catch (Exception e) {
            log.error(this.user + "| caculateFechaVencimiento()", e);
        }
        log.info(this.user + " | Fin caculateFechaVencimiento()");
    }



    public void cambiarIsPQRS(ValueChangeEvent evt) {
        log.debug(user+" -> Inicio: cambiarIsPQRS():" + evt.getNewValue());
        try {
            Boolean ispqrs = (Boolean) evt.getNewValue();
            log.debug(user+" -> ispqrs: " + ispqrs);
            setElObjectIntoBinding("#{bindings.isPQRS.inputValue}", ispqrs);
            if (ispqrs) {
                setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", "S");
                setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "S");
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 10);
                this.marcaPqrs = true;
            } else {
                setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", "N");
                setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "N");
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 0);
                this.marcaPqrs = false;
            }
            log.debug(user+" -> socTipoPQRS: " + socTipoPQRS.getValue());
            setElObjectIntoBinding("#{bindings.idTipoPQRS.inputValue}", 0);
            log.debug(user+" -> idTipoPQRS: " + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
            socTipoPQRS.setValue(0);
            log.debug(user+" -> socTipoPQRS: " + socTipoPQRS.getValue());
            log.debug(user+" -> idTipoPQRS:" + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
            log.debug(user+" -> requiereRespuestaDO: " + getElObjectFromBinding("#{bindings.requiereRespuestaDO.inputValue}"));
            //socTipoPQRS.resetValue(); //limpia el valor del componente
            //AdfFacesContext.getCurrentInstance().addPartialTarget(socTipoPQRS);
            
        } catch (Exception e) {
            log.error("cambiarIsPQRS() ", e);
        } catch (Throwable e) {
            log.error("cambiarIsPQRS() Throwable error ", e);
        }
    }

    // Cambiar requiere respuesta
    public void cambiarRequiereRespuesta(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio: cambiarRequiereRespuesta():" + evt.getNewValue());
        try {
            String requiereRespuesta = (String) evt.getNewValue();
            this.marcaRequiereRespuesta = requiereRespuesta;
            if (requiereRespuesta.equals("S")) {
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 10);
                this.tiempoRespuestaPQRS = ((BigInteger) getElObjectFromBinding("#{bindings.tiempoRespuesta.inputValue}")).intValue();
                log.debug(this.user+" | cambiarRequiereRespuesta() --> tiempoRespuestaPQRS : "+this.tiempoRespuestaPQRS);                
                //calcula fecha de vencimiento y setea el valor en el binding
                this.calcularFechaVencimiento(this.tiempoRespuestaPQRS);
                
            } else {
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", 0);
                setElObjectIntoBinding("#{bindings.fechaVencimiento.inputValue}", null);
                log.debug(this.user+" | cambiarRequiereRespuesta() --> tiempoRespuesta=0; fechaVencimiento=null");
            }
        } catch (Exception e) {
            // TODO: Add catch code
            log.error("cambiarRequiereRespuesta() ", e);
        } catch (Throwable e) {
            // TODO: Add catch code
            log.error("cambiarRequiereRespuesta() Throwable error ", e);
        }
    }

    
    private void precargarTaxonomia ( ){
        log.debug(user+" -> precargarTaxonomia() - init");
        String idCatProceso = null;
        String idProceso = null;
        String idSerie = null;
        String idSubSerie = null;
        Integer idTramite = null;
        ValueChangeEvent ve = null;
        if ( getElObjectFromBinding("#{bindings.idCatProceso.inputValue}")!=null ){
            idCatProceso = (String)getElObjectFromBinding("#{bindings.idCatProceso.inputValue}");
            log.debug(user+" -> precargarTaxonomia() - idCatProceso: "+idCatProceso);
            cargarProcesos(idCatProceso);
            if ( getElObjectFromBinding("#{bindings.idProceso.inputValue}")!=null ){                
                idProceso = (String)getElObjectFromBinding("#{bindings.idProceso.inputValue}");
                log.debug(user+" -> precargarTaxonomia() - idProceso: "+idProceso);
                cargarSeries(idProceso);
                if ( getElObjectFromBinding("#{bindings.idSerie.inputValue}")!=null ){
                    idSerie = (String)getElObjectFromBinding("#{bindings.idSerie.inputValue}");
                    log.debug(user+" -> precargarTaxonomia() - idSerie: "+idSerie);
                    cargarSubseries(idSerie);
                    if ( getElObjectFromBinding("#{bindings.idSubSerie.inputValue}")!=null ){
                        idSubSerie = (String)getElObjectFromBinding("#{bindings.idSubSerie.inputValue}");
                        log.debug(user+" -> precargarTaxonomia() - idSubSerie: "+idSubSerie);
                        cargarCuadernos(idSubSerie);
                    }
                }
            }
        }
        
        log.debug(user+" -> precargarTaxonomia() - end");
        
    }

    // Taxonomia metodo 1
    public void cambiarCategoriaProceso(ValueChangeEvent evt) {

        log.debug(user+" -> inicio cambiarCategoriaProceso(): " + evt.getNewValue());

        String catProcesoSel = (String) evt.getNewValue();
        cargarProcesos(catProcesoSel);
        this.cambiarProceso(null);
        log.debug(user+" -> Fin: cambiarCategoriaProceso()");
    }


    private void cargarProcesos ( String catProcesoSel ){
        this.procesosSet = this.comunicacionesBean.getFolderFoldersFindByParentGuid(catProcesoSel);
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

    // Taxonomia metodo 2
    public void cambiarProceso(ValueChangeEvent evt) {

        log.debug(user+" -> inicio cambiarProceso(): " +
                  (evt != null && evt.getNewValue() != null ? evt.getNewValue() : "vacio"));

        if (evt == null || evt.getNewValue() == null) {
            this.series = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folderfolders>(0);
            setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);
        } else {
            String idProceso = (String) evt.getNewValue();
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

        this.seriesSet = this.comunicacionesBean.getFolderFoldersFindByParentGuid(idProceso);
        this.series = new ArrayList<SelectItem>(this.seriesSet.size());

        log.debug(user+" -> cargarSeries(): Encontro: " + this.seriesSet.size() + " Series");

        for (Folderfolders f : seriesSet) {
            this.series.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }
    }
    

    //taxonomia metodo 3
    public void cambiarSerie(ValueChangeEvent evt) {
        log.debug(user+" -> Inicio cambiarSerie()");
        if (evt == null || evt.getNewValue() == null) {
            this.subseries = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<Folderfolders>(0);
            setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
            setElObjectIntoBinding("#{bindings.serie.inputValue}", null);
        } else {
            String idSerie = (String) evt.getNewValue();
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

        this.subseriesSet = this.comunicacionesBean.getFolderFoldersFindByParentGuid(idSerie);
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
            log.debug(user+" -> Sï¿½ hay subseries documentales para esta serie");

        }
    }

    //Taxonomia metodo 4
    public void cambiarSubSerie(ValueChangeEvent evt) {
        try {
           log.debug(user+" -> Inicio: cambiarSubSerie()");

           if (evt == null || evt.getNewValue() == null) {
               this.cuadernos = new ArrayList<SelectItem>(0);
               this.cuadernosSet = new ArrayList<Folderfolders>(0);
               setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
               setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);
           } else {
               String idSubSerie = (String) evt.getNewValue();
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
               /*log.info("Valida si es PQRS  codigoSerie "+ getElObjectFromBinding("#{bindings.codigoSerie.inputValue}"));
               
               if ( getElObjectFromBinding("#{bindings.codigoSerie.inputValue}").equals("SD47")){
                    setElObjectIntoBinding("#{bindings.isPQRS.inputValue}", true);
                   setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", "S");
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", 'S');
                   
                    log.info("Valida si es PQRS  isPQRS "+ getElObjectFromBinding("#{bindings.isPQRS.inputValue}"));
               }else {
                    //En caso de que no sea tipificada se asigna como el usuario la marco
                    setElObjectIntoBinding("#{bindings.isPQRS.inputValue}", this.marcaPqrs);
                    setElObjectIntoBinding("#{bindings.esPQRSDO.inputValue}", this.marcaPqrs? "S" : "N");
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", this.marcaRequiereRespuesta);
                }*/
           }
           this.cambiarCuaderno(null);
       } catch (Exception e) {
            log.error("Exception cambiarSubSerie",e);
        }
    }

    private void cargarCuadernos ( String idSubSerie ){

        this.cuadernosSet = this.comunicacionesBean.getFolderFoldersFindByParentGuid(idSubSerie);
        this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());

        log.debug(user+" -> cambiarSubSerie(): Encontro: " + this.cuadernos.size() + " cuadernos");

        for (Folderfolders f : cuadernosSet) {
            this.cuadernos.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }

        //validacion para pintar el selecOneChoice de cuaderno (expediente)
        if (this.cuadernos.isEmpty()) {
            this.renderedCuadernos = false;
            log.debug(user+" -> No hay subseries documentales para esta serie");
            ;
        } else {
            this.renderedCuadernos = true;
            log.debug(user+" -> Sï¿½ hay subseries documentales para esta serie");
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

    /****
     * Fecha: 15Sept2024
     * Se actualiza metodo para soportar la categorizacion desde la actividad de usuario
     * 'Asignar Usuario Competente' por desactivacion de la actividad 'Categorizar comunicacion'
     * ****/
    public void cambiarCuaderno(ValueChangeEvent evt) {
        try {
            log.debug(user+" -> Inicio: cambiarCuaderno()");

            if (evt == null || evt.getNewValue() == null) {
                setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", null);
                setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", null);
            } else {
                String cuadernoSel = (String) evt.getNewValue();
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
    
    /**
     * Llena el combo de departamentos y municipios de acuerdo al pais
     * @param vce
     */
    public void cambiarPais(ValueChangeEvent vce) {
        log.info("BEGIN cambiarPais");
        try {
            //Capturar codigo pais seleccionado y validar si es Colombia (52)
            log.debug(user + " -> Codigo pais: " + vce.getNewValue().toString());
            setElObjectIntoBinding("#{bindings.idPais.inputValue}", vce.getNewValue());
            listaDepartamentoSI.clear();
            listaMunicipioSI.clear();

            if (vce.getNewValue().toString().equals("52")) { //Codigo Colombia
                //Generacion de listado municipio-ciudad
                try {
                    log.info("Inicio generar listaMunicipio");
                    this.listaMunicipio = this.comBean.getSgdMunicipioFindAll();
                    log.debug(user+" -> lista tamano listaMunicipio : " + listaMunicipio.size());
                    for (SgdMunicipio municipio : listaMunicipio) {
                        this.listaMunicipioSI.add(new SelectItem(municipio.getIdMunicipio(), municipio.getNombre()));
                    }
                } catch (Exception e) {
                    log.error("Error generando listaDepartamento " + e);
                }

            }
            log.debug(user + " -> lstDepartamento size: " + listaMunicipioSI.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarPais", e);
        }
        log.info("END cambiarPais");
    }

    // Cambiar la selecciï¿½n de municipios
    public void cambiarDepartamento(ValueChangeEvent evt) {
        log.debug(user+" -> Inicio: cambiarDepartamento()");
        log.debug(user+" -> value " + evt.getNewValue().toString());
        log.debug(user+" -> class " + evt.getNewValue().getClass());
        this.listaMunicipio = new ArrayList<SgdMunicipio>(0);
        this.listaMunicipioSI = new ArrayList<SelectItem>(0);

        try {

            if (evt == null || evt.getNewValue() == null) {
                this.listaMunicipio = new ArrayList<SgdMunicipio>(0);
                this.listaMunicipioSI = new ArrayList<SelectItem>(0);
                setElObjectIntoBinding("#{bindings.idMunicipio.inputValue}", null);
            } else {

                this.departamentoSelected = Integer.parseInt(evt.getNewValue().toString());
                setElObjectIntoBinding("#{bindings.idDepartamento.inputValue}", departamentoSelected);

                this.listaMunicipio = this.comBean.getMunicipioByDepartamento(departamentoSelected);
                log.debug(user+" -> lista tamano listaMunicipio : " + listaMunicipio.size());
                for (SgdMunicipio municipio : listaMunicipio) {
                    this.listaMunicipioSI.add(new SelectItem(municipio.getIdMunicipio(), municipio.getNombre()));
                }
            }
        } catch (Exception e) {
            log.error("Error generando " + e);
        }
    }


    public void validarForm(ActionEvent action) {
        
 
        log.info("Inicio validarForm()");
        try {
            uploadAdjunto();
            if (!validarAttachment()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "Debe adjuntar al menos un archivo (Documento principal)");
                return;
            }else if (!validarTipoPQRS()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el Tipo de PQRS");
                return;
            } else if (!validarExpMinero()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Debe ingresar un Expediente Minero, de lo contrario desmarque la casilla");
                return;
            }
            
            
            log.debug(user+" -> End success validarForm()");
            //Si las validaciones son exitosas continuar flujo
            setOperation(action);
        } catch (Exception e) {
            log.error("Error validarForm() ", e);
        }
    }
    
    
    public void validarFormAsignarUsuario(ActionEvent action) {
        log.info(this.user+" | Inicio validarFormAsignarUsuario()");
        try {
            if (!validarDestinoSeleccionado()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, this.message);
            } else {
                setOperation(action);
                log.debug(this.user+" | End success validarFormAsignarUsuario()");
            }
        } catch (Exception e) {
            log.error(this.user+" | validarFormAsignarUsuario() ", e);
        }
    }

    public void reasignarActionListener(ActionEvent action){
        setOperation(action);
    }

    public void validarFormGestionarComunicacion(ActionEvent action) {
        if (!validarDatosCierreREQ04()) {
            javax.faces.context.FacesContext.getCurrentInstance().validationFailed();
            javax.faces.context.FacesContext.getCurrentInstance().renderResponse();

            throw new javax.faces.event.AbortProcessingException(
                "Datos de cierre REQ04 inválidos");
        }
        
        log.info("Inicio validarFormGestionarComunicacion()");
        try {
            if (!validarTipoPQRS()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el Tipo de PQRS");
            } else if (!validarExpMinero()) {                
                log.debug(user+" ->  | validarFormGestionarComunicacion | expminero true, placa false");
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Debe ingresar un Expediente Minero, de lo contrario desmarque la casilla");
            }else if ( !validarTaxonomia() ) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Debe seleccionar una Taxonomï¿½a valida incluyendo una serie documental");
            }
            else {
                setOperation(action);
                log.debug(user+" -> End success validarFormGestionarComunicacion()");
            }
        } catch (Exception e) {
            log.error("Error validarFormGestionarComunicacion() ", e);
        }
    }

    //Este metodo es llamaddo para validar el cambio de la placa minera
    protected boolean validarTipoPQRS() {
        log.info("Init validarTipoPQRS()");
        try {
            
            if ((Boolean) getElObjectFromBinding("#{bindings.isPQRS.inputValue}")) {
                log.info("Enter if isPQRS true");
                log.debug(user+" -> idTipoPQRS: "+getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
                if(getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}")!=null){
                    if ((Long) getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}") == 0L) {
                        log.debug(user+" -> PQRS marcado y tipo null");
                        return false;
                    } else {
                        log.debug(user+" -> PQRS marcado y con tipo");
                        return true;
                    }
                }else{
                    
                    socTipoPQRS.setValue(0);
                    log.debug(user+" -> PQRS marcado y tipo null");
                    return false;
                }
                
            } else {
                log.debug(user+" -> PQRS no marcado. Contiue normal");
                return true;
            }
        } catch (Exception e) {
            log.error("validarTipoPQRS() ", e);
            return false;
        }
    }
    
    
    private boolean validarTaxonomia() {
        log.debug("Init validarTaxonomia()");
        Boolean isTitulo = (Boolean) getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}");
        log.info("isTitulo: "+isTitulo);
        if (isTitulo) {
            return true;
        }
        String catProceso = (String)getElObjectFromBinding("#{bindings.idCatProceso.inputValue}");
        String proceso = (String)getElObjectFromBinding("#{bindings.idProceso.inputValue}");
        String serie = (String)getElObjectFromBinding("#{bindings.idSerie.inputValue}");
        log.info("catProceso: "+catProceso);
        log.info("proceso: "+proceso);
        log.info("serie: "+serie);
        
        if ( serie==null||serie.trim().equals("") ){
            log.error("Error de taxonomia seleccionada, debe elegir hasta el nivel de serie documental");
            return false;
        }
        return true;
        /*try {
            if (isPqrs) {
                return true;
            }
                log.info("Enter if isPQRS true");
                log.debug(user+" -> idTipoPQRS: "+getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
                if(getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}")!=null){
                    if ((Long) getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}") == 0L) {
                        log.debug(user+" -> PQRS marcado y tipo null");
                        return false;
                    } else {
                        log.debug(user+" -> PQRS marcado y con tipo");
                        return true;
                    }
                }else{
                    socTipoPQRS.setValue(0);
                    log.debug(user+" -> PQRS marcado y tipo null");
                    return false;
                }
                
            } else {
                log.debug(user+" -> PQRS no marcado. Contiue normal");
                return true;
            }
        } catch (Exception e) {
            log.error("validarTipoPQRS() ", e);
            return false;
        }*/
    }


    protected boolean validarAttachment() {
        log.info("Inicio validarAttachment()");
        try {
            log.debug(user+" -> path del attachement: " + this.filePath);
            if (this.filePath == null || this.filePath.isEmpty()) {
                return false;
            } else {

                return true;
            }
        } catch (Exception e) {
            log.error("Error validarAttachment() " + e);
            return false;
        }
    }
    
    //metodo para validar que se seleccione un usuario o una dependencia segun el radiobutton
    //formulario de asignar usuario competente
    protected boolean validarDestinoSeleccionado() {
        log.info(this.user+" | Inicio validarDestinoSeleccionado()");
        String destino = "";
        try {
            destino = getElObjectFromBinding("#{bindings.comunicacionReasignadaDO.inputValue}").toString();
            log.debug(this.user+" | bindings.comunicacionReasignadaDO.inputValue : "+destino);
            
            if (destino.equals("U")) {
                if (getElObjectFromBinding("#{bindings.usuario.inputValue}") == null) {
                    this.message =  "Debe seleccionar un usuario de destino";
                    log.debug(user+" -> bindings.usuario.inputValue : null");
                    return false;
                }else{
                    log.debug(this.user+" | bindings.usuario.inputValue : "+getElObjectFromBinding("#{bindings.usuario.inputValue}"));
                    return true;
                    }
            }else{
                    if (getElObjectFromBinding("#{bindings.jefeAsignadoDO.inputValue}") == null) {
                        this.message =  "Debe seleccionar una dependencia de destino";
                        log.debug(user+" -> bindings.jefeAsignadoDO.inputValue : null");
                        return false;
                    }else{
                        log.debug(this.user+" | bindings.jefeAsignadoDO.inputValue : "+getElObjectFromBinding("#{bindings.jefeAsignadoDO.inputValue}"));
                        return true;
                        }                
                }                                
        } catch (Exception e) {
            log.error(this.user+" | validarDestinoSeleccionado() ", e);
            return false;
        }
    }


    /**
     *Mï¿½todo que se encarga de asignar la operaciï¿½n correspondiente a un boton.
     *Este mï¿½todo llama al metodo setOperation de la clase InvokeActionBean.
     * @param action
     */
    protected void setOperation(ActionEvent action) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        InvokeActionBean invokeActionBean =
            (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
        invokeActionBean.setOperation(action);
    }


    // Este metodo es llamado para validar el cambio de la placa minera
    public void cambiarExpMinero(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio: cambiarExpMinero()");
        log.debug(user+" -> value " + evt.getNewValue().toString());
        log.debug(user+" -> class " + evt.getNewValue().getClass());
        try {
            if (evt == null || evt.getNewValue() == null) {
                log.debug(user+" -> cambiarExpMinero() expMinero null");
            } else {
                String expMinero = evt.getNewValue().toString();
                if (!validarExpMinero(expMinero)) {
                    mostrarMensaje(FacesMessage.SEVERITY_WARN, "El Expediente Minero \"" + expMinero + "\" actualmente no se encuentra registrado" +
                                   " en el SGD de la ANM." +
                        "Se enviara una notificacion al Administrador");
                    
                    /* ****** comandos para limpiar el campo placa ********
                        ********** se suspenden para permitir placas no existentes en wcc ***
                    itPlaca.resetValue(); //limpia el valor del componente
                    log.debug(user+" -> itPlaca.getValue() " + itPlaca.getValue());                    
                    //refresca el componente asociado al binding itPlaca
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itPlaca); */
                }
            }
        } catch (Exception e) {
            log.error("cambiarExpMinero() " + e);
        }
    }

    protected boolean validarExpMinero(String expMinero) {
        try {
            WccResponse expMineroResponse = new WccResponse();
            expMineroResponse = expedienteMinero.validarExpedienteMinero(expMinero);
            String statusCode = expMineroResponse.getStatusCode();
            log.debug(user+" -> validarExpMinero() StatusCode " + statusCode);
            if (statusCode.equals("0")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("validarExpMinero() " + e);
            return false;
        }
    }

    protected boolean validarExpMinero() {
        log.info("Init validarExpMinero()");
        try {
            if ((Boolean) getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}")) {
                log.info("Enter if esTituloMinero true");
                if (getElObjectFromBinding("#{bindings.nroPlaca.inputValue}").toString().isEmpty()) {
                    return false;
                } else {
                    log.debug(user+" -> Expediente minero marcado y con placa valida");
                    return true;
                }
            } else {
                log.debug(user+" -> Expediente no marcado. Contiue normal");
                return true;
            }
        } catch (Exception e) {
            log.error("validarExpMinero() " + e);
            return false;
        }
    }

    public void cambiarEsTituloMinero(ValueChangeEvent evt) {

        log.debug(user+" -> Inicio: cambiarEsTituloMinero()");
        log.debug(user+" -> value " + evt.getNewValue().toString());
        log.debug(user+" -> class " + evt.getNewValue().getClass());
        try {
            if (evt == null || evt.getNewValue() == null) {
                log.debug(user+" -> cambiarEsTituloMinero()  null");
            } else {
                this.setElObjectIntoBinding("#{bindings.nroPlaca.inputValue}", "");
                log.debug(user+" -> seteada placa vacia ok");
            }
        } catch (Exception e) {
            log.error("cambiarExpMinero() " + e);
        }
    }


    /*************************************
     * GETTERS AND SETTERS
     * **********************************/

    public void setFechaActual(Timestamp fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Timestamp getFechaActual() {

        // Asignar Fecha
        Date fecha = new Date();
        this.fechaActual = new Timestamp(fecha.getTime());
        log.debug(user + " -> fechaActual: " + this.fechaActual);

        try {

            Timestamp ts = new Timestamp(fecha.getTime());
            setElObjectIntoBinding("#{bindings.fecRadicacion.inputValue}", ts);
            log.debug(user + " ->Correct fechaActual: " + ts);
            log.debug(user + "binding fecRad :" + getElObjectFromBinding("#{bindings.fecRadicacion.inputValue}"));

        } catch (Exception e) {
            log.debug(user + " ->Exception fechaActual: " + e);
        }
        return fechaActual;
    }
    
    public boolean isRenderDocPrincipal ( ){
        String docPrincipal = (String)getElObjectFromBinding("#{bindings.urlDocumentoPrincipalDO.inputValue}");
        log.debug(user+" -> isRenderDocPrincipal: "+docPrincipal);
        return docPrincipal!=null && docPrincipal.length()>1;
    }
    
    public boolean isRenderAdjunto ( ){
        String docAdjunto = (String)getElObjectFromBinding("#{bindings.urlArchivoAdjuntoDO.inputValue}");
        log.debug(user+" -> isRenderAdjunto: "+docAdjunto);
        return docAdjunto!=null && docAdjunto.length()>1;
    }

    public void setComunicacionesBean(WCCSessionEJBLocal9 comunicacionesBean) {
        this.comunicacionesBean = comunicacionesBean;
    }

    public WCCSessionEJBLocal9 getComunicacionesBean() {
        return comunicacionesBean;
    }

    public void setDependenciaDestino(String dependenciaDestino) {
        this.dependenciaDestino = dependenciaDestino;
    }

    public String getDependenciaDestino() {
        return dependenciaDestino;
    }

    public void setDependenciasSet(Collection<AnmUnidadadministrativaTb> dependenciasSet) {
        this.dependenciasSet = dependenciasSet;
    }

    public Collection<AnmUnidadadministrativaTb> getDependenciasSet() {
        return dependenciasSet;
    }

    public void setListaDependencias(Collection<SelectItem> listaDependencias) {
        this.listaDependencias = listaDependencias;
    }

    public Collection<SelectItem> getListaDependencias() {
        return listaDependencias;
    }

    public void setSgdUsuario(SgdUsuario sgdUsuario) {
        this.sgdUsuario = sgdUsuario;
    }

    public SgdUsuario getSgdUsuario() {
        return sgdUsuario;
    }

    public void setListUsuarios(List<SgdUsuario> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }

    public List<SgdUsuario> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuariosSI(List<SelectItem> listUsuariosSI) {
        this.listUsuariosSI = listUsuariosSI;
    }

    public List<SelectItem> getListUsuariosSI() {
        return listUsuariosSI;
    }

    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
    }

    public Interesado getInteresado() {
        return interesado;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setComReasignada(String comReasignada) {
        this.comReasignada = comReasignada;
    }

    public String getComReasignada() {
        return comReasignada;
    }

    public void setIsPQRS(Boolean isPQRS) {
        this.isPQRS = isPQRS;
    }

    public Boolean getIsPQRS() {
        return isPQRS;
    }

    public void setListTipoPQRSSI(List<SelectItem> listTipoPQRSSI) {
        this.listTipoPQRSSI = listTipoPQRSSI;
    }

    public List<SelectItem> getListTipoPQRSSI() {
        return listTipoPQRSSI;
    }

    public void setTipoPQRS(String tipoPQRS) {
        this.tipoPQRS = tipoPQRS;
    }

    public String getTipoPQRS() {
        return tipoPQRS;
    }

    public void setTiposDocumentales(List<SelectItem> tiposDocumentales) {
        this.tiposDocumentales = tiposDocumentales;
    }

    public List<SelectItem> getTiposDocumentales() {
        return tiposDocumentales;
    }

    public void setTiposDocumentalesSet(List<AnmTipodtalseguntramite> tiposDocumentalesSet) {
        this.tiposDocumentalesSet = tiposDocumentalesSet;
    }

    public List<AnmTipodtalseguntramite> getTiposDocumentalesSet() {
        return tiposDocumentalesSet;
    }

    public void setTramites(List<SelectItem> tramites) {
        this.tramites = tramites;
    }

    public List<SelectItem> getTramites() {
        return tramites;
    }

    public void setTramitesSet(List<AnmTramiteTb> tramitesSet) {
        this.tramitesSet = tramitesSet;
    }

    public List<AnmTramiteTb> getTramitesSet() {
        return tramitesSet;
    }

    public void setTieneReferencia(boolean tieneReferencia) {
        this.tieneReferencia = tieneReferencia;
    }

    public boolean isTieneReferencia() {
        return tieneReferencia;
    }

    public void setEsPQRS(String esPQRS) {
        this.esPQRS = esPQRS;
    }

    public String getEsPQRS() {
        return esPQRS;
    }

    public void setRequiereRespuesta(String requiereRespuesta) {
        this.requiereRespuesta = requiereRespuesta;
    }

    public String getRequiereRespuesta() {
        return requiereRespuesta;
    }

    public void setRequiereGestionCompartida(String requiereGestionCompartida) {
        this.requiereGestionCompartida = requiereGestionCompartida;
    }

    public String getRequiereGestionCompartida() {
        return requiereGestionCompartida;
    }

    public void setCodDependencia(Long codDependencia) {
        this.codDependencia = codDependencia;
    }

    public Long getCodDependencia() {
        return codDependencia;
    }

    public void setListaTipoSolicitud(Collection<SgdTipoSolicitud> listaTipoSolicitud) {
        this.listaTipoSolicitud = listaTipoSolicitud;
    }

    public Collection<SgdTipoSolicitud> getListaTipoSolicitud() {
        return listaTipoSolicitud;
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

    public void setFechaVencimiento(Timestamp fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public void onFileUploadValueChangeListener(ValueChangeEvent valueChangeEvent) {
        this.resetValue();
        UploadedFile documento = (UploadedFile) valueChangeEvent.getNewValue();
        int length = (int) documento.getLength();
        fileContent = new byte[length];
        this.fileFormat = documento.getContentType();
        
        try {
            //this.fileBase64 = FileUtils.encodeFileToBase64Binary(documento);
            this.fileInputStream = documento.getInputStream();
            documento.getInputStream().read(fileContent);
            this.fileName = documento.getFilename();
            log.debug("File : '" + documento.getFilename() + "', '" + documento.getContentType() + "', '" +
                         documento.getLength() + "'");
        } catch (IOException e) {
            log.error("Error al leer o escribir el documento", e);
        }
    }
    
    /*
     * validarSubcategoria: Metodo encargado de listar las subcategorias del tipo de solicitud
     * seleccionado por el usuario
     * 
     * Fecha: 13Marzo2023 (Tomado de la lÃ³gica actual del formulario PQRS
     */
    public void validarSubcategoria(ValueChangeEvent vce) {
        Long tipo = (Long) vce.getNewValue();
        log.info("CrearComunicacionEntElecHT_jsfbean | validarSubcategoria | tipo: "+tipo);

        //Enable "placa" input field for "sugerencia" in "tipo solicitud"
        /*if(tipo.equals(10L)){
            itPlaca.setVisible(true);
        }else{
            itPlaca.setVisible(false);
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(itPlaca);*/
        try{
            if(vce.getNewValue()!= null){
                setElObjectIntoBinding("#{bindings.idTipoSolicitud.inputValue}", Integer.valueOf(vce.getNewValue().toString()) );
            }else{
                setElObjectIntoBinding("#{bindings.idTipoSolicitud.inputValue}", 0);
            }
        }catch (NumberFormatException nfe){
            log.error("Error al capturar la el tipo de solicitud "+vce.getNewValue(), nfe);
        }
        
        for (SgdTipoSolicitud tp : this.tiposSolicitudSet) {
            if (tipo.equals(tp.getIdTipoSolicitud())) {

                SgdSubCategoria temp = null;
                setSubcategoriaSet(tp.getCategorias());
                subcategoriaSet = ordenar(getSubcategoriaSet());
                setSubcategorias(new ArrayList<SelectItem>(getSubcategoriaSet().size()));
                for (SgdSubCategoria sb : getSubcategoriaSet()) {

                    if (!sb.getSubCategoria().equalsIgnoreCase("Otro")) {
                        getSubcategorias().add(new SelectItem(sb.getIdSubCategoria().longValue(), sb.getSubCategoria()));
                    } else {
                        temp = sb;
                    }
                }
                if (temp != null) {
                    getSubcategorias().add(new SelectItem(temp.getIdSubCategoria().longValue(), temp.getSubCategoria()));
                    break;
                }
            }
        }
        this.idSubcategoria = null;
    }
    public void subcategoriaVCL(ValueChangeEvent vce){
        try{
            if(vce.getNewValue()!= null){
                setElObjectIntoBinding("#{bindings.idSubCategoria.inputValue}", Integer.valueOf(vce.getNewValue().toString()) );
            }else{
                setElObjectIntoBinding("#{bindings.idSubCategoria.inputValue}", 0);
            }
        }catch (NumberFormatException nfe){
            log.error("Error al capturar la subcategoria "+vce.getNewValue(), nfe);
        }
    }
    
    
    private Collection<SgdSubCategoria> ordenar(Collection<SgdSubCategoria> cat) {

        List<SgdSubCategoria> result = new ArrayList<SgdSubCategoria>(cat);

        Collections.sort(result, new Comparator<SgdSubCategoria>() {
            public int compare(SgdSubCategoria p1, SgdSubCategoria p2) {
                return p1.getSubCategoria().compareTo(p2.getSubCategoria());
            }
        });

        return result;
    }
    
    
    public void resetValue() {
        if (fileName != null)
            fileName = null;
        if (fileContent != null)
            fileContent = null;
    }
    
    
    private void uploadAdjunto() {
        log.debug("---uploadAdjunto---");
        if (this.fileContent != null && this.fileName != null && !this.fileName.isEmpty()) {
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
            this.filePath = PropertiesLoader.getInstance().getProperty(PropertiesKeys.LOCAL_TMP_PATH) + dateTime+"_"+this.fileName;
            log.debug("filePath :" + this.filePath);
            try {
                this.copyStreamToFile(fileInputStream, new File(this.filePath));
                this.fileUrl = PropertiesLoader.getInstance().getProperty(PropertiesKeys.VIRTUAL_DIRECTORY_PATH) + dateTime+"_"+ this.fileName;
                log.debug("fileUrl :"+fileUrl);
                DCIteratorBinding iterBind = ADFUtils.findIterator("anexosIterator");
                ADFUtils.executeOperationBinding("CreateAnexo");
                Row row = iterBind.getCurrentRow();
                row.setAttribute("descripcion","Anexo de comunicacion ");
                row.setAttribute("contenido",this.fileUrl);
                row.setAttribute("mimeType",this.fileFormat);
                row.setAttribute("nombre",this.fileName);
                
            } catch (Exception e){
                log.error("Error de lectura o escritura del archivo", e);
            }
        }
    }
    private void copyStreamToFile ( InputStream is, java.io.File dest ) throws Exception{
        OutputStream out = new FileOutputStream(dest);

        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        is.close();
        out.close();
    }

    public Timestamp getFechaVencimiento() {
        log.debug(user+" -> Inicio: asignarFechaVencimientoPQRSWeb()");
        try {
            log.debug(user+" -> Tipo de pqrs selected " + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));
            if(getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}")!=null){
                //get el tipo de PQRS
                this.tipoSolicitudSelected =
                    Long.parseLong(getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}").toString());

                //setear el tipo de PQRS en el binding
                setElObjectIntoBinding("#{bindings.idTipoPQRS.inputValue}", this.tipoSolicitudSelected);
                log.debug(user+" -> Tipo de pqrs selected " + getElObjectFromBinding("#{bindings.idTipoPQRS.inputValue}"));

                // setear el tiempo de respuesta segun el tipo de PQRS
                tiempoRespuestaPQRS = this.comBean.getTipoSolicitudById(tipoSolicitudSelected).getTiempoRespuesta();
                log.debug(user+" ->  getFechaVencimiento() tiempo: " + tiempoRespuestaPQRS);
                setElObjectIntoBinding("#{bindings.tiempoRespuesta.inputValue}", tiempoRespuestaPQRS);

                // Calcular la fecha de vencimiento service proxy
                log.debug(user+" -> Init calcular fecha vencimiento");
                FechaVencimientoRequest fvrequest = new FechaVencimientoRequest();
                fvrequest.setDiasRespuesta(tiempoRespuestaPQRS);
                log.debug(user+" -> Init cast fecha");
                Timestamp ts = (Timestamp) getElObjectFromBinding("#{bindings.fecRadicacion.inputValue}");
                log.debug(user+" -> cast fecha - timestamp " + ts.toString());
                Date date = new Date(ts.getTime());
                log.debug(user+" -> cast fecha - date " + date.toString());
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                log.debug(user+" -> cast fecha gcalendar " + c.getTime().toString());
                XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                log.debug(user+" -> cast fecha xmlgcalendar " + cal.toString());
                fvrequest.setFechaRadicacion(cal);
                log.debug(user+" -> fecha de radicacion " + fvrequest.getFechaRadicacion().toString());
                FechaVencimientoResponse fvresponse = comunicaciones.calcularFechaVencimiento(fvrequest);
                XMLGregorianCalendar calfv = fvresponse.getFechaVencimiento();
                log.debug(user+" -> Fecha de vencimiento " + calfv.toString());
                log.debug(user+" -> end calcular fecha vencimiento");
                log.debug(user+" -> cast to timestamp fecha vencimiento");
                Timestamp tsfv = new Timestamp(calfv.toGregorianCalendar()
                                                    .getTime()
                                                    .getTime());
                //Setear en el binding la fecha de vencimiento del tipo timeStamp
                setElObjectIntoBinding("#{bindings.fechaVencimiento.inputValue}", tsfv);
                log.debug(user+" -> set fecha vencimiento into binding: " + tsfv);
                log.debug(user+" -> get fecha vencimiento into binding :" +
                          getElObjectFromBinding("#{bindings.fechaVencimiento.inputValue}"));
                return tsfv;
            }
        } catch (Exception e) {
            log.error("No fue posible encontrar el tiempo de respuesta para este tipo de solicitud", e);
        }
        log.debug(user+" -> Fin: asignarFechaVencimientoPQRSWeb()");
        return null;
    }

    public void setListaTipoIdentificacionSI(List<SelectItem> listaTipoIdentificacionSI) {
        this.listaTipoIdentificacionSI = listaTipoIdentificacionSI;
    }

    public List<SelectItem> getListaTipoIdentificacionSI() {
        return listaTipoIdentificacionSI;
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

    public void setListaPaisSI(List<SelectItem> listaPaisSI) {
        this.listaPaisSI = listaPaisSI;
    }

    public List<SelectItem> getListaPaisSI() {
        return listaPaisSI;
    }

    public void setListaDepartamentoSI(List<SelectItem> listaDepartamentoSI) {
        this.listaDepartamentoSI = listaDepartamentoSI;
    }

    public List<SelectItem> getListaDepartamentoSI() {
        return listaDepartamentoSI;
    }

    public void setListaMunicipioSI(List<SelectItem> listaMunicipioSI) {
        this.listaMunicipioSI = listaMunicipioSI;
    }

    public List<SelectItem> getListaMunicipioSI() {
        return listaMunicipioSI;
    }

    public void setItPlaca(RichInputText itPlaca) {
        this.itPlaca = itPlaca;
    }

    public RichInputText getItPlaca() {
        return itPlaca;
    }

    public void setSocTipoPQRS(RichSelectOneChoice socTipoPQRS) {
        this.socTipoPQRS = socTipoPQRS;
    }

    public RichSelectOneChoice getSocTipoPQRS() {
        return socTipoPQRS;
    }
    
    public ArrayList<SelectItem> getTiposSolicitantes() {
        return tiposSolicitantes;
    }

    public void setTiposSolicitantes(ArrayList<SelectItem> tiposSolicitantes) {
        this.tiposSolicitantes = tiposSolicitantes;
    }
	
	public void setOcupaciones(ArrayList<SelectItem> ocupaciones) {
        this.ocupaciones = ocupaciones;
    }	
    public ArrayList<SelectItem> getOcupaciones() {	
        return ocupaciones;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setIdTpSolicitud(Long idTpSolicitud) {
        this.idTpSolicitud = idTpSolicitud;
    }

    public Long getIdTpSolicitud() {
        return idTpSolicitud;
    }

    public void setTiposSolicitud(ArrayList<SelectItem> tiposSolicitud) {
        this.tiposSolicitud = tiposSolicitud;
    }

    public ArrayList<SelectItem> getTiposSolicitud() {
        return tiposSolicitud;
    }

    public void setTiposSolicitudSet(Collection<SgdTipoSolicitud> tiposSolicitudSet) {
        this.tiposSolicitudSet = tiposSolicitudSet;
    }

    public Collection<SgdTipoSolicitud> getTiposSolicitudSet() {
        return tiposSolicitudSet;
    }

    public void setIdSubcategoria(Long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public Long getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setSubcategorias(ArrayList<SelectItem> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public ArrayList<SelectItem> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategoriaSet(Collection<SgdSubCategoria> subcategoriaSet) {
        this.subcategoriaSet = subcategoriaSet;
    }

    public Collection<SgdSubCategoria> getSubcategoriaSet() {
        return subcategoriaSet;
    }

    public void setItCuaderno(RichInputText itCuaderno) {
        this.itCuaderno = itCuaderno;
    }

    public RichInputText getItCuaderno() {
        return itCuaderno;
    }
    
    private boolean validarDatosCierreREQ04() {
        Object tipoObj =
            getElObjectFromBinding("#{bindings.tipoCierreDO.inputValue}");
        Object requiereObj =
            getElObjectFromBinding("#{bindings.requiereRespuestaDO.inputValue}");
        Object justificacionObj =
            getElObjectFromBinding("#{bindings.justificacionCierreDO.inputValue}");

        String tipo = tipoObj == null ? "" : tipoObj.toString();
        String requiere = requiereObj == null ? "" : requiereObj.toString();
        String justificacion =
            justificacionObj == null ? "" : justificacionObj.toString();

        if ("S".equals(requiere)
            && !"RESPUESTA".equals(tipo)
            && !"CIERRE_AUTOMATICO".equals(tipo)) {

            mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                           "Debe seleccionar un tipo de cierre válido.");
            return false;
        }

        if ("CIERRE_AUTOMATICO".equals(tipo)) {
            if (!"S".equals(requiere)) {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                               "El cierre automático requiere una comunicación que requiere respuesta.");
                return false;
            }

            if (justificacion.trim().isEmpty()
                || justificacion.length() < 100
                || justificacion.length() > 1000) {

                mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                               "Justificación de Cierre Automático debe contener mínimo 100 caracteres y máximo 1000 caracteres");
                return false;
            }
        }

        return true;
    }
}
