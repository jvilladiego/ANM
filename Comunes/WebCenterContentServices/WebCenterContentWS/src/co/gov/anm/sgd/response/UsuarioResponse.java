package co.gov.anm.sgd.response;

import com.oracle.ucm.Field;

import java.util.List;

public class UsuarioResponse extends WCCResponse {
    public UsuarioResponse(List<Field> list) {
        super(list);
    }

    public UsuarioResponse(String string) {
        super(string);
    }

    public UsuarioResponse(String string, String string1) {
        super(string, string1);
    }

    public UsuarioResponse() {
        super();
    }
    
    private List<Usuario> usuarios;

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
