package ejb;

import entities.AnmPlantilla;
import entities.AnmTipodtalseguntramite;
import entities.AnmTramiteTb;
import entities.AnmUnidadAdministrativaTb;
import entities.SgdAnexoComunciacion;
import entities.SgdComunicacion;
import entities.SgdDepartamento;
import entities.SgdDependencia;
import entities.SgdInteresadoInt;
import entities.SgdMunicipio;
import entities.SgdPais;
import entities.SgdRol;
import entities.SgdTipoIdentificacion;
import entities.SgdTipoSolicitud;
import entities.SgdUsuario;
import entities.SgdUsuarioRol;

import entities.TipoSolicitante;
import entities.folderfolders;

import java.math.BigDecimal;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Local
public interface SessionEJBLocal3 {
    
    List<folderfolders> getfolderfoldersFindAll();
    List<folderfolders> getfolderfoldersFindParent();
    List<folderfolders> getfolderfoldersFindByPArentGuid(String param);
    
    
    List<SgdTipoSolicitud> getSgdTipoSolicitudFindAll();
    SgdTipoSolicitud getSgdTipoSolicitudFindById(Integer param);
    
    List<SgdPais> getSgdPaisFindAll();
    List<SgdDepartamento> getSgdDepartamentoFindAll();
    List<SgdMunicipio> getSgdMunicipioFindByDepartamento(Long idDpto);
    List<SgdMunicipio> getSgdMunicipioFindById(Long idMunicipio);
    
    AnmTramiteTb persistTramite(AnmTramiteTb tramite);
   AnmTramiteTb mergeTramite(AnmTramiteTb tramite);
   void removeTramite(AnmTramiteTb tramite);
   List<AnmTramiteTb> getTramiteFindAll();
   
   AnmTipodtalseguntramite persistTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
   AnmTipodtalseguntramite mergeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
   void removeTipoDocumentalTramite(AnmTipodtalseguntramite tipoDocumentalTramite);
   List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindAll();
   List<AnmTipodtalseguntramite> getTipoDocumentalTramiteFindByTramite(BigDecimal idTramite);
       
       
    AnmUnidadAdministrativaTb persistAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    AnmUnidadAdministrativaTb mergeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    void removeAnmUnidadAdministrativaTb(AnmUnidadAdministrativaTb AnmUnidadAdministrativaTb);
    List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindAll();
    List<AnmUnidadAdministrativaTb> getAnmUnidadAdministrativaTbFindByCodigo(Integer param);
    
    
    AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla);

    AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla);

    void removeAnmPlantilla(AnmPlantilla anmPlantilla);

    List<AnmPlantilla> getAnmPlantillaFindAll();

    SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion);

    SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion);

    void removeSgdComunicacion(SgdComunicacion sgdComunicacion);

    List<SgdComunicacion> getSgdComunicacionFindAll();


    SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);
    SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);
    void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);
    List<SgdUsuarioRol> getSgdUsuarioRolFindAll();
    List<SgdUsuario> getSgdUsuarioByCodDependencia(Long codDependencia);
    
    
    
    
    SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    List<SgdInteresadoInt> getSgdInteresadoIntFindAll();
    

    SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);
    SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);
    void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);
    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll();
    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindByComunicacion(Long idComunicacion);
    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindByNumeroRadicado(String numeroRadicado);

    SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll();

    SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario);

    SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario);

    void removeSgdUsuario(SgdUsuario sgdUsuario);

    List<SgdUsuario> getSgdUsuarioFindAll();
    List<SgdUsuario> getSgdUsuarioFindById(String param);
    List<SgdUsuario> getSgdUsuarioFindByCodDependencia(Long param);
    

    SgdDependencia persistSgdDependencia(SgdDependencia sgdDependencia);

    SgdDependencia mergeSgdDependencia(SgdDependencia sgdDependencia);

    void removeSgdDependencia(SgdDependencia sgdDependencia);

    List<SgdDependencia> getSgdDependenciaFindAll();

    SgdRol persistSgdRol(SgdRol sgdRol);

    SgdRol mergeSgdRol(SgdRol sgdRol);

    void removeSgdRol(SgdRol sgdRol);

    List<SgdRol> getSgdRolFindAll();
    
    // Llenar lista de tipo solicitante
    List<TipoSolicitante> getTipoSolicitanteFindAll();
}
