package co.gov.anm.comunicaciones.model.ejb;

import co.gov.anm.comunicaciones.model.entity.Cuenta;
import co.gov.anm.comunicaciones.model.entity.EntidadProductora;
import co.gov.anm.comunicaciones.model.entity.Folder;
import co.gov.anm.comunicaciones.model.entity.FolderFolders;
import co.gov.anm.comunicaciones.model.entity.TipoDocumental;
import co.gov.anm.comunicaciones.model.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.model.entity.TipoRecursoInformacion;
import co.gov.anm.comunicaciones.model.entity.Tramite;
import co.gov.anm.comunicaciones.model.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Local;

@Local
public interface WebCenterContentLocal3 {
    TipoRecursoInformacion persistTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion);

    TipoRecursoInformacion mergeTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion);

    void removeTipoRecursoInformacion(TipoRecursoInformacion tipoRecursoInformacion);

    List<TipoRecursoInformacion> getTipoRecursoInformacionFindAll();

    TipoDocumentalTramite persistTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    TipoDocumentalTramite mergeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    void removeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    List<TipoDocumentalTramite> getTipoDocumentalTramiteFindAll();

    EntidadProductora persistEntidadProductora(EntidadProductora entidadProductora);

    EntidadProductora mergeEntidadProductora(EntidadProductora entidadProductora);

    void removeEntidadProductora(EntidadProductora entidadProductora);

    List<EntidadProductora> getEntidadProductoraFindAll();

    UnidadAdministrativa persistUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    UnidadAdministrativa mergeUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    void removeUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa);

    List<UnidadAdministrativa> getUnidadAdministrativaFindAll();

    Cuenta persistCuenta(Cuenta cuenta);

    Cuenta mergeCuenta(Cuenta cuenta);

    void removeCuenta(Cuenta cuenta);

    List<Cuenta> getCuentaFindAll();

    Folder persistFolder(Folder folder);

    Folder mergeFolder(Folder folder);

    void removeFolder(Folder folder);

    List<Folder> getFolderFindAll();

    TipoDocumental persistTipoDocumental(TipoDocumental tipoDocumental);

    TipoDocumental mergeTipoDocumental(TipoDocumental tipoDocumental);

    void removeTipoDocumental(TipoDocumental tipoDocumental);

    List<TipoDocumental> getTipoDocumentalFindAll();

    FolderFolders persistFolderFolders(FolderFolders folderFolders);

    FolderFolders mergeFolderFolders(FolderFolders folderFolders);

    void removeFolderFolders(FolderFolders folderFolders);

    List<FolderFolders> getFolderFoldersFindAll();

    Tramite persistTramite(Tramite tramite);

    Tramite mergeTramite(Tramite tramite);

    void removeTramite(Tramite tramite);

    List<Tramite> getTramiteFindAll();
    
    List<FolderFolders> getFolderFoldersFindByParentGuid(String parentGuid);
    
    List<FolderFolders> getFolderFoldersFindByFolderName(String foldername);
    
    List<TipoDocumentalTramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite);
}
