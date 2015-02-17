package com.infotop.system.attachment.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.infotop.common.FileUtils;
import com.infotop.system.attachment.entity.Attachment;
import com.infotop.system.attachment.repository.AttachmentDao;

/**
 * SpmissysattachmentManager
 * $Id: SpmissysattachmentManager.java,v 0.0.1   $
 */
@Component
@Transactional(readOnly = true)
public class AttachmentService {
	
	
	@Autowired
	private AttachmentDao attachmentDao;
	/**
	 * 保存一个Spmissysattachment，如果保存成功返回该对象的id，否则返回null
	 * @param entity
	 * @return 保存成功的对象的Id
	 */
	@Transactional(readOnly = false)
	public void save(Attachment entity){
		attachmentDao.save(entity);
	}
	
	/**
	 * 根据一个ID得到Spmissysattachment
	 * 
	 * @param id
	 * @return
	 */
	public Attachment get(Long id){
		return attachmentDao.findOne(id);
	}
	
	/**
	 * 根据记录唯一表示获取附件列表
	 * @param rid
	 * @return
	 */
	public List<Attachment> getByRid(String rid){
		return attachmentDao.findByRid(rid);
	}
	
	/**
	 * 删除一个Spmissysattachment
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
    public void delete(Long id) {
        this.attachmentDao.delete(id);
    }
	
	/**
	 * 批量删除附件
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(List<Long> ids,HttpServletRequest request){
		List<Attachment> test = (List<Attachment>) this.attachmentDao.findAll(ids);
		for (Attachment attachment : test) {
			FileUtils.deleteFile(request.getSession().getServletContext().getRealPath("/")+attachment.getFilePath());
			attachmentDao.delete(attachment);
		}
	}
	
	/**
	 * 批量删除附件
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = false)
	public void delete(String rid,HttpServletRequest request){
		List<Attachment> test = attachmentDao.findByRid(rid);
		for (Attachment attachment : test) {
			attachmentDao.delete(attachment);
		}
		FileUtils.DeleteFolder(request.getSession().getServletContext().getRealPath("/")+"static/upload/attachment/"+rid);
	}
	


    public Page<Attachment> getAllSpmissysattachment(Map<String, Object> filterParams, int pageNumber, int pageSize,
            String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Attachment> spec = buildSpecification(filterParams);
        return attachmentDao.findAll(spec, pageRequest);
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Direction.DESC, "id");
        } else if ("name".equals(sortType)) {
            sort = new Sort(Direction.DESC, "name");
        }
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Attachment> buildSpecification(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
        Specification<Attachment> spec = DynamicSpecifications.bySearchFilter(filters.values(), Attachment.class);
        return spec;
    }
    public List<Attachment> getAll() {
		List<Attachment> searchdatalist = (List<Attachment>) attachmentDao.findAll();
		return searchdatalist;
	}
    	
			
			
		

}