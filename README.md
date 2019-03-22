# college-admission

Instruction to use the application:

1.) Clone down the repo by using git clone


2.) Build the artifact by running # "mvn clean install" or the artifact is already included in this repo, which is the college-admisssion.jar file


3.) Navigate to the target folder and run "java -jar college-admission.jar" to start up the application.


4.) Since this is to examine a list of college applicants with several information so I figured that putting all the information into
a Excel file would be a proper to handle this, so the input file should be an Excel file.


5.) An example record file is already included which located at src/test/resources/record.xlsx. After the application is up and running, it will ask the user to provide a Excel file, which in this the user could use the record file that is in the src/test/resources folder.


6.) The result is written in the same excel file and now the user could see the result by opening up the same record file.


Note: 

a.)In order to add additional rules in the future, the user could go into QualificationVerifier.java and add check method and evaluate the statement that is returned in the verifyStudentQualification method. 

b.)The use would also have the ability to add additional evaluated information in the Student.java to the Student and the StudentBuilder class. This application is also capable to read and write an excel file that has more than one sheet.
