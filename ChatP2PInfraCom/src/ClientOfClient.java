import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class ClientOfClient extends Thread {
    private final int port;
    private final String address;
    public DataOutputStream saida;
    private final Interface interface0;
    public String msg = "";

    public ClientOfClient (int port, String address, Interface interface0) {
        this.port = port;
        this.address = address;
        this.interface0 = interface0;
    }

    @Override
    public synchronized void run() {
        Socket socketWithServer;
        //int numberOfMessage = 1;
        try {
            socketWithServer = new Socket(address, port);
            while (!msg.equals("fim")) {
                try {
                    interface0.clientOfClientThread = this;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg = interface0.textField.getText();
                    interface0.textField.setText("");
                    saida = new DataOutputStream(socketWithServer.getOutputStream());
                    interface0.writeContent(msg, interface0.textAreaQueue, true, saida);
                } catch (Exception e) {
                    //TODO: handle exception
                    socketWithServer.close();
                    System.out.println("Conexão encerrada pelo erro: " + e);
                }
            }
            socketWithServer.close();
            System.out.println("Conexão encerrada normalmente");
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Erro: " + e);
        }
    }

    public synchronized void wakeUpThread() {
        notify();
    }

}