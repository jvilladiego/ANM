package co.gov.anm.sgd.util;

import co.gov.anm.sgd.exception.WSException;
import static co.gov.anm.sgd.util.Constants.ERWCC02;
import static co.gov.anm.sgd.util.Constants.ERWCC06;
import static co.gov.anm.sgd.util.Constants.ERWCC19;
import static co.gov.anm.sgd.util.Constants.PARAM_CARGAR_DOC;
import static co.gov.anm.sgd.util.Constants.PARAM_CREAR_SOLICITUD;
import static co.gov.anm.sgd.util.Constants.PARAM_CREAR_TITULO;
import static co.gov.anm.sgd.util.Constants.PARAM_DESCARGAR_DOC;
import static co.gov.anm.sgd.util.Constants.PARAM_RECHAZAR_SOLICITUD;
import static co.gov.anm.sgd.util.Constants.PATH_SOLICITUDES_MINERAS;
import static co.gov.anm.sgd.util.Constants.PATH_TITULOS_MINEROS;
import static co.gov.anm.sgd.util.Constants.SOL_EXTEMPORANEA;
import static co.gov.anm.sgd.util.Constants.SOL_RADICADOR;

import java.lang.annotation.Annotation;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * La clase <code>Utilities</code> contiene utilidades comunes para toda
 * la aplicaci&oacute;n.
 *
 * @author Jainer Eduardo Quiceno Rojas [jaine.quiceno@oracle.com]
 */
public class Utilities {

    public static final Logger logger = Logger.getLogger(Utilities.class);

    private static List<String> cuadernosTituloPin = null;
    private static List<String> cuadernosTituloMinero = null;

    private static Map<String, List<String>> parametersMap = null;
    private static Map<String, String> wccErrorsMap = null;

    public static void loadAppProperties() throws Exception {
        List<String> parametersList = null;

        //Carga nombres de cuadernos de los titulos
        cuadernosTituloPin = new ArrayList<String>();

        for (String nombre : PropertiesLoader.getProperty(Constants.CUADERNOS_TITULO_PIN).split(";")) {
            cuadernosTituloPin.add(nombre);
        }

        cuadernosTituloMinero = new ArrayList<String>();

        for (String nombre : PropertiesLoader.getProperty(Constants.CUADERNOS_TITULO_MINERO).split(";")) {
            cuadernosTituloMinero.add(nombre);
        }

        parametersMap = new HashMap<String, List<String>>();

        parametersList = new ArrayList<String>();
        for (String param : PropertiesLoader.getProperty(PARAM_CARGAR_DOC).split(";")) {
            parametersList.add(param);
        }
        parametersMap.put(PARAM_CARGAR_DOC, parametersList);

        parametersList = new ArrayList<String>();
        for (String param : PropertiesLoader.getProperty(PARAM_CREAR_SOLICITUD).split(";")) {
            parametersList.add(param);
        }
        parametersMap.put(PARAM_CREAR_SOLICITUD, parametersList);

        parametersList = new ArrayList<String>();
        for (String param : PropertiesLoader.getProperty(PARAM_CREAR_TITULO).split(";")) {
            parametersList.add(param);
        }
        parametersMap.put(PARAM_CREAR_TITULO, parametersList);

        parametersList = new ArrayList<String>();
        for (String param : PropertiesLoader.getProperty(PARAM_RECHAZAR_SOLICITUD).split(";")) {
            parametersList.add(param);
        }
        parametersMap.put(PARAM_RECHAZAR_SOLICITUD, parametersList);

        parametersList = new ArrayList<String>();
        for (String param : PropertiesLoader.getProperty(PARAM_DESCARGAR_DOC).split(";")) {
            parametersList.add(param);
        }
        parametersMap.put(PARAM_DESCARGAR_DOC, parametersList);

        wccErrorsMap = new HashMap<String, String>();
        for (String error : PropertiesLoader.getErrorProperty(Constants.WCC_ERRORS).split(";")) {
            String[] errorsArray = error.split("|");

            wccErrorsMap.put(errorsArray[0], errorsArray[1]);
        }
    }

