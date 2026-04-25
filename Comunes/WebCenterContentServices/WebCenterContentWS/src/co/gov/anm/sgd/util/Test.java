package co.gov.anm.sgd.util;

public class Test {
    public Test() {
        super();
    }
    public static void main (String[] args) {
        try {
            Mail.enviarCorreo("jleonardo.vanegas@outlook.es", "prueba", "prueba");
        } catch (Exception e) {
            e.getStackTrace();
        }
    } 
}
