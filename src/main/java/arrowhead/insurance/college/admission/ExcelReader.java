package arrowhead.insurance.college.admission;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import arrowhead.insurance.college.admission.Student.StudentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {

    private final File file;
    private final String STATE = "California";
    private List<String> headers = Lists.newArrayList();
    private final DataFormatter DATA_FORMATTER = new DataFormatter();

    private ExcelReader(String fileName) {
        file = new File(fileName);
    }

    protected Set<Student> getStudentsInfo() throws IOException, InvalidFormatException{
        Set<Student> students = Sets.newHashSet();
        Workbook workbook = WorkbookFactory.create(file);
        boolean isHeaderRow = true;
        for (Sheet sheet : workbook) {          //Iterating through each individual sheet in the excel file
            sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (isHeaderRow) {
                    headers = getHeaderRowInfo(row);
                    isHeaderRow = false;
                } else {
                    Student student = createStudentFromExcelRow(row);
                    if (students.add(student)) {
                        throw new UnsupportedOperationException("Should not add the same student more than once.");
                    }
                }
            }
            isHeaderRow = true;
        }
        return students;
    }

    private List<String> getHeaderRowInfo(Row row) {
        List<String> headerCells = Lists.newArrayList();
        for (Cell cell : row) {
            String cellValue = DATA_FORMATTER.formatCellValue(cell);
            headerCells.add(cellValue);
        }
        return headerCells;
    }

    private Student createStudentFromExcelRow(Row row) {
        int numCell = 0;
        StudentBuilder builder = StudentBuilder.newBuilder();
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
                    builder.setIsInStateStudent(cellValue.equals(STATE));
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
}
