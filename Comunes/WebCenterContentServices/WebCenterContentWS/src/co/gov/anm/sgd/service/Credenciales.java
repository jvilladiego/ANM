
package co.gov.anm.sgd.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for credenciales complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="credenciales"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contrasena" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credenciales", propOrder = { "contrasena", "usuario" })
public class Credenciales {

    protected String contrasena;
    protected String usuario;

    /**
     * Gets the value of the contrasena property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Sets the value of the contrasena property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContrasena(String value) {
        this.contrasena = value;
    }

    /**
     * Gets the value of the usuario property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

}
