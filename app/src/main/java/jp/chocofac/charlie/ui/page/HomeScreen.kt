package jp.chocofac.charlie.ui.page

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.data.model.toLatLng
import jp.chocofac.charlie.ui.theme.CharlieTheme
import jp.chocofac.charlie.ui.viewmodel.HomeViewModel

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
    viewModel.startFetch(context)

    HomeContent(
        onSignOutButtonClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(NavItem.LoginScreen.name) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        },
        locationState.location
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    onSignOutButtonClick: () -> Unit = {},
    location: Location = Location("")
) {
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
        }
    ) { paddingValues ->
        val cameraPosition =
            CameraPosition.fromLatLngZoom(location.toLatLng(), 18f)
        val cameraPositionState = CameraPositionState(cameraPosition)
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            // FABに被るので、一旦ZoomControlsを消してる
            // TODO: ZoomControlsをどうするか検討
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
        )
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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CharlieTheme {
        HomeContent()
    }
}