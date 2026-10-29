package co.gov.anm.session.com;

import co.gov.anm.entity.AnmPlantilla;
import co.gov.anm.entity.SgdAnexoComunciacion;
import co.gov.anm.entity.SgdComunicacion;
import co.gov.anm.entity.SgdDependencia;
import co.gov.anm.entity.SgdInteresadoInt;
import co.gov.anm.entity.SgdRol;
import co.gov.anm.entity.SgdTipoIdentificacion;
import co.gov.anm.entity.SgdUsuario;
import co.gov.anm.entity.SgdUsuarioRol;
import co.gov.anm.entity.SgdTipoSolicitud;
import co.gov.anm.entity.SgdRangoEdad;
import co.gov.anm.entity.SgdPoblacionVulnerable;
import co.gov.anm.entity.SgdEtnia;
import co.gov.anm.entity.SgdPais;
import co.gov.anm.entity.SgdDepartamento;
import co.gov.anm.entity.SgdMunicipio;
import co.gov.anm.entity.SgdOcupacion;
import co.gov.anm.entity.TipoSolicitante;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(
    name = "comunicacionesEntSessionEJB_V4",
    mappedName = "ComunicacionesEntEmailBpmApp-ComunicacionesEntElecBpmViewPrj-comunicacionesEntSessionEJB_V4"
)
public class ComunicacionesEntSessionEJBBean9 implements ComunicacionesEntSessionEJB9, ComunicacionesEntSessionEJBLocal9 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "ComunicacionesEntElecBpmViewPrj")
    private EntityManager em;

    public ComunicacionesEntSessionEJBBean9() {
    }

    public SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario) {
        em.persist(sgdUsuario);
        return sgdUsuario;
    }

    public SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario) {
        return em.merge(sgdUsuario);
    }

    public void removeSgdUsuario(SgdUsuario sgdUsuario) {
        sgdUsuario = em.find(SgdUsuario.class, sgdUsuario.getIdUsuario());
        em.remove(sgdUsuario);
    }

    /** <code>select o from SgdUsuario o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioFindAll() {
        return em.createNamedQuery("SgdUsuario.findAll", SgdUsuario.class).getResultList();
    }

    /** <code>select o from SgdUsuario o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioByCodDependencia(Long codDependencia) {
        return em.createNamedQuery("SgdUsuario.findByCodDependencia", SgdUsuario.class).setParameter("codDependencia", codDependencia).getResultList();
    }
    
    /** <code>select o from SgdUsuario o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdUsuario getSgdUsuarioById(String idUsuario) {
        return em.createNamedQuery("SgdUsuario.findById", SgdUsuario.class).setParameter("idUsuario", idUsuario).getSingleResult();
    }
    public SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        em.persist(sgdInteresadoInt);
        return sgdInteresadoInt;
    }

    public SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        return em.merge(sgdInteresadoInt);
    }

    public void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt) {
        sgdInteresadoInt = em.find(SgdInteresadoInt.class, sgdInteresadoInt.getIdInteresadoInt());
        em.remove(sgdInteresadoInt);
    }

    /** <code>select o from SgdInteresadoInt o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdInteresadoInt> getSgdInteresadoIntFindAll() {
        return em.createNamedQuery("SgdInteresadoInt.findAll", SgdInteresadoInt.class).getResultList();
    }

    public SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        em.persist(sgdTipoIdentificacion);
        return sgdTipoIdentificacion;
    }

    public SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        return em.merge(sgdTipoIdentificacion);
    }

    public void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion) {
        sgdTipoIdentificacion = em.find(SgdTipoIdentificacion.class, sgdTipoIdentificacion.getCodigo());
        em.remove(sgdTipoIdentificacion);
    }

    /** <code>select o from SgdTipoIdentificacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll() {
        return em.createNamedQuery("SgdTipoIdentificacion.findAll", SgdTipoIdentificacion.class).getResultList();
    }

// tipo de solicitud -PQRS (agregado manualmente)
    
        public SgdTipoSolicitud persistSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud) {
        em.persist(sgdTipoSolicitud);
        return sgdTipoSolicitud;
    }

    public SgdTipoSolicitud mergeSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud) {
        return em.merge(sgdTipoSolicitud);
    }

    public void removeSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud) {
        sgdTipoSolicitud = em.find(SgdTipoSolicitud.class, sgdTipoSolicitud.getIdTipoSolicitud());
        em.remove(sgdTipoSolicitud);
    }

    /** <code>select o from SgdTipoSolicitud o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdTipoSolicitud> getSgdTipoSolicitudFindAll() {
        return em.createNamedQuery("SgdTipoSolicitud.findAll", SgdTipoSolicitud.class).getResultList();
    }
    
    /** <code>select o from SgdTipoSolicitud o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdTipoSolicitud getTipoSolicitudById(Long idTipoPQRS) {
        return em.createNamedQuery("SgdTipoSolicitud.getTipoSolicitudById", SgdTipoSolicitud.class)
                 .setParameter("idTipoSolicitud", idTipoPQRS)
                 .getSingleResult();
    }
    
    public SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        em.persist(sgdUsuarioRol);
        return sgdUsuarioRol;
    }

    public SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        return em.merge(sgdUsuarioRol);
    }

    public void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol) {
        sgdUsuarioRol = em.find(SgdUsuarioRol.class, sgdUsuarioRol.getIdUsuarioRol());
        em.remove(sgdUsuarioRol);
    }

    /** <code>select o from SgdUsuarioRol o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuarioRol> getSgdUsuarioRolFindAll() {
        return em.createNamedQuery("SgdUsuarioRol.findAll", SgdUsuarioRol.class).getResultList();
    }

    public SgdDependencia persistSgdDependencia(SgdDependencia sgdDependencia) {
        em.persist(sgdDependencia);
        return sgdDependencia;
    }

    public SgdDependencia mergeSgdDependencia(SgdDependencia sgdDependencia) {
        return em.merge(sgdDependencia);
    }

    public void removeSgdDependencia(SgdDependencia sgdDependencia) {
        sgdDependencia = em.find(SgdDependencia.class, sgdDependencia.getIdSgdDependencia());
        em.remove(sgdDependencia);
    }

    /** <code>select o from SgdDependencia o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdDependencia> getSgdDependenciaFindAll() {
        return em.createNamedQuery("SgdDependencia.findAll", SgdDependencia.class).getResultList();
    }

    public SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion) {
        em.persist(sgdComunicacion);
        return sgdComunicacion;
    }

    public SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion) {
        return em.merge(sgdComunicacion);
    }

    public void removeSgdComunicacion(SgdComunicacion sgdComunicacion) {
        sgdComunicacion = em.find(SgdComunicacion.class, sgdComunicacion.getIdComunicacion());
        em.remove(sgdComunicacion);
    }

    /** <code>select o from SgdComunicacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdComunicacion> getSgdComunicacionFindAll() {
        return em.createNamedQuery("SgdComunicacion.findAll", SgdComunicacion.class).getResultList();
    }

    public AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla) {
        em.persist(anmPlantilla);
        return anmPlantilla;
    }

    public AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla) {
        return em.merge(anmPlantilla);
    }

    public void removeAnmPlantilla(AnmPlantilla anmPlantilla) {
        anmPlantilla = em.find(AnmPlantilla.class, anmPlantilla.getIdAnmPlantilla());
        em.remove(anmPlantilla);
    }

    /** <code>select o from AnmPlantilla o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmPlantilla> getAnmPlantillaFindAll() {
        return em.createNamedQuery("AnmPlantilla.findAll", AnmPlantilla.class).getResultList();
    }

    public SgdRol persistSgdRol(SgdRol sgdRol) {
        em.persist(sgdRol);
        return sgdRol;
    }

    public SgdRol mergeSgdRol(SgdRol sgdRol) {
        return em.merge(sgdRol);
    }

    public void removeSgdRol(SgdRol sgdRol) {
        sgdRol = em.find(SgdRol.class, sgdRol.getIdRol());
        em.remove(sgdRol);
    }

    /** <code>select o from SgdRol o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdRol> getSgdRolFindAll() {
        return em.createNamedQuery("SgdRol.findAll", SgdRol.class).getResultList();
    }

    public SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        em.persist(sgdAnexoComunciacion);
        return sgdAnexoComunciacion;
    }

    public SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        return em.merge(sgdAnexoComunciacion);
    }

    public void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion) {
        sgdAnexoComunciacion = em.find(SgdAnexoComunciacion.class, sgdAnexoComunciacion.getIdAnexoComunicacion());
        em.remove(sgdAnexoComunciacion);
    }

    /** <code>select o from SgdAnexoComunciacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll() {
        return em.createNamedQuery("SgdAnexoComunciacion.findAll", SgdAnexoComunciacion.class).getResultList();
    }
    
    //rango edad
    public SgdRangoEdad persistSgdRangoEdad(SgdRangoEdad sgdRangoEdad) {
    em.persist(sgdRangoEdad);
    return sgdRangoEdad;
    }

    public SgdRangoEdad mergeSgdRangoEdad(SgdRangoEdad sgdRangoEdad) {
    return em.merge(sgdRangoEdad);
    }

    public void removeSgdRangoEdad(SgdRangoEdad SgdRangoEdad) {
    SgdRangoEdad = em.find(SgdRangoEdad.class, SgdRangoEdad.getIdRangoEdad());
    em.remove(SgdRangoEdad);
    }

    /** <code>select o from SgdRangoEdad o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdRangoEdad> getSgdRangoEdadFindAll() {
    return em.createNamedQuery("SgdRangoEdad.findAll", SgdRangoEdad.class).getResultList();
    }
    
    //poblacion vulnerable
    public SgdPoblacionVulnerable persistSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable) {
    em.persist(sgdPoblacionVulnerable);
    return sgdPoblacionVulnerable;
    }

    public SgdPoblacionVulnerable mergeSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable) {
    return em.merge(sgdPoblacionVulnerable);
    }

    public void removeSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable) {
    sgdPoblacionVulnerable = em.find(SgdPoblacionVulnerable.class, sgdPoblacionVulnerable.getIdPoblacionVulnerable());
    em.remove(sgdPoblacionVulnerable);
    }

    /** <code>select o from SgdPoblacionVulnerable o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdPoblacionVulnerable> getSgdPoblacionVulnerableFindAll() {
    return em.createNamedQuery("SgdPoblacionVulnerable.findAll", SgdPoblacionVulnerable.class).getResultList();
    }
    
    //etnia
    public SgdEtnia persistSgdEtnia(SgdEtnia sgdEtnia) {
    em.persist(sgdEtnia);
    return sgdEtnia;
    }

    public SgdEtnia mergeSgdEtnia(SgdEtnia sgdEtnia) {
    return em.merge(sgdEtnia);
    }

    public void removeSgdEtnia(SgdEtnia sgdEtnia) {
    sgdEtnia = em.find(SgdEtnia.class, sgdEtnia.getIdEtnia());
    em.remove(sgdEtnia);
    }

    /** <code>select o from SgdEtnia o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdEtnia> getSgdEtniaFindAll() {
    return em.createNamedQuery("SgdEtnia.findAll", SgdEtnia.class).getResultList();
    }
    
    //pais
    public SgdPais persistSgdPais(SgdPais sgdPais) {
    em.persist(sgdPais);
    return sgdPais;
    }

    public SgdPais mergeSgdPais(SgdPais sgdPais) {
    return em.merge(sgdPais);
    }

    public void removeSgdPais(SgdPais sgdPais) {
    sgdPais = em.find(SgdPais.class, sgdPais.getId());
    em.remove(sgdPais);
    }

    /** <code>select o from SgdPais o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdPais> getSgdPaisFindAll() {
    return em.createNamedQuery("SgdPais.findAll", SgdPais.class).getResultList();
    }
    
    //departamento
    public SgdDepartamento persistSgdDepartamento(SgdDepartamento sgdDepartamento) {
    em.persist(sgdDepartamento);
    return sgdDepartamento;
    }

    public SgdDepartamento mergeSgdDepartamento(SgdDepartamento sgdDepartamento) {
    return em.merge(sgdDepartamento);
    }

    public void removeSgdDepartamento(SgdDepartamento sgdDepartamento) {
    sgdDepartamento = em.find(SgdDepartamento.class, sgdDepartamento.getIdDepartamento());
    em.remove(sgdDepartamento);
    }

    /** <code>select o from SgdDepartamento o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdDepartamento> getSgdDepartamentoFindAll() {
    return em.createNamedQuery("SgdDepartamento.findAll", SgdDepartamento.class).getResultList();
    }
    
    //municipio-ciudad
    public SgdMunicipio persistSgdMunicipio(SgdMunicipio sgdMunicipio) {
    em.persist(sgdMunicipio);
    return sgdMunicipio;
    }

    public SgdMunicipio mergeSgdMunicipio(SgdMunicipio sgdMunicipio) {
    return em.merge(sgdMunicipio);
    }

    public void removeSgdMunicipio(SgdMunicipio sgdMunicipio) {
    sgdMunicipio = em.find(SgdMunicipio.class, sgdMunicipio.getIdMunicipio());
    em.remove(sgdMunicipio);
    }

    /** <code>select o from SgdMunicipio o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdMunicipio> getSgdMunicipioFindAll() {
    return em.createNamedQuery("SgdMunicipio.findAll", SgdMunicipio.class).getResultList();
    }
    
    /** <code>select o from SgdMunicipio o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdMunicipio> getMunicipioByDepartamento(int idDepartamento) {
        return em.createNamedQuery("SgdMunicipio.getMunicipioByDepartamento", SgdMunicipio.class).setParameter("idDepartamento", idDepartamento).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoSolicitante> getTipoSolicitanteFindAll() {
        return em.createNamedQuery("TipoSolicitante.findAll", TipoSolicitante.class).getResultList();
    }
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdOcupacion> getOcupacionFindAll() {
        return em.createNamedQuery("SgdOcupacion.findAll", SgdOcupacion.class).getResultList();
    }
}
