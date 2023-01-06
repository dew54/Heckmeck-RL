package Heckmeck;

import exception.IllegalInput;

public class TCPInputHandler implements InputHandler{
    //private GameServer gameServer = new GameServer();
    GameServer gameServer;



    public TCPInputHandler(GameServer gameserver){
        this.gameServer = gameserver;
    }

    @Override
    public boolean wantToPlay() throws IllegalInput {
        return true;
    }

    @Override
    public int chooseNumberOfPlayers(){
        return gameServer.getNumOfPlayers();
    }

    private int getInputNumber() {
        return 0;
    }

    @Override
    public String choosePlayerName(int playerNumber) throws IllegalInput {

        //gameServer.clients.stream().forEach(client -> client.writeMessage("GET PLAYER_NAME"));

        String playerName = readMessage(playerNumber);

        return playerName;
    }

    @Override
    public Die.Face chooseDiceFace() throws IllegalInput {
        return null;
    }

    @Override
    public boolean wantToPick() throws IllegalInput {
        return false;
    }

    @Override
    public void pressEnter() {

    }

    @Override
    public boolean wantToSteal() throws IllegalInput {
        return false;
    }

    public String readMessage(int playerId){
        return gameServer.clients.get(playerId).readReceivedMessage();
    }
}