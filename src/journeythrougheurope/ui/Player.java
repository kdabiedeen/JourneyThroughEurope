/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Player implements Serializable{
    private boolean computer;
    private String name;
    private ArrayList<Integer> colorValues;
    private String[] colors;
    private Cities cityInfo;
    private String[] destinations;
    private int reachedDest = 2;
    private String previousCity;
    private int skippedTurns;
    private ArrayList<String> history = new ArrayList();
    
    public Player(String name, boolean computer){
        skippedTurns = 0;
        colors = new String[3];
        destinations = new String[3];
        colorValues = new ArrayList();
        this.computer = computer;
        this.name = name;
        previousCity = "XXXXX";
        cityInfo = new Cities();
    }
    

    /**
     * @return the computer
     */
    public boolean isComputer() {
        return computer;
    }

    /**
     * @param computer the computer to set
     */
    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cards
     */
    public ArrayList<Integer> getColorValues() {
        return colorValues;
    }
    
    public void initializeColors() {
        int pointer = 0;
        int red = 0;
        int green = 1;
        int yellow = 2;
        for(Integer i : colorValues){
            if(i.equals((Integer)red)){
                colors[pointer] = "red";
                pointer++;
            }
            else if(i.equals((Integer)green)){
                colors[pointer] = "green";
                pointer++;
            }
            else if(i.equals((Integer)yellow)){
                colors[pointer] = "yellow";
                pointer++;
            }
        }
    }
    
    public void print(){
    //  System.out.println("");
        for(String i : colors){
           // System.out.print(i + " ");
        }
    }

    public void intializeCards() {
       String[] citiesSource = cityInfo.getName();
       int pointer = 0;
       for(String i : colors){
            if(i.equals("green")){
                destinations[pointer] = citiesSource[(int) Math.floor(Math.random()*60)];
                pointer++;
            }
            if(i.equals("red")){
                destinations[pointer] = citiesSource[(int) Math.floor(Math.random()*60) + 60];
                pointer++;
            }
            if(i.equals("yellow")){
                destinations[pointer] = citiesSource[(int) Math.floor(Math.random()*60) + 120];
                pointer++;
            }
        }
       
       for(String i : getDestinations()){
          //  System.out.print(i + " ");
        }
    }

    /**
     * @return the destinations
     */
    public String[] getDestinations() {
        return destinations;
    }

    /**
     * @return the reachedDest
     */
    public int getReachedDest() {
        return reachedDest;
    }

    /**
     * @param reachedDest the reachedDest to set
     */
    public void setReachedDest(int reachedDest) {
        this.reachedDest = reachedDest;
    }

    /**
     * @return the previousCity
     */
    public String getPreviousCity() {
        return previousCity;
    }

    /**
     * @param previousCity the previousCity to set
     */
    public void setPreviousCity(String previousCity) {
        this.previousCity = previousCity;
    }
    
    public void setDestinations(String[] dest){
        destinations = dest;
    }
    
    public int getTurnsToSkip(){
        return skippedTurns;
    }
    
    public void setTurnsToSkip(int skippedTurns){
        this.skippedTurns = skippedTurns;
    }
    
    public ArrayList<String> getHistory(){
        return history;
    }
    
    
}
