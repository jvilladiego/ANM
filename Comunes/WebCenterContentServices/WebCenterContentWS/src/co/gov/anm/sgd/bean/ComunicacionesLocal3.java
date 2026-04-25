package co.gov.anm.sgd.bean;

import co.gov.anm.sgd.entity.SgdAnexoComunicacion;
import co.gov.anm.sgd.entity.SgdComunicacion;
import co.gov.anm.sgd.entity.SgdFestivo;
import co.gov.anm.sgd.entity.SgdInteresado;
import co.gov.anm.sgd.entity.SgdUsuario;

import co.gov.anm.sgd.entity.Subcategoria;
import co.gov.anm.sgd.entity.TipoSolicitante;
import co.gov.anm.sgd.entity.TipoSolicitud;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ComunicacionesLocal3 {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    void removeSgdFestivo(SgdFestivo sgdFestivo);

    List<SgdFestivo> getSgdFestivoFindAll();
    
    Date getFechaVencimiento(Integer diasRespuesta, Date fechaRadicacion);
    
    String getNumeroRadicado (String codDependencia, String tipoComunicacion);
    
    SgdUsuario getUsuarioById (String idUsuario);
    
    void insertComunicacion (SgdComunicacion comunicacion);
    
    //SgdComunicacion getSgdComunicacionByNroRadicado(String nroRadicado);
    List<SgdComunicacion> getSgdComunicacionByNroRadicado(String nroRadicado);
    
    List<SgdAnexoComunicacion> getAnexosByIdComunicacion (BigDecimal idComunicacion);
    
    List<SgdInteresado> getInteresadosByIdComunicacion (BigDecimal idComunicacion);    
    
    int deleteSgdAnexoByIdComunicacion (BigDecimal idComunicacion);
    
    void updateComunicacion (SgdComunicacion comunicacion);
    
    List findUsuarioByDepRol (Long idRol, Long codDependencia);
    
    TipoSolicitud getTipoSolicitudById(Long id);
    
    Subcategoria getSubcategoriaById(BigDecimal id);
    
    TipoSolicitante getTipoSolicitanteById(Long id);
    
    List<TipoSolicitud> getTipoSolicitudAlerta();
    
    List<SgdComunicacion> getComunicacionesTipoSol( Long idTiposolicitud, String diasAlerta);
    
    SgdUsuario getUsuarioByInstanceNumber(Long instanceNumber);
}
