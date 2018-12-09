package license;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class LicenseValue {
    private LicenseMetadata licenseMetadata = new LicenseMetadata();

    public static void main(String[] args) {
        try {
            String line = Files.readAllLines(Paths.get("./src/main/resources/day8_license_nodes.txt")).get(0);
            System.out.println(new LicenseValue().resolveLicenseValue(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int resolveLicenseValue(String license) {
        LicenseNode root = licenseMetadata.parseRoot(license);
        return root.resolveValue();
    }
}
