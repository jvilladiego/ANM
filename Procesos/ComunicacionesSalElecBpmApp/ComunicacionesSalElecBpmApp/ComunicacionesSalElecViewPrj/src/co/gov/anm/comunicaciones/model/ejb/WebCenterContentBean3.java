package co.gov.anm.comunicaciones.model.ejb;

import co.gov.anm.comunicaciones.model.entity.Cuenta;
import co.gov.anm.comunicaciones.model.entity.EntidadProductora;
import co.gov.anm.comunicaciones.model.entity.EntidadProductoraPK;
import co.gov.anm.comunicaciones.model.entity.Folder;
import co.gov.anm.comunicaciones.model.entity.FolderFolders;
import co.gov.anm.comunicaciones.model.entity.TipoDocumental;
import co.gov.anm.comunicaciones.model.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.model.entity.TipoRecursoInformacion;

import co.gov.anm.comunicaciones.model.entity.Tramite;
import co.gov.anm.comunicaciones.model.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "WebCenterContent",
           mappedName = "ComunicacionesSalElecBpmApp-ComunicacionesSalElecViewPrj-WebCenterContent")
public class WebCenterContentBean3 implements WebCenterContent3, WebCenterContentLocal3 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "WebCenterContentPU")
    private EntityManager em;

    public WebCenterContentBean3() {
    }

    public TipoRecursoInformacion persistTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion) {
        em.persist(tipoRecursoInformacion);
        return tipoRecursoInformacion;
    }

    public TipoRecursoInformacion mergeTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion) {
        return em.merge(tipoRecursoInformacion);
    }

    public void removeTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion) {
        tipoRecursoInformacion =
            em.find(TipoRecursoInformacion.class, tipoRecursoInformacion.getIdtiporecurinformacion());
        em.remove(tipoRecursoInformacion);
    }

    /** <code>select o from TipoRecursoInformacion o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoRecursoInformacion> getTipoRecursoInformacionFindAll() {
        return em.createNamedQuery("TipoRecursoInformacion.findAll", TipoRecursoInformacion.class).getResultList();
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
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumentalTramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite){
        return em.createNamedQuery("TipoDocumentalTramite.tiposDocumentalesTramites", TipoDocumentalTramite.class).setParameter("p_id_tramite", idTramite).getResultList();
    }

    public EntidadProductora persistEntidadProductora(EntidadProductora entidadProductora) {
        em.persist(entidadProductora);
        return entidadProductora;
    }

    public EntidadProductora mergeEntidadProductora(EntidadProductora entidadProductora) {
        return em.merge(entidadProductora);
    }

    public void removeEntidadProductora(EntidadProductora entidadProductora) {
        entidadProductora =
            em.find(EntidadProductora.class,
                    new EntidadProductoraPK(entidadProductora.getEntidadproductora(),
                                            entidadProductora.getIdentidadproductora()));
        em.remove(entidadProductora);
    }

    /** <code>select o from EntidadProductora o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<EntidadProductora> getEntidadProductoraFindAll() {
        return em.createNamedQuery("EntidadProductora.findAll", EntidadProductora.class).getResultList();
    }

    public UnidadAdministrativa persistUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        em.persist(unidadAdministrativa);
        return unidadAdministrativa;
    }

    public UnidadAdministrativa mergeUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        return em.merge(unidadAdministrativa);
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

    public Cuenta persistCuenta(Cuenta cuenta) {
        em.persist(cuenta);
        return cuenta;
    }

    public Cuenta mergeCuenta(Cuenta cuenta) {
        return em.merge(cuenta);
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

    public Folder persistFolder(Folder folder) {
        em.persist(folder);
        return folder;
    }

    public Folder mergeFolder(Folder folder) {
        return em.merge(folder);
    }

    public void removeFolder(Folder folder) {
        folder = em.find(Folder.class, folder.getDfolderid());
        em.remove(folder);
    }

    /** <code>select o from Folder o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Folder> getFolderFindAll() {
        return em.createNamedQuery("Folder.findAll", Folder.class).getResultList();
    }

    public TipoDocumental persistTipoDocumental(TipoDocumental tipoDocumental) {
        em.persist(tipoDocumental);
        return tipoDocumental;
    }

    public TipoDocumental mergeTipoDocumental(TipoDocumental tipoDocumental) {
        return em.merge(tipoDocumental);
    }

    public void removeTipoDocumental(TipoDocumental tipoDocumental) {
        tipoDocumental = em.find(TipoDocumental.class, tipoDocumental.getIdtipodocumental());
        em.remove(tipoDocumental);
    }

    /** <code>select o from TipoDocumental o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumental> getTipoDocumentalFindAll() {
        return em.createNamedQuery("TipoDocumental.findAll", TipoDocumental.class).getResultList();
    }

    public FolderFolders persistFolderFolders(FolderFolders folderFolders) {
        em.persist(folderFolders);
        return folderFolders;
    }

    public FolderFolders mergeFolderFolders(FolderFolders folderFolders) {
        return em.merge(folderFolders);
    }

    public void removeFolderFolders(FolderFolders folderFolders) {
        folderFolders = em.find(FolderFolders.class, folderFolders.getFfolderguid());
        em.remove(folderFolders);
    }

    /** <code>select o from FolderFolders o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FolderFolders> getFolderFoldersFindAll() {
        return em.createNamedQuery("FolderFolders.findAll", FolderFolders.class).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FolderFolders> getFolderFoldersFindByParentGuid(String parentGuid){
        return em.createNamedQuery("FolderFolders.findByParentGuid", FolderFolders.class).setParameter("p_parent_guid", parentGuid).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FolderFolders> getFolderFoldersFindByFolderName(String foldername){
        return em.createNamedQuery("FolderFolders.findByFolderName", FolderFolders.class).setParameter("p_folder_name", foldername).getResultList();        
    }

    public Tramite persistTramite(Tramite tramite) {
        em.persist(tramite);
        return tramite;
    }

    public Tramite mergeTramite(Tramite tramite) {
        return em.merge(tramite);
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
}
