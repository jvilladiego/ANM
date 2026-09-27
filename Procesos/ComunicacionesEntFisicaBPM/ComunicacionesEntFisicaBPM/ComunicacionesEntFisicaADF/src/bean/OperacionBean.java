package bean;

import co.gov.anm.sgd.handler.CMCHandler;
import co.gov.anm.sgd.handler.CMCHandlerResolver;
import co.gov.anm.sgd.service.AnexoComunicacionRequest;
import co.gov.anm.sgd.service.ComunicacionRequest;
import co.gov.anm.sgd.service.Comunicaciones;
import co.gov.anm.sgd.service.DependenciaRol;
import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.FechaVencimientoRequest;
import co.gov.anm.sgd.service.FechaVencimientoResponse;
import co.gov.anm.sgd.service.InteresadoRequest;
import co.gov.anm.sgd.service.ProcesoComunicacionRequest;
import co.gov.anm.sgd.service.RadicadoResponse;
import co.gov.anm.sgd.service.SolicitudRadicado;
import co.gov.anm.sgd.service.UsuarioResponse;
import co.gov.anm.sgd.service.WccResponse;
import co.gov.anm.sgd.util.SGDWebServiceLocator;

import com.itextpdf.text.pdf.PdfContentByte;

import ejb.SessionEJBLocal3;

import entities.AnmPlantilla;
import entities.AnmTipodtalseguntramite;
import entities.AnmTramiteTb;
import entities.AnmUnidadAdministrativaTb;
import entities.SgdAnexoComunciacion;
import entities.SgdDepartamento;
import entities.SgdMunicipio;
import entities.SgdPais;
import entities.SgdTipoIdentificacion;
import entities.SgdTipoSolicitud;
import entities.SgdUsuario;
import entities.TipoSolicitante;
import entities.folderfolders;

import gov.anm.cmc.objetos.Anexos;
import gov.anm.cmc.objetos.Respuesta;
import gov.anm.cmc.serviciosintegracion.IntegracionSGDCMC;

import java.io.File;
import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.SelectionEvent;
// probando save para prevenir perdidas de radicado
// refresh page


@ManagedBean(name = "operacionBean")
@ViewScoped
public class OperacionBean implements Serializable {
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 3049741450621151487L;

    @EJB
    private SessionEJBLocal3 sessionEJB;

    private static String rutaFisica = "/ftp/comunicaciones/";

    private Logger log;
    private String user;
    private SgdUsuario sgdUsuario = new SgdUsuario();
    private AnmUnidadAdministrativaTb anmUnidad = new AnmUnidadAdministrativaTb();
    private List<AnmPlantilla> lstPlantilla = new ArrayList<AnmPlantilla>();
    private List<Interesado> lstDestinatario = new ArrayList<Interesado>();
    private List<SelectItem> lstDependencias = new ArrayList<SelectItem>();
    private List<SelectItem> lstDependencias2 = new ArrayList<SelectItem>();
    private List<SelectItem> lstUsuariosObj = new ArrayList<SelectItem>();
    private List<SelectItem> lstTipoSolicitud = new ArrayList<SelectItem>();

    private List<Anexo> lstAnexo = new ArrayList<Anexo>();
    private RichTable tplantilla;

    private RichTable tdestinatario;
    private Date fechaActual;
    private Interesado nuevoDestinatario = new Interesado();
    private Interesado selectedDestinatario;
    private Anexo selectedAnexo;
    private Anexo nuevoAnexo = new Anexo();
    private RichInputText itFecha;
    private RichInputText itDias = new RichInputText();
    private RichInputText itFechaVencimiento = new RichInputText();
    private RichSelectOneChoice socReqRespuesta;

    //Formulario Crear comunicacion
    private RichButton btnRadicado, btnEnviar, btnSticker;
    private RichButton btnRadicadoCMC;
    private RichOutputText itNoRadicado;
    private List<SelectItem> lstTiposIdentificacion = new ArrayList<SelectItem>();
    private List<SelectItem> lstPais = new ArrayList<SelectItem>();
    private List<SelectItem> lstPais2 = new ArrayList<SelectItem>();
    private List<SelectItem> lstDepartamento = new ArrayList<SelectItem>();
    private List<SelectItem> lstDepartamento2 = new ArrayList<SelectItem>();
    private List<SelectItem> lstMunicipio = new ArrayList<SelectItem>();
    private List<SelectItem> lstMunicipio2 = new ArrayList<SelectItem>();
    private Long codPais;
    private Integer codDepartamento;
    private List<AnmUnidadAdministrativaTb> lstAnmUnidad = new ArrayList<AnmUnidadAdministrativaTb>();

    //Variables
    private Boolean esPqrs = Boolean.FALSE;
    //Formulario Gestion comunicacion
    private Boolean tieneReferencia;
    private RichTable tanexo;

    //Formulario asginar usuario
    private String comReasignada;

    //Formulario gestion comunicacion
    private List<SelectItem> lstTiposDocum = new ArrayList<SelectItem>();
    private List<AnmTipodtalseguntramite> listaTiposDocum = new ArrayList<AnmTipodtalseguntramite>();
    private List<SelectItem> lstTramites = new ArrayList<SelectItem>();
    private List<AnmTramiteTb> listaTramites = new ArrayList<AnmTramiteTb>();
    //Taxonomia
    private List<SelectItem> lstCategoria = new ArrayList<SelectItem>();
    private List<folderfolders> listaCategoria = new ArrayList<folderfolders>();
    private List<SelectItem> lstProceso = new ArrayList<SelectItem>();
    private List<folderfolders> listaProceso = new ArrayList<folderfolders>();
    private List<SelectItem> lstSerie = new ArrayList<SelectItem>();
    private List<folderfolders> listaSerie = new ArrayList<folderfolders>();
    private List<SelectItem> lstSubserie = new ArrayList<SelectItem>();
    private List<folderfolders> listaSubserie = new ArrayList<folderfolders>();
    private List<SelectItem> lstExpediente = new ArrayList<SelectItem>();
    private List<folderfolders> listaExpediente = new ArrayList<folderfolders>();
    private RichInputText itPlaca;
    // Atributos para controlar tipo solicitud
    private ArrayList<SelectItem> tiposSolicitantes = null;
    private Collection<TipoSolicitante> tiposSolicitanteSet = null;

    private Boolean comRadicada = Boolean.FALSE;
    private String tituloPantalla;
    private RichButton btnSave;

    private String dependenciaDestino;
    
    //Comunicaciones proxy
    SGDWebServiceLocator locator = new SGDWebServiceLocator();
    Comunicaciones comunicaciones = locator.getComunicacionesProxy();
    ExpedienteMinero expedienteMinero = locator.getExpedienteMineroProxy();

