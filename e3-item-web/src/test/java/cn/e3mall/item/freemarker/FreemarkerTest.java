package cn.e3mall.item.freemarker;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {

	@Test
	public void genFile() throws Exception {
//		第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
		Configuration configuration=new Configuration(Configuration.getVersion());
//		第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(new  File("D:\\Java\\eclipse-workplace\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
//		第三步：设置模板文件使用的字符集。一般就是utf-8.
		configuration.setDefaultEncoding("utf-8");
//		第四步：加载一个模板，创建一个模板对象。
		Template template=configuration.getTemplate("hello.ftl");
//		第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Student student1 =new Student("张三1", 18, 20171003l);
		Student student2 =new Student("张三2", 18, 20171003l);
		Student student3 =new Student("张三3", 18, 20171003l);
		Student student4 =new Student("张三4", 18, 20171003l);
		Student student5 =new Student("张三5", 18, 20171003l);
		Student student6=new Student("张三6", 18, 20171003l);
		
		List list =new ArrayList<>();
		list.add(student1);
		list.add(student2);
		list.add(student3);
		list.add(student4);
		list.add(student5);
		list.add(student6);
		
		Date date =new  Date();
		
		String nulls=null;
		Map map=new  HashMap<>();
		map.put("hello", "hello-freemarker-test");
		map.put("student", student1);
		map.put("list", list);
		map.put("date", date);
		map.put("nulls", nulls);
//		第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer writer=new FileWriter(new File("G:\\freemakrer_test\\hello.html"));
//		第七步：调用模板对象的process方法输出文件。
		template.process(map, writer);
//		第八步：关闭流。
		writer.close();
	}
}
