package sirius;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sirius.mybatis.mapper.DictionaryMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:conf/spring/applicationContext.xml",
		"classpath:conf/springmvc/servlet-context.xml" })
public class Test {

	@Resource
	private DictionaryMapper dictionaryMapper;

	@org.junit.Test
	public void text() {

		System.out.println("1111111");

		
	}

}
