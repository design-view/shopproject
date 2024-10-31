package org.green.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.green.shop.constant.Role;

import java.security.PrivateKey;

@Entity
@Table(name="member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@SequenceGenerator(name="member_seq", sequenceName = "member_seq",allocationSize = 1)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "member_seq")
    @Column(name="member_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
}
