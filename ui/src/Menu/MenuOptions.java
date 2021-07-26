package Menu;

import Generated.ETTDescriptor;
import UI.*;
import descriptor.Descriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Scanner;

public enum MenuOptions {
    //                * Read Xml
    //                * Display time table
    //                * Run evolutionary algorithm (engine)
    //                * Show best solution
    //                * View engine progression
    //                * Exit
    READ_XML(1, "Load data from XML file."){
        @Override
        public void start() {
            String filename = "C:\\Users\\oroth\\IdeaProjects\\Evolution Time Project\\xml_parser\\src\\XML\\EX1-small.xml";
            //Scanner scanner = new Scanner(System.in);/TODO uncomment before submitting.
            //filename = scanner.nextLine();

            try {
                File file = new File(filename);
                JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ETTDescriptor ettdescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
                UI.setDescriptor(new Descriptor(ettdescriptor));

            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
    },
    DISPLAY_TIME_TABLE(2, "Display info about the time table."){
        @Override
        public void start() {
            //TODO add function
        }
    },
    RUN_ENGINE(3, "Run evolutionary algorithm."){
        @Override
        public void start() {
            //TODO add function
        }
    },
    SHOW_BEST_SOLUTION(4, "Display best solution."){
        @Override
        public void start() {
            //TODO add function
        }
    },
    VIEW_PROGRESS(5, "View progress."){
        @Override
        public void start() {
            //TODO add function
        }
    },
    EXIT(6,"Exit."){
        @Override
        public void start() {
            UI.setExit(true);
        }
    };

    int number;
    String action;

    MenuOptions(int number, String action){
        this.number = number;
        this.action = action;
    }

    abstract public void start();

    @Override
    public String toString() {
        return number + " - " + action;
    }
}
