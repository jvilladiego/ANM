package co.gov.anm.sgd.util;

import co.gov.anm.digitalsignature.model.validarUsuario.RootResponse;

import co.gov.anm.digitalsignature.model.validarUsuario.UsuarioFirma;
import co.gov.anm.digitalsignature.service.FirmaDigitalService;

import co.gov.anm.digitalsignature.utils.FileUtils;

import co.gov.anm.sgd.model.GrafoFirmaDigital;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.InvalidNavigationException;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;

public class TemplateMerger {

    private static final Logger logger = Logger.getLogger(TemplateMerger.class);

    public TemplateMerger() {
        super();
    }

    public void buscarYReemplazar(Object documento, String mimeType, String cadenaReemplazo,
                                  String cadenaReemplazar) throws InvalidNavigationException {

        if (mimeType.equals("application/vnd.oasis.opendocument.text")) {
            buscarYReemplazarEnODT((TextDocument) documento, cadenaReemplazo, cadenaReemplazar);
        } else {
            buscarYReemplazarEnDocum((XWPFDocument) documento, cadenaReemplazo, cadenaReemplazar);
        }
    }

    private void buscarYReemplazarEnODT(TextDocument documento, String cadenaReemplazo,
                                        String cadenaReemplazar) throws InvalidNavigationException {
        TextNavigation search1;
        search1 = new TextNavigation(cadenaReemplazar, documento);

        while (search1.hasNext()) {
            TextSelection item = (TextSelection) search1.nextSelection();

            item.replaceWith(cadenaReemplazo);
        }
    }

    public void buscarYReemplazarImagen(Object objectDoc, String rutaImagen, String cadenaReemplazar,
                                        String mimeType) throws InvalidNavigationException, URISyntaxException {
        logger.debug("INICIO buscarYReemplazarImagen");
        if (mimeType.equals("application/vnd.oasis.opendocument.text")) {
            buscarYReemplazarImagenEnOdt((TextDocument) objectDoc, rutaImagen, cadenaReemplazar);
        } else {
            buscarYReemplazarEnDocum((XWPFDocument) objectDoc, rutaImagen, cadenaReemplazar);
        }
        logger.debug("FIN buscarYReemplazarImagen");
    }


    private void buscarYReemplazarImagenEnOdt(TextDocument documento, String rutaImagen,
                                              String cadenaReemplazar) throws InvalidNavigationException,
                                                                              URISyntaxException {
        logger.debug("INICIO buscarYReemplazarImagenEnOdt");
        logger.debug("rutaImagen: " + rutaImagen);
        logger.debug("cadenaReemplazar: " + cadenaReemplazar);
        try {
            /***************************************************
             * Las lineas a continuacion se comentan ya que el codigo genera un mensaje
             * de error "IndexOutofbounds", a pesar de ser el codigo oficial sugerido
             * https://incubator.apache.org/odftoolkit/simple/document/cookbook/Manipulate%20TextSearch.html
             * ***************************************************/
            /*
            while(search1.hasNext()) {
                logger.debug("TRAZA");
                TextSelection item = (TextSelection) search1.nextSelection();
                logger.debug("Item index: "+item.getIndex());
                item.replaceWith(new URI(rutaImagen));
                logger.debug("Item text: "+item.getText());
            }
            */
            /***************************************************
             * Se itera un total de cinco veces para los casos donde el documento
             * contiene mas de una seccion (cambio de orientacion de la pagina)
             * ***************************************************/
            for (int i = 0; i < 5; i++) {
                TextNavigation search1 = new TextNavigation(cadenaReemplazar, documento);
                if (search1.hasNext()) {
                    TextSelection item = (TextSelection) search1.nextSelection();
                    item.replaceWith(new URI(rutaImagen));
                    
                }
            }
        } catch (Exception e) {
            logger.debug("Exception buscarYReemplazarImagenEnOdt", e);
        }
        logger.debug("FIN buscarYReemplazarImagenEnOdt");
    }

    /*
    private void buscarYReemplazarImagenEnDocum(XWPFDocument doc, String findText, String rutaImagen){
        logger.debug("INICIO buscarYReemplazarImagenEnDocum");
        try {
            logger.debug("findText: "+findText);
            logger.debug("rutaImagen: "+rutaImagen);
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    logger.debug(run.text());
                    if (run.text().contains(findText)) {
                        logger.debug("encontr� cadena");
                        run.setText(new URI(rutaImagen));
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Exception buscarYReemplazarImagenEnDocum",e);
        }
        logger.debug("FIN buscarYReemplazarImagenEnDocum");
    }
    */


