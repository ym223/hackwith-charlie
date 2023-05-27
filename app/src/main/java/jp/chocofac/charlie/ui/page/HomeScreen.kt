package jp.chocofac.charlie.ui.page

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.data.model.PostData
import jp.chocofac.charlie.data.model.toLatLng
import jp.chocofac.charlie.ui.component.LoadingCircle
import jp.chocofac.charlie.ui.theme.CharlieTheme
import jp.chocofac.charlie.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

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

    val locationState by viewModel.nowLocationState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
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
                location = locationState.location,
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
    location: Location = Location(""),
    onSignOutButtonClick: () -> Unit = {},
    onAddButtonClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
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
        val cameraPosition =
            CameraPosition.fromLatLngZoom(location.toLatLng(), 18f)
        val cameraPositionState = CameraPositionState(cameraPosition)

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            // FABに被るので、一旦ZoomControlsを消してる
            // TODO: ZoomControlsをどうするか検討
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
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
                        true
                    }
                )
            }
        }
    }
}

@Composable
fun SignOutButton(
    onSignOutButtonClick: () -> Unit = {}
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
        HomeContent()
    }
}