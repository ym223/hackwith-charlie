package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.runtime.Composable

@Composable
fun GourmetRankingScreen() {
    GourmetRankingContent()
}

@Composable
fun GourmetRankingContent() {
    val mockSenryu1 = Senryu("イカ丼", "まじうまかった", "幸せ満開")
    val mockSenryu2 = Senryu("海より濃い", "醤油や味噌じゃ", "なく塩ラーメン")
    val mockSenryu3 = Senryu("ラッキーピエロ", "何食えばいい", "悩み多し")
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