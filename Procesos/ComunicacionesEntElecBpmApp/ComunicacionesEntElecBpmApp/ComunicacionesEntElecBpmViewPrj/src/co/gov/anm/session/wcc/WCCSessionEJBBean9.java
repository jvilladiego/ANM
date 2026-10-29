package co.gov.anm.session.wcc;

import co.gov.anm.entity.AnmTipodtalseguntramite;
import co.gov.anm.entity.AnmTramiteTb;
import co.gov.anm.entity.AnmUnidadadministrativaTb;
import co.gov.anm.entity.Folderfolders;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(
    name = "WCCSessionEJB_V4",
    mappedName = "ComunicacionesEntEmailBpmApp-ComunicacionesEntElecBpmViewPrj-WCCSessionEJB_V4"
)
public class WCCSessionEJBBean9 implements WCCSessionEJB9, WCCSessionEJBLocal9 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "WebCenterContentPU")
    private EntityManager em;

    public WCCSessionEJBBean9() {
    }

    public AnmUnidadadministrativaTb persistAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb) {
        em.persist(anmUnidadadministrativaTb);
        return anmUnidadadministrativaTb;
    }

    public AnmUnidadadministrativaTb mergeAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb) {
        return em.merge(anmUnidadadministrativaTb);
    }

    public void removeAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb) {
        anmUnidadadministrativaTb =
            em.find(AnmUnidadadministrativaTb.class, anmUnidadadministrativaTb.getIdUnidadadministrativa());
        em.remove(anmUnidadadministrativaTb);
    }

    /** <code>select o from AnmUnidadadministrativaTb o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmUnidadadministrativaTb> getAnmUnidadadministrativaTbFindAll() {
        return em.createNamedQuery("AnmUnidadadministrativaTb.findAll", AnmUnidadadministrativaTb.class)
               .getResultList();
    }

    /** <code>select o from AnmUnidadadministrativaTb o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AnmUnidadadministrativaTb getAnmUnidadadministrativaByCod(Long codDependencia) {
        return em.createNamedQuery("AnmUnidadadministrativaTb.getUnidadAdministrativaByCod", AnmUnidadadministrativaTb.class)
                 .setParameter("codUnidadadministrativa", codDependencia)
                 .getSingleResult();
    }

    // init Tramite
    public AnmTramiteTb persistTramite(AnmTramiteTb tramite) {
        em.persist(tramite);
        return tramite;
    }

    public AnmTramiteTb mergeTramite(AnmTramiteTb tramite) {
        return em.merge(tramite);
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
    // end Tramite

    //init tramites-tipos
    public AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        em.persist(tipoDocumentalTramite);
        return tipoDocumentalTramite;
    }

    public AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        return em.merge(tipoDocumentalTramite);
    }

    public void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite) {
        tipoDocumentalTramite =
            em.find(AnmTipodtalseguntramite.class, tipoDocumentalTramite.getIdTipodtalseguntramite());
        em.remove(tipoDocumentalTramite);
    }

    /** <code>select o from TipoDocumentalTramite o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll() {
        return em.createNamedQuery("AnmTipodtalseguntramite.findAll", AnmTipodtalseguntramite.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite) {
        return em.createNamedQuery("AnmTipodtalseguntramite.tiposDocumentalesTramites", AnmTipodtalseguntramite.class)
                 .setParameter("p_id_tramite", idTramite)
                 .getResultList();
    }
    // end tramites-tipos

    //folders
    public Folderfolders persistFolderFolders(Folderfolders folderFolders) {
        em.persist(folderFolders);
        return folderFolders;
    }

    public Folderfolders mergeFolderFolders(Folderfolders folderFolders) {
        return em.merge(folderFolders);
    }

    public void removeFolderFolders(Folderfolders folderFolders) {
        folderFolders = em.find(Folderfolders.class, folderFolders.getFfolderguid());
        em.remove(folderFolders);
    }

    /** <code>select o from FolderFolders o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folderfolders> getFolderFoldersFindAll() {
        return em.createNamedQuery("Folderfolders.findAll", Folderfolders.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folderfolders> getFolderFoldersFindByParentGuid(String parentGuid) {
        return em.createNamedQuery("Folderfolders.findByParentGuid", Folderfolders.class)
                 .setParameter("p_parent_guid", parentGuid)
                 .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folderfolders> getFolderFoldersFindByFolderName(String foldername) {
        return em.createNamedQuery("Folderfolders.findByFolderName", Folderfolders.class)
                 .setParameter("p_folder_name", foldername)
                 .getResultList();
    }


}
