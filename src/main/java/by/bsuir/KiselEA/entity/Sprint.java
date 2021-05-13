package by.bsuir.KiselEA.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Table(name = "sprints")
@Getter
@Setter
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "project_id")
    private Project project;

    @NotBlank(message = "Не может быть пустым")
    private String name;
    private Boolean isDone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sprint", fetch = FetchType.EAGER)
    private Collection<Task> tasks;
}
