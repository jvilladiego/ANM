package bean;


import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.WccResponse;
import co.gov.anm.sgd.util.SGDWebServiceLocator;

import enums.TipoEnvio;

import java.io.OutputStream;
import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletResponse;

import jersey.repackaged.com.google.common.io.ByteStreams;

import model.AnmPlantilla;
import model.AnmTipodtalseguntramite;
import model.AnmTramiteTb;
import model.AnmUnidadAdministrativaTb;
import model.Destinatario;
import model.SessionEJBLocal4;
import model.SgdDepartamento;
import model.SgdMunicipio;
import model.SgdTipoIdentificacion;
import model.SgdUsuario;
import model.folderfolders;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.bpel.services.workflow.task.model.AttachmentTypeImpl;
import oracle.bpel.services.workflow.worklist.adf.DocMgmtBean;
import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.commons.io.IOUtils;

import utils.ADFUtils;


@ManagedBean(name = "operacionBean")
@ViewScoped
public class OperacionBean implements Serializable {
    @SuppressWarnings("compatibility:5456603943559077402")
    private static final long serialVersionUID = 1L;
    @EJB
    private SessionEJBLocal4 sessionEJB;

    private Logger log;
    private String user;
    private SgdUsuario sgdUsuario;
    private AnmUnidadAdministrativaTb anmUnidad;
    private List<AnmPlantilla> lstPlantilla = new ArrayList<AnmPlantilla>();
    private List<Destinatario> lstDestinatario = new ArrayList<Destinatario>();
    private RichTable tplantilla;

    private RichTable tdestinatario;
    private Date fechaActual;
    private Destinatario nuevoDestinatario = new Destinatario();
    private Destinatario selectedDestinatario;

    //Crear comunicacion
    private List<SelectItem> lstTiposIdentificacion = new ArrayList<SelectItem>();
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
    private List<SelectItem> lstMunicipio = new ArrayList<SelectItem>();

    private List<SelectItem> lstDepartamento = new ArrayList<SelectItem>();
    private List<SelectItem> lstDepartamento2 = new ArrayList<SelectItem>();

    private List<SgdDepartamento> lstDptos = new ArrayList<SgdDepartamento>();
    private List<SgdMunicipio> lstMunic = new ArrayList<SgdMunicipio>();

    //Variables
    private Boolean esPqrs = Boolean.FALSE;
    private Boolean tieneReferencia = Boolean.FALSE;
    private Boolean lstMunicFlag = Boolean.FALSE;
    private Boolean getNombreMunicipioEjecutado = Boolean.FALSE;
    private Boolean getNombreDepartamentoEjecutado = Boolean.FALSE;

    //Tipo de env�o
    private List<SelectItem> listaTiposEnvio = new ArrayList<SelectItem>();
    private RichInputFile ifAnexo;

    private String message = "";

