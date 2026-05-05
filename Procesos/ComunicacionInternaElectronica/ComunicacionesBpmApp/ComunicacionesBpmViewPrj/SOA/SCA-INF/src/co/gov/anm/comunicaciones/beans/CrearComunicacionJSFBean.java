package co.gov.anm.comunicaciones.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.datacontrol.WorkflowService;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.verification.IWorkflowContext;

import org.w3c.dom.Element;


public class CrearComunicacionJSFBean {
    

    public CrearComunicacionJSFBean() {
        super();
    }    
    
    public void cuadernoSelectListener ( ValueChangeEvent selectionEvent ){
        FacesContext context = FacesContext.getCurrentInstance();
        Object cuadernoObj = context.getApplication().evaluateExpressionGet(context, "#{bindings.Cuaderno1.inputValue}", String.class);
        Object subserieObj = context.getApplication().evaluateExpressionGet(context, "#{bindings.Subserie1.inputValue}", String.class);        
        Object subserieItemsObj = context.getApplication().evaluateExpressionGet(context, "#{bindings.Subserie1.items}", Object.class);        
        Object cuadernosItemsObj = context.getApplication().evaluateExpressionGet(context, "#{bindings.Cuaderno1.items}", Object.class);        
            
        System.out.println("***********************cuadernoObj: "+cuadernoObj);
        System.out.println("***********************subserieObj: "+subserieObj);
        System.out.println("***********************subserieItemsObj: "+subserieItemsObj.getClass());        
        System.out.println("***********************subserieItemsObj: "+subserieItemsObj.getClass());
        System.out.println("***********************cuadernosItemsObj: "+cuadernosItemsObj.getClass());
        
        String ctx = (String) context.getApplication().evaluateExpressionGet(context, "#{pageFlowScope.bpmWorklistContext}", String.class);
        String tskId = (String)context.getApplication().evaluateExpressionGet(context, "#{pageFlowScope.bpmWorklistTaskId}", String.class);
        IWorkflowServiceClient workflowSvcClient = WorkflowServiceClientFactory.getWorkflowServiceClient(WorkflowServiceClientFactory.REMOTE_CLIENT);
        ITaskQueryService wfQueryService = workflowSvcClient.getTaskQueryService();
        try{
            IWorkflowContext wfContext = wfQueryService.getWorkflowContext(ctx);
            Task myTask = wfQueryService.getTaskDetailsById(wfContext, tskId);
            Element xmlPayload = (Element) myTask.getPayloadAsElement();
            System.out.println("-----------------------"+xmlPayload);
        }catch (Exception e){
            e.printStackTrace();
        }
    }   
    
   
}
