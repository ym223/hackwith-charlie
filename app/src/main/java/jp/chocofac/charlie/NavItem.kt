package jp.chocofac.charlie

// Navigationを便利にするやつyatu
sealed class NavItem(
    val name: String
) {
    object HomeScreen : NavItem("home")
}
