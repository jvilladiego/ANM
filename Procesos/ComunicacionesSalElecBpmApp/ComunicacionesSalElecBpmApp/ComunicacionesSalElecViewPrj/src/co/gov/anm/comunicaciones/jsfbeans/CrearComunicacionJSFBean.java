package co.gov.anm.comunicaciones.jsfbeans;


import co.gov.anm.comunicaciones.model.ejb.ModeloComunicacionesLocal3;
import co.gov.anm.comunicaciones.model.ejb.WebCenterContentLocal3;
import co.gov.anm.comunicaciones.model.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.model.entity.Cuenta;
import co.gov.anm.comunicaciones.model.entity.FolderFolders;
import co.gov.anm.comunicaciones.model.entity.SgdTipoIdentificacion;
import co.gov.anm.comunicaciones.model.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.model.entity.Tramite;
import co.gov.anm.comunicaciones.model.entity.UnidadAdministrativa;
import co.gov.anm.comunicaciones.model.type.TrueFalseType;
import co.gov.anm.sgd.service.ExpedienteMinero;
import co.gov.anm.sgd.service.WccResponse;
import co.gov.anm.sgd.util.SGDWebServiceLocator;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.ejb.EJB;

import javax.el.ELContext;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.bpel.services.workflow.worklist.adf.DocMgmtBean;
import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import org.apache.log4j.Logger;


@ManagedBean(name = "CrearComunicacionJSFBean")
@ViewScoped
public class CrearComunicacionJSFBean extends CommonJSFBean {

    private static Logger logger = Logger.getLogger(CrearComunicacionJSFBean.class.getSimpleName());

    @EJB
    private ModeloComunicacionesLocal3 comunicacionesBean;
    @EJB
    private WebCenterContentLocal3 webcenterContentBean;
    private Collection<AnmPlantilla> plantillasSet;
    private Collection<String> trueFalseSet;
    private Collection<SelectItem> tiposIdentificacionSet;
    private Collection<UnidadAdministrativa> unidadesAdministrativasSet;
    private String dependenciaSelected;
    private boolean esTituloMinero = false;
    private Collection<SelectItem> categoriasProceso = new ArrayList<>();
    private Collection<FolderFolders> categoriasProcesoSet = new ArrayList<>();
    private Collection<SelectItem> procesos = new ArrayList<>();
    private Collection<FolderFolders> procesosSet = new ArrayList<>();
    private Collection<SelectItem> series = new ArrayList<>();
    private Collection<FolderFolders> seriesSet = new ArrayList<>();
    private Collection<SelectItem> subseries = new ArrayList<>();
    private Collection<FolderFolders> subseriesSet = new ArrayList<>();
    private Collection<SelectItem> cuadernos = new ArrayList<>();
    private Collection<FolderFolders> cuadernosSet = new ArrayList<>();
    private Collection<SelectItem> cuentas = new ArrayList<>();
    private Collection<Cuenta> cuentasSet = new ArrayList<>();
    private Collection<SelectItem> tramites = new ArrayList<>();
    private Collection<Tramite> tramitesSet = new ArrayList<>();
    private Collection<SelectItem> tiposDocumentales = new ArrayList<>();
    private Collection<TipoDocumentalTramite> tiposDocumentalesSet = new ArrayList<>();

    private boolean placaValida = true;
    private String message = "";

    private final static String PARENT_TAX_NAME = "ANM";

    public CrearComunicacionJSFBean() {
        super();
    }

