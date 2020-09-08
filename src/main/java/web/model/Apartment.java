package web.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apartment")
public class Apartment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Номер комнаты не задан")
    @Positive(message = "Номер комнаты должен быть положительным")
    @Column(name = "number")
    private Integer number;

    @NotNull(message = "Номер блока не задан")
    @Positive(message = "Номер блока должен быть положительным")
    @Column(name = "block")
    private Integer block;

    @NotNull(message = "Номер этажа не задан")
    @Positive(message = "Номер этажа должен быть положительным")
    @Column(name = "floor")
    private Integer floor;

    public String toString(){
        return floor + "-" + block + "-" + number;
    }
}
