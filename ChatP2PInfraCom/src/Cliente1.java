import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente1 {
    public static void main(String[] args) {
        int port = 28886;
        int serverPort = 28877;
        String address = "localhost";
        Socket socket;
        DataOutputStream saida;
        DataInputStream entradaServer;
        int otherClientPort;

        try {
            socket = new Socket(address, serverPort);
            try {
                // Manda para o servidor o número de porta desse cliente
                saida = new DataOutputStream(socket.getOutputStream());
                saida.writeUTF(String.valueOf(port));
                // Recebe do servidor o número de porta do outro cliente
                entradaServer = new DataInputStream(socket.getInputStream());
                otherClientPort = Integer.parseInt(entradaServer.readUTF());
                // Agora eu já tenho as duas portas, já posso começar as threads
                Interface interface1 = new Interface("Interface cliente 1", "Cliente 1");
                Thread receiveChatMessages = new ServerOfClient(port, interface1);
                receiveChatMessages.start();
                Thread sendChatMessages = new ClientOfClient(otherClientPort, address, interface1);
                sendChatMessages.start();
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("Saída pelo erro: " + e);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }

    }
}
