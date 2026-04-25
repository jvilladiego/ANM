package co.gov.anm.sgd.util;


/**
 * La interfaz <code>Constants</code> representa las constantes de la aplicaci&oacute;n.
 *
 * @author Jainer Eduardo Quiceno Rojas [jaine.quiceno@oracle.com]
 */
public interface Constants {

    public static String OK = "0";
    public static String S  = "S";
    public static String N  = "N";

    /**
     * Nombres de servicios
     */
    public static String SVR_GET_FILE           = "GET_FILE";
    public static String SVR_CHECK_NEW          = "CHECKIN_NEW";
    public static String SVR_GET_SEARCH_RESULTS = "GET_SEARCH_RESULTS";
    public static String SVR_DOC_INFO_BY_NAME   = "DOC_INFO_BY_NAME";
    public static String SVR_FLD_FOLDER_SEARCH  = "FLD_FOLDER_SEARCH";
    public static String SVR_FLD_CREATE_FOLDER  = "FLD_CREATE_FOLDER";
    public static String SVR_FLD_CREATE_FILE    = "FLD_CREATE_FILE";
    public static String SVR_FLD_DELETE         = "FLD_DELETE";
    public static String SVR_FLD_MOVE           = "FLD_MOVE";
    public static String SVR_FLD_EDIT_FOLDER    = "FLD_EDIT_FOLDER";
    public static String SVR_FLD_BROWSE         = "FLD_BROWSE";
    public static String SVR_FLD_COPY           = "FLD_COPY";
    public static String SVR_FLD_EDIT_FILE      = "FLD_EDIT_FILE";

    /**
     * Propiedades del keystore
     */
    public static String JPS_PATH           = "JPS_PATH";
    public static String KEYSTORE_PATH      = "KEYSTORE_PATH";
    public static String KEYSTORE_PASSWORD  = "KEYSTORE_PASSWORD";
    public static String KEY_ALIAS          = "KEY_ALIAS";
    public static String KEY_PASSWORD       = "KEY_PASSWORD";

    /**
     * Propiedades del servicio web de WCC
     */
    public static String URL_WSDL_GENERIC = "URL_WSDL_GENERIC";
    public static String WCC_CONN_TIMEOUT = "WCC_CONN_TIMEOUT";
    public static String WCC_READ_TIMEOUT = "WCC_READ_TIMEOUT";
    
    /**
     * Propiedades del usuario BPM para WCC
     */
    public static String WCC_BPM_USER       = "WCC_USER";
    public static String WCC_BPM_PASSWORD   = "WCC_PASSWORD";
    
    /**
     * Propiedades del usuario CMC para WCC
     */
    public static String WCC_CMC_USER   = "WCC_CMC_USER";
    public static String WCC_CMC_PASS   = "WCC_CMC_PASS";
    
    /**
     * Servicio de de WCC por Idc
     */
    public static String URL_IDC    = "URL_IDC";
    public static String URL_IDC2    = "URL_IDC2";
    
    //Propiedades del framework JOD para generacion de PDFs
    public static String LIBRE_OFFICE_PATH = "libre.office.path";
    public static String JOD_PORT_NUMBERS = "jod.port.numbers";
    public static String JOD_TASK_QUEUE_TIMEOUT = "jod.queue.timeout";
    public static String JOD_TASK_EXEC_TIMEOUT = "jod.exec.timeout";
    public static String JOD_TASK_PER_PROCESS = "jod.task.process";
    public static String JOD_RETRY_TIMEOUT = "jod.retry.timeout";

    /**
     * Constantes de metadatos
     */
    public static String queryText                  = "QueryText";
    public static String path                       = "path";
    public static String folderPath                 = "fFolderPath";
    public static String parentPath                 = "fParentPath";
    public static String folderName                 = "fFolderName";
    public static String folderGUID                 = "fFolderGUID";
    public static String parentGUID                 = "fParentGUID";
    public static String fileName                   = "fFileName";
    public static String fileGUID                   = "fFileGUID";
    public static String fileType                   = "fFileType";
    public static String dID                        = "dID";
    public static String docName                    = "dDocName";
    public static String securityGroup              = "dSecurityGroup";
    public static String docAccount                 = "dDocAccount";
    public static String docType                    = "dDocType";
    public static String docTitle                   = "dDocTitle";
    public static String revLabel                   = "dRevLabel";
    public static String format                     = "dFormat";
    public static String primaryFile                = "primaryFile";
    public static String anmTramite                 = "xANM_Tramite";
    public static String anmTipoDocumental          = "xANM_Tipo_Documental";
    public static String anmUnidadAdministrativa    = "xANM_UnidadAdministrativa";
    public static String anmReferencia              = "xANM_Referencia";
    public static String nombreExpediente           = "xANM_Nombre_del_Expediente";
    public static String anmEstadoArchivo           = "xANM_EstadoArchivo";
    public static String xANM_Idiomas               = "xANM_Idiomas";
    public static String comments                   = "xComments";
    public static String tipoRecursoInformacion     = "xANM_TipoRecursoInformacion";
    public static String origenSolicitud            = "origenSolicitud";
    public static String docAuthor                  = "dDocAuthor";
    
