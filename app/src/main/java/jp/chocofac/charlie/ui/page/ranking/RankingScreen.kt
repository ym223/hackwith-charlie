package jp.chocofac.charlie.ui.page.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.chocofac.charlie.R
import jp.chocofac.charlie.ui.fontFamily
import jp.chocofac.charlie.ui.theme.CharlieTheme

data class Senryu(val firstLine: String, val secondLine: String, val thirdLine: String) {
}

@Composable
fun RankingScreen() {
    Column {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp),
//            text = "ここにTabScreenを表示させたい人生でした"
//        )
        TabScreen()
        RankingContent()
    }
}

@Composable
fun RankingContent() {
    // TODO: ランキング画面のUI
    val mockSenryu1 = Senryu("未来大", "オープンスペース", "かっこいいな")
    val mockSenryu2 = Senryu("100万ドル", "夜景が映える", "未来大")
    val mockSenryu3 = Senryu("きらきら海", "宝石箱食べた", "幸せ朝")
    val mockSenryu4 = Senryu("恋人と", "綺麗な夜景", "心に残る")
    val mockSenryu5 = Senryu("きらり輝く", "宝石箱のよう", "美味し海鮮")
    val mockSenryu6 = Senryu("恋人と", "夜景を見に来て", "大切な人")
    val names = listOf(
        Senryu(mockSenryu1.firstLine, mockSenryu1.secondLine, mockSenryu1.thirdLine),
        Senryu(mockSenryu2.firstLine, mockSenryu2.secondLine, mockSenryu2.thirdLine),
        Senryu(mockSenryu3.firstLine, mockSenryu3.secondLine, mockSenryu3.thirdLine),
        Senryu(mockSenryu4.firstLine, mockSenryu4.secondLine, mockSenryu4.thirdLine),
        Senryu(mockSenryu5.firstLine, mockSenryu5.secondLine, mockSenryu5.thirdLine),
        Senryu(mockSenryu6.firstLine, mockSenryu6.secondLine, mockSenryu6.thirdLine),
        // 投稿数かさ増 2set分繰り返し
        Senryu(mockSenryu1.firstLine, mockSenryu1.secondLine, mockSenryu1.thirdLine),
        Senryu(mockSenryu2.firstLine, mockSenryu2.secondLine, mockSenryu2.thirdLine),
        Senryu(mockSenryu3.firstLine, mockSenryu3.secondLine, mockSenryu3.thirdLine),
        Senryu(mockSenryu4.firstLine, mockSenryu4.secondLine, mockSenryu4.thirdLine),
        Senryu(mockSenryu5.firstLine, mockSenryu5.secondLine, mockSenryu5.thirdLine),
        Senryu(mockSenryu6.firstLine, mockSenryu6.secondLine, mockSenryu6.thirdLine)
    )
    SenryuCardList(names = names)
}

@Composable
fun SenryuCardList(names: List<Senryu>) {
    LazyColumn {
        items(names) { name ->
            SenryuListItem(name = name)
        }
    }
}

@Composable
fun SenryuListItem(name: Senryu) {
    Card(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
            .shadow(
                elevation = 10.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ),
        RoundedCornerShape(16.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        top = 16.dp,
                        bottom = 4.dp
                    ),
                text = name.firstLine,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = fontFamily
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 0.dp,
                        horizontal = 32.dp
                    ),
                text = name.secondLine,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = fontFamily
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        top = 4.dp,
                        bottom = 0.dp
                    ),
                text = name.thirdLine,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = fontFamily
            )
            Row {
                val onClick by mutableStateOf({})
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = onClick,
                    modifier = Modifier
                        .size(
                            width = 120.dp,
                            height = 48.dp
                        )
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 0.dp,
                            bottom = 8.dp
                        )
                        .shadow(
                            elevation = 2.dp,
                            ambientColor = Color.Gray,
                            spotColor = Color.Gray,
                            shape = RoundedCornerShape(48.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .size(
                                width = 120.dp,
                                height = 40.dp
                            ),
                        text = "❤+15",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 18.sp
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 32.dp,
                            end = 32.dp,
                            top = 4.dp,
                            bottom = 0.dp
                        ),
                    text = "ゆめ",
                    textAlign = TextAlign.End,
                    fontFamily = fontFamily
                )
            }
        }
    }
}

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("総合ランキング", "#未来大", "#函館山", "#グルメ", "#歴史", "#市電")

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Image(
                                painterResource(id = R.drawable.round_format_list_bulleted_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            1 -> Image(
                                painterResource(id = R.drawable.baseline_domain_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            2 -> Image(
                                painterResource(id = R.drawable.baseline_volcano_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            3 -> Image(
                                painterResource(id = R.drawable.outline_restaurant_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            4 -> Image(
                                painterResource(id = R.drawable.outline_history_edu_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            5 -> Image(
                                painterResource(id = R.drawable.baseline_train_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> RankingContent()
            1 -> FunRankingScreen()
            2 -> MtHakodateRankingScreen()
            3 -> GourmetRankingScreen()
            4 -> RankingContent()
            5 -> RankingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    CharlieTheme {
        RankingContent()
    }
}
