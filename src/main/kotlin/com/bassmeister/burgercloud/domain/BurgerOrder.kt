package com.bassmeister.burgercloud.domain

import javax.persistence.*


@Entity
@Table(name = "OrderedBurgers")
class BurgerOrder(
    @OneToOne
    @JoinColumn(name="burger_id")
    val burger: Burger,
    val amount:Int){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L
}