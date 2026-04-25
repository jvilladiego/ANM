package co.gov.anm.sgd.bean;

import co.gov.anm.sgd.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "WCContentBean", mappedName = "WCContent")
public class WCContentBean3 implements WCContent3, WCContentLocal3 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "WebCenterContentWS")
    private EntityManager em;

    public WCContentBean3() {
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

    public void removeUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        unidadAdministrativa = em.find(UnidadAdministrativa.class, unidadAdministrativa.getIdUnidadadministrativa());
        em.remove(unidadAdministrativa);
    }

    /** <code>select o from UnidadAdministrativa o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UnidadAdministrativa> getUnidadAdministrativaFindAll() {
        return em.createNamedQuery("UnidadAdministrativa.findAll", UnidadAdministrativa.class).getResultList();
    }

    /** <code>select o from UnidadAdministrativa o where o.codUnidadadministrativa=:p_codUnidadadministrativa</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UnidadAdministrativa> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa) {
        return em.createNamedQuery("UnidadAdministrativa.findByCod", UnidadAdministrativa.class)
                 .setParameter("p_codUnidadadministrativa", p_codUnidadadministrativa)
                 .getResultList();
    }
}
