package co.gov.anm.comunicaciones.control;

import co.gov.anm.model.view.UnidadesAdministrativasVOImpl;
import co.gov.anm.model.view.UsuarioVOImpl;

import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

public class ProcesarRecepcionComunicacionJSFBean extends CommonJSFBean{
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = -3332943377814363670L;

    public ProcesarRecepcionComunicacionJSFBean() {
        super();
    }
    
    
    public void seleccionarUsuarioDestino ( ValueChangeEvent evt ){
        System.out.println("Inicio seleccionarUsuarioDestino():"+evt.getOldValue());
        
        String newValue = (String)evt.getNewValue();
        String codDependencia = (String)super.getElObjectFromBinding("#{bindings.codDependenciaDestino.inputValue}");
        
        try{
            if ( newValue!=null ){            
                UsuarioVOImpl vo = (UsuarioVOImpl)super.getViewObjectComunicaciones("UsuarioVO1");
                vo.applyViewCriteria(vo.getViewCriteria("UsuarioVOCriteria"));
                vo.setNamedWhereClauseParam("codDependenciaSel", codDependencia);
                vo.executeQuery();
                RowSetIterator iter = vo.createRowSetIterator(null);
                Row row = null;
                while ( iter.hasNext() ){                
                    row = iter.next();    
                    
                    System.out.println("seleccionarUsuarioDestino() Buscando usuario:"+newValue);
                    
                    if ( newValue.equals(row.getAttribute("IdUsuario")) ){
                        System.out.println("seleccionarUsuarioDestino() Encontro usuario:"+row.getAttribute("IdUsuario"));
                        super.setElObjectIntoBinding("#{bindings.idDestinatario.inputValue}", row.getAttribute("IdUsuario"));
                        super.setElObjectIntoBinding("#{bindings.usuarioDestinatario.inputValue}", row.getAttribute("NombreUsuario"));
                        super.setElObjectIntoBinding("#{bindings.nombreAD.inputValue}", row.getAttribute("NombreUsuario"));
                        break;
                    }                    
                }
            }    
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        System.out.println("Fin seleccionarUsuarioDestino():"+evt.getNewValue());
    }
}
