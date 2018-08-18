package com.jhao.wordCount;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCountRunner {

	static class WordCountMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {

		int count = 0;

		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();

			String[] fields = StringUtils.split(line, "\t");

			Text domain = new Text(fields[4]);

			context.write(new Text(domain), new LongWritable(1));

		}
	}

	static class WordCountReducer extends
			Reducer<Text, LongWritable, Text, LongWritable> {

		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {

			long counter = 0;

			for (LongWritable value : values) {

				counter += value.get();

			}

			context.write(key, new LongWritable(counter));
		}

	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {


		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Job job = Job.getInstance(conf);

		job.setJarByClass(WordCountRunner.class);
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setNumReduceTasks(1); 

		String inputPath = "/usr/local/mapreduce/data";
		String outputPath = "/usr/local/mapreduce/wordcount";
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
