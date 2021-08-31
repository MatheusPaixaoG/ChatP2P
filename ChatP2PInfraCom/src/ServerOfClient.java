import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ServerOfClient extends Thread {
    private int port;
    private DataInputStream entrada;
    public String resposta;
    public Interface interface0;

    public ServerOfClient (int port, Interface interface0) {
        this.interface0 = interface0;
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor do cliente aberto. Esperando contato");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado. Podem começar a conversar");
            interface0.serverOfClientThread = this;
            while (true) {
                entrada = new DataInputStream(clientSocket.getInputStream());
                resposta = entrada.readUTF();
                interface0.textArea.append(resposta + "\n");
            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Deu erro: " + e);
        }
    }
}