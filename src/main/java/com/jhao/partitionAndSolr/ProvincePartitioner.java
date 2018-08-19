package com.jhao.partitionAndSolr;

import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Bean, Text> {

	private static HashMap<String, Integer> provinceMap = new HashMap<String, Integer>();


	public int getPartition(Bean key, Text value, int numPartitions) {

		Integer province = null;
		province = provinceMap.get(key.getDomain().toString());

		if (province == null) {
			province = 1;
		}

		return province;
	}

}