
package co.gov.anm.sgd.transfer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import co.gov.anm.sgd.service.ModificacionExpediente;
import co.gov.anm.sgd.service.Solicitud;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the co.gov.anm.sgd.transfer package.
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

    private final static QName _RechazoSolicitud_QNAME =
        new QName("http://transfer.sgd.anm.gov.co", "RechazoSolicitud");
    private final static QName _ModificacionExpediente_QNAME =
        new QName("http://transfer.sgd.anm.gov.co", "ModificacionExpediente");
    private final static QName _CreacionTitulo_QNAME = new QName("http://transfer.sgd.anm.gov.co", "CreacionTitulo");
    private final static QName _NroPlacaValidacion_QNAME =
        new QName("http://transfer.sgd.anm.gov.co", "NroPlacaValidacion");
    private final static QName _CreacionSolicitudMinera_QNAME =
        new QName("http://transfer.sgd.anm.gov.co", "CreacionSolicitudMinera");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.gov.anm.sgd.transfer
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     */
    @XmlElementDecl(namespace = "http://transfer.sgd.anm.gov.co", name = "RechazoSolicitud")
    public JAXBElement<Solicitud> createRechazoSolicitud(Solicitud value) {
        return new JAXBElement<Solicitud>(_RechazoSolicitud_QNAME, Solicitud.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificacionExpediente }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ModificacionExpediente }{@code >}
     */
    @XmlElementDecl(namespace = "http://transfer.sgd.anm.gov.co", name = "ModificacionExpediente")
    public JAXBElement<ModificacionExpediente> createModificacionExpediente(ModificacionExpediente value) {
        return new JAXBElement<ModificacionExpediente>(_ModificacionExpediente_QNAME, ModificacionExpediente.class,
                                                       null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     */
    @XmlElementDecl(namespace = "http://transfer.sgd.anm.gov.co", name = "CreacionTitulo")
    public JAXBElement<Solicitud> createCreacionTitulo(Solicitud value) {
        return new JAXBElement<Solicitud>(_CreacionTitulo_QNAME, Solicitud.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://transfer.sgd.anm.gov.co", name = "NroPlacaValidacion")
    public JAXBElement<String> createNroPlacaValidacion(String value) {
        return new JAXBElement<String>(_NroPlacaValidacion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Solicitud }{@code >}
     */
    @XmlElementDecl(namespace = "http://transfer.sgd.anm.gov.co", name = "CreacionSolicitudMinera")
    public JAXBElement<Solicitud> createCreacionSolicitudMinera(Solicitud value) {
        return new JAXBElement<Solicitud>(_CreacionSolicitudMinera_QNAME, Solicitud.class, null, value);
    }

}
