package jp.chocofac.charlie.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import jp.chocofac.charlie.ui.fontFamily

@Composable
fun PostedSenryuScreen(
    first: String,
    second: String,
    last: String
) {
    Column {
        Text(text = first, fontSize = 48.sp, fontFamily = fontFamily)
        Text(text = second, fontSize = 48.sp, fontFamily = fontFamily)
        Text(text = last, fontSize = 48.sp, fontFamily = fontFamily)
    }
}