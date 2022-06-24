package link.karurisuro.peopledirect.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String imgUrl;
    @Column(length = 500)
    private String bio;
    private String role;
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_id")
    private List<Contact> contacts = new ArrayList<>();

}
