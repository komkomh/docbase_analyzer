package com.kmt.docbase_analyzer.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Post(
    @Id
    val id: Int,
    val title: String,
    @Column(nullable = true, columnDefinition = "TEXT")
    val body: String?,
    val createdAt: LocalDateTime,
    @Column(nullable = true)
    val tags: String?,
    val userName: String,
    val starsCount: Int,
    val goodJobs: Int
): Serializable
