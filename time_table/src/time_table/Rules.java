package time_table;

import Generated.ETTRule;
import Generated.ETTRules;
import evolution.rules.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rules {
    List<Rule> ruleList;
    int hardRulesWeight;

    Rules(ETTRules gen) {
        ruleList = new ArrayList<>();
        hardRulesWeight = gen.getHardRulesWeight();

        for (ETTRule r : gen.getETTRule()) {
            for (Rule rule : Rule.values()) {
                if (r.getETTRuleId().equals(rule.ruleId)) {
                    rule.setConfiguration(r.getETTConfiguration());
                    if (r.getType().equals("Hard")) {
                        rule.setType(Type.HARD);
                    } else if (r.getType().equals("Soft")) {
                        rule.setType((Type.SOFT));
                    }
                    ruleList.add(rule);
                }
            }
        }
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for (Rule rule : ruleList) {
            result.append("    ").append(rule.toString()).append(lineSeparator);
        }
        return lineSeparator + result + "The weight of Hard rules is: " + hardRulesWeight;

    }

    public String checkValidity() {
        Set<Rule> ruleSet = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (Rule rule : ruleList) {
            if (!ruleSet.add(rule)) {
                duplicates.add(rule.ruleId);
            }
        }
        if (duplicates.isEmpty()) {
            return "";
        } else {
            StringBuilder res = new StringBuilder("There are duplicate rules in the file: ");
            for (String id : duplicates) {
                res.append(id).append(", ");
            }
            res.append(".");
            return res.toString();
        }
    }
}
