package co.gov.anm.sgd.transfer;


public class GeneracionODTRequest {
   
   private ComunicacionODT comunicacion;
   private String rutaArchivo;
   private boolean pdf;

    public void setComunicacion(ComunicacionODT comunicacion) {
        this.comunicacion = comunicacion;
    }

    public ComunicacionODT getComunicacion() {
        return comunicacion;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    public boolean isPdf() {
        return pdf;
    }

}