    public static void validateParams(Map<String, String> metadataMap, List<String> paramList) throws WSException {
        for (String param : paramList) {
            logger.debug("validando parametro: " + param);

            if (param.equals("dDocAccount")) {
                continue;
            }

            if (!metadataMap.containsKey(param)) {
                throw new WSException(ERWCC02, MessageFormat.format(PropertiesLoader.getErrorProperty(ERWCC02), param));
            } else if (metadataMap.get(param) == null) {
                throw new WSException(ERWCC06, MessageFormat.format(PropertiesLoader.getErrorProperty(ERWCC06), param));
            }
        }
    }

    public static void validateRequestOrigin(String origin) throws WSException {
        if (!SOL_RADICADOR.equals(origin) && SOL_EXTEMPORANEA.equals(origin)) {
            throw new WSException(ERWCC19, PropertiesLoader.getErrorProperty(ERWCC19));
        }
    }

    /**
     * Valida que el expediente exista en la taxonom&iacute;a de T&iacute;tulos Mineros y/o
     * Solicitudes Mineras.
     */
    public static Map<String, String> validateRecordTaxonomy(List<Map<String, String>> resultSetList,
                                                             int validationType) {
        for (Map<String, String> resultSet : resultSetList) {
            String path = resultSet.get("path");

            switch (validationType) {
            case 0:
                if (path.startsWith(PropertiesLoader.getProperty(PATH_SOLICITUDES_MINERAS)) ||
                    path.startsWith(PropertiesLoader.getProperty(PATH_TITULOS_MINEROS))) {
                    logger.debug("la carpeta es solicitud o titulo");

                    return resultSet;
                }
                break;
            case 1:
                if (path.startsWith(PropertiesLoader.getProperty(PATH_TITULOS_MINEROS))) {
                    logger.debug("la carpeta es titulo");
                    return resultSet;
                }
                break;
            case 2:
                if (path.startsWith(PropertiesLoader.getProperty(PATH_SOLICITUDES_MINERAS))) {
                    return resultSet;
                }
                break;
            default:
                break;
            }
        }
        return null;
    }

    public static String getDate(String formato) {
        SimpleDateFormat formateDate = new SimpleDateFormat(formato);

        return formateDate.format(Calendar.getInstance().getTime());
    }

    public static String date2String(Date date, String formato) {
        SimpleDateFormat formateDate = new SimpleDateFormat(formato);

        return formateDate.format(date);
    }

    public static Calendar date2Calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    /**
     * Valida que el valor tengo la longitud esperada, sino, trunca el valor
     * @param value
     * @return
     */
    public static String truncateStringValue(String value, int longitud) {
        String stringValue = null;
        if (value != null) {
            if (value.length() > longitud) {
                stringValue = value.substring(0, longitud);
            } else {
                stringValue = value;
            }
        } else {
            stringValue = " ";
        }
        return stringValue;
    }
    
    public static void main (String args[]) {
        System.out.println(truncateStringValue("2222222222222222222222", 20));
    }
    
    public static Integer truncateIntegerValue (Integer value, int longitud) {
        Integer intValue = null;
        if (value != null) {
            if (value.toString().length() > longitud) {
                intValue = Integer.valueOf(value.toString().substring(0, longitud));
            } else {
                intValue = value;
            }
        } else {
            intValue = 0;
        }
        return intValue;
    }

    @SuppressWarnings("unchecked")
    private String getClassAnnotationValue(Class classType, Class annotationType, String attributeName) {
        String value = null;

        @SuppressWarnings("unchecked")
        Annotation annotation = classType.getAnnotation(annotationType);
        if (annotation != null) {
            try {
                value = (String) annotation.annotationType()
                                           .getMethod(attributeName)
                                           .invoke(annotation);
            } catch (Exception ex) {
                logger.error("error obteniendo valor de la anotacion: " + ex.getMessage(), ex);
            }
        }

        return value;
    }

    public static void buildUCMResponseError(String code, String message) throws WSException {
        throw new WSException("ERUCM" + code.substring(1), message);
    }

    public static List<String> getParameters(String parameter) {
        return parametersMap.get(parameter);
    }

    public static List<String> getCuadernosTituloPIN() {
        return cuadernosTituloPin;
    }

    public static List<String> getCuadernosTituloMinero() {
        return cuadernosTituloMinero;
    }
}
