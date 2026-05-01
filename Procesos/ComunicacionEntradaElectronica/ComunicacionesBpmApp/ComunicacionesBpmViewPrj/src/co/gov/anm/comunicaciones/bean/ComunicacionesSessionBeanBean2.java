package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;
import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.SgdRol;
import co.gov.anm.comunicaciones.entity.SgdUsuario;
import co.gov.anm.comunicaciones.entity.SgdUsuarioRol;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "ComunicacionesSessionBean",
           mappedName = "ComunicacionesBpmApp-ComunicacionesBpmViewPrj-ComunicacionesSessionBean")
public class ComunicacionesSessionBeanBean2 implements ComunicacionesSessionBean2, ComunicacionesSessionBeanLocal2 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "ComunicacionesBpmViewPrj")
    private EntityManager em;

    public ComunicacionesSessionBeanBean2() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
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
        return em.createNamedQuery("SgdUsuarioRol.findAll").getResultList();
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
        return em.createNamedQuery("SgdRol.findAll").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioFindByDependencia(Long codigoDependencia) {
        return em.createNamedQuery("SgdUsuario.findByDependencia").setParameter("codDependencia", codigoDependencia).getResultList() ;
    } 
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdUsuario> getSgdUsuarioFindById(String idUsuario) {
        return em.createQuery("select o from SgdUsuario o where o.idUsuario = :idUsuario")
                 .setParameter("idUsuario", idUsuario)
                 .getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmPlantilla> getAnmPlantillaFindAll() {
        return em.createNamedQuery("AnmPlantilla.findAll").getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UnidadAdministrativa> getUnidadAdministrativaFindAll() {
        return em.createNamedQuery("UnidadAdministrativa.findAll").getResultList();
    }


}
