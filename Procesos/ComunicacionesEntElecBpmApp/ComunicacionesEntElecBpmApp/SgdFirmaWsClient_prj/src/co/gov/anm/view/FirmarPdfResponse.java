
package co.gov.anm.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firmarPdfResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="firmarPdfResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://view.anm.gov.co/}firmarResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmarPdfResponse", propOrder = { "_return" })
public class FirmarPdfResponse {

    @XmlElement(name = "return")
    protected FirmarResponse _return;

    /**
     * Gets the value of the return property.
     *
     * @return
     *     possible object is
     *     {@link FirmarResponse }
     *
     */
    public FirmarResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     *
     * @param value
     *     allowed object is
     *     {@link FirmarResponse }
     *
     */
    public void setReturn(FirmarResponse value) {
        this._return = value;
    }

}
