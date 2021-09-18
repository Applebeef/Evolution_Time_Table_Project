package evolutionaryApp.servlets;

import Generated.ETTDescriptor;
import descriptor.Descriptor;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;


@WebServlet("/pages/uploadTimeTable")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadTimeTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Collection<Part> partCollection = request.getParts();
        for (Part part : partCollection) {
            try {
                Descriptor descriptor = getDescriptor(part.getInputStream());
                descriptor.getTimeTable().setUploader(SessionUtils.getUsername(request));
                ServletUtils.getTimeTableManager(request.getServletContext()).addTimetable(descriptor.getTimeTable());
            } catch (JAXBException ignored) {
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public Descriptor getDescriptor(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ETTDescriptor ettdescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(inputStream);
        return new Descriptor(ettdescriptor);
    }

}
