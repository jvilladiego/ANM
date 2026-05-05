package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.Cuenta;
import co.gov.anm.comunicaciones.entity.Folders;
import co.gov.anm.comunicaciones.entity.TipoDocumentalTramite;
import co.gov.anm.comunicaciones.entity.Tramite;
import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface WCContent2 {
    void removeFolders(Folders folders);

    List<Folders> getFoldersFindAll();

    List<Folders> getFoldersFindByParentGuid(String p_parent_guid);

    List<Folders> getFoldersFindByFolderName(String p_folder_name);

    void removeCuenta(Cuenta cuenta);

    List<Cuenta> getCuentaFindAll();

    void removeTramite(Tramite tramite);

    List<Tramite> getTramiteFindAll();

    Cuenta persistCuenta(Cuenta cuenta);

    Cuenta mergeCuenta(Cuenta cuenta);

    Folders persistFolders(Folders folders);

    Folders mergeFolders(Folders folders);

    TipoDocumentalTramite persistTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    TipoDocumentalTramite mergeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    void removeTipoDocumentalTramite(TipoDocumentalTramite tipoDocumentalTramite);

    List<TipoDocumentalTramite> getTipoDocumentalTramiteFindAll();

    List<TipoDocumentalTramite> getTipoDocumentalTramiteTiposDocumentalesTramites(BigDecimal p_id_tramite);

    Tramite persistTramite(Tramite tramite);

    Tramite mergeTramite(Tramite tramite);
    
    List<UnidadAdministrativa> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa);
    
    List<UnidadAdministrativa> getUnidadAdministrativaFindAll ();
}
