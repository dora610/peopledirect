package link.karurisuro.peopledirect.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String userName;
    private String designation;
    @Column(unique = true, nullable = false)
    private String email;
    private String imgUrl;
    @Column(length = 1000)
    private String description;
    private String phone;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user_id;
}
