import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
//we have the calculator server class here 
public class CalculatorServer {
    public CalculatorServer() {}//no specific initialization needed here

    public static void main(String[] args) {//sets up the RMI server that we need to follow
        try {
            // the actual logic of the calculator methods will be defined for the processsing
            CalculatorImplementation obj = new CalculatorImplementation();
            //objectavailable when there is an RMI call (0) available port for the process
            Calculator stub = (Calculator) UnicastRemoteObject.exportObject(obj, 0);
            // usually it run on the specified port now that allow the client to take up the remote objects by the name
            Registry registry = LocateRegistry.getRegistry();
            // Binding the remote object (stub) in the registry,any client with ids can look up to the name in reference
            registry.bind("CalculatorServer", stub);
            //if the server is ready it will indicate that it is ready to accept the call from the clients
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