    private void buscarYReemplazarEnDocum(XWPFDocument doc, String findText, String replaceText) {
        logger.debug("INICIO buscarYReemplazarEnDocum");
        try {
            logger.debug("findText: " + findText);
            logger.debug("replaceText: " + replaceText);
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    logger.debug("text: " + run.text());
                    if (run.text().contains(findText)) {
                        logger.debug("encontr� texto");
                        run.setText(replaceText);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Exception buscarYReemplazarEnDocum", e);
        }
        logger.debug("FIN buscarYReemplazarEnDocum");
    }

    /*
    private void buscarYReemplazarEnDoc(XWPFDocument doc, String findText, String replaceText) {

        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                logger.debug(run.text());
                if (run.text().contains(findText)) {
                    logger.debug("encontr� texto");
                    run.setText(replaceText);
                }
            }
        }

//        Range r1 = doc.getRange();
//
//        for (int i = 0; i < r1.numSections(); ++i) {
//            Section s = r1.getSection(i);
//            for (int x = 0; x < s.numParagraphs(); x++) {
//                Paragraph p = s.getParagraph(x);
//                for (int z = 0; z < p.numCharacterRuns(); z++) {
//                    CharacterRun run = p.getCharacterRun(z);
//                    String text = run.text();
//                    if (text.contains(findText)) {
//                        run.replaceText(findText, replaceText);
//                    }
//                }
//            }
//        }
        //return doc;
    }
*/
    public void saveDoc(Object objectDoc, String filePath, String mimeType) throws FileNotFoundException, IOException,
                                                                                   Exception {
        if (mimeType.equals("application/vnd.oasis.opendocument.text")) {
            TextDocument documentoODT = (TextDocument) objectDoc;

            documentoODT.save(filePath);
        } else {
            saveWord(filePath, (XWPFDocument) objectDoc);
        }
    }

    private void saveWord(String filePath, XWPFDocument doc) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            doc.write(out);
        } finally {
            out.close();
        }
    }

    public String generarCodigoBarras(String nroRadicado, String ruta) throws IOException {
        Code39Bean bean39 = new Code39Bean();
        final int dpi = 160;

        //Configure the barcode generator
        bean39.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        bean39.setHeight(6.0);
        bean39.setWideFactor(3);
        bean39.setFontSize(0);
        bean39.doQuietZone(false);

        //Open output file
        File outputFile = new File(ruta + File.separator + nroRadicado + ".png");

        FileOutputStream out = new FileOutputStream(outputFile);

        //Set up the canvas provider for monochrome PNG output
        BitmapCanvasProvider canvas =
            new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        //Generate the barcode
        bean39.generateBarcode(canvas, nroRadicado);

        //Signal end of generation
        canvas.finish();

        logger.debug("codigo de barras generado exitosamente: " + outputFile.getPath());

        return outputFile.getPath();
    }

    public GrafoFirmaDigital generarGrafo(String documento, String ruta) throws IOException {
        /**
         * Obtener el grafo a traves del servicio de
         */
        GrafoFirmaDigital firmaDigital;
        FirmaDigitalService firmaDigitalService = new FirmaDigitalService();
        RootResponse responseValidar = firmaDigitalService.validarUsuario(documento);
        String path = ruta + File.separator + documento + ".png";
        if (responseValidar.getRespuesta() != null && !responseValidar.getRespuesta()
                                                                      .getUsuariosFirma()
                                                                      .isEmpty()) {
            UsuarioFirma usuarioFirma = responseValidar.getRespuesta()
                                                       .getUsuariosFirma()
                                                       .get(0);
            FileUtils.Base64BinaryToEncodeFile(usuarioFirma.getGrafoFirma(), path);
            firmaDigital = new GrafoFirmaDigital(usuarioFirma.getNombreUsuario(), usuarioFirma.getCargoUsuario(), path);
            logger.debug("cargo"+firmaDigital.getCargoUsuario()+" - nombre :"+firmaDigital.getNombreUsuario()+" - ruta :"+firmaDigital.getRutaGrafo());
            return firmaDigital;

        } else {
            return null;
        }
    }


    public String obtenerRutaArchivos(String rutaArchivo) {
        String[] vectorRuta = rutaArchivo.split(File.separator);
        String path = "";
        for (int i = 1; i < vectorRuta.length - 1; i++) {
            path = path + File.separator + vectorRuta[i];
        }
        return path;
    }

    public String obtenerNombreArchivo(String rutaArchivo, String nroRadicado, boolean isPDF) {
        String[] rutaVector = rutaArchivo.split(File.separator);
        String nombre = rutaVector[rutaVector.length - 1];
        String[] nomVector = nombre.split("\\.");
        if (isPDF) {
            if (nroRadicado != null && nroRadicado.length() > 2) {
                return ("COM_" + nroRadicado + ".pdf");
            } else {
                return (nombre.substring(0, nombre.lastIndexOf(".")) + "_R.pdf");
            }
        }

        return (nombre.substring(0, nombre.lastIndexOf(".")) + "_R." +
                nombre.substring(nombre.lastIndexOf(".") + 1, nombre.length()));
    }

    /*private String convertirArchivoPDF(String rutaArchivo, String rutaDestino) throws FileNotFoundException, Exception {
            Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);


            IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

            // 3) Convert ODT 2 PDF
            InputStream in;
            in = new FileInputStream(new File(rutaArchivo));
            OutputStream out = new FileOutputStream(new File(rutaDestino));

            converter.convert(in, out, options);

            return null;
        }*/
}
