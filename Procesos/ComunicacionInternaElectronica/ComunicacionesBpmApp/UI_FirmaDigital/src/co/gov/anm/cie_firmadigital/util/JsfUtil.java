package co.gov.anm.cie_firmadigital.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;
/*
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import oracle.bpel.services.workflow.worklist.adf.ADFWorklistBeanUtil;
*/
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import oracle.bpel.services.workflow.worklist.adf.ADFWorklistBeanUtil;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaManager;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * Clase de Utileria para el manejo de componentes java basicos de JSF (Java Server Faces)
 * y algunos de ADF Context (al final de la clase) ...
 * Para metodos adicionales de ADF para componentes tipo caja negra
 * ver clase  @see AdfBcUtil
 *
 * @author DLPS
 * @modified Jhonattan Camargo G.
 * @dateModified 16/09/2016
 * @version 2.1
 */
public class JsfUtil {

    /**
     * Package containing the bundle associated with the web
     */
    private static final String RUTA_BUNDLE = "co.org.liberty.pqr.web.bundle.LibertyPQRWebBundle";

    private JsfUtil() {
        super();
        throw new AssertionError();
    }

    /**
     * Retorna el current Context
     *
     * Calls to FacesContext.getCurrentInstance() return a thread local data structure. Request and unscoped managed beans are of course safe as well.
     *
     * @return faces context
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Add JSF error message.
     * @param msg error message string
     */
    public static void addFacesErrorMessage(String msg) {
        FacesContext ctx = getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, msg);
        ctx.addMessage(null, fm);
    }

    /**
     * Add JSF info message.
     * @param msg info message string
     */
    public static void addFacesInfoMessage(String msg) {
        FacesContext ctx = getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, null, msg);
        ctx.addMessage(null, fm);
    }

    /**
     * Add JSF warning message.
     * @param msg warning message string
     */
    public static void addFacesWarningMessage(String msg) {
        FacesContext ctx = getFacesContext();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, null, msg);
        ctx.addMessage(null, fm);
    }

    /**
     * execute bindings operation
     * @param operationName name operation
     * @return
     */
    public static Object executeOperation(String operationName) {
        return getOperationBinding(operationName).execute();
    }

    /**
     * execute bindings operation catching transaction exception
     * @param operationName name operation
     * @return
     */
    public static List executeOperationCatchException(String operationName) {

        return ((OperationBinding) getOperationBinding(operationName).execute()).getErrors();
    }

    /**
     * execute a methos action and return errro list
     * @param operationName name method action
     * @return List errors
     */
    public static List executeMethodActionWithExceptionReturn(String operationName) {
        OperationBinding method = getOperationBinding(operationName);
        method.execute();

        return method.getErrors();
    }

    /**
     * Get operation bindings
     * @param operationName name operation bindings
     * @return
     */
    public static OperationBinding getOperationBinding(String operationName) {
        return getBindingContainer().getOperationBinding(operationName);
    }

    /**
     * Get the bindings container
     * @return bindings container
     */
    public static DCBindingContainer getBindingContainer() {
        return (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    /**
     * Get application module for an application module data control by name.
     * @param name application module data control name
     * @return ApplicationModule
     */
    public static ApplicationModule getApplicationModuleForDataControl(String name) {
        return (ApplicationModule) JsfUtil.getElObject("#{data." + name + ".dataProvider}");
    }

    /**
     * Internal method to pull out the correct local
     * message bundle
     */
    public static ResourceBundle getAppBundle() {
        return ResourceBundle.getBundle(JsfUtil.RUTA_BUNDLE);
    }

    /**
     * <p>Método sobrecargado que permite obtener un recurso bundle con base a
     * la ruta que especifiquemos</p>
     *
     * @param pathBundle ruta con nombre del recurso bundle a buscar
     * @return bundle Recurso encontrado
     */
    public static ResourceBundle getAppBundle(String pathBundle) {
        return ResourceBundle.getBundle(pathBundle);
    }

    /**
     * Get iterator from executables
     * @param iteratorName
     * @return
     */
    public static DCIteratorBinding getIterator(String iteratorName) {
        return getBindingContainer().findIteratorBinding(iteratorName);
    }

    /**
     * Metodo que devuelve el tamaño de las filas de un iterador
     *
     * @param iteratorName Nombre del iterador
     * @return
     */
    public static int getIteratorLengh(String iteratorName) {
        int lenght = 0;

        if (iteratorName != null) {
            DCIteratorBinding iterator = getIterator(iteratorName);

            if (iterator != null) {
                iterator.executeQuery();

                if (iterator.getAllRowsInRange() != null) {
                    lenght = iterator.getAllRowsInRange().length;
                } //End if
            } //End if
        } //End if

        return lenght;
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static Object getElObject(String expression) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

        return valueExp.getValue(elContext);
    }

    /**
     * Replaces the object (value) of a JSF EL expression (eg
     * Manage bean "# {mibean}" by the value in newValue.
     *
     * @param expression EL expression
     * @param newValue new value to set
     * @return el ValueExpression If you want to work with besides saving in ELContext said "binding"
     * @throws Exception = exception
     */
    public static ValueExpression setExpValue(String expression, Object newValue) throws Exception {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

        //Check that the input newValue can be cast to the property type
        //expected by the managed bean.
        //If the managed Bean expects a primitive we rely on Auto-Unboxing
        Class bindClass = valueExp.getType(elContext);

        if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
            valueExp.setValue(elContext, newValue);
        } else {
            System.err.println("No se pudo realizar el SET del valor de expresion EL porque los tipos de datos no coincidian bindClass=" +
                               bindClass.getCanonicalName() + " newValueClass=" +
                               newValue.getClass().getCanonicalName());
        } //End if else

        return valueExp;
    }

    /**
     * Se encarga de verificar si se esta realizando postbanck a la página
     * @return Bandera que define si se esta o haciendo postback sobre a página
     */
    public static boolean isPostback() {
        return Boolean.TRUE.equals(evaluateEL("#{adfFacesContext.postback}"));
    }


    /**
     * Invoca un metodo de tipo ActionListener (el que usan los button para navegar para invocar una funcionalidad, antes del action)
     * Dichos metodos tienen la signatura void METODO(ActionEvent evt);
     * por lo que unico que se requiere es el expresion EL que apunta al metodo que se desea invocar y el
     * UIComponent con el que se armara el ActionEvent.
     *
     * @param expEL expresion EL que apunta al metodo que se desea invocar
     * @param component UIComponent donde se origina el evento para el ACtionListener
     * @throws Exception = exception
     */
    public static void invokeActionListenerMethod(String expEL, UIComponent component) throws Exception {
        ActionEvent ae = new ActionEvent(component);
        JsfUtil.resolveMethodExpression(expEL, null, new Class[] { ActionEvent.class }, new Object[] { ae });
    }

    /**
     * Permite invocar un metodo de EL JSF tipo #{mibean.miactionmetodo}
     * como introspeccion pero resolviendo primero la expresion EL
     *
     * @param expEL expresion EL que apunta al metodo que se desea invocar
     * @param returnType    Class del retorno de dicho metodo
     * @param argTypes      Arreglo de class de los tipos de datos que recibe el metodo por argumento
     * @param argValues     Valores de los argumentos sencuenciales dentro de un Object[]
     * @return object
     * @throws Exception = exception
     */
    public static Object resolveMethodExpression(String expEL, Class returnType, Class[] argTypes,
                                                 Object[] argValues) throws Exception {
        FacesContext facesContext = getFacesContext();
        ELContext elContext = facesContext.getELContext();
        MethodExpression me = createMethodExpression(expEL, returnType, argTypes);

        return me.invoke(elContext, argValues);
    }

    /**
     * Crea el objeto MethodExpression de EL para un metodo que sea apuntado por una expresion EL.
     *
     * @param expEL expresion EL que apunta al metodo que se desea invocar
     * @param returnType    Class del retorno de dicho metodo
     * @param argTypes      Arreglo de class de los tipos de datos que recibe el metodo por argument
     * @return method expression
     */
    public static MethodExpression createMethodExpression(String expEL, Class returnType, Class[] argTypes) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expEL, returnType, argTypes);
        return methodExpression;
    }

    /**
     * Programmatic invocation of a method that an EL evaluates to.
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the parameters
     * @param params Array of Object defining the values of the parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

        return exp.invoke(elContext, params);
    }

    /**
     * Programmatic evaluation of EL.
     *
     * @param el EL to evaluate
     * @return Result of the evaluation
     */
    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        return exp.getValue(elContext);
    }

    /**
     * Invoca un metodo de tipo ActionListener (el que usan los button para navegar para invocar una funcionalidad, antes del action)
     * Dichos metodos tienen la signatura void METODO(ActionEvent evt);
     * por lo que unico que se requiere es el expresion EL que apunta al metodo que se desea invocar y el
     * y el ActionEvent.
     *
     * @param expEL expresion EL que apunta al metodo que se desea invocar
     * @param evt Evento del ActionListener
     * @throws Exception = exception
     */
    public static void invokeActionListenerMethod(String expEL, ActionEvent evt) throws Exception {
        JsfUtil.resolveMethodExpression(expEL, null, new Class[] { ActionEvent.class }, new Object[] { evt });
    }

    /**
     *Permite cargar una lista de items personalizada
     * @param iterator Iterador que contiene los datos a listar
     * @param nameValue Nombre del parámetro asociado al valor y que esta contenido en el iterador
     * @param nameLabel Nombre del parámetro asociado al label y que esta contenido en el iterador
     * @return Lista de items
     */
    public static List<SelectItem> loadItems(Object iterator, String nameValue, String nameLabel) {
        List<SelectItem> items = new ArrayList<SelectItem>();
        Row[] actividadesRows = null;

        if (iterator instanceof DCIteratorBinding) {
            actividadesRows = ((DCIteratorBinding) iterator).getAllRowsInRange();

        } else if (iterator instanceof ViewObject) {
            actividadesRows = ((ViewObject) iterator).getAllRowsInRange();
        } //End if else if

        //Recorremos el set de filas
        for (Row row : actividadesRows) {
            //Obtenemos los valores
            Object value = row.getAttribute(nameValue);
            String label = (String) row.getAttribute(nameLabel);
            //Egregamos los servicios
            items.add(new SelectItem(value, label));
        } //End for

        return items;
    }

    /**
     * Method for invoking a script method implemented on the page or fragment scripts section.
     * @param methodSignature - the script method signature
     */
    public static void invokeScriptMethod(String methodSignature) {
        FacesContext facesContext = getFacesContext();
        ExtendedRenderKitService erks = Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);
        erks.addScript(facesContext, methodSignature);
    }

    /**
     * Actualiza un objeto faces dado el contexto de invocación
     * @param component Componente ADF Faces
     */
    public static void updateFacesComponent(UIComponent component) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }

    /**
     * <p>Invoca el evento asociado al binding de evento asociado a un componenete UI</p>
     *
     * @param componentUI Componente UI
     * @throws Exception Excepción asociada al la invocación del evento
     */
    public static void invokeProcessActionEventBinding(UIComponent componentUI) throws Exception {
        invokeActionListenerMethod("#{bindings.eventBinding.listener.processAction}", componentUI);
    }

    /**
     * Returns the machine's host name
     * @return host name
     */
    public static String getHostName() throws UnknownHostException {
        InetAddress dirreccionIp = InetAddress.getLocalHost();
        return dirreccionIp.getHostName();
    }

    /**
     * Returns the host machine IP address
     * @return IP address
     */
    public static String getIpAddress() throws UnknownHostException {
        InetAddress dirreccionIp = InetAddress.getLocalHost();
        return dirreccionIp.getHostAddress();
    }

    /**
     * Retorna el usuario actual del contexto de BPM
     * @return
     *
     */
    /*
    public static String getUserLogin() {
        String usuario = "";
        IWorkflowServiceClient wfSvcClient;
        ITaskQueryService queryService;
        IWorkflowContext wfContext;

        try {
            String contextStr = ADFWorklistBeanUtil.getWorklistContextId();
            wfSvcClient       = ADFWorklistBeanUtil.getWorkflowServiceClient();
            queryService      = wfSvcClient.getTaskQueryService();
            wfContext         = queryService.getWorkflowContext(contextStr);
            usuario           = wfContext.getUser();

        } catch (Exception e) {
            e.printStackTrace();
        }//End try - catch

        return usuario;
    }

*/

    /**
     * Método que nos permite ejecutar un viewCriteria desde un ViewObject
     *
     * @param viewObject Nombre del ViewObject que contiene el ViewCriteria
     * @param nameViewCriteria Nombre del ViewCriteria que querramos ejecutar
     * @param bindVariable BindVariable de consulta
     * @param value Valor de busqueda
     * @return ViewObject con la informaciï¿½n resultante
     */
    public static ViewObject executeViewCriteriaFromViewObject(ViewObject viewObject, String nameViewCriteria,
                                                               Object bindVariable, Object value) {
        //Obtenemos el manejador de ViewCriterias
        ViewCriteriaManager viewCriteriaManager = viewObject.getViewCriteriaManager();
        //Obtenemos el ViewCriteria que necesitemos con base al parï¿½metro de entrada
        ViewCriteria viewCriteria = viewCriteriaManager.getViewCriteria(nameViewCriteria);
        //String pathViewCriteria = viewCriteria.getRootCriteriaRelativeName();//Se realizo por cuestiones de pruebas

        //Aplicamos el ViewCriteria que se encontro a nuestro ViewObject
        viewObject.applyViewCriteria(viewCriteria);
        //Asignamos los valores correspondientes al BindVariable que se pasa como parï¿½metro de busqueda
        viewObject.setNamedWhereClauseParam(bindVariable.toString(), value.toString());
        //Ejecutamos el viewCriteria
        viewObject.executeQuery();
        //Retornamos los valores consultados
        return viewObject;
    }

    /**
     *<p>Método que obtiene un View solicitado a partir de un ApplicationModule definido</p>
     * @param nameModule Nombre del ApplictionModule al que se esta referenciando
     * @param nameView Nombre del View contenido en el ApplicationModule
     * @return El View consultado
     */
    public static ViewObject getViewObjectFromApplicationModule(String nameModule, String nameView) {
        ApplicationModule am = getApplicationModuleForDataControl(nameModule);
        ViewObject viewObject = am.findViewObject(nameView);
        return viewObject;
    }

    /**
     * Open a new browser tab/window starting a new bounded task flow.
     *
     * @param taskFlowId - id of bounded task flow to show in new window
     * @param taskFlowParams - params for the task flow (if any)
     * @param windowName - name of browser tab/window (window.name)
     * @param openInWindow - true will open a browser window (if settings of the browser
     *     allow this), false will open a new browser tab.
     */
    public static void launchTaskFlowInNewWindow(TaskFlowId taskFlowId, Map taskFlowParams, String windowName,
                                                 boolean openInWindow) {
        launchTaskFlowInNewWindow(taskFlowId, taskFlowParams, windowName, openInWindow, 1000, 750);

    }


    /**
     * Open a new browser tab/window starting a new bounded task flow.
     *
     * @param taskFlowId - id of bounded task flow to show in new window
     * @param taskFlowParams - params for the task flow (if any)
     * @param windowName - name of browser tab/window (window.name)
     * @param openInWindow - true will open a browser window (if settings of the browser
     *     allow this), false will open a new browser tab.
     * @param width
     * @param height
     */
    public static void launchTaskFlowInNewWindow(TaskFlowId taskFlowId, Map taskFlowParams, String windowName,
                                                 boolean openInWindow, int width, int height) {
        String url = ControllerContext.getInstance().getTaskFlowURL(false, taskFlowId, taskFlowParams);

        if (url == null) {
            throw new Error("No se puede iniciar la ventana para la tarea con identificador de flujo " + taskFlowId);
        } //End if

        FacesContext context = FacesContext.getCurrentInstance();
        ExtendedRenderKitService extendedRenderKitService =
            Service.getService(context.getRenderKit(), ExtendedRenderKitService.class);

        // Build javascript to open a new browser tab/window
        StringBuilder script = new StringBuilder();

        // Unable to get a named firefox tab to gain focus.  To workaround
        // issue we close the tab first, then open it.
        if (!openInWindow && windowName != null) {

            script.append("var hWinx = window.open(\"");
            script.append("about:blank"); // the URL
            script.append("\",\"");
            script.append(windowName);
            script.append("\"");
            script.append(");");
            script.append("\n");
            script.append("hWinx.close();\n");
        } //End if

        // Set a variable with the window properties
        script.append("var winProps = \"status=yes,toolbar=no,copyhistory=no,width=" + width + ",height=" + height +
                      "\";");
        // If we aren't going to open in a new window, then clear the window properties
        if (!openInWindow) {
            script.append("winProps = '';");
        } //End if

        // Set isOpenerValid to true if window.opener (a parent window) is defined and open
        script.append("var isOpenerValid = (typeof(window.opener) != 'undefined' && window.opener != undefined && !window.opener.closed);");
        // Set useProps to true if openInWindow is true or isOpenerValid is true
        script.append("var useProps = (" + openInWindow + " || isOpenerValid);");
        // Set win to the current window, unless we need to use the parent, then set to window.opener (the parent window)
        script.append("var win = window; if (typeof(isChildWindow) != 'undefined' && isChildWindow != undefined && isChildWindow == true && isOpenerValid) {win = window.opener;}");
        // Set hWin to the window returned by calling open on win
        script.append("var hWin = win.open(\"");
        script.append(url); // the URL
        script.append("\",\"");
        script.append(windowName);
        script.append("\"");
        script.append(", winProps");
        script.append(");");
        // Set focus to the window opened.
        script.append("hWin.focus();");

        extendedRenderKitService.addScript(context, script.toString());
    }

    /**
     * Permite enviar un partial triget a un componente UI
     * @param component
     */
    public static void addPartialTriger(UIComponent component) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static Object resolveExpression(String expression) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }
    /**
     * Retorna el usuario actual del contexto de BPM
     * @return
     *
     */
    public static String getUserLogin() {
        String usuario = ""; 
        IWorkflowServiceClient wfSvcClient; 
        ITaskQueryService queryService; 
        IWorkflowContext wfContext; 
        
        try {        
            String contextStr = ADFWorklistBeanUtil.getWorklistContextId(); 
            wfSvcClient       = ADFWorklistBeanUtil.getWorkflowServiceClient(); 
            queryService      = wfSvcClient.getTaskQueryService(); 
            wfContext         = queryService.getWorkflowContext(contextStr); 
            usuario           = wfContext.getUser(); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }//End try - catch
        
        return usuario; 
    }
    public static void errorMenssage(String clientId,String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }
    public static void warningMenssage(String clientId ,String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, mensaje, null);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }
    public static void infoMenssage(String clientId ,String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }
    public static void fatalMenssage(String clientId, String mensaje) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensaje, null);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }
}
// ******* Free Use License GNU  FuZionTEK (C) 2006 ********
