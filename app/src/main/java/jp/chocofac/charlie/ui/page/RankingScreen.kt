package jp.chocofac.charlie.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import jp.chocofac.charlie.ui.theme.CharlieTheme

@Composable
fun RankingScreen() {
    // TODO: ランキング画面のstateなど
    RankingContent()
}

@Composable
fun RankingContent() {
    // TODO: ランキング画面のUI
}

@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    CharlieTheme {
        RankingContent()
    }
}