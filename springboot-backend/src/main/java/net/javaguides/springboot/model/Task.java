package net.javaguides.springboot.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "task_name")
    private String task_name;
    @Column(name = "task_type")
    private String task_type;
    @Column(name = "task_content")
    private String task_content;
    @Column(name = "start_date")
    private Date start_date;
    @Column(name = "end_date")
    private Date end_date;
    @Column(name = "task_completeness")
    private int task_completeness;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tasks")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

    public void Task(String task_name, String task_type, String task_content, Date start_date, Date end_date){
        this.task_name = task_name;
        this.task_type = task_type;
        this.task_content = task_content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.task_completeness = 0;
    }
    public void setTask_name(String name){
        this.task_name = name;
    }
    public String getTask_name(){
        return this.task_name;
    }
    public void setTask_type(String type){
        this.task_type = type;
    }
    public String getTask_type(){
        return this.task_type;
    }
    public void setTask_content(String content){
        this.task_content = content;
    }
    public String getTask_content(){
        return this.task_content;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    public  Date getStart_date(){
        return this.start_date;
    }
    public void setEnd_date(Date end_date){
        this.end_date = end_date;
    }
    public Date getEnd_date(){
        return this.end_date;
    }
    public void setTask_completeness(int completeness){
        this.task_completeness = completeness;
    }
    public int getTask_completeness(){
        return this.task_completeness;
    }
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public Set<Employee> getEmployees(){
        return this.employees;
    }
    public void setEmployees(Set<Employee> employees){
        this.employees = employees;
    }

}
