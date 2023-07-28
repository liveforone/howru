package howru.howru.reply.domain

import howru.howru.comments.domain.Comments
import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import howru.howru.post.domain.Post
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ReplyTest {

    @Test
    fun editContentTest() {
        //given
        val writer = Member.create("test_reply@gmail.com", "1111", Role.MEMBER)
        val post = Post.create(writer, "test_post")
        val comments = Comments.create(writer, post, "test_comments_content")
        val content = "test reply content"
        val reply = Reply.create(writer, comments, content)

        //when
        val updatedContent = "updated content"
        reply.editContent(updatedContent)

        //then
        Assertions.assertThat(reply.content).isEqualTo(updatedContent)
    }
}