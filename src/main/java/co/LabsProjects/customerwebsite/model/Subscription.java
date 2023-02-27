package co.LabsProjects.customerwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue
    private long id;

    @NotNull(message = "A subscription must have a tier")
    @NotBlank(message = "A subscription must have a tier")
    private String tier;

    @NotNull
    @Min(value = 1, message = "A subscription must be at least 1 month")
    private long subscriptionMonths;

    @NotNull
//    @Min(value = 10, message = "A subscription must have a minimum price of 10")
    private double price;

    @OneToOne(mappedBy = "subscription")
    private Customer customer;

    @Override
    public String toString(){
        return tier + " - " + subscriptionMonths + " - $";
    }
}
