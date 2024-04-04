package Controller;

import Model.Applicant;
import Model.Comparators.DateTimeComparator;
import Model.Comparators.ScoreAfterAdjustmentComparator;
import Model.Comparators.ScoreComparator;
import com.google.gson.Gson;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class ApplicantsProcessor {
    public ApplicantsProcessor() {
        super();
    }

    public BigDecimal getAverageTopHalfScore(List<Applicant> list) {
        int size = list.size();
        int halfSize = size / 2 + size % 2;

        List<Applicant> sortedList = new ArrayList<>(list);
        sortedList.sort(new ScoreComparator());

        BigDecimal scoreSum = BigDecimal.ZERO;
        for (int i = 0; i < halfSize; i++) {
            scoreSum = scoreSum.add(BigDecimal.valueOf(sortedList.get(i).getScore()));
        }

        return scoreSum.divide(BigDecimal.valueOf(halfSize), 2, RoundingMode.HALF_UP);
    }

    public String[] topThreeApplicants(List<Applicant> list) {
        List<Applicant> copyList = new ArrayList<>();
        for (Applicant applicant : list) {
            copyList.add(applicant.clone());
        }
        copyList.sort(new DateTimeComparator());

        LocalDateTime firstDate;
        LocalDateTime lastDate;
        firstDate = copyList.get(0).getDate();
        lastDate = copyList.get(copyList.size() - 1).getDate();

        if (firstDate.compareTo(lastDate) != 0) {
            modifyScore(copyList, firstDate, lastDate);
        }

        copyList.sort(new ScoreAfterAdjustmentComparator(firstDate, lastDate));

        int index;
        if (copyList.size() < 3) {
            index = copyList.size();
        } else {
            index = 3;
        }

        String[] topThree = new String[index];

        for (int i = 0; i < index; i++) {
            String name = copyList.get(i).getName();
            topThree[i] = name.substring(name.lastIndexOf(" ") + 1);
        }

        return topThree;
    }

    public String processApplicants(InputStream csvStream) {
        List<Applicant> list = CSVReader.readFromCSV(csvStream);

        int numberUniqueApplicants = CalculateNumberOfUniqueApplicants(list);
        System.out.println("Number of unique applicants: " + numberUniqueApplicants + "\n");

        String[] topThree = topThreeApplicants(list);
        System.out.println("Top three applicants by score: ");
        for (String applicantName : topThree) {
            System.out.println(applicantName);
        }

        BigDecimal averageScoreTopHalf = getAverageTopHalfScore(list);
        System.out.println("\nAverage score of top half applicants: " + averageScoreTopHalf + "\n");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("numberUniqueApplicants", numberUniqueApplicants);
        jsonMap.put("topThree", topThree);
        jsonMap.put("averageScoreTopHalf", averageScoreTopHalf);

        Gson gson = new Gson();

        return gson.toJson(jsonMap);
    }

    private void modifyScore(List<Applicant> list, LocalDateTime firstDate, LocalDateTime lastDate) {
        for (Applicant applicant : list) {
            if (applicant.getDate().getDayOfMonth() == firstDate.getDayOfMonth()) {
                double score = applicant.getScore();
                applicant.setScore(Math.min(score + 1, 10));
            }
            if (applicant.getDate().getDayOfMonth() == lastDate.getDayOfMonth() && applicant.getDate().getHour() >= 12) {
                double score = applicant.getScore();
                applicant.setScore(Math.max(score - 1, 0));
            }
        }
    }

    public int CalculateNumberOfUniqueApplicants(List<Applicant> list) {
        int size = list.size();

        List<Applicant> applicantsToRemove = new ArrayList<>();
        Set<String> emailList = new HashSet<>();
        for (int i = size - 1; i >= 0; i--) {
            boolean canInsert = emailList.add(list.get(i).getEmail());
            if (!canInsert) {
                applicantsToRemove.add(list.get(i));
            }
        }

        for (Applicant applicant : applicantsToRemove) {
            list.remove(applicant);
        }

        return emailList.size();
    }
}
