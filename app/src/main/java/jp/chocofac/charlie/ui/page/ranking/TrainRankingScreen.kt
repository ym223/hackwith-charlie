package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.runtime.Composable

@Composable
fun TrainRankingScreen() {
    TrainRankingContent()
}

@Composable
fun TrainRankingContent() {
    val mockSenryu1 = Senryu("市電の音", "懐かしさが", "心を満たす")
    val mockSenryu2 = Senryu("市電通る", "事故心配する", "私の気持ち")
    val mockSenryu3 = Senryu("車窓に映える", "市電のラッピング", "心躍る")
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