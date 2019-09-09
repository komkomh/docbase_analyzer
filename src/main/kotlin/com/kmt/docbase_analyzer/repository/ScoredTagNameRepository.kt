package com.kmt.docbase_analyzer.repository

import com.kmt.docbase_analyzer.entity.ScoredTagName
import org.springframework.data.jpa.repository.JpaRepository

interface ScoredTagNameRepository : JpaRepository<ScoredTagName, String> {}
