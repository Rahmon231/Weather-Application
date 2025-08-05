package com.dev.weatherapplication.widgets

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dev.weatherapplication.navigation.WeatherScreens
import kotlin.math.log

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    navController : NavController,
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {
    val showDialog = remember{
        mutableStateOf(false)
    }
    if (showDialog.value){
        ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.secondary,
        ),
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    )
            )
        },
        windowInsets = WindowInsets(0.dp),
        navigationIcon = {
            if (icon != null) {
                IconButton(onClick = { onButtonClicked.invoke() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "nav icon",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        actions = {
            if (isMainScreen) {
                IconButton(onClick = { onAddActionClicked.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "MoreVert Icon"
                    )
                }
            }else Box { }

        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>,
                            navController: NavController) {

    var expanded by remember {
        mutableStateOf(true)
    }
    val items = listOf("About", "Favorites", "Settings")
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopEnd)
        .absolutePadding(top = 45.dp, right = 20.dp)){
        DropdownMenu(
            expanded = showDialog.value,
            onDismissRequest = {showDialog.value = false},
            modifier = Modifier
                .width(140.dp)
                .background(color = Color.White)) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = {
                        Text(text = text)
                    },
                    onClick = {
                        showDialog.value = false
                        Log.d("AboutTest", "ShowSettingDropDownMenu: $text")
                        navController.navigate(when(text){
                            "About" -> {
                                Log.d("AboutTestNav", "ShowSettingDropDownMenu: $text")
                                WeatherScreens.AboutScreen.name
                            }

                            "Favorites" -> WeatherScreens.FavoriteScreen.name
                            else -> WeatherScreens.SettingsScreen.name
                        })


                },
                    leadingIcon = { Icon(imageVector = when(text){
                        "About" -> Icons.Filled.Info
                        "Favorites" -> Icons.Default.Favorite
                        else -> Icons.Default.Settings
                    }, contentDescription = null, tint = Color.LightGray)},

                )

            }


        }

    }

}
