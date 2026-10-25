package co.gov.anm.session.com;

import co.gov.anm.entity.AnmPlantilla;
import co.gov.anm.entity.SgdAnexoComunciacion;
import co.gov.anm.entity.SgdComunicacion;
import co.gov.anm.entity.SgdDepartamento;
import co.gov.anm.entity.SgdDependencia;
import co.gov.anm.entity.SgdEtnia;
import co.gov.anm.entity.SgdInteresadoInt;
import co.gov.anm.entity.SgdMunicipio;
import co.gov.anm.entity.SgdPais;
import co.gov.anm.entity.SgdPoblacionVulnerable;
import co.gov.anm.entity.SgdRangoEdad;
import co.gov.anm.entity.SgdRol;
import co.gov.anm.entity.SgdTipoIdentificacion;
import co.gov.anm.entity.SgdTipoSolicitud;
import co.gov.anm.entity.SgdUsuario;
import co.gov.anm.entity.SgdUsuarioRol;
import co.gov.anm.entity.SgdOcupacion;

import co.gov.anm.entity.TipoSolicitante;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ComunicacionesEntSessionEJBLocal9 {
    SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario);

    SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario);

    void removeSgdUsuario(SgdUsuario sgdUsuario);

    List<SgdUsuario> getSgdUsuarioFindAll();

    List<SgdUsuario> getSgdUsuarioByCodDependencia(Long codDependencia);

    SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    List<SgdInteresadoInt> getSgdInteresadoIntFindAll();

    SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll();

    // tipo de solicitud agregado manualmente

    SgdTipoSolicitud persistSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud);

    SgdTipoSolicitud mergeSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud);

    void removeSgdTipoSolicitud(SgdTipoSolicitud sgdTipoSolicitud);

    List<SgdTipoSolicitud> getSgdTipoSolicitudFindAll();

    SgdTipoSolicitud getTipoSolicitudById(Long idTipoPQRS);

    SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    List<SgdUsuarioRol> getSgdUsuarioRolFindAll();

    SgdDependencia persistSgdDependencia(SgdDependencia sgdDependencia);

    SgdDependencia mergeSgdDependencia(SgdDependencia sgdDependencia);

    void removeSgdDependencia(SgdDependencia sgdDependencia);

    List<SgdDependencia> getSgdDependenciaFindAll();

    SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion);

    SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion);

    void removeSgdComunicacion(SgdComunicacion sgdComunicacion);

    List<SgdComunicacion> getSgdComunicacionFindAll();

    AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla);

    AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla);

    void removeAnmPlantilla(AnmPlantilla anmPlantilla);

    List<AnmPlantilla> getAnmPlantillaFindAll();

    SgdRol persistSgdRol(SgdRol sgdRol);

    SgdRol mergeSgdRol(SgdRol sgdRol);

    void removeSgdRol(SgdRol sgdRol);

    List<SgdRol> getSgdRolFindAll();

    SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll();

    SgdUsuario getSgdUsuarioById(String idUsuario);

    //rango edad
    SgdRangoEdad persistSgdRangoEdad(SgdRangoEdad sgdRangoEdad);

    SgdRangoEdad mergeSgdRangoEdad(SgdRangoEdad sgdRangoEdad);

    void removeSgdRangoEdad(SgdRangoEdad SgdRangoEdad);

    List<SgdRangoEdad> getSgdRangoEdadFindAll();

    //poblacion vulnerable
    SgdPoblacionVulnerable persistSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable);

    SgdPoblacionVulnerable mergeSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable);

    void removeSgdPoblacionVulnerable(SgdPoblacionVulnerable sgdPoblacionVulnerable);

    List<SgdPoblacionVulnerable> getSgdPoblacionVulnerableFindAll();

    //etnia
    SgdEtnia persistSgdEtnia(SgdEtnia sgdEtnia);

    SgdEtnia mergeSgdEtnia(SgdEtnia sgdEtnia);

    void removeSgdEtnia(SgdEtnia sgdEtnia);

    List<SgdEtnia> getSgdEtniaFindAll();

    //pais
    SgdPais persistSgdPais(SgdPais sgdPais);

    SgdPais mergeSgdPais(SgdPais sgdPais);

    void removeSgdPais(SgdPais sgdPais);

    List<SgdPais> getSgdPaisFindAll();

    //departamento
    SgdDepartamento persistSgdDepartamento(SgdDepartamento sgdDepartamento);

    SgdDepartamento mergeSgdDepartamento(SgdDepartamento sgdDepartamento);

    void removeSgdDepartamento(SgdDepartamento sgdDepartamento);

    List<SgdDepartamento> getSgdDepartamentoFindAll();

    //municipio-ciudad
    SgdMunicipio persistSgdMunicipio(SgdMunicipio sgdMunicipio);

    SgdMunicipio mergeSgdMunicipio(SgdMunicipio sgdMunicipio);

    void removeSgdMunicipio(SgdMunicipio sgdMunicipio);

    List<SgdMunicipio> getSgdMunicipioFindAll();

    List<SgdMunicipio> getMunicipioByDepartamento(int idDepartamento);
    
    // Llenar lista de tipo solicitante
    List<TipoSolicitante> getTipoSolicitanteFindAll();
	
	List<SgdOcupacion> getOcupacionFindAll();
}