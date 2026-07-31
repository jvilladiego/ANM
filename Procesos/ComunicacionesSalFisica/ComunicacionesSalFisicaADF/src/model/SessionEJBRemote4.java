package model;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SessionEJBRemote4 {
    
    AnmTramiteTb persistTramite(AnmTramiteTb tramite);
    AnmTramiteTb mergeTramite(AnmTramiteTb tramite);
    void removeTramite(AnmTramiteTb tramite);
    List<AnmTramiteTb> getTramiteFindAll();
    
    
    AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
    AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
    void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll();
    List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite);
    
    
    List<SgdDepartamento> getSgdDepartamentoFindAll();
    List<SgdMunicipio> getSgdMunicipioFindByDepartamento(Long idDpto);
    
    List<folderfolders> getfolderfoldersFindAll();
    List<folderfolders> getfolderfoldersFindParent();
    List<folderfolders> getfolderfoldersFindByPArentGuid(String param);
    
    SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll();
    
    AnmUnidadAdministrativaTb persistAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    AnmUnidadAdministrativaTb mergeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    void removeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindAll();
    List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindByCodigo(Integer param);

    SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario);

    SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario);

    void removeSgdUsuario(SgdUsuario sgdUsuario);

    List<SgdUsuario> getSgdUsuarioFindAll();
    
    List<SgdUsuario> getSgdUsuarioFindById(String param);
    
    List<SgdUsuario> getSgdUsuarioFindByCodDependencia(Long param);

    SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    List<SgdInteresadoInt> getSgdInteresadoIntFindAll();

    SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll();

    SgdRol persistSgdRol(SgdRol sgdRol);

    SgdRol mergeSgdRol(SgdRol sgdRol);

    void removeSgdRol(SgdRol sgdRol);

    List<SgdRol> getSgdRolFindAll();

    AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla);

    AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla);

    void removeAnmPlantilla(AnmPlantilla anmPlantilla);

    List<AnmPlantilla> getAnmPlantillaFindAll();

    Dept persistDept(Dept dept);

    Dept mergeDept(Dept dept);

    void removeDept(Dept dept);

    List<Dept> getDeptFindAll();

    SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion);

    SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion);

    void removeSgdComunicacion(SgdComunicacion sgdComunicacion);

    List<SgdComunicacion> getSgdComunicacionFindAll();

    PsTxn persistPsTxn(PsTxn psTxn);

    PsTxn mergePsTxn(PsTxn psTxn);

    void removePsTxn(PsTxn psTxn);

    List<PsTxn> getPsTxnFindAll();

    SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    List<SgdUsuarioRol> getSgdUsuarioRolFindAll();

    Emp persistEmp(Emp emp);

    Emp mergeEmp(Emp emp);

    void removeEmp(Emp emp);

    List<Emp> getEmpFindAll();
}
