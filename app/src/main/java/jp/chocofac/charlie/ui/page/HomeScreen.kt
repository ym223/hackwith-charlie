package jp.chocofac.charlie.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.data.model.PostData
import jp.chocofac.charlie.data.model.toLatLng
import jp.chocofac.charlie.ui.component.LoadingCircle
import jp.chocofac.charlie.ui.theme.CharlieTheme
import jp.chocofac.charlie.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val user by remember {
        mutableStateOf(Firebase.auth.currentUser)
    }
    val navController = LocalNavController.current
    val context = LocalContext.current
    if (user == null) {
        navController.navigate(NavItem.LoginScreen.name)
    }
    val uiState by viewModel.uiState.collectAsState()
    val locationState by viewModel.nowLocationState.collectAsState()
    viewModel.startFetch(context)

    when {
        uiState.error != null -> {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onDismissRequest()
                },
                title = {
                    Text("${uiState.error!!.cause}")
                },
                text = {
                    Text("${uiState.error!!.message}")
                },
                confirmButton = {
                    Button(onClick = { viewModel.onDismissRequest() }) {
                        Text("OK")
                    }
                }
            )
        }

        uiState.loading -> {
            LoadingCircle()
        }

        else -> {
            HomeContent(
                dataList = uiState.data,
                onSignOutButtonClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(NavItem.LoginScreen.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onAddButtonClick = {
                    navController.navigate(NavItem.SenryuScreen.name)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    dataList: List<PostData> = emptyList(),
    cameraPositionState: CameraPositionState = CameraPositionState(),
    onSignOutButtonClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
) {
    Timber.d("Content: Recompose")
    val scope = rememberCoroutineScope()
    val isSheetShow = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                actions = {
                    SignOutButton(onSignOutButtonClick)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = { AddSenryuButton(onAddButtonClick) }
    ) { paddingValues ->
        val postData = remember {
            mutableStateOf(PostData())
        }


        var properties by remember {
            mutableStateOf(MapProperties(isMyLocationEnabled = true))
        }
        var uiSettings by remember {
            mutableStateOf(MapUiSettings(zoomControlsEnabled = false))
        }
        val latlng = LatLng(41.8421809, 140.7670068)
        val cameraPosition =
            CameraPosition.fromLatLngZoom(latlng, 18f)
        val cameraPositionState = rememberCameraPositionState{
            position = cameraPosition
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState,
            properties = properties,
            // FABに被るので、一旦ZoomControlsを消してる
            // TODO: ZoomControlsをどうするか検討
            uiSettings = uiSettings,
            onMapClick = {
                isSheetShow.value = false
            }
        ) {
            dataList.forEach { data ->
                Marker(
                    state = MarkerState(
                        data.geoPoint.toLatLng()
                    ),
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newCameraPosition(
                                    CameraPosition(data.geoPoint.toLatLng(), 18f, 0f, 0f)
                                ),
                                durationMs = 1000
                            )
                        }
                        isSheetShow.value = true
                        postData.value = data
                        true
                    }
                )
            }
        }

        DetailPopup(
            isVisible = isSheetShow.value,
            data = postData.value,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun SignOutButton(
    onSignOutButtonClick: () -> Unit
) {
    Button(
        onClick = {
            onSignOutButtonClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Text(stringResource(id = R.string.sign_out_button_label))
    }
}

@Composable
fun AddSenryuButton(
    onAddButtonClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onAddButtonClick() },
    ) {
        Icon(Icons.Filled.Add, "Localized description")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CharlieTheme {
        HomeContent(onSignOutButtonClick = {})
    }
}

@Composable
fun DetailPopup(
    isVisible: Boolean,
    data: PostData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideIn(tween(100, easing = LinearOutSlowInEasing)) { fullSize ->
                IntOffset(0, (fullSize.height * 0.3).toInt())
            },
            exit = slideOut(tween(100, easing = FastOutSlowInEasing)) { fullSize ->
                IntOffset(0, fullSize.height)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Spacer(modifier = Modifier.padding(2.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray)
                    ) {
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = data.first,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 8.dp)
                        )
                        Text(
                            text = data.second,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = data.third,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var likeCount by remember {
                            mutableStateOf(2)
                        }
                        var like by remember {
                            mutableStateOf(false)
                        }
                        LikeButton(
                            onClick = {
                                if (like) {
                                    likeCount -= 1
                                } else {
                                    likeCount += 1
                                }
                                like = !like
                            },
                            likeCount = likeCount,
                            like = like
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = data.contributor,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun LikeButton(
    onClick: () -> Unit = {},
    likeCount: Int = 0,
    like: Boolean = false
) {
    Button(
        onClick = onClick
    ) {
        if (like) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red.copy(alpha = 1f))
        } else {
            Icon(Icons.Default.Favorite, contentDescription = null)
        }
        Text("$likeCount")
    }
}

@Preview(showBackground = true)
@Composable
fun PopUpPreview() {
    CharlieTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailPopup(isVisible = true, data = PostData(
                "静けさや",
                "リンク飛び込む",
                "厄災の音",
                "",
                "こた"
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LikeButtonPreview() {
    CharlieTheme {
        Surface(
            modifier = Modifier
        ) {
            LikeButton(like = true)
        }
    }
}