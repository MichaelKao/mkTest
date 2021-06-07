package tw.musemodel.dingzhiqingren.model;

import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author p@musemodel.tw
 */
public class Activated {

	@NotEmpty
	@NotNull
	private UUID identifier;

	@NotEmpty
	@NotNull
	private String shadow;

	public Activated() {
	}

	@Override
	public String toString() {
		return new StringBuilder("{").
			append("\"identifier\":").append(Objects.isNull(identifier) ? "null" : "\"" + identifier + "\"").append(",").
			append("\"shadow\":").append(Objects.isNull(shadow) ? "null" : "\"" + shadow + "\"").append("}").
			toString();
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public String getShadow() {
		return shadow;
	}

	public void setShadow(String shadow) {
		this.shadow = shadow;
	}
}
