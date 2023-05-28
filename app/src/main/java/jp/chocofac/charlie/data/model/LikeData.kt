package jp.chocofac.charlie.data.model

data class LikesData(
    val ids: List<String> = listOf(),
) {
    fun count(): Int {
        return ids.count()
    }

    fun isContain(id: String): Boolean {
        return ids.contains(id)
    }
}