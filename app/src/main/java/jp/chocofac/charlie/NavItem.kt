package jp.chocofac.charlie

// Navigationを便利にするやつyatu
sealed class NavItem(
    val name: String
) {
    object LoginScreen: NavItem("login")
    object HomeScreen : NavItem("home")
    object RankingScreen: NavItem("ranking")
    object SenryuScreen : NavItem("senryu")
    object PostScreen : NavItem("post")
}
