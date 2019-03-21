package arrowhead.insurance.college.admission;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class QualificationVerifier {

    private static List<String> currRejectReason = Lists.newArrayList();
    private static final int ACCEPT_GPA = 90;
    private static final int REJECT_GPA = 70;
    private static final int ACCEPT_SAT_SCORE = 1920;
    private static final int ACCEPT_ACT_SCORE = 27;
    private static final String STATE = "California";
    
    //Prevent from instantiation
    private QualificationVerifier() {};

    protected static boolean verifyStudentQualification(Student student) {
        boolean isQualified = checkLegalName(student.getFirstName()) &&
                              checkLegalName(student.getLastName()) &&
                              checkAge(student.getAge(), student.getState()) &&
                              checkGPA(student.getGPA()) &&
                              checkTestScore(student.getSATscore(), student.getACTscore()) &&
                              checkFelonies(student.hasFelony()) &&
                              currRejectReason.isEmpty();

        student.setRejectReasons(currRejectReason);
        return isQualified;
    }

    private static boolean checkLegalName(String name) {
        Preconditions.checkArgument(Strings.isNullOrEmpty(name), "Both first and last name should not be empty");
        return checkFirstLetterUpperCase(name.charAt(0)) &&
                checkRestLetterLowerCase(name.substring(1));
    }
    
    private static boolean checkFirstLetterUpperCase(char firstChar) {
        int ascii = (int)firstChar;
        if (ascii >= 65 && ascii <= 90) {
            return true;
        }
        currRejectReason.add("The first letter of either first or last name is not capitalized.\n");
        return false;
    }
    
    private static boolean checkRestLetterLowerCase(String name) {
        if (name.equals(name.toLowerCase())) {
            return true;
        }
        currRejectReason.add("Either the first or last name without the first letter are not all lower case.\n");
        return false;
    }

    private static boolean checkAge(int age, String state) {
        boolean isInStateStudent = state.equals(STATE);
        if ((isInStateStudent && age >= 17 && age < 26) ||
            (!isInStateStudent && age > 80)) {
            return true;
        } else if (age < 0) {
            currRejectReason.add("The age of the applicant should not be negative.\n");
        }
        return false;
    }

    private static boolean checkGPA(double gpa) {
        if (gpa >= ACCEPT_GPA) {
            return true;
        } else if (gpa < REJECT_GPA) {
            currRejectReason.add("The application's GPA is below 70% of the scale provided on application.\n");
        }
        return false;
    }

    private static boolean checkTestScore(Optional<Integer> scoreSAT, Optional<Integer> scoreACT) {
        Preconditions.checkArgument(scoreSAT.isPresent() || scoreACT.isPresent(), "At least one of the SAT or ACT score must be provided.");
        return ((scoreSAT.isPresent() && scoreSAT.get() > ACCEPT_SAT_SCORE) ||
                (scoreACT.isPresent() && scoreACT.get() > ACCEPT_ACT_SCORE));
    }

    private static boolean checkFelonies(boolean hasFelony) {
        if (hasFelony) {
            currRejectReason.add("The applicant has one or more felonies over the past 5 years.\n");
            return false;
        }
        return true;
    }

}
