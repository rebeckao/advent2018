package license;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class LicenseMetadata {

    public static void main(String[] args) {
        try {
            String line = Files.readAllLines(Paths.get("./src/main/resources/day8_license_nodes.txt")).get(0);
            System.out.println(new LicenseMetadata().sumOfMetadata(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int sumOfMetadata(String license) {
        LicenseNode root = parseRoot(license);
        return root.resolveMetadataSum();
    }

    LicenseNode parseRoot(String license) {
        List<Integer> licenseInfo = Stream.of(license.split(" ")).map(Integer::parseInt).collect(toList());
        Integer numberOfChildNodes = licenseInfo.get(0);
        Integer numberOfMetadataEntries = licenseInfo.get(1);
        LicenseNode root = new LicenseNode();
        parseChildNodesAndMetadata(licenseInfo, numberOfChildNodes, numberOfMetadataEntries, root);
        return root;
    }

    private List<Integer> parseChildNodesAndMetadata(List<Integer> licenseInfo, Integer numberOfChildNodes, Integer numberOfMetadataEntries, LicenseNode node) {
        List<Integer> restOfLicenseInfo = licenseInfo.subList(2, licenseInfo.size());
        for (int i = 0; i < numberOfChildNodes; i++) {
            restOfLicenseInfo = parseNode(restOfLicenseInfo, node);
        }
        node.setMetadata(restOfLicenseInfo.subList(0, numberOfMetadataEntries));
        return restOfLicenseInfo.subList(numberOfMetadataEntries, restOfLicenseInfo.size());
    }

    private List<Integer> parseNode(List<Integer> licenseInfo, LicenseNode parent) {
        Integer numberOfChildNodes = licenseInfo.get(0);
        Integer numberOfMetadataEntries = licenseInfo.get(1);
        LicenseNode node = new LicenseNode();
        parent.addChild(node);
        return parseChildNodesAndMetadata(licenseInfo, numberOfChildNodes, numberOfMetadataEntries, node);
    }
}
