package co.gov.anm.sgd.transfer;

import java.io.Serializable;

public class ComunicacionODT implements Serializable{


    @SuppressWarnings("compatibility:-6826513973352989294")
    private static final long serialVersionUID = 1L;
	
    private String nroRadicado;     
    private String asunto;     
    private String nroFolios;    
    private String referencia;    
    private String esTituloMinero;    
    private String nroPlaca;    
    private String nroRadicadoRelacionado;    
    private String requiereRespuesta;    
    private String nroAnexos;     
    private String fechaRadicado;       
    private String dependenciaDestino;     
    private String destinatarioInterno;    
    private String tipoComunicacion;    
    private String tiempoRespuesta;    
    private String fechaCreacion;    
    private String medioEnvio;      
    private String dependenciaOrigen;
    private String contenido;
    
    private String elaboro;
    private String tipoPQRS;
    
    private String tipoIdentificacionInteresado;
    private String numIdentificacionInteresado;
    private String nomCompletoInteresado;
    private String emailInteresado;
    private String telefonoInteresado;
    private String celularInteresado;
    private String direccionInteresado;
    private String paisInteresado;
    private String departamentoInteresado;
    private String municipioInteresado;

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setNroFolios(String nroFolios) {
        this.nroFolios = nroFolios;
    }

    public String getNroFolios() {
        return nroFolios;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setEsTituloMinero(String esTituloMinero) {
        this.esTituloMinero = esTituloMinero;
    }

    public String getEsTituloMinero() {
        return esTituloMinero;
    }

    public void setNroPlaca(String nroPlaca) {
        this.nroPlaca = nroPlaca;
    }

    public String getNroPlaca() {
        return nroPlaca;
    }

    public void setNroRadicadoRelacionado(String nroRadicadoRelacionado) {
        this.nroRadicadoRelacionado = nroRadicadoRelacionado;
    }

    public String getNroRadicadoRelacionado() {
        return nroRadicadoRelacionado;
    }

    public void setRequiereRespuesta(String requiereRespuesta) {
        this.requiereRespuesta = requiereRespuesta;
    }

    public String getRequiereRespuesta() {
        return requiereRespuesta;
    }

    public void setNroAnexos(String nroAnexos) {
        this.nroAnexos = nroAnexos;
    }

    public String getNroAnexos() {
        return nroAnexos;
    }

    public void setFechaRadicado(String fechaRadicado) {
        this.fechaRadicado = fechaRadicado;
    }

    public String getFechaRadicado() {
        return fechaRadicado;
    }

    public void setDependenciaDestino(String dependenciaDestino) {
        this.dependenciaDestino = dependenciaDestino;
    }

    public String getDependenciaDestino() {
        return dependenciaDestino;
    }

    public void setDestinatarioInterno(String destinatarioInterno) {
        this.destinatarioInterno = destinatarioInterno;
    }

    public String getDestinatarioInterno() {
        return destinatarioInterno;
    }

    public void setTipoComunicacion(String tipoComunicacion) {
        this.tipoComunicacion = tipoComunicacion;
    }

    public String getTipoComunicacion() {
        return tipoComunicacion;
    }

    public void setTiempoRespuesta(String tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setMedioEnvio(String medioEnvio) {
        this.medioEnvio = medioEnvio;
    }

    public String getMedioEnvio() {
        return medioEnvio;
    }

    public void setDependenciaOrigen(String dependenciaOrigen) {
        this.dependenciaOrigen = dependenciaOrigen;
    }

    public String getDependenciaOrigen() {
        return dependenciaOrigen;
    }

    public void setElaboro(String elaboro) {
        this.elaboro = elaboro;
    }

    public String getElaboro() {
        return elaboro;
    }

    public void setTipoPQRS(String tipoPQRS) {
        this.tipoPQRS = tipoPQRS;
    }

    public String getTipoPQRS() {
        return tipoPQRS;
    }

    public void setTipoIdentificacionInteresado(String tipoIdentificacionInteresado) {
        this.tipoIdentificacionInteresado = tipoIdentificacionInteresado;
    }

    public String getTipoIdentificacionInteresado() {
        return tipoIdentificacionInteresado;
    }

    public void setNumIdentificacionInteresado(String numIdentificacionInteresado) {
        this.numIdentificacionInteresado = numIdentificacionInteresado;
    }

    public String getNumIdentificacionInteresado() {
        return numIdentificacionInteresado;
    }

    public void setNomCompletoInteresado(String nomCompletoInteresado) {
        this.nomCompletoInteresado = nomCompletoInteresado;
    }

    public String getNomCompletoInteresado() {
        return nomCompletoInteresado;
    }

    public void setEmailInteresado(String emailInteresado) {
        this.emailInteresado = emailInteresado;
    }

    public String getEmailInteresado() {
        return emailInteresado;
    }

    public void setTelefonoInteresado(String telefonoInteresado) {
        this.telefonoInteresado = telefonoInteresado;
    }

    public String getTelefonoInteresado() {
        return telefonoInteresado;
    }

    public void setCelularInteresado(String celularInteresado) {
        this.celularInteresado = celularInteresado;
    }

    public String getCelularInteresado() {
        return celularInteresado;
    }

    public void setDireccionInteresado(String direccionInteresado) {
        this.direccionInteresado = direccionInteresado;
    }

    public String getDireccionInteresado() {
        return direccionInteresado;
    }

    public void setPaisInteresado(String paisInteresado) {
        this.paisInteresado = paisInteresado;
    }

    public String getPaisInteresado() {
        return paisInteresado;
    }

    public void setDepartamentoInteresado(String departamentoInteresado) {
        this.departamentoInteresado = departamentoInteresado;
    }

    public String getDepartamentoInteresado() {
        return departamentoInteresado;
    }

    public void setMunicipioInteresado(String municipioInteresado) {
        this.municipioInteresado = municipioInteresado;
    }

    public String getMunicipioInteresado() {
        return municipioInteresado;
    }
}
