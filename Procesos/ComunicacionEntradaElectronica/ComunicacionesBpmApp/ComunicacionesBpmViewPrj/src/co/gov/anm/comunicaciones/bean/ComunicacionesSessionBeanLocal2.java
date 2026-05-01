package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.SgdRol;
import co.gov.anm.comunicaciones.entity.SgdUsuario;
import co.gov.anm.comunicaciones.entity.SgdUsuarioRol;

import co.gov.anm.comunicaciones.entity.UnidadAdministrativa;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ComunicacionesSessionBeanLocal2 {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    SgdUsuarioRol persistSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    SgdUsuarioRol mergeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    void removeSgdUsuarioRol(SgdUsuarioRol sgdUsuarioRol);

    List<SgdUsuarioRol> getSgdUsuarioRolFindAll();

    SgdRol persistSgdRol(SgdRol sgdRol);

    SgdRol mergeSgdRol(SgdRol sgdRol);

    void removeSgdRol(SgdRol sgdRol);

    List<SgdRol> getSgdRolFindAll();
    
    List<SgdUsuario> getSgdUsuarioFindByDependencia(Long codigoDependencia);
    
    List<AnmPlantilla> getAnmPlantillaFindAll();
    
    public List<SgdUsuario> getSgdUsuarioFindById(String idUsuario);
    
    public List<UnidadAdministrativa> getUnidadAdministrativaFindAll();
    
}
