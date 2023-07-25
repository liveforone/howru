package howru.howru.comments.domain

import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.post.domain.Post
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CommentsTest {

    @Test
    fun editContentTest() {
        //given
        val writer = Member.create("writer_test1@gmail.com", "1111", Role.MEMBER)
        val post = Post.create(writer, "test_post1")
        val content = "test_comments_content"
        val comments = Comments.create(writer, post, content)

        //when
        val updatedContent = "updated_content"
        comments.editContent(updatedContent)

        //then
        Assertions.assertThat(comments.content).isEqualTo(updatedContent)
    }
}