package com.changhongit;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;


/**
 * 数据同步
 */
public class SyncDataHbaseObserver extends BaseRegionObserver {

	private static Client client = null;
	
	private static final Log LOG = LogFactory.getLog(SyncDataHbaseObserver.class);
	
	
	@Override
	public void start(CoprocessorEnvironment e) throws IOException {
		// init
		Settings settings = Settings.settingsBuilder().build();
		TransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
				InetAddress.getByName("192.168.2.137"), 9300);
		client = TransportClient.builder().settings(settings).build().addTransportAddress(inetSocketTransportAddress);
	}
	
	@Override
	  public void prePut(final ObserverContext<RegionCoprocessorEnvironment> e, 
	      final Put put, final WALEdit edit, final Durability durability) throws IOException {
		LOG.info("【prePut】");
	  }
	
	@Override
	public void postPut(final ObserverContext<RegionCoprocessorEnvironment> e, final Put put, final WALEdit edit,
			final Durability durability) throws IOException {
		LOG.info("【添加数据了】" + "beign===client=" + client);
		String indexId = new String(put.getRow());
		NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
		Map<String,String> json = new HashMap<String,String>();
		for(Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()){
			for(Cell cell : entry.getValue()){
				json.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
			}
		}
		LOG.info("【添加数据了】" + ESUtils.toJson(json));
		IndexResponse response = client.prepareIndex("changhongit", "employee").setId(indexId).setSource(ESUtils.toJson(json)).execute().actionGet();
		LOG.info("【添加完数据getVersion】" + response.getVersion());
	}
	
	
}
