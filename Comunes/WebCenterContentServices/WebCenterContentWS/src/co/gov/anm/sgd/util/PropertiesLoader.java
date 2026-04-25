package co.gov.anm.sgd.util;

import java.io.FileInputStream;

import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {
    
    private static final Logger logger = Logger.getLogger(PropertiesLoader.class);

    /**
     * Singleton instance of the class
     */
    private static PropertiesLoader propsLoader = null;

    private static String errorsPath;
    private static String propsPath;
    private Properties properties = null;
    private Properties errorProperties = null;

    /**
     * Método constructor de la clase.
     * @throws Exception
     */
    private PropertiesLoader() throws Exception {
        loadPropertiesFile();
    }

    /**
     * M&eacute;todo encargado de devolver una nueva instancia de la clase.
     * @return
     * @throws Exception
     */
    private static PropertiesLoader getInstance() throws Exception {
        if (propsLoader == null) {
            propsLoader = new PropertiesLoader();
        }

        return propsLoader;
    }

    /**
     * M&eacute;todo que se encarga de obtener mensajes de error de la
     * aplicación.
     *
     * @param String Propiedad a obtener del archivo de propiedades
     * @return String Mensaje de error
     */
    public static String getErrorProperty(String prop) {
        try {
            return getInstance().errorProperties.getProperty(prop);
        } catch (Exception e) {
            logger.error("Error cargando propiedad " + prop + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * M&eacute;todo que se encarga de retornar una determinada propiedad.
     * @param prop
     * @return
     */
    public static String getProperty(String prop) {
        try {
            return getInstance().properties.getProperty(prop);
        } catch (Exception e) {
            logger.error("Error cargando propiedad " + prop + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Carga el singleton de la clase y así obtener las propiedades de la
     * aplicación.
     *
     * @param context
     * @throws Exception
     */
    public static void load(String propPath, String errorPath) {
        propsPath = propPath;
        errorsPath = errorPath;
    }    

    /**
     * Método que se encarga de leer un archivo de propiedades.
     * @throws Exception
     */
    private void loadPropertiesFile() throws Exception {
        try {
            logger.debug("errorPath: " + errorsPath);
            logger.debug("propsPath: " + propsPath);

            if (propsPath != null && !propsPath.trim().equals("") &&
            	errorsPath != null && !errorsPath.trim().equals("")) {
                
                Properties props = new Properties();
                props.load(new FileInputStream(propsPath));
                properties = props;
                
                props = new Properties();                       
                props.load(new FileInputStream(errorsPath));
                errorProperties = props;
            } else {
                throw new Exception("Los archivos de propiedades no estan configurados " + 
        				"aun (" + propsPath + ", " + errorsPath + ").");
            }
        } catch (Exception e) {
            logger.error("Error cargando archivos de propiedades");

            throw e;
        }
    }
}
