package com.kmt.docbase_analyzer.repository

import com.kmt.docbase_analyzer.entity.ScoredBody
import org.springframework.data.jpa.repository.JpaRepository

interface ScoredBodyRepository : JpaRepository<ScoredBody, String> {}
