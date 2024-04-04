package Model;

import java.time.LocalDateTime;

public class Applicant {
    private String name;
    private String email;
    private LocalDateTime date;
    private double score;

    public Applicant(){};
    public Applicant (String name, String email, LocalDateTime date, Double score) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.score = score;
    }
    //Getters
    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public Double getScore(){
        return score;
    }

    //Setters
    public String setName(String name){
        return this.name = name;
    }

    public String setEmail(String email){
        return this.email = email;
    }

    public LocalDateTime setDate(LocalDateTime date){
        return this.date = date;
    }

    public Double setScore(Double score){
        return this.score = score;
    }

    public Applicant clone() {
        return new Applicant(this.name, this.email, this.date, this.score);
    }

    public String toString(){
        return "Name: " + name + "\nEmail: " + email + "\nDate: " + date + "\nScore: " + score;
    }
}