    public OperacionBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN OperacionBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error(user + " -> Exception OperacionBean", e);
        }
        log.info("END filcoBean");
    }


    @PostConstruct
    public void initComponents() {
        log.info("BEGIN initComponents");
        try {
            //Fecha actual
            fechaActual = new Date();
            //Setear fecha radicacion si aplica
            log.debug(user + " -> fechaRadicacion: " +
                      getElObjectFromBindings("#{bindings.fechaRadicacion.inputValue}"));
            if (getElObjectFromBindings("#{bindings.fechaRadicacion.inputValue}") == null)
                setElObjectIntoBinding("#{bindings.fechaRadicacion.inputValue}", new Timestamp(fechaActual.getTime()));

            //Inicializar campo esTituloMinero
            log.debug(user + " -> esTituloMinero: " + getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}"));
            if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") == null)
                setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);
            else if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString().equals("N"))
                setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.FALSE);
            else if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString().equals("S"))
                setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", Boolean.TRUE);
            log.debug(user + " -> esTituloMinero: " + getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}"));

            //Cargar plantillas
            lstPlantilla = sessionEJB.getAnmPlantillaFindAll();
            log.debug(user + " -> lstPlantilla size: " + lstPlantilla.size());
            //Cargar usuario
            List<SgdUsuario> lstUsuario = sessionEJB.getSgdUsuarioFindById(user);
            log.debug(user + " -> lstUsuario size: " + lstUsuario.size());
            if (lstUsuario.size() > 0) {
                sgdUsuario = lstUsuario.get(0);
                log.debug(user + " -> Cod.Dependencia: " + sgdUsuario.getCodigoDependencia());
                //Cargar dependencia
                Integer depend = sgdUsuario.getCodigoDependencia().intValue();
                log.debug(user + " -> depend: " + depend);
                //Setear codigo dependencia
                setElObjectIntoBinding("#{bindings.codDependencia.inputValue}", depend);
                List<AnmUnidadAdministrativaTb> lstAnmUnidad =
                    sessionEJB.getAnmUnidadAdministrativaTbFindByCodigo(depend);
                log.debug(user + " -> lstAnmUnidad size: " + lstAnmUnidad.size());
                if (lstAnmUnidad.size() > 0) {
                    anmUnidad = lstAnmUnidad.get(0);
                    log.debug(user + " -> anmUnidad: " + anmUnidad.getNombreUnidadadministrativa());
                } else
                    log.debug(user + " -> No se encontr� la unidad con codigo: " + sgdUsuario.getCodigoDependencia());

                //Consultar tipos identificacion
                log.debug(user + " -> lstTiposIdentificacion size: " + lstTiposIdentificacion.size());
                if (lstTiposIdentificacion.size() == 0) {
                    List<SgdTipoIdentificacion> listaTiposIden = sessionEJB.getSgdTipoIdentificacionFindAll();
                    log.debug(user + " -> listaTiposIden size: " + listaTiposIden.size());
                    lstTiposIdentificacion.clear();
                    for (SgdTipoIdentificacion reg : listaTiposIden) {
                        lstTiposIdentificacion.add(new SelectItem(reg.getCodigo().toString(), reg.getNombre()));
                    }
                    log.debug(user + " -> lstTiposIdentificacion size: " + lstTiposIdentificacion.size());
                }

                //Consultar departamentos
                this.lstDptos = sessionEJB.getSgdDepartamentoFindAll();
                log.debug(user + " -> lstDptos size: " + lstDptos.size());
                for (SgdDepartamento reg : lstDptos) {
                    lstDepartamento.add(new SelectItem(reg.getIdDepartamento().intValue(), reg.getNombre()));
                    //lstDepartamento2.add(new SelectItem(Integer.parseInt(reg.getCodigo()), reg.getNombre()));
                }
                log.debug(user + " -> lstDepartamento size: " + lstDepartamento.size());

                // esto al parecer no funcionó
                //                // Precargar municipios
                //                // TODO
                //                if (getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}") != null) {
                //                    log.debug("#{row.bindings.idDepartamento.inputValue} not null :) ");
                //                    this.precargarMunicipios();
                //                }else {
                //                    log.debug("#{row.bindings.idDepartamento.inputValue} = null ");
                //                    }


            } else {
                log.debug(user + " -> No se encontr� el usuario ");
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "El usuario autenticado no se encontr� en base de datos.");
            }

            if (getElObjectFromBindings("bindings.esPqrs.inputValue") != null &&
                getElObjectFromBindings("bindings.esPqrs.inputValue").toString().equals("true")) {
                esPqrs = Boolean.TRUE;
            } else {
                esPqrs = Boolean.FALSE;
            }

            // cargue de listas Gestion Documental
            if (getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}") != null) {
                if (new Boolean(getElObjectFromBindings("#{bindings.esTituloMinero.inputValue}").toString())) {
                    log.info(user + " | Es titulo minero, se precargan tramites y tipos documentales");
                    this.precargarMetadatos();

                } else {
                    //jucjimenezmo: Cuando ya se selecciono la taxonomia
                    log.info(user + " | No es titulo minero, se precarga taxonomia");
                    this.precargarTaxonomia();
                }
            }


        } catch (Exception e) {
            log.error(user + " -> Exception initComponents", e);
        }
        log.info("END initComponents");
    }


    public void selectDestinatario(SelectionEvent selEvent) {
        log.info(user + " -> BEGIN selectDestinatario");
        try {
            //Get selection source
            RichTable rtDest = (RichTable) selEvent.getSource();
            //Get selected row
            selectedDestinatario = (Destinatario) rtDest.getSelectedRowData();
            log.info(user + " -> selectedDestinatario: " + selectedDestinatario.getNombre());

        } catch (Exception e) {
            log.error(user + " -> Exception selectDestinatario", e);
        }
        log.info(user + " -> FIN selectDestinatario");
    }

    public void borrarRegistro(ActionEvent ae) {
        log.info(user + " -> BEGIN borrarRegistro");
        try {
            lstDestinatario.remove(selectedDestinatario);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tdestinatario);
        } catch (Exception e) {
            log.error(user + " -> Exception borrarRegistro", e);
        }
        log.info(user + " -> FIN borrarRegistro");
    }

    public void agregarDestinatario(DialogEvent de) {
        log.info(user + " -> BEGIN agregarDestinatario");
        try {
            lstDestinatario.add(nuevoDestinatario);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tdestinatario);
            nuevoDestinatario = new Destinatario();
        } catch (Exception e) {
            log.error(user + " -> Exception agregarDestinatario", e);
        }
        log.info(user + " -> FIN agregarDestinatario");
    }

    public void editarDestinatario(DialogEvent de) {
        log.info(user + " -> BEGIN editarDestinatario");
        try {
            AdfFacesContext.getCurrentInstance().addPartialTarget(tdestinatario);
        } catch (Exception e) {
            log.error(user + " -> Exception editarDestinatario", e);
        }
        log.info(user + " -> FIN editarDestinatario");
    }


    public void cambiarPlaca(ValueChangeEvent evt) {
        log.info("BEGIN cambiarPlaca");
        try {
            log.debug("Inicio: cambiarPlaca(): " + evt.getNewValue());
            log.debug("Inicio: cambiarPlaca(): " + evt.getNewValue().getClass());
            String placa = evt.getNewValue().toString();
            validarPlaca(placa);

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarPlaca", e);
        }
        log.info("END cambiarPlaca");
    }


    private Boolean validarPlaca(String placa) {
        log.info(user + " -> INICIO validarPlaca");
        try {
            //Invocar proxy WS
            ExpedienteMinero proxyExpMinero = new SGDWebServiceLocator().getExpedienteMineroProxy();
            //Construir solicitud
            WccResponse response = proxyExpMinero.validarExpedienteMinero(placa);
            log.debug(user + " -> response.getStatusCode(): " + response.getStatusCode());
            if (response.getStatusCode().equals("0")) {
                return Boolean.TRUE;
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "El expediente minero \"" + placa +
                               "\" actualmente no se encuentre registrado en el SGD. Se enviara notificación al Administrador");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error(user + " -> Exception validarPlaca", e);
        }
        log.info(user + " -> FIN validarPlaca");
        return Boolean.FALSE;
    }


    protected void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        try {
            FacesMessage message = new FacesMessage(severity, mensaje, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            log.error(user + " -> Exception mostrarMensaje", e);
        }
    }


    public void validarForm(ActionEvent action) {
        log.info("Inicio validarForm()");
        try {
            //Validar destinatarios
            if (!validarDestinatarios()) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "Debe agregar al menos un destinatario");
            //} else if (!validarAttachment()) {
              //  mostrarMensaje(FacesMessage.SEVERITY_WARN, "Debe adjuntar al menos un archivo (documento principal)");
                } else if (!validarExpMinero()) {
                    mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                   "Debe ingresar un Expediente Minero, de lo contrario desmarque la casilla");
                    } else if (!validarGestionDocumental()) {
                        mostrarMensaje(FacesMessage.SEVERITY_WARN, this.message);
                        } else if(getElObjectFromBinding("#{bindings.name1.inputValue}")==null){
                            mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                           "Debe adjuntar el documento principal para continuar, por favor verifique.");
                            }else{
                                setOperation(action);
                                log.debug("End success validarForm()");   
                            }
            //Validacion para extension archivo       
            /*DocMgmtBean docMngr = (DocMgmtBean) getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            for (AttachmentTypeImpl attachment : docMngr.getAddedAttachmentList()) {
                log.debug(attachment.getMimeType());
            }
            if (docMngr.getAddedAttachmentList().size() > 0 && 
                !docMngr.getAddedAttachmentList().get(0).getMimeType().equals("application/vnd.oasis.opendocument.text")) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "La extensi�n del documento debe ser 'odt'. Por favor verificar.");
            } else {
                setOperation(action);
                log.debug("End success validarForm()");   
            }*/
        } catch (Exception e) {
            log.error("Error validarForm() ", e);
        }
    }

    @SuppressWarnings("deprecation")
    protected boolean validarDestinatarios() {
        log.info("Inicio validarDestinatarios()");
        try {
            //TO DO
            int cantidadDestinatarios;

            if (getElObjectFromBinding("#{bindings.interesados}") != null) {
                FacesCtrlHierBinding interesados_binding =
                    (FacesCtrlHierBinding) getElObjectFromBinding("#{bindings.interesados}");
                //log.debug("dest.getCollectionModel() "+ interesados_binding.getCollectionModel().getClass());

                cantidadDestinatarios = interesados_binding.getCollectionModel().getRowCount();
                log.debug(user + " -> validarDestinatarios() : cantidadDestinatarios : " + cantidadDestinatarios);

            } else {
                log.error(user + " -> validarDestinatarios() : \"#{bindings.interesados}\" = null ");
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe agregar al menos un destinatario");
                cantidadDestinatarios = 0;
            }

            if (cantidadDestinatarios == 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Error validarDestinatarios()", e);
            return false;
        }


    }

    protected boolean validarAttachment() {

        log.info("Inicio validarAttachment()");
        try {
            DocMgmtBean docMngr = (DocMgmtBean) getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
            int cantidadAttachments = docMngr.getDocList().size();
            log.debug("Cantidad de attachements: " + cantidadAttachments);
            if (cantidadAttachments == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error("Error validarAttachment() ", e);
            return false;
        }
    }


    protected boolean validarExpMinero() {
        log.info("Init validarExpMinero()");
        try {
            if (new Boolean(getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}").toString())) {
                log.info("Enter if esTituloMinero true");
                if (getElObjectFromBinding("#{bindings.placa.inputValue}").toString().isEmpty()) {
                    return false;
                } else {
                    log.debug("Expediente minero marcado y con placa valida");
                    return true;
                }
            } else {
                log.debug("Expediente no marcado. Contiue normal");
                return true;
            }
        } catch (Exception e) {
            log.error("validarExpMinero() " + e);
            return false;
        }
    }


    protected boolean validarGestionDocumental() {

        log.debug("BEGIN | validarGestionDocumental()");

        log.debug("#{bindings.esTituloMinero.inputValue} : " +
                  getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}").toString());

        //obtener expMinero
        boolean expMinero =
            Boolean.parseBoolean(getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}").toString());
        log.debug("validarGestionDocumental() | expMinero : " + expMinero);

        if (expMinero) {
            //valida tramite y tipo != null
            if (getElObjectFromBinding("#{bindings.idTramite.inputValue}") != null &&
                getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}") != null) {
                return true;
            } else {
                this.message = "Seleccione Trámite y Tipo Documental para continuar";
                return false;
            }
        } else {
            //valida taxonomia
            if (getElObjectFromBinding("#{bindings.idCatProceso.inputValue}") != null &&
                getElObjectFromBinding("#{bindings.idProceso.inputValue}") != null &&
                getElObjectFromBinding("#{bindings.idSerie.inputValue}") != null) {
                    log.debug(" | validarGestionDocumental() | cat-pro-ser :  NOT null ;  subseriesSet.isEmpty() : " +
                              listaSubserie.isEmpty());
                    return true;
                /*
                  if (this.listaSubserie.isEmpty()) {
                    //no requiere subserie
                    return true;
                } else if (getElObjectFromBinding("#{bindings.idSubSerie.inputValue}") != null) {
                    //si requiere subserie y está diligenciada
                    if (this.listaExpediente.isEmpty()) {
                        //no requiere Expediente
                        return true;
                    } else if (getElObjectFromBinding("#{bindings.idCuaderno.inputValue}") != null) {
                        //si requiere Expediente y esta diligenciado
                        return true;
                    } else {
                        //cuaderno no diligenciado
                        this.message = "Seleccione 'Expediente' para continuar";
                        return false;
                    }
                } else {
                    //subserie no diligenciada
                    this.message = "Seleccione subserie para continuar";
                    return false;
                }
                  */
            } else {
                //Gestion documental no diligenciada
                this.message = "Debe diligenciar la sección de Gestión Documental";
                return false;
            }
        }
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
               limpiarAdjunto();
           } else {
               //Revisar content Type del archivo
               //10 Octubre de 2017 -> Se restringe a archivos ODT unicamente
               if(file.getContentType().contains("vnd.oasis.opendocument.text")){
                //file.getContentType().contains("vnd.openxmlformats-officedocument.wordprocessingml.document")){
                   //Maximo 20 Mb
                   if (file.getLength() > 20971520) {
                       mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                      "El archivo debe ser menor a 20Mb, por favor seleccione otro para continuar.");
                       limpiarAdjunto();
                   } else {
                       //Modificar el nombre del archivo Doc.Principal para agregar el numero de la instancia
                       String nombreDoc = file.getFilename();
                       String parts[] = nombreDoc.split("\\.(?=[^\\.]+$)");
                       String nombreModificado = parts[0]+"_"+getElObjectFromBinding("#{bindings.instanceId.inputValue}").toString()+"."+parts[1];
                       log.debug(user+" nombre modificado: "+nombreModificado);
                       setElObjectIntoBinding("#{bindings.name1.inputValue}", nombreModificado);
                       log.debug(user+" -> name1: "+getElObjectFromBinding("#{bindings.name1.inputValue}"));
                       //Setear demas variables del adjunto
                       setElObjectIntoBinding("#{bindings.mimeType2.inputValue}", file.getContentType());
                       setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                       byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                       setElObjectIntoBinding("#{bindings.content1.inputValue}", java.util.Base64.getEncoder().encodeToString(bytes));
                   }
                   log.debug(user + " -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
               }else{
                   mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                  "La extensi�n del archivo es correcta pero el contenido invalido, por favor actualice el archivo.");
                   limpiarAdjunto();
               }
           }
       } catch (Exception e) {
            log.error(user + " -> Exception cambiarDocPpal", e);
        }
        log.info("END cambiarDocPpal");
    }
    
    
    
    public void cambiarSoporteEntrega(ValueChangeEvent vce){
        log.info("BEGIN cambiarSoporteEntrega");
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

           if (!esValidoPdf(fileExtn)) {
               mostrarMensaje(FacesMessage.SEVERITY_WARN,
                              "Solo se permiten archivos 'Pdf', por favor actualice el archivo.");
               limpiarAdjunto();
           } else {
               //Revisar content Type del archivo
               if(file.getContentType().contains("application/pdf")){
                   //Maximo 20 Mb
                   if (file.getLength() > 20971520) {
                       mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                      "El archivo debe ser menor a 20Mb, por favor seleccione otro para continuar.");
                       limpiarAdjunto();
                   } else {
                       //Modificar el nombre del soporte para agregar el numero de la instancia
                       String nombreDoc = file.getFilename();
                       log.debug(user+" nombre original: "+nombreDoc);
                       //String parts[] = nombreDoc.split("\\.(?=[^\\.]+$)");
                       //String nombreModificado = parts[0]+"_"+getElObjectFromBinding("#{bindings.instanceId.inputValue}").toString()+"."+parts[1];
                       String nombreModificado = "guia_" + getElObjectFromBinding("#{bindings.nroRadicado.inputValue}").toString() + ".pdf";
                       log.debug(user+" nombre modificado: "+nombreModificado);
                       setElObjectIntoBinding("#{bindings.name1.inputValue}", nombreModificado);
                       log.debug(user+" -> name1: "+getElObjectFromBinding("#{bindings.name1.inputValue}"));
                       //Setear demas variables del adjunto
                       log.debug(user+" getContentType: "+file.getContentType());
                       setElObjectIntoBinding("#{bindings.mimeType1.inputValue}", file.getContentType());
                       log.debug(user+" getLength: "+file.getLength());
                       setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                       byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                       log.debug(user+" bytes: "+java.util.Base64.getEncoder().encodeToString(bytes));
                       setElObjectIntoBinding("#{bindings.content1.inputValue}", java.util.Base64.getEncoder().encodeToString(bytes));
                   }
                   log.debug(user + " -> nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
               }else{
                   mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                  "La extensi�n del archivo es correcta pero el contenido invalido, por favor actualice el archivo.");
                   limpiarAdjunto();
               }
           }
       } catch (Exception e) {
            log.error(user + " -> Exception cambiarSoporteEntrega", e);
        }
        log.info("END cambiarSoporteEntrega");
    }
    
    
    
    public void limpiarAdjunto(){
        log.info("BEGIN limpiarAdjunto");    
        try {
           ifAnexo.resetValue();
           AdfFacesContext.getCurrentInstance().addPartialTarget(ifAnexo);
       } catch (Exception e) {
            log.error(user + " -> Exception limpiarAdjunto", e);
        }
        log.info("END limpiarAdjunto");    
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
    
    private boolean esValidoPdf(String fileExtn) {
        if (fileExtn.equals("pdf"))
            return true;
        else
            return false;
    }

    

    /**         metodos value change event        **/

    public void cambiarDepartamento(ValueChangeEvent vce) {
        log.info("BEGIN cambiarDepartamento");
        try {
            // Capturar ID departamneto seleccionado
            log.debug(user + " -> ID dpto: " + vce.getNewValue());
            if (vce.getNewValue() != null) {
                log.debug(user + " -> ID dpto class: " + vce.getNewValue().getClass());
                setElObjectIntoBinding("#{row.bindings.idDepartamento.inputValue}",
                                       new Long(vce.getNewValue().toString()));


                // seteo del nombre del departamento
                Long idDpto = Long.parseLong(vce.getNewValue().toString());
                log.debug("vccambiarDepartamento() | e.getNewValue().toString() : " + idDpto);
                String nombreDepartamento = this.findNombreDepartamentoById(idDpto);
                log.debug("cambiarDepartamento() | resultado busqueda nombre : " + nombreDepartamento);

                setElObjectIntoBinding("#{row.bindings.nombreDepartamento.inputValue}", nombreDepartamento);
                nombreDepartamento = getElObjectFromBinding("#{row.bindings.nombreDepartamento.inputValue}").toString();
                log.debug("cambiarDepartamento() | testNombreDepartamento : " + nombreDepartamento);

                lstMunicipio.clear();
                if (vce.getNewValue() != null) {
                    this.lstMunic =
                        sessionEJB.getSgdMunicipioFindByDepartamento(Long.parseLong(vce.getNewValue().toString()));
                    log.debug(user + " -> lstMunic size: " + lstMunic.size());
                    for (SgdMunicipio reg : lstMunic)
                        lstMunicipio.add(new SelectItem(reg.getIdMunicipio(), reg.getNombre()));
                }
                log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());
            }

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarDepartamento", e);
        }
        log.info("END cambiarDepartamento");
    }


    public void cambiarMunicipio(ValueChangeEvent vce) {
        log.info("BEGIN cambiarMunicipio");
        try {
            // Capturar id municipio seleccionado
            log.debug(user + " -> ID municipio: " + vce.getNewValue());
            if (vce.getNewValue() != null) {
                log.debug(user + " -> ID municipio class: " + vce.getNewValue().getClass());
                Long idMunicipioSelected = Long.parseLong(vce.getNewValue().toString());
                log.debug(user + "prueba objeto completo adrian --> id : " + idMunicipioSelected);
                setElObjectIntoBinding("#{row.bindings.idMunicipio.inputValue}", idMunicipioSelected);


                // seteo del nombre del municipio
                String nombreMunicipio = this.findNombreMunicipioById(idMunicipioSelected);
                log.debug("cambiarMunicipio() | resultado busqueda nombre : " + nombreMunicipio);
                setElObjectIntoBinding("#{row.bindings.nombreMunicipio.inputValue}", nombreMunicipio);
                nombreMunicipio = getElObjectFromBinding("#{row.bindings.nombreMunicipio.inputValue}").toString();
                log.debug("cambiarMunicipio() | row.bindings.nombreMunicipio.inputValue : " + nombreMunicipio);
            }

        } catch (Exception e) {
            log.error(user + " -> Exception cambiarMunicipio", e);
        }
        log.info("END cambiarMunicipio");
    }


    public void aprobar(ActionEvent ae) {
        log.info(user + " -> INICIO aprobar");
        try {
            //Continuar flujo
            setOperation(ae);
        } catch (Exception e) {
            log.error(user + " -> Exception aprobar", e);
        } catch (Throwable t) {
            log.error(user + " -> Throwable aprobar", t);
        }
        log.info(user + " -> FIN aprobar");
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
            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.nombre2.inputValue}", evt.getNewValue());

            //Setear nombre de la categoria
            for (folderfolders item : listaCategoria) {
                if (item.getFFOLDERGUID().equals(evt.getNewValue())) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.catProceso.inputValue}", item.getFFOLDERNAME());
                    break;
                }
            }

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
            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.nombre2.inputValue}", evt.getNewValue());

            //Setear nombre del proceso
            for (folderfolders item : listaProceso) {
                if (item.getFFOLDERGUID().equals(evt.getNewValue())) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.proceso.inputValue}", item.getFFOLDERNAME());
                    break;
                }
            }

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
            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.nombre2.inputValue}", evt.getNewValue());

            //Setear nombre de la serie
            for (folderfolders item : listaSerie) {
                if (item.getFFOLDERGUID().equals(evt.getNewValue())) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.serie.inputValue}", item.getFFOLDERNAME());
                    break;
                }
            }

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
            //Setear valor Folder GUID
            setElObjectIntoBinding("#{bindings.nombre2.inputValue}", evt.getNewValue());

            //Setear nombre de la subserie
            for (folderfolders item : listaSubserie) {
                if (item.getFFOLDERGUID().equals(evt.getNewValue())) {
                    log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                    setElObjectIntoBinding("#{bindings.subserie.inputValue}", item.getFFOLDERNAME());
                    break;
                }
            }

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
            if (evt.getNewValue() != null) {
                log.debug(user + " -> cambiarExpediente(): " + evt.getNewValue().getClass());
                //Setear valor Expediente
                setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", evt.getNewValue());
                //Setear valor Folder GUID
                setElObjectIntoBinding("#{bindings.nombre2.inputValue}", evt.getNewValue());

                //Setear nombre del Expediente
                for (folderfolders item : listaExpediente) {
                    if (item.getFFOLDERGUID().equals(evt.getNewValue())) {
                        log.debug(user + " -> item.getFFOLDERGUID()(): " + item.getFFOLDERGUID());
                        setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", item.getFFOLDERNAME());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarExpediente", e);
        }
        log.info("END cambiarExpediente");
    }

    public void cambiarTramite(ValueChangeEvent vce) {
        log.info("BEGIN cambiarTramite");
        try {
            log.debug(user + " -> Inicio: cambiarTramite(): " + vce.getNewValue());

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


    /*****************************************
     ************* METODOS PRECARGAS******************
     * ******************************************/

    private void precargarMunicipios() {
        log.info("BEGIN precargarMunicipios");

        try {
            log.debug(user + " -> row.idDepartamento: " +
                      getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}"));
            log.debug(user + " -> row.idDepartamento class: " +
                      getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}").getClass());
            log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());
            lstMunicipio.clear();
            List<SgdMunicipio> lstMunic =
                sessionEJB.getSgdMunicipioFindByDepartamento(new Long(getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}")
                                                                      .toString()));
            log.debug(user + " -> lstMunic size: " + lstMunic.size());
            for (SgdMunicipio reg : lstMunic)
                lstMunicipio.add(new SelectItem(reg.getIdMunicipio(), reg.getNombre()));
            log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());

            if (getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}") != null) {
                log.debug(user + " -> row.idMunicipio: " +
                          getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}"));
                log.debug(user + " -> row.idMunicipio class: " +
                          getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}").getClass());
            }

        } catch (Exception e) {
            log.error(user + " -> Exception getLstMunicipio", e);
        }

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

    private void precargarMetadatos() {
        log.info("BEGIN precargarMetadatos");

        try {
            //Consultar listado de tramites
            listaTramites = this.sessionEJB.getTramiteFindAll();
            for (AnmTramiteTb f : listaTramites) {
                lstTramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
            }
            log.debug(user + " -> lstTramites size: " + lstTramites.size());

            if (getElObjectFromBinding("#{bindings.idTramite.inputValue}") != null) {
                String tramiteSelected = (String) getElObjectFromBinding("#{bindings.idTramite.inputValue}");
                this.cargarTiposDocumentales(tramiteSelected);
            } else {
                log.debug("idTramite null");
            }
        } catch (Exception e) {
            log.error(user + " -> Exception precargarMetadatos", e);
        }
        log.info("END precargarMetadatos");
    }

    private void precargarTaxonomia() {
        log.info("BEGIN precargarTaxonomia");
        try {
            String idCatProceso = null;
            String idProceso = null;
            String idSerie = null;
            String idSubSerie = null;
            lstCategoria.clear();
            //Inicializar taxonomia
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


    /******************************************************************************/
    /************************* otras utilidades ******************************************/

    /******************************************************************************/


    private String findNombreDepartamentoById(Long id) {

        String nombreDepartamento = "";
        for (SgdDepartamento departamento : this.lstDptos) {
            if (departamento.getIdDepartamento().equals(id)) {
                nombreDepartamento = departamento.getNombre();
                log.debug("findNombreDepartamentoById() | enter if id : " + id);
                break;
            }
        }
        return nombreDepartamento;
    }

    private String findNombreMunicipioById(Long id) {

        log.debug(this.user + " | findNombreMunicipioById() ID : " + id);
        String nombreMunicipio = "NO-MUNICIPIO";
        // log.debug("findNombreMunicipioById | size lstMunic : "+lstMunic.size());
        for (SgdMunicipio municipio : this.lstMunic) {
            // log.debug("hola soy el id del municipio : " + municipio.getIdMunicipio());
            if (municipio.getIdMunicipio().equals(id)) {
                nombreMunicipio = municipio.getNombre();
                log.debug("findNombreMunicipioById() | enter if id : " + id);
                break;
            }
        }
        return nombreMunicipio;
    }


    /******************************************************************************/
    /************************* UTILIDADES ******************************************/

    /******************************************************************************/


    private Object getElObjectFromBindings(String expr) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ValueBinding vb = fc.getApplication().createValueBinding(expr);
        return vb.getValue(fc);
    }

    //jucjimenezmo
    protected Object getElObjectFromBinding(String expr) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
        Object res = ve.getValue(facesCtx.getELContext());
        return res;
    }

    /**
     *M�todo que se encarga de asignar la operaci�n correspondiente a un boton.
     *Este m�todo llama al metodo setOperation de la clase InvokeActionBean.
     * @param action
     */
    protected void setOperation(ActionEvent action) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        InvokeActionBean invokeActionBean =
            (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
        invokeActionBean.setOperation(action);
    }


    private static void setElObjectIntoBinding(String expr, Object valor) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesCtx.getELContext();
        ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
        ve.setValue(elContext, valor);
    }


    /*************** get nombre pais-municipio*********
     ************************************ */

    public String getNombreDepartamento() {
        log.info("BEGIN | getNombreDepartamento()");
        String nombreDepartamento = "NA";
        try {

            if (!this.getNombreDepartamentoEjecutado) {
                // si no se ha ejectado este metodo

                // seteo del nombre del departamento
                Long idDpto =
                    Long.parseLong(getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}").toString());
                log.debug("getNombreDepartamento() | idDpto : " + idDpto);
                nombreDepartamento = this.findNombreDepartamentoById(idDpto);
                log.debug("getNombreDepartamento() | resultado busqueda nombre : " + nombreDepartamento);

                setElObjectIntoBinding("#{row.bindings.nombreDepartamento.inputValue}", nombreDepartamento);
                nombreDepartamento = getElObjectFromBinding("#{row.bindings.nombreDepartamento.inputValue}").toString();
                log.debug("getNombreDepartamento() | nombreDepartamento (row.binding) : " + nombreDepartamento);

                // llenado lista de municipios
                /*
               lstMunicipio.clear();
                   this.lstMunic =
                       sessionEJB.getSgdMunicipioFindByDepartamento(idDpto);
                   log.debug(user + " -> lstMunic size: " + lstMunic.size());
                   for (SgdMunicipio reg : lstMunic)
                       lstMunicipio.add(new SelectItem(reg.getIdMunicipio(), reg.getNombre()));
               log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());        */

                this.getNombreDepartamentoEjecutado = Boolean.TRUE;
            }


        } catch (Exception e) {
            log.error("getNombreDepartamento | Exception", e);
        }
        return nombreDepartamento;
    }

    public String getNombreMunicipio() {
        log.info("BEGIN | getNombreMunicipio()");
        String nombreMunicipio = "NA";

        try {

            if (!this.getNombreMunicipioEjecutado) {
                // si no se ha ejecutado este metodo
                // seteo del nombre del municipio
                Long idMunicipioSelected =
                    Long.parseLong(getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}").toString());

                nombreMunicipio = this.findNombreMunicipioById(idMunicipioSelected);
                log.debug("getNombreMunicipio() | resultado busqueda nombre : " + nombreMunicipio);
                setElObjectIntoBinding("#{row.bindings.nombreMunicipio.inputValue}", nombreMunicipio);
                nombreMunicipio = getElObjectFromBinding("#{row.bindings.nombreMunicipio.inputValue}").toString();
                log.debug("getNombreMunicipio() | nombreMunicipio (row.bindings) : " + nombreMunicipio);

                this.getNombreMunicipioEjecutado = Boolean.TRUE;
            }


        } catch (Exception e) {
            log.error("getNombreMunicipio() | Exception", e);
        }

        return nombreMunicipio;
    }


    /******************************************************************************/
    /************************* SETTERS - GETTERS ******************************************/

    /******************************************************************************/


    public void setLstPlantilla(List<AnmPlantilla> lstPlantilla) {
        this.lstPlantilla = lstPlantilla;
    }

    public List<AnmPlantilla> getLstPlantilla() {
        return lstPlantilla;
    }

    public void setTplantilla(RichTable tplantilla) {
        this.tplantilla = tplantilla;
    }

    public RichTable getTplantilla() {
        return tplantilla;
    }

    public void setSgdUsuario(SgdUsuario sgdUsuario) {
        this.sgdUsuario = sgdUsuario;
    }

    public SgdUsuario getSgdUsuario() {
        return sgdUsuario;
    }

    public void setLstDestinatario(List<Destinatario> lstDestinatario) {
        this.lstDestinatario = lstDestinatario;
    }

    public List<Destinatario> getLstDestinatario() {
        return lstDestinatario;
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

    public void setNuevoDestinatario(Destinatario nuevoDestinatario) {
        this.nuevoDestinatario = nuevoDestinatario;
    }

    public Destinatario getNuevoDestinatario() {
        return nuevoDestinatario;
    }

    public void setAnmUnidad(AnmUnidadAdministrativaTb anmUnidad) {
        this.anmUnidad = anmUnidad;
    }

    public AnmUnidadAdministrativaTb getAnmUnidad() {
        return anmUnidad;
    }


    public void setSelectedDestinatario(Destinatario selectedDestinatario) {
        this.selectedDestinatario = selectedDestinatario;
    }

    public Destinatario getSelectedDestinatario() {
        return selectedDestinatario;
    }

    public void setLstCategoria(List<SelectItem> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public List<SelectItem> getLstCategoria() {
        return lstCategoria;
    }

    public void setListaCategoria(List<folderfolders> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<?> getListaCategoria() {
        return listaCategoria;
    }

    public void setLstProceso(List<SelectItem> lstProceso) {
        this.lstProceso = lstProceso;
    }

    public List<SelectItem> getLstProceso() {
        return lstProceso;
    }

    public void setListaProceso(List<folderfolders> listaProceso) {
        this.listaProceso = listaProceso;
    }

    public List<?> getListaProceso() {
        return listaProceso;
    }

    public void setLstSerie(List<SelectItem> lstSerie) {
        this.lstSerie = lstSerie;
    }

    public List<SelectItem> getLstSerie() {
        return lstSerie;
    }

    public void setListaSerie(List<folderfolders> listaSerie) {
        this.listaSerie = listaSerie;
    }

    public List<?> getListaSerie() {
        return listaSerie;
    }

    public void setLstSubserie(List<SelectItem> lstSubserie) {
        this.lstSubserie = lstSubserie;
    }

    public List<SelectItem> getLstSubserie() {
        return lstSubserie;
    }

    public void setListaSubserie(List<folderfolders> listaSubserie) {
        this.listaSubserie = listaSubserie;
    }

    public List<?> getListaSubserie() {
        return listaSubserie;
    }

    public void setLstExpediente(List<SelectItem> lstExpediente) {
        this.lstExpediente = lstExpediente;
    }

    public List<SelectItem> getLstExpediente() {
        return lstExpediente;
    }

    public void setListaExpediente(List<folderfolders> listaExpediente) {
        this.listaExpediente = listaExpediente;
    }

    public List<?> getListaExpediente() {
        return listaExpediente;
    }

    public void setLstTiposIdentificacion(List<SelectItem> lstTiposIdentificacion) {
        this.lstTiposIdentificacion = lstTiposIdentificacion;
    }

    public List<SelectItem> getLstTiposIdentificacion() {
        return lstTiposIdentificacion;
    }

    public void setLstMunicipio(List<SelectItem> lstMunicipio) {
        this.lstMunicipio = lstMunicipio;
    }

    public List<SelectItem> getLstMunicipio() {
        log.debug(user + " -> getLstMunicipio()");

        // la precarga del listado de municipios debe hacerse en este punto.
        // no esposible hacerla en el postconstruct dado que corresponde a un atributo de una lista

        try {

            if (getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}") != null) {
                log.debug(user + " -> row.idDepartamento: " +
                          getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}"));
                log.debug(user + " -> row.idDepartamento class: " +
                          getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}").getClass());

                // seteo nombre de departamento
                // this.setNombreDepartamento();

                // cargar listado de municipios
                log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());
                lstMunicipio.clear();
                this.lstMunic =
                    sessionEJB.getSgdMunicipioFindByDepartamento(new Long(getElObjectFromBindings("#{row.bindings.idDepartamento.inputValue}")
                                                                          .toString()));
                log.debug(user + " -> lstMunic size: " + this.lstMunic.size());
                for (SgdMunicipio reg : this.lstMunic)
                    lstMunicipio.add(new SelectItem(reg.getIdMunicipio().intValue(), reg.getNombre()));
                log.debug(user + " -> lstMunicipio size: " + lstMunicipio.size());
            }
            if (getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}") != null) {
                log.debug(user + " -> row.idMunicipio: " +
                          getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}"));
                log.debug(user + " -> row.idMunicipio class: " +
                          getElObjectFromBindings("#{row.bindings.idMunicipio.inputValue}").getClass());

                // set nombre municipio
                // this.setNombreMunicipio();
            }

        } catch (Exception e) {
            log.error(user + " -> Exception getLstMunicipio", e);
        }

        return lstMunicipio;
    }

    public void cambiarTipoEnvio(ValueChangeEvent evt) {
        log.debug("Inicio cambiarTipoEnvio():" + evt.getOldValue());

        String newValue = (String) evt.getNewValue();

        try {
            if (newValue != null) {
                for (TipoEnvio tp : TipoEnvio.values()) {
                    if (tp.getCodigoTipo().equals(newValue)) {
                        log.debug("setea tipo de envio: " + newValue);

                        setElObjectIntoBinding("#{bindings.nombreEnvio.inputValue}", tp.getNombreTipo());

                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarTipoEnvio", e);
        }
        log.debug("Fin cambiarTipoEnvio():" + evt.getNewValue());
    }

    public void cambiarEsTituloMinero(ValueChangeEvent evt) {
        log.debug("Inicio cambiarEsTituloMinero():" + evt.getNewValue());
        log.debug("placa :"+ADFUtils.getBoundAttributeValue("placa"));
        try {
            if (new Boolean(evt.getNewValue().toString())) {
                this.precargarMetadatos();
            } else {
                this.precargarTaxonomia();
                log.debug("cleaning placa field");
                ADFUtils.setBoundAttributeValue("placa", null);
            }
        } catch (Exception e) {
            log.error(user + " -> Exception cambiarEsTituloMinero", e);
        }
        log.debug("Fin cambiarEsTituloMinero():" + evt.getNewValue());
    }

    public void setLstDepartamento(List<SelectItem> lstDepartamento) {
        this.lstDepartamento = lstDepartamento;
    }

    public List<SelectItem> getLstDepartamento() {
        return lstDepartamento;
    }

    public void setLstDepartamento2(List<SelectItem> lstDepartamento2) {
        this.lstDepartamento2 = lstDepartamento2;
    }

    public List<SelectItem> getLstDepartamento2() {
        return lstDepartamento2;
    }

    public void setLstTiposDocum(List<SelectItem> lstTiposDocum) {
        this.lstTiposDocum = lstTiposDocum;
    }

    public List<SelectItem> getLstTiposDocum() {
        return lstTiposDocum;
    }

    public void setListaTiposDocum(List<AnmTipodtalseguntramite> listaTiposDocum) {
        this.listaTiposDocum = listaTiposDocum;
    }

    public List<AnmTipodtalseguntramite> getListaTiposDocum() {
        return listaTiposDocum;
    }

    public void setLstTramites(List<SelectItem> lstTramites) {
        this.lstTramites = lstTramites;
    }

    public List<SelectItem> getLstTramites() {
        return lstTramites;
    }

    public void setListaTramites(List<AnmTramiteTb> listaTramites) {
        this.listaTramites = listaTramites;
    }

    public List<AnmTramiteTb> getListaTramites() {
        return listaTramites;
    }

    public void setEsPqrs(Boolean esPqrs) {
        this.esPqrs = esPqrs;
    }

    public Boolean getEsPqrs() {
        return esPqrs;
    }

    public void setTieneReferencia(Boolean tieneReferencia) {
        this.tieneReferencia = tieneReferencia;
    }

    public Boolean getTieneReferencia() {
        return tieneReferencia;
    }

    public void setListaTiposEnvio(List<SelectItem> listaTiposEnvio) {
        this.listaTiposEnvio = listaTiposEnvio;
    }

    public List<SelectItem> getListaTiposEnvio() {
        return listaTiposEnvio;
    }

    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
