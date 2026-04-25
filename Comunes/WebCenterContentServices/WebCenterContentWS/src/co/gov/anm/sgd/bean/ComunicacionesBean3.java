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

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;

@Stateless(name = "Comunicaciones", mappedName = "ComunicacionesBean")
public class ComunicacionesBean3 implements Comunicaciones3, ComunicacionesLocal3 {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "Comunicaciones")
    private EntityManager em;

    public ComunicacionesBean3() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
    }

    public void removeSgdFestivo(SgdFestivo sgdFestivo) {
        sgdFestivo = em.find(SgdFestivo.class, sgdFestivo.getIdFestivo());
        em.remove(sgdFestivo);
    }

    /** <code>select o from SgdFestivo o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdFestivo> getSgdFestivoFindAll() {
        return em.createNamedQuery("SgdFestivo.findAll").getResultList();
    }
    
    /*@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdComunicacion getSgdComunicacionByNroRadicado(String nroRadicado) {
        return (SgdComunicacion) em.createNamedQuery("SgdComunicacion.findByNroRadicado").
                                    setParameter("nroRadicado", nroRadicado).getSingleResult();
    }*/
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdComunicacion> getSgdComunicacionByNroRadicado(String nroRadicado) {
            return em.createNamedQuery("SgdComunicacion.findByNroRadicado").
                                        setParameter("nroRadicado", nroRadicado).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Date getFechaVencimiento(Integer diasRespuesta, Date fechaRadicacion) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("calcularFechaResp");
        
        query.registerStoredProcedureParameter("diasRespuesta", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("fechaRadicacion", java.sql.Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("fechaRespuesta", java.sql.Date.class, ParameterMode.OUT);
        
        query.setParameter("diasRespuesta", diasRespuesta);
        query.setParameter("fechaRadicacion", fechaRadicacion);
        
        query.execute();
        
        return (Date) query.getOutputParameterValue("fechaRespuesta");
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getNumeroRadicado (String codDependencia, String tipoComunicacion) {
        return (String) em.createNativeQuery("select to_char(sysdate, 'YYYY') || " +
            codDependencia + " || lpad(SGD_COM_" + codDependencia + "_SEQ.nextval, 6, '0') || " + tipoComunicacion +
            " from dual").getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdUsuario getUsuarioById (String idUsuario) {
        return (SgdUsuario) em.createNamedQuery("SgdUsuario.findById").setParameter("idUsuario", idUsuario).getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void insertComunicacion (SgdComunicacion comunicacion) {        
        em.persist(comunicacion);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateComunicacion (SgdComunicacion comunicacion) {
        em.merge(comunicacion);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdAnexoComunicacion> getAnexosByIdComunicacion (BigDecimal idComunicacion) {
        return em.createQuery("SgdAnexoComunicacion.findByIdComunicacion").
                  setParameter("idComunicacion", idComunicacion).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public int deleteSgdAnexoByIdComunicacion (BigDecimal idComunicacion) {
        return em.createNativeQuery("delete from Sgd_Anexo_Comunicacion o where o.id_comunicacion = " + idComunicacion).executeUpdate();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdInteresado> getInteresadosByIdComunicacion (BigDecimal idComunicacion) {
        SgdComunicacion comunicacion = new SgdComunicacion();
        comunicacion.setIdComunicacion(idComunicacion);
        return em.createNamedQuery("SgdInteresado.findByIdComunicacion").setParameter("idComunicacion", comunicacion).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List findUsuarioByDepRol (Long idRol, Long codDependencia) {
        String sql = "SELECT U.* " + 
                     "FROM SGD_USUARIO U, SGD_USUARIO_ROL UR " + 
                     "WHERE U.ID_USUARIO = UR.ID_USUARIO ";
        
        if (codDependencia != 0) {
            sql = sql + "AND U.CODIGO_DEPENDENCIA = " + codDependencia + " ";
        }
        
        sql = sql + "AND UR.ID_ROL = " + idRol;
        
        return em.createNativeQuery(sql).getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoSolicitud getTipoSolicitudById(Long id) {
        return (TipoSolicitud) em.createNamedQuery("TipoSolicitud.findById")
                 .setParameter("idTipo", id).getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Subcategoria getSubcategoriaById(BigDecimal id) {
        return (Subcategoria) em.createNamedQuery("Subcategoria.findById")
                 .setParameter("idSub", id).getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoSolicitante getTipoSolicitanteById(Long id) {
        return (TipoSolicitante) em.createNamedQuery("TipoSolicitante.findById")
                 .setParameter("idTipo", id).getSingleResult();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoSolicitud> getTipoSolicitudAlerta(){
        Query query = em.createQuery("Select t from TipoSolicitud t where t.diasAlerta is not null", TipoSolicitud.class );
        return (List<TipoSolicitud>)query.getResultList(); 
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgdComunicacion> getComunicacionesTipoSol( Long idTiposolicitud, String diasAlerta){
        
        String sql = "Select * from Sgd_Comunicacion c " +
                     "where c.id_Tipo_Solicitud = "+ idTiposolicitud +" "+
                     "and c.estado_Comunicacion not in ('CERRADO','CERRADA')"+
                     "and (c.fecha_Vencimiento - trunc(sysdate) ) in ( "+diasAlerta +") ";
        Query q = em.createNativeQuery(sql, SgdComunicacion.class);
        q.setParameter("idTiposolicitud", idTiposolicitud);
        q.setParameter("diasAlerta", diasAlerta);
        return (List<SgdComunicacion>)q.getResultList();
                                                                    
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public SgdUsuario getUsuarioByInstanceNumber( Long instanceNumber ){
        String sql = "select id_usuario, nombre_usuario, email " +
                     "from sgd_usuario u, ( select (substr(t.assignees, 0,instr(t.assignees, ',')-1)) usuario " +
                                            "from wftaskhistory t " +
                                            "where t.instanceid= '"+ instanceNumber +"' "+
                                            "and t.assignees is not null " +
                                            "order by t.createddate desc) a " +
                     "where u.id_usuario = a.usuario" +
                     " and rownum = 1 ";
        Query query = em.createNativeQuery(sql, SgdUsuario.class);
        query.setParameter("instanceNumber",instanceNumber);
        return (SgdUsuario) query.getSingleResult();
    }
    
}
