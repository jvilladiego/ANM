package co.gov.anm.comunicaciones.jsfbeans;


import java.io.OutputStream;
import java.io.Serializable;

import java.util.Base64;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletResponse;

import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.server.ViewObjectImpl;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

public class CommonJSFBean implements Serializable {

    private static Logger logger = Logger.getLogger(CommonJSFBean.class.getSimpleName());

    private RichInputFile ifAnexo;

    public CommonJSFBean() {
        super();
    }

    protected BindingContainer getBindingContainer() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    protected void setElObjectIntoBinding(String expr, Object valor) {
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


    protected ViewObjectImpl getViewObjectWccContent(String vo) throws Exception {
        String amDef = "co.gov.anm.model.WebcenterContentModule";
        String config = "WebcenterContentModuleLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObjectImpl result = (ViewObjectImpl) am.findViewObject(vo);
        return result;
    }


    protected ViewObjectImpl getViewObjectComunicaciones(String vo) throws Exception {
        String amDef = "co.gov.anm.model.ComunicacionesModule";
        String config = "ComunicacionesModuleLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObjectImpl result = (ViewObjectImpl) am.findViewObject(vo);
        return result;
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

    public void cambiarDocPpal(ValueChangeEvent vce) {
        logger.info("BEGIN cambiarDocPpal");
        try {
            UploadedFile file = (UploadedFile) vce.getNewValue();
            logger.debug("file: " + file);

            logger.debug("fileName: " + file.getFilename());
            logger.debug("fileType: " + file.getContentType());
            logger.debug("opaqueData: " + file.getOpaqueData());
            logger.debug("inputStream: " + file.getInputStream());
            String fileExtn = getFileExtn(file.getFilename());
            logger.debug("fileExtn: " + fileExtn);
            logger.debug("instanceId: " + getElObjectFromBinding("#{bindings.instanceId.inputValue}"));
            
            if (!esValido(fileExtn)) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "Solo se permiten archivos 'Odt'. Por favor actualice el archivo.");

                limpiarDocPpal();

            } else if (file.getContentType().contains("vnd.oasis.opendocument.text")) {

                if (file.getLength() > 26214400L) {
                    mostrarMensaje(FacesMessage.SEVERITY_WARN,
                                   "El archivo debe ser menor a 25Mb. Por favor seleccione otro para continuar.");

                    limpiarDocPpal();
                } else {
                    String nombreDoc = file.getFilename();
                    String[] parts = nombreDoc.split("\\.(?=[^\\.]+$)");

                    String nombreModificado =
                        parts[0] + "_" + getElObjectFromBinding("#{bindings.instanceId.inputValue}").toString() + "." +
                        parts[1];

                    logger.debug("nombre modificado: " + nombreModificado);
                    setElObjectIntoBinding("#{bindings.name1.inputValue}", nombreModificado);
                    logger.debug("name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));

                    setElObjectIntoBinding("#{bindings.mimeType2.inputValue}", file.getContentType());
                    setElObjectIntoBinding("#{bindings.size.inputValue}", Long.valueOf(file.getLength()));
                    byte[] bytes = IOUtils.toByteArray(file.getInputStream());
                    setElObjectIntoBinding("#{bindings.content1.inputValue}",
                                           Base64.getEncoder().encodeToString(bytes));

                    ifAnexo.setValid(true);
                }
                logger.debug("nombre Adjunto: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_WARN,
                               "La extensión del archivo es correcta pero el contenido invalido. Por favor actualice el archivo.");

                limpiarDocPpal();
            }
        } catch (Exception e) {
            logger.error("Exception cambiarDocPpal", e);
        }
        logger.info("END cambiarDocPpal");
    }


    public void limpiarDocPpal() {
        logger.info("BEGIN limpiarDocPpal");
        try {
            ifAnexo.resetValue();
            ifAnexo.setValid(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(ifAnexo);
        } catch (Exception e) {
            logger.error("Exception limpiarDocPpal", e);
        }
        logger.info("END limpiarDocPpal");
    }


    public void descargar(FacesContext fc, OutputStream os) {
        logger.info("INICIO descargar");
        try {
            logger.debug("name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            logger.debug("mimeType2: " + getElObjectFromBinding("#{bindings.mimeType2.inputValue}"));
            logger.debug("size: " + getElObjectFromBinding("#{bindings.size.inputValue}"));

            byte[] encodeFile = getElObjectFromBinding("#{bindings.content1.inputValue}").toString().getBytes();
            logger.debug("length-e: " + encodeFile.length);

            byte[] decodeFile = Base64.getDecoder().decode(encodeFile);
            logger.debug("length-d: " + decodeFile.length);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                                                                             .getExternalContext()
                                                                             .getResponse();
            response.setHeader("Content-Disposition",
                               "attachment; filename=\"" + getElObjectFromBinding("#{bindings.name1.inputValue}"));
            response.setContentLength(decodeFile.length);
            response.getOutputStream().write(decodeFile);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            logger.error("Exception descargar", e);
        }
        logger.info("FIN descargar");
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

    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
