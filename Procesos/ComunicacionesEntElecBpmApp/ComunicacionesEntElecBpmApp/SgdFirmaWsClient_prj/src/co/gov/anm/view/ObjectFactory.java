
package co.gov.anm.view;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the co.gov.anm.view package.
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

    private final static QName _FirmarPdf_QNAME = new QName("http://view.anm.gov.co/", "firmarPdf");
    private final static QName _FirmarPdfResponse_QNAME = new QName("http://view.anm.gov.co/", "firmarPdfResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.gov.anm.view
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FirmarPdf }
     *
     */
    public FirmarPdf createFirmarPdf() {
        return new FirmarPdf();
    }

    /**
     * Create an instance of {@link FirmarPdfResponse }
     *
     */
    public FirmarPdfResponse createFirmarPdfResponse() {
        return new FirmarPdfResponse();
    }

    /**
     * Create an instance of {@link FirmarResponse }
     *
     */
    public FirmarResponse createFirmarResponse() {
        return new FirmarResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarPdf }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://view.anm.gov.co/", name = "firmarPdf")
    public JAXBElement<FirmarPdf> createFirmarPdf(FirmarPdf value) {
        return new JAXBElement<FirmarPdf>(_FirmarPdf_QNAME, FirmarPdf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarPdfResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://view.anm.gov.co/", name = "firmarPdfResponse")
    public JAXBElement<FirmarPdfResponse> createFirmarPdfResponse(FirmarPdfResponse value) {
        return new JAXBElement<FirmarPdfResponse>(_FirmarPdfResponse_QNAME, FirmarPdfResponse.class, null, value);
    }

}
