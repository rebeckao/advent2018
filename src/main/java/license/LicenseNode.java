package license;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class LicenseNode {
    @Setter
    private List<Integer> metadata;
    private List<LicenseNode> children = new ArrayList<>();

    int resolveMetadataSum() {
        return sumOfMetadata() +
                children.stream()
                        .mapToInt(LicenseNode::resolveMetadataSum)
                        .sum();
    }

    private int sumOfMetadata() {
        return metadata.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    void addChild(LicenseNode child) {
        children.add(child);
    }

    int resolveValue() {
        if (children.isEmpty()) {
            return sumOfMetadata();
        } else {
            return metadata.stream()
                    .map(i -> i - 1)
                    .filter(i -> i < children.size())
                    .map(i -> children.get(i))
                    .mapToInt(LicenseNode::resolveValue)
                    .sum();
        }
    }
}