    @PostConstruct
    public void inicializar() throws Exception {
        logger.info("CrearComunicacionJSFBean | BEGIN | inicializar()");

        this.plantillasSet = this.comunicacionesBean.getAnmPlantillaFindAll();
        Collection<SgdTipoIdentificacion> tiposIdentificacion =
            this.comunicacionesBean.getSgdTipoIdentificacionFindAll();
        this.tiposIdentificacionSet = new ArrayList<SelectItem>(tiposIdentificacion.size());
        for (SgdTipoIdentificacion tp : tiposIdentificacion) {
            this.tiposIdentificacionSet.add(new SelectItem(tp.getCodigo(), tp.getNombre()));
        }
        this.trueFalseSet = new ArrayList(TrueFalseType.values().length);
        for (TrueFalseType values : TrueFalseType.values()) {
            this.trueFalseSet.add(values.getValue());
        }
        this.unidadesAdministrativasSet = this.webcenterContentBean.getUnidadAdministrativaFindAll();

        Long codDependencia = (Long) getElObjectFromBinding("#{bindings.codDependencia.inputValue}");

        logger.debug("CodDependencia Seleccionada: " + codDependencia);
        logger.debug("Nombre Usuario: " + getElObjectFromBinding("#{bindings.nombre2.inputValue}"));

        for (UnidadAdministrativa und : this.unidadesAdministrativasSet) {
            logger.debug("CodDependencia Comparada: " + und.getCodUnidadadministrativa());
            if (codDependencia != null && codDependencia.longValue() == und.getCodUnidadadministrativa().longValue()) {
                this.dependenciaSelected = codDependencia + "-" + und.getNombreunidadadministrativa();
                super.setElObjectIntoBinding("#{bindings.dependenciaOrigen.inputValue}",
                                             codDependencia + "-" + und.getNombreunidadadministrativa());
                super.setElObjectIntoBinding("#{bindings.idDependenciaOrigen.inputValue}",
                                             und.getIdUnidadadministrativa());
                break;
            }
        }

        Collection<FolderFolders> taxonomiaParent =
            this.webcenterContentBean.getFolderFoldersFindByFolderName(PARENT_TAX_NAME);
        String parentGuid = null;
        if (taxonomiaParent == null || taxonomiaParent.isEmpty()) {
            throw new Exception("Se debe configurar el nodo principal de la taxonomia 'ANM'");
        }

        for (FolderFolders f : taxonomiaParent) {
            parentGuid = f.getFfolderguid();
            break;
        }
        this.categoriasProcesoSet = this.webcenterContentBean.getFolderFoldersFindByParentGuid(parentGuid);
        this.categoriasProceso = new ArrayList<SelectItem>(categoriasProcesoSet.size());
        for (FolderFolders f : categoriasProcesoSet) {
            this.categoriasProceso.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }

        this.cuentasSet = this.webcenterContentBean.getCuentaFindAll();
        this.cuentas = new ArrayList<SelectItem>(this.cuentasSet.size());
        for (Cuenta f : cuentasSet) {
            this.cuentas.add(new SelectItem(f.getDdocaccount(), f.getDdocaccount()));
        }

        this.tramitesSet = this.webcenterContentBean.getTramiteFindAll();
        this.tramites = new ArrayList<SelectItem>(this.tramitesSet.size());
        for (Tramite f : tramitesSet) {
            this.tramites.add(new SelectItem(f.getIdtramite().toString(), f.getTramite()));
        }

        //super.setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", "NO");
        super.setElObjectIntoBinding("#{bindings.nroFolios.inputValue}", Integer.valueOf("1"));
        super.setElObjectIntoBinding("#{bindings.nroAnexos.inputValue}", "0");
    }

