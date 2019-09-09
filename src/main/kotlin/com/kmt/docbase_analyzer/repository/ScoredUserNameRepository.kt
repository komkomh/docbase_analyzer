package com.kmt.docbase_analyzer.repository

import com.kmt.docbase_analyzer.entity.ScoredUserName
import org.springframework.data.jpa.repository.JpaRepository

interface ScoredUserNameRepository : JpaRepository<ScoredUserName, String> {}
