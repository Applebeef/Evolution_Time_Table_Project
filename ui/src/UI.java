import Generated.ETTDescriptor;
import Solution.TimeTableSolution;
import descriptor.Descriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Scanner;

public class UI {
    //Singleton instance:
    private static final UI instance = new UI();
    public boolean exit;
    public boolean fileLoaded;
    public Descriptor descriptor;

    // Inner enum MenuOptions:
    public enum MenuOptions {
        //                * Read Xml
        //                * Display time table
        //                * Run evolutionary algorithm (engine)
        //                * Show best solution
        //                * View engine progression
        //                * Exit
        READ_XML(1, "Load data from XML file.") {
            @Override
            public void start(UI ui) {//TODO check file validity.
                String filename = "xml_parser\\src\\XML\\EX1-small.xml";
                //Scanner scanner = new Scanner(System.in);/TODO uncomment before submitting.
                //filename = scanner.nextLine();

                try {
                    File file = new File(filename);
                    JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    ETTDescriptor ettdescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
                    ui.setDescriptor(new Descriptor(ettdescriptor));

                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                ui.fileLoaded = true;
                System.out.println("Loading from XML completed.");

            }
        },
        DISPLAY_INFO(2, "Display info about the time table and engine.") {
            @Override
            public void start(UI ui) {
                if (ui.fileLoaded)
                    System.out.println(ui.descriptor);
                else
                    System.out.println("No file loaded, please load an XML file first (1).");
            }
        },
        RUN_ENGINE(3, "Run evolutionary algorithm.") {
            @Override
            public void start(UI ui) {
                Scanner scanner = new Scanner(System.in);
                if (ui.fileLoaded) {
                    if (ui.descriptor.getEngine().isEngineStarted()) {// Engine is started:
                        System.out.println("Engine already initialized, do you wish to overwrite previous run? (Y/N)");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            runEngineHelper(ui);
                        }
                    } else {// Engine is not started:
                        runEngineHelper(ui);
                    }
                } else {
                    System.out.println("No file loaded, please load an XML file first (1).");
                }
            }

            private void runEngineHelper(UI ui) {
                Scanner scanner = new Scanner(System.in);
                int frequency;
                int number_of_generations;
                // Recieve number of generations from user:
                System.out.println("Enter requested amount of generations (at least 100): ");
                do {
                    number_of_generations = scanner.nextInt();
                    if (number_of_generations < 100) {
                        System.out.println("Number of generations needs to be at least 100.");
                    }
                } while (number_of_generations < 100);

                ui.descriptor.
                        getEngine().
                        initSolutionPopulation(
                                ui.descriptor.getTimeTable(), number_of_generations
                        );
                System.out.println("Initial population initialized.");
                System.out.println("In which frequency of generations do you wish to view the progress? (1 - " + number_of_generations + ")");
                frequency = scanner.nextInt();
                ui.descriptor.getEngine().runEvolution(frequency);
            }
        },
        SHOW_BEST_SOLUTION(4, "Display best solution.") {
            @Override
            public void start(UI ui) {
                int choice;
                Scanner scanner = new Scanner(System.in);
                Exception exception = new Exception();
                // Show TimeTableSolution's display options:
                for (TimeTableSolution.PresentationOptions option : TimeTableSolution.PresentationOptions.values()) {
                    System.out.println(option.toString());
                }
                // While is used to handle exceptions (e.g. outOfBound)
                while (exception != null) {
                    // Recieve display choice from user:
                    choice = scanner.nextInt();
                    // Print according to display choice:
                    try {
                        System.out.println(ui.descriptor.getEngine().getBestSolutionDisplay(choice));
                        exception = null;
                    } catch (Exception e) {
                        exception = e;
                    }
                }
            }
        },
        VIEW_PROGRESS(5, "View progress.") {
            @Override
            public void start(UI ui) {
                //TODO add function
            }
        },
        EXIT(6, "Exit.") {
            @Override
            public void start(UI ui) {
                ui.setExit(true);
            }
        };

        int number;
        String action;

        MenuOptions(int number, String action) {
            this.number = number;
            this.action = action;
        }

        abstract public void start(UI ui);

        @Override
        public String toString() {
            return number + " - " + action;
        }

    }

    // Private constructor for singleton:
    private UI() {
        this.exit = false;
        this.fileLoaded = false;
        this.descriptor = null;
    }

    // Static method getinstance for singleton:
    public static UI getInstance() {
        return instance;
    }

    public void runMenu() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        //TODO: change at the end
        MenuOptions.values()[0].start(this);
        MenuOptions.values()[2].start(this);

        while (!this.exit) {
            for (MenuOptions option : MenuOptions.values()) {
                System.out.println(option.toString());
            }
            choice = scanner.nextInt();
            MenuOptions.values()[choice - 1].start(this);
        }
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }

    public void setFileLoaded(boolean fileLoaded) {
        this.fileLoaded = fileLoaded;
    }


}
