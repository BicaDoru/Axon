package Model.Comparators;

import Model.Applicant;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ScoreAfterAdjustmentComparator implements Comparator<Applicant> {
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    public ScoreAfterAdjustmentComparator() {}

    public ScoreAfterAdjustmentComparator(LocalDateTime first, LocalDateTime last) {
        this.firstDay = first;
        this.lastDay = last;
    }

    @Override
    public int compare(Applicant o1, Applicant o2) {
        int compared = Double.compare(o2.getScore(), o1.getScore());

        if(compared == 0) {
            double firstScore = o2.getScore();
            double secondScore = o1.getScore();

            if(o2.getDate().getDayOfMonth() == firstDay.getDayOfMonth()) {
                firstScore--;
            }
            if(o2.getDate().getDayOfMonth() == lastDay.getDayOfMonth() && o2.getDate().getHour() > 12) {
                firstScore++;
            }

            if(o1.getDate().getDayOfMonth() == firstDay.getDayOfMonth()) {
                secondScore--;
            }
            if(o1.getDate().getDayOfMonth() == lastDay.getDayOfMonth() && o1.getDate().getHour() > 12) {
                secondScore++;
            }

            if(Double.compare(firstScore, secondScore) == 0) {
                compared = new DateTimeComparator().compare(o1, o2);
                if(compared == 0) {
                    compared = o1.getEmail().compareTo(o2.getEmail());
                }
            }
            else {
                compared = Double.compare(firstScore, secondScore);
            }
        }

        return compared;
    }
}
