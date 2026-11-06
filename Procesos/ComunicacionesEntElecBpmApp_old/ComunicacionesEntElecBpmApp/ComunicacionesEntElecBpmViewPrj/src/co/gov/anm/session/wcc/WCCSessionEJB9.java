package co.gov.anm.session.wcc;

import co.gov.anm.entity.AnmTipodtalseguntramite;
import co.gov.anm.entity.AnmTramiteTb;
import co.gov.anm.entity.AnmUnidadadministrativaTb;

import co.gov.anm.entity.Folderfolders;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface WCCSessionEJB9 {
    AnmUnidadadministrativaTb persistAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb);

    AnmUnidadadministrativaTb mergeAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb);

    void removeAnmUnidadadministrativaTb(AnmUnidadadministrativaTb anmUnidadadministrativaTb);

    List<AnmUnidadadministrativaTb> getAnmUnidadadministrativaTbFindAll();

    //Agregados Julian
    AnmUnidadadministrativaTb getAnmUnidadadministrativaByCod(Long codDependencia);

    //tramite
    AnmTramiteTb persistTramite(AnmTramiteTb tramite);

    AnmTramiteTb mergeTramite(AnmTramiteTb tramite);

    void removeTramite(AnmTramiteTb tramite);

    List<AnmTramiteTb> getTramiteFindAll();

    //tramite-tipo
    AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);

    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll();

    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite);

    //folderfolders
    Folderfolders persistFolderFolders(Folderfolders folderFolders);

    Folderfolders mergeFolderFolders(Folderfolders folderFolders);

    void removeFolderFolders(Folderfolders folderFolders);

    List<Folderfolders> getFolderFoldersFindAll();

    List<Folderfolders> getFolderFoldersFindByParentGuid(String parentGuid);

    List<Folderfolders> getFolderFoldersFindByFolderName(String foldername);
}
