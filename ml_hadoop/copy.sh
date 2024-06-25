mkdir -p ~/hadoop_outputs/jobtitle
mkdir -p ~/hadoop_outputs/titleexperience
mkdir -p ~/hadoop_outputs/employeeresidence
mkdir -p ~/hadoop_outputs/averageyear
hdfs dfs -get /user/emrekaplan/output_jobtitle/part-r-00000 ~/hadoop_outputs/jobtitle/jobtitle_output.txt
hdfs dfs -get /user/emrekaplan/output_titleexperience/part-r-00000 ~/hadoop_outputs/titleexperience/titleexperience_output.txt
hdfs dfs -get /user/emrekaplan/output_employeeresidence/part-r-00000 ~/hadoop_outputs/employeeresidence/us_output.txt
hdfs dfs -get /user/emrekaplan/output_employeeresidence/part-r-00001 ~/hadoop_outputs/employeeresidence/non_us_output.txt
hdfs dfs -get /user/emrekaplan/output_averageyear/part-r-00000 ~/hadoop_outputs/averageyear/output_2024.txt
hdfs dfs -get /user/emrekaplan/output_averageyear/part-r-00001 ~/hadoop_outputs/averageyear/output_2023.txt
hdfs dfs -get /user/emrekaplan/output_averageyear/part-r-00002 ~/hadoop_outputs/averageyear/output_before_2023.txt
