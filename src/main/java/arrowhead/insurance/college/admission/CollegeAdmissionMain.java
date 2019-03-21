package arrowhead.insurance.college.admission;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
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
            InputStream excelInputStream = new FileInputStream(inputExcelPath);
            excelUtility.evaluateApplicantsAndWriteResults(excelInputStream);
        }
    }
}
