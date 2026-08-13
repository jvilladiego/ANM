package co.gov.anm.comunicaciones.model.ejb;

import co.gov.anm.comunicaciones.model.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.model.entity.SgdAnexoComunciacion;
import co.gov.anm.comunicaciones.model.entity.SgdComunicacion;
import co.gov.anm.comunicaciones.model.entity.SgdDependencia;
import co.gov.anm.comunicaciones.model.entity.SgdInteresadoInt;
import co.gov.anm.comunicaciones.model.entity.SgdRol;
import co.gov.anm.comunicaciones.model.entity.SgdTipoIdentificacion;
import co.gov.anm.comunicaciones.model.entity.SgdUsuario;
import co.gov.anm.comunicaciones.model.entity.SgdUsuarioRol;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ModeloComunicacionesLocal3 {
    SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    List<SgdUsuarioRol> getSgdUsuarioRolFindAll();

    SgdDependencia persistSgdDependencia(SgdDependencia sgdDependencia);

    SgdDependencia mergeSgdDependencia(SgdDependencia sgdDependencia);

    void removeSgdDependencia(SgdDependencia sgdDependencia);

    List<SgdDependencia> getSgdDependenciaFindAll();

    SgdUsuario persistSgdUsuario(SgdUsuario sgdUsuario);

    SgdUsuario mergeSgdUsuario(SgdUsuario sgdUsuario);

    void removeSgdUsuario(SgdUsuario sgdUsuario);

    List<SgdUsuario> getSgdUsuarioFindAll();

    SgdInteresadoInt persistSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    SgdInteresadoInt mergeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    void removeSgdInteresadoInt(SgdInteresadoInt sgdInteresadoInt);

    List<SgdInteresadoInt> getSgdInteresadoIntFindAll();

    SgdComunicacion persistSgdComunicacion(SgdComunicacion sgdComunicacion);

    SgdComunicacion mergeSgdComunicacion(SgdComunicacion sgdComunicacion);

    void removeSgdComunicacion(SgdComunicacion sgdComunicacion);

    List<SgdComunicacion> getSgdComunicacionFindAll();

    SgdRol persistSgdRol(SgdRol sgdRol);

    SgdRol mergeSgdRol(SgdRol sgdRol);

    void removeSgdRol(SgdRol sgdRol);

    List<SgdRol> getSgdRolFindAll();

    SgdAnexoComunciacion persistSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    SgdAnexoComunciacion mergeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    void removeSgdAnexoComunciacion(SgdAnexoComunciacion sgdAnexoComunciacion);

    List<SgdAnexoComunciacion> getSgdAnexoComunciacionFindAll();

    SgdTipoIdentificacion persistSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    SgdTipoIdentificacion mergeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    void removeSgdTipoIdentificacion(SgdTipoIdentificacion sgdTipoIdentificacion);

    List<SgdTipoIdentificacion> getSgdTipoIdentificacionFindAll();

    AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla);

    AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla);

    void removeAnmPlantilla(AnmPlantilla anmPlantilla);

    List<AnmPlantilla> getAnmPlantillaFindAll();
}
