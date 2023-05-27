package jp.chocofac.charlie.data.model

sealed class CollectionTags(
    val name: String
) {
    object Posts: CollectionTags("posts")
}
