package com.odianyun.search.whale.store.bulkload;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TsvImporterMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.odianyun.search.whale.store.hbasestore.HBaseRecord;
import com.odianyun.search.whale.store.hbasestore.json.HbaseJsonConvertor;


public class TsvToHBaseMapper extends TsvImporterMapper {
	public static final String conf_table_name = "table.name";
	
	public static final Logger log = Logger.getLogger(TsvToHBaseMapper.class);
	
	Gson gson= HbaseJsonConvertor.getGson();
	
	Configuration conf;
	
	@Override
	protected void setup(Context context) {
		super.doSetup(context);
		this.conf=context.getConfiguration();
	}

	/**
	 * Convert a line of TSV text into an HBase table row.
	 */
	@Override
	public void map(LongWritable offset, Text value, Context context)
			throws IOException {
		String json=value.toString();
		HBaseRecord hbaseRecord=gson.fromJson(json, HBaseRecord.class);
		hbaseRecord.setTimestamp(conf.getLong("importtsv.timestamp", hbaseRecord.getTimestamp()));
		Put put=hbaseRecord.toPut();
		ImmutableBytesWritable rowKey = new ImmutableBytesWritable(put.getRow());
		try {
			context.write(rowKey, put);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}
}
