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

public class TotalSalary {
    public static class SalaryMapper extends Mapper<Object, Text, Text, LongWritable> {
        private final static LongWritable one = new LongWritable(1);
        private Text word = new Text();
        private boolean isHeader = true;
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            if (isHeader) {
                isHeader = false; // Skip the header row
                return;
            }
            String[] fields = value.toString().split(",");
            long salary = Long.parseLong(fields[4]);  // Assuming salary_in_usd is at index 5
            context.write(new Text("total"), new LongWritable(salary));
        }
    }

    public static class SalaryReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }
            context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Total Salary");
        job.setJarByClass(TotalSalary.class);
        job.setMapperClass(SalaryMapper.class);
        job.setCombinerClass(SalaryReducer.class);
        job.setReducerClass(SalaryReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
