package co.gov.anm.comunicaciones.control;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;

import oracle.binding.BindingContainer;

import oracle.bpel.services.workflow.worklist.adf.InvokeActionBean;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.server.ViewObjectImpl;


public class CommonJSFBean implements Serializable, Cloneable{
    public CommonJSFBean() {
        super();
    }
    
    
    protected BindingContainer getBindingContainer ( ){
        return BindingContext.getCurrent().getCurrentBindingsEntry(); 
    }
    
    protected void setElObjectIntoBinding(String expr, Object valor) {
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            Application app = facesCtx.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            ELContext elContext = facesCtx.getELContext();
            ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
            ve.setValue(elContext, valor);
    }
    
    
    protected Object getElObjectFromBinding(String expr) {
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            Application app = facesCtx.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            ELContext elContext = facesCtx.getELContext();
            ValueExpression ve = elFactory.createValueExpression(elContext, expr, Object.class);
            Object res = ve.getValue(facesCtx.getELContext());
            return res;
    }
    
    
    protected ViewObjectImpl getViewObjectWccContent ( String vo ) throws Exception{
        String amDef = "co.gov.anm.model.WebcenterContentModule";
        String config = "WebcenterContentModuleLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef,config);
        ViewObjectImpl result = (ViewObjectImpl)am.findViewObject(vo);
        return result;
    }
    
    
    protected ViewObjectImpl getViewObjectComunicaciones ( String vo ) throws Exception{
        String amDef = "co.gov.anm.model.ComunicacionesModule";
        String config = "ComunicacionesModuleLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef,config);
        ViewObjectImpl result = (ViewObjectImpl)am.findViewObject(vo);
        return result;
    }
    
    protected void showMessage(FacesMessage.Severity severity, String mensaje) {
        try {
            FacesMessage message = new FacesMessage(severity, mensaje, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.err.println("Exception mostrarMensaje"+ e);
        }
    }
    
    /**
         *M�todo que se encarga de asignar la operaci�n correspondiente a un boton.
         *Este m�todo llama al metodo setOperation de la clase InvokeActionBean.
         * @param action
         */
        protected void setOperation(ActionEvent action) {
            Application app = FacesContext.getCurrentInstance().getApplication();
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            InvokeActionBean invokeActionBean =
                (InvokeActionBean) app.getELResolver().getValue(elContext, null, "invokeActionBean");
            invokeActionBean.setOperation(action);
        }    
}
