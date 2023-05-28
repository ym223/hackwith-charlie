package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.runtime.Composable

@Composable
fun MtHakodateRankingScreen() {
    MtHakodateRankingContent()
}

@Composable
fun MtHakodateRankingContent() {
    val mockSenryu1 = Senryu("雲に隠れ", "函館山の夜", "見えず悔やむ")
    val mockSenryu2 = Senryu("函館山", "夜景に酔いしれる", "心躍う")
    val mockSenryu3 = Senryu("駆け上がる", "忙しきロープウェイ", "胸はしめつけ")
    val names = listOf(
        Senryu(mockSenryu1.firstLine, mockSenryu1.secondLine, mockSenryu1.thirdLine),
        Senryu(mockSenryu2.firstLine, mockSenryu2.secondLine, mockSenryu2.thirdLine),
        Senryu(mockSenryu3.firstLine, mockSenryu3.secondLine, mockSenryu3.thirdLine),
        // 投稿数かさ増 2set分繰り返し
        Senryu(mockSenryu1.firstLine, mockSenryu1.secondLine, mockSenryu1.thirdLine),
        Senryu(mockSenryu2.firstLine, mockSenryu2.secondLine, mockSenryu2.thirdLine),
        Senryu(mockSenryu3.firstLine, mockSenryu3.secondLine, mockSenryu3.thirdLine),
    )
    SenryuCardList(names = names)
}