package cl.camanchaca.orders.utils;

import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class InputStreamUtils {
    public static InputStream combineInputStreams(List<InputStream> inputStreamList) {
        ByteArrayOutputStream combinedOutput = new ByteArrayOutputStream();

        for (InputStream inputStream : inputStreamList) {
            try {
                IOUtils.copy(inputStream, combinedOutput);
                inputStream.close();
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }

        return new ByteArrayInputStream(combinedOutput.toByteArray());
    }

    private InputStreamUtils(){}
}
