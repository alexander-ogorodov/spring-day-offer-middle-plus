package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
public class SimpleTaskDistributor implements TaskDistributor {
    @Override
    public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) {
        tasks
            .sort(Comparator.comparing(TaskDTO::getPriority)
                    .thenComparing(Comparator.comparing(TaskDTO::getLeadTime).reversed()));

        int taskNumber = 0;
        // простой но не самый оптимальный алгоритм. первый работник получает самые приоритетные таски
//        for (EmployeeDTO employee : employees) {
//            for (int i = taskNumber; i < tasks.size(); i++) {
//                TaskDTO currentTask = tasks.get(i);
//                if (employee.getTotalLeadTime() + currentTask.getLeadTime() < 420) {
//                    employee.getTasks().add(currentTask);
//                    taskNumber = i + 1;
//                }
//            }
//        }
        // Более оптимальны алгоритм, самые приоритетные таски сразу распределяются между разными сотрудниками
        // дальше добиваются менее приоритетными
        while (taskNumber < tasks.size()) {
            for (EmployeeDTO employee : employees) {
                if (taskNumber >= tasks.size()) {
                    return;
                }
                TaskDTO task = tasks.get(taskNumber);
                if (employee.getTotalLeadTime() + task.getLeadTime() < 420) {
                    employee.getTasks().add(task);
                    taskNumber++;
                } else if (employees.indexOf(employee) == employees.size() - 1) {
                    taskNumber++;
                }
            }
        }
    }
}
