package com.infotop.webharvest.dataharvest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.seagatesoft.sde.DataRecord;
import org.seagatesoft.sde.DataRegion;
import org.seagatesoft.sde.TagTree;
import org.seagatesoft.sde.columnaligner.ColumnAligner;
import org.seagatesoft.sde.columnaligner.PartialTreeAligner;
import org.seagatesoft.sde.datarecordsfinder.DataRecordsFinder;
import org.seagatesoft.sde.datarecordsfinder.MiningDataRecords;
import org.seagatesoft.sde.dataregionsfinder.DataRegionsFinder;
import org.seagatesoft.sde.dataregionsfinder.MiningDataRegions;
import org.seagatesoft.sde.tagtreebuilder.DOMParserTagTreeBuilder;
import org.seagatesoft.sde.tagtreebuilder.TagTreeBuilder;
import org.seagatesoft.sde.treematcher.EnhancedSimpleTreeMatching;
import org.seagatesoft.sde.treematcher.SimpleTreeMatching;
import org.seagatesoft.sde.treematcher.TreeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

@Component
@Transactional(readOnly = true)
public class DataHarvestService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List getPiechartData()
	{
		String sqlStr = "select count(url)  as value, url as name from page_url_info group by url order by  value desc limit 10";
		return jdbcTemplate.queryForList(sqlStr);
	}
	
	public List getPiechartDate(String url)
	{
		
		String sqlStr = "select  count(url)as value, date_format(extracted_date,'%d-%m-%Y') as name  from webharvest.page_url_info where url='"+url+"' group by name";
		return jdbcTemplate.queryForList(sqlStr);
	}
	
}
