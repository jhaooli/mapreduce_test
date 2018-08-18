package com.jhao.mySolr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Bean implements WritableComparable<Bean> {

	private long Width;
	private long timestamp;

	public Bean() {
	}


	public Bean(long Width, long timestamp) {
		this.set(Width, timestamp);
	}

	public void set(long Width, long timestamp) {
		this.Width = Width;
		this.timestamp = timestamp;
	}

	public long getWidth() {
		return Width;
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(Width);
		out.writeLong(timestamp);

	}


	public void readFields(DataInput in) throws IOException {
		Width = in.readLong();
		timestamp = in.readLong();
	}

	public int compareTo(Bean Key) {
		long min = Width - Key.Width;
		if (min != 0) {
			return (int) min;
		} else {
			return (int) (timestamp - Key.timestamp);
		}
	}
}
