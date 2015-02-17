package com.infotop.system.attachment.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infotop.system.attachment.entity.Attachment;
public interface AttachmentDao extends PagingAndSortingRepository<Attachment, Long>, JpaSpecificationExecutor<Attachment>  {

	List<Attachment> findByRid(String rid);

}