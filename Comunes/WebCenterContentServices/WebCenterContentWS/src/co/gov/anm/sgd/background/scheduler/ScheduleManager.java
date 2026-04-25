package co.gov.anm.sgd.background.scheduler;

import co.gov.anm.sgd.bean.ComunicacionesLocal3;
import co.gov.anm.sgd.background.business.AlertaMailComunicacion;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.EJB;

import org.apache.log4j.Logger;

//@Startup
//@Singleton
public class ScheduleManager {

    private static final Logger logger = Logger.getLogger(ScheduleManager.class);

    //@EJB
    //ComunicacionesLocal comunicacionLocal;

    //private AlertaMailComunicacion alertaMailComunicacion = new AlertaMailComunicacion();
    
    //Job se ejecuta una vez al dia para enviar alertas de correo
    /*     @Schedule(hour="7", persistent = false)
    public void ejecutarCorreoAlerta() {
        logger.info("Scheduler ejecutarCorreoAlerta Init" + new Date().toString());
        
        try {
            alertaMailComunicacion.generaAlertaComunicacion(comunicacionLocal);
        } catch (Exception e) {
            logger.error("Error ejecutarCorreoAlerta : " + e.getMessage(), e);
        }
    } */
    
}
