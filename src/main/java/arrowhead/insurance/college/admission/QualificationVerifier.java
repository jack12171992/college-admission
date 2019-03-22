package arrowhead.insurance.college.admission;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * The purpose of this class is to determine the eligibility of each student based on their information
 * @author Jack
 *
 */
public class QualificationVerifier {

    private static List<String> currRejectReason;               //The collection that contains any possible reject reasons
    private static final int ACCEPT_GPA = 90;
    private static final int REJECT_GPA = 70;
    private static final int ACCEPT_SAT_SCORE = 1920;
    private static final int ACCEPT_ACT_SCORE = 27;
    private static final String STATE = "California";
    private static final String REJECTION_INVALID_FIRST_LETTER = "The first letter of either first or last name is not capitalized.";
    private static final String REJECTION_INVALID_REST_LETTERS = "Either the first or last name without the first letter are not all lower case.";
    private static final String REJECTION_LOW_GPA = "The application's GPA is below 70% of the scale provided on application.";
    private static final String REJECTION_HAS_FELONY = "The applicant has one or more felonies over the past 5 years.";

    //Prevent from instantiation
    private QualificationVerifier() {};

    protected static boolean verifyStudentQualification(Student student) {
        currRejectReason = Lists.newArrayList();    //Reset the collection for each student

        boolean hasLegalFirstName = checkLegalName(student.getFirstName());
        boolean hasLegalLastName = checkLegalName(student.getLastName());
        boolean hasLegalAgeAndState = checkAgeAndState(student.getAge(), student.getState());
        boolean hasPassedGPA = checkGPA(student.getGPA());
        boolean hasPassedTestScore = checkTestScore(student.getSATscore(), student.getACTscore());
        boolean hasFelonies = checkHasFelonies(student.hasFelony());

        boolean isQualified = hasLegalFirstName && hasLegalLastName && hasLegalAgeAndState &&
                              hasPassedGPA && hasPassedTestScore && !hasFelonies;

        if (!isQualified && !currRejectReason.isEmpty()) {
            student.setRejectReasons(currRejectReason);
        }
        return isQualified;
    }

    private static boolean checkLegalName(String name) {
        return checkFirstLetterUpperCase(name.charAt(0)) && checkRestLetterLowerCase(name.substring(1));
    }
    
    private static boolean checkFirstLetterUpperCase(char firstChar) {
        int ascii = (int)firstChar;
        if (ascii >= 65 && ascii <= 90) {
            return true;
        }
        if (!currRejectReason.contains(REJECTION_INVALID_FIRST_LETTER)) {
            currRejectReason.add(REJECTION_INVALID_FIRST_LETTER);
        }
        return false;
    }
    
    private static boolean checkRestLetterLowerCase(String name) {
        if (name.equals(name.toLowerCase())) {
            return true;
        }
        if (!currRejectReason.contains(REJECTION_INVALID_REST_LETTERS)) {
            currRejectReason.add(REJECTION_INVALID_REST_LETTERS);
        }
        return false;
    }

    private static boolean checkAgeAndState(int age, String state) {
        boolean isInStateStudent = state.equals(STATE);
        if (age < 0) {
            currRejectReason.add("The age of the applicant should not be negative.\n");
        } else if ((isInStateStudent && age >= 17 && age < 26) || (!isInStateStudent && age > 80)) {
            return true;
        }
        return false;
    }

    private static boolean checkGPA(double gpa) {
        if (gpa >= ACCEPT_GPA) {
            return true;
        } else if (gpa < REJECT_GPA) {
            currRejectReason.add(REJECTION_LOW_GPA);
        }
        return false;
    }

    private static boolean checkTestScore(Optional<Integer> scoreSAT, Optional<Integer> scoreACT) {
        Preconditions.checkArgument(scoreSAT.isPresent() || scoreACT.isPresent(), "At least one of the SAT or ACT score must be provided.");
        return ((scoreSAT.isPresent() && scoreSAT.get() > ACCEPT_SAT_SCORE) ||
                (scoreACT.isPresent() && scoreACT.get() > ACCEPT_ACT_SCORE));
    }

    private static boolean checkHasFelonies(boolean hasFelony) {
        if (hasFelony) {
            currRejectReason.add(REJECTION_HAS_FELONY);
            return true;
        }
        return false;
    }

}
