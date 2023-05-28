package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.runtime.Composable

@Composable
fun HistoryRankingScreen() {
    HistoryRankingContent()
}

@Composable
fun HistoryRankingContent() {
    val mockSenryu1 = Senryu("枯れ葉舞う", "街路樹揺らし", "電線だけ立つ")
    val mockSenryu2 = Senryu("険しい坂", "過去の火災が", "心探る")
    val mockSenryu3 = Senryu("撃てば", "壊せるかな", "五稜郭")
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