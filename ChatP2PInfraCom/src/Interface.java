import groovy.console.ui.SystemOutputInterceptor;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class Interface implements ActionListener {
    public JPanel panel1;
    public JTextField textField;
    public JButton sendButton;
    public JButton clearButton;
    public JTextArea textArea;
    public JButton undoButton;
    public ClientOfClient clientOfClientThread;
    public ServerOfClient serverOfClientThread;
    private JLabel infracomChat;
    public Queue<String> textAreaQueue = new LinkedList<String>();
    private String nome;
    public queueManager gerenteFila = new queueManager(textAreaQueue, textField.getText(), true, "a");
    private int numberOfMessage = 1;


    public Interface(String nameOfInterface, String nome) {
        this.nome = nome;
        this.infracomChat.setText(nameOfInterface);
        JFrame janela = new JFrame();
        janela.setContentPane(panel1);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(540, 540);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    clientOfClientThread.wakeUpThread();
                }
            }
        });


        sendButton.addActionListener(this);
        sendButton.setActionCommand("sendButton_command");

        clearButton.addActionListener(this);
        clearButton.setActionCommand("clearButton_command");

        undoButton.addActionListener(this);
        undoButton.setActionCommand("undoButton_command");
    }

    public static boolean isNullEmpty(String str) { // Testa se a string é válida para ser enviada
        if (str == null) {
            return false;
        }
        else if (str.trim().isEmpty()) {
            return false;
        }
        else { // Só será enviada se não for vazia nem em branco
            return true;
        }
    }

    public void writeContent(String textFieldText, Queue<String> textAreaQueue, boolean sendOrUndo, DataOutputStream saida) {
        if (isNullEmpty(textFieldText)) { // Só entra se o que tiver sido escrito não for vazio nem em branco
            gerenteFila.saida = saida;
            String queueString = textAreaQueue.poll(); // Removo o que estiver na fila
            String textFieldString = textFieldText;
            textAreaQueue.add(textFieldString); // Adiciono o que acabou de ser escrito à fila
            try {
                if (queueString == null) { // Como não tem nada na fila, eu apenas inicio a contagem
                    gerenteFila.numberOfMessage = numberOfMessage;
                    gerenteFila.start();
                }
                else { // Tem coisa na fila, então eu tenho que interromper a execução da thread
                    if (sendOrUndo) {
                        try {
                            gerenteFila.interrupt();
                            gerenteFila.join();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        gerenteFila.start();
                    }
                    else {
                        try {
                            gerenteFila.canStart = false;
                            numberOfMessage--;
                            gerenteFila.interrupt();
                            gerenteFila.join();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            } catch (IllegalThreadStateException i) { // Crio uma nova thread caso eu tente iniciar uma thread já executada
                gerenteFila = new queueManager(textAreaQueue, textFieldText, true, nome);
                gerenteFila.saida = saida;
                numberOfMessage++;
                gerenteFila.numberOfMessage = numberOfMessage;
                gerenteFila.start();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("sendButton_command")) {
            clientOfClientThread.wakeUpThread();
        }
        else if (command.equals("clearButton_command")) {
            textArea.setText("");
        }
        else if (command.equals("undoButton_command")) {
            writeContent(clientOfClientThread.msg, textAreaQueue, false, clientOfClientThread.saida);
        }
    }
}
