package bean;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

import org.apache.log4j.Logger;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

@ManagedBean(name = "barcodeBean")
@SessionScoped
public class BarcodeBean {

    //@ManagedProperty(value="#{operacionBean}")
    //private OperacionBean operacionBean;

    private Logger log;
    private String user;
    private Date fechaActual;

    private String nroRadicado = null;
    private String imagePath = null;
    private String imageName = null;
    private String folios;
    private String anexos;
    private String destino;
    private String asunto;
    private String placa;
    private List<Anexo> lstAnexo = new ArrayList<Anexo>();

    public BarcodeBean() {
        try {
            //Logger for App
            log = Logger.getLogger(this.getClass().getSimpleName());
            log.info("BEGIN BarcodeBean");
            ADFContext adfCtx = ADFContext.getCurrent();
            SecurityContext secCntx = adfCtx.getSecurityContext();
            user = secCntx.getUserPrincipal().getName();
            log.debug(user + " -> user: " + user);
        } catch (Exception e) {
            log.error(user + " -> Exception BarcodeBean", e);
        }
        log.info("END BarcodeBean");
    }


    public void generarBarCode(ActionEvent ae) {
        log.info("INICIO generarBarCode");
        try {
            fechaActual = new Date();
            //Imagen
            this.imageName = nroRadicado + ".png";
            log.debug(user + " -> imageName: " + imageName);
            this.generarCodigoBarras("/ftp/comunicaciones/");
            this.imagePath = "http://soa.anm.local/sgd.admin/" + nroRadicado + ".png";
            log.debug(user + " -> imagePath: " + imagePath);
            log.debug(user + " -> LstAnexo size: " + lstAnexo.size());


        } catch (Exception e) {
            log.error(user + " -> Exception generarBarCode", e);
        }
        log.info("END generarBarCode");
    }

    /*
    public static void main(String[] args) {
        try {
            //Create the barcode bean
            Code39Bean bean = new Code39Bean();

            final int dpi = 150;

            //Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
                                                             //width exactly one pixel
            bean.setWideFactor(3);
            bean.setBarHeight(3);
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
            bean.doQuietZone(false);

            //Open output file
            File outputFile = new File("out.jpg");
            java.io.OutputStream out = new FileOutputStream(outputFile);
            try {
                //Set up the canvas provider for monochrome JPEG output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

                //Generate the barcode
                bean.generateBarcode(canvas, "20175500003072");

                //Signal end of generation
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void generarCodigoBarras(String ruta) {
        log.info("INICIO generarCodigoBarras");
        try {
            final int dpi = 150;
            //Configure the barcode generator
            Code39Bean bean39 = new Code39Bean();
            bean39.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
            bean39.setWideFactor(4);
            bean39.setBarHeight(3.5);
            bean39.setMsgPosition(HumanReadablePlacement.HRP_NONE);
            bean39.doQuietZone(false);

            //Open output file
            File outputFile = new File(ruta + File.separator + imageName);
            FileOutputStream out = new FileOutputStream(outputFile);
            //Set up the canvas provider for monochrome PNG output
            BitmapCanvasProvider canvas =
                new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            //Generate the barcode
            log.debug(user + " -> No.Radicado: " + nroRadicado);
            bean39.generateBarcode(canvas, nroRadicado);
            log.debug(user + " -> antes del finish");
            //Signal end of generation
            canvas.finish();
            log.debug(user + " -> finish");
        } catch (IOException e) {
            log.error(user + " -> IOException generarCodigoBarras", e);
        } catch (Exception e) {
            log.error(user + " -> Exception generarCodigoBarras", e);
        } catch (Throwable t) {
            log.error(user + " -> Throwable generarCodigoBarras", t);
        }
        log.info("END generarCodigoBarras");
    }


    /******************************************************************************/
    /************************* GETTERS & SETTERS **********************************/

    /******************************************************************************/

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public String getFolios() {
        return folios;
    }

    public void setAnexos(String anexos) {
        this.anexos = anexos;
    }

    public String getAnexos() {
        return anexos;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDestino() {
        return destino;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setLstAnexo(List<Anexo> lstAnexo) {
        this.lstAnexo = lstAnexo;
    }

    public List<Anexo> getLstAnexo() {
        return lstAnexo;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaActual() {
        return fechaActual;
    }
}
