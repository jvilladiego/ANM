package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.SgdRol;
import co.gov.anm.comunicaciones.entity.SgdUsuario;
import co.gov.anm.comunicaciones.entity.SgdUsuarioRol;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "ComunicacionesSessionComIntElc",
           mappedName = "ComunicacionesIntFisicasBpmApp-ComunicacionesIntFisicas_ADF-ComunicacionesSessionComIntElc")
public class ComunicacionesSessionBeanBean implements ComunicacionesSessionBean, ComunicacionesSessionBeanLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "ComunicacionesComIntElc")
    private EntityManager em;

    public ComunicacionesSessionBeanBean() {
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
}
