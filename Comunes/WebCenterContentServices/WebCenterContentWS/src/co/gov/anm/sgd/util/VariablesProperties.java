package co.gov.anm.sgd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Properties;

public class VariablesProperties {

	/** Creamos un Objeto de tipo Properties */
	private Properties propiedades = new Properties();

	private static final VariablesProperties INSTANCIA = new VariablesProperties();

	public VariablesProperties() {
		/** Cargamos el archivo desde la ruta especificada */
		try {
			propiedades.load(new FileInputStream("properties/variables.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("Error, El archivo no exite");
		} catch (IOException e) {
			System.out.println("Error, No se puede leer el archivo");
		}
	}

	public String getValorPropiedad(String llave) {
		/** Obtenemos los parametros definidos en el archivo */
		String valor = propiedades.getProperty(llave);
		return valor; 
	}

	public static VariablesProperties getInstancia() {
		return INSTANCIA;
	}
}
