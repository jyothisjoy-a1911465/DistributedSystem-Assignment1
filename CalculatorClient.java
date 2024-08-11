import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Used to track the operations for the client
            String clientId = "Client123"; // Change the clientId for multiple clients

            // Here the client connects the RMI to the local machine
            Registry registry = LocateRegistry.getRegistry(null);
            Calculator stub = (Calculator) registry.lookup("CalculatorServer");

            // Test 1: Push values and perform min operation (1)
            System.out.println("Test case 1.\n");
            stub.pushValue(clientId, 20);
            stub.pushValue(clientId, 5);
            stub.pushValue(clientId, 15);
            System.out.println("Testing min operation");
            stub.pushOperation(clientId, "min");
            int minResult = stub.pop(clientId);
            System.out.println("Expected min result: 5, Actual: " + minResult);
            assert minResult == 5 : "Test Failed: Expected 5 but got " + minResult;
            System.out.println(); // Blank line after test case 1

            // Edge Case 1: Min operation with only one value (2)
            System.out.println("Test case 2.\n");
            stub.pushValue(clientId, 42);
            System.out.println("Testing min operation with one value");
            stub.pushOperation(clientId, "min");
            int singleMinResult = stub.pop(clientId);
            System.out.println("Expected min result: 42, Actual: " + singleMinResult);
            assert singleMinResult == 42 : "Test Failed: Expected 42 but got " + singleMinResult;
            System.out.println(); // Blank line after test case 2

            // Failure Case 1: Pop from an empty stack (3)
            System.out.println("Test case 3.\n");
            try {
                stub.pop(clientId);
                throw new AssertionError("Test Failed: Expected RemoteException when popping from an empty stack");
            } catch (Exception e) {
                System.out.println("Expected exception caught for popping from an empty stack: " + e);
            }
            System.out.println(); // Blank line after test case 3

            // Failure Case 2: Push an invalid operation (4)
            System.out.println("Test case 4.\n");
            try {
                stub.pushValue(clientId, 10);
                stub.pushOperation(clientId, "invalidOperation");
                throw new AssertionError("Test Failed: Expected RemoteException for invalid operation");
            } catch (Exception e) {
                System.out.println("Expected exception caught for invalid operation: " + e);
            }
            System.out.println(); // Blank line after test case 4

            // Edge Case 2: LCM with a single value (5)
            System.out.println("Test case 5.\n");
            stub.pushValue(clientId, 13);
            System.out.println("Testing LCM operation with one value");
            stub.pushOperation(clientId, "lcm");
            int singleLcmResult = stub.pop(clientId);
            System.out.println("Expected LCM result: 13, Actual: " + singleLcmResult);
            assert singleLcmResult == 13 : "Test Failed: Expected 13 but got " + singleLcmResult;
            System.out.println(); // Blank line after test case 5

            // Edge Case 3: GCD with identical values (6)
            System.out.println("Test case 6.\n");
            stub.pushValue(clientId, 9);
            stub.pushValue(clientId, 9);
            stub.pushValue(clientId, 9);
            System.out.println("Testing GCD operation with identical values");
            stub.pushOperation(clientId, "gcd");
            int gcdIdenticalResult = stub.pop(clientId);
            System.out.println("Expected GCD result: 9, Actual: " + gcdIdenticalResult);
            assert gcdIdenticalResult == 9 : "Test Failed: Expected 9 but got " + gcdIdenticalResult;
            System.out.println(); // Blank line after test case 6

            // Test case to check the delayPop (7)
            System.out.println("Test case 7.\n");
            stub.pushValue(clientId, 10);
            System.out.println("Testing delayPop with 2 seconds delay");
            long startTime = System.currentTimeMillis();
            int delayPopResult = stub.delayPop(clientId, 2000);
            long elapsedTime = System.currentTimeMillis() - startTime;
            assert delayPopResult == 10 : "Test Failed: Expected 10 but got " + delayPopResult;
            assert elapsedTime >= 2000 : "delayPop did not wait long enough";
            System.out.println(); // Blank line after test case 7

            // Automated Test: Check if stack is empty after all operations (8)
            System.out.println("Test case 8.\n");
            boolean isEmpty = stub.isEmpty(clientId);
            System.out.println("Is stack empty? " + isEmpty);
            assert isEmpty : "Test Failed: Stack should be empty after all operations";
            System.out.println("All tests completed successfully.");
            System.out.println(); // Blank line after test case 8
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
