package com.kmt.docbase_analyzer

import com.kmt.docbase_analyzer.service.PostsAnalyzer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class DocbaseAnalyzerApplication

fun main(args: Array<String>) {
    val context = runApplication<DocbaseAnalyzerApplication>(*args)
//    val postsGetter = context.getBean(PostsGetter::class.java)
//    postsGetter.run()

    val postsAnalyzer = context.getBean(PostsAnalyzer::class.java)
    postsAnalyzer.run()
}
