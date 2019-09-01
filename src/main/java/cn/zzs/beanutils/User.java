package cn.zzs.beanutils;

import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户实体类
 * @author: zzs
 * @date: 2019年9月1日 下午12:29:41
 */
public class User {
	private Integer id;
	private String name;
	private Integer age;
	private Date gmt_create;
	private Date gmt_modified;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getGmt_create() {
		return gmt_create;
	}
	public void setGmt_create(Date gmt_create) {
		this.gmt_create = gmt_create;
	}
	public Date getGmt_modified() {
		return gmt_modified;
	}
	public void setGmt_modified(Date gmt_modified) {
		this.gmt_modified = gmt_modified;
	}
	public User(Integer id, String name, Integer age, Date gmt_create, Date gmt_modified) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.gmt_create = gmt_create;
		this.gmt_modified = gmt_modified;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", gmt_create=" + gmt_create + ", gmt_modified="
				+ gmt_modified + "]";
	}
	
}
