package Converted;

import Generated.ETTClass;
import Generated.ETTClasses;
import java.util.List;
import java.util.stream.Collectors;

public class SchoolClasses {
    List<SchoolClass> classList;

    SchoolClasses(ETTClasses gen){
        classList = gen.getETTClass().stream().map(SchoolClass::new).collect(Collectors.toList());
    }

    public List<SchoolClass> getClassList() {
        return classList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder allClasses = new StringBuilder();
        for(SchoolClass schoolClass : classList){
            allClasses.append(schoolClass.toString()).append(lineSeparator);
        }
        return allClasses.toString();
    }
}
