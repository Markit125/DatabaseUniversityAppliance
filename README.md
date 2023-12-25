# DatabaseUniversityAppliance

Appliance Form screenshots:
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/6c78c916-7543-421a-91f1-2f7bfbb5bf39)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/77388254-b248-4658-8c7d-cfef5ac8ef33)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/a87ebdff-f34a-481a-a4bb-1d2158a0163c)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/0e83561f-0d19-4ad8-842d-832c7fd787ff)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/3ad698e0-17e2-43d3-88f2-6a09e97aa595)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/35957fee-dbf2-4b48-8aab-33fb67ddcb48)

In downloads folder (in project) appears after successful submission:
![image_2023-12-22_23-59-24](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/151cf937-0f58-4516-8171-75401b8deba1)





Appliance viewer screenshots:
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/f51e59b3-5bbf-4ece-aa6d-ee0bf70460bd)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/3b4dc834-d0e6-43f8-8032-35459f25c6ba)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/2fe3af30-cbb9-466c-9dd9-fa1458efc929)
![image](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/4c86cfa2-3d72-4442-b400-3e20e82eca14)



Appliance deleter screenshots:
![image_2023-12-20_21-19-53](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/f0f50c65-1a6c-4d42-9023-ae4c63b5e883)
![image_2023-12-20_21-20-04](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/0b588bac-55d3-491f-ac58-6d057de97ce4)
![image_2023-12-20_21-20-35](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/0ff7df0d-63d1-4746-890c-7f03e40647cf)
![image_2023-12-20_21-20-46](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/53887c4d-1520-4692-8eb7-9b43bb91b090)


Student viewer screenshots:
![image_2023-12-23_00-01-44](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/172ada3c-d9a8-4d61-89b9-372f3baedb39)
When you click save, an html file with the current table appears in the downloads folder (in the project):
![image_2023-12-23_00-02-43](https://github.com/Markit125/DatabaseUniversityAppliance/assets/72036187/0b2c6c4f-2421-4c71-b9aa-49673c0ddcf1)


To create database:
sql'''
CREATE TABLE Applience
(
  Applience_id         INTEGER NOT NULL,
  Student_ID           INTEGER NOT NULL,
  Department_ID        INTEGER NULL,
  Exam_subject_1_ID    INTEGER NULL,
  Exam_subject_2_ID    INTEGER NULL,
  Exam_subject_3_ID    INTEGER NULL
);



ALTER TABLE Applience
ADD PRIMARY KEY (Applience_id);

CREATE INDEX XIE1Applience ON Applience
(
  Student_ID
);

CREATE TABLE Department
(
  Department_ID        INTEGER NOT NULL,
  Department_name      VARCHAR(20) NULL,
  Budget_places        INTEGER NULL,
  Paid_places          VARCHAR(20) NULL
  subject_1_id         INTEGER NULL,
  subject_2_id         INTEGER NULL,
  subject_3_id         INTEGER NULL
);

ALTER TABLE Department
ADD PRIMARY KEY (Department_ID);

CREATE UNIQUE INDEX XAK1Department ON Department
(
  Department_name
);

CREATE TABLE Exam_subject
(
  Exam_subject_ID      INTEGER NOT NULL,
  Exam_subject_name    VARCHAR(20) NULL
);

ALTER TABLE Exam_subject
ADD PRIMARY KEY (Exam_subject_ID);



CREATE TABLE Result
(
  Result_ID            INTEGER NOT NULL,
  Result_score         INTEGER NULL,
  Student_ID           INTEGER NOT NULL,
  Exam_subject_ID      INTEGER NOT NULL
);

ALTER TABLE Result
ADD PRIMARY KEY (Result_ID);

CREATE INDEX XIE1Result ON Result
(
  Student_ID
);

CREATE TABLE Student
(
  Student_ID           INTEGER NOT NULL,
  Student_first_name   VARCHAR(20) NULL,
  Student_middle_name  VARCHAR(20) NULL,
  Student_last_name    VARCHAR(20) NULL,
  Birth_date           DATE NULL,
  Achievements         INTEGER NULL,
  Passport             VARCHAR(20) NULL
);

ALTER TABLE Student
ADD PRIMARY KEY (Student_ID);

CREATE UNIQUE INDEX XAK1Student ON Student
(
  Passport
);

ALTER TABLE Applience
ADD FOREIGN KEY R_12 (Student_ID) REFERENCES Student (Student_ID);

ALTER TABLE Applience
ADD FOREIGN KEY R_15 (Department_ID) REFERENCES Department (Department_ID);

ALTER TABLE Applience
ADD FOREIGN KEY R_16 (Exam_subject_1_ID) REFERENCES Exam_subject (Exam_subject_ID);

ALTER TABLE Applience
ADD FOREIGN KEY R_18 (Exam_subject_2_ID) REFERENCES Exam_subject (Exam_subject_ID);

ALTER TABLE Applience
ADD FOREIGN KEY R_19 (Exam_subject_3_ID) REFERENCES Exam_subject (Exam_subject_ID);

ALTER TABLE Result
ADD FOREIGN KEY R_1 (Student_ID) REFERENCES Student (Student_ID);

ALTER TABLE Result
ADD FOREIGN KEY R_2 (Exam_subject_ID) REFERENCES Exam_subject (Exam_subject_ID);
'''
