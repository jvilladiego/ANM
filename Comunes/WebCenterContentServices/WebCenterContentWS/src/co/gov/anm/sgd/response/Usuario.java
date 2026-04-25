package co.gov.anm.sgd.response;


public class Usuario {
    
    private String idUsuario;
    private String nombreUsuario;
    private Long codigoDependencia;
    private String email;

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setCodigoDependencia(Long codigoDependencia) {
        this.codigoDependencia = codigoDependencia;
    }

    public Long getCodigoDependencia() {
        return codigoDependencia;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
