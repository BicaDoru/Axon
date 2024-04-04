package Controller;

import Model.Applicant;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<Applicant> readFromCSV(InputStream csvInput) {
        BufferedReader csvReader = null;
        List<Applicant> listApplicants = new ArrayList<>();

        String line = "";

        try {
            csvReader = new BufferedReader(new InputStreamReader(csvInput));
            while ((line = csvReader.readLine()) != null) {
                Applicant applicant = parseApplicant(line);

                if(applicant != null) {
                    listApplicants.add(applicant);
                }
            }
        }catch(IOException exp) {
            throw new RuntimeException("Reading CSV File failed", exp);
        }finally {
            if(csvReader != null) {
                try{
                    csvReader.close();
                } catch (IOException e) {
                    System.out.println("Error while closing the file: " + e);
                    e.printStackTrace();
                }
            }
        }

        return listApplicants;
    }

    public static Applicant parseApplicant(String line) {
        String[] fields = line.split(",");
        if(fields.length != 4) {
            return null;
        }

        String name = fields[0];
        boolean foundSpace = false;
        for(int i = 0; i < name.length(); i++) {
            if(name.charAt(i) == ' ') {
                foundSpace = true;
            }
        }
        if (!foundSpace) {
            return null;
        }

        String email = fields[1];
        if(!isEmailValid(email)) {
            return null;
        }

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(fields[2], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch(DateTimeParseException e) {
            return null;
        }

        double score;
        try {
            score = Double.parseDouble(fields[3]);
        }catch (NumberFormatException e) {
            return null;
        }
        if(score < 0 || score > 10) {
            return null;
        }

        return new Applicant(name, email, dateTime, score);
    }

    private static boolean isEmailValid(String email) {
        int size = email.length();

        for(int i = 0; i < size; i++) {
            char character = email.charAt(i);
            if(!Character.isLetterOrDigit(character) && character != '@' && character != '.' && character != '_' && character != '_') {
                return false;
            }
        }

        if(!Character.isLetter(email.charAt(0))) {
            return false;
        }

        int count = 0;
        for(int i = 0; i < size; i++) {
            if(email.charAt(i) == '@'){
                count++;
            }
        }
        if(count != 1) {
            return false;
        }

        int atIndex = email.indexOf('@');
        int dotIndex = email.indexOf('.', atIndex);
        if(dotIndex <= atIndex + 1) {
            return false;
        }

        if(!Character.isLetter(email.charAt(size - 1))) {
            return false;
        }

        return true;
    }

    public static InputStream createInputStreamFromCSVFile(String filename) {
        try{
            return new FileInputStream(filename);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
