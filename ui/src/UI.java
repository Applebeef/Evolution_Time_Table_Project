import Generated.ETTDescriptor;
import solution.TimeTableSolution;
import descriptor.Descriptor;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UI {
    //Singleton instance:
    private static final UI instance = new UI();
    public boolean exit;
    public boolean fileLoaded;
    public Descriptor descriptor;
    private Thread engine_thread;

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
            public void start(UI ui) {
                Scanner scanner = new Scanner(System.in);
                String filename;
                boolean wantsToLoadFile = ui.isFileNotLoaded();
                if (ui.engine_thread != null && ui.engine_thread.isAlive()) {
                    System.out.println("Engine is already running, would you like to stop the current run and load a new file?");
                    wantsToLoadFile = ui.yesOrNo();
                    if (wantsToLoadFile) {
                        ui.engine_thread.interrupt();
                        try {
                            ui.engine_thread.join();
                        } catch (InterruptedException ignored) {

                        }
                        ui.getDescriptor().getEngine().reset();
                    } else {
                        return;
                    }
                }
                if (!wantsToLoadFile) {
                    System.out.println("A file is already loaded, do you wish to load a new one?");
                    if (ui.yesOrNo()) {
                        wantsToLoadFile = true;
                    }
                }

                if (wantsToLoadFile) {
                    System.out.println("Please enter the path to the XML file.");
                    filename = scanner.nextLine();

                    try {
                        File file = new File(filename);
                        String xml = ".xml";
                        if (filename.length() < xml.length() || !filename.substring(filename.length() - xml.length()).equalsIgnoreCase(xml)) {
                            System.out.println("The file isn't an xml file, please enter a valid xml file.");
                            return;
                        } else if (!file.exists()) {
                            System.out.println("The file specified doesnt exist.");
                            return;
                        }

                        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        ETTDescriptor ettdescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
                        Descriptor descriptor = new Descriptor(ettdescriptor);
                        Set<String> errorSet = descriptor.checkValidity();
                        if (errorSet.size() == 1 && errorSet.contains("")) {
                            ui.setDescriptor(descriptor);
                            ui.fileLoaded = true;
                            System.out.println("Loading from XML completed.");
                        } else {
                            System.out.println("Errors found: ");
                            for (String error : errorSet) {
                                System.out.println(error);
                            }
                        }

                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                }
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
                if (ui.engine_thread == null) {
                    if (!ui.fileLoaded) {
                        System.out.println("No file loaded, please load an XML file first (1).");
                    } else {
                        runEngineHelper(ui);
                    }
                } else if (ui.engine_thread.isAlive()) {
                    System.out.println("Engine is already running, would you like to start over?");
                    if (ui.yesOrNo()) {
                        ui.engine_thread.interrupt();
                        try {
                            ui.engine_thread.join();
                        } catch (InterruptedException ignored) {

                        }
                        ui.getDescriptor().getEngine().reset();
                        runEngineHelper(ui);
                    }
                } else if (ui.fileLoaded) {
                    if (ui.descriptor.getEngine().isEngineStarted()) {// Engine is started:
                        System.out.println("Engine already initialized, do you wish to overwrite previous run?");
                        if (ui.yesOrNo()) {
                            ui.getDescriptor().getEngine().reset();
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
                int frequency;
                int number_of_generations = 0;
                int max_fitness = 101;//if user doesnt choose a max fitness, we send 101, which shouldn't be a possible fitness score.
                // Receive number of generations from user:
                System.out.println("Enter requested amount of generations (at least 100): ");
                while (number_of_generations < 100) {
                    number_of_generations = ui.numberInput();
                    if (number_of_generations < 100) {
                        System.out.println("Number of generations needs to be at least 100.");
                    }
                }
                System.out.println("Would you like to choose a maximum fitness score?");
                if (ui.yesOrNo()) {
                    System.out.println("Please enter a fitness score (0-100): ");
                    while (max_fitness > 100 || max_fitness < 0) {
                        max_fitness = ui.numberInput();

                        if (max_fitness > 100 || max_fitness < 0) {
                            System.out.println("Please choose a number between 0-100.");
                        }
                    }
                }

                ui.descriptor.getEngine().initSolutionPopulation(ui.descriptor.getTimeTable(), number_of_generations);
                System.out.println("Initial population initialized.");
                System.out.println("In which frequency of generations do you wish to view the progress? (1 - " + number_of_generations + ")");
                frequency = -1;
                while (frequency < 1 || frequency > number_of_generations) {
                    frequency = ui.numberInput();
                    if (frequency < 1 || frequency > number_of_generations) {
                        System.out.println("Please choose a number between 1 and " + number_of_generations + ".");
                    }
                }
                ui.descriptor.getEngine().initThreadParameters(frequency, max_fitness, System.out::println);
                ui.createNewEngineThread();
                ui.engine_thread.start();
            }
        },
        SHOW_BEST_SOLUTION(4, "Display best solution.") {
            @Override
            public void start(UI ui) {
                int choice = 0;

                // Show TimeTableSolution's display options:
                if (ui.isFileNotLoaded()) {
                    System.out.println("No file loaded, please load an XML file first (1).");
                } else if (!ui.descriptor.getEngine().isEngineStarted()) {
                    System.out.println("Evolution hasn't been started yet, please start the evolutionary algorithm first (3)");
                } else {
                    for (TimeTableSolution.PresentationOptions option : TimeTableSolution.PresentationOptions.values()) {
                        System.out.println(option.toString());
                    }
                    // While is used to handle exceptions (e.g. outOfBound)
                    while (choice < 1 || choice > TimeTableSolution.PresentationOptions.values().length) {
                        // Receive display choice from user:
                        choice = ui.numberInput();
                        if (choice < 1 || choice > TimeTableSolution.PresentationOptions.values().length) {
                            System.out.println("Please choose a number between 1 and " + TimeTableSolution.PresentationOptions.values().length + ".");
                        } else {
                            // Print according to display choice:
                            System.out.println(ui.descriptor.getEngine().getBestSolutionDisplay(choice));
                        }
                    }
                }

            }
        },
        VIEW_PROGRESS(5, "View progress.") {
            @Override
            public void start(UI ui) {
                if (ui.isFileNotLoaded()) {
                    System.out.println("No file loaded, please load an XML file first (1).");
                } else if (!ui.getDescriptor().getEngine().isEngineStarted()) {
                    System.out.println("Evolution hasn't been started yet, please start the evolutionary algorithm first (3)");
                } else {

                    if (!ui.engine_thread.isAlive()) {
                        List<Pair<Integer, Solution>> bestSolutions = ui.descriptor.getEngine().getBestSolutionsPerFrequency();
                        printBestSolutions(bestSolutions);
                    } else {
                        synchronized (ui.descriptor.getEngine().getBestSolutionsPerFrequency()) {
                            List<Pair<Integer, Solution>> pairList = ui.descriptor.getEngine().getBestSolutionsPerFrequency();
                            if (ui.descriptor.getEngine().getBestSolutionsPerFrequency().size() > 10) {
                                pairList = pairList.subList(pairList.size() - 10, pairList.size());
                            }
                            printBestSolutions(pairList);
                        }
                    }
                }
            }

            private void printBestSolutions(List<Pair<Integer, Solution>> bestSolutions) {
                Double prevFitness = 0.0;
                Double currentFitness;
                for (Pair<Integer, Solution> pair : bestSolutions) {
                    currentFitness = pair.getV2().getFitness();
                    System.out.println("Generation no. " + pair.getV1() + " Fitness: " + String.format("%.1f", currentFitness) + ".");
                    if (pair.getV1() != 1) {
                        System.out.println("The difference in fitness from the last generation is: " + String.format("%.1f", currentFitness - prevFitness));
                    }
                    System.out.println();
                    prevFitness = currentFitness;
                }
            }
        },
        EXIT(6, "Exit.") {
            @Override
            public void start(UI ui) {
                if (ui.engine_thread != null && ui.engine_thread.isAlive()) {
                    ui.engine_thread.interrupt();
                }
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

    private int numberInput() {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        boolean correctInput = false;
        do {
            try {
                input = scanner.nextInt();
                correctInput = true;
            } catch (InputMismatchException ignored) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
            }
        } while (!correctInput);
        return input;
    }

    // Private constructor for singleton:
    private UI() {
        this.exit = false;
        this.fileLoaded = false;
        this.descriptor = null;
    }

    // Static method getInstance for singleton:
    public static UI getInstance() {
        return instance;
    }

    public void runMenu() {
        int choice;

        while (!this.exit) {
            for (MenuOptions option : MenuOptions.values()) {
                System.out.println(option.toString());
            }
            choice = numberInput();
            MenuOptions.values()[choice - 1].start(this);
        }
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

    public boolean isFileNotLoaded() {
        return !fileLoaded;
    }

    private void createNewEngineThread() {
        engine_thread = new Thread(descriptor.getEngine());
        engine_thread.setName("Engine");
    }

    private boolean yesOrNo() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        do {
            System.out.println("Please choose Y/N: ");
            answer = scanner.nextLine();
        } while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N"));
        return answer.equalsIgnoreCase("Y");
    }

}