    /**
     * Origenes de las solicitudes mineras
     */
    public static String SOL_RADICADOR      = "R";
    public static String SOL_EXTEMPORANEA   = "X";
    
    /**
     * Tipos de result set de las consultas a WCC
     */
    public static final String searchresultsFLD    = "search_results";
    public static final String searchresultsDOC    = "searchresults";

    /**
     * Rutas de subseries
     */
    public static final String CUADERNOS_TITULO_MINERO  = "CUADERNOS_TITULO_MINERO";
    public static final String CUADERNOS_TITULO_PIN     = "CUADERNOS_TITULO_PIN";

    public static final String TITULO_PIN = "TITULO_PIN";

    /**
     * Par�metros de servicios
     */
    public static final String PARAM_CARGAR_DOC         = "PARAM_CARGAR_DOC";
    public static final String PARAM_CREAR_SOLICITUD    = "PARAM_CREAR_SOLICITUD";
    public static final String PARAM_CREAR_TITULO       = "PARAM_CREAR_TITULO";
    public static final String PARAM_RECHAZAR_SOLICITUD = "PARAM_RECHAZAR_SOLICITUD";
    public static final String PARAM_DESCARGAR_DOC      = "PARAM_DESCARGAR_DOC";

    /**
     * Validaciones
     */
    public static int EXPEDIENTE    = 0;
    public static int TITULOS       = 1;
    public static int SOLICITUDES   = 2;

    /**
     * Constantes de las series
     */
    public static String PATH_TITULOS_MINEROS       = "PATH_TITULOS_MINEROS";
    public static String PATH_SOLICITUDES_MINERAS   = "PATH_SOLICITUDES_MINERAS";
    public static String PATH_SS_PIN                = "PATH_SS_PIN";
    public static String PATH_CONS_COM              = "PATH_CONS_COM";
    public static String PATH_PQRS                  = "PATH_PQRS";
    
    public static String PATH_PLANTILLAS    = "PATH_PLANTILLAS";
    
    public static String PATH_VIRTUAL_DIRECTORY_HTTP = "virtual.directory.path.http";
    public static String PATH_VIRTUAL_DIRECTORY = "virtual.directory.path";

    /**
     * Constantes que definen los mensajes de error
     */
    public static String WCC_ERRORS = "WCC_ERRORS";
    public static String ERWCC00    = "ERWCC00";
    public static String ERWCC06    = "ERWCC06";
    public static String ERWCC07    = "ERWCC07";
    public static String ERWCC08    = "ERWCC08";
    public static String ERWCC09    = "ERWCC09";
    public static String ERWCC01    = "ERWCC01";
    public static String ERWCC02    = "ERWCC02";
    public static String ERWCC03    = "ERWCC03";
    public static String ERWCC04    = "ERWCC04";
    public static String ERWCC05    = "ERWCC05";
    public static String ERWCC10    = "ERWCC10";
    public static String ERWCC11    = "ERWCC11";
    public static String ERWCC12    = "ERWCC12";
    public static String ERWCC13    = "ERWCC13";
    public static String ERWCC14    = "ERWCC14";
    public static String ERWCC15    = "ERWCC15";
    public static String ERWCC16    = "ERWCC16";
    public static String ERWCC17    = "ERWCC17";
    public static String ERWCC18    = "ERWCC18";
    public static String ERWCC19    = "ERWCC19";
    public static String ERWCC20    = "ERWCC20";
    public static String ERWCC21    = "ERWCC21";
    public static String ERWCC22    = "ERWCC22";
    
    /**
     * Propiedades Mail Service 
     */
    public static String MAIL_SMTP_AUTH = "mail.smtp.auth"; 
    public static String MAIL_SMTP_TLS_ENABLE = "mail.smtp.starttls.enable"; 
    public static String MAIL_SMTP_HOST = "mail.smtp.host"; 
    public static String MAIL_SMTP_PORT = "mail.smtp.port"; 
    
    public static String MAIL_ACCOUNT_USERNAME = "mail.account.username"; 
    public static String MAIL_ACCOUNT_PASSWORD = "mail.account.password"; 
    
    
}
