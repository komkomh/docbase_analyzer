package com.kmt.docbase_analyzer.service


import com.atilika.kuromoji.TokenizerBase
import com.atilika.kuromoji.ipadic.Tokenizer
import com.kmt.docbase_analyzer.entity.ScoredBody
import com.kmt.docbase_analyzer.entity.ScoredTagName
import com.kmt.docbase_analyzer.entity.ScoredUserName
import com.kmt.docbase_analyzer.repository.PostRepository
import com.kmt.docbase_analyzer.repository.ScoredBodyRepository
import com.kmt.docbase_analyzer.repository.ScoredTagNameRepository
import com.kmt.docbase_analyzer.repository.ScoredUserNameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PostsAnalyzer {

    val tokenizer = Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build()

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var scoredBodyRepository: ScoredBodyRepository

    @Autowired
    lateinit var scoredTagNameRepository: ScoredTagNameRepository

    @Autowired
    lateinit var scoredUserNameRepository: ScoredUserNameRepository

    fun run() {
//        val post = postRepository.findById(880427).get()
        postRepository.findAll().forEachIndexed { index, post ->
            println("index = ${index} id = ${post.id}")
            val score = post.goodJobs             // いいね = スコア(点数)
            scoreBody(score, post.title)          // タイトルをスコア付け
            if (post.body != null) {
                scoreBody(score, post.body)       // 内容をスコア付け
            }
            if (post.tags != null && post.tags.isNotEmpty()) {
                scoreTagName(score, post.tags)    // タグ名をスコア付け
            }
            scoreUserName(score, post.userName)   // ユーザ名をスコア付け
        }

//        val tokens = tokenizer.tokenize("第2回Vueランチ 2019.09.02(月)");
//        tokens.filter { it.partOfSpeechLevel1 == "名詞" } .forEach {
//            println("---------------")
//            println(it.surface)
//            println("all=${it.allFeatures}")
//            println("reading=${it.reading}")
//            println("baseForm=${it.baseForm}")
//            println("partOfSpeechLevel1=${it.partOfSpeechLevel1}")
//            println("partOfSpeechLevel2=${it.partOfSpeechLevel2}")
//            println("partOfSpeechLevel3=${it.partOfSpeechLevel3}")
//            println("partOfSpeechLevel4=${it.partOfSpeechLevel4}")
//        }
    }

    fun scoreBody(score: Int, body: String) {
        val tokens = tokenizer.tokenize(body)
                .filter { it.partOfSpeechLevel1 == "名詞" }
                .mapNotNull { it.surface.toLowerCase() }
                .distinct()
        tokens.forEach {
            val token = when (it.length > 191) {
                true -> it.substring(0, 191)
                false -> it
            }
            val currentScore = scoredBodyRepository.findByIdOrNull(token)?.let { it.score } ?: 0
            scoredBodyRepository.save(ScoredBody(token, currentScore + score))
        }
    }

    fun scoreTagName(score: Int, tagNames: String) {
        tagNames.split(",").forEach {
            val currentScore = scoredTagNameRepository.findByIdOrNull(it)?.let { it.score } ?: 0
            scoredTagNameRepository.save(ScoredTagName(it, currentScore + score))
        }
    }

    fun scoreUserName(score: Int, userName: String) {
        val currentScore = scoredUserNameRepository.findByIdOrNull(userName)?.let { it.score } ?: 0
        scoredUserNameRepository.save(ScoredUserName(userName, currentScore + score))
    }
}
