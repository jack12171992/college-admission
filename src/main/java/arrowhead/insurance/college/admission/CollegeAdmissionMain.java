package arrowhead.insurance.college.admission;

import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * This purpose of this class is to be the entry point of the application, where
 * the user starts up the application and can provide an Excel file that contains
 * the students' records and the result will be written into the same Excel file.
 */
public class CollegeAdmissionMain 
{
    public static void main( String[] args ) throws InvalidFormatException, IOException
    {
        Scanner scanner = new Scanner(System.in);
        ExcelUtility excelUtility = new ExcelUtility();
        System.out.println("Please enter an Excel file that contains records of college applicants:");
        while (scanner.hasNextLine()) {
            String inputExcelPath = scanner.nextLine();
            excelUtility.evaluateApplicantsAndWriteResults(inputExcelPath);
            System.out.println("Please enter another excel file: ");
        }
    }
}
