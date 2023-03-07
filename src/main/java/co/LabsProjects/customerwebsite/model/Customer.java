package co.LabsProjects.customerwebsite.model;

import lombok.*;
import org.aspectj.bridge.IMessage;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@Builder
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Full name must have a value")
    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    @NotNull(message = "Email address must have a value")
    @NotBlank(message = "Email address must not be blank")
    private String emailAddress;

    @NotNull(message = "Address must have a value")
    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotNull(message = "Age must have a value")
    @Min(value = 18, message = "Age must be greater than 18")
    @Max(value = 999, message = "Age must be less than 999")
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "subscription_id",
            referencedColumnName = "id")
    private Subscription subscription;
}