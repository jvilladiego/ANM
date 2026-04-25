
package co.gov.anm.sgd.response;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import co.gov.anm.sgd.service.WccResponse;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the co.gov.anm.sgd.response package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RespuestaUCM_QNAME = new QName("http://response.sgd.anm.gov.co", "RespuestaUCM");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.gov.anm.sgd.response
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WccResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WccResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://response.sgd.anm.gov.co", name = "RespuestaUCM")
    public JAXBElement<WccResponse> createRespuestaUCM(WccResponse value) {
        return new JAXBElement<WccResponse>(_RespuestaUCM_QNAME, WccResponse.class, null, value);
    }

}
