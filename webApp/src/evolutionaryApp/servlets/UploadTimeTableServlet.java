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
import java.util.Set;


@WebServlet("/pages/mainPage/uploadTimeTable")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadTimeTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Collection<Part> partCollection = request.getParts();
        String str = "File uploaded successfully.";
        for (Part part : partCollection) {
            try {
                Descriptor descriptor = getDescriptor(part.getInputStream());
                Set<String> set = descriptor.checkValidity();
                if (set.isEmpty() || (set.size() == 1 && set.contains(""))) {
                    descriptor.getTimeTable().setUploader(SessionUtils.getUsername(request));
                    ServletUtils.getDescriptorManager(request.getServletContext()).addDescriptor(descriptor);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    set.forEach(stringBuilder::append);
                    str = stringBuilder.toString();
                }
            } catch (JAXBException ignored) {
            }
        }
        try (PrintWriter out = response.getWriter()) {
            out.println(str);
            out.flush();
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
