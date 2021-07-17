# Evolution_Time_Table_Project

* UI:  
    * Classes:  
        * Menu (ENUM?):
            * Enums:
                * Read Xml
                * Display time table
                * Run evolutionary algorithm (engine)
                * Show best solution
                * View engine progression
                * Exit
            * Abstract Method: Verify input
* Engine:  
    * Interface:  
        * Evolutionable:
            * Methods:
                * Crossover
                * Mutation
                * Selection - Truncation
    * Classes:  
        * Solution


* Time_Table: (implements Evolutionable)
    * Classes:
        * Main class
        * Subject - ID,name
        * Teacher - ID, name, subject_id_list
        * Grade - ID, name, map(subject_id,weekly_hours)
        * XML Handler:
          * Methods:
            * File Exists
            * ספרור רץ?
            * No Duplicate Rules
            * Teacher teaching real subject
            * Grades subject list is real subject
            * Grade total weekly hours <= H*D
    * Enum:
        * Rules - boolean(soft/hard)
            * TeacherIsHuman - hard
            * Singularity - hard
            * Knowledgable - hard
        * Abstract Method: Test rule
    * Fields:
        * int days/week
        * int hours/day
        * Subject[] subjects
        * Teacher[] teachers
        * Grade[] grades