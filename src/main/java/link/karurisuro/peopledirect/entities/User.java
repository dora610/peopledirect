package link.karurisuro.peopledirect.entities;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @NotEmpty(message = "Please enter valid name")
    @Size(min = 4,max = 50, message = "user name should be between 4 to 50 characters long")
    private String name;
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9+.-]+$")
    @NotEmpty(message = "Invalid email id")
    @Column(unique = true, nullable = false)
    private String email;
    @NotEmpty(message = "Please enter valid password")
    @Size(min = 6, message = "Minimum password length must be 6")
    private String password;
    private String imgUrl;
    @Column(length = 500)
    private String bio;
    private String role;
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_id")
    private List<Contact> contacts = new ArrayList<>();

    @Transient
    @AssertTrue(message = "Terms & conditions are required to be agreed!!")
    private boolean agreement;
}
