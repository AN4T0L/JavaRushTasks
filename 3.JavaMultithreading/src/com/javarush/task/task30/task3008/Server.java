package com.javarush.task.task30.task3008;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message){
        for (Map.Entry<String, Connection> map :
                connectionMap.entrySet()) {
            try{
                map.getValue().send(message);}
            catch (IOException e){
                ConsoleHelper.writeMessage("Message not send to " + map.getKey());
            }
        }
    }
    public static void main(String[] args) throws IOException {

        int port = ConsoleHelper.readInt();
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ConsoleHelper.writeMessage("Server started");
            for (; ; ) {
                Socket s = ss.accept();
                Handler handler = new Handler(s);
                handler.start();
            }
        }catch (IOException e){
            ConsoleHelper.writeMessage("io error");
            ss.close();
        }

    }
    private static class Handler extends Thread{
        private Socket socket;
        Handler(Socket socket){
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{
            String name = "";
            while(true){
            connection.send(new Message(MessageType.NAME_REQUEST));
            Message msgName = connection.receive();
                if(msgName.getType()==MessageType.USER_NAME  && !msgName.getData().isEmpty() ){
                    name = msgName.getData();
                    if(!connectionMap.containsKey(name)) {
                        connectionMap.put(name,connection);
                        connection.send(new Message(MessageType.NAME_ACCEPTED));
//                        connection.close();
                        break;
                    }
                }

            }
            return name;
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException{
            for (Map.Entry<String, Connection> map :
                    connectionMap.entrySet()) {
                if(!map.getKey().equals(userName)){
                    Message message = new Message(MessageType.USER_ADDED,map.getKey());
                    connection.send(message);
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException{
            for(;;){
                Message message = connection.receive();
                if(message.getType()==MessageType.TEXT) {
                   String text = userName + ": " + message.getData();
                   Message textMessage = new Message(MessageType.TEXT,text);
                  sendBroadcastMessage(textMessage);
                } else ConsoleHelper.writeMessage("Received message not text");
            }
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Connection opened with "+ socket.getRemoteSocketAddress());
            Connection connection = null;
            String name = "";
            try{
                connection = new Connection(socket);
                name = serverHandshake(connection);
                Message messageUserAdded = new Message(MessageType.USER_ADDED,name);
                sendBroadcastMessage(messageUserAdded);
                sendListOfUsers(connection,name);
                serverMainLoop(connection,name);
            }
            catch (Exception e){
                ConsoleHelper.writeMessage("Server error");
            }

            connectionMap.remove(name);
            Message messageUserRemoved = new Message(MessageType.USER_REMOVED,name);
            sendBroadcastMessage(messageUserRemoved);
            ConsoleHelper.writeMessage("Connection closed for "+ socket.getRemoteSocketAddress());
        }
    }
}
