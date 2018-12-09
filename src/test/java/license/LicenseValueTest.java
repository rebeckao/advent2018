package license;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LicenseValueTest {
    private LicenseValue licenseValue = new LicenseValue();

    @Test
    void licenseValue() {
        int actual = licenseValue.resolveLicenseValue("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2");
        assertEquals(66, actual);
    }
}