    public OperacionBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN OperacionBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
            log.debug(user + " -> sessionEJB: " + sessionEJB);
        } catch (Exception e) {
            log.error(user + " -> Exception OperacionBean", e);
        }
        log.info("END OperacionBean");
    }

    @PostConstruct
    public void initComponents() {
        log.info("BEGIN initComponents");
        try {
            tituloPantalla = " ";
            //Imprimir titulo de la pantalla
            if(getElObjectFromBindings("#{bindings.title.inputValue}")!=null)
                this.tituloPantalla = getElObjectFromBindings("#{bindings.title.inputValue}").toString();
            log.debug(user + " -> title: " + this.tituloPantalla);
            // pantalla de crear comunicacion - se verifica si fue radicada previamente - evita perdidas de radicado
            if (this.tituloPantalla.contains("Crear Comunicaci") &&
                getElObjectFromBindings("#{bindings.nroRadicado.inputValue}") != null) {
                log.debug(user + " | Instancia creada previamente - No. de Radicado generado : " +
                          getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString());
                // comunicacion ya fue radicada
                this.comRadicada = Boolean.TRUE;
                // this.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El numero de radicado de esta comunicacion es : "+getElObjectFromBindings("#{bindings.nroRadicado.inputValue}"));
                log.debug(user + " | comRadicada : " + this.comRadicada);

                if (getElObjectFromBindings("#{bindings.referencia.inputValue}") != null) {
                    this.tieneReferencia = Boolean.TRUE;
                }

            } else {
                log.debug(user + " | Creaci�n de la instancia - No. de Radicado no ha sido generado");
            }

            fechaActual = new Date();
            //Asignar valor al campo idUsuario
            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            if (getElObjectFromBindings("#{bindings.idUsuario.inputValue}") == null) {
                setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", user);
                log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            }

            //Cargar usuario
            log.debug(user + " -> sessionEJB: " + sessionEJB);
            List<SgdUsuario> lstUsuario = new ArrayList<SgdUsuario>();
            //List<AnmUnidadAdministrativaTb> lstAnmUnidad;
            if (sessionEJB != null) {
                lstUsuario = sessionEJB.getSgdUsuarioFindById(user);
                log.debug(user + " -> lstUsuario size: " + lstUsuario.size());
                lstAnmUnidad = sessionEJB.getAnmUnidadAdministrativaTbFindAll();
                log.debug(user + " -> lstAnmUnidad size: " + lstAnmUnidad.size());
                for (AnmUnidadAdministrativaTb unidad : lstAnmUnidad) {
                    lstDependencias.add(new SelectItem(unidad.getCodUnidadAdministrativa(),
                                                       unidad.getCodUnidadAdministrativa() + " - " +
                                                       unidad.getNombreUnidadadministrativa()));
                    lstDependencias2.add(new SelectItem(unidad.getCodUnidadAdministrativa().longValue(),
                                                        unidad.getCodUnidadAdministrativa() + " - " +
                                                        unidad.getNombreUnidadadministrativa()));
                }
                log.debug(user + " -> lstDependencias size: " + lstDependencias.size());
                log.debug(user + " -> lstDependencias2 size: " + lstDependencias2.size());
                //Consultar tipos de solicitud
                List<SgdTipoSolicitud> lstSgdTipoSolicitud = new ArrayList<SgdTipoSolicitud>();
                lstSgdTipoSolicitud = sessionEJB.getSgdTipoSolicitudFindAll();
                log.debug(user + " -> lstSgdTipoSolicitud size: " + lstSgdTipoSolicitud.size());
                for (SgdTipoSolicitud reg : lstSgdTipoSolicitud)
                    lstTipoSolicitud.add(new SelectItem(reg.getIdTipoSolicitud().toString(), reg.getNombre()));
                log.debug(user + " -> lstTipoSolicitud size: " + lstTipoSolicitud.size());
            }
            if (lstUsuario.size() > 0) {
                sgdUsuario = lstUsuario.get(0);
                log.debug(user + " -> Cod.Dependencia: " + sgdUsuario.getCodigoDependencia());
                //Cargar dependencia
                Integer depend = sgdUsuario.getCodigoDependencia().intValue();
                log.debug(user + " -> depend: " + depend);
                //Setear codigo dependencia
                //setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", depend);
                List<AnmUnidadAdministrativaTb> anmUnidadUser =
                    sessionEJB.getAnmUnidadAdministrativaTbFindByCodigo(depend);
                log.debug(user + " -> anmUnidadUser size: " + anmUnidadUser.size());
                if (anmUnidadUser.size() > 0) {
                    anmUnidad = anmUnidadUser.get(0);
                    log.debug(user + " -> anmUnidad: " + anmUnidad.getNombreUnidadadministrativa());
                } else
                    log.debug(user + " -> No se encontr� la unidad con codigo: " + sgdUsuario.getCodigoDependencia());

            } else {
                log.debug(user + " -> No se encontr� el usuario ");
            }

            //Consultar paises
            log.debug(user + " -> lstPais size: " + lstPais.size());
            if (lstPais.size() == 0) {
                List<SgdPais> listaPais = sessionEJB.getSgdPaisFindAll();
                log.debug(user + " -> listaPais size: " + listaPais.size());
                lstPais.clear();
                lstPais2.clear();
                for (SgdPais reg : listaPais) {
                    lstPais.add(new SelectItem(reg.getId(), reg.getIso() + " - " + reg.getNombre()));
                    lstPais2.add(new SelectItem(Integer.parseInt(reg.getId().toString()),
                                                reg.getIso() + " - " + reg.getNombre()));
                }
                log.debug(user + " -> lstPais size: " + lstPais.size());
                log.debug(user + " -> lstPais2 size: " + lstPais2.size());
            }
            //Consultar departamentos
            List<SgdDepartamento> lstDptos = sessionEJB.getSgdDepartamentoFindAll();
            log.debug(user + " -> lstDptos size: " + lstDptos.size());
            for (SgdDepartamento reg : lstDptos) {
                lstDepartamento.add(new SelectItem(reg.getCodigo(), reg.getNombre()));
                lstDepartamento2.add(new SelectItem(Integer.parseInt(reg.getCodigo()), reg.getNombre()));
            }

            log.debug(user + " -> lstDepartamento size: " + lstDepartamento.size());

            // consultar municipios
            if (getElObjectFromBindings("#{bindings.idDepartamento.inputValue}") != null) {
                List<SgdMunicipio> lstMunic =
                    sessionEJB.getSgdMunicipioFindByDepartamento(Long.parseLong(getElObjectFromBindings("#{bindings.idDepartamento.inputValue}")
                                                                                .toString()));
                log.debug(user + " -> lstMunic size: " + lstMunic.size());
                for (SgdMunicipio reg : lstMunic)
                    lstMunicipio.add(new SelectItem(Integer.parseInt(reg.getCodigo()), reg.getNombre()));
            }
            
            // Generacion de listado de tipos de solicitud
            tiposSolicitanteSet = this.sessionEJB.getTipoSolicitanteFindAll();
            setTiposSolicitantes(new ArrayList<SelectItem>(tiposSolicitanteSet.size()));
            for (TipoSolicitante tp : tiposSolicitanteSet) {
                getTiposSolicitantes().add(new SelectItem(tp.getIdTipoSolicitante().intValue(), tp.getNombre()));
            }


            //Consultar tipos identificacion
            log.debug(user + " -> lstTiposIdentificacion size: " + lstTiposIdentificacion.size());
            if (lstTiposIdentificacion.size() == 0) {
                List<SgdTipoIdentificacion> listaTiposIden = sessionEJB.getSgdTipoIdentificacionFindAll();
                log.debug(user + " -> listaTiposIden size: " + listaTiposIden.size());
                lstTiposIdentificacion.clear();
                for (SgdTipoIdentificacion reg : listaTiposIden) {
                    lstTiposIdentificacion.add(new SelectItem(reg.getCodigo(), reg.getNombre()));
                }
                log.debug(user + " -> lstTiposIdentificacion size: " + lstTiposIdentificacion.size());
            }

            //Inicializar variable Pais con valor por defecto si aplica
            log.debug(user + " -> idPais: " + getElObjectFromBindings("#{bindings.idPais.inputValue}"));
            if (getElObjectFromBindings("#{bindings.idPais.inputValue}") == null) {
                codPais = 52L; //Colombia
                setElObjectIntoBinding("#{bindings.idPais.inputValue}", codPais);
                log.debug(user + " -> Cod.Pais precargado: " +
                          getElObjectFromBindings("#{bindings.idPais.inputValue}"));
            }
            if (getElObjectFromBindings("#{bindings.idPais.inputValue}") != null)
                log.debug(user + " -> idPais class: " +
                          getElObjectFromBindings("#{bindings.idPais.inputValue}").getClass());
            log.debug(user + " -> idDepartamento: " + getElObjectFromBindings("#{bindings.idDepartamento.inputValue}"));
            if (getElObjectFromBindings("#{bindings.idDepartamento.inputValue}") != null)
                log.debug(user + " -> idDepartamento class: " +
                          getElObjectFromBindings("#{bindings.idDepartamento.inputValue}").getClass());

            //Setear fecha creaci�n
            setElObjectIntoBinding("#{bindings.fechaCreacion.inputValue}", new Timestamp(new Date().getTime()));

            //Cargar listado anexos no digitalizables si aplica
            log.debug(user + " -> Nro.Radicado: " + getElObjectFromBindings("#{bindings.nroRadicado.inputValue}"));
            if (getElObjectFromBindings("#{bindings.nroRadicado.inputValue}") != null) {
                String nroRad = getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString();
                List<SgdAnexoComunciacion> lstSgdAnexo = sessionEJB.getSgdAnexoComunciacionFindByNumeroRadicado(nroRad);
                log.debug(user + " -> lstSgdAnexo size: " + lstSgdAnexo.size());
                Anexo anexo;
                for (SgdAnexoComunciacion reg : lstSgdAnexo) {
                    anexo = new Anexo();
                    anexo.setCantidad(Double.parseDouble(reg.getCantidad().toString()));
                    anexo.setDescripcion(reg.getDescripcion());
                    lstAnexo.add(anexo);
                }
                log.debug(user + " -> lstAnexo size: " + lstAnexo.size());
            }

            log.debug(user + " -> esTituloMinero: " + getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}"));
            if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") != null) {
                if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").equals("false")) {
                    //Precargar taxonomia
                    precargarTaxonomia();
                } else {
                    precargarTramites();
                }
            }

            //Verificacion valor variables
            if (getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}") != null) {
                log.debug(user + " -> esPqrsBO: " + getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}"));
                log.debug(user + " -> tipoPqrsBO: " + getElObjectFromBindings("#{bindings.tipoPqrsBO.inputValue}"));
                //Validar campo esPqrs para la actividad Gestion Comunicacion
                if (getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}").toString().equals("true"))
                    if (getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}") != null)
                        setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "S");

            }
            if (getElObjectFromBindings("#{bindings.esPqrsDO.inputValue}") != null) {
                log.debug(user + " -> esPqrsDO: " + getElObjectFromBindings("#{bindings.esPqrsDO.inputValue}"));
                Object tipoPqrs = getElObjectFromBindings("#{bindings.tipoPqrsDO.inputValue}");
                log.debug(user + " -> tipoPqrsDO: " + tipoPqrs);
                if (tipoPqrs != null)
                    log.debug(user + " -> tipoPqrsDO class: " + tipoPqrs.getClass());
            }

            //Actividad Asignar usuario gestion comp
            //Listado de Usuarios Dependencia del jefe
            log.debug(user + " -> Cod.Depend. Usuario: " + sgdUsuario.getCodigoDependencia());
            List<SgdUsuario> listaUsuarios =
                sessionEJB.getSgdUsuarioByCodDependencia(sgdUsuario.getCodigoDependencia());
            log.debug(user + " -> listaUsuarios size : " + listaUsuarios.size());
            lstUsuariosObj.clear();
            for (SgdUsuario usuario : listaUsuarios) {
                if(usuario.getSgdUsuarioRolList().size()>0)
                    lstUsuariosObj.add(new SelectItem(usuario, usuario.getNombreUsuario()));
            }
            log.debug(user + " -> lstUsuariosObj size: " + lstUsuariosObj.size());
            /*
            //Calcular dias si aplica
            log.debug(user+" -> tipoPqrsBO: " +getElObjectFromBindings("#{bindings.tipoPqrsBO.inputValue}"));
            if(getElObjectFromBindings("#{bindings.tipoPqrsBO.inputValue}")!=null){
                String idTipoPqrs = getElObjectFromBindings("#{bindings.tipoPqrsBO.inputValue}").toString();
                log.debug(user+" -> idTipoPqrs: " + idTipoPqrs);
                actualizarNumeroDias(Integer.parseInt(idTipoPqrs));
                if(itDias!=null){
                    log.debug(user+" -> itDias: " + itDias.getValue());
                    //Calcular fecha vencimiento
                    Calendar calendar = Calendar.getInstance();
                    Date fechaRad = (Date)getElObjectFromBindings("#{bindings.fechaRadicacion.inputValue}");
                    log.debug("fechaRad: "+fechaRad);
                    calendar.setTime(fechaRad);
                    calendar.add(Calendar.DATE,(Integer)itDias.getValue()); ///
                    calendar.getTime(); // java te devuelve un objeto date de acuerdo a la suma de dias que se pidieron.
                    log.debug("calendar: "+calendar.getTime());
                    if(itFechaVencimiento!=null){
                        itFechaVencimiento.setValue(calendar.getTime());
                        log.debug("itFechaVencimiento: "+itFechaVencimiento.getValue());
                    }
                }
            }*/

            //Verificar variable virtualDirectory
            log.debug("virtualDirectory: " + getElObjectFromBindings("#{bindings.virtualDirectory.inputValue}"));
            log.debug("rutaFisica: " + rutaFisica);

            //Limpiar preseleccion radio button "Asignar" para la pantalla "AsignarUsuario"
            if (getElObjectFromBindings("#{bindings.title.inputValue}").toString().contains("Reasignar a usuario")) {
                setElObjectIntoBinding("#{bindings.tipoAsignacionDO.inputValue}", null);
                log.debug(user + " -> tipoAsignacionDO: " +
                          getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}"));
            }

            if (getElObjectFromBindings("#{bindings.title.inputValue}").toString().contains("Gestionar com")) {
                if (getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}") != null &&
                Boolean.parseBoolean(getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}").toString())) {
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "S");
                    log.debug(user + " -> requiereRespuestaDO: " +
                          getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}"));                    
                } else {
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "N");
                    log.debug(user + " -> requiereRespuestaDO: " +
                          getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}"));                    
                }
            }

            /* se inicializa variable en el script de "Inicializar"
            //Inicializar variable tiempoRespuestaBO para evitar faltas en "Data association" (Crear comunicacion)
            if (getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}") != null) {
                setElObjectIntoBinding("#{bindings.tiempoRespuestaBO.inputValue}", 0);
            }*/

        } catch (Exception e) {
            log.error(user + " -> Exception initComponents", e);
        } 
        log.info("END initComponents");
    }


    private void cargarTiposDocumentales(String tramiteSelected) {
        log.info("BEGIN cargarTiposDocumentales");
        try {
            listaTiposDocum = sessionEJB.getTipoDocumentalTramiteFindByTramite(new BigDecimal(tramiteSelected));
            log.debug(user + " -> listaTiposDocum size: " + listaTiposDocum.size());
            lstTiposDocum.clear();
            for (AnmTipodtalseguntramite f : listaTiposDocum) {
                lstTiposDocum.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
            }
            log.debug(user + " -> lstTiposDocum size: " + lstTiposDocum.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cargarTiposDocumentales", e);
        }
        log.info("END cargarTiposDocumentales");
    }



    private void precargarTramites(){
        log.info("BEGIN precargarTramites");
        try {
            //Consultar tramites
            listaTramites = this.sessionEJB.getTramiteFindAll();
            for (AnmTramiteTb f : listaTramites) {
                //log.debug(user+" -> Tramite ID: " + f.getIdtramite());
                lstTramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
            }
            log.debug(user + " -> lstTramites size: " + lstTramites.size());
            //Precargar tipos documentales si aplica
            if (getElObjectFromBindings("#{bindings.idTramite.inputValue}") != null) {
                String idTramite = getElObjectFromBindings("#{bindings.idTramite.inputValue}").toString();
                log.debug(user + " -> idTramite: " + idTramite);
                cargarTiposDocumentales(idTramite);
            }
        } catch (Exception e) {
            log.error(user + " -> Exception precargarTramites", e);
        }
        log.info("END precargarTramites");
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
            lstCategoria.clear();
            listaCategoria = this.sessionEJB.getfolderfoldersFindParent();
            if (listaCategoria.size() > 0) {
                folderfolders parent = listaCategoria.get(0);
                listaCategoria = this.sessionEJB.getfolderfoldersFindByPArentGuid(parent.getFFOLDERGUID());
                log.debug(user + " -> listaCategoria size: " + listaCategoria.size());
                for (folderfolders f : listaCategoria) {
                    lstCategoria.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
                }
                log.debug(user + " -> lstCategoria size: " + lstCategoria.size());
            }
            //Precargar los combos hijos si aplica
            if (getElObjectFromBindings("#{bindings.idCatProceso.inputValue}") != null) {
                idCatProceso = (String) getElObjectFromBindings("#{bindings.idCatProceso.inputValue}");
                log.debug("precargarTaxonomia() - idCatProceso: " + idCatProceso);
                cargarProcesos(idCatProceso);
                if (getElObjectFromBindings("#{bindings.idProceso.inputValue}") != null) {
                    idProceso = (String) getElObjectFromBindings("#{bindings.idProceso.inputValue}");
                    log.debug("precargarTaxonomia() - idProceso: " + idProceso);
                    cargarSeries(idProceso);
                    if (getElObjectFromBindings("#{bindings.idSerie.inputValue}") != null) {
                        idSerie = (String) getElObjectFromBindings("#{bindings.idSerie.inputValue}");
                        log.debug("precargarTaxonomia() - idSerie: " + idSerie);
                        cargarSubseries(idSerie);
                        if (getElObjectFromBindings("#{bindings.idSubSerie.inputValue}") != null) {
                            idSubSerie = (String) getElObjectFromBindings("#{bindings.idSubSerie.inputValue}");
                            log.debug("precargarTaxonomia() - idSubSerie: " + idSubSerie);
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
            lstProceso.clear();
            this.listaProceso = this.sessionEJB.getfolderfoldersFindByPArentGuid(catProcesoSel);

            log.debug(user + " -> listaProceso size: " + listaProceso.size());
            for (folderfolders f : listaProceso) {
                lstProceso.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstProceso size: " + lstProceso.size());

            //Setear nombre de la categoria
            for (folderfolders item : listaCategoria) {
                if (item.getFFOLDERGUID().equals(catProcesoSel)) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.catProceso.inputValue}", item.getFFOLDERNAME());
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
            lstSerie.clear();
            listaSerie = sessionEJB.getfolderfoldersFindByPArentGuid(idProceso);
            log.debug(user + " -> listaSerie size: " + listaSerie.size());
            for (folderfolders f : listaSerie) {
                lstSerie.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstSerie size: " + lstSerie.size());

            //Setear nombre del proceso
            for (folderfolders item : listaProceso) {
                if (item.getFFOLDERGUID().equals(idProceso)) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.proceso.inputValue}", item.getFFOLDERNAME());
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
            for (folderfolders item : listaSerie) {
                if (item.getFFOLDERGUID().equals(idSerie)) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.serie.inputValue}", item.getFFOLDERNAME());
                    break;
                }
            }
            //Obtener lista de subseries
            lstSubserie.clear();
            listaSubserie = sessionEJB.getfolderfoldersFindByPArentGuid(idSerie);
            log.debug(user + " -> listaSubserie size: " + listaSubserie.size());
            for (folderfolders f : listaSubserie) {
                lstSubserie.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
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
            lstExpediente.clear();
            listaExpediente = sessionEJB.getfolderfoldersFindByPArentGuid(idSubSerie);
            log.debug(user + " -> listaExpediente size: " + listaExpediente.size());
            for (folderfolders f : listaExpediente) {
                lstExpediente.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstExpediente size: " + lstExpediente.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cargarCuadernos", e);
        }
        log.info("END cargarCuadernos");
    }


    public void cambiarAsignacion(ValueChangeEvent vce) {
        log.info("BEGIN cambiarAsignacion");
        try {
            //Listado de Usuarios por Dependencia
            log.debug(user + " -> Cod.Depend. Usuario: " + sgdUsuario.getCodigoDependencia());
            List<SgdUsuario> listaUsuarios =
                sessionEJB.getSgdUsuarioByCodDependencia(sgdUsuario.getCodigoDependencia());
            log.debug(user + " -> listaUsuarios size : " + listaUsuarios.size());
            lstUsuariosObj.clear();
            for (SgdUsuario usuario : listaUsuarios){
                if(usuario.getSgdUsuarioRolList().size()>0)
                    lstUsuariosObj.add(new SelectItem(usuario, usuario.getNombreUsuario()));
            }   
            log.debug(user + " -> lstUsuariosObj size: " + lstUsuariosObj.size());

            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarAsignacion", e);
        }
        log.info("END cambiarAsignacion");
    }


    public void cambiarTramite(ValueChangeEvent vce) {
        log.info("BEGIN cambiarTramite");
        try {
            log.debug(user + " -> Inicio: cambiarTramite(): " + vce.getNewValue());
            log.debug(user + " -> Inicio: cambiarTramite(): " + vce.getNewValue().getClass());

            String tramiteSelected = (String) vce.getNewValue();

            //Setear tramite para tipificacion
            setElObjectIntoBinding("#{bindings.idTramite.inputValue}", vce.getNewValue());
            log.debug(user + " -> idTramite: " + getElObjectFromBindings("#{bindings.idTramite.inputValue}"));

            for (AnmTramiteTb t : this.listaTramites) {
                if (tramiteSelected.equals(t.getIdtramite().toString())) {
                    log.debug(user + " -> t.getTramite(): " + t.getTramite());
                    setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                    break;
                }
            }

            listaTiposDocum = sessionEJB.getTipoDocumentalTramiteFindByTramite(new BigDecimal(tramiteSelected));
            log.debug(user + " -> listaTiposDocum size: " + listaTiposDocum.size());
            lstTiposDocum.clear();
            for (AnmTipodtalseguntramite f : listaTiposDocum) {
                lstTiposDocum.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
            }
            log.debug(user + " -> lstTiposDocum size: " + lstTiposDocum.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarTramite", e);
        }
        log.info("END cambiarTramite");
    }

    public void cambiarTipoDocumental(ValueChangeEvent vce) {
        log.info("BEGIN cambiarTipoDocumental");
        try {
            log.debug(user + " -> Inicio: cambiarTipoDocumental(): " + vce.getNewValue());
            log.debug(user + " -> Inicio: cambiarTipoDocumental(): " + vce.getNewValue().getClass());

            String idTpDoc = (String) vce.getNewValue();

            //Setear tipo documental para tipificacion
            setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", idTpDoc);
            log.debug(user + " -> idTpDoc: " + getElObjectFromBindings("#{bindings.idTpDocumento.inputValue}"));

            for (AnmTipodtalseguntramite t : this.listaTiposDocum) {
                if (idTpDoc.equals(t.getIdtipodtal().toString())) {
                    System.out.println("encuentra tipo documental");

                    setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());

                    System.out.println("asigna tipo documental");
                    break;
                }
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarTipoDocumental", e);
        }
        log.info("END cambiarTipoDocumental");
    }


    public void cambiarPlaca(ValueChangeEvent evt) {
        log.info("BEGIN cambiarPlaca");
        try {
            log.debug(user + " -> Inicio: cambiarPlaca(): " + evt.getNewValue());
            log.debug(user + " -> Inicio: cambiarPlaca(): " + evt.getNewValue().getClass());
            String placa = evt.getNewValue().toString();
            Boolean esValida = validarPlaca(placa);
            log.debug(user + " -> esValida: " + esValida);
            /*
             * Fecha: 13Junio2017
             * El siguiente bloque de codigo ha sido modificado para atender el ticket ANM-598
             */
            /******************************
            if(!esValida){
                itPlaca.setValue(null);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itPlaca);
                log.debug(user+" -> placa: " + getElObjectFromBindings("#{bindings.placa.inputValue}"));
            }
            ************************************************/

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarPlaca", e);
        }
        log.info("END cambiarPlaca");
    }


    public void cambiarDependencia(ValueChangeEvent vce) {
        log.info("BEGIN cambiarDependencia");
        try {
            String usuarioJefe = null, usuarioEnlace = null;
            log.debug(user + " -> cambiarDependencia: " + vce.getNewValue());
            log.debug(user + " -> cambiarDependencia tipo: " + vce.getNewValue().getClass());
            log.debug(user + " -> tipoAsignacionDO: " +
                      getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}"));

            //Asignar valor de la dependencia al payload
            setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", vce.getNewValue());
            log.debug(user + " -> codDependencia: " + getElObjectFromBindings("#{bindings.codDependencia.inputValue}"));

            //Invocar servicios
            /*ComunicacionesPortBindingQSService comunicacionesPortBindingQSService =
                new ComunicacionesPortBindingQSService();
            Comunicaciones comunicaciones = comunicacionesPortBindingQSService.getComunicacionesPortBindingQSPort();*/

            //Consulta obsoleta: Se debe consultar desde actividad de servicio para afeectar correctamente el payload
            //Consultar jefe dependencia
            Long codDepen = Long.parseLong(getElObjectFromBindings("#{bindings.codDependencia.inputValue}").toString());
            DependenciaRol dep = new DependenciaRol();
            log.debug(user + " | codDepen : " + codDepen);
            dep.setCodigoDependencia(codDepen);
            dep.setIdRol(2L); //Rol jefe
            UsuarioResponse response = this.comunicaciones.buscarUsuariosPorDependenciaRol(dep);
            log.debug(user + " | reponse.getStatusCode() : "+response.getStatusCode().toString());
            log.debug(user + " | reponse.getStatusMessage() : " + response.getStatusMessage().toString());
            if (response.getStatusCode().equals("0")) {
                usuarioJefe = response.getUsuarios()
                                      .get(0)
                                      .getIdUsuario();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "La dependencia seleccionada no tiene jefe asignado, por favor informe a TI.");
            }
            log.debug(user + " -> usuarioJefe: " + usuarioJefe);
            log.debug(user + " -> bindings.idUsuario.inputValue : " +
                      getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            if (usuarioJefe == null)
                setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", "weblogic");
            else setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuarioJefe);
            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));

            //Consultar enlace dependencia
            dep = new DependenciaRol();
            dep.setCodigoDependencia(codDepen);
            dep.setIdRol(3L); //Rol enlace
            response = this.comunicaciones.buscarUsuariosPorDependenciaRol(dep);
            if (response.getStatusCode().equals("0")) {
                usuarioEnlace = response.getUsuarios().get(0).getIdUsuario();
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "La dependencia seleccionada no tiene enlace asignado, por favor informe a TI.");
            }

            if (!getElObjectFromBindings("#{bindings.title.inputValue}").toString().contains("Gestionar comunicaci") ||
                !getElObjectFromBindings("#{bindings.title.inputValue}").toString().contains("Asignar/ Reasignar")) {
                //Asignacion usuario enlace dependencia al payload
                log.debug(user + " -> usuarioEnlace: " + usuarioEnlace);
                if (usuarioEnlace == null)
                    setElObjectIntoBinding("#{bindings.usuarioEnlace.inputValue}", "weblogic");
                else setElObjectIntoBinding("#{bindings.usuarioEnlace.inputValue}", usuarioEnlace);
            }
            log.debug(user + " -> usuarioEnlace: "+getElObjectFromBindings("#{bindings.usuarioEnlace.inputValue}"));

            // julian jimenez - la variable nombre2, corresponde al nombre del usuario, no se debe usarse para es fin
            //Obtener nombre de la dependencia
            for (AnmUnidadAdministrativaTb reg : lstAnmUnidad) {
                //log.debug(user+" -> codDepen: " +codDepen+" -- reg.getCodUnidadAdministrativa: " +reg.getCodUnidadAdministrativa() );
                //log.debug(user+" -> comparacion: " + reg.getCodUnidadAdministrativa().toString().equals(codDepen.toString()));
                if (reg.getCodUnidadAdministrativa().toString().equals(codDepen.toString())) {
                    this.dependenciaDestino = reg.getNombreUnidadadministrativa();
                    break;
                }
            }
            log.debug(user + " -> Dependencia Destino: " + this.dependenciaDestino);

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarDependencia", e);
        }
        log.info("END cambiarDependencia");
    }


    public void cambiarUsuario(ValueChangeEvent evt) {
        log.info(this.user + " -> BEGIN | cambiarUsuario");
        log.debug(" cambiarUsuario(): " + evt.getNewValue());
        log.debug(" cambiarUsuario(): " + evt.getNewValue().getClass());
        try {
            SgdUsuario usuarioSelected = (SgdUsuario) evt.getNewValue();
            // set id usuario
            setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuarioSelected.getIdUsuario());
            // set dependencia usuario
            setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", usuarioSelected.getCodigoDependencia());
            // set nombre usuario
            setElObjectIntoBinding("#{bindings.nombre2.inputValue}", usuarioSelected.getNombreUsuario());
            // set email usuario
            setElObjectIntoBinding("#{bindings.email1.inputValue}", usuarioSelected.getEmail());
            //Imprimir valores
            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            log.debug(user + " -> codDependencia: " + getElObjectFromBindings("#{bindings.codDependencia.inputValue}"));
            log.debug(user + " -> nombre2: " + getElObjectFromBindings("#{bindings.nombre2.inputValue}"));
            log.debug(user + " -> email1: " + getElObjectFromBindings("#{bindings.email1.inputValue}"));

        } catch (Exception e) {
            log.error(this.user + " | Exception | cambiarUsuario()", e);
        }
        log.info(this.user + " -> END | cambiarUsuario");
    }


    public void cambiarUsuarioGestionComp(ValueChangeEvent evt) {
        log.info(this.user + " -> BEGIN | cambiarUsuarioGestionComp");
        log.debug(" cambiarUsuarioGestionComp(): " + evt.getNewValue());
        log.debug(" cambiarUsuarioGestionComp(): " + evt.getNewValue().getClass());
        try {
            SgdUsuario usuarioSelected = (SgdUsuario) evt.getNewValue();
            // set id usuario
            setElObjectIntoBinding("#{bindings.usuarioGestionCompDO.inputValue}", usuarioSelected.getIdUsuario());
            setElObjectIntoBinding("#{bindings.idUsuario.inputValue}", usuarioSelected.getIdUsuario());
            // set dependencia usuario
            setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", usuarioSelected.getCodigoDependencia());
            // set nombre usuario
            setElObjectIntoBinding("#{bindings.nombre.inputValue}", usuarioSelected.getNombreUsuario());
            // set email usuario
            setElObjectIntoBinding("#{bindings.email.inputValue}", usuarioSelected.getEmail());
            //Imprimir valores
            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            log.debug(user + " -> usuarioGestionCompDO: " +
                      getElObjectFromBindings("#{bindings.usuarioGestionCompDO.inputValue}"));
            log.debug(user + " -> codDependencia: " + getElObjectFromBindings("#{bindings.codDependencia.inputValue}"));
            log.debug(user + " -> nombre: " + getElObjectFromBindings("#{bindings.nombre.inputValue}"));
            log.debug(user + " -> email: " + getElObjectFromBindings("#{bindings.email.inputValue}"));

        } catch (Exception e) {
            log.error(this.user + " | Exception | cambiarUsuarioGestionComp", e);
        }
        log.info(this.user + " -> END | cambiarUsuarioGestionComp");
    }


    public void cambiarPqrs(ValueChangeEvent vce) {
        log.info("BEGIN cambiarPqrs");
        try {
            log.debug(user + " -> cambiarPqrs: " + vce.getNewValue());
            log.debug(user + " -> cambiarPqrs tipo: " + vce.getNewValue().getClass());
            //Campo Tiempo de Respuesta, cuando se trata de una comunicacion PQRS el campo debe ser Calculado y no
            //modificable. Cuando la comunicacion no es PQRS y requiere respuesta el campo tiempo de respuesta puede
            //ser modificado por el usuario
            if (vce.getNewValue()
                   .toString()
                   .equals("S")) {
                socReqRespuesta.setValue("S");
                socReqRespuesta.setDisabled(true);
                itDias.setDisabled(true);
            } else
                socReqRespuesta.setDisabled(false);
            // AdfFacesContext.getCurrentInstance().addPartialTarget(socReqRespuesta);
            itDias.setValue(null);

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarPqrs", e);
        }
        log.info("END cambiarPqrs");
    }


    public void cambiarTipoPqrs(ValueChangeEvent vce) {
        log.info("BEGIN cambiarTipoPqrs");
        String tipoSolicitudSelected;
        String nombrePQRS;
        
        try {
            log.debug(user + " -> cambiarTipoPqrs: " + vce.getNewValue());
            log.debug(user + " -> cambiarTipoPqrs tipo: " + vce.getNewValue().getClass());

            //get el tipo de PQRS
            tipoSolicitudSelected = vce.getNewValue().toString();
            log.debug("cambiarTipoPQRS() --> tipoSolicitudSelected " + tipoSolicitudSelected);
            
            setElObjectIntoBinding("#{bindings.tipoPqrsBO.inputValue}", vce.getNewValue());
            actualizarNumeroDias(Integer.parseInt(vce.getNewValue().toString()));

            // setear el tipo de pqrs (string)
            log.debug("tipoSolicitudSelected.intValue() : " + Integer.parseInt(tipoSolicitudSelected));
            nombrePQRS = this.sessionEJB.getSgdTipoSolicitudFindById(Integer.parseInt(tipoSolicitudSelected)).getNombre();
            log.debug(this.user + " | cambiarTipoPQRS() | nombrePQRS: " + nombrePQRS);
            setElObjectIntoBinding("#{bindings.nombreTipoPQRS.inputValue}", nombrePQRS);
            
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarTipoPqrs", e);
        }
        log.info("END cambiarTipoPqrs");
    }


    public void cambiarTipoPqrsGestion(ValueChangeEvent vce) {
        log.info("BEGIN | cambiarTipoPqrsGestion()");
        String tipoSolicitudSelected;
        String nombrePQRS;

        try {
            log.debug(user + " | cambiarTipoPqrsGestion() | vce.getNewValue() : " + vce.getNewValue());
            log.debug(user + " | cambiarTipoPqrsGestion() | vce class : " + vce.getNewValue().getClass());

            //get el tipo de PQRS
            tipoSolicitudSelected = vce.getNewValue().toString();
            log.debug(user + " | cambiarTipoPqrsGestion() | tipoSolicitudSelected : " + tipoSolicitudSelected);

            // set id tipo de pqrs
            setElObjectIntoBinding("#{bindings.tipoPqrsBO.inputValue}", vce.getNewValue());

            //Numero de dias de respuesta
            actualizarNumeroDias(Integer.parseInt(vce.getNewValue().toString()));

            // Calcular fecha de vencimiento
            Integer tiempoRespuestaPQRS = (Integer) getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}");
            this.calcularFechaVencimiento(tiempoRespuestaPQRS);

            // setear el tipo de pqrs (string)
            log.debug(user + " | cambiarTipoPqrsGestion() | (int) tipoSolicitudSelected : " +
                      Integer.parseInt(tipoSolicitudSelected));
            nombrePQRS = this.sessionEJB
                             .getSgdTipoSolicitudFindById(Integer.parseInt(tipoSolicitudSelected))
                             .getNombre();
            log.debug(user + " | cambiarTipoPqrsGestion() | nombrePQRS : " + nombrePQRS);
            setElObjectIntoBinding("#{bindings.nombreTipoPQRS.inputValue}", nombrePQRS);

        } catch (Exception e) {
            log.error(user + " | Exception | cambiarTipoPqrsGestion()", e);
        }
        log.info("END cambiarTipoPqrsGestion()");
    }


    protected void calcularFechaVencimiento(Integer dias) {

        log.info(this.user + " | Inicio caculateFechaVencimiento()");
        try {

            // Calcular la fecha de vencimiento service proxy
            log.debug("Init calcular fecha vencimiento");
            FechaVencimientoRequest fvrequest = new FechaVencimientoRequest();
            fvrequest.setDiasRespuesta(dias);
            log.debug("Init cast fecha");
            Timestamp ts = (Timestamp) getElObjectFromBindings("#{bindings.fechaRadicacion.inputValue}");
            log.debug("cast fecha - timestamp " + ts.toString());
            Date date = new Date(ts.getTime());
            log.debug("cast fecha - date " + date.toString());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(date);
            log.debug("cast fecha gcalendar " + c.getTime().toString());
            XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            log.debug("cast fecha xmlgcalendar " + cal.toString());
            fvrequest.setFechaRadicacion(cal);
            log.debug("fecha de radicacion " + fvrequest.getFechaRadicacion().toString());
            FechaVencimientoResponse fvresponse = this.comunicaciones.calcularFechaVencimiento(fvrequest);
            XMLGregorianCalendar calfv = fvresponse.getFechaVencimiento();
            log.debug("Fecha de vencimiento " + calfv.toString());
            log.debug("end calcular fecha vencimiento");
            log.debug("cast to timestamp fecha vencimiento");
            Timestamp tsfv = new Timestamp(calfv.toGregorianCalendar()
                                                .getTime()
                                                .getTime());

            //Setear en el binding la fecha de vencimiento del tipo timeStampvencimiento
            setElObjectIntoBinding("#{bindings.vencimiento.inputValue}", tsfv);
            log.debug("set fecha vencimiento into binding: " + tsfv);
            log.debug("get fecha vencimiento into binding :" +
                      getElObjectFromBindings("#{bindings.vencimiento.inputValue}"));

        } catch (Exception e) {
            log.error(this.user + "| caculateFechaVencimiento()", e);
        }

    }
    
    private void actualizarNumeroDias(Integer tipoSolicitud) {
        log.info("BEGIN actualizarNumeroDias");
        try {
            log.debug(user + " -> tipoSolicitud: " + tipoSolicitud);
            //Consultar dias para el tipo de solicitud
            SgdTipoSolicitud solic = sessionEJB.getSgdTipoSolicitudFindById(tipoSolicitud);
            if (solic != null) {
                itDias.setValue(solic.getTiempoRespuesta());
                // AdfFacesContext.getCurrentInstance().addPartialTarget(itDias);
                setElObjectIntoBinding("#{bindings.tiempoRespuestaBO.inputValue}", solic.getTiempoRespuesta());
                log.debug(user + " -> tiempoRespuestaBO: " +
                          getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}"));
            } else {
                log.debug(user + " -> No se obtuvo respuesta de la base de datos (SgdTipoSolicitud)");
            }
            log.debug(user + " -> itDias value: " + itDias.getValue());
        } catch (Exception e) {
            log.error(user + " -> Exception actualizarNumeroDias", e);
        }
        log.info("END actualizarNumeroDias");
    }


    public void cambiarReqRespuesta(ValueChangeEvent vce) {
        log.info("BEGIN cambiarReqRespuesta");
        try {
            log.debug(user + " -> cambiarReqRespuesta: " + vce.getNewValue());
            log.debug(user + " -> cambiarReqRespuesta tipo: " + vce.getNewValue().getClass());
            if (vce.getNewValue()
                   .toString()
                   .equals("S")) {
                itDias.setDisabled(false);
                setElObjectIntoBinding("#{bindings.requiereGestionCompartidaDO.inputValue}", "N");
            } else {
                itDias.setDisabled(true);
                setElObjectIntoBinding("#{bindings.requiereGestionCompartidaDO.inputValue}", "N");
            }
            // AdfFacesContext.getCurrentInstance().addPartialTarget(itDias);
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarReqRespuesta", e);
        }
        log.info("END cambiarReqRespuesta");
    }


    private Boolean validarPlaca(String placa) {
        log.info(user + " -> INICIO validarPlaca");
        try {
            //Invocar proxy WS
            WccResponse response = expedienteMinero.validarExpedienteMinero(placa);
            log.debug(user + " -> response.getStatusCode(): " + response.getStatusCode());
            
            if (response.getStatusCode().equals("0")) {
                return Boolean.TRUE;
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "El n�mero de Expediente ingresado no existe. Presione 'Aceptar' para continuar.");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error(user + " -> Exception validarPlaca", e);
            if (e.getMessage().contains("OSB"))
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Se present� un inconveniente con el servicio web, por favor informe a TI.");
        }
        log.info(user + " -> FIN validarPlaca");
        return Boolean.FALSE;
    }


    public void borrarRegistro(ActionEvent ae) {
        log.info(user + " -> BEGIN borrarRegistro");
        try {
            lstAnexo.remove(selectedAnexo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tanexo);
        } catch (Exception e) {
            log.error(user + " -> Exception borrarRegistro", e);
        }
        log.info(user + " -> FIN borrarRegistro");
    }


    public void selectAnexo(SelectionEvent selEvent) {
        log.info(user + " -> BEGIN selectAnexo");
        try {
            //Get selection source
            RichTable rtDest = (RichTable) selEvent.getSource();
            //Get selected row
            selectedAnexo = (Anexo) rtDest.getSelectedRowData();
            log.info(user + " -> selectedAnexo: " + selectedAnexo.getDescripcion());

        } catch (Exception e) {
            log.error(user + " -> Exception selectAnexo", e);
        }
        log.info(user + " -> FIN selectAnexo");
    }


    public void agregarAnexo(DialogEvent de) {
        log.info(user + " -> BEGIN agregarAnexo");
        try {
            lstAnexo.add(nuevoAnexo);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tanexo);
            nuevoAnexo = new Anexo();
        } catch (Exception e) {
            log.error(user + " -> Exception agregarAnexo", e);
        }
        log.info(user + " -> FIN agregarAnexo");
    }


    private Boolean generarNumeroRadicado(){
        log.info(user + " -> BEGIN generarNumeroRadicado");
        try {
           //Validar valor inicial titulo minero en nulo
           if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") == null)
               setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);
           else if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").equals("true"))
               setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.TRUE);
           else
               setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);

           /*
            * Fecha: 13Junio2017
            * El siguiente bloque de codigo ha sido modificado para atender el ticket ANM-598
            */
           /******************************
           Object esTitulo = getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}");
           log.debug(user+" -> esTitulo: " + esTitulo);
           if(Boolean.parseBoolean(esTitulo.toString())){
               //Validar placa si aplica
               Object placa = getElObjectFromBindings("#{bindings.placa.inputValue}");
               log.debug(user+" -> placa: " + placa);
               if(placa!=null){
                   Boolean placaValida = validarPlaca(placa.toString());
                   log.debug(user+" -> placaValida: " + placaValida);
                   if (!placaValida){
                       mostrarMensaje(FacesMessage.SEVERITY_WARN, "Importante: La placa digitada no existe.");
                       return;
                   }
               }else{
                   mostrarMensaje(FacesMessage.SEVERITY_WARN, "Importante: La placa digitada no existe.");
                   return;
               }
           }
           ********************************************/

           //Invocar servicios
           /*ComunicacionesPortBindingQSService comunicacionesPortBindingQSService =
               new ComunicacionesPortBindingQSService();
           Comunicaciones comunicaciones = comunicacionesPortBindingQSService.getComunicacionesPortBindingQSPort();*/
            
           /*SGDWebServiceLocator locator = new SGDWebServiceLocator();
           Comunicaciones comunicaciones = locator.getComunicacionesProxy();*/

           //Construir objeto
           SolicitudRadicado sol = new SolicitudRadicado();
           log.debug(user + " -> getCodigoDependencia: " + sgdUsuario.getCodigoDependencia());
           sol.setCodDependencia(sgdUsuario.getCodigoDependencia().toString());
           sol.setTipoComunicacion("2"); //Entradas=2

           //Generar numero radicado
           RadicadoResponse response = this.comunicaciones.generarNumeroRadicado(sol);
           log.debug(user + " -> response.getStatusCode(): " + response.getStatusCode());
           //Verificar respuesta
           if (response.getStatusCode().equals("0")) {
               btnSticker.setDisabled(false);
               btnEnviar.setDisabled(false);
               AdfFacesContext.getCurrentInstance().addPartialTarget(btnEnviar);
               AdfFacesContext.getCurrentInstance().addPartialTarget(btnSticker);
               log.debug(user + " -> response.getNoRadicado(): " + response.getNoRadicado());
               //Mostrar numero radicado en pantalla
               setElObjectIntoBinding("#{bindings.nroRadicado.inputValue}", response.getNoRadicado());
               AdfFacesContext.getCurrentInstance().addPartialTarget(itNoRadicado);
               //Deshabilitar boton generar radicado
               btnRadicado.setDisabled(true);
               AdfFacesContext.getCurrentInstance().addPartialTarget(btnRadicado);
               //Mostrar mensaje de confirmacion
               mostrarMensaje(FacesMessage.SEVERITY_INFO,
                              "La radicaci�n ha sido exitosa.\n No.radicado: " + response.getNoRadicado()); //\u00e9
               //Setear fecha radicacion
               setElObjectIntoBinding("#{bindings.fechaRadicacion.inputValue}", new Timestamp(new Date().getTime()));

               //Enviar valores al bean Barcode
               BarcodeBean barcode = new BarcodeBean();
               barcode.setNroRadicado(getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString());
               
               barcode.setAsunto(getElObjectFromBindings("#{bindings.asunto.inputValue}").toString());
               barcode.setAsunto(barcode.getAsunto().length() > 45? barcode.getAsunto().substring(0, 45) : barcode.getAsunto());
               
               if(getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") != null && 
                  getElObjectFromBindings("#{bindings.placa.inputValue}") != null)
                   barcode.setPlaca(getElObjectFromBindings("#{bindings.placa.inputValue}").toString());
               else barcode.setPlaca("N.A.");
               barcode.setDestino(getElObjectFromBindings("#{bindings.codDependencia.inputValue}").toString() + " - " +
                                  this.dependenciaDestino);
               barcode.generarBarCode(null);
               log.debug(user + " -> No.folios: " + getElObjectFromBindings("#{bindings.nroFolios.inputValue}"));
               barcode.setFolios(getElObjectFromBindings("#{bindings.nroFolios.inputValue}").toString());
               barcode.setAnexos(lstAnexo.size() + "");
               barcode.setLstAnexo(lstAnexo);
               HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                                                                             .getExternalContext()
                                                                             .getRequest();
               request.getSession(false).setAttribute("barcodeBean", barcode);
               
           } else {
               btnSticker.setDisabled(true);
               mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                              "No se pudo generar el n�mero de radicado, por favor verifique e intente nuevamente.");
               return Boolean.FALSE;
           }
       } catch (Exception e) {
            log.error(user + " -> Exception generarNumeroRadicado",e);
        }
        log.info(user + " -> END generarNumeroRadicado");
        return Boolean.TRUE;
    }  
    
    
    private Boolean registrarComunicacionBD(){
        log.info("BEGIN registrarComunicacionBD");
        try {
            //Invocar servicios
            /*ComunicacionesPortBindingQSService comunicacionesPortBindingQSService =
                new ComunicacionesPortBindingQSService();
            Comunicaciones comunicaciones = comunicacionesPortBindingQSService.getComunicacionesPortBindingQSPort();*/
            
            /************************************************************
             * 20170323 jquiceno: Registra comunicacion en base de datos
             * Actualizado por: Adrian Molina - adrimol@gmail.com
             * Fecha actualizacion: 18Abril2017
             * Actualizado por: Julian Jimenez - julianjimenezun@gmail.com
             * Fecha actualizacion: 14Junio2017
             ***********************************************************/

            // registrar en base de datos la comunicacion

            log.info(this.user + " | BEGIN | Registro DB");
            ProcesoComunicacionRequest procComunicacionRequest = new ProcesoComunicacionRequest();

            // registro comunicacion
            ComunicacionRequest comunicacionRequest = new ComunicacionRequest();
            comunicacionRequest.setAsunto((String) getElObjectFromBindings("#{bindings.asunto.inputValue}"));
            if(comunicacionRequest.getAsunto().length()>500){
                comunicacionRequest.setAsunto(comunicacionRequest.getAsunto().substring(0,500));
            }
            comunicacionRequest.setEsPQRS(getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}") != null ?
                                          getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}").toString()
                                          .equals("true") ? "S" : "N" : "N");
            
            comunicacionRequest.setEsTitulo(getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") != null ?
                                            getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString()
                                            .equals("true") ? "S" : "N" : "N");
            comunicacionRequest.setEstadoComunicacion("PENDIENTE_DIGIT");
            comunicacionRequest.setFechaCreacion(DatatypeFactory.newInstance()
                                                 .newXMLGregorianCalendar(new GregorianCalendar()));
            comunicacionRequest.setUsuarioActualizacion(this.user);
            comunicacionRequest.setGrupoSeguridad("Public");
            comunicacionRequest.setIdDependenciaDestino(Integer.parseInt(getElObjectFromBindings("#{bindings.codDependencia.inputValue}")
                                                                         .toString()));
            //comunicacionRequest.setIdUnidadProductora(550)
            log.debug("sgdUsuario.getCodigoDependencia : "+sgdUsuario.getCodigoDependencia().intValue());
            comunicacionRequest.setIdUnidadProductora(sgdUsuario.getCodigoDependencia().intValue());
            comunicacionRequest.setNroAnexos(lstAnexo.size());
            log.debug(user + " -> No.folios2: " + getElObjectFromBindings("#{bindings.nroFolios.inputValue}"));
            Integer noFolios = Integer.parseInt(getElObjectFromBindings("#{bindings.nroFolios.inputValue}").toString());
            log.debug(user + " -> noFolios: " + noFolios);
            comunicacionRequest.setNroFolios(noFolios);
            log.debug(user + " -> Com. No.folios: " + comunicacionRequest.getNroFolios());
            log.debug(user + " -> placa: " + getElObjectFromBindings("#{bindings.placa.inputValue}"));
            if (getElObjectFromBindings("#{bindings.placa.inputValue}") != null){
                comunicacionRequest.setNroPlaca(getElObjectFromBindings("#{bindings.placa.inputValue}").toString());
                if(comunicacionRequest.getNroPlaca().length()>100){
                    comunicacionRequest.setNroPlaca(comunicacionRequest.getNroPlaca().substring(0,100));
                }
            }
            comunicacionRequest.setReferencia(getElObjectFromBindings("#{bindings.referencia.inputValue}") != null ?
                                              getElObjectFromBindings("#{bindings.referencia.inputValue}").toString() :
                                              null);

            comunicacionRequest.setNroRadicado(getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString());
            comunicacionRequest.setTipoComunicacion("Entrada");
            comunicacionRequest.setUsuarioCreacion(getElObjectFromBindings("#{bindings.inicializador.inputValue}")
                                                   .toString());
            if (getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}") != null) {
                log.debug(user + " | bindings.esPqrsBO : " +
                          getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}").toString());
            }
            log.debug(user + " -> esPqrsBO: "+getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}"));
            comunicacionRequest.setEsPQRS(getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}") != null &&
                                          getElObjectFromBindings("#{bindings.esPqrsBO.inputValue}").toString()
                                          .equals("true") ? "S" : "N");
            log.debug(user + " -> requiereRespuestaDO: "+getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}"));
            comunicacionRequest.setRequiereRespuesta(getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}") !=
                                                     null ?
                                                     getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}")
                                                     .toString() : "N");
            log.debug(user + " -> tiempoRespuestaBO: "+getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}"));
            comunicacionRequest.setDiasParaRespuesta(getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}") != null ?
                                                     Integer.parseInt(getElObjectFromBindings("#{bindings.tiempoRespuestaBO.inputValue}")
                                                                      .toString()) : 0);


            log.debug(user + " - > numero de instancia: " + getElObjectFromBindings("#{bindings.instanceNumber.inputValue}"));
            comunicacionRequest.setIdComunicacion(getElObjectFromBindings("#{bindings.instanceNumber.inputValue}") !=
                                                  null ?
                                                  Integer.parseInt(getElObjectFromBindings("#{bindings.instanceNumber.inputValue}")
                                                                   .toString()) : 0);
            procComunicacionRequest.setComunicacion(comunicacionRequest);
            log.debug(user + " -> TRACK 2");

            // registro interesado
            InteresadoRequest interesadoReq = new InteresadoRequest();
            interesadoReq.setTipoIdentificacion(getElObjectFromBindings("#{bindings.tipoIdentificacion.inputValue}")
                                                .toString());
            interesadoReq.setNroIdentificacion(getElObjectFromBindings("#{bindings.numeroIdentificacion.inputValue}")
                                               .toString());
            if(interesadoReq.getNroIdentificacion().length()>20){
                interesadoReq.setNroIdentificacion(interesadoReq.getNroIdentificacion().substring(0, 20));
            }
            interesadoReq.setPrimerNombre(getElObjectFromBindings("#{bindings.primerNombre.inputValue}").toString());
            if(interesadoReq.getPrimerNombre().length()>100){
                interesadoReq.setPrimerNombre(interesadoReq.getPrimerNombre().substring(0,100));
            }
            interesadoReq.setSegundoNombre(getElObjectFromBindings("#{bindings.segundoNombre.inputValue}") != null ?
                                           getElObjectFromBindings("#{bindings.segundoNombre.inputValue}").toString() :null);
            if(interesadoReq.getSegundoNombre()!=null){
                if(interesadoReq.getSegundoNombre().length()>100){
                    interesadoReq.setSegundoNombre(interesadoReq.getSegundoNombre().substring(0,100));
                }
            }            
            interesadoReq.setPrimerApellido(getElObjectFromBindings("#{bindings.primerApellido.inputValue}") != null ?
                                            getElObjectFromBindings("#{bindings.primerApellido.inputValue}").toString() : null);
            if(interesadoReq.getPrimerApellido()!=null){
                if(interesadoReq.getPrimerApellido().length()>100){
                    interesadoReq.setPrimerApellido(interesadoReq.getPrimerApellido().substring(0,100));
                }
            }
            interesadoReq.setSegundoApellido(getElObjectFromBindings("#{bindings.segundoApellido.inputValue}") != null ?
                                             getElObjectFromBindings("#{bindings.segundoApellido.inputValue}").toString() : null);
            if(interesadoReq.getSegundoApellido()!=null){
                if(interesadoReq.getSegundoApellido().length()>100){
                    interesadoReq.setSegundoApellido(interesadoReq.getSegundoApellido().substring(0,100));
                }
            }
            interesadoReq.setEmail(getElObjectFromBindings("#{bindings.email1.inputValue}").toString());
            if(interesadoReq.getEmail().length()>200){
                interesadoReq.setEmail(interesadoReq.getEmail().substring(0,200));
            }
            interesadoReq.setTelefono(getElObjectFromBindings("#{bindings.telefono.inputValue}") != null ?
                                     getElObjectFromBindings("#{bindings.telefono.inputValue}").toString() : null);
            if(interesadoReq.getTelefono()!=null){
                if(interesadoReq.getTelefono().length()>20){
                    interesadoReq.setTelefono(interesadoReq.getTelefono().substring(0,20));
                }
            }
            interesadoReq.setCelular(getElObjectFromBindings("#{bindings.celular.inputValue}") != null ?
                                     getElObjectFromBindings("#{bindings.celular.inputValue}").toString() : null);
            if(interesadoReq.getCelular()!=null){
                if(interesadoReq.getCelular().length()>20){
                    interesadoReq.setCelular(interesadoReq.getCelular().substring(0,20));
                }
            }
            interesadoReq.setDireccion(getElObjectFromBindings("#{bindings.direccion.inputValue}").toString());
            if(interesadoReq.getDireccion().length()>200){
                interesadoReq.setDireccion(interesadoReq.getDireccion().substring(0,200));
            }
            
            if(getElObjectFromBindings("#{bindings.idPais.inputValue}") == null){
                log.debug("idpais null");
            }else if (getElObjectFromBindings("#{bindings.idDepartamento.inputValue}") == null){
                log.debug("departamento null");
            }else if (getElObjectFromBindings("#{bindings.idMunicipio.inputValue}") == null){
                log.debug("municipio null");
            }else {
                log.debug(getElObjectFromBindings("#{bindings.idPais.inputValue}").toString());
                log.debug(getElObjectFromBindings("#{bindings.idDepartamento.inputValue}").toString());
                log.debug(getElObjectFromBindings("#{bindings.idMunicipio.inputValue}").toString());
                
                }
            
            interesadoReq.setIdPais(Integer.parseInt(getElObjectFromBindings("#{bindings.idPais.inputValue}")
                                                     .toString()));
            interesadoReq.setIdDepartamento(getElObjectFromBindings("#{bindings.idDepartamento.inputValue}") != null ?
                                            Integer.parseInt(getElObjectFromBindings("#{bindings.idDepartamento.inputValue}")
                                                               .toString()) : null);
            interesadoReq.setIdMunicipio(getElObjectFromBindings("#{bindings.idMunicipio.inputValue}") != null ?
                                         Integer.parseInt(getElObjectFromBindings("#{bindings.idMunicipio.inputValue}")
                                                            .toString()) : null);
            interesadoReq.setCiudad(getElObjectFromBindings("#{bindings.ciudad.inputValue}") != null ?
                                         getElObjectFromBindings("#{bindings.ciudad.inputValue}")
                                                          .toString() : null);
            
            interesadoReq.setTipoSolicitante(getElObjectFromBindings("#{bindings.tipoSolicitante.inputValue}") != null ?
                                         ((Number)getElObjectFromBindings("#{bindings.tipoSolicitante.inputValue}")).longValue()
                                                           : null);
            log.debug(user + " -> TRACK 3");
            procComunicacionRequest.getInteresadosList().add(interesadoReq);
            
            
            /************************************************************/

            //Guardar anexos no digitalizables
            if (lstAnexo.size() > 0) {
                // SgdAnexoComunciacion anexo;
                for (Anexo reg : lstAnexo) {  
                    AnexoComunicacionRequest anexoReq = new AnexoComunicacionRequest();
                    anexoReq.setDescripcion(reg.getDescripcion());
                    if(anexoReq.getDescripcion().length()>200){
                        anexoReq.setDescripcion(anexoReq.getDescripcion().substring(0,200));
                    }
                    anexoReq.setNroRadicado(getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString());
                    anexoReq.setCantidad(new BigDecimal(reg.getCantidad()));
                    procComunicacionRequest.getAnexosList().add(anexoReq);
                    log.debug(this.user+" | registrando anexo");
                }
            }

            log.debug(user + " -> Invocacion WccResponse");
            WccResponse respCom = this.comunicaciones.registrarComunicacion(procComunicacionRequest);
            log.debug(user + " -> respCom Code: " + respCom.getStatusCode());
            log.debug(user + " -> respCom Message: " + respCom.getStatusMessage());
            log.debug(user + " -> respCom Metadata: " + respCom.getMetaDataFields());
            
            if(!respCom.getStatusCode().equals("0"))
                return Boolean.FALSE;
            
        } catch (Exception e) {
            log.info("Exception registrarComunicacionBD",e);
        }
        log.info("END registrarComunicacionBD");
        return Boolean.TRUE;
    }
    
    
    
    

    public void generarRadicado(ActionEvent ae) {
        log.info(user + " -> BEGIN generarRadicado");
        try {
            if(!generarNumeroRadicado())
                return;
                
            //Copiar valor cantidad de anexos
            log.debug(user + " -> lstAnexo size(): " + lstAnexo.size());
            setElObjectIntoBinding("#{bindings.nroAnexos.inputValue}", lstAnexo.size());
            log.debug(user + " -> nroAnexos: " + getElObjectFromBindings("#{bindings.nroAnexos.inputValue}"));

            if(!registrarComunicacionBD())
                return;
           
            // se asume a esta instancia como radicada
            this.comRadicada = Boolean.TRUE;

           //Radicar en CMC si aplica
            if(getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString().equals("true"))
                generarRadicadoCMC();
            
            // se llama el metodo save
            //this.save();
           

        } catch (Exception e) {
            log.error(user + " -> Exception generarRadicado", e);
        }
        log.info(user + " -> FIN generarRadicado");
    }
    

    public String generarSticker() {

        try {
            log.debug(user + " -> lstAnexo size: " + lstAnexo.size());
            log.debug(user+" -> hora: " + new Date().toString());
        } catch (Exception e) {
            log.error("exception", e);
        }
        return "";
    }

    public void generarRadicadoCMC() {
        log.info(user+" -> INICIO generarRadicadoCMC");
        try {
            if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") != null) {
                if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString().equals("false")) {
                    mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                                   "La comunicaci�n debe estar relacionada con un expediente minero " +
                                   " para generar un radicado en CMC. Por favor verificar.");

                    return;
                } else {
                    if (getElObjectFromBindings("#{bindings.placa.inputValue}") == null) {
                        mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                                       "Debe digitar un n�mero de placa. Por favor verificar.");

                        return;
                    }
                    Integer codUnidadCmc = 0;
                    if (getElObjectFromBindings("#{bindings.codDependencia.inputValue}") == null) {
                        mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                                       "Debe seleccionar una dependencia. Por favor verificar.");

                        return;
                    } else {
                        /************************************************
                 * Actualizado por: Adrian Molina - adrimol@gmail.com
                 * Fecha: 06Abril2017
                 * *****************************************/
                        //Obtener codigo de la dependencia segun catastro con el codigo dependencia
                        //Utiliza nueva columna "COD_CMC" en tabla "ANM_UNIDADADMINISTRATIVA_TB"
                        Integer codUnidad =
                            Integer.parseInt(getElObjectFromBindings("#{bindings.codDependencia.inputValue}")
                                             .toString());
                        log.debug("codUnidad: " + codUnidad);
                        List<AnmUnidadAdministrativaTb> lstUnidadAdmin =
                            sessionEJB.getAnmUnidadAdministrativaTbFindByCodigo(codUnidad);
                        log.debug("lstUnidadAdmin: " + lstUnidadAdmin.size());
                        if (lstUnidadAdmin.size() > 0) {
                            codUnidadCmc = lstUnidadAdmin.get(0).getCodCmc();
                        } else {
                            log.debug("No se obtuvieron Unidades Admin. en base de datos con el c�digo dependencia: " +
                                      codUnidad);
                        }
                    }
                    log.debug("codUnidadCmc: " + codUnidadCmc);
                    List<Anexos> anexosList = new ArrayList<Anexos>();

                    Anexos anexo1 = new Anexos();
                    anexo1.setNombre("NOMBREDOCUMENTO");
                    anexo1.setValor(getElObjectFromBindings("#{bindings.placa.inputValue}").toString());
                    anexosList.add(anexo1);

                    Anexos anexo2 = new Anexos();
                    anexo2.setNombre("ASUNTO");
                    anexo2.setValor(getElObjectFromBindings("#{bindings.asunto.inputValue}").toString());
                    anexosList.add(anexo2);

                    Anexos anexo3 = new Anexos();
                    anexo3.setNombre("DEPENDENCIA");
                    anexo3.setValor(codUnidadCmc.toString());
                    anexosList.add(anexo3);

                    Anexos anexo4 = new Anexos();
                    anexo4.setNombre("IDENTIFICADORDOCUMENTO");
                    anexo4.setValor("112"); //COMUNICACIONES DEL TITULAR A LA AUTORIDAD MINERA - REGISTRO MINERO NACIONAL
                    anexosList.add(anexo4);

                    try {
                        String usuario = getElObjectFromBindings("#{bindings.idUsuario.inputValue}").toString();
                        log.debug("usuario: " + usuario);
                        String codExpediente = getElObjectFromBindings("#{bindings.placa.inputValue}").toString();
                        log.debug("codExpediente: " + codExpediente);
                        log.debug("anexosList size: " + anexosList.size());

                        //Invocar proxy WS
                        SGDWebServiceLocator locator = new SGDWebServiceLocator();
                        IntegracionSGDCMC cmc = locator.getCmcProxy();
                        Respuesta respuesta = cmc.radicarComunicacionCmc(usuario, codExpediente, anexosList);

                        log.debug("Resultado getCodigoError: " + respuesta.getCodigoError());
                        log.debug("Resultado getDocumentoRadicacion: " + respuesta.getDocumentoRadicacion());
                        log.debug("Resultado getError: " + respuesta.getError());
                        log.debug("Resultado getNumeroRadicado: " + respuesta.getNumeroRadicado());

                        CMCHandler handler = ((CMCHandlerResolver) locator.getCMCHandlerResolver()).getHandler();
                        respuesta = handler.getRespuesta();

                        log.debug("Resultado getCodigoError: " + respuesta.getCodigoError());
                        log.debug("Resultado getDocumentoRadicacion: " + respuesta.getDocumentoRadicacion());
                        log.debug("Resultado getError: " + respuesta.getError());
                        log.debug("Resultado getNumeroRadicado: " + respuesta.getNumeroRadicado());
                        log.debug("Resultado ubicacion doc: " + respuesta.getRutaUbicacionDocumentoRadicacion());
                        /************************************************/
                        /*
                        IntegracionSGDCMC proxy = new SGDWebServiceLocator().getCmcProxy();
                        log.debug("proxy: "+proxy);
                        Respuesta respuesta = proxy.radicarComunicacionCmc(usuario,codExpediente,anexosList);
                        log.debug("respuesta: "+respuesta);
                        log.debug("Codigo Error: "+respuesta.getCodigoError());
                        */
                        if (respuesta.getCodigoError().equals("-1")) {
                            /*setElObjectIntoBinding("#{bindings.rutaDocCMC.inputValue}",
                                                   respuesta.getRutaUbicacionDocumentoRadicacion() +
                                                   respuesta.getDocumentoRadicacion()
                                                   .substring(0, respuesta.getDocumentoRadicacion().lastIndexOf('.')));*/
                            setElObjectIntoBinding("#{bindings.rutaDocCMC.inputValue}",
                                                           respuesta.getRutaUbicacionDocumentoRadicacion() +
                                                           respuesta.getDocumentoRadicacion());
                            setElObjectIntoBinding("#{bindings.nombreDocCMC.inputValue}",
                                                   respuesta.getDocumentoRadicacion());
                            setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "S");

                            log.debug("rutaDocCMC: " + getElObjectFromBindings("#{bindings.rutaDocCMC.inputValue}"));
                            log.debug("nombreDocCMC: " +
                                      getElObjectFromBindings("#{bindings.nombreDocCMC.inputValue}"));
                            log.debug("radicadoCMCHecho: " +
                                      getElObjectFromBindings("#{bindings.radicadoCMCHecho.inputValue}"));
                                                                                    
                            /* se copia en ftp/comunicaciones con el objetivo de agregarlo en el virtual directory
                             * esto permite exponer el documento en el link para el end user
                             * */
                            File file =
                                new File(respuesta.getRutaUbicacionDocumentoRadicacion() +
                                         respuesta.getDocumentoRadicacion());
                            log.debug("file name: " + file.getName());
                            //log.debug("virtualDirectory: " + getElObjectFromBindings("#{bindings.virtualDirectory.inputValue}"));

                            if (file.renameTo(new File(rutaFisica + file.getName()))) {
                                log.debug(user + " -> INFO radicadoCMC copiado");
                                setElObjectIntoBinding("#{bindings.urlDocCMC.inputValue}",
                                                       getElObjectFromBindings("#{bindings.virtualDirectory.inputValue}") +
                                                       respuesta.getDocumentoRadicacion());

                            } else {
                                log.error(user + " -> ERROR copiando radicadoCMC");

                                setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "N");
                                setElObjectIntoBinding("#{bindings.urlDocCMC.inputValue}", null);
                                
                                mostrarMensaje(FacesMessage.SEVERITY_WARN, "El documento de radicado de CMC no pudo ser generado.");

                                return;
                            }
                            log.debug("urlDocCMC: " + getElObjectFromBindings("#{bindings.urlDocCMC.inputValue}"));
                            //btnRadicadoCMC.setDisabled(true);
                            //AdfFacesContext.getCurrentInstance().addPartialTarget(btnRadicadoCMC);

                            mostrarMensaje(FacesMessage.SEVERITY_INFO, "El radicado de CMC fue generado exitosamente");
                        } else {
                            setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "N");

                            log.error(user + " -> ERROR generarRadicadoCMC codigo: " + respuesta.getCodigoError());
                            log.error(user + " -> ERROR generarRadicadoCMC mensaje: " + respuesta.getError());

                            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Se gener� un error en la radicaci�n en CMC");

                            return;
                        }
                    } catch (Exception e) {
                        setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "N");
                        log.error(user + " -> ERROR generarRadicadoCMC", e);
                        mostrarMensaje(FacesMessage.SEVERITY_WARN, "Se gener� un error en la radicacion en CMC");
                        return;
                    }
                }
            } else {
                setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "N");
                mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                               "La comunicaci�n debe estar relacionada con un expediente minero " +
                               " para generar un radicado en CMC. Por favor verificar.");
                return;
            }     
        } catch (Exception e) {
            setElObjectIntoBinding("#{bindings.radicadoCMCHecho.inputValue}", "N");
            log.error(user + " -> ERROR generarRadicadoCMC", e);
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Se gener� un error en la radicaci�n en CMC");
            return;
        }
        log.info(user+" -> FIN generarRadicadoCMC");
    }
    


    public void aprobar(ActionEvent actionEvent) {
        log.info(user + " -> INICIO aprobar");
        try {
            //Continuar ejecucion flujo
            setOperation(actionEvent);
        } catch (Exception e) {
            log.error(user + " -> Exception aprobar", e);
        }
        log.info(user + " -> FIN aprobar");
    }


    public void cambiarPais(ValueChangeEvent vce) {
        log.info("BEGIN cambiarPais");
        try {
            //Capturar codigo pais seleccionado y validar si es Colombia (52)
            log.debug(user + " -> Codigo pais: " + vce.getNewValue().toString());
            log.debug(user + " -> codPais: " + codPais);
            setElObjectIntoBinding("#{bindings.idPais.inputValue}", vce.getNewValue());
            lstDepartamento.clear();
            lstMunicipio.clear();

            log.debug(user + " " + getElObjectFromBindings("#{bindings.idPais.inputValue}"));
            if (vce.getNewValue()
                   .toString()
                   .equals("52")) { //Codigo Colombia
                List<SgdDepartamento> lstDptos = sessionEJB.getSgdDepartamentoFindAll();
                log.debug(user + " -> lstDptos size: " + lstDptos.size());
                for (SgdDepartamento reg : lstDptos) {
                    lstDepartamento.add(new SelectItem(reg.getCodigo(), reg.getNombre()));
                }

            }
            log.debug(user + " -> lstDepartamento size: " + lstDepartamento.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarPais", e);
        }
        log.info("END cambiarPais");
    }


    public void cambiarDepartamento(ValueChangeEvent vce) {
        log.info("BEGIN cambiarDepartamento");
        try {
            //Capturar codigo departamneto seleccionado
            log.debug(user + " -> Codigo dpto: " + vce.getNewValue().toString());
            log.debug(user + " -> codDepartamento: " + codDepartamento);
            setElObjectIntoBinding("#{bindings.idDepartamento.inputValue}", vce.getNewValue());
            lstMunicipio.clear();
            if (vce.getNewValue() != null) {
                List<SgdMunicipio> lstMunic =
                    sessionEJB.getSgdMunicipioFindByDepartamento(Long.parseLong(vce.getNewValue().toString()));
                log.debug(user + " -> lstMunic size: " + lstMunic.size());
                for (SgdMunicipio reg : lstMunic)
                    lstMunicipio.add(new SelectItem(Integer.parseInt(reg.getCodigo()), reg.getNombre()));
            }
            log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarDepartamento", e);
        }
        log.info("END cambiarDepartamento");
    }


    public void cambiarMunicipio(ValueChangeEvent vce) {
        log.info("BEGIN cambiarMunicipio");
        try {
            //Capturar codigo departamneto seleccionado
            log.debug(user + " -> Codigo municipio: " + vce.getNewValue().toString());
            setElObjectIntoBinding("#{bindings.idMunicipio.inputValue}", vce.getNewValue());
            //Obtener nombre del municipio para payload
            String ciudad = "";
            Long idMunic = Long.parseLong(getElObjectFromBindings("#{bindings.idMunicipio.inputValue}").toString());
            List<SgdMunicipio> lstMunic = sessionEJB.getSgdMunicipioFindById(idMunic);
            if (lstMunic.size() > 0) {
                ciudad = lstMunic.get(0).getNombre();
            } else
                log.debug(user + " -> No se obtuvo nombre de ciudad (" + idMunic + ")");
            log.debug(user + " -> ciudad: " + ciudad);
            setElObjectIntoBinding("#{bindings.ciudad.inputValue}", ciudad);

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarMunicipio", e);
        }
        log.info("END cambiarMunicipio");
    }


    public void cambiarExpMinero(ValueChangeEvent vce) {
        log.info("BEGIN cambiarExpMinero");
        try {
            log.debug(user + " -> vce: " + vce.getNewValue().toString());
            setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", vce.getNewValue());
            //Limpiar campo numero placa
            if(vce.getNewValue().toString().equals("true")){
                log.debug(user + " -> Precargar tramites");
                precargarTramites();
            }else{
                log.debug(user + " -> Precargar taxonomia");
                precargarTaxonomia();
                setElObjectIntoBinding("#{bindings.placa.inputValue}", "");
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarExpMinero", e);
        }
        log.info("END cambiarExpMinero");
    }


    public void cambiarTieneReferencia(ValueChangeEvent vce) {
        log.info("BEGIN cambiarTieneReferencia");
        try {

            // por ahora que no haga nada... es una prueba
            setElObjectIntoBinding("#{bindings.tieneReferencia.inputValue}", vce.getNewValue());


        } catch (Exception e) {
            log.error(user + " -> Exception cambiarEsPqrs", e);
        }
        log.info("END cambiarTieneReferencia");
    }


    public void cambiarEsPqrs(ValueChangeEvent vce) {
        log.info("BEGIN cambiarEsPqrs");
        try {
            log.debug(user + " -> vce: " + vce.getNewValue().toString());
            setElObjectIntoBinding("#{bindings.esPqrsBO.inputValue}", vce.getNewValue());

           // if (getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}") != null) {
                if (vce.getNewValue()
                       .toString()
                       .equals("true")) {
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "S");
                    itDias.setDisabled(true);
                } else {
                    setElObjectIntoBinding("#{bindings.requiereRespuestaDO.inputValue}", "N");
                    itDias.setValue(0);
                }
                log.debug(user + " -> requiereRespuestaDO: " +
                          getElObjectFromBindings("#{bindings.requiereRespuestaDO.inputValue}"));
            
            if (!this.tituloPantalla.contains("Crear Comunicaci")){
                // AdfFacesContext.getCurrentInstance().addPartialTarget(itDias);
                
                }
                
                
            //}

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarEsPqrs", e);
        }
        log.info("END cambiarEsPqrs");
    }

    public void cambiarCategoria(ValueChangeEvent evt) {
        log.info("BEGIN cambiarCategoria");
        try {
            lstExpediente.clear();
            lstSubserie.clear();
            lstSerie.clear();
            lstProceso.clear();
            log.debug(user + " -> cambiarCategoria(): " + evt.getNewValue());
            log.debug(user + " -> cambiarCategoria(): " + evt.getNewValue().getClass());
            //Setear valor categoria
            setElObjectIntoBinding("#{bindings.idCatProceso.inputValue}", evt.getNewValue());
            //Setear nombre de la categoria
            for (folderfolders f : listaCategoria) {
                if (f.getFFOLDERGUID().equals(evt.getNewValue().toString()))
                    setElObjectIntoBinding("#{bindings.catProceso.inputValue}", f.getFFOLDERNAME()); //));
            }
            log.debug(user + " -> catProceso: " + getElObjectFromBindings("#{bindings.catProceso.inputValue}"));

            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.folderGUID.inputValue}", evt.getNewValue());

            //Obtener lista de procesos
            listaProceso = sessionEJB.getfolderfoldersFindByPArentGuid(evt.getNewValue().toString());
            log.debug(user + " -> listaProceso size: " + listaProceso.size());
            for (folderfolders f : listaProceso) {
                lstProceso.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstProceso size: " + lstProceso.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarCategoria", e);
        }
        log.info("END cambiarCategoria");
    }

    public void cambiarProceso(ValueChangeEvent evt) {
        log.info("BEGIN cambiarProceso");
        try {
            lstExpediente.clear();
            lstSubserie.clear();
            lstSerie.clear();
            log.debug(user + " -> cambiarProceso(): " + evt.getNewValue());
            log.debug(user + " -> cambiarProceso(): " + evt.getNewValue().getClass());
            //Setear valor proceso
            setElObjectIntoBinding("#{bindings.idProceso.inputValue}", evt.getNewValue());

            //Setear nombre del proceso
            for (folderfolders f : listaProceso) {
                if (f.getFFOLDERGUID().equals(evt.getNewValue().toString()))
                    setElObjectIntoBinding("#{bindings.proceso.inputValue}", f.getFFOLDERNAME()); //));
            }
            log.debug(user + " -> proceso: " + getElObjectFromBindings("#{bindings.proceso.inputValue}"));

            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.folderGUID.inputValue}", evt.getNewValue());

            //Obtener lista de series
            listaSerie = sessionEJB.getfolderfoldersFindByPArentGuid(evt.getNewValue().toString());
            log.debug(user + " -> listaSerie size: " + listaSerie.size());
            for (folderfolders f : listaSerie) {
                lstSerie.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstSerie size: " + lstSerie.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarProceso", e);
        }
        log.info("END cambiarProceso");
    }


    public void cambiarSerie(ValueChangeEvent evt) {
        log.info("BEGIN cambiarSerie");
        try {
            lstExpediente.clear();
            lstSubserie.clear();
            log.debug(user + " -> cambiarSerie(): " + evt.getNewValue());
            log.debug(user + " -> cambiarSerie(): " + evt.getNewValue().getClass());
            //Setear valor serie
            setElObjectIntoBinding("#{bindings.idSerie.inputValue}", evt.getNewValue());

            //Setear nombre de la serie
            for (folderfolders f : listaSerie) {
                if (f.getFFOLDERGUID().equals(evt.getNewValue().toString()))
                    setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFFOLDERNAME());
            }
            log.debug(user + " -> serie: " + getElObjectFromBindings("#{bindings.serie.inputValue}"));

            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.folderGUID.inputValue}", evt.getNewValue());

            //Obtener lista de subseries
            listaSubserie = sessionEJB.getfolderfoldersFindByPArentGuid(evt.getNewValue().toString());
            log.debug(user + " -> listaSubserie size: " + listaSubserie.size());
            for (folderfolders f : listaSubserie) {
                lstSubserie.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstSubserie size: " + lstSubserie.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarSerie", e);
        }
        log.info("END cambiarSerie");
    }


    public void cambiarSubserie(ValueChangeEvent evt) {
        log.info("BEGIN cambiarSubserie");
        try {
            lstExpediente.clear();
            log.debug(user + " -> cambiarSubserie(): " + evt.getNewValue());
            log.debug(user + " -> cambiarSubserie(): " + evt.getNewValue().getClass());
            //Setear valor subserie
            setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", evt.getNewValue());

            //Setear nombre de la subserie
            for (folderfolders f : listaSubserie) {
                if (f.getFFOLDERGUID().equals(evt.getNewValue().toString()))
                    setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFFOLDERNAME());
            }
            log.debug(user + " -> subserie: " + getElObjectFromBindings("#{bindings.subserie.inputValue}"));

            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.folderGUID.inputValue}", evt.getNewValue());

            //Obtener lista de expedientes
            listaExpediente = sessionEJB.getfolderfoldersFindByPArentGuid(evt.getNewValue().toString());
            log.debug(user + " -> listaExpediente size: " + listaExpediente.size());
            for (folderfolders f : listaExpediente) {
                lstExpediente.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            }
            log.debug(user + " -> lstExpediente size: " + lstExpediente.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarSubserie", e);
        }
        log.info("END cambiarSubserie");
    }

    public void cambiarExpediente(ValueChangeEvent evt) {
        log.info("BEGIN cambiarExpediente");
        try {
            lstExpediente.clear();
            log.debug(user + " -> cambiarExpediente(): " + evt.getNewValue());
            log.debug(user + " -> cambiarExpediente(): " + evt.getNewValue().getClass());
            //Setear valor cuaderno
            setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", evt.getNewValue());

            //Setear nombre del expediente
            for (folderfolders f : listaExpediente) {
                if (f.getFFOLDERGUID().equals(evt.getNewValue().toString()))
                    setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", f.getFFOLDERNAME());
            }
            log.debug(user + " -> cuaderno: " + getElObjectFromBindings("#{bindings.cuaderno.inputValue}"));

            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.folderGUID.inputValue}", evt.getNewValue());

            //Obtener lista de expedientes
            lstExpediente.clear();
            for (folderfolders f : listaExpediente)
                lstExpediente.add(new SelectItem(f.getFFOLDERGUID(), f.getFFOLDERNAME()));
            log.debug(user + " -> lstExpediente size: " + lstExpediente.size());
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarExpediente", e);
        }
        log.info("END cambiarExpediente");
    }


    /******************************************************************************/
    /************************* ARCHIVO PDF ******************************************/

    /******************************************************************************/

    //PDF variables
    private PdfContentByte contentByte; // = docWriter.getDirectContent();
    private static String FILE = "Sticker.pdf";

