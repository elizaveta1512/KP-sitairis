package by.bsuir.KiselEA.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @NotBlank(message = "Не может быть пустым")
    private String name;
    private Boolean isDone;
    private String description;
    @Transient
    private long number = -1;

    @ManyToOne
    private Priority priority = new Priority();

    @ManyToOne
    private Employee employee = new Employee();

    @Transient
    private long priorityTemp;
    @Transient
    private long employeeTemp;
}
