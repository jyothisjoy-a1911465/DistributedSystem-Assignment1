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
        
            System.out.println(); // Blank line after test case 8

            // Test 9: Perform multiple operations in sequence (9)
            System.out.println("Test case 9.\n");
            stub.pushValue(clientId, 10);
            stub.pushValue(clientId, 20);
            stub.pushOperation(clientId, "min");
            stub.pushValue(clientId, 5);
            stub.pushOperation(clientId, "max");
            int result = stub.pop(clientId);
            System.out.println("Expected result after sequence: 10, Actual: " + result);
            assert result == 10 : "Test Failed: Expected 10 but got " + result;
            System.out.println(); // Blank line after test case 9

            // Test 10: Perform max operation on empty stack (10)
            System.out.println("Test case 10.\n");
            try {
                stub.pushOperation(clientId, "max");
                throw new AssertionError("Test Failed: Expected RemoteException when performing max on empty stack");
            } catch (Exception e) {
                System.out.println("Expected exception caught for max operation on empty stack: " + e);
            }
            System.out.println(); // Blank line after test case 10

            // Test 11: Push negative values and perform min operation (11)
            System.out.println("Test case 11.\n");
            stub.pushValue(clientId, -5);
            stub.pushValue(clientId, -10);
            stub.pushValue(clientId, -3);
            System.out.println("Testing min operation with negative values");
            stub.pushOperation(clientId, "min");
            int minNegResult = stub.pop(clientId);
            System.out.println("Expected min result: -10, Actual: " + minNegResult);
            assert minNegResult == -10 : "Test Failed: Expected -10 but got " + minNegResult;
            System.out.println(); // Blank line after test case 11

            // Test 12: Push and pop repeatedly (12)
            System.out.println("Test case 12.\n");
            stub.pushValue(clientId, 7);
            stub.pushValue(clientId, 14);
            int popResult1 = stub.pop(clientId);
            stub.pushValue(clientId, 21);
            int popResult2 = stub.pop(clientId);
            int popResult3 = stub.pop(clientId);
            System.out.println("Expected pop results: 14, 21, 7 | Actual: " + popResult1 + ", " + popResult2 + ", " + popResult3);
            assert popResult1 == 14 && popResult2 == 21 && popResult3 == 7 : "Test Failed: Pop sequence did not match";
            System.out.println(); // Blank line after test case 12

            // Test 13: Perform GCD operation with mixed values (13)
            System.out.println("Test case 13.\n");
            stub.pushValue(clientId, 15);
            stub.pushValue(clientId, 25);
            stub.pushValue(clientId, 35);
            System.out.println("Testing GCD operation with mixed values");
            stub.pushOperation(clientId, "gcd");
            int gcdMixedResult = stub.pop(clientId);
            System.out.println("Expected GCD result: 5, Actual: " + gcdMixedResult);
            assert gcdMixedResult == 5 : "Test Failed: Expected 5 but got " + gcdMixedResult;
            System.out.println(); // Blank line after test case 13

            // Test 14: Perform LCM operation with larger values (14)
            System.out.println("Test case 14.\n");
            stub.pushValue(clientId, 12);
            stub.pushValue(clientId, 15);
            System.out.println("Testing LCM operation with larger values");
            stub.pushOperation(clientId, "lcm");
            int lcmResult = stub.pop(clientId);
            System.out.println("Expected LCM result: 60, Actual: " + lcmResult);
            assert lcmResult == 60 : "Test Failed: Expected 60 but got " + lcmResult;
            System.out.println(); // Blank line after test case 14

                    // Test 15: Test a complex sequence of operations (15)
            System.out.println("Test case 15.\n");
            stub.pushValue(clientId, 8);
            stub.pushValue(clientId, 16);
            stub.pushOperation(clientId, "gcd");
            stub.pushValue(clientId, 32);
            stub.pushOperation(clientId, "lcm");
            int complexResult = stub.pop(clientId);
            System.out.println("Expected result: 32, Actual: " + complexResult);
            assert complexResult == 32 : "Test Failed: Expected 32 but got " + complexResult;
            System.out.println(); // Blank line after test case 15

            // Test 16: Push multiple values and perform both min and max (16)
            System.out.println("Test case 16.\n");
            stub.pushValue(clientId, 100);
            stub.pushValue(clientId, 200);
            stub.pushValue(clientId, 50);
            System.out.println("Testing combined min and max operations");
            stub.pushOperation(clientId, "min");
            int minResult2 = stub.pop(clientId);
            System.out.println("Expected min result: 50, Actual: " + minResult2);
            assert minResult2 == 50 : "Test Failed: Expected 50 but got " + minResult2;

            stub.pushValue(clientId, 1000);
            stub.pushValue(clientId, 500);
            stub.pushOperation(clientId, "max");
            int maxResult = stub.pop(clientId);
            System.out.println("Expected max result: 1000, Actual: " + maxResult);
            assert maxResult == 1000 : "Test Failed: Expected 1000 but got " + maxResult;
            
            System.out.println(); // Blank line after test case 16
            

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

