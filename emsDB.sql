-- Drop the existing tables
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS employeedepartment;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS account;

-- Create the account table
CREATE TABLE account (
  accountid SERIAL PRIMARY KEY,
  firstname VARCHAR NOT NULL,
  lastname VARCHAR,
  email VARCHAR(50) NOT NULL UNIQUE,
  password TEXT NOT NULL,
  role VARCHAR(20),
  adminRequestPending BOOLEAN
);

-- Create the employee table
CREATE TABLE employee (
  employeeid SERIAL PRIMARY KEY,
  accountid INT UNIQUE,
  FOREIGN KEY (accountid) REFERENCES account (accountid) ON DELETE CASCADE
);

-- Create the department table
CREATE TABLE department (
  departmentid SERIAL PRIMARY KEY,
  departmentname VARCHAR NOT NULL UNIQUE
);

-- Create the employeedepartment table
CREATE TABLE employeedepartment (
  employeeid INT,
  departmentid INT,
  assignmentdate DATE,
  assignmentid SERIAL PRIMARY KEY,
  CONSTRAINT fk_employeedepartment_employeeid FOREIGN KEY (employeeid) REFERENCES "employee" (employeeid) ON DELETE CASCADE,
  CONSTRAINT fk_employeedepartment_departmentid FOREIGN KEY (departmentid) REFERENCES "department" (departmentid) ON DELETE CASCADE
);

-- Create the record table
CREATE TABLE record (
  recordid SERIAL PRIMARY KEY,
  employeeid INT,
  departmentid INT,
  recorddate DATE,
  present BOOLEAN,
  onsite BOOLEAN,
  donesyncupcall BOOLEAN,
  CONSTRAINT fk_record_employee FOREIGN KEY (employeeid) REFERENCES "employee" (employeeid) ON DELETE CASCADE,
    CONSTRAINT fk_record_department FOREIGN KEY (departmentid) REFERENCES "department" (departmentid) ON DELETE CASCADE
);
