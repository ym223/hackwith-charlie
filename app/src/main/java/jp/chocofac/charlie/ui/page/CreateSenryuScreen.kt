package jp.chocofac.charlie.ui.page

import android.Manifest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.data.service.senryu.Senryu
import jp.chocofac.charlie.ui.fontFamily
import jp.chocofac.charlie.ui.viewmodel.CreateSenryuViewModel
import jp.chocofac.charlie.ui.viewmodel.HomeViewModel
import jp.chocofac.charlie.ui.viewmodel.SenryuViewState

@Composable
fun CreateSenryuScreen(
    senryuViewModel: CreateSenryuViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    CreateSenryuContent(
        viewModel = senryuViewModel
    ) {
        navController.navigate("${NavItem.PostScreen.name}/${it.first}/${it.second}/${it.last}")
        homeViewModel.postData(it.first, it.second, it.last)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSenryuContent(
    viewModel: CreateSenryuViewModel,
    onSubmitButtonClick: (Senryu) -> Unit
) {
    val state = viewModel.senryuViewState.collectAsState()
    var text by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name), fontFamily = fontFamily)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CameraPreview()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "思ったことを書くのじゃ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(16.dp))
                SenryuTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.postImpressions(text) }) {
                    Text(text = "川柳を詠むのじゃ", fontFamily = fontFamily)
                }
                Spacer(modifier = Modifier.height(8.dp))
                when (state.value) {
                    SenryuViewState.Initial -> {
                    }

                    is SenryuViewState.Error -> {
                        Text(text = "Error")
                    }

                    SenryuViewState.Loading -> {
                        Text(text = "Loading")
                    }

                    is SenryuViewState.Success -> {
                        // 取得した川柳を表示 (画面遷移?)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val first = state.value.require().first
                            val second = state.value.require().second
                            val last = state.value.require().last
                            Text(text = "$first\n$second\n$last", fontFamily = fontFamily)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { onSubmitButtonClick(state.value.require()) }) {
                                Text(text = "投稿するのじゃ！", fontFamily = fontFamily)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreview() {
    val multiplePermissionsState = rememberMultiplePermissionsState(buildList {
        add(Manifest.permission.CAMERA)
    })
    Surface(
        modifier = Modifier
            .aspectRatio(16f / 9f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceTint
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (multiplePermissionsState.allPermissionsGranted) {
                // カメラのプレビュー?
                Text("Preview Image")
            } else {
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                    Text("Request permissions")
                }
            }
        }
    }
}

@Composable
fun SenryuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(6.dp)
            ),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        singleLine = true,
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .padding(horizontal = 36.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceTint)
            ) {
                innerTextField()
            }
        }
    )
}

private fun SenryuViewState.require(): Senryu {
    if (this !is SenryuViewState.Success) throw java.lang.IllegalStateException("no senryu")
    return senryu
}