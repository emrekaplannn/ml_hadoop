import csv

input_file = 'data/salaries.csv'
output_file = 'out/input.tsv'

with open(input_file, 'r') as csvfile, open(output_file, 'w') as tsvfile:
    csv_reader = csv.reader(csvfile)
    tsv_writer = csv.writer(tsvfile, delimiter='\t')
    
    for row in csv_reader:
        tsv_writer.writerow(row)
