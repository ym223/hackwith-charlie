package jp.chocofac.charlie.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.ui.fontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostedSenryuScreen(
    first: String,
    second: String,
    last: String
) {

    val navController = LocalNavController.current

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("ここで一句", fontFamily = fontFamily)
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { paddingValues ->
        Row(
            Modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = first, fontSize = 48.sp, fontFamily = fontFamily)
                Text(text = second, fontSize = 48.sp, fontFamily = fontFamily)
                Text(text = last, fontSize = 48.sp, fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navController.navigate(NavItem.HomeScreen.name) }) {
                    Text(text = "ホームに戻る")
                }
            }
        }
    }
}