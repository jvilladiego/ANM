package co.gov.anm.sgd.background.business;

import co.gov.anm.sgd.mngr.ComunicacionesMngr;

import java.util.List;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.entity.SgdAnexoComunicacion;
import co.gov.anm.sgd.entity.TipoSolicitud;
import co.gov.anm.sgd.entity.SgdComunicacion;
import co.gov.anm.sgd.entity.SgdInteresado;
import co.gov.anm.sgd.entity.SgdUsuario;
import co.gov.anm.sgd.util.Mail;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

public class AlertaMailComunicacion {

    private static final Logger logger = Logger.getLogger(AlertaMailComunicacion.class);

    public AlertaMailComunicacion() {
    }

    public void generaAlertaComunicacion(ComunicacionesLocal3 comunicacionLocal) throws Exception {
        
        logger.info("generaAlertaComunicacion init ");
        
        List<TipoSolicitud> tipoSolicitudAlerta = null;
        List<SgdComunicacion> comunicaciones = null;

        tipoSolicitudAlerta = comunicacionLocal.getTipoSolicitudAlerta();

        for (TipoSolicitud tipoSolicitud : tipoSolicitudAlerta) {
            comunicaciones = comunicacionLocal.getComunicacionesTipoSol(tipoSolicitud.getIdTipoSolicitud(),
                                                           tipoSolicitud.getDiasAlerta().replace("-", ","));

            for (SgdComunicacion comunicacion : comunicaciones) {

                SgdAnexoComunicacion anexo = null;
                
                if (comunicacion.getSgdAnexoComunicacionList() != null &&
                    comunicacion.getSgdAnexoComunicacionList().size() > 0  ) {
                    anexo = comunicacion.getSgdAnexoComunicacionList()
                                        .iterator()
                                        .next();
                }

                SgdInteresado interesado = null;
                if (comunicacion.getSgdInteresadoList() != null && !comunicacion.getSgdInteresadoList().isEmpty()) {
                    interesado = comunicacion.getSgdInteresadoList()
                                             .iterator()
                                             .next();
                }
                
                String login = comunicacion.getUsuarioActualizacion() != null && !comunicacion.getUsuarioActualizacion().isEmpty()? 
                    comunicacion.getUsuarioActualizacion() : comunicacion.getUsuarioCreacion();
                SgdUsuario usuario = null;
                logger.info("Nro Radicado : "+comunicacion.getNroRadicado());
                logger.info("comunicacion.UsuarioActualizacion : "+comunicacion.getUsuarioActualizacion());
                logger.info("comunicacion.UsuarioCreacion : "+comunicacion.getUsuarioCreacion());
                logger.info("login del usuario a informar : "+login);
                try{
                    usuario = comunicacionLocal.getUsuarioById(login);
                }catch (NoResultException nre){
                    logger.error("El usuario : "+login+" no esta registrado",nre);
                    
                }
                    //comunicacionLocal.getUsuarioByInstanceNumber(comunicacion.getInstanceNumber()) : null;
                    

                if (usuario != null) {
                    
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
                    Date today = formatter.parse(formatter.format(new Date())); 
                    long diffDays = TimeUnit.DAYS.convert(Math.abs( comunicacion.getFechaVencimiento().getTime() - today.getTime()), TimeUnit.MILLISECONDS); 
                    logger.debug("tipoSolicitud.getPlantillaAlerta : "+tipoSolicitud.getPlantillaAlerta());
                    logger.debug("comunicacion.getNroRadicado : "+comunicacion.getNroRadicado());
                    logger.debug("diffDays : "+String.valueOf(diffDays));
                    logger.debug("anexo.getEnlaceContent : "+((anexo != null) ? anexo.getEnlaceContent() : ""));
                    logger.debug("tipoSolicitud.getNombre : "+tipoSolicitud.getNombre());
                    logger.debug("interesado.getNombreCompleto : "+((interesado != null) ? interesado.getNombreCompleto() : ""));
                    logger.debug("interesado.getEmail : "+((interesado != null) ? interesado.getEmail() : ""));
                    
                    try {
                        String mailBody =
                            Mail.formatText(tipoSolicitud.getPlantillaAlerta(), 
                                            comunicacion.getNroRadicado(),
                                            (new SimpleDateFormat("dd-MM-yyyy").format(comunicacion.getFechaVencimiento())).toString(), 
                                            String.valueOf(diffDays),
                                            (anexo != null) ? anexo.getEnlaceContent() : "", 
                                            tipoSolicitud.getNombre(),
                                            (interesado != null) ? interesado.getNombreCompleto() : "",
                                            (interesado != null) ? interesado.getEmail() : "",
                                            (new SimpleDateFormat("dd-MM-yyyy").format(comunicacion.getFechaCreacion())).toString());
                        Mail.enviarCorreo(usuario.getEmail(), mailBody, "PQRS " + comunicacion.getNroRadicado() + "  pr�xima a vencer ");
                        logger.info("Se envia correo de alerta Radicado: " + comunicacion.getNroRadicado());
                    } catch (AddressException e) {
                        logger.error("Error AlertaMailComunicacion.AddressException : " + e.getMessage(), e);
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        logger.error("Error AlertaMailComunicacion.MessagingException : " + e.getMessage(), e);
                        e.printStackTrace();
                    } catch (Exception e) {
                        logger.error("Error AlertaMailComunicacion.Exception : " + e.getMessage(), e);
                        e.printStackTrace();
                    } 
                } else {
                    logger.error(" Radicado  : " + comunicacion.getNroRadicado() +
                                 " sin usuario asignado. No se envia correo de alerta");
                }
            }

        }
    }
}
