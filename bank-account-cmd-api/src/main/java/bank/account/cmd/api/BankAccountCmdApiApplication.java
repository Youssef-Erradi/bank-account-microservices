package bank.account.cmd.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import bank.account.core.config.AxonConfig;

@SpringBootApplication
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Import({AxonConfig.class})
public class BankAccountCmdApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountCmdApiApplication.class, args);
	}

}
