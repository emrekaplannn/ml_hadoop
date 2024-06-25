import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class EmployeeResidence {
    public static class EmployeeResidenceMapper extends Mapper<Object, Text, Text, LongWritable> {
        private boolean isHeader = true;

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            if (isHeader) {
                isHeader = false; // Skip the header row
                return;
            }
            String[] fields = value.toString().split(",");
            String residence = fields[7].equals("US") ? "US" : "Non-US";
            long salary = Long.parseLong(fields[4]);
            context.write(new Text(residence), new LongWritable(salary));
        }
    }

    public static class EmployeeResidenceReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            int count = 0;
            for (LongWritable val : values) {
                sum += val.get();
                count++;
            }
            context.write(key, new LongWritable(sum / count));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Average Salary by Employee Residence");
        job.setJarByClass(EmployeeResidence.class);
        job.setMapperClass(EmployeeResidenceMapper.class);
        job.setCombinerClass(EmployeeResidenceReducer.class);
        job.setReducerClass(EmployeeResidenceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
