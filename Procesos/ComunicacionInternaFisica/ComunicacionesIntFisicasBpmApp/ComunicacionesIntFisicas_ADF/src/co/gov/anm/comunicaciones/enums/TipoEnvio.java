package co.gov.anm.comunicaciones.enums;


public enum TipoEnvio {
    
    EXPRESS ("1","Mensajería Express"),
    VIA_TERRESTRE ("2","Mensajería Vía Terrestre"),
    INTERNACIONAL ("3", "Mensajería Internacional"),
    FACTURAS ("4", "Mensajería de Facturas"),
    MOTORIZADA ("5", "Mensajería Motorizada");
    
    private String codigoTipo;
    private String nombreTipo;
    
    TipoEnvio ( String codigo, String nombre ){
            this.codigoTipo = codigo;
            this.nombreTipo = nombre;
    }

    public String getCodigoTipo() {
            return codigoTipo;
    }

    public void setCodigoTipo(String codigoTipo) {
            this.codigoTipo = codigoTipo;
    }

    public String getNombreTipo() {
            return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
            this.nombreTipo = nombreTipo;
    }
    
    
    public static TipoEnvio getTipoArchivoPorCod ( String cod ) throws Exception{
            for ( TipoEnvio tp : TipoEnvio.values() ){
                    if ( tp.codigoTipo.equals(cod) ){
                            return tp;
                    }
            }
            throw new Exception("Tipo de envío no soportado: "+cod);
    }
}
