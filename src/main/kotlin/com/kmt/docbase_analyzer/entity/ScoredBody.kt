package com.kmt.docbase_analyzer.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ScoredBody(
    @Id
    @Column(length = 191)
    val token: String,
    val score: Int
): Serializable
