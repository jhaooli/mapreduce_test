package com.jhao.partitionAndSolr;

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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BeanRunnerPartitionsolr {

	static class BeanMapper extends Mapper<LongWritable, Text, Bean, Text> {

		public Bean bean = new Bean();

		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();

			String[] fields = StringUtils.split(line, "\t");

			String timestamp = fields[0];

			String ip = fields[1];

			String domain = fields[4];

			String Width = fields[5];

			Text tx = new Text(timestamp + " \t " + ip);

			bean.set(domain, Long.parseLong(Width));

			context.write(bean, tx);

		}

	}

	static class BeanReduce extends Reducer<Bean, Text, Bean, Text> {

		protected void reduce(Bean key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			for (Text bean : values) {
				context.write(key, bean);
			}

		}

	}

	public static void main(String[] args) throws Exception, IOException,
			InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(BeanRunnerPartitionsolr.class);
		job.setMapperClass(BeanMapper.class);
		job.setReducerClass(BeanReduce.class);
		job.setPartitionerClass(ProvincePartitioner.class);
		job.setNumReduceTasks(1);

		job.setOutputKeyClass(Bean.class);
		job.setOutputValueClass(Text.class);

		String inputPath = "/usr/local/mapreduce/data";
		String outputPath = "/usr/local/mapreduce/partitionAndSolr";

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
