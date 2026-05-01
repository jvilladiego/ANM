package co.gov.anm.comunicaciones.control;

import java.io.OutputStream;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.context.AdfFacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;



@ManagedBean(name="aprobarComunicacionJSFBean")
@ViewScoped
public class AprobarComunicacionJSFBean extends CommonJSFBean{
    
    
    private String user;
    private Logger log;
    private RichInputFile ifAnexo;


    public AprobarComunicacionJSFBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN AprobarComunicacionJSFBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error("Excepcion AprobarComunicacionJSFBean",e);
        }
        log.info("END AprobarComunicacionJSFBean");
    }
    
    
    @PostConstruct
    public void init () throws Exception{
        log.info("BEGIN init");
        try {
            ;
       } catch (Exception e) {
            log.error("Excepcion init",e);
        }
        log.info("END init");
    }
    
    
    
    
    /****************************************
     * Author: Adrian Molina - adriomol@gmail.com
     * Date: 10Oct2017
     * *****************************************/
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
                           setElObjectIntoBinding("#{bindings.name1.inputValue}", nombreModificado);
                           log.debug(user+" -> name1: "+getElObjectFromBinding("#{bindings.name1.inputValue}"));
                           //Setear demas variables del adjunto
                           setElObjectIntoBinding("#{bindings.mimeType1.inputValue}", file.getContentType());
                           setElObjectIntoBinding("#{bindings.size.inputValue}", file.getLength());
                           byte[] bytes = IOUtils.toByteArray(file.getInputStream());
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
               setElObjectIntoBinding("#{bindings.mimeType1.inputValue}", null);
               setElObjectIntoBinding("#{bindings.size.inputValue}", null);
               setElObjectIntoBinding("#{bindings.content1.inputValue}",null);*/
           } catch (Exception e) {
                log.error(user + " -> Exception limpiarDocPpal", e);
            }
            log.info("END limpiarDocPpal");    
        }
        

    public void descargar(FacesContext fc, OutputStream os) {
            log.info("INICIO descargar");
            try {
                log.debug(user + " -> name1: " + getElObjectFromBinding("#{bindings.name1.inputValue}"));
                log.debug(user + " -> mimeType1: " + getElObjectFromBinding("#{bindings.mimeType1.inputValue}"));
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
    
    
    
    public void aprobarComunicacion ( ActionEvent event ){        
        log.info("INCIO aprobarComunicacion");
        try {
            ;
       } catch (Exception e) {
            log.error(user + " Exception aprobarComunicacion", e);
        }
        log.info("FIN aprobarComunicacion");
    }


    public void setIfAnexo(RichInputFile ifAnexo) {
        this.ifAnexo = ifAnexo;
    }

    public RichInputFile getIfAnexo() {
        return ifAnexo;
    }
}
