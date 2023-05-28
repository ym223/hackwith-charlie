package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FunRankingScreen() {
    FunRankingContent()
}

@Composable
fun FunRankingContent() {
    val mockSenryu1 = Senryu("未来の夢", "広がる世界を", "手に取りたい")
    val mockSenryu2 = Senryu("未来を見る", "ガラス張りの恐怖", "胸がざわめく")
    val mockSenryu3 = Senryu("未来の夜", "美しく輝く", "感動の景色")
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