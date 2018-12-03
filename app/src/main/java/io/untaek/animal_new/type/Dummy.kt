package io.untaek.animal_new.type

import java.util.*

object Dummy {

    val dbsdlswp = UserDetail(
        User(), 0, 0, 0, hashMapOf()
    )

    val content1 = Content(
        "image/jpg",
        "photo1.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqK9wO9Xt8vYKdLlI2ZnYUjbLLicNwAIEaZEt47joX09DwWL5r",
        600, 800)

    val content2 = Content(
        "image/jpg",
        "photo1.jpg",
        "https://cdn.pixabay.com/photo/2016/02/19/15/46/dog-1210559__340.jpg",
        1000, 700)

    val post1 = Post("postId", User(), "hello", Date(), content1)
    val post2 = Post("postId", User(), "good", Date(), content2)
}