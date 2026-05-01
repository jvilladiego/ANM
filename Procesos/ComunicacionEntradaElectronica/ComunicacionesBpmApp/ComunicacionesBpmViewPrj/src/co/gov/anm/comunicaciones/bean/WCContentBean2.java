package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.Cuenta;
import co.gov.anm.comunicaciones.entity.Folders;
import co.gov.anm.comunicaciones.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.entity.Tramite;
import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "WCContent", mappedName = "ComunicacionesBpmApp-ComunicacionesBpmViewPrj-WCContent")
public class WCContentBean2 implements WCContent2, WCContentLocal2 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "WCContent")
    private EntityManager em;

    public WCContentBean2() {
    }

    public void removeFolders(Folders folders) {
        folders = em.find(Folders.class, folders.getFfolderguid());
        em.remove(folders);
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

    public void removeCuenta(Cuenta cuenta) {
        cuenta = em.find(Cuenta.class, cuenta.getDdocaccount());
        em.remove(cuenta);
    }

    /** <code>select o from Cuenta o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Cuenta> getCuentaFindAll() {
        return em.createNamedQuery("Cuenta.findAll", Cuenta.class).getResultList();
    }

    public void removeTramite(Tramite tramite) {
        tramite = em.find(Tramite.class, tramite.getIdtramite());
        em.remove(tramite);
    }

    /** <code>select o from Tramite o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Tramite> getTramiteFindAll() {
        return em.createNamedQuery("Tramite.findAll", Tramite.class).getResultList();
    }

    public Cuenta persistCuenta(Cuenta cuenta) {
        em.persist(cuenta);
        return cuenta;
    }

    public Cuenta mergeCuenta(Cuenta cuenta) {
        return em.merge(cuenta);
    }

    public Folders persistFolders(Folders folders) {
        em.persist(folders);
        return folders;
    }

    public Folders mergeFolders(Folders folders) {
        return em.merge(folders);
    }

    public TipoDocumentalTramite persistTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite) {
        em.persist(tipoDocumentalTramite);
        return tipoDocumentalTramite;
    }

    public TipoDocumentalTramite mergeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite) {
        return em.merge(tipoDocumentalTramite);
    }

    public void removeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite) {
        tipoDocumentalTramite = em.find(TipoDocumentalTramite.class, tipoDocumentalTramite.getIdTipodtalseguntramite());
        em.remove(tipoDocumentalTramite);
    }

    /** <code>select o from TipoDocumentalTramite o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumentalTramite> getTipoDocumentalTramiteFindAll() {
        return em.createNamedQuery("TipoDocumentalTramite.findAll", TipoDocumentalTramite.class).getResultList();
    }

    /** <code>select o from TipoDocumentalTramite o where o.idtramite=:p_id_tramite</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumentalTramite> getTipoDocumentalTramiteTiposDocumentalesTramites(BigDecimal p_id_tramite) {
        return em.createNamedQuery("TipoDocumentalTramite.tiposDocumentalesTramites", TipoDocumentalTramite.class)
                 .setParameter("p_id_tramite", p_id_tramite)
                 .getResultList();
    }

    public Tramite persistTramite(Tramite tramite) {
        em.persist(tramite);
        return tramite;
    }

    public Tramite mergeTramite(Tramite tramite) {
        return em.merge(tramite);
    }
    
    /** <code>select o from UnidadAdministrativa o where o.codUnidadadministrativa=:p_codUnidadadministrativa</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UnidadAdministrativa> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa) {
        return em.createNamedQuery("UnidadAdministrativa.findByCod", UnidadAdministrativa.class)
                 .setParameter("p_codUnidadadministrativa", p_codUnidadadministrativa)
                 .getResultList();
    }    
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UnidadAdministrativa> getUnidadAdministrativaFindAll () {
        return em.createNamedQuery("UnidadAdministrativa.findAll", UnidadAdministrativa.class).getResultList();
    }
}
