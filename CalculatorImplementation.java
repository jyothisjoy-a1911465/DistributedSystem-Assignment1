import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
//the server side implementation for the calculator interface
public class CalculatorImplementation implements Calculator {
    private Map<String, Stack<Integer>> clientStacks;
//store the stack of integers for each client, integer that is being pushed by the client
    public CalculatorImplementation() throws RemoteException {
        clientStacks = new HashMap<>();
    }
//this constructor initializes the map and throw an exception
    private Stack<Integer> getClientStack(String clientId) {
        return clientStacks.computeIfAbsent(clientId, k -> new Stack<>());
    }
//retrives the stack assosiated with the given clientid if client doesnt have a stack new one is created and added to the map
    @Override// push value operations
    public synchronized void pushValue(String clientId, int val) throws RemoteException {
        getClientStack(clientId).push(val);
    }
//operations related to the values pushed
@Override
public synchronized void pushOperation(String clientId, String operator) throws RemoteException {
    Stack<Integer> stack = getClientStack(clientId);
    if (stack.isEmpty()) {
        throw new RemoteException("Stack is empty");
    }

    List<Integer> values = new ArrayList<>();
    while (!stack.isEmpty()) {
        values.add(stack.pop());
    }

    switch (operator.toLowerCase()) {
        case "min":
            stack.push(values.stream().min(Integer::compare).orElse(0));
            break;
        case "max":
            stack.push(values.stream().max(Integer::compare).orElse(0));
            break;
        case "lcm":
            stack.push(lcm(values));
            break;
        case "gcd":
            stack.push(gcd(values));
            break;
        default:
            throw new RemoteException("Invalid operator");
    }
}

//to pop an element out of the stack
    @Override
    public synchronized int pop(String clientId) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty");
        }
        return stack.pop();
    }
// to check whether the stack is empty and then return that
    @Override
    public synchronized boolean isEmpty(String clientId) throws RemoteException {
        return getClientStack(clientId).isEmpty();
    }
//so here we are looking to delay the operations for milliseconds before the operations
    @Override
    public synchronized int delayPop(String clientId, int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pop(clientId);
    }
// the formula for lcm,gcd and all through the RMI 
    private int lcm(List<Integer> values) {
        return values.stream().reduce(1, (a, b) -> (a * b) / gcd(a, b));
    }
    private int gcd(List<Integer> values) {
        return values.stream().reduce(values.get(0), this::gcd);
    }
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
