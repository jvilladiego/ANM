package co.gov.anm.sgd.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "SgdInteresado.findAll", query = "select o from SgdInteresado o"),
                @NamedQuery(name = "SgdInteresado.findByIdComunicacion", query = "select o from SgdInteresado o where o.sgdComunicacion = :idComunicacion")
             })
@Table(name = "SGD_INTERESADO")
public class SgdInteresado implements Serializable {
    private static final long serialVersionUID = -6882190703662262300L;
    @Column(length = 20)
    private String celular;
    @Column(length = 20)
    private String ciudad;
    @Column(length = 20)
    private String direccion;
    @Column(length = 20)
    private String email;
    @Column(name = "ID_DEPARTAMENTO")
    private BigDecimal idDepartamento;
    @Column(name = "ID_ETNIA")
    private BigDecimal idEtnia;
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SGD_INTERESADO_SEQ")
    @SequenceGenerator(name="SGD_INTERESADO_SEQ", sequenceName="SGD_INTERESADO_SEQ", allocationSize=1)
    @Id
    @Column(name = "ID_INTERESADO", nullable = false)
    private BigDecimal idInteresado;
    @Column(name = "ID_MUNICIPIO")
    private BigDecimal idMunicipio;
    @Column(name = "ID_PAIS")
    private BigDecimal idPais;
    @Column(name = "ID_POBLACION_VULNERABLE")
    private BigDecimal idPoblacionVulnerable;
    @Column(name = "ID_RANGO_EDAD")
    private BigDecimal idRangoEdad;
    @Column(name = "NUMERO_IDENTIFICACION", length = 20)
    private String numeroIdentificacion;
    @Column(length = 20)
    private String ocupacion;
    @Column(name = "PRIMER_APELLIDO", length = 20)
    private String primerApellido;
    @Column(name = "PRIMER_NOMBRE", nullable = false, length = 20)
    private String primerNombre;
    @Column(name = "SEGUNDO_APELLIDO", length = 20)
    private String segundoApellido;
    @Column(name = "SEGUNDO_NOMBRE", length = 20)
    private String segundoNombre;
    @Column(length = 20)
    private String telefono;
    @Column(name = "TIPO_IDENTIFICACION", length = 20)
    private String tipoIdentificacion;
    @ManyToOne
    @JoinColumn(name = "ID_COMUNICACION")
    private SgdComunicacion sgdComunicacion;
    @ManyToOne
    @JoinColumn(name = "TIPO_SOLICITANTE")
    private TipoSolicitante tipoSolicitante;

    public SgdInteresado() {
    }

    public SgdInteresado(String celular, String ciudad, String direccion, String email, SgdComunicacion sgdComunicacion,
                         BigDecimal idDepartamento, BigDecimal idEtnia, BigDecimal idInteresado, BigDecimal idMunicipio,
                         BigDecimal idPais, BigDecimal idPoblacionVulnerable, BigDecimal idRangoEdad,
                         String numeroIdentificacion, String ocupacion, String primerApellido, String primerNombre,
                         String segundoApellido, String segundoNombre, String telefono, String tipoIdentificacion) {
        this.celular = celular;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.email = email;
        this.sgdComunicacion = sgdComunicacion;
        this.idDepartamento = idDepartamento;
        this.idEtnia = idEtnia;
        this.idInteresado = idInteresado;
        this.idMunicipio = idMunicipio;
        this.idPais = idPais;
        this.idPoblacionVulnerable = idPoblacionVulnerable;
        this.idRangoEdad = idRangoEdad;
        this.numeroIdentificacion = numeroIdentificacion;
        this.ocupacion = ocupacion;
        this.primerApellido = primerApellido;
        this.primerNombre = primerNombre;
        this.segundoApellido = segundoApellido;
        this.segundoNombre = segundoNombre;
        this.telefono = telefono;
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public BigDecimal getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(BigDecimal idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public BigDecimal getIdEtnia() {
        return idEtnia;
    }

    public void setIdEtnia(BigDecimal idEtnia) {
        this.idEtnia = idEtnia;
    }

    public BigDecimal getIdInteresado() {
        return idInteresado;
    }

    public void setIdInteresado(BigDecimal idInteresado) {
        this.idInteresado = idInteresado;
    }

    public BigDecimal getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(BigDecimal idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public BigDecimal getIdPais() {
        return idPais;
    }

    public void setIdPais(BigDecimal idPais) {
        this.idPais = idPais;
    }

    public BigDecimal getIdPoblacionVulnerable() {
        return idPoblacionVulnerable;
    }

    public void setIdPoblacionVulnerable(BigDecimal idPoblacionVulnerable) {
        this.idPoblacionVulnerable = idPoblacionVulnerable;
    }

    public BigDecimal getIdRangoEdad() {
        return idRangoEdad;
    }

    public void setIdRangoEdad(BigDecimal idRangoEdad) {
        this.idRangoEdad = idRangoEdad;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public SgdComunicacion getSgdComunicacion() {
        return sgdComunicacion;
    }

    public void setSgdComunicacion(SgdComunicacion sgdComunicacion) {
        this.sgdComunicacion = sgdComunicacion;
    }

    public TipoSolicitante getTipoSolicitante() {
        return tipoSolicitante;
    }

    public void setTipoSolicitante(TipoSolicitante tipoSolicitante) {
        this.tipoSolicitante = tipoSolicitante;
    }
    
    public String getNombreCompleto ( ){
            StringBuilder sb = new StringBuilder(this.primerNombre);
            sb.append(" ");
            if ( this.segundoNombre!=null ){
                sb.append(this.segundoNombre).append(" ");
            }
            if (this.primerApellido != null){
                sb.append(this.primerApellido).append(" ");    
            }
            if ( this.segundoApellido!=null ){
                sb.append(this.segundoApellido);
            }
            return sb.toString();
        }
}
