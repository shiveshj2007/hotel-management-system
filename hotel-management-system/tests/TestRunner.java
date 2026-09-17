package tests;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   RUNNING HOTEL MANAGEMENT SYSTEM UNIT TESTS     ");
        System.out.println("==================================================");

        BusinessLogicTest testSuite = new BusinessLogicTest();
        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;

        testSuite.setUp();

        Runnable[] tests = new Runnable[]{
                testSuite::testValidationUtil,
                testSuite::testGuestCreationAndDuplicateCheck,
                testSuite::testRoomManagement,
                testSuite::testReservationDateOverlapAndDoubleBooking,
                testSuite::testCheckInCheckOutStateTransitions,
                testSuite::testBillAndTaxCalculations,
                testSuite::testReportMetrics
        };

        for (Runnable test : tests) {
            totalTests++;
            try {
                test.run();
                passedTests++;
                System.out.println("    -> PASSED");
            } catch (AssertionError e) {
                failedTests++;
                System.err.println("    -> " + e.getMessage());
            } catch (Exception e) {
                failedTests++;
                System.err.println("    -> UNEXPECTED EXCEPTION: " + e.getMessage());
            }
        }

        System.out.println("==================================================");
        System.out.println("TEST SUMMARY: Total: " + totalTests + " | Passed: " + passedTests + " | Failed: " + failedTests);
        System.out.println("==================================================");

        if (failedTests > 0) {
            System.exit(1);
        }
    }
}
