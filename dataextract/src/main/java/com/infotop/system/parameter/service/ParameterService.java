package com.infotop.system.parameter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.infotop.web.easyui.DataGrid;
import net.infotop.web.easyui.Tree;

import org.apache.commons.lang3.StringUtils;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infotop.system.parameter.entity.Parameter;
import com.infotop.system.parameter.repository.ParameterDao;

@Component
@Transactional(readOnly = true)
public class ParameterService {

	@Autowired
	private ParameterDao parameterDao;

	public List<Tree> parameterTree(Map<String, Object> filterParams) {
		List<Tree> treeList = Lists.newArrayList();
		Specification<Parameter> spec = buildSpecification(filterParams);
		List<Parameter> pl = parameterDao.findAll(spec, new Sort(Direction.ASC,
				"sort"));
		if (pl != null && pl.size() > 0) {
			for (Parameter p : pl) {
				Tree tree = new Tree();
				tree.setId(p.getId() + "");
				if (p.getParentId() > 0) {
					tree.setPid(p.getParentId() + "");
					tree.set_parentId(p.getParentId() + "");
				}
				tree.setText(p.getName());
				tree.setIconCls("status_online");
				tree.setState("closed");
				tree.setValue(p.getUuid());
				Map<String, String> attribute = Maps.newHashMap();
				attribute.put("category", p.getCategory());
				attribute.put("subcategory", p.getSubcategory());
				attribute.put("name", p.getName());
				attribute.put("value", p.getValue());
				attribute.put("remark", p.getRemark());
				attribute.put("sort", String.valueOf(p.getSort()));
				attribute.put("parentId", String.valueOf(p.getParentId()));
				tree.setAttributes(attribute);
				treeList.add(tree);
			}
		}
		return treeList;
	}

	/**
	 * 根据参数类型获得参数树
	 * 
	 * @param category
	 * @return
	 */
	public List<Tree> categoryParameter(String category) {
		List<Tree> treeList = Lists.newArrayList();
		Map<String, Object> filterParams = new HashMap<String, Object>();
		filterParams.put("EQ_category", category);
		Specification<Parameter> spec = buildSpecification(filterParams);
		List<Parameter> pl = parameterDao.findAll(spec, new Sort(Direction.ASC,
				"sort"));
		if (pl != null && pl.size() > 0) {
			for (Parameter p : pl) {
				Tree tree = new Tree();
				tree.setId(p.getId() + "");
				if (p.getParentId() > 0) {
					tree.setPid(p.getParentId() + "");
					tree.set_parentId(p.getParentId() + "");
				}
				tree.setText(p.getName());
				tree.setIconCls("status_online");
				tree.setState("open");
				tree.setValue(p.getUuid());
				Map<String, String> attribute = Maps.newHashMap();
				attribute.put("category", p.getCategory());
				attribute.put("name", p.getName());
				attribute.put("value", p.getUuid());
				attribute.put("remark", p.getRemark());
				attribute.put("sort", String.valueOf(p.getSort()));
				attribute.put("parentId", String.valueOf(p.getParentId()));
				tree.setAttributes(attribute);
				treeList.add(tree);
			}
		}
		return treeList;
	}

	/**
	 * 根据参数类型获得参数树
	 * 
	 * @param category
	 * @return
	 */
	public List<Tree> categoryParametersecond(String second) {
		List<Tree> treeList = Lists.newArrayList();
		List<Parameter> pl = parameterDao.findByCategoryAndSubcategory(
				"industry_promotion_office", second, new Sort(Direction.ASC,
						"sort"));
		if (pl != null && pl.size() > 0) {
			for (Parameter p : pl) {
				Tree tree = new Tree();
				tree.setId(p.getId() + "");
				if (p.getParentId() > 0) {
					tree.setPid(p.getParentId() + "");
					tree.set_parentId(p.getParentId() + "");
				}
				tree.setText(p.getName());
				tree.setIconCls("status_online");
				tree.setState("open");
				tree.setValue(p.getUuid());
				Map<String, String> attribute = Maps.newHashMap();
				attribute.put("category", p.getCategory());
				attribute.put("name", p.getName());
				attribute.put("value", p.getUuid());
				attribute.put("remark", p.getRemark());
				attribute.put("sort", String.valueOf(p.getSort()));
				attribute.put("parentId", String.valueOf(p.getParentId()));
				tree.setAttributes(attribute);
				treeList.add(tree);
			}
		}
		return treeList;
	}

