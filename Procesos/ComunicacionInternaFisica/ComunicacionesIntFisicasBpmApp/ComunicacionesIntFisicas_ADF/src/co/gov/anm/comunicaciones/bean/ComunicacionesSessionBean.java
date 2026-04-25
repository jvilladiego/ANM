package co.gov.anm.comunicaciones.bean;

import co.gov.anm.comunicaciones.entity.AnmPlantilla;
import co.gov.anm.comunicaciones.entity.SgdRol;
import co.gov.anm.comunicaciones.entity.SgdUsuarioRol;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ComunicacionesSessionBean {
    
    AnmPlantilla persistAnmPlantilla(AnmPlantilla anmPlantilla);

    AnmPlantilla mergeAnmPlantilla(AnmPlantilla anmPlantilla);

    void removeAnmPlantilla(AnmPlantilla anmPlantilla);

    List<AnmPlantilla> getAnmPlantillaFindAll();
    
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
}
