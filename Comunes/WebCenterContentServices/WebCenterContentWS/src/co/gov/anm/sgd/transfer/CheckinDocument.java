package co.gov.anm.sgd.transfer;


public class CheckinDocument {
    
    private String docAccount;
    private String docTitle;
    private String primaryFile;
    private String tramite;
    private String tipoDocumental;
    private String unidadAdministrativa;
    private String nroPlaca;
    private String folderGUID;
    private String nroRadicado;
    private boolean expMinero;
    private boolean PQRS;
    private boolean documentoPrincipal;
    private Credenciales credenciales;

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setDocumentoPrincipal(boolean documentoPrincipal) {
        this.documentoPrincipal = documentoPrincipal;
    }

    public boolean isDocumentoPrincipal() {
        return documentoPrincipal;
    }

    public void setExpMinero(boolean expMinero) {
        this.expMinero = expMinero;
    }

    public boolean isExpMinero() {
        return expMinero;
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

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setCredenciales(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

    public void setDocAccount(String docAccount) {
        this.docAccount = docAccount;
    }

    public String getDocAccount() {
        return docAccount;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setPrimaryFile(String primaryFile) {
        this.primaryFile = primaryFile;
    }

    public String getPrimaryFile() {
        return primaryFile;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setTipoDocumental(String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }

    public String getTipoDocumental() {
        return tipoDocumental;
    }
}
