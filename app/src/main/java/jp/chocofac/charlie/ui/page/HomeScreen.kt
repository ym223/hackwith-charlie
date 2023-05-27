package jp.chocofac.charlie.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.chocofac.charlie.LocalNavController
import jp.chocofac.charlie.NavItem
import jp.chocofac.charlie.R
import jp.chocofac.charlie.ui.viewmodel.HomeViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val user by remember {
        mutableStateOf(Firebase.auth.currentUser)
    }
    val navController = LocalNavController.current
    if (user == null) {
        navController.navigate(NavItem.LoginScreen.name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                actions = {
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(NavItem.LoginScreen.name) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Text("Sign out")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(50) {
                Text("$it")
            }
        }
    }
}