package Heckmeck;

import exception.IllegalInput;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    // final (?)
    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private IOHandler io;
    private boolean gameFinished;

    private Player actualPlayer;

    public Game(OutputHandler output, InputHandler input) {
        io = new IOHandler(input,output);
    }

    public void init(){
       io.showWelcomeMessage();
        if (io.wantToPlay()){
            int numberOfPlayers = io.chooseNumberOfPlayers();
            this.players = setupPlayers(numberOfPlayers);
            this.dice = Dice.generateDice(); // TODO ha senso rinominare in init()?
            this.boardTiles = BoardTiles.init();
            gameFinished = false;
        }
        else {
            System.exit(0);
        }
    }

    public Game(Player[] players, OutputHandler output, InputHandler input){
        this.players = players;
        this.dice = Dice.generateDice();// TODO ha senso rinominare in init()?
        this.boardTiles = BoardTiles.init();
        gameFinished = false;
    }
    public void play() throws IOException {
        int playerNumber = 0;
        actualPlayer = players[playerNumber];
        while(!boardTiles.isEmpty()){
            io.showBoardTiles(boardTiles);
            playerTurn();
            playerNumber++;
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber];
        }
        Player winnerPlayer = whoIsTheWinner();
        io.showWinnerPlayerMessage(winnerPlayer);
    }

    public Player whoIsTheWinner() {
        
        List<Player> winners = Arrays.stream(players).sorted(Comparator.comparingInt(Player::getWormNumber)).toList();
        int highestWormScore = winners.get(winners.size()-1).getWormNumber();
        winners = winners.stream().filter(e -> e.getWormNumber() >= highestWormScore).collect(Collectors.toList());
        if(winners.size() == 1) return winners.get(0);
        else {
            winners.sort(Comparator.comparingInt(Player::getNumberOfPlayerTile));
            int lowerNumberOfTiles = winners.get(0).getNumberOfPlayerTile();
            winners = winners.stream().filter(p -> p.getNumberOfPlayerTile() <= lowerNumberOfTiles).collect(Collectors.toList());
            if(winners.size() == 1) return winners.get(0);
            else{
                winners.sort(Comparator.comparingInt(Player::getHighestTileNumber));
                return winners.get(winners.size()-1);
            }
        }
    }

    private void playerTurn() throws IOException {
        //TODO: FIXARE Press any key
        //output.showTurnBeginConfirm(actualPlayer);  // pass only the String (?)
        //input.pressAnyKey();
        boolean isOnRun = roll();
        while (isOnRun){
            if (dice.isWormChosen()) {
                if (steal() || pick()) {
                    isOnRun = false;
                } else isOnRun = roll();
            } else isOnRun = roll();
        }
        dice.resetDice();
    }

    //TODO: catch IOException somewhere
    private boolean pick() throws IOException{
        if(!canPick()) return false;
        io.showPlayerScore(actualPlayer,dice);
        if(io.wantToPick()) {
            pickBoardTile(dice.getScore());
            return true;
        }else return false;
    }

    private boolean canPick(){
        return dice.getScore() >= boardTiles.getMinValueTile().getNumber();
    }


    private boolean steal() {
        if(!canSteal()) return false;
        if(io.wantToSteal()){
            stealTile();
            return true;
        }else return false;
    }

    private void stealTile() {
        int playerScore = dice.getScore();
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && robbedPlayer.hasTile() && playerScore == robbedPlayer.getLastPickedTile().getNumber()){
                actualPlayer.pickTileFromPlayer(robbedPlayer.getLastPickedTile(),robbedPlayer);
            }
        }
    }

    //TODO: equals method for players
    private boolean canSteal() {
        int playerScore = dice.getScore();
        if(playerScore < Tile.tileMinNumber) return false;
        for(Player player : players){
            if(!player.equals(actualPlayer) && player.hasTile() && playerScore == player.getLastPickedTile().getNumber()){
                return true;
            }
        }
        return false;
    }

    private void pickBoardTile(int actualPlayerScore){
        TreeSet<Tile> acquirableTiles = new TreeSet<>(boardTiles.getTilesList().stream().filter(tile-> tile.getNumber() <= actualPlayerScore).toList());
        actualPlayer.pickTileFromBoard(acquirableTiles.last(), boardTiles);
    }

    private boolean roll() throws IOException {
        dice.rollDice();
        io.showPlayerData(actualPlayer, dice);
        io.showDice(dice);
        if(dice.canPickAFace()){
            Die.Face chosenDieFace = pickDieFace();
            dice.chooseDice(chosenDieFace);
            return true;
        } else{
            bust();
            return false;
        }
    }

    private Die.Face pickDieFace() {
        while(true){
            Die.Face chosenDieFace = io.chooseDieFace(dice);
            if(!dice.isFacePresent(chosenDieFace)) io.showFaceNotPresentMessage();
            else if(dice.isFaceChosen(chosenDieFace)) io.showAlreadyPickedDice();
            else return chosenDieFace;
        }
    }

    private void bust(){
        io.showBustMessage();
        actualPlayer.removeLastPickedTile();
        boardTiles.bust();
    }

    private Player[] setupPlayers(int numberOfPlayers) {
        Player[] playersList = new Player[numberOfPlayers];
        for(int i=0; i<numberOfPlayers; i++){
            String playerName = io.choosePlayerName(i + 1);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.showAlreadyPickedPlayerName();
                playerName = io.choosePlayerName(i+1);
            }
            playersList[i] = Player.generatePlayer(playerName);
        }
        return playersList;
    }

    private boolean isNameAlreadyPicked(String name, Player[] playersList){
        return Arrays.stream(playersList).filter(Objects::nonNull).anyMatch(player -> player.getName().equals(name));
    }
}
