
package co.gov.anm.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firmarPdf complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="firmarPdf"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroRadicado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taskNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="identificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contrasenaEncriptada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmarPdf",
         propOrder = { "numeroRadicado", "taskNumber", "identificacion", "usuario", "contrasenaEncriptada" })
public class FirmarPdf {

    protected String numeroRadicado;
    protected Integer taskNumber;
    protected String identificacion;
    protected String usuario;
    protected String contrasenaEncriptada;

    /**
     * Gets the value of the numeroRadicado property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumeroRadicado() {
        return numeroRadicado;
    }

    /**
     * Sets the value of the numeroRadicado property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumeroRadicado(String value) {
        this.numeroRadicado = value;
    }

    /**
     * Gets the value of the taskNumber property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getTaskNumber() {
        return taskNumber;
    }

    /**
     * Sets the value of the taskNumber property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setTaskNumber(Integer value) {
        this.taskNumber = value;
    }

    /**
     * Gets the value of the identificacion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Sets the value of the identificacion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdentificacion(String value) {
        this.identificacion = value;
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

    /**
     * Gets the value of the contrasenaEncriptada property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContrasenaEncriptada() {
        return contrasenaEncriptada;
    }

    /**
     * Sets the value of the contrasenaEncriptada property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContrasenaEncriptada(String value) {
        this.contrasenaEncriptada = value;
    }

}
