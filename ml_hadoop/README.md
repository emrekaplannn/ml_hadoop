# Hadoop MapReduce Jobs for Salary Analysis

This project contains Hadoop MapReduce jobs for analyzing machine learning engineer salaries. The jobs compute various statistics such as average salary by job title, experience level, employee residence, and work year.

## Prerequisites

- Hadoop 3.x installed and configured in pseudo-distributed mode
- Java Development Kit (JDK) 8 or later
- HDFS setup and running

## Environment

- Operating System: Ubuntu 20.04
- Hadoop Version: 3.3.1
- JDK Version: OpenJDK 11
- IDE: IntelliJ IDEA / Eclipse

## Job Descriptions

1. **JobTitle**: Computes the average salary for each job title.
2. **TitleExperience**: Computes the average salary for each job title and experience level combination.
3. **EmployeeResidence**: Computes the average salary for US and non-US employee residences.
4. **AverageYear**: Computes the average salary for different work year partitions.

## Setup

1. Ensure Hadoop is running in pseudo-distributed mode.
2. Place the input dataset (salaries.csv) in HDFS.

# Start Hadoop services
start-dfs.sh
start-yarn.sh

# Create directory in HDFS and upload the dataset
hdfs dfs -mkdir -p /user/emrekaplan/data
hdfs dfs -put /path/to/your/local/salaries.csv /user/emrekaplan/data/

# Compile JobTitle
hadoop com.sun.tools.javac.Main TotalSalary.java
jar cf TotalSalary.jar TotalSalary*.class

# Compile JobTitle
hadoop com.sun.tools.javac.Main JobTitle.java
jar cf JobTitle.jar JobTitle*.class

# Compile TitleExperience
hadoop com.sun.tools.javac.Main TitleExperience.java
jar cf TitleExperience.jar TitleExperience*.class

# Compile EmployeeResidence
hadoop com.sun.tools.javac.Main EmployeeResidence.java
jar cf EmployeeResidence.jar EmployeeResidence*.class

# Compile AverageYear
hadoop com.sun.tools.javac.Main AverageYear.java
jar cf AverageYear.jar AverageYear*.class

Running the Jobs

Run each MapReduce job with the corresponding input and output paths.

# Run Total Salary
hadoop jar TotalSalary.jar TotalSalary /user/emrekaplan/data/salaries.csv /user/emrekaplan/output_total

# Run JobTitle
hadoop jar JobTitle.jar JobTitle /user/emrekaplan/data/salaries.csv /user/emrekaplan/output_jobtitle

# Run TitleExperience
hadoop jar TitleExperience.jar TitleExperience /user/emrekaplan/data/salaries.csv /user/emrekaplan/output_titleexperience

# Run EmployeeResidence
hadoop jar EmployeeResidence.jar EmployeeResidence /user/emrekaplan/data/salaries.csv /user/emrekaplan/output_employeeresidence

# Run AverageYear
hadoop jar AverageYear.jar AverageYear /user/emrekaplan/data/salaries.csv /user/emrekaplan/output_averageyear
