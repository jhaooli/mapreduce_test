package com.jhao.partitionAndSolr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Bean implements WritableComparable<Bean> {

	private String domain;
	private long Width;

	public Bean() {
	}


	public Bean(String domain, long Width) {
		this.set(domain, Width);
	}

	public void set(String domain, long Width) {
		this.domain = domain;
		this.Width = Width;
	}

	public String getDomain() {
		return domain;
	}

	public long getWidth() {
		return Width;
	}


	public void write(DataOutput out) throws IOException {
		out.writeUTF(domain);
		out.writeLong(Width);

	}


	public void readFields(DataInput in) throws IOException {
		domain = in.readUTF();
		Width = in.readLong();
	}

	public String toString() {

		return domain + "\t" + Width;
	}

	public int compareTo(Bean Key) {

		return (int) (Width - Key.Width);

	}
}
