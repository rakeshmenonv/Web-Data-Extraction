package com.infotop.webharvest.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.infotop.util.DateTimeUtil;
import net.infotop.web.easyui.Message;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infotop.webharvest.dataharvest.service.DataHarvestService;
import com.infotop.webharvest.pagedatainfo.entity.Pagedatainfo;
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
 List<Pageurlinfo> listPageurlinfos =pageurlinfoService.findByJobonNotNull();
			 
			 for (Pageurlinfo pageurlinfo :listPageurlinfos){
				 
				
						 if ( pageurlinfo.getNextScheduleOn()== null){
							 pageurlinfo.setNextScheduleOn(pageurlinfo.getJobon());
							 
						 }
						 try{
							 int schedulingInt   = Integer.parseInt(pageurlinfo.getNextScheduleOn());
							 
							 if (schedulingInt==0){
								 	pageurlinfo.setNextScheduleOn(pageurlinfo.getJobon());
									pageurlinfoService.save(pageurlinfo);
								 
								 pageurlinfo.setId(null);
									pageurlinfo.setJobon(null);
									pageurlinfo.setNextScheduleOn(null);
									pageurlinfo.setExtractedDate(DateTimeUtil.nowTimeStr());
									System.out.println("in loop ;) "+pageurlinfo.getUrl());
									pageurlinfoService.save(pageurlinfo);
									if (pageurlinfo.getElement().isEmpty()) {
										dataHarvestService.basicsave(pageurlinfo);
									} else {
									dataHarvestService.selectedsave(pageurlinfo);
									}
							 }else{
								 schedulingInt = schedulingInt-1;
								 pageurlinfo.setNextScheduleOn(String.valueOf(schedulingInt));
							 }
							 pageurlinfoService.save(pageurlinfo);
						 }catch(Exception e){
							 e.printStackTrace();
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
