package cn.e3mall.item.freemarker;

public class Student {

	private String  name;
	private int age ;
	private long number;
	
	public Student(String name, int age, long number) {
		super();
		this.name = name;
		this.age = age;
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	
	
}
