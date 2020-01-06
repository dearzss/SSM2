package test;
import com.dearzss.service.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestSpring {

    @Test
    public void run1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:config/spring/applicationContext.xml");
        AccountService accountService = (AccountService)applicationContext.getBean("accountService");
        accountService.findAll();
    }
}
