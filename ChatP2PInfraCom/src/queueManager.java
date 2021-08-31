import groovy.console.ui.SystemOutputInterceptor;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

public class queueManager extends Thread {
    public Queue<String> textAreaQueue;
    public String textToDisplay;
    public boolean canStart;
    public DataOutputStream saida;
    public int numberOfMessage;
    public String nome;

    public queueManager (Queue<String> textAreaQueue, String textToDisplay, boolean canStart, String nome) {
        this.textToDisplay = textToDisplay;
        this.textAreaQueue = textAreaQueue;
        this.canStart = canStart;
        this.nome = nome;
    }

    @Override
    public void run() { // A thread apenas conta 10 segundos antes de colocar o texto na textArea
        String descarte;
        textToDisplay = textAreaQueue.element();
        try {
            Thread.sleep(10000);
            if (isNullEmpty(textToDisplay)) { // Se não for vazia nem em branco, aí adiciona na tela
                try {
                    saida.writeUTF("Mensagem " + numberOfMessage + "\n" + nome + ":\n" + textToDisplay);
                } catch (IOException e) {
                    System.out.println("IOException");
                }
            }
            descarte = textAreaQueue.poll(); // Adicionando ou não na tela, preciso remover da fila
        } catch (InterruptedException e) {
            if (isNullEmpty(textToDisplay)) { // Só adiciona se a string não for vazia nem em branco
                if (canStart) {
                    try {
                        saida.writeUTF("Mensagem " + numberOfMessage + "\n" + nome + ":\n" + textToDisplay);
                    } catch (IOException ex) {
                        System.out.println("IOException");
                    }
                }
            }
        }
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
}
