package howru.howru.post.domain

import howru.howru.member.domain.Member
import howru.howru.member.domain.Role
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class PostTest {

    @Test
    fun editContentTest() {
        //given
        val email = "post_edit_test@gmail.com"
        val pw = "1234"
        val writer = Member.create(email, pw, Role.MEMBER)
        val content = "test_content"
        val post = Post.create(writer, content)

        //when
        val updatedContent = "updated_content"
        post.editContent(updatedContent)

        //then
        Assertions.assertThat(post.content).isEqualTo(updatedContent)
    }
}