/*
    public void generatePDF(FacesContext facesContext, java.io.OutputStream outputStream) {
        this.generatePDFFile(facesContext, outputStream); // Generate PDF File
        this.downloadPDF(facesContext, outputStream); // Download PDF File
    }
*/

/*
    private void generatePDFFile(FacesContext facesContext,

        java.io.OutputStream outputStream) {
        try {
            log.debug(user + " -> In Generate PDF................");
            Rectangle pagesize = new Rectangle(420f, 100f);
            //Rectangle pagesize = new Rectangle(800f, 600f);
            Document document = new Document(pagesize, 10f, 10f, 5f, 5f);
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addContent(document);
            //addContent(document);
            document.close();
            log.debug(user + " -> End of PDF......................");
            facesContext = facesContext.getCurrentInstance();
            ServletContext context = (ServletContext) facesContext.getExternalContext().getContext();
            System.out.println(context.getRealPath("/"));
            File file = new File(FILE);
            FileInputStream fdownload;
            byte[] b;
            log.debug(user + " -> getCanonicalPath: " + file.getCanonicalPath());
            log.debug(user + " -> getAbsolutePath: " + file.getAbsolutePath());
            fdownload = new FileInputStream(file);

            int n;
            while ((n = fdownload.available()) > 0) {
                b = new byte[n];
                int result = fdownload.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error(user + " -> Exception generatePDFFile", e);
        }
    }
*/

