package jp.chocofac.charlie.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.data.service.rememberFirebaseAuthLauncher
import jp.chocofac.charlie.ui.fontFamily
import jp.chocofac.charlie.ui.theme.CharlieTheme
import jp.chocofac.charlie.ui.viewmodel.LoginViewModel
import timber.log.Timber

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var user by remember {
        mutableStateOf(Firebase.auth.currentUser)
    }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = {
            user = it.user
            Timber.d("$user")
        },
        onAuthError = {
            user = null
            viewModel.onAuthError(it)
        }
    )
    val token = stringResource(R.string.default_web_client_id)

    val context = LocalContext.current
    val navController = LocalNavController.current
    if (user != null) {
        navController.navigate(NavItem.HomeScreen.name)
    }

    when {
        state.error != null -> {
            AlertDialog(
                onDismissRequest = {
                    viewModel.onAlertDismiss()
                },
                confirmButton = {
                    viewModel.onAlertDismiss()
                },
                title = {
                    Text("${state.error}")
                }
            )
        }
    }
    LoginContent(
        onLoginButtonClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    onLoginButtonClick: () -> Unit = {}
) {
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "",
                modifier = Modifier.size(300.dp)
            )
            Text(
                stringResource(id = R.string.login_welcome_message),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                fontFamily = fontFamily
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = onLoginButtonClick
            ) {
                Text(stringResource(id = R.string.login_sign_in_button_label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    CharlieTheme {
        LoginContent()
    }
}