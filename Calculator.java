import java.rmi.Remote;
import java.rmi.RemoteException;
//Calculator will be an interface that extends the remote interface (invoked remotely)
public interface Calculator extends Remote {
    void pushValue(String clientId, int val) throws RemoteException;//pushes the value into the stack assosiated with the clientid that we are provided
    void pushOperation(String clientId, String operator) throws RemoteException;//takes both the string client id and also the operators
    int pop(String clientId) throws RemoteException;//if we push the elements into the stack at first then this will help to pop the element out of the stack
    boolean isEmpty(String clientId) throws RemoteException;//checks whether the stack is empty or not
    int delayPop(String clientId, int millis) throws RemoteException;//check after how much time the operation is permitted 
}
