package Start;

import Controller.ApplicantsProcessor;
import Controller.CSVReader;

import java.io.InputStream;

public class Main {
    public static void main(String args[]) {
        ApplicantsProcessor applicantsProcessor = new ApplicantsProcessor();
        InputStream inputStream = CSVReader.createInputStreamFromCSVFile("src/file2.csv");
        String outputJSON = applicantsProcessor.processApplicants(inputStream);
        System.out.println(outputJSON);
    }
}