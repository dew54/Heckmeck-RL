package Heckmeck;
import java.net.URL;
import java.nio.file.Path;
import  java.util.*;

import static java.util.Map.entry;

public class Die {

    private Face dieFace;
    private static final Random PRNG = new Random();

    public enum Face {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        WORM

    }

    // TODO scegliere che mappa usare
    private static final Map<Face,Integer> faceToIntMap = Map.ofEntries(
            entry(Face.ONE, 1),
            entry(Face.TWO, 2),
            entry(Face.THREE,3),
            entry(Face.FOUR, 4),
            entry(Face.FIVE, 5),
            entry(Face.WORM, 5));

    public static final Map<String, Face> stringToFaceMap = Map.ofEntries(
            entry("1", Face.ONE),
            entry("2", Face.TWO),
            entry("3", Face.THREE),
            entry("4", Face.FOUR),
            entry("5", Face.FIVE),
            entry("w", Face.WORM));


    //TODO: come inizializzare die
    private Die(){
        rollDie();
    }
    public static Die generateDie(Face face){
        Die die = generateDie();
        die.setDieFace(face);
        return die;
    }
    public static Die generateDie(){
        return new Die();
    }

    private void setDieFace(Face f){
        dieFace = f;
    }
    public Face getDieFace(){ return dieFace; }

    // TODO: What if text is not present in the Map (just null or excep?)
    // TODO: Mark private stringToFaceMap?
    public static Face getFaceByString(String text) {
        return stringToFaceMap.get(text);
    }

    public int getDieScore(){
        return faceToIntMap.get(dieFace);
    }

    public void rollDie(){
        int n = PRNG.nextInt(Face.values().length);
        setDieFace(Face.values()[n]);
    }

    @Override
    public String toString() {
        String dieAsText = null;
        if (dieFace==Face.ONE) {
            URL dieOneResource = Die.class.getClassLoader().getResource("TEST_DIE_ONE");
            try {
                dieAsText = FileReader.readDieFile(Path.of(dieOneResource.toURI()));
            } catch (Exception ex) {
                System.err.println(ex);
            }
        } else if (dieFace == Face.TWO) {
            URL dieTwoResource = Die.class.getClassLoader().getResource("TEST_DIE_TWO");
            try {
                dieAsText = FileReader.readDieFile(Path.of(dieTwoResource.toURI()));
            } catch (Exception ex) {
                System.err.println(ex);
            }
        } else {
            URL dieThreeResource = Die.class.getClassLoader().getResource("TEST_DIE_THREE");
            try {
                dieAsText = FileReader.readDieFile(Path.of(dieThreeResource.toURI()));
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
        return dieAsText;
    }

}
