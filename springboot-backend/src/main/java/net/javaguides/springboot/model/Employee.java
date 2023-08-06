package net.javaguides.springboot.model;

import jakarta.persistence.*;
//import org.springframework.scheduling.config.Task;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String emailId;
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinTable(name = "employee_task", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
	private Set<Task> tasks = new HashSet<>();

	public Employee() {
		
	}
	
	public Employee(String firstName, String lastName, String emailId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public void addTask(Task task){
		this.tasks.add(task);
		task.getEmployees().add(this);
	}
	public void removeTask(long taskId){
		Task task1 = this.tasks.stream().filter(t->t.getId() == taskId).findFirst().orElse(null);
		if(task1!=null){
			this.tasks.remove(task1);
			task1.getEmployees().remove(this);
		}

	}
	public Set<Task> getTasks(){
		return tasks;
	}
	public void setTasks(Set<Task> tasks){
		this.tasks = tasks;

	}
}
