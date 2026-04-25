package co.gov.anm.sgd.response;

import java.util.List;

public class UsuarioComunicacion extends WCCResponse {
    
    private String idUsuario;
    private String nombreUsuario;
    private Long codigoDependencia;
    private String email;

    private List<Rol> rolList;
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    
    public UsuarioComunicacion () {
        super();
    }
    
    public UsuarioComunicacion (String erroCode) {
        super(erroCode);
    }
    
    public UsuarioComunicacion (String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    public List<Rol> getRolList() {
        return rolList;
    }

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
}
