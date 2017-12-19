package teistermask.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teistermask.bindingModel.TaskBindingModel;
import teistermask.entity.Task;
import teistermask.repository.TaskRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TaskController {
	private final TaskRepository taskRepository;

	@Autowired
	public TaskController(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Task> openTasks = taskRepository.findAllByStatus("Open");
		List<Task> inProgressTasks = taskRepository.findAllByStatus("In Progress");
		List<Task> finishedTasks = taskRepository.findAllByStatus("Finished");

		model.addAttribute("view", "task/index");
		model.addAttribute("openTasks", openTasks);
		model.addAttribute("inProgressTasks", inProgressTasks);
		model.addAttribute("finishedTasks", finishedTasks);

		return "base-layout";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view", "task/create");

		return "base-layout";
	}

	@PostMapping("/create")
	public String createProcess(Model model,
								@Valid TaskBindingModel taskBindingModel,
								BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "redirect:/create";
		}

		Task task = new Task();
		task.setStatus(taskBindingModel.getStatus());
		task.setTitle(taskBindingModel.getTitle());

		taskRepository.saveAndFlush(task);

		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable int id) {
		Task task = taskRepository.findOne(id);

		if (task == null) {
			return "redirect:/";
		}

		model.addAttribute("view", "task/edit");
		model.addAttribute("task", task);

		return "base-layout";
	}

	@PostMapping("/edit/{id}")
	public String editProcess(@PathVariable int id,
							  @Valid TaskBindingModel taskBindingModel,
							  BindingResult bindingResult,
							  Model model) {
		Task task = taskRepository.findOne(id);

		if (task == null) {
			return "redirect:/";
		}

		if(bindingResult.hasErrors()) {
			model.addAttribute("view", "task/edit");
			model.addAttribute("task", task);

			return "base-layout";
		}

		task.setTitle(taskBindingModel.getTitle());
		task.setStatus(taskBindingModel.getStatus());

		taskRepository.saveAndFlush(task);

		return "redirect:/";
	}
}
