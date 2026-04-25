package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.AnmTipodtalseguntramite;
import co.gov.anm.comunicaciones.entity.AnmTramiteTb;
import co.gov.anm.comunicaciones.entity.AnmUnidadadministrativaTb;
import co.gov.anm.comunicaciones.entity.Folders;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Local;

@Local
public interface WCContentLocal {

    List<Folders> getFoldersFindAll();

    List<Folders> getFoldersFindByParentGuid(String p_parent_guid);

    List<Folders> getFoldersFindByFolderName(String p_folder_name);

    void removeTramite(AnmTramiteTb tramite);

    List<AnmTramiteTb> getTramiteFindAll();

    Folders persistFolders(Folders folders);

    Folders mergeFolders(Folders folders);

    AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll();

    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteTiposDocumentalesTramites(BigDecimal p_id_tramite);

    AnmTramiteTb persistTramite(AnmTramiteTb tramite);

    AnmTramiteTb mergeTramite(AnmTramiteTb tramite);
    
    List<AnmUnidadadministrativaTb> getUnidadAdministrativaFindByCod(BigDecimal p_codUnidadadministrativa);    
    
    List<AnmUnidadadministrativaTb> getUnidadAdministrativaFindAll();
}
