package net.javaguides.springboot.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.model.Task;
import net.javaguides.springboot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/tasks")
    public List<Task> getTasks(){
        return taskRepository.findAll();
    }
    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task){
        return taskRepository.save(task);
    }
    @PostMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id){
    Task task =taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"+id));
    return ResponseEntity.ok(task);
    }
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTaskById(@PathVariable long id, @RequestBody Task taskDetails){
        Task task = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"+id));
        task.setTask_name(taskDetails.getTask_name());
        task.setTask_type(taskDetails.getTask_type());
        task.setTask_content(taskDetails.getTask_content());
        task.setStart_date(taskDetails.getStart_date());
        task.setEnd_date(taskDetails.getEnd_date());
        task.setTask_completeness(taskDetails.getTask_completeness());
        Task updateTask = taskRepository.save(task);
        return ResponseEntity.ok(updateTask);
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteTaskById(@PathVariable long id){
        Task task = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found"+id));
        taskRepository.delete(task);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/employees/{employeeId}/tasks/{taskId}")
    public ResponseEntity<HttpStatus>deleteTaskFromEmployee(@PathVariable(value = "employeeId") Long employeeId, @PathVariable(value = "taskId") Long taskId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Not Found Employee" + employeeId));
        employee.removeTask(taskId);
        employeeRepository.save(employee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/employees/{employeeId}/tasks")
    public ResponseEntity<List<Task>> getAllTasksByEmployeeId(@PathVariable(value = "employeeId") Long employeeId){
        if(!employeeRepository.existsById(employeeId)){
            throw new ResourceNotFoundException("Not Found Employee with id"+employeeId);
        }
        List<Task> tasks = taskRepository.findTasksByEmployeeId(employeeId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/employees/{employeeId}/tasks")
    public ResponseEntity<List<Employee>> getAllEmployeesByTaskId(@PathVariable(value = "taskId") Long taskId){
        if(!taskRepository.existsById(taskId)){
            throw new ResourceNotFoundException("Not Found Task with id" + taskId);
        }
        List<Employee> employees = employeeRepository.findEmployeesByTaskId(taskId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping("/employees/{employeeId}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable(value = "employeeId") Long employeeId, @RequestBody Task taskRequest){
        Task task = employeeRepository.findById(employeeId).map(employee -> {long taskId = taskRequest.getId();
        if(taskId != 0L){
            Task _task = taskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException("Not Found Task with id" + taskId));
            employee.addTask(_task);
            employeeRepository.save(employee);
            return _task;
        }
        employee.addTask(taskRequest);
        return taskRepository.save(taskRequest);
    }).orElseThrow(()->new ResourceNotFoundException("Not Found Employee with id" + employeeId));
        return new ResponseEntity<>(task,HttpStatus.CREATED);
    }
}