/*
    private static void addMetaData(Document document) {
        document.addTitle("ANM - Sticker");
        //document.addSubject("Using iText");
        //document.addKeywords("Java, PDF, iText");
        document.addAuthor("Adrian Molina");
        document.addCreator("Adrian Molina");
    }*/

/*
    private void addContent(Document document) throws DocumentException {
        try {
            Font normalFont = FontFactory.getFont("Times-Roman", 10);
            Font grandeFont = FontFactory.getFont("Times-Roman", 14);
            Font negritaFont = FontFactory.getFont("Times-Roman", 10, Font.BOLD);
            String spaces = "               ";

            Image logo = Image.getInstance("logo_anm_small.png");
            logo.scaleToFit(90, 90);
            logo.setAlignment(Image.LEFT | Image.TEXTWRAP);
            document.add(logo);

            Paragraph linea1 = new Paragraph(new SimpleDateFormat("dd/MMM/yy hh:mm a").format(new Date()), normalFont);
            //linea1.add(new Chunk(spaces+" Exp.: ", negritaFont));
            //linea1.add(new Chunk(spaces+spaces+"Fol. ", normalFont));
            linea1.setAlignment(Element.ALIGN_RIGHT);
            document.add(linea1);

            Paragraph linea2 =
                new Paragraph("Destino: " + getElObjectFromBindings("#{bindings.nombre2.inputValue}").toString(),
                              normalFont);
            document.add(linea2);

            //Linea 3 - Codigo barras
            Barcode128 code128 = new Barcode128();
             code128.setCode("123456789");
             code128.setCodeType(Barcode128.CODE128);
             Image code128Image = code128.createImageWithBarcode(contentByte, null, null);
             code128Image.setAbsolutePosition(10,700);
             code128Image.scalePercent(125);
             document.add(code128Image);
            
            Paragraph linea4 =
                new Paragraph("No. " + getElObjectFromBindings("#{bindings.nroRadicado.inputValue}").toString(),
                              grandeFont);
            linea4.setAlignment(Element.ALIGN_LEFT);
            document.add(linea4);

            if (getElObjectFromBindings("#{bindings.placa.inputValue}") != null) {
                Paragraph linea5 =
                    new Paragraph("Placa minera: " + getElObjectFromBindings("#{bindings.placa.inputValue}").toString(),
                                  normalFont);
                document.add(linea5);
            }
            if (getElObjectFromBindings("#{bindings.nroFolios.inputValue}") != null) {
                Paragraph linea6 =
                    new Paragraph("Folios: " + getElObjectFromBindings("#{bindings.nroFolios.inputValue}").toString(),
                                  normalFont);
                document.add(linea6);
            }
            log.debug(user + " -> lstAnexo size: " + lstAnexo.size());
            if (lstAnexo.size() > 0) {
                Paragraph linea7 =
                    new Paragraph("Anexos: " + lstAnexo.size() + " - Desc: " + lstAnexo.get(0).getDescripcion(),
                                  normalFont);
                document.add(linea7);
            }

        } catch (Exception e) {
            log.error(user + " -> Exception addContent", e);
        }
    }
*/


    /**
     * Method to create barcode image of type Barcode39 for mytext
     */
    /**
     * Code 39 character set consists of barcode symbols representing
     * characters 0-9, A-Z, the space character and the following symbols:
     * - . $ / + %
     */
    /*
    public Image createBarCode39(String myText) {
       
        Barcode39 myBarCode39 = new Barcode39();
        myBarCode39.setCode(myText);
        myBarCode39.setStartStopText(false);
        Image myBarCodeImage39 = myBarCode39.createImageWithBarcode(contentByte, null, null);
        return myBarCodeImage39;
    }
*/

