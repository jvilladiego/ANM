package co.gov.anm.comunicaciones.control;

import co.gov.anm.comunicaciones.enums.TipoEnvio;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;


@ManagedBean(name = "ConfirmarComunicacionJSFBean")
@ViewScoped
public class ConfirmarComunicacionJSFBean extends CommonJSFBean {

    private Collection<SelectItem> tiposEnvio = new ArrayList<>();

    public ConfirmarComunicacionJSFBean() {
        super();
    }

    @PostConstruct
    public void init() {
        for (TipoEnvio tp : TipoEnvio.values()) {
            tiposEnvio.add(new SelectItem(tp.getCodigoTipo(), tp.getNombreTipo()));
        }
        
        System.out.println("Estado Comunicación: " + (String)super.getElObjectFromBinding("#{bindings.idEstado.inputValue}"));
        
        //Cuando el id de estado es "Envío" inicializa la fecha de envío
        if (((String)super.getElObjectFromBinding("#{bindings.idEstado.inputValue}")).equals("1")) {
            try {
                super.setElObjectIntoBinding("#{bindings.fecEnvio.inputValue}", new Timestamp(System.currentTimeMillis()));
                
                System.out.println("valor de la fecha: " + super.getElObjectFromBinding("#{bindings.fecEnvio.inputValue}"));
            } catch (Exception e) {            
                e.printStackTrace();
            }   
        }
    }

    public void cambiarTipoEnvio(ValueChangeEvent evt) {
        System.out.println("Inicio cambiarTipoEnvio():" + evt.getOldValue());

        String newValue = (String) evt.getNewValue();

        try {
            if (newValue != null) {
                for (TipoEnvio tp : TipoEnvio.values()) {
                    if (tp.getCodigoTipo().equals(newValue)) {
                        System.out.println("setea tipo de envio: " + newValue);

                        super.setElObjectIntoBinding("#{bindings.nombreEnvio.inputValue}", tp.getNombreTipo());

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Fin cambiarTipoEnvio():" + evt.getNewValue());
    }
    
    public void confirmarComunicacion ( ActionEvent evt ) {
        try {
            if (super.getElObjectFromBinding("#{bindings.idEnvio.inputValue}") == null ||
                ((String)super.getElObjectFromBinding("#{bindings.idEnvio.inputValue}")).trim().equals("")) {
                super.showMessage(FacesMessage.SEVERITY_WARN, "Debe diligenciar el Tipo de Envío. Por favor verificar.");
                
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setOperation(evt);
    }

    public void setTiposEnvio(Collection<SelectItem> tiposEnvio) {
        this.tiposEnvio = tiposEnvio;
    }

    public Collection<SelectItem> getTiposEnvio() {
        return tiposEnvio;
    }
}
