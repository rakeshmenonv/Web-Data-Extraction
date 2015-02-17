package com.infotop.system.attachment.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.infotop.web.URLEncode;
import net.infotop.web.easyui.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;

import com.infotop.common.BasicController;
import com.infotop.common.FileUtils;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;
import com.infotop.system.attachment.entity.Attachment;
import com.infotop.system.attachment.service.AttachmentService;

/**
 * SpmissysattachmentAction $Id: SpmissysattachmentAction.java,v 0.0.1 $
 */
@Controller
@RequestMapping(value = "/attachment")
public class AttachmentController extends BasicController {
	@Autowired
	private AttachmentService attachmentService;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String create(@Valid Attachment attachment,
			@RequestParam("files") MultipartFile file,
			HttpServletRequest request) {
		try {
			String path = "";
			String filePath = "";
			// String fileName =
			// FileUtils.generateFileName(file.getOriginalFilename());//重命名
			String fileName = file.getOriginalFilename();
			path = request.getSession().getServletContext().getRealPath("/")
					+ "static/upload/attachment/" + attachment.getRid() + "/";
			filePath = "static/upload/attachment/" + attachment.getRid() + "/";

			if (file.getSize() > 0) {
				FileUtils.uploadFile(file, path, fileName);
				attachment.setFileName(file.getOriginalFilename());
				attachment.setFilePath(filePath + fileName);
			}
			attachmentService.save(attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "true";
	}

	@RequestMapping(value = "download/{uuid}/{id}", method = RequestMethod.GET)
	public String download(@PathVariable(value = "uuid") String uuid,
			@PathVariable(value = "id") Long id,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ServletOutputStream out;
		out = response.getOutputStream();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		String savePath = request.getSession().getServletContext()
				.getRealPath("static/upload/attachment/")
				+ "/" + uuid + "/";
		Attachment attachment = attachmentService.get(id);
		String fileName = attachment.getFileName();
		response.setHeader(
				"Content-disposition",
				"attachment; filename*=UTF-8''"
						+ URLEncode.URLencoding(attachment.getFileName(),
								"UTF-8"));
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(
				savePath + fileName));
		byte[] data = new byte[4 * 1024];
		int bytesRead;
		while ((bytesRead = is.read(data)) != -1) {
			out.write(data, 0, bytesRead);
		}

		return null;
	}

	@RequestMapping(value = "uploadnodb", method = RequestMethod.POST)
	@ResponseBody
	public String createnodb(@Valid Attachment attachment,
			@RequestParam("files") MultipartFile file,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String path = "";
			String filePath = "";
			// String fileName =
			// FileUtils.generateFileName(file.getOriginalFilename());//重命名
			String fileName = file.getOriginalFilename();
			path = request.getSession().getServletContext().getRealPath("/")
					+ "static/upload/attachment/" + attachment.getRid() + "/";
			filePath = "static/upload/attachment/" + attachment.getRid() + "/";

			if (file.getSize() > 0) {
				FileUtils.uploadFile(file, path, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "true";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(@RequestParam(value = "ids") List<Long> ids,
			HttpServletRequest request) throws Exception {
		try {
			ShiroUser su = super.getLoginUser();
			User user = accountService.findUserByLoginName(su.getLoginName());
			if (user != null) {
				attachmentService.delete(ids, request);
				msg.setSuccess(true);
				msg.setMessage("信息删除成功");
				msg.setData("");
			} else {
				logger.log(this.getClass(),Logger.ERROR_INT,"登陆帐号无效!","",null);
				msg.setSuccess(false);
				msg.setMessage("登陆帐号无效!");
				msg.setData("");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			msg.setSuccess(false);
			msg.setMessage(ex.getMessage());
			msg.setData("");

		}
		return msg;
	}

	@RequestMapping(value = "imageview/{projectid}/{rid}", method = RequestMethod.GET)
	public String imageview(@PathVariable(value = "projectid") long projectid,
			@PathVariable(value = "rid") String rid, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		/*
		 * Projectreserve projectreserve = projectreserveService
		 * .getByprojectId(projectid);
		 */
		model.addAttribute("attachmentList", attachmentService.getByRid(rid));
		return "imageviewer/galleryview";
	}

	@RequestMapping(value = "pluploads", method = RequestMethod.POST)
	public void create(
			@Valid Attachment attachment,
			MultipartFile file,
			HttpServletRequest request,
			HttpServletResponse response,
			RedirectAttributes redirectAttributes,
			@RequestParam(required = false, defaultValue = "sample") String name,
			@RequestParam(required = false, defaultValue = "-1") int chunk,
			@RequestParam(required = false, defaultValue = "-1") int chunks)
			throws IOException {
		/* System.out.println("chunk is"+chunk+"chunks is"+chunks); */
		String relativePath = "static/upload/attachment/" + attachment.getRid()
				+ "/";
		String realPath = request.getSession().getServletContext()
				.getRealPath("/");
		File destFile = null;
		try {
			File folder = new File(realPath + relativePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			destFile = new File(folder, name);
			if (chunk == 0 && destFile.exists()) {
				destFile.delete();
				destFile = new File(folder, name);
			}
			appendFile(file.getInputStream(), destFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> m = new HashMap<String, Object>();
		if (chunks == -1 || chunk == chunks - 1) {
			attachment.setFileName(name);
			attachment.setFilePath(relativePath + name);
			attachmentService.save(attachment);
		}
		m.put("status", true);
		m.put("fileUrl", relativePath);
		m.put("name", destFile.getName());
		response.getWriter().write(json.toJson(m));
	}

	private void appendFile(InputStream in, File destFile) {
		int BUFFER_SIZE = 100 * 1024;
		BufferedOutputStream out = null;

		try {
			if (destFile.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(destFile,
						true), BUFFER_SIZE);
			} else {
				out = new BufferedOutputStream(new FileOutputStream(destFile),
						BUFFER_SIZE);
			}
			in = new BufferedInputStream(in, BUFFER_SIZE);
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {

		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}