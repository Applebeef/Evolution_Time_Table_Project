package Converted;

import Generated.ETTClass;

public class SchoolClass {
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Class name: " + name + System.getProperty("line.separator") + "ID: " + id + System.getProperty("line.separator") + requirements.toString();
    }
}
