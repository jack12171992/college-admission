package arrowhead.insurance.college.admission;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import arrowhead.insurance.college.admission.Student.StudentBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * The purpose of this class is to read the Excel file that contains the students' information
 * and write the application status and any reject reasons to the same file
 * @author Jack
 *
 */
public class ExcelUtility {

    private final int NUMBER_OF_STUDENT_INFO = 8;                       //The number of columns of required information from the student
    private final DataFormatter DATA_FORMATTER = new DataFormatter();
    private Set<Student> students = Sets.newHashSet();                  //The collection that contains all the studendts' records
    private Workbook workbook;
    private final String NA = "N/A";

    /**
     * This is the method that takes in the path of the record file and examine the records and write
     * the result to the same file that was provided from the user
     * @param inputExcelPath the path of the input file
     */
    protected void evaluateApplicantsAndWriteResults(String inputExcelPath) throws InvalidFormatException, IOException {
        students = getStudentsInfo(new FileInputStream(inputExcelPath), students);
        for (Student student : students) {
            int sheetNum = student.getExcelSheet();
            Sheet sheet = workbook.getSheetAt(sheetNum);    //Get the correct page of sheet of the excel file
            boolean isQualified = QualificationVerifier.verifyStudentQualification(student);
            writeResult(sheet, isQualified, student);       //Write the result in the correct sheet
        }
        FileOutputStream fileOut = new FileOutputStream(inputExcelPath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private Set<Student> getStudentsInfo(InputStream is, Set<Student> students) throws IOException, InvalidFormatException{
        workbook = WorkbookFactory.create(is);
        boolean isHeaderRow = true;             //The header flag
        int sheetNum = 0;                       //The sheet number in the excel file
        for (Sheet sheet : workbook) {          //Iterating through each individual sheet in the excel file
            sheet = workbook.getSheetAt(sheetNum);
            int rowNum = 0;                     //The row number in the particular excel sheet
            for (Row row : sheet) {
                if (isHeaderRow) {              //If it's the header, ignore it and go on to the next row
                    isHeaderRow = false;
                } else {
                    Student student = createStudentFromExcelRow(row, sheetNum, rowNum);
                    if (!students.add(student)) {   //If exactly same student already existed in the collection, throw an Exception
                        throw new UnsupportedOperationException("Should not add the same student more than once.");
                    }
                }
                rowNum++;
            }
            isHeaderRow = true;
            sheetNum++;
        }
        return students;
    }

    private Student createStudentFromExcelRow(Row row, int sheetNum, int rowNum) {
        int numCell = 0;
        StudentBuilder builder = StudentBuilder.newBuilder()
                .setExcelSheet(sheetNum)                //Save the sheet page and the row number
                .setExcelSheetRow(rowNum);
        for (Cell cell : row) {
            String cellValue = DATA_FORMATTER.formatCellValue(cell);
            //Read the data and assigned them to the corresponding student
            switch (numCell) {
                case 0 : 
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter the first name for the college applicant");
                    }
                    builder.setFirstName(cellValue);
                    break;
                case 1 :
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter the last name for the college applicant");
                    }
                    builder.setLastName(cellValue);
                    break;
                case 2 :
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter the state for the college applicant");
                    }
                    builder.setState(cellValue);
                    break;
                case 3 :
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter age for the college applicant");
                    }
                    builder.setAge(Integer.parseInt(cellValue));
                    break;
                case 4 :
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter the GPA for the college applicant");
                    }
                    String[] results = cellValue.split("/");
                    double gpa = Double.parseDouble(results[0]);
                    double gpaScale = Double.parseDouble(results[1]);
                    builder.setGPA((gpa / gpaScale) * 100);
                    break;
                case 5 :
                    if (!cellValue.equals(NA)) {
                        builder.setScoreSAT(Integer.parseInt(cellValue));
                    }
                    break;
                case 6 :
                    if (!cellValue.equals(NA)) {
                        builder.setScoreACT(Integer.parseInt(cellValue));
                    }
                    break;
                case 7 :
                    if (Strings.isNullOrEmpty(cellValue)) {
                        throw new IllegalArgumentException("Please enter how many felnoies that the college applicant has committed");
                    }
                    int felonies = Integer.parseInt(cellValue);
                    builder.sethasFelony(felonies > 0);
                    break;
                    
            }
            numCell++;
        }
        return builder.build();
    }

    private void writeResult(Sheet sheet, boolean isQualified, Student student) {
        int sheetRow = student.getExcelSheetRow();
        Row row = sheet.getRow(sheetRow);
        Cell resultCell = row.createCell(NUMBER_OF_STUDENT_INFO);                   //Create a cell to write the result
        boolean noRejectReasons = !student.getRejectReasons().isPresent();
        resultCell.setCellValue(getResult(isQualified, noRejectReasons));
        //Write the reject reasons if the student is not instant accept and has reject reasons in another new cell
        if (!isQualified && student.getRejectReasons().isPresent()) {
            Cell rejectReasonsCell = row.createCell(NUMBER_OF_STUDENT_INFO + 1);
            rejectReasonsCell.setCellValue(getRejectReasons(student.getRejectReasons().get()));
        }
    }

    private String getResult(boolean isQualified, boolean noRejectReaons) {
        if (isQualified) {
            return "instant accpet";
        }
        return (noRejectReaons) ? "further review" : "instant reject";
    }

    /**
     * @param rejectReasons the collection that contains all the reject reasons
     * @return All the reject reasons that the student received
     */
    private String getRejectReasons(List<String> rejectReasons) {
        StringBuilder sb = new StringBuilder();
        int numReasons = rejectReasons.size();
        for (int i = 0; i < numReasons; i++) {
            sb.append((i + 1) + ".) " + rejectReasons.get(i) + " ");
        }
        return sb.toString();
    }

}
