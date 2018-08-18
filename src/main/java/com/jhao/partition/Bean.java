package com.jhao.partition;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class Bean implements Writable {

	private String timestamp;
	private String ip;

	public Bean() {
	}

	public Bean(String timestamp, String ip) {
		this.set(timestamp, ip);
	}

	public void set(String timestamp, String ip) {
		this.timestamp = timestamp;
		this.ip = ip;
	}


	public void write(DataOutput out) throws IOException {
		out.writeUTF(timestamp);
		out.writeUTF(ip);
	}


	public void readFields(DataInput in) throws IOException {
		timestamp = in.readUTF();
		ip = in.readUTF();
	}

	public String toString() {

		return timestamp + "\t" + ip;
	}

}
