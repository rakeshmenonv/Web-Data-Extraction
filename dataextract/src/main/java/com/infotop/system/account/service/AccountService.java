package com.infotop.system.account.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Tree;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Collections3;
import org.springside.modules.utils.Encodes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infotop.common.log.BusinessLogger;
import com.infotop.system.account.entity.Role;
import com.infotop.system.account.entity.User;
import com.infotop.system.account.entity.UserPage;
import com.infotop.system.account.repository.RoleDao;
import com.infotop.system.account.repository.UserDao;
import com.infotop.system.account.service.ShiroDbRealm.ShiroUser;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class AccountService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory
			.getLogger(AccountService.class);
	private BusinessLogger businessLogger;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;

	/**
	 * 通过Id获得User信息
	 * 
	 * @param id
	 * @return User
	 * @exception
	 */
	public User getUser(Long id) {
		return userDao.findOne(id);
	}

	/**
	 * 用户批量删除
	 * 
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public void deleteUser(List<Long> ids) {
		List<User> test = (List<User>) this.userDao.findAll(ids);
		if (Collections3.isNotEmpty(test)) {
			for (User user : test) {
				if (isSupervisor(user)) {
					logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
					throw new ServiceException("不能删除超级管理员用户");
				} else {
					userDao.delete(user);
				}
			}
		}
		Map logData = Maps.newHashMap();
		logData.put("用户ID列表", ids);
		businessLogger.log("USER", "删除", getCurrentUserName(), logData);
	}

	/**
	 * 获得所有User信息
	 * 
	 * @return List<User>
	 * @exception
	 */
	public List<User> getAllUser() {
		return (List<User>) userDao.findAll();
	}

	/**
	 * 带条件的分页查询
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<User>
	 * @exception
	 */
	public Page<User> getAllUser(Map<String, Object> filterParams,
			int pageNumber, int pageSize, String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<User> spec = buildSpecification(filterParams);
		return userDao.findAll(spec, pageRequest);
	}

	/**
	 * 通过登录名获得User信息
	 * 
	 * @param loginName
	 * @return User
	 * @exception
	 */
	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	/**
	 * 用户注册
	 * @param user
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional(readOnly = false)
	public void registerUser(User user) throws NoSuchAlgorithmException {
		entryptPassword(user);
		userDao.save(user);
		Map logData = Maps.newHashMap();
		logData.put("user", user);
		businessLogger.log("USER", "注册", getCurrentUserName(), logData);
	}

	@Transactional(readOnly = false)
	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			try {
				entryptPassword(user);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		userDao.save(user);
		Map logData = Maps.newHashMap();
		logData.put("用户ID", user.getId());
		businessLogger.log("USER", "更新", getCurrentUserName(), logData);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
			throw new ServiceException("不能删除超级管理员用户");
		} else {
			userDao.delete(id);
		}
		Map logData = Maps.newHashMap();
		logData.put("用户ID", id);
		businessLogger.log("USER", "删除", getCurrentUserName(), logData);
	}


	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(User user) {
		return (user.getId() != null && user.getId() == 1L);
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	private void entryptPassword(User user) throws NoSuchAlgorithmException {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));
		// MessageDigest md=MessageDigest.getInstance("MD5");
		// byte[] hashPassword = md.digest(user.getPlainPassword().getBytes());
		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(),
				salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));

	}

	public Role getRole(Long id) {
		return roleDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveRole(Role entity) {
		roleDao.save(entity);
		Map logData = Maps.newHashMap();
		logData.put("角色", entity);
		businessLogger.log("ROLE", "保存", getCurrentUserName(), logData);
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteWithReference(id);
		roleDao.delete(id);
		Map logData = Maps.newHashMap();
		logData.put("角色ID", id);
		businessLogger.log("ROLE", "删除", getCurrentUserName(), logData);
	}

	@Transactional(readOnly = false)
	public void deleteRole(List<Long> ids) {
		if (ids != null && ids.size() > 0) {
			for (long id : ids) {
				if (id == 1) {
					logger.warn("操作员{}尝试删除系统保留角色", getCurrentUserName());
					throw new ServiceException("不能删除系统保留角色");
				} else {
					deleteRole(id);
				}
			}
		}
		Map logData = Maps.newHashMap();
		logData.put("角色ID列表", ids);
		businessLogger.log("ROLE", "批量删除", getCurrentUserName(), logData);
	}

	public List<Role> getAllRole() {
		return (List<Role>) this.roleDao.findAll();
	}

	public Page<Role> getAllRole(Map<String, Object> filterParams,
			int pageNumber, int pageSize, String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		return roleDao.findAll(pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType, String order) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			if ("asc".equalsIgnoreCase(order)) {
				sort = new Sort(Direction.ASC, "id");
			} else {
				sort = new Sort(Direction.DESC, "id");
			}
		} else {
			if ("asc".equalsIgnoreCase(order)) {
				sort = new Sort(Direction.ASC, sortType);
			} else {
				sort = new Sort(Direction.DESC, sortType);
			}
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<User> buildSpecification(
			Map<String, Object> filterParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
		Specification<User> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), User.class);
		return spec;
	}

	private Specification<Role> buildSpecificationRole(
			Map<String, Object> filterParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
		Specification<Role> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Role.class);
		return spec;
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}

	public List<Role> getRoleByRoleType(String value) {
		List<Role> roleList = roleDao.findRoleByRoleType(value);
		return roleList;
	}

	public DataGrid dataGridUser(Map<String, Object> searchParams,
			int pageNumber, int rows, String sortType, String order) {
		Page<User> page = getAllUser(searchParams, pageNumber, rows, sortType,
				order);
		List<User> temp = page.getContent();
		List<UserPage> tempUserPage = Lists.newArrayList();
		for (User user : temp) {
			UserPage up = new UserPage();
			BeanMapper.copy(user, up);
			tempUserPage.add(up);
		}

		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(tempUserPage);
		return dataGrid;
	}

	public List<Tree> roleTree() {
		List<Role> l = (List<Role>) roleDao.findAll();
		List<Tree> lt = Lists.newArrayList();
		if (l != null && l.size() > 0) {
			for (Role t : l) {
				Tree tree = new Tree();
				tree.setId(t.getId() + "");
				tree.setText(t.getName());
				tree.setState("open");
				tree.setIconCls("status_online");
				if (t.getPid() > 0) {
					tree.setPid(t.getPid() + "");
				}
				lt.add(tree);
			}
		}
		return lt;
	}

	public DataGrid roleDataGrid(Map<String, Object> searchParams,
			int pageNumber, int rows, String sortType, String order) {
		Page<Role> page = getAllRole(searchParams, pageNumber, rows, sortType,
				order);
		/*
		 * List<Role> roles=page.getContent(); List<Tree> lt =
		 * Lists.newArrayList(); if (roles != null && roles.size() > 0) { for
		 * (Role p : roles) { Map<String, String> other = Maps.newHashMap();
		 * Tree tree = new Tree(); tree.setId(p.getId() + "");
		 * tree.setPid(p.getPid() + ""); if (p.getPid()>0) {
		 * tree.set_parentId(p.getPid()+""); } tree.setText(p.getName());
		 * tree.setIconCls("status_online"); tree.setAttributes(other);
		 * lt.add(tree); } }
		 */
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(page.getContent());
		return dataGrid;
	}

	public List<User> findUserByUserType(int i) {
		return userDao.findByUserType(i);
	}	
	
	@Autowired
	public void setBusinessLogger(BusinessLogger businessLogger) {
		this.businessLogger = businessLogger;
	}
}
