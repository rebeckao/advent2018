package sleigh;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "label")
class Step {
    private String label;
    private Set<String> dependsOn = new HashSet<>();

    Step(String label) {
        this.label = label;
    }

    void addDependency(String prerequisiteStep) {
        dependsOn.add(prerequisiteStep);
    }

}
