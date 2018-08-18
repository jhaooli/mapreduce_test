package com.jhao.partition;

import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Text, Bean> {

	private static HashMap<String, Integer> provinceMap = new HashMap<String, Integer>();


	public int getPartition(Text key, Bean value, int numPartitions) {

		Integer province = null;
		province = provinceMap.get(key.toString());

		if (province == null) {
			province = 1;
		}

		return province;
	}

}