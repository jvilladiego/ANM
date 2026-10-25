package co.gov.anm.cee.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;
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
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewCriteriaManager;
import oracle.jbo.ViewObject;


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
