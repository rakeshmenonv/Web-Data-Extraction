package com.infotop.webharvest.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.infotop.util.DateTimeUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infotop.webharvest.dataharvest.service.DataHarvestService;
import com.infotop.webharvest.pageurlinfo.entity.Pageurlinfo;
import com.infotop.webharvest.pageurlinfo.service.PageurlinfoService;

/**
 * A synchronous worker
 */
@Component("syncWorker")
public class SyncWorker implements Worker {

	@Autowired
	private PageurlinfoService pageurlinfoService;

	protected static Logger logger = Logger.getLogger("service");
	@Autowired
	private DataHarvestService dataHarvestService;
	public void work() {
		String threadName = Thread.currentThread().getName();
		logger.debug("   " + threadName + " has began working.");
		System.out.println("   " + threadName + " has began working.");
		try {
			Calendar calendarFirstDayOfTheMonth = Calendar.getInstance();
			calendarFirstDayOfTheMonth.set(Calendar.DAY_OF_MONTH, 1);
			Calendar calendarFirstDayOfTheYear = Calendar.getInstance();
			calendarFirstDayOfTheYear.set(Calendar.DAY_OF_YEAR, 1);
			Calendar cal = Calendar.getInstance();
			
			
			List<Pageurlinfo> listPageurlinfo = pageurlinfoService.getPageurlinfoByjobon("day");
			System.out.println("list size with dayonly "+listPageurlinfo.size());
			if(cal.equals(calendarFirstDayOfTheMonth)){
				List<Pageurlinfo> listPageurlinfoforyear = pageurlinfoService.getPageurlinfoByjobon("month");
				listPageurlinfo.addAll(listPageurlinfoforyear);
	    	}
			System.out.println("list size after month "+listPageurlinfo.size());
			if(cal.equals(calendarFirstDayOfTheYear)){
				List<Pageurlinfo> listPageurlinfoformonth = pageurlinfoService.getPageurlinfoByjobon("year");
				listPageurlinfo.addAll(listPageurlinfoformonth);
	    	}
			System.out.println("list size after year "+listPageurlinfo.size());
			for (Pageurlinfo pageurlinfo : listPageurlinfo) {
				System.out.println("in loop ;) "+pageurlinfo.getUrl());
				pageurlinfo.setId(null);
				pageurlinfo.setJobon(null);
				pageurlinfo.setExtractedDate(DateTimeUtil.nowTimeStr());
				pageurlinfoService.save(pageurlinfo);
				if (pageurlinfo.getElement().isEmpty()) {
					dataHarvestService.basicsave(pageurlinfo);
				} else {
					dataHarvestService.selectedsave(pageurlinfo);
				}
			}
			logger.debug("working...");
			System.out.println("working...");
//			Thread.sleep(10000); // simulates work
		} catch ( Exception e) {
			e.printStackTrace();
		}
		logger.debug("   " + threadName + " has completed work.");
		System.out.println("   " + threadName + " has completed work.");
	}

}
