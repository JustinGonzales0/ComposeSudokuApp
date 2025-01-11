package com.example.sudoku.ui

import android.R.attr.label
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudoku.R

data class BottomNavigationItem(
    val title: String,
    val selectedIconImageVector: ImageVector?,
    val unselectedIconImageVector: ImageVector?,
    val selectedIconPainter: Painter?,
    val unselectedIconPainter: Painter?
)

@Composable
fun BottomNavigationBar(
    selectedItemIndex: Int,
    onNavigateItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Solve",
            selectedIconPainter = painterResource(R.drawable.baseline_grid_on_24),
            unselectedIconPainter = painterResource(R.drawable.outline_grid_on_24),
            selectedIconImageVector = null,
            unselectedIconImageVector = null
        ),
        BottomNavigationItem(
            title = "Home",
            selectedIconImageVector = Icons.Filled.Home,
            unselectedIconImageVector =  Icons.Outlined.Home,
            selectedIconPainter = null,
            unselectedIconPainter = null,
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIconImageVector = Icons.Filled.Settings,
            unselectedIconImageVector = Icons.Outlined.Settings,
            selectedIconPainter = null,
            unselectedIconPainter = null
        )
    )

    NavigationBar(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { onNavigateItemClick(index) },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    if (item.selectedIconImageVector != null && item.unselectedIconImageVector != null) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIconImageVector
                            } else {
                                item.unselectedIconImageVector
                            },
                            contentDescription = item.title
                        )
                    } else if (item.selectedIconPainter != null && item.unselectedIconPainter != null) {
                        Icon(
                            painter = if (index == selectedItemIndex) {
                                item.selectedIconPainter
                            } else {
                                item.unselectedIconPainter
                            },
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    Scaffold (
        bottomBar = {
            BottomNavigationBar(
                0,
                {},
                modifier = Modifier)
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }

}