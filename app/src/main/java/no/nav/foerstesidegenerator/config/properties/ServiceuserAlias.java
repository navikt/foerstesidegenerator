package no.nav.foerstesidegenerator.config.properties;

//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
//@Getter
//@Setter
//@ToString
@ConfigurationProperties("serviceuser")
@Validated
public class ServiceuserAlias {
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.***passord=gammelt_passord***;
	}

	@Override
	public String toString() {
		return "ServiceuserAlias{" +
				"username='" + username + '\'' +
				", ***passord=gammelt_passord*** + '\'' +
				'}';
	}
}
