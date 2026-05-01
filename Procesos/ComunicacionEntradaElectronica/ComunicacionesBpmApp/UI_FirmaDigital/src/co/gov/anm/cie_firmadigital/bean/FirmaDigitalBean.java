package co.gov.anm.cie_firmadigital.bean;

import co.gov.anm.digitalsignature.model.firmar.RootRequest;
import co.gov.anm.digitalsignature.model.firmar.RootResponse;
import co.gov.anm.digitalsignature.model.firmar.Usuario;
import co.gov.anm.digitalsignature.service.FirmaDigitalService;

import co.gov.anm.digitalsignature.utils.FileUtils;

import co.gov.anm.cie_firmadigital.util.ADFUtils;

import co.gov.anm.cie_firmadigital.util.JsfUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.el.ELContext;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import org.apache.log4j.Logger;

public class FirmaDigitalBean implements Serializable {
    @SuppressWarnings("compatibility:-7766208882271671094")
    private static final long serialVersionUID = 1L;

    private Logger logger;

    private String identificacion = "";
    private String user = "";
    private String password = "";
    private String fileName;
    private String path;

    public FirmaDigitalBean() {
        super();
        logger = Logger.getLogger(this.getClass().getSimpleName());
        this.path = (String) ADFUtils.getBoundAttributeValue("primaryFile");
        int index = path.lastIndexOf('/');
        this.fileName = path.substring(index + 1);
        logger.debug("File Path :"+path);
        Object u = ADFUtils.getBoundAttributeValue("jefeDependencia");
        if (u!= null){
            identificacion = ADFUtils.getBoundAttributeValue("jefeDependencia").toString();
        }
        logger.debug("identificacion :"+identificacion);
    }

    public void firmaDigitalAL(ActionEvent ae) {

        boolean firmado = true;
        if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
            FirmaDigitalService firmaDigitalService = new FirmaDigitalService();
            /**
         * Construccion del objeto de la peticion
         */
            RootRequest rootRequest = new RootRequest();
            rootRequest.setNumeroRadicado((String) ADFUtils.getBoundAttributeValue("nroRadicado"));
            try {
                rootRequest.setPdf(FileUtils.encodeFileToBase64Binary(path));
            } catch (IOException e) {
                logger.error("Error obteniendo el archivo", e);

            }
            
            /**
             * Validacion para usuario del ambiente de pruebas
             */
            if("jefe".equals(identificacion)){
                rootRequest.getUsuarios().add(new Usuario(user, user, password));
            }else{
                rootRequest.getUsuarios().add(new Usuario(identificacion, user, password));
            }
            
            rootRequest.setTipoFirma("1");
            rootRequest.setUrl("NO");

            RootResponse comunicadoFirmado = null;
            try {
                logger.debug("rootRequest : " + rootRequest);
                comunicadoFirmado = firmaDigitalService.firmarDocumento(rootRequest);
                if (comunicadoFirmado == null) {
                    firmado = false;
                    JsfUtil.errorMenssage(null,
                                          comunicadoFirmado.getErrores().getCodigo() + " : " +
                                          comunicadoFirmado.getErrores().getMensaje());
                } else {
                    FileUtils.Base64BinaryToEncodeFile(comunicadoFirmado.getRespuesta().getPdf_firmado(), path);
                }
            } catch (IOException ioe) {
                firmado = false;
                JsfUtil.errorMenssage(null, "Error al ejecutar el servicio de Firma Digital");
                logger.error("error", ioe);
            } catch (Exception e) {
                firmado = false;
                JsfUtil.errorMenssage(null, "Error al ejecutar el servicio de Firma Digital");
                logger.error("error", e);
            }
        } else {
            firmado = false;
            JsfUtil.errorMenssage(null, "Usuario y Clave Requeridos");
            logger.error("Usuario y Clave de firma digital requeridos");
        }
        if (firmado) {
            setOperation(ae);
        }
    }

    public void firmaManualAL(ActionEvent ae) {
        setOperation(ae);
    }

    protected void setOperation(ActionEvent action) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        InvokeActionBean invokeActionBean =
            (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
        invokeActionBean.setOperation(action);
    }

    /**Method to download file from actual path
     * @param facesContext
     * @param outputStream
     */
    public void downloadFileListener(FacesContext facesContext, OutputStream outputStream) throws IOException {
        //Read file from particular path, path bind is binding of table field that contains path
        File filed = new File((String) ADFUtils.getBoundAttributeValue("primaryFile"));
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);
            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream.flush();
    }


    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

}
