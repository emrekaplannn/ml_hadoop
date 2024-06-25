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

public class AverageYear {
    public static class AverageYearMapper extends Mapper<Object, Text, Text, LongWritable> {
        private boolean isHeader = true;

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            if (isHeader) {
                isHeader = false; // Skip the header row
                return;
            }
            String[] fields = value.toString().split(",");
            String workYear;
            int year = Integer.parseInt(fields[0]);
            if (year == 2024) {
                workYear = "2024";
            } else if (year == 2023) {
                workYear = "2023";
            } else {
                workYear = "Before 2023";
            }
            long salary = Long.parseLong(fields[4]);
            context.write(new Text(workYear), new LongWritable(salary));
        }
    }

    public static class AverageYearReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
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
        Job job = Job.getInstance(conf, "Average Salary by Work Year");
        job.setJarByClass(AverageYear.class);
        job.setMapperClass(AverageYearMapper.class);
        job.setCombinerClass(AverageYearReducer.class);
        job.setReducerClass(AverageYearReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
