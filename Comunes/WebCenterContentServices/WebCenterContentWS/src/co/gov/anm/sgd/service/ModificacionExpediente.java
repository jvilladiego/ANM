
package co.gov.anm.sgd.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modificacionExpediente complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="modificacionExpediente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="credenciales" type="{http://service.sgd.anm.gov.co}credenciales" minOccurs="0"/&gt;
 *         &lt;element name="destino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroPlaca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoModificacion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modificacionExpediente", propOrder = { "credenciales", "destino", "numeroPlaca", "tipoModificacion" })
public class ModificacionExpediente {

    protected Credenciales credenciales;
    protected String destino;
    protected String numeroPlaca;
    protected Integer tipoModificacion;

    /**
     * Gets the value of the credenciales property.
     *
     * @return
     *     possible object is
     *     {@link Credenciales }
     *
     */
    public Credenciales getCredenciales() {
        return credenciales;
    }

    /**
     * Sets the value of the credenciales property.
     *
     * @param value
     *     allowed object is
     *     {@link Credenciales }
     *
     */
    public void setCredenciales(Credenciales value) {
        this.credenciales = value;
    }

    /**
     * Gets the value of the destino property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Sets the value of the destino property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestino(String value) {
        this.destino = value;
    }

    /**
     * Gets the value of the numeroPlaca property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    /**
     * Sets the value of the numeroPlaca property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumeroPlaca(String value) {
        this.numeroPlaca = value;
    }

    /**
     * Gets the value of the tipoModificacion property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getTipoModificacion() {
        return tipoModificacion;
    }

    /**
     * Sets the value of the tipoModificacion property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setTipoModificacion(Integer value) {
        this.tipoModificacion = value;
    }

}
