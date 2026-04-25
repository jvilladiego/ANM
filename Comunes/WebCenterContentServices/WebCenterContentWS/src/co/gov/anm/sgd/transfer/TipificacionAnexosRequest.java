package co.gov.anm.sgd.transfer;


public class TipificacionAnexosRequest {
  
    private Credenciales credenciales;
    private boolean expedienteMinero;
    private boolean PQRS;
    private boolean docPpal;
    private String nroPlaca;
    private String nroRadicado;
    private String idTramite;
    private String idTipoDocumental;
    private String idDependenciaOrigen;
    private String folderGUID;
    private String nombreDoc;

    public void setDocPpal(boolean docPpal) {
        this.docPpal = docPpal;
    }

    public boolean isDocPpal() {
        return docPpal;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }

    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setPQRS(boolean PQRS) {
        this.PQRS = PQRS;
    }

    public boolean isPQRS() {
        return PQRS;
    }

    public void setFolderGUID(String folderGUID) {
        this.folderGUID = folderGUID;
    }

    public String getFolderGUID() {
        return folderGUID;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setExpedienteMinero(boolean expedienteMinero) {
        this.expedienteMinero = expedienteMinero;
    }

    public boolean isExpedienteMinero() {
        return expedienteMinero;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setTramite(String tramite) {
        this.idTramite = tramite;
    }

    public String getTramite() {
        return idTramite;
    }

    public void setTipoDocumental(String tipoDocumental) {
        this.idTipoDocumental = tipoDocumental;
    }

    public String getTipoDocumental() {
        return idTipoDocumental;
    }

    public void setIdDependenciaOrigen(String idDependenciaOrigen) {
        this.idDependenciaOrigen = idDependenciaOrigen;
    }

    public String getIdDependenciaOrigen() {
        return idDependenciaOrigen;
    }
}
