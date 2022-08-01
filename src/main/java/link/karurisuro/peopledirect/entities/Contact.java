package link.karurisuro.peopledirect.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @NotEmpty(message = "Please enter valid name")
    @Size(min = 2, max = 30, message = "Name should be between 2 to 50 characters long")
    private String name;
    @NotEmpty(message = "Please enter valid user name")
    @Size(min = 5, max = 15, message = "User name should be between 5 to 15 characters long")
    @Column(unique = true, nullable = false, length = 15)
    private String userName;
    @NotEmpty(message = "Please enter valid designation")
    private String designation;
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9+.-]+$")
    @NotEmpty(message = "Invalid email id")
    @Column(unique = true, nullable = false)
    private String email;
    private String imgUrl;
    @Column(length = 5000)
    private String description;
    @NotEmpty(message = "Please enter valid phone number")
    private String phone;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;
}
