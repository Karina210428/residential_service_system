package web.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Заголовок новости не задан")
    @Column(name = "heading")
    private String heading;

    @NotEmpty(message = "Новость не написана")
    @Column(name = "text", length = 4096)
    private String text;

    @Column(name = "publicationDate")
    private LocalDateTime publicationDate;

    public String publicationDateString(){
        return publicationDate.getYear() + "-" + publicationDate.getMonthValue() + "-" + publicationDate.getDayOfMonth()+
                " " + publicationDate.getHour()+ ":"+ publicationDate.getMinute();
    }
}
