/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Cities {

    private ArrayList<String> nameArr;
    private String[] name;
    private String[] color;
    private int[] quarter;
    private double[] xCoord;
    private double[] yCoord;
    String routesData;

    //routeData
    private ArrayList<String> routeNames;
    private ArrayList<String[]> landRoutes;
    private ArrayList<String[]> seaRoutes;

    //airportData
    private ArrayList<String> airportCities;
    private int[] airportRegions;
    private double[] airportxCoord;
    private double[] airportyCoord;

    //special cities
    private ArrayList<String> specialCities;
    private String specialDataStr;

    //parallel arrays
    private ArrayList<String> spCitiesName;
    private ArrayList<String> spCitiesData;

    private ArrayList<String> cityList;
    private ArrayList<String> infoList;
    
    private ArrayList<String> seaPorts;
    
    private ArrayList<ArrayList<String>> matrix;

    public Cities() {
        matrix = new ArrayList();
        cityList = new ArrayList();
        infoList = new ArrayList();
        nameArr = new ArrayList();
        spCitiesName = new ArrayList();
        spCitiesData = new ArrayList();
        specialCities = new ArrayList();
        routeNames = new ArrayList();
        landRoutes = new ArrayList();
        seaRoutes = new ArrayList();
        airportCities = new ArrayList();
        airportRegions = new int[35];
        airportxCoord = new double[35];
        airportyCoord = new double[35];
        townData();
        specialCities();
        specialData();
        specialDataParse(specialDataStr);
        routeData();
        routesData(routesData);
        airportsData();
        name = new String[180];
        color = new String[180];
        quarter = new int[180];
        xCoord = new double[180];
        yCoord = new double[180];
        run();
        for (String i : name) {
            nameArr.add(i);
        }
        for (String i : cityList) {
            i = i.toUpperCase();
        }
        
        seaPorts = new ArrayList();
        for(String[] i : seaRoutes){
            for(String j : i){
                seaPorts.add(j);
            }
        }
        seaPorts = removeDuplicates(seaPorts);
        
        for(int i = 0; i < 180; i++){
             matrix.add(new ArrayList<String>());
        }
        
        for(int i = 0; i < routeNames.size(); i++){
            matrix.get(i).add(routeNames.get(i));
            String[] land = landRoutes.get(i);
            String[] sea = seaRoutes.get(i);
            
            for(String j : land){
                if(!j.equals(""))
                    matrix.get(i).add(j);
            }
            
            for(String z : sea){
                if(!z.equals(""))
                    matrix.get(i).add(z);
            }
            
            System.out.println(i + " " + matrix.get(i));
        }
        print();
    }
    
    public ArrayList<String> computePath(ArrayList<String> path, ArrayList<String> temp, String start, String end) {
        if (temp.contains(end)) {
            path.add(end);
            return path;
        } else {
            for (ArrayList<String> i : matrix) {
                if (i.contains(start)) {
                    System.out.println("CONTAINS " +i);
                    for(String j : i){
                        path.add(j);
                        ArrayList<String> tempVa = i;
                        tempVa.remove(i.get(0));
                        System.out.println(j);
                        if(tempVa.contains(start)){
                            tempVa.remove(start);
                        }
                        return computePath(path,tempVa, j, end);
                    }
                }
            }
            return null;
        }
    }
    
    public static ArrayList<String> removeDuplicates(ArrayList<String> c) {
        ArrayList<String> result = new ArrayList();
        for (String o : c) {
            if (!result.contains(o)) {
                result.add(o);
            }
        }
        return result;
    }

    public void specialCities() {
        int pointer = 0;
        try {
            FileInputStream fstream = new FileInputStream("src/specialCities.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                specialCities.add(strLine);
            }

            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void run() {
        int pointer = 0;
        try {
            FileInputStream fstream = new FileInputStream("src/cities.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                name[pointer] = strLine.substring(0, strLine.indexOf(","));
                strLine = strLine.substring(strLine.indexOf(",") + 1, strLine.length());
                color[pointer] = strLine.substring(0, strLine.indexOf(","));
                strLine = strLine.substring(strLine.indexOf(",") + 1, strLine.length());
                quarter[pointer] = Integer.parseInt(strLine.substring(0, strLine.indexOf(",")));
                strLine = strLine.substring(strLine.indexOf(",") + 1, strLine.length());
                xCoord[pointer] = .24 * Double.parseDouble(strLine.substring(0, strLine.indexOf(",")));
                strLine = strLine.substring(strLine.indexOf(",") + 1, strLine.length());
                yCoord[pointer] = .24 * Double.parseDouble(strLine);
                // System.out.println(name[pointer] + " " + color[pointer] + " " + quarter[pointer] + " " + xCoord[pointer] + " " + yCoord[pointer]);
                pointer++;
            }

            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void print() {
        //System.out.println(getSpCitiesName().size());
    }

    /**
     * @return the name
     */
    public String[] getName() {
        return name;
    }

    /**
     * @return the color
     */
    public String[] getColor() {
        return color;
    }

    /**
     * @return the quarter
     */
    public int[] getQuarter() {
        return quarter;
    }

    /**
     * @return the xCoord
     */
    public double[] getxCoord() {
        return xCoord;
    }

    /**
     * @return the yCoord
     */
    public double[] getyCoord() {
        return yCoord;
    }

    public void routeData() {
        try {
            FileInputStream fstream = new FileInputStream("src/routes.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            String routes = "";

            while ((strLine = br.readLine()) != null) {
                routes = routes.concat(strLine);
            }
            routesData = routes.replaceAll(" ", "");
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void routesData(String routesData) {
        routesData = routesData.replace("<routes>", "");
        routesData = routesData.replace("</routes>", "");
        String[] temp = routesData.split("</city><city><name>");
        temp[0] = temp[0].replaceFirst("<city><name>", "");
        for (String i : temp) {
            i = i.replace("</name>", "");
            String q = i;
            q = q.substring(0, q.lastIndexOf("<land>"));
            routeNames.add(q);

            String j = i;
            String z = j.substring(j.indexOf("<land>") + 6, j.lastIndexOf("</land>"));
            z = z.replaceAll("<city>", " ");
            z = z.replaceAll("</city>", " ");
            z = z.trim();
            String[] landArray = z.split(" ");
            String[] landArray2 = new String[landArray.length / 2 + 1];
            int jf = 0;
            for (int jh = 0; jh < landArray.length; jh += 2) {
                landArray2[jf] = landArray[jh];
                jf++;
            }
            landRoutes.add(landArray2);

            String g = i;
            String f = g.substring(g.indexOf("<sea>") + 5, g.lastIndexOf("</sea>"));
            f = f.replaceAll("<city>", " ");
            f = f.replaceAll("</city>", " ");
            f = f.trim();
            String[] seaArray = f.split(" ");
            String[] seaArray2 = new String[seaArray.length / 2 + 1];
            int gf = 0;
            for (int gh = 0; gh < seaArray.length; gh += 2) {
                seaArray2[gf] = seaArray[gh];
                gf++;
            }
            seaRoutes.add(seaArray2);

        }
    }

    public void airportsData() {
       try {
            FileInputStream fstream = new FileInputStream("src/airport.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            int pointer = 0;

            while ((strLine = br.readLine()) != null) {
                String[] temp = strLine.split(" ");
                //System.out.println(temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
                airportCities.add(temp[0]);
                airportRegions[pointer] = Integer.parseInt(temp[1]);
                airportxCoord[pointer] = Double.parseDouble(temp[2]);
                airportyCoord[pointer] = Double.parseDouble(temp[3]);
                //System.out.println(airportCities[pointer] + " " + airportRegions[pointer] + " " + getAirportxCoord()[pointer] + " " + getAirportyCoord()[pointer]);
                pointer++;
            }

       } catch (FileNotFoundException e) {
           System.out.println("ERROR IN THIS");
        }
       catch(IOException e){
        System.out.println("ERROR YO");
    }
    }

    public void specialDataParse(String str) {
        String[] temp = str.split("</card><card>");
        for (String i : temp) {
            if (!i.contains("false")) {
                String tempo = i;
                i = i.substring(6, i.indexOf("/") - 1);
                tempo = tempo.substring(tempo.indexOf("<instruction") + 19, tempo.indexOf("</instruction"));
                tempo = tempo.replace("</type>", " ");
                getSpCitiesName().add(i);
                getSpCitiesData().add(tempo);
               // System.out.println(i + " " + tempo);
            }
        }

    }

    public void specialData() {
        try {
            FileInputStream fstream = new FileInputStream("src/specialInstructions.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            specialDataStr = "";

            while ((strLine = br.readLine()) != null) {
                specialDataStr = specialDataStr.concat(strLine);
            }

            specialDataStr = specialDataStr.replaceAll(" ", "");

            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void townData() {
        int pointer = 0;
        try {
            FileInputStream fstream = new FileInputStream("src/townInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            String data = "";

            while ((strLine = br.readLine()) != null) {
                if (strLine.length() < 30 && !strLine.equals("") && !strLine.equals(" ")) {
                    getCityList().add(strLine);
                }
                if (strLine.length() > 30) {
                    getInfoList().add(strLine);
                }
            }

            for (int i = 0; i < getCityList().size(); i++) {
                //System.out.println(getCityList().get(i));
            }

            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getRouteNames() {
        return routeNames;
    }

    public ArrayList<String[]> getLandRoutes() {
        return landRoutes;
    }

    public ArrayList<String[]> getSeaRoutes() {
        return seaRoutes;
    }

    public ArrayList<String> getAirportCities() {
        return airportCities;
    }

    /**
     * @return the airportxCoord
     */
    public double[] getAirportxCoord() {
        return airportxCoord;
    }

    /**
     * @return the airportyCoord
     */
    public double[] getAirportyCoord() {
        return airportyCoord;
    }
    
    public int[] getAirportRegions(){
        return airportRegions;
    }

    public ArrayList<String> getSpecialCities() {
        return specialCities;
    }

    /**
     * @return the spCitiesName
     */
    public ArrayList<String> getSpCitiesName() {
        return spCitiesName;
    }

    /**
     * @return the spCitiesData
     */
    public ArrayList<String> getSpCitiesData() {
        return spCitiesData;
    }

    public ArrayList<String> getNameArr() {
        return nameArr;
    }

    /**
     * @return the cityList
     */
    public ArrayList<String> getCityList() {
        return cityList;
    }

    /**
     * @return the infoList
     */
    public ArrayList<String> getInfoList() {
        return infoList;
    }
    
     public ArrayList<String> getSeaPorts(){
        return seaPorts;
    }
     
    public ArrayList<ArrayList<String>> getMatrix(){
        return matrix;
    }
}
