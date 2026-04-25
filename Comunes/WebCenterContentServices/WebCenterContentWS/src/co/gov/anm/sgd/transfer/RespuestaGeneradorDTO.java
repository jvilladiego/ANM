package co.gov.anm.sgd.transfer;


public class RespuestaGeneradorDTO {
	
	private String respuesta;
	
	private String rutaArchivoFinal;
	
	private String trazaError;

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRutaArchivoFinal() {
		return rutaArchivoFinal;
	}

	public void setRutaArchivoFinal(String rutaArchivoFinal) {
		this.rutaArchivoFinal = rutaArchivoFinal;
	}

	public String getTrazaError() {
		return trazaError;
	}

	public void setTrazaError(String trazaError) {
		this.trazaError = trazaError;
	}

}
