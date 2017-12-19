package teistermask.bindingModel;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TaskBindingModel {
	@NotBlank
	@Size(min = 0)
    private String title;

	@NotBlank
	@Size(min = 0)
	private String status;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
