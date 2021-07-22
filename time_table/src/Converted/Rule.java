package Converted;
import Generated.ETTRule;
import Rules.*;

public enum Rule implements Testable {
    TEACHER_IS_HUMAN("TeacherIsHuman"){
        @Override
        public boolean Test() {
            return true; //TODO add test
        }
    },
    SINGULARITY("Singularity"){
        @Override
        public boolean Test() {
            return true;//TODO add test
        }
    },
    KNOWLEDGEABLE("Knowledgeable"){
        @Override
        public boolean Test() {
            return true;//TODO add test
        }
    },
    SATISFACTORY("Satisfactory"){
        @Override
        public boolean Test() {
            return true;//TODO add test
        }
    };

    String ruleId;
    String Configuration;
    Type type;

    Rule(String id){
        ruleId = id;
    }

    public String getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(String configuration) {
        Configuration = configuration;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
