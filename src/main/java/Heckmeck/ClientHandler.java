package Heckmeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    public final int playerId;
    private String message = "";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket, int playerId) {
        this.clientSocket = clientSocket;
    this.playerId = playerId;

    }


    public void run(){
        System.out.println("Connection in client handler ok, this is client thread #" + playerId);
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){
                this.message =  in.readLine();
                if(this.message.equals("hello server")){
                    writeMessage("hello client " + playerId);
                    System.out.println("Server writing message: " + "hello client " + playerId);
                }
                else{
                    writeMessage("OK");
                }
            }
            /*
            while(true){

                this.message =  in.readLine();
                //message = readReceivedMessage();



                if(this.message.equals("Player12")){
                    writeMessage("OKK");
                }
                if(){
                    writeMessage("OKK");
                }

            }  */



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public String readReceivedMessage(){
        return this.message;
    }

    public String ReadIncomingMessage(){
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeMessage(String message){

        out.println(message);
        out.flush();

    }



}