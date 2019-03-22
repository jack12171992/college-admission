package arrowhead.insurance.college.admission;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * The purpose of this class is to represent a Student
 * @author Jack
 *
 */
public class Student {

    private final String firstName;
    private final String lastName;
    private final String state;
    private final int age;
    private final double gpaGrade;
    private final Optional<Integer> scoreSAT;
    private final Optional<Integer> scoreACT;
    private final boolean hasFelony;
    private final int excelSheet;
    private final int excelSheetRow;
    private Optional<List<String>> rejectReason = Optional.empty();     //The reject reasons that the student might have after verifying student's qualification

    public Student(StudentBuilder builder) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(builder.firstName), "The first name of the applicant should not be empty or null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(builder.lastName), "The last name of the applicant should not be empty or null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(builder.state), "The state where the  applicant comes from should not be empty or null");
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.state = builder.state;
        this.age = builder.age;
        this.gpaGrade = builder.gpaGrade;
        this.scoreSAT = builder.scoreSAT;
        this.scoreACT = builder.scoreACT;
        this.hasFelony = builder.hasFelony;
        this.excelSheet = builder.excelSheet;
        this.excelSheetRow = builder.excelSheetRow;
    }

    public static class StudentBuilder {
        private  String firstName = null;
        private String lastName = null;
        private String state = null;
        private int age;
        private double gpaGrade;
        private Optional<Integer> scoreSAT = Optional.empty();
        private Optional<Integer> scoreACT = Optional.empty();
        private boolean hasFelony = false;
        private int excelSheet = -1;
        private int excelSheetRow = -1;

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

        protected StudentBuilder setState(String state) {
            this.state = state;
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

        protected StudentBuilder setExcelSheet(int excelSheet) {
            this.excelSheet = excelSheet;
            return this;
        }

        protected StudentBuilder setExcelSheetRow(int excelSheetRow) {
            this.excelSheetRow = excelSheetRow;
            return this;
        }

        protected Student build() {
            return new Student(this);
        }
    }

    protected String getFirstName() {
        return firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected String getState() {
        return state;
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

    protected int getExcelSheet() {
        return excelSheet;
    }

    protected int getExcelSheetRow() {
        return excelSheetRow;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            Student student = (Student)obj;
            if (firstName.equals(student.firstName)) {
                if (lastName.equals(student.lastName)) {
                    if (state.equals(student.state)) {
                        if (age == student.age) {
                            if (gpaGrade == student.gpaGrade) {
                                if (scoreSAT == student.scoreSAT) {
                                    if (scoreACT == student.scoreACT) {
                                        if (hasFelony == student.hasFelony) {
                                            if (excelSheet == student.excelSheet) {
                                                if (excelSheetRow == student.excelSheetRow) {
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
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, state, age, gpaGrade,
                            scoreSAT, scoreACT, hasFelony, excelSheetRow, excelSheet);
    }

    protected void setRejectReasons(List<String> rejectReasons) {
        this.rejectReason= Optional.ofNullable(rejectReasons);
    }

    protected Optional<List<String>> getRejectReasons(){
        return rejectReason;
    }
}
