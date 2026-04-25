package co.gov.anm.sgd.response;

import com.oracle.ucm.Field;

import java.util.List;

public class DocumentDetailResponse extends WCCResponse {
    
    //fileInfo
    private String nombreDocumento;
    private String idDocumento;
    private String versionPublicada;
    private String creador;
    private String ultimoModificador;
    private String fechaCreacioon;
    private String ultimaFechaModificacion;
    private String grupoSeguridad;
    private String cuenta;
    
    //Doc Info
    private String numeroPlaca;
    private String unidadAdministrativa;
    private String estadoArchivo;
    private String tramite;

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setVersionPublicada(String versionPublicada) {
        this.versionPublicada = versionPublicada;
    }

    public String getVersionPublicada() {
        return versionPublicada;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getCreador() {
        return creador;
    }

    public void setUltimoModificador(String ultimoModificador) {
        this.ultimoModificador = ultimoModificador;
    }

    public String getUltimoModificador() {
        return ultimoModificador;
    }

    public void setFechaCreacioon(String fechaCreacioon) {
        this.fechaCreacioon = fechaCreacioon;
    }

    public String getFechaCreacioon() {
        return fechaCreacioon;
    }

    public void setUltimaFechaModificacion(String ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    public String getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }

    public void setGrupoSeguridad(String grupoSeguridad) {
        this.grupoSeguridad = grupoSeguridad;
    }

    public String getGrupoSeguridad() {
        return grupoSeguridad;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setEstadoArchivo(String estadoArchivo) {
        this.estadoArchivo = estadoArchivo;
    }

    public String getEstadoArchivo() {
        return estadoArchivo;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTramite() {
        return tramite;
    }

    public DocumentDetailResponse(List<Field> list) {
        super(list);
    }

    public DocumentDetailResponse(String string) {
        super(string);
    }

    public DocumentDetailResponse(String string, String string1) {
        super(string, string1);
    }

    public DocumentDetailResponse() {
        super();
    }
}
