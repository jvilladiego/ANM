package co.gov.anm.comunicaciones.model.ejb;

import co.gov.anm.comunicaciones.model.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.model.entity.SgdAnexoComunciacion;
import co.gov.anm.comunicaciones.model.entity.SgdComunicacion;
import co.gov.anm.comunicaciones.model.entity.SgdDependencia;
import co.gov.anm.comunicaciones.model.entity.SgdInteresadoInt;
import co.gov.anm.comunicaciones.model.entity.SgdRol;
import co.gov.anm.comunicaciones.model.entity.SgdTipoIdentificacion;
import co.gov.anm.comunicaciones.model.entity.SgdUsuario;
import co.gov.anm.comunicaciones.model.entity.SgdUsuarioRol;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ModeloComunicaciones",
           mappedName = "ComunicacionesSalElecBpmApp-ComunicacionesSalElecViewPrj-ModeloComunicaciones")
public class ModeloComunicacionesBean3 implements ModeloComunicaciones3, ModeloComunicacionesLocal3 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "ComunicacionesPU")
    private EntityManager em;

    public ModeloComunicacionesBean3() {
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
}
