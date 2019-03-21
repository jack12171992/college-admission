package arrowhead.insurance.college.admission;

import java.util.Objects;
import java.util.Optional;


public class Student {

    private final String firstName;
    private final String lastName;
    private final boolean isInStateStudent;
    private final int age;
    private final double gpaGrade;
    private final Optional<Integer> scoreSAT;
    private final Optional<Integer> scoreACT;
    private final boolean hasFelony;
    private String rejectReason = "";
    private Optional<Boolean> applicationStatus = Optional.empty();

    public Student (String firstName, String lastName, boolean isInStateStudent, int age,
                    double gpaGrade, Optional<Integer> scoreSAT, Optional<Integer> scoreACT, boolean hasFelony) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isInStateStudent = isInStateStudent;
        this.age = age;
        this.gpaGrade = gpaGrade;
        this.scoreSAT = scoreSAT;
        this.scoreACT = scoreACT;
        this.hasFelony = hasFelony;
    }

    public static class StudentBuilder {
        private  String firstName;
        private String lastName;
        private boolean isInStateStudent;
        private int age;
        private double gpaGrade;
        private Optional<Integer> scoreSAT;
        private Optional<Integer> scoreACT;
        private boolean hasFelony;

        protected static StudentBuilder newBuilder() {
            return new StudentBuilder();
        }

        protected StudentBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        protected StudentBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        protected StudentBuilder setIsInStateStudent(boolean isInStateStudent) {
            this.isInStateStudent = isInStateStudent;
            return this;
        }

        protected StudentBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        protected StudentBuilder setGPA(double gpaGrade) {
            this.gpaGrade = gpaGrade;
            return this;
        }

        protected StudentBuilder setScoreSAT(int scoreSAT) {
            this.scoreSAT = Optional.of(scoreSAT);
            return this;
        }

        protected StudentBuilder setScoreACT(int scoreACT) {
            this.scoreACT = Optional.of(scoreACT);
            return this;
        }

        protected StudentBuilder sethasFelony(boolean hasFelony) {
            this.hasFelony = hasFelony;
            return this;
        }

        protected Student build() {
            return new Student(firstName, lastName, isInStateStudent, age,
                               gpaGrade, scoreSAT, scoreACT, hasFelony);
        }
    }

    protected String getFirstName() {
        return firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected boolean isInStateStudent() {
        return isInStateStudent;
    }

    protected int getAge() {
        return age;
    }

    protected double getGPA() {
        return gpaGrade;
    }

    protected Optional<Integer> getSATscore(){
        return scoreSAT;
    }

    protected Optional<Integer> getACTscore(){
        return scoreACT;
    }

    protected boolean hasFelony() {
        return hasFelony;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student student = (Student)obj;
            if (firstName.equals(student.firstName)) {
                if (lastName.equals(student.lastName)) {
                    if (isInStateStudent == student.isInStateStudent) {
                        if (age == student.age) {
                            if (gpaGrade == student.gpaGrade) {
                                if (scoreSAT == student.scoreSAT) {
                                    if (scoreACT == student.scoreACT) {
                                        if (hasFelony == student.hasFelony) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, isInStateStudent, age,
                            gpaGrade, scoreSAT, scoreACT, hasFelony);
    }

    protected void setApplicationStatus(boolean status) {
        applicationStatus = Optional.of(status);
    }

    protected void setRejectReasons(String rejectReason) {
        this.rejectReason= rejectReason;
    }
}
