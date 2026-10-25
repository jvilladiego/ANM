package co.gov.anm.cee.utils;


public enum PropertiesKeys {
    
    JNDI_LDAP_URL("ad.ldap.url"),
    JNDI_LDAP_PRINCIPAL("ad.security.principal"),
    JNDI_LDAP_CREDENTIALS("ad.security.credentials"),
    JNDI_SEARCH_BASE("ad.search.base"),
    LOCAL_TMP_PATH("local.tmp.file.path"),
    VIRTUAL_DIRECTORY_PATH("virtual.directory.path.http"),
    PQRS_WSDL_URL("pqrs.proxy.webservice"),
    TIMEOUT_CONEXION("timeout.conexion"),
    TIMEOUT_SOLICITUD("timeout.solicitud"),
    COMUNICACIONES_WSDL_URL("comunicaciones.proxy.webservice"),
    DEFENSAJURIDICA_WSDL_URL("defensajuridica.proxy.webservice"),
    LOG_4J_PROPS("log4j.properties.file"),
    DOCUMENT_WSDL_URL("document.proxy.webservice"),
    COD_DEP_COMUNICACIONES("cod.dep.comunicaciones"),
    SGD_FIRMA_WS_URL("sgd.firma.ws.url"),
    SGD_FIRMAR_ID("firmar.doc.identificacion"),
    SGD_FIRMAR_USR("firmar.doc.usr"),
    SGD_FIRMAR_PWD("firmar.doc.pwd");
    
    
    private String key;
    
    private PropertiesKeys ( String key ) {
        this.key = key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}

