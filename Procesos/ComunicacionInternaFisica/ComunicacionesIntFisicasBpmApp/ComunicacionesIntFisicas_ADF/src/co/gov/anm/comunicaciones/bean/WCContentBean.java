package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.AnmTipodtalseguntramite;
import co.gov.anm.comunicaciones.entity.AnmTramiteTb;
import co.gov.anm.comunicaciones.entity.AnmUnidadadministrativaTb;
import co.gov.anm.comunicaciones.entity.Folders;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "WCContentComIntElc", mappedName = "ComunicacionesIntFisicasBpmApp-ComunicacionesIntFisicas_ADF-WCContentComIntElc")
public class WCContentBean implements WCContent, WCContentLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "WCContentComIntElc")
    private EntityManager em;

    public WCContentBean() {
    }

    /** <code>select o from Folders o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folders> getFoldersFindAll() {
        return em.createNamedQuery("Folders.findAll", Folders.class).getResultList();
    }

    /** <code>select o from Folders o where o.fparentguid = :p_parent_guid</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folders> getFoldersFindByParentGuid(String p_parent_guid) {
        return em.createNamedQuery("Folders.findByParentGuid", Folders.class)
                 .setParameter("p_parent_guid", p_parent_guid)
                 .getResultList();
    }

    /** <code>select o from Folders o where o.ffoldername = :p_folder_name</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folders> getFoldersFindByFolderName(String p_folder_name) {
        return em.createNamedQuery("Folders.findByFolderName", Folders.class)
                 .setParameter("p_folder_name", p_folder_name)
                 .getResultList();
    }

    public void removeTramite(AnmTramiteTb tramite) {
        tramite = em.find(AnmTramiteTb.class, tramite.getIdtramite());
        em.remove(tramite);
    }

    /** <code>select o from Tramite o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTramiteTb> getTramiteFindAll() {
        return em.createNamedQuery("AnmTramiteTb.findAll", AnmTramiteTb.class).getResultList();
    }

    public Folders persistFolders(Folders folders) {
        em.persist(folders);
        return folders;
    }

    public Folders mergeFolders(Folders folders) {
        return em.merge(folders);
    }

    public AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        em.persist(tipoDocumentalTramite);
        return tipoDocumentalTramite;
    }

    public AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        return em.merge(tipoDocumentalTramite);
    }

    public void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        tipoDocumentalTramite = em.find(AnmTipodtalseguntramite.class, tipoDocumentalTramite.getIdTipodtalseguntramite());
        em.remove(tipoDocumentalTramite);
    }

    /** <code>select o from TipoDocumentalTramite o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll() {
        return em.createNamedQuery("AnmTipodtalseguntramite.findAll", AnmTipodtalseguntramite.class).getResultList();
    }

    /** <code>select o from TipoDocumentalTramite o where o.idtramite=:p_id_tramite</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteTiposDocumentalesTramites(BigDecimal p_id_tramite) {
        return em.createNamedQuery("AnmTipodtalseguntramite.tiposDocumentalesTramites", AnmTipodtalseguntramite.class)
                 .setParameter("p_id_tramite", p_id_tramite)
                 .getResultList();
    }

    public AnmTramiteTb persistTramite(AnmTramiteTb tramite) {
        em.persist(tramite);
        return tramite;
    }

    public AnmTramiteTb mergeTramite(AnmTramiteTb tramite) {
        return em.merge(tramite);
    }
    
    /** <code>select o from AnmUnidadadministrativaTb o where o.codUnidadadministrativa=:p_codUnidadadministrativa</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmUnidadadministrativaTb> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa) {
        return em.createNamedQuery("AnmUnidadadministrativaTb.findByCod", AnmUnidadadministrativaTb.class)
                 .setParameter("p_codUnidadadministrativa", p_codUnidadadministrativa)
                 .getResultList();
    }   
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmUnidadadministrativaTb> getUnidadAdministrativaFindAll () {
        return em.createNamedQuery("AnmUnidadadministrativaTb.findAll", AnmUnidadadministrativaTb.class).getResultList();
    }
}