/*
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
*/

/*
    private void downloadPDF(FacesContext facesContext,

        java.io.OutputStream outputStream) {
        try {
            facesContext = facesContext.getCurrentInstance();
            ServletContext context = (ServletContext) facesContext.getExternalContext().getContext();
            ExternalContext ctx = facesContext.getExternalContext();
            HttpServletResponse res = (HttpServletResponse) ctx.getResponse();
            res.setContentType("application/pdf");
            System.out.println(context.getRealPath("/"));
            File file = new File(FILE);
            FileInputStream fdownload;
            byte[] b;
            fdownload = new FileInputStream(file);
            int n;
            while ((n = fdownload.available()) > 0) {
                b = new byte[n];
                int result = fdownload.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void reasignarActionListener(ActionEvent ae) {
        setOperation(ae);
    }

    public void aprobarGestionCom(ActionEvent ae) {
        log.info(user + " -> INICIO aprobarGestionCom");
        try {
            log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));
            
            Boolean esTitluloMinero = Boolean.parseBoolean(getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString()); 
            String serieDoc = (String)getElObjectFromBindings("#{bindings.idSerie.inputValue}");
            
            log.debug(user + " -> esTitluloMinero: " + esTitluloMinero);
            log.debug(user + " -> serieDoc: " + serieDoc);
            
            if ( !esTitluloMinero && (serieDoc==null||serieDoc.trim().equals("")) ){
                log.error(user + " -> Error, no se selecciono taxonomia: " + serieDoc);
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Debe seleccionar una Taxonom�a valida que incluya una serie documental.");
                return;
            }
            
            setOperation(ae);

        } catch (Exception e) {
            log.error(user + " -> Exception aprobarGestionCom", e);
        } catch (Throwable t) {
            log.error(user + " -> Throwable aprobarGestionCom", t);
        }
        log.info(user + " -> FIN aprobarGestionCom");
    }


    public void aprobarAsignacionUsuario(ActionEvent actionEvent) {
        log.info(user + " -> INICIO aprobarAsignacionUsuario");
        try {
            //Validar reasignacion
            log.debug(user + " -> tipoAsignacionDO: " +
                      getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}"));
            if (getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}") != null) {
                log.debug(user + " -> codDependencia: " +
                          getElObjectFromBindings("#{bindings.codDependencia.inputValue}"));
                log.debug(user + " -> idUsuario: " + getElObjectFromBindings("#{bindings.idUsuario.inputValue}"));

                if (getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}").toString().equals("D")) {
                    if (getElObjectFromBindings("#{bindings.codDependencia.inputValue}") == null) {
                        mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                       "Debe seleccionar una dependencia del listado para continuar.");
                        return;
                    }
                }
                if (getElObjectFromBindings("#{bindings.tipoAsignacionDO.inputValue}").toString().equals("U")) {
                    if (getElObjectFromBindings("#{bindings.idUsuario.inputValue}") == null) {
                        mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                       "Debe seleccionar un usuario del listado para continuar.");
                        return;
                    }
                }
            }
            //Continuar ejecucion flujo
            setOperation(actionEvent);

        } catch (Exception e) {
            log.error(user + " -> Exception aprobarAsignacionUsuario", e);
        }
        log.info(user + " -> FIN aprobarAsignacionUsuario");
    }


    
    /******************************************************************************/
    /************************* UTILIDADES ******************************************/

    /******************************************************************************/


    private Object getElObjectFromBindings(String expr) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ValueBinding vb = fc.getApplication().createValueBinding(expr);
        return vb.getValue(fc);
    }

    private static void setElObjectIntoBinding(String expr, Object valor) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ExpressionFactory elFactory = facesCtx.getApplication().getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
        ve.setValue(elContext, valor);
    }


    /**
     * Programmatic invocation of a method that an EL evaluates to.
     *
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the parameters
     * @param params Array of Object defining the values of the parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

        return exp.invoke(elContext, params);
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


    protected void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        try {
            FacesMessage message = new FacesMessage(severity, mensaje, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            log.error(user + " -> Exception mostrarMensaje", e);
        }
    }

    /**
     *Metodo que se encarga de guardar el payload de un jsp
     */
    public void save() {
        log.info(user + " | BEGIN | save()");
        try {
            // guarda los bindings
            BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
            OperationBinding method = bindings.getOperationBinding("update");
            if (method != null) {
                method.execute();
            }
            /// Se invoca el set operation del boton save para refrescar la pagina
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            //cb1 is the fully qualified name of the button
            RichButton button = (RichButton) root.findComponent("save");
            ActionEvent actionEvent = new ActionEvent(button);
            setOperation(actionEvent);
            log.info(user + " -> getPhaseId: "+actionEvent.getPhaseId());
            //actionEvent.queue();
            // we are queuing the all action programmaticly

        } catch (Exception e) {
            log.error(user + " | Exception | save()", e);
        }
        log.info(user + " | END | save()");
    }


    /******************************************************************************/
    /************************* SETTERS - GETTERS ******************************************/

    /******************************************************************************/


    public void setSgdUsuario(SgdUsuario sgdUsuario) {
        this.sgdUsuario = sgdUsuario;
    }

    public SgdUsuario getSgdUsuario() {
        return sgdUsuario;
    }

    public void setAnmUnidad(AnmUnidadAdministrativaTb anmUnidad) {
        this.anmUnidad = anmUnidad;
    }

    public AnmUnidadAdministrativaTb getAnmUnidad() {
        return anmUnidad;
    }

    public void setLstPlantilla(List<AnmPlantilla> lstPlantilla) {
        this.lstPlantilla = lstPlantilla;
    }

    public List<AnmPlantilla> getLstPlantilla() {
        return lstPlantilla;
    }

    public void setLstDestinatario(List<Interesado> lstDestinatario) {
        this.lstDestinatario = lstDestinatario;
    }

    public List<Interesado> getLstDestinatario() {
        return lstDestinatario;
    }

    public void setTplantilla(RichTable tplantilla) {
        this.tplantilla = tplantilla;
    }

    public RichTable getTplantilla() {
        return tplantilla;
    }

    public void setTdestinatario(RichTable tdestinatario) {
        this.tdestinatario = tdestinatario;
    }

    public RichTable getTdestinatario() {
        return tdestinatario;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setNuevoDestinatario(Interesado nuevoDestinatario) {
        this.nuevoDestinatario = nuevoDestinatario;
    }

    public Interesado getNuevoDestinatario() {
        return nuevoDestinatario;
    }

    public void setSelectedDestinatario(Interesado selectedDestinatario) {
        this.selectedDestinatario = selectedDestinatario;
    }

    public Interesado getSelectedDestinatario() {
        return selectedDestinatario;
    }

    public void setItFecha(RichInputText itFecha) {
        this.itFecha = itFecha;
    }

    public RichInputText getItFecha() {
        return itFecha;
    }

    public void setLstDependencias(List<SelectItem> lstDependencias) {
        this.lstDependencias = lstDependencias;
    }

    public List<SelectItem> getLstDependencias() {
        return lstDependencias;
    }

    public void setTieneReferencia(Boolean tieneReferencia) {
        this.tieneReferencia = tieneReferencia;
    }

    public Boolean getTieneReferencia() {
        return tieneReferencia;
    }

    public void setLstAnexo(List<Anexo> lstAnexo) {
        this.lstAnexo = lstAnexo;
    }

    public List<Anexo> getLstAnexo() {
        return lstAnexo;
    }

    public void setTanexo(RichTable tanexo) {
        this.tanexo = tanexo;
    }

    public RichTable getTanexo() {
        return tanexo;
    }

    public void setSelectedAnexo(Anexo selectedAnexo) {
        this.selectedAnexo = selectedAnexo;
    }

    public Anexo getSelectedAnexo() {
        return selectedAnexo;
    }

    public void setNuevoAnexo(Anexo nuevoAnexo) {
        this.nuevoAnexo = nuevoAnexo;
    }

    public Anexo getNuevoAnexo() {
        return nuevoAnexo;
    }

    public void setComReasignada(String comReasignada) {
        this.comReasignada = comReasignada;
    }

    public String getComReasignada() {
        return comReasignada;
    }

    public void setLstTiposDocum(List<SelectItem> lstTiposDocum) {
        this.lstTiposDocum = lstTiposDocum;
    }

    public List<SelectItem> getLstTiposDocum() {
        return lstTiposDocum;
    }

    public void setLstTramites(List<SelectItem> lstTramites) {
        this.lstTramites = lstTramites;
    }

    public List<SelectItem> getLstTramites() {
        return lstTramites;
    }

    public void setBtnEnviar(RichButton btnEnviar) {
        this.btnEnviar = btnEnviar;
    }

    public RichButton getBtnEnviar() {
        return btnEnviar;
    }

    public void setBtnSticker(RichButton btnSticker) {
        this.btnSticker = btnSticker;
    }

    public RichButton getBtnSticker() {
        return btnSticker;
    }

    public void setItNoRadicado(RichOutputText itNoRadicado) {
        this.itNoRadicado = itNoRadicado;
    }

    public RichOutputText getItNoRadicado() {
        return itNoRadicado;
    }

    public void setBtnRadicado(RichButton btnRadicado) {
        this.btnRadicado = btnRadicado;
    }

    public RichButton getBtnRadicado() {
        return btnRadicado;
    }

    public void setLstTiposIdentificacion(List<SelectItem> lstTiposIdentificacion) {
        this.lstTiposIdentificacion = lstTiposIdentificacion;
    }

    public List<SelectItem> getLstTiposIdentificacion() {
        return lstTiposIdentificacion;
    }

    public void setLstPais(List<SelectItem> lstPais) {
        this.lstPais = lstPais;
    }

    public List<SelectItem> getLstPais() {
        return lstPais;
    }

    public void setLstDepartamento(List<SelectItem> lstDepartamento) {
        this.lstDepartamento = lstDepartamento;
    }

    public List<SelectItem> getLstDepartamento() {
        return lstDepartamento;
    }

    public void setLstMunicipio(List<SelectItem> lstMunicipio) {
        this.lstMunicipio = lstMunicipio;
    }

    public List<SelectItem> getLstMunicipio() {
        return lstMunicipio;
    }

    public void setCodPais(Long codPais) {
        this.codPais = codPais;
    }

    public Long getCodPais() {
        return codPais;
    }

    public void setCodDepartamento(Integer codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public Integer getCodDepartamento() {
        return codDepartamento;
    }

    public void setLstTipoSolicitud(List<SelectItem> lstTipoSolicitud) {
        this.lstTipoSolicitud = lstTipoSolicitud;
    }

    public List<SelectItem> getLstTipoSolicitud() {
        return lstTipoSolicitud;
    }

    public void setItDias(RichInputText itDias) {
        this.itDias = itDias;
    }

    public RichInputText getItDias() {
        return itDias;
    }

    public void setSocReqRespuesta(RichSelectOneChoice socReqRespuesta) {
        this.socReqRespuesta = socReqRespuesta;
    }

    public RichSelectOneChoice getSocReqRespuesta() {
        return socReqRespuesta;
    }

    public void setLstCategoria(List<SelectItem> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public List<SelectItem> getLstCategoria() {
        return lstCategoria;
    }

    public void setLstProceso(List<SelectItem> lstProceso) {
        this.lstProceso = lstProceso;
    }

    public List<SelectItem> getLstProceso() {
        return lstProceso;
    }

    public void setLstSerie(List<SelectItem> lstSerie) {
        this.lstSerie = lstSerie;
    }

    public List<SelectItem> getLstSerie() {
        return lstSerie;
    }

    public void setLstSubserie(List<SelectItem> lstSubserie) {
        this.lstSubserie = lstSubserie;
    }

    public List<SelectItem> getLstSubserie() {
        return lstSubserie;
    }

    public void setLstExpediente(List<SelectItem> lstExpediente) {
        this.lstExpediente = lstExpediente;
    }

    public List<SelectItem> getLstExpediente() {
        return lstExpediente;
    }

    public void setItPlaca(RichInputText itPlaca) {
        this.itPlaca = itPlaca;
    }

    public RichInputText getItPlaca() {
        return itPlaca;
    }

    public void setLstPais2(List<SelectItem> lstPais2) {
        this.lstPais2 = lstPais2;
    }

    public List<SelectItem> getLstPais2() {
        return lstPais2;
    }

    public void setLstDepartamento2(List<SelectItem> lstDepartamento2) {
        this.lstDepartamento2 = lstDepartamento2;
    }

    public List<SelectItem> getLstDepartamento2() {
        return lstDepartamento2;
    }

    public void setLstMunicipio2(List<SelectItem> lstMunicipio2) {
        this.lstMunicipio2 = lstMunicipio2;
    }

    public List<SelectItem> getLstMunicipio2() {
        return lstMunicipio2;
    }

    public void setBtnRadicadoCMC(RichButton btnRadicadoCMC) {
        this.btnRadicadoCMC = btnRadicadoCMC;
    }

    public RichButton getBtnRadicadoCMC() {
        return btnRadicadoCMC;
    }

    public void setEsPqrs(Boolean esPqrs) {
        this.esPqrs = esPqrs;
    }

    public Boolean getEsPqrs() {
        return esPqrs;
    }

    public void setLstDependencias2(List<SelectItem> lstDependencias2) {
        this.lstDependencias2 = lstDependencias2;
    }

    public List<SelectItem> getLstDependencias2() {
        return lstDependencias2;
    }

    public void setItFechaVencimiento(RichInputText itFechaVencimiento) {
        this.itFechaVencimiento = itFechaVencimiento;
    }

    public RichInputText getItFechaVencimiento() {
        return itFechaVencimiento;
    }

    public void setLstUsuariosObj(List<SelectItem> lstUsuariosObj) {
        this.lstUsuariosObj = lstUsuariosObj;
    }

    public List<SelectItem> getLstUsuariosObj() {
        return lstUsuariosObj;
    }

    public void setComRadicada(Boolean comRadicada) {
        this.comRadicada = comRadicada;
    }

    public Boolean getComRadicada() {
        return comRadicada;
    }

    public void setBtnSave(RichButton btnSave) {
        this.btnSave = btnSave;
    }

    public RichButton getBtnSave() {
        return btnSave;
    }

    public void setDependenciaDestino(String dependenciaDestino) {
        this.dependenciaDestino = dependenciaDestino;
    }

    public String getDependenciaDestino() {
        return dependenciaDestino;
    }
    
    public ArrayList<SelectItem> getTiposSolicitantes() {
        return tiposSolicitantes;
    }

    public void setTiposSolicitantes(ArrayList<SelectItem> tiposSolicitantes) {
        this.tiposSolicitantes = tiposSolicitantes;
    }
}
