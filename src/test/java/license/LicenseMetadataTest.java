package license;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LicenseMetadataTest {
    private LicenseMetadata licenseMetadata = new LicenseMetadata();

    @Test
    void sumOfMetadata() {
        int actual = licenseMetadata.sumOfMetadata("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2");
        assertEquals(138, actual);
    }
}