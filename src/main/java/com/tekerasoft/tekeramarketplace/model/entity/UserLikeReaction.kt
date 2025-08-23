package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.LikeState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_product_reactions")
data class UserLikeReaction(
    @ManyToOne
    val user: User,

    @ManyToOne
    val product: Product,

    @Enumerated(EnumType.STRING)
    var state: LikeState = LikeState.EMPTY
): BaseEntity() {
    constructor() : this(User(), Product(), LikeState.EMPTY)
}
