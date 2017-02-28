package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;
import java.net.Socket;


public class Client {
    protected Connection connection;
    private  volatile boolean clientConnected = false;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    protected String getServerAddress(){
        return ConsoleHelper.readString();
    }

    protected int getServerPort(){
        return ConsoleHelper.readInt();
    }

    protected String getUserName(){
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole(){
        return true;
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try{
            connection.send(new Message(MessageType.TEXT,text));
        }
        catch (IOException e){
            ConsoleHelper.writeMessage("Wait error");
            clientConnected =false;
        }
    }
    public void run(){
        SocketThread thread = getSocketThread();
        thread.setDaemon(true);
        thread.start();
        try{
            synchronized (this){
                this.wait();
            }
        }
        catch (InterruptedException e){
            ConsoleHelper.writeMessage("Thread error");
            return;
        }
        if(clientConnected)ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        while(clientConnected){
            String s = ConsoleHelper.readString();
            if(s.equalsIgnoreCase("exit"))break;
            else if(shouldSendTextFromConsole()){
                sendTextMessage(s);
            }
        }
    }

    public class SocketThread extends Thread{
        @Override
        public void run() {
            String serverAddr = getServerAddress();
            int serverPort = getServerPort();
            try{
                Socket socket = new Socket(serverAddr,serverPort);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            }catch (IOException|ClassNotFoundException e){
                notifyConnectionStatusChanged(false);
            }
        }

        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }
        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " join to chat");
        }
        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " exit from chat");

        }
        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this){
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException{
            while(true){
                Message message = Client.this.connection.receive();
                if(message.getType()==MessageType.NAME_REQUEST){
                    String clientName = getUserName();
                    Message msg = new Message(MessageType.USER_NAME,clientName);
                    Client.this.connection.send(msg);
                }
                else if(message.getType()==MessageType.NAME_ACCEPTED){
                    notifyConnectionStatusChanged(true);
                    return;
                }
                else throw  new IOException("Unexpected MessageType");
            }
        }
        protected void clientMainLoop() throws IOException, ClassNotFoundException{
            while(!this.isInterrupted()){
                Message message;
                message = Client.this.connection.receive();
                if(message.getType()==MessageType.TEXT)processIncomingMessage(message.getData());
                else if(message.getType()==MessageType.USER_ADDED) informAboutAddingNewUser(message.getData());
                else if(message.getType()==MessageType.USER_REMOVED) informAboutDeletingNewUser(message.getData());
                else throw new IOException("Unexpected MessageType");
            }
        }
    }
}