	/**
	 * 项目分类树
	 * 
	 * @return
	 */
	public List<Tree> projectCategoryTree() {
		List<Tree> treeList = Lists.newArrayList();
		Map<String, Object> filterParams = new HashMap<String, Object>();
		Map<String, String> attribute = Maps.newHashMap();
		// 根节点
		Tree node = new Tree();
		node.setId("0");
		node.setText("统计分类");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "root");// 分类名称
		attribute.put("value", "root"); // 分类
		attribute.put("search", "0"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);
		// 按实施阶段分类
		node = new Tree();
		node.setId("phase");
		node.setPid("0");
		node.setText("实施阶段");
		node.setState("closed");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "phase"); // 分类
		attribute.put("search", "0"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("phase0");
		node.setPid("phase");
		node.setText("策划");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "0"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("phase1");
		node.setPid("phase");
		node.setText("洽谈");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "1"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("phase2");
		node.setPid("phase");
		node.setText("签约");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "2"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("phase3");
		node.setPid("phase");
		node.setText("开工");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "3"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("phase4");
		node.setPid("phase");
		node.setText("竣工");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "phase");// 分类名称
		attribute.put("value", "4"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		// 按项目来源分类
		node = new Tree();
		node.setId("type");
		node.setPid("0");
		node.setText("项目来源");
		node.setState("closed");
		attribute = Maps.newHashMap();
		attribute.put("category", "type");// 分类名称
		attribute.put("value", "type"); // 分类
		attribute.put("search", "0"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("type2");
		node.setPid("type");
		node.setText("招商引资");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "type");// 分类名称
		attribute.put("value", "2"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		node = new Tree();
		node.setId("type3");
		node.setPid("type");
		node.setText("市内项目");
		node.setState("open");
		attribute = Maps.newHashMap();
		attribute.put("category", "type");// 分类名称
		attribute.put("value", "3"); // 分类
		attribute.put("search", "1"); // 是否按此节点查询
		node.setAttributes(attribute);
		treeList.add(node);

		// 按发展主体分类
		filterParams.clear();
		filterParams.put("EQ_category", "county");
		Specification<Parameter> spec1 = buildSpecification(filterParams);
		List<Parameter> countyList = parameterDao.findAll(spec1, new Sort(
				Direction.ASC, "sort"));
		if (countyList != null && countyList.size() > 0) {
			for (Parameter p : countyList) {
				node = new Tree();
				node.setId(p.getId() + "");
				node.setText(p.getName());
				node.setPid(p.getParentId() + "");
				if (p.getParentId() == 0) {
					node.setState("closed");
					attribute = Maps.newHashMap();
					attribute.put("category", "county");// 分类名称
					attribute.put("value", "county"); // 分类
					attribute.put("search", "0"); // 是否按此节点查询
					node.setAttributes(attribute);
				} else {
					node.setState("open");
					attribute = Maps.newHashMap();
					attribute.put("category", "county");// 分类名称
					attribute.put("value", p.getId() + ""); // 分类
					attribute.put("search", "1"); // 是否按此节点查询
					node.setAttributes(attribute);
				}
				treeList.add(node);
			}
		}

		// 按推进办分类
		filterParams.clear();
		filterParams.put("EQ_category", "industry_promotion_office");
		Specification<Parameter> spec2 = buildSpecification(filterParams);
		List<Parameter> officeList = parameterDao.findAll(spec2, new Sort(
				Direction.ASC, "sort"));
		if (officeList != null && officeList.size() > 0) {
			for (Parameter p : officeList) {
				if ("industry_promotion_office_third"
						.equals(p.getSubcategory())) {
					node = new Tree();
					node.setId(p.getId() + "");
					node.setPid(p.getParentId() + "");
					node.setText(p.getName());
					node.setState("open");
					attribute = Maps.newHashMap();
					attribute.put("category", "team");// 分类名称
					attribute.put("value", p.getId() + ""); // 分类
					attribute.put("search", "1"); // 是否按此节点查询
					node.setAttributes(attribute);
				} else if ("industry_promotion_office_second".equals(p
						.getSubcategory())) {
					node = new Tree();
					node.setId(p.getId() + "");
					node.setPid(p.getParentId() + "");
					node.setText(p.getName());
					node.setState("closed");
					attribute = Maps.newHashMap();
					attribute.put("category", "office");// 分类名称
					attribute.put("value", p.getId() + ""); // 分类
					attribute.put("search", "1"); // 是否按此节点查询
					node.setAttributes(attribute);

				} else {
					node = new Tree();
					node.setId(p.getId() + "");
					node.setPid(p.getParentId() + "");
					node.setText(p.getName());
					node.setState("closed");
					attribute = Maps.newHashMap();
					attribute.put("category", "office");// 分类名称
					attribute.put("value", p.getId() + ""); // 分类
					attribute.put("search", "0"); // 是否按此节点查询
					node.setAttributes(attribute);

				}
				treeList.add(node);

			}
		}

		return treeList;
	}

	/**
	 * 根据条件查询数据记录
	 * 
	 * @param value
	 * @return
	 */
	public Long getCount(Map<String, Object> filterParams) {
		Specification<Parameter> spec = buildSpecification(filterParams);
		return parameterDao.count(spec);
	}

	public Parameter get(Long id) {
		return this.parameterDao.findOne(id);
	}

	public Parameter get(String category, Long id) {
		return this.parameterDao.findParameterByCategoryAndId(category, id);
	}

	public Parameter get(String category, String subcategory, Long id) {
		return this.parameterDao.findParameterByCategoryAndSubcategoryAndId(
				category, subcategory, id);
	}

	public Parameter get(String value) {
		return this.parameterDao.findParameterByValue(value);
	}

	@Transactional(readOnly = false)
	public void save(Parameter entity) {
		this.parameterDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ids) {
		String[] infoArray = ids.split(",");
		if (infoArray != null && infoArray.length > 0) {
			for (String id : infoArray) {
				if (StringUtils.isNotBlank(id))
					parameterDao.remove(Long.parseLong(id));
			}
		}
	}

	public List<Parameter> getParameters(List<Long> ids) {
		List<Parameter> parameterList = (List<Parameter>) this.parameterDao
				.findAll(ids);
		return parameterList;
	}

	public Parameter getParameterByUuid(String uuid) {
		return parameterDao.findByUuid(uuid);
	}

	public List<Parameter> getParameterByPID(String pid) {
		return parameterDao
				.findByParentId(pid, new Sort(Direction.ASC, "sort"));
	}

	public List<Parameter> getParameterByCategory(String category) {
		return parameterDao.findByCategory(category, new Sort(Direction.ASC,
				"sort"));
	}

	public List<Parameter> getParameterByCategoryAndSubcategory(
			String category, String subcategory) {
		return parameterDao.findByCategoryAndSubcategory(category, subcategory,
				new Sort(Direction.ASC, "sort"));
	}

	public List<Parameter> getParameterByCategoryAndParentId(String category,
			Long parentid) {
		return parameterDao.findByCategoryAndParentId(category, parentid,
				new Sort(Direction.ASC, "sort"));
	}

	public Parameter getParameterByValue(String value) {
		return parameterDao.findParameterByValue(value);
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		this.parameterDao.remove(id);
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
	public Page<Parameter> getAllParameter(Map<String, Object> filterParams,
			int pageNumber, int pageSize, String sortType, String order) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType, order);
		Specification<Parameter> spec = buildSpecification(filterParams);
		return parameterDao.findAll(spec, pageRequest);
	}

	private Specification<Parameter> buildSpecification(
			Map<String, Object> filterParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(filterParams);
		Specification<Parameter> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Parameter.class);
		return spec;
	}

	public DataGrid dataGrid(Map<String, Object> searchParams, int pageNumber,
			int rows, String sortType, String order) {
		Page<Parameter> page = getAllParameter(searchParams, pageNumber, rows,
				sortType, order);
		List<Tree> lt = Lists.newArrayList();
		List<Parameter> parameters = page.getContent();
		if (parameters != null && parameters.size() > 0) {
			for (Parameter p : parameters) {
				Map<String, String> attribute = Maps.newHashMap();
				Tree tree = new Tree();
				tree.setId(p.getId() + "");
				tree.setPid(p.getParentId() + "");
				if (p.getParentId() != 0) {
					tree.set_parentId(p.getParentId() + "");
				}
				tree.setText(p.getName());
				tree.setIconCls("status_online");
				attribute.put("category", p.getCategory());
				attribute.put("subcategory", p.getSubcategory());
				attribute.put("name", p.getName());
				attribute.put("value", p.getValue());
				attribute.put("remark", p.getRemark());
				attribute.put("sort", String.valueOf(p.getSort()));
				attribute.put("parentId", String.valueOf(p.getParentId()));
				tree.setAttributes(attribute);
				lt.add(tree);
			}
		}
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(page.getTotalElements());
		dataGrid.setRows(lt);
		return dataGrid;
	}

	public List<Tree> parameterTree() {
		List<Parameter> l = (List<Parameter>) parameterDao.findAll();
		List<Tree> lt = Lists.newArrayList();
		if (l != null && l.size() > 0) {
			for (Parameter t : l) {
				Tree tree = new Tree();
				tree.setId(t.getId() + "");
				tree.setText(t.getName());
				tree.setState("open");
				tree.setIconCls("status_online");
				tree.setPid(t.getParentId() + "");
				lt.add(tree);
			}
		}
		return lt;
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

	@Transactional(readOnly = false)
	public void delete(List<Long> ids) {
		List<Parameter> test = (List<Parameter>) this.parameterDao.findAll(ids);
		this.parameterDao.delete(test);
	}

	public Parameter getParameterByValueAndCategory(String reportUnit,
			String category) {
		return parameterDao.findByValueAndCategory(reportUnit, category);
	}

	public String getParameterNameByUuid(String uuid) {
		return parameterDao.findNameByUuid(uuid);
	}

	/*
	 * public List<Parameter> getParameterBySubCategory(String subcategory) {
	 * return parameterDao.findBySubCategory(subcategory, new Sort(
	 * Direction.ASC, "sort")); }
	 */

}
