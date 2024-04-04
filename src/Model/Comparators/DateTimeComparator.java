package Model.Comparators;

import Model.Applicant;

import java.util.Comparator;

public class DateTimeComparator implements Comparator<Applicant> {

    @Override
    public int compare(Applicant o1, Applicant o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}