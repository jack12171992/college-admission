package arrowhead.insurance.college.admission;

import com.google.common.collect.Sets;

import arrowhead.insurance.college.admission.Student.StudentBuilder;

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

public class ExcelUtility {

    private final int NUMBER_OF_STUDENT_INFO = 8;
    private final DataFormatter DATA_FORMATTER = new DataFormatter();
    private Set<Student> students = Sets.newHashSet();
    private Workbook workbook;

    protected void evaluateApplicantsAndWriteResults(InputStream is) throws InvalidFormatException, IOException {
        students = getStudentsInfo(is, students);
        for (Student student : students) {
            int sheetNum = student.getExcelSheet();
            Sheet sheet = workbook.getSheetAt(sheetNum);
            boolean isQualified = QualificationVerifier.verifyStudentQualification(student);
            writeResult(sheet, isQualified, student);
        }
        workbook.close();
    }

    private Set<Student> getStudentsInfo(InputStream is, Set<Student> students) throws IOException, InvalidFormatException{
        workbook = WorkbookFactory.create(is);
        boolean isHeaderRow = true;
        int sheetNum = 0;
        for (Sheet sheet : workbook) {          //Iterating through each individual sheet in the excel file
            sheet = workbook.getSheetAt(sheetNum);
            int rowNum = 0;
            for (Row row : sheet) {
                if (isHeaderRow) {
                    isHeaderRow = false;
                } else {
                    Student student = createStudentFromExcelRow(row, sheetNum, rowNum);
                    if (students.add(student)) {
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
                .setExcelSheet(sheetNum)
                .setExcelSheetRow(rowNum);
        for (Cell cell : row) {
            String cellValue = DATA_FORMATTER.formatCellValue(cell);
            switch (numCell) {
                case 0 : 
                    builder.setFirstName(cellValue);
                    break;
                case 1 :
                    builder.setLastName(cellValue);
                    break;
                case 2 :
                    builder.setState(cellValue);
                    break;
                case 3 :
                    builder.setAge(Integer.parseInt(cellValue));
                    break;
                case 4 :  
                    String[] results = cellValue.split("/");
                    double gpa = Double.parseDouble(results[0]);
                    double gpaScale = Double.parseDouble(results[1]);
                    builder.setGPA((gpa / gpaScale) * 100);
                    break;
                case 5 :
                    builder.setScoreSAT(Integer.parseInt(cellValue));
                    break;
                case 6 :
                    builder.setScoreACT(Integer.parseInt(cellValue));
                    break;
                case 7 :
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
        Cell resultCell = row.createCell(NUMBER_OF_STUDENT_INFO);
        boolean noRejectReasons = student.getRejectReasons().isEmpty();
        resultCell.setCellValue(getResult(isQualified, noRejectReasons));
        if (isQualified) {
            Cell rejectReasonsCell = row.createCell(NUMBER_OF_STUDENT_INFO + 1);
            rejectReasonsCell.setCellValue(getRejectReasons(student.getRejectReasons()));
        }
    }

    private String getResult(boolean isQualified, boolean noRejectReaons) {
        if (isQualified) {
            return "instant accpet";
        }
        return (noRejectReaons) ? "further review" : "instant reject";
    }

    private String getRejectReasons(List<String> rejectReasons) {
        StringBuilder sb = new StringBuilder();
        int numReasons = rejectReasons.size();
        boolean firstLine = true;
        for (int i = 0; i < numReasons; i++) {
            if (!firstLine) {
                sb.append("\n");
            }
            sb.append(rejectReasons.get(i));
            firstLine = false;
        }
        return sb.toString();
    }

}
