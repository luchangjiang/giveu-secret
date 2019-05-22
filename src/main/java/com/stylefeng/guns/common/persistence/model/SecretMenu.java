package com.stylefeng.guns.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2017-06-01
 */
public class SecretMenu extends Model<SecretMenu> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 菜单编号
     */
	private String menuCode;
    /**
     * 菜单父编号
     */
	private String pcode;
    /**
     * 菜单名称
     */
	private String menuName;
    /**
     * 菜单图标
     */
	private String icon;
    /**
     * url地址
     */
	private String url;
    /**
     * 菜单排序号
     */
	private Integer num;
    /**
     * 菜单层级
     */
	private Integer levels;
    /**
     * 是否是菜单(1:是  0:不是(为按钮))
     */
	private Integer ismenu;
    /**
     * 备注
     */
	private String tips;
    /**
     * 菜单状态 :  1:启用   0:不启用
     */
	private Integer menuStatus;
    /**
     * 是否打开:    1:打开   0:不打开
     */
	private Integer isopen;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public Integer getIsmenu() {
		return ismenu;
	}

	public void setIsmenu(Integer ismenu) {
		this.ismenu = ismenu;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(Integer menuStatus) {
		this.menuStatus = menuStatus;
	}

	public Integer getIsopen() {
		return isopen;
	}

	public void setIsopen(Integer isopen) {
		this.isopen = isopen;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
