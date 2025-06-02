package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "target_pictures")
open class TargetPicture(
    open var targetPic: String = "",
    open var mindPath: String? = null
): BaseEntity()