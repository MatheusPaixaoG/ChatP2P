import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        int port = 28877;
        int port2 = 28876; 
        Socket socket1;
        Socket socket2;
        DataInputStream canalRecebimentoPorta;
        DataOutputStream canalEnvioPorta;
        int client1Port;
        int client2Port;
        ServerSocket serverSocket;
        ServerSocket serverSocket2;
        
        try {
            serverSocket = new ServerSocket(port);
            serverSocket2 = new ServerSocket(port2);
            while (true) {
                socket1 = serverSocket.accept();
                System.out.println("usuário 1 conectado");
                socket2 = serverSocket2.accept();
                System.out.println("usuário 2 conectado");
                canalRecebimentoPorta = new DataInputStream(socket1.getInputStream());
                client1Port = Integer.parseInt(canalRecebimentoPorta.readUTF());
                canalRecebimentoPorta = new DataInputStream(socket2.getInputStream());
                client2Port = Integer.parseInt(canalRecebimentoPorta.readUTF());

                canalEnvioPorta = new DataOutputStream(socket1.getOutputStream());
                canalEnvioPorta.writeUTF(String.valueOf(client2Port));
                canalEnvioPorta = new DataOutputStream(socket2.getOutputStream());
                canalEnvioPorta.writeUTF(String.valueOf(client1Port));

                socket1.close();
                socket2.close();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
