package sol;


import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;


//mine
import java.util.ArrayList;

/**
 * Class for finding the line numbers where a given word appears in a file.
 */
public class CSVParser {

    ArrayList<ArrayList<String>> ourCSV;

    //maps strings to (the rows that they appear on to the columns in that row that have the word)
    HashMap<String, HashMap<Integer, HashSet<Integer>>> valueMap;

    public CSVParser(String filename) {
        ourCSV = new ArrayList<ArrayList<String>>();
        valueMap = new HashMap<String, HashMap<Integer, HashSet<Integer>>>();

        try {
            FileReader fReader = new FileReader(filename);
            BufferedReader bReader = new BufferedReader(fReader);
            String row = bReader.readLine();

            // We go through row by row, starting at the first row
            int rowIndex = 0;
            while (row != null) {
                String[] rowArray = row.split(",");
                ArrayList<String> parsedRow= new ArrayList<>();

                //We go through one row's values, column by column
                int colIndex = 0;
                for (String value : rowArray) {
                    parsedRow.add(value);
                    //if the value has already been seen once, it'll be in the valueMap.
                    //we need to check if it has already been spotted in this row
                    if (valueMap.containsKey(value)) {
                        if (valueMap.get(value).containsKey(rowIndex)) {
                            valueMap.get(value).get(rowIndex).add(colIndex);
                        } else {
                            valueMap.get(value).put(rowIndex, new HashSet<>());
                            valueMap.get(value).get(rowIndex).add(colIndex);
                        }
                    } else {
                        valueMap.put(value, new HashMap<>());
                        valueMap.get(value).put(rowIndex, new HashSet<>());
                        valueMap.get(value).get(rowIndex).add(colIndex);
                    }
                    colIndex++;
                }
                ourCSV.add(parsedRow);
                rowIndex ++;
                row = bReader.readLine();
            }
            bReader.close();
        } catch (IOException e){
            System.out.print(e.getMessage());
        }
    }



    public Set<Integer> lookupRows(String value) {
        if (valueMap.containsKey(value)) {
            return valueMap.get(value).keySet();
        } else {
            return new HashSet<Integer>();
        }
    }


    public static void main(String[] args) {
        if (!(args.length >= 2)) {
            System.out.println("Please only provide a single file, then the word you are looking up, and nothing else!");
        }
        try {
            CSVParser newCSVParser = new CSVParser(args[0]);
            System.out.println(args[1] + " found on lines: " + newCSVParser.lookupRows(args[1]));

        } catch (Exception e){
            System.out.println("File not found: " + args[0]);
        }


    }

}