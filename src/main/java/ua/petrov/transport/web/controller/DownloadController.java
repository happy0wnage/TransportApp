package ua.petrov.transport.web.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.ParserPath;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Владислав on 18.01.2016.
 */
@Controller
@RequestMapping(Mapping.DOWNLOAD)
public class DownloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    @RequestMapping(value = Mapping.RESULTS, method = RequestMethod.GET)
     public void downloadResultsFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = request.getServletContext();
        File downloadFile = new ClassPathResource(ParserPath.RESULTS_XML).getFile();
        OutputStream outStream = null;
        try (FileInputStream inputStream = new FileInputStream(downloadFile)) {
            response.setContentLength((int) downloadFile.length());
            response.setContentType(context.getMimeType(ParserPath.RESULTS_XML));
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
            outStream = response.getOutputStream();
            IOUtils.copy(inputStream, outStream);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }

        }
    }
}
