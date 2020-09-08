package web.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @NotEmpty(message = "Номер комнаты не задан")
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    private String email;

    @Column(name = "birthday")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "*Please provide your birthday")
    private LocalDate birthday;

    @Column(name = "phone")
    @NotEmpty(message = "*Please provide your phone")
    private String phone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
}
