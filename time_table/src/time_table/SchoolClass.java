package time_table;

import Generated.ETTClass;

public class SchoolClass implements SchoolClassOrTeacher{
    String name;
    Requirements requirements;
    int id;

    SchoolClass(ETTClass gen){
        this.name = gen.getETTName();
        this.requirements =  new Requirements(gen.getETTRequirements());
        this.id = gen.getId();
    }

    public String getName() {
        return name;
    }

    public Requirements getRequirements() {
        return requirements;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   Class name: " + name + lineSeparator +
                "   Id: " + id + lineSeparator +
                "   Requirements: " + lineSeparator + requirements.toString();
    }
}
