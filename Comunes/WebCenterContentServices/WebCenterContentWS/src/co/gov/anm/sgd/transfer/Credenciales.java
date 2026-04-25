package co.gov.anm.sgd.transfer;

import java.io.Serializable;

@SuppressWarnings("oracle.jdeveloper.java.serialversionuid-field-missing")
public class Credenciales implements Serializable {
    
    private String usuario;
    private String contrasena;


    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasena() {
        return contrasena;
    }
}
