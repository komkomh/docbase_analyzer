package com.kmt.docbase_analyzer.repository

import com.kmt.docbase_analyzer.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Int> {}
