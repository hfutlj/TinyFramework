package tiny.framework.test;

import tiny.framework.annotation.Bean;
import tiny.framework.annotation.Inject;

@Bean
public class TestBean {
	
	@Inject
	public InjectTest injectTest;
	
	public void show() {
		injectTest.show();
	}

}
