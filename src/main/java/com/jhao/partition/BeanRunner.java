package com.jhao.partition;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.FileSystem;

public class BeanRunner {

	static class BeanMapper extends Mapper<LongWritable, Text, Text, Bean> {

		public Bean tx = new Bean();

		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();

			String[] fields = StringUtils.split(line, "\t");

			String timestamp = fields[0];

			String ip = fields[1];

			Text domain = new Text(fields[4]);

			tx.set(timestamp, ip);

			context.write(domain, tx);

		}

	}

	static class BeanReduce extends Reducer<Text, Bean, Text, Bean> {

		protected void reduce(Text key, Iterable<Bean> values, Context context)
				throws IOException, InterruptedException {

			for (Bean bean : values) {
				context.write(key, bean);
			}

		}

	}

	public static void main(String[] args) throws Exception, IOException,
			InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(BeanRunner.class);
		job.setMapperClass(BeanMapper.class);
		job.setReducerClass(BeanReduce.class);
		job.setPartitionerClass(ProvincePartitioner.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Bean.class);
		String inputPath = "/usr/local/mapreduce/data";
		String outputPath = "/usr/local/mapreduce/Partitioner";

		Path path = new Path(outputPath);
		FileSystem fileSystem = path.getFileSystem(conf);
		if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
		}

		FileInputFormat.setInputPaths(job, new Path(inputPath));

		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		job.waitForCompletion(true);
	}

}
