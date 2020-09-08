package web.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "request")
@EqualsAndHashCode(exclude={"occupant", "employee", "specialization"})
@ToString(exclude={"occupant", "employee", "specialization"})
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "request_id")
    private Integer id;

    @NotEmpty(message = "Проблема не указана")
    @Column(name = "problem")
    private String problem;

    @Column(name = "done")
    private Boolean isDone;

    @Column(name = "confirm")
    private Boolean confirm;

    @Column(name = "dateOfRequest")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfRequest;

    @Column(name = "dateOfDeadline")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfDeadline;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "occupant_id")
    private Occupant occupant;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
}