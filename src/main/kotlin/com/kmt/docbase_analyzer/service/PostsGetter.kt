package com.kmt.docbase_analyzer.service

import com.kmt.docbase_analyzer.entity.Post
import com.kmt.docbase_analyzer.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class PostsGetter {

    private val restTemplate = RestTemplate()

    @Autowired
    lateinit var postRepository: PostRepository

    fun run() {
        var page = 0
        while(getPosts(++page)) {
            println("read page${page}")
        }
    }

    fun getPosts(page: Int):Boolean {
        val url = "https://api.docbase.io/teams/currentia/posts/?per_page=100&page=${page}"
        val headers = HttpHeaders()
        headers.set("X-DocBaseToken", "ひみつ")
        val entity = HttpEntity<String>(headers)
        val jsonMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map::class.java)
        val posts = jsonMap.body!!.get("posts")!! as List<Map<String, Object>>
        if (posts.isEmpty()) {
            return false
        }
        posts.forEach {
            println("id = ${it.get("id")!! as Int}")
            val createdAt = LocalDateTime.parse(it.get("created_at") as String,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+09:00"))
            val tags = (it.get("tags") as List<Map<String, Object>>)
                    .map { it.get("name") as String }
                    .takeIf { it.isNotEmpty() }
                    ?.reduce { left, name -> "${left},${name}" }
                    ?: ""
            val userName = (it.get("user") as Map<String, Object>).get("name") as String
            val post = Post(it.get("id")!! as Int,
                    it.get("title")!! as String,
                    it.get("body") as String,
                    createdAt,
                    tags,
                    userName,
                    it.get("stars_count") as Int,
                    it.get("good_jobs_count") as Int)
            postRepository.save(post)
        }
        return true
    }
}