    public void validarPlaca(ValueChangeEvent evt) {
        String nuevaPlaca = evt.getNewValue().toString();
        logger.debug("Nro Placa Digitado: " + nuevaPlaca);

        ExpedienteMinero proxy = new SGDWebServiceLocator().getExpedienteMineroProxy();
        WccResponse response = proxy.validarExpedienteMinero(nuevaPlaca);
        this.placaValida = true;
        if (response == null || !response.getStatusCode().equalsIgnoreCase("0")) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage fm =
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                                 "El Expediente Minero \"" + nuevaPlaca + "\" actualmente no se encuentra registrado" +
                                 " en el SGD de la ANM." + " Si desea continuar de clic en \"Aceptar.\"", "");
            ctx.addMessage("", fm);
            this.placaValida = false;
        }
    }

    public String submit() {
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        oracle.bpel.services.workflow.worklist.adf.InvokeActionBean invokeActionBean =
            (oracle.bpel.services.workflow.worklist.adf.InvokeActionBean) map.get("invokeActionBean");

        logger.debug("Inicio del metodo submit(): ");
        
        DocMgmtBean docMngr = (DocMgmtBean) super.getElObjectFromBinding("#{pageFlowScope.docMgmtBean}");
        logger.debug("Cantidad de attachements:" + docMngr.getAddedAttachmentList().size());

        String l = (String) super.getElObjectFromBinding("#{bindings.interesadosExternos.inputValue}");
        String result = null;

        logger.debug("metodo submit(): " + l);

        if (getIfAnexo().getValue() == null) {
            logger.debug("No se ha seleccionado un documento principal");
            
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "No ha seleccionado un documento principal. Por favor validar");
        } else if (l == null || l.isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage fm =
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un destinatario", "");
            ctx.addMessage("", fm);
        } else if (!validarGestionDocumental()) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, this.message);
        } else {
            result = invokeActionBean.invokeOperation();
        }

        logger.debug("Fin del metodo submit(): ");
        return result;
    }

    protected boolean validarGestionDocumental() {
        logger.debug("BEGIN | validarGestionDocumental()");

        logger.debug("#{bindings.esTituloMinero.inputValue} : " +
                     super.getElObjectFromBinding("#{bindings.esTituloMineroBool.inputValue}").toString());

        //obtener expMinero
        boolean expMinero =
            Boolean.parseBoolean(super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}").toString());
        logger.debug("validarGestionDocumental() | expMinero : " + expMinero);
        
        //Setea el valor NO o SI al binding de salida, protecci�n para cuando no esExpMinero es false desde el inicio
        setEsTituloMinero(expMinero);

        if (expMinero) {
            //valida tramite y tipo != null
            if (super.getElObjectFromBinding("#{bindings.idTramite.inputValue}") != null &&
                super.getElObjectFromBinding("#{bindings.idTpDocumento.inputValue}") != null) {

                //valida placa
                if (super.getElObjectFromBinding("#{bindings.placa.inputValue}") != null && !super.getElObjectFromBinding("#{bindings.placa.inputValue}")
                                                                                                  .toString()
                                                                                                  .trim()
                                                                                                  .equals("")) {
                    return true;
                } else {
                    this.message = "Digite No. de placa para continuar";
                    return false;
                }
            } else {
                this.message = "Seleccione Tr�mite y Tipo Documental para continuar";
                return false;
            }
        } else {
            //valida taxonomia
            if (super.getElObjectFromBinding("#{bindings.idCatProceso.inputValue}") != null &&
                super.getElObjectFromBinding("#{bindings.idProceso.inputValue}") != null &&
                super.getElObjectFromBinding("#{bindings.idSerie.inputValue}") != null) {
                logger.debug(" | validarGestionDocumental() | cat-pro-ser :  NOT null ;  subseriesSet.isEmpty() : " +
                             subseriesSet.isEmpty());
               
                return true;
            } else {
                //Gestion documental no diligenciada
                this.message = "Debe diligenciar la secci�n de Gesti�n Documental";
                return false;
            }
        }
    }

    public void cambiarEsExpMinero(ValueChangeEvent evt) {
        logger.info("BEGIN | cambiarEsExpMinero()");

        Boolean selectedValue = (Boolean) evt.getNewValue();

        try {
            if (selectedValue != null) {
                if (selectedValue.equals(Boolean.FALSE)) {
                    logger.debug("cambiarEsExpMinero() | exp minero false");

                    super.setElObjectIntoBinding("#{bindings.placa.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idTramite.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idTpDocumento.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", null);
                } else {
                    logger.debug("cambiarEsExpMinero() | exp minero true");

                    super.setElObjectIntoBinding("#{bindings.idCatProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.catProceso.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);

                    super.setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", null);
                    super.setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", null);
                }
            }
        } catch (Exception e) {
            logger.error(" | Exception | cambiarEsExpMinero()", e);
        }
    }

    public void cambiarCategoriaProceso(ValueChangeEvent evt) {
        logger.debug("inicio cambiarCategoriaProceso(): " + evt.getNewValue());

        String catProcesoSel = (String) evt.getNewValue();
        this.procesosSet = this.webcenterContentBean.getFolderFoldersFindByParentGuid(catProcesoSel);
        this.procesos = new ArrayList<SelectItem>(this.procesosSet.size());

        logger.debug("cambiarCategoriaProceso(): Encontro: " + this.procesosSet.size() + " Procesos");

        for (FolderFolders f : procesosSet) {
            this.procesos.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
        }

        //setea los demas datos de la taxonomia
        for (FolderFolders f : this.categoriasProcesoSet) {
            if (f.getFfolderguid().equals(catProcesoSel)) {
                logger.debug("cambiarCategoriaProceso(): Define la categoria de proceso: " + f.getFfoldername());
                super.setElObjectIntoBinding("#{bindings.catProceso.inputValue}", f.getFfoldername());
                break;
            }
        }
        this.cambiarProceso(null);

        logger.debug("Fin: cambiarCategoriaProceso()");
    }

    public void cambiarProceso(ValueChangeEvent evt) {
        logger.debug("inicio cambiarProceso(): " + (evt != null && evt.getNewValue() != null ? evt.getNewValue() : "vacio"));

        if (evt == null || evt.getNewValue() == null) {
            this.series = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<FolderFolders>(0);
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", null);
        } else {
            String idProceso = (String) evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idProceso.inputValue}", idProceso);

            for (FolderFolders f : this.procesosSet) {
                if (f.getFfolderguid().equals(idProceso)) {
                    super.setElObjectIntoBinding("#{bindings.proceso.inputValue}", f.getFfoldername());
                    break;
                }
            }

            this.seriesSet = this.webcenterContentBean.getFolderFoldersFindByParentGuid(idProceso);
            this.series = new ArrayList<SelectItem>(this.seriesSet.size());

            logger.debug("cambiarProceso(): Encontro: " + this.seriesSet.size() + " Series");

            for (FolderFolders f : seriesSet) {
                this.series.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }
        }
        this.cambiarSerie(null);
    }

    public void cambiarSerie(ValueChangeEvent evt) {
        logger.debug("Inicio cambiarSerie()");
        if (evt == null || evt.getNewValue() == null) {
            this.subseries = new ArrayList<SelectItem>(0);
            this.subseriesSet = new ArrayList<FolderFolders>(0);
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.serie.inputValue}", null);
        } else {
            String idSerie = (String) evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSerie.inputValue}", idSerie);

            for (FolderFolders f : this.seriesSet) {
                if (f.getFfolderguid().equals(idSerie)) {
                    super.setElObjectIntoBinding("#{bindings.serie.inputValue}", f.getFfoldername());
                    break;
                }
            }

            this.subseriesSet = this.webcenterContentBean.getFolderFoldersFindByParentGuid(idSerie);
            this.subseries = new ArrayList<SelectItem>(this.subseriesSet.size());
            logger.debug("cambiarSerie(): Encontro: " + this.subseries.size() + " Subseries");
            for (FolderFolders f : subseriesSet) {
                this.subseries.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }
        }
        this.cambiarSubSerie(null);
    }

    public void cambiarSubSerie(ValueChangeEvent evt) {
        logger.debug("Inicio: cambiarSubSerie()");

        if (evt == null || evt.getNewValue() == null) {
            this.cuadernos = new ArrayList<SelectItem>(0);
            this.cuadernosSet = new ArrayList<FolderFolders>(0);
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", null);
        } else {
            String idSubSerie = (String) evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idSubSerie.inputValue}", idSubSerie);

            for (FolderFolders f : this.subseriesSet) {
                if (f.getFfolderguid().equals(idSubSerie)) {
                    super.setElObjectIntoBinding("#{bindings.subserie.inputValue}", f.getFfoldername());
                    break;
                }
            }

            this.cuadernosSet = this.webcenterContentBean.getFolderFoldersFindByParentGuid(idSubSerie);
            this.cuadernos = new ArrayList<SelectItem>(this.cuadernosSet.size());

            logger.debug("cambiarSubSerie(): Encontro: " + this.cuadernosSet.size() + " cuaernos");

            for (FolderFolders f : cuadernosSet) {
                this.cuadernos.add(new SelectItem(f.getFfolderguid(), f.getFfoldername()));
            }
        }

        this.cambiarCuaderno(null);
    }

    public void cambiarCuaderno(ValueChangeEvent evt) {
        logger.debug("Inicio: cambiarCuaderno()");

        if (evt == null || evt.getNewValue() == null) {
            super.setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", null);
            super.setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", null);
        } else {
            String cuadernoSel = (String) evt.getNewValue();
            super.setElObjectIntoBinding("#{bindings.idCuaderno.inputValue}", cuadernoSel);

            for (FolderFolders f : this.cuadernosSet) {
                if (f.getFfolderguid().equals(cuadernoSel)) {
                    super.setElObjectIntoBinding("#{bindings.cuaderno.inputValue}", f.getFfoldername());
                    break;
                }
            }
        }
    }

    public void cambiarTramite(ValueChangeEvent evt) {
        logger.debug("Inicio: cambiarTramite(): " + evt.getNewValue());

        if (evt == null || evt.getNewValue() == null || evt.getNewValue().equals("")) {
            super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", null);
            this.tiposDocumentalesSet = new ArrayList<TipoDocumentalTramite>();
            this.tiposDocumentales = new ArrayList<SelectItem>();
            return;
        }

        String tramiteSelected = (String) evt.getNewValue();
        for (Tramite t : this.tramitesSet) {
            if (tramiteSelected.equals(t.getIdtramite().toString())) {
                logger.debug("Tramite seleccionado: " + tramiteSelected);
                super.setElObjectIntoBinding("#{bindings.nombreTramite.inputValue}", t.getTramite());
                break;
            }
        }
        
        this.tiposDocumentalesSet =
            this.webcenterContentBean.getTipoDocumentalTramiteFindByTramite(new BigDecimal(tramiteSelected));
        this.tiposDocumentales = new ArrayList<SelectItem>(this.tiposDocumentalesSet.size());
        for (TipoDocumentalTramite f : tiposDocumentalesSet) {
            this.tiposDocumentales.add(new SelectItem(f.getIdtipodtal().toString(), f.getNombretipodocumental()));
        }
        logger.debug("Fin: cambiarTramite()");
    }


    public void cambiarTipoDocumento(ValueChangeEvent evt) {
        logger.debug("Inicio: cambiarTipoDocumento():" + evt.getNewValue());
        logger.debug("Inicio: cambiarTipoDocumento():" + evt.getNewValue().getClass());

        String tipoDocSele = (String) evt.getNewValue();
        for (TipoDocumentalTramite t : this.tiposDocumentalesSet) {
            if (tipoDocSele.equals(t.getIdtipodtal().toString())) {
                super.setElObjectIntoBinding("#{bindings.nombreTpDocumento.inputValue}", t.getNombretipodocumental());
                break;
            }
        }
        logger.debug("Fin: cambiarTipoDocumento()");
    }

    /**
     *M&eacute;todo que se encarga de asignar la operaci?n correspondiente a un boton.
     *Este m&eacute;todo llama al metodo setOperation de la clase InvokeActionBean.
     * @param action
     */
    protected void setOperation(ActionEvent action) {
        logger.info("BEGIN | setOperation()");
        try {
            Application app = FacesContext.getCurrentInstance().getApplication();
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            InvokeActionBean invokeActionBean =
                (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
            invokeActionBean.setOperation(action);

        } catch (Exception e) {
            logger.error("Exception | setOperation() ", e);
        }
    }

    //pop-up de mensaje al usuario
    protected void mostrarMensaje(FacesMessage.Severity severity, String mensaje) {
        try {
            FacesMessage message = new FacesMessage(severity, mensaje, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            logger.error(" | Exception mostrarMensaje", e);
        }
    }

    /**************************** GETTERS AND SETTERS ***********************
     **************************************************************************      */

    public void setPlantillasSet(Collection<AnmPlantilla> plantillasSet) {
        this.plantillasSet = plantillasSet;
    }

    public Collection<AnmPlantilla> getPlantillasSet() {
        return plantillasSet;
    }

    public void setTrueFalseSet(Collection<String> trueFalseSet) {
        this.trueFalseSet = trueFalseSet;
    }

    public Collection<String> getTrueFalseSet() {
        return trueFalseSet;
    }

    public void setTiposIdentificacionSet(Collection<SelectItem> tiposIdentificacionSet) {
        this.tiposIdentificacionSet = tiposIdentificacionSet;
    }

    public Collection<SelectItem> getTiposIdentificacionSet() {
        return tiposIdentificacionSet;
    }
    
    public void setDependenciaSelected(String dependenciaSelected) {
        this.dependenciaSelected = dependenciaSelected;
    }

    public String getDependenciaSelected() {
        return dependenciaSelected;
    }

    public void setEsTituloMinero(boolean esTituloMinero) {
        this.esTituloMinero = esTituloMinero;
        logger.debug("setEsTituloMinero() Nuevo valor: " + esTituloMinero);
        if (esTituloMinero) {
            super.setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", "SI");
            super.setElObjectIntoBinding("#{bindings.esTituloMineroBool.inputValue}", Boolean.TRUE);
        } else {
            super.setElObjectIntoBinding("#{bindings.esTituloMinero.inputValue}", "NO");
            super.setElObjectIntoBinding("#{bindings.esTituloMineroBool.inputValue}", Boolean.FALSE);
        }
    }

    public boolean isEsTituloMinero() {
        Boolean valorEsTitulo = (Boolean)super.getElObjectFromBinding("#{bindings.esTituloMinero.inputValue}");
        super.setElObjectIntoBinding("#{bindings.esTituloMineroBool.inputValue}", valorEsTitulo);
        
        logger.debug("Valor seleccionado para esTituloMinero: " + valorEsTitulo);
        return valorEsTitulo.booleanValue();
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

    public boolean isRenderCuaderno() {
        return this.cuadernosSet != null && !cuadernos.isEmpty();
    }

    public boolean isRenderSubserie() {
        return this.subseriesSet != null && !subseries.isEmpty();
    }
}
