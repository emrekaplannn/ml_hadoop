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

public class JobTitle {
    public static class JobTitleMapper extends Mapper<Object, Text, Text, LongWritable> {
        private boolean isHeader = true;

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            if (isHeader) {
                isHeader = false; // Skip the header row
                return;
            }
            String[] fields = value.toString().split(",");
            String jobTitle = fields[3];
            long salary = Long.parseLong(fields[4]);
            context.write(new Text(jobTitle), new LongWritable(salary));
        }
    }

    public static class JobTitleReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
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
        Job job = Job.getInstance(conf, "Average Salary by Job Title");
        job.setJarByClass(JobTitle.class);
        job.setMapperClass(JobTitleMapper.class);
        job.setCombinerClass(JobTitleReducer.class);
        job.setReducerClass(JobTitleReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
