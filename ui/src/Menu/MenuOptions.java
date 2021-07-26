package Menu;

import UI.*;

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
            //TODO add function
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
            UI.exit = true;
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
