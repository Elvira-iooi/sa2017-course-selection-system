DROP TABLE student IF EXISTS;


CREATE TABLE  students (
  id            BIGINT IDENTITY   PRIMARY  KEY,
  studentname   VARCHAR(80),
  department    VARCHAR(80),
  grade         VARCHAR(80),
  usual_grade   INT,
  design_grade  INT,
  exam_grade    INT
)