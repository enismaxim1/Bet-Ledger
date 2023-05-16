package hu.ait.betledger.ui.screen.betlist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.ait.betledger.data.BetItem

@Composable
fun BetCard(
    betItem: BetItem,
    onRemoveItem: () -> Unit = {},
    onEditItem: (BetItem) -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = betItem.title,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.clickable {
                            onRemoveItem()
                        },
                        tint = Color.Red
                    )

                    IconButton(onClick = {
                        onEditItem(betItem)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) {
                                "Less"
                            } else {
                                "More"
                            }
                        )
                    }
                }
            }

            if (expanded) {
                Divider(color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Description: ${betItem.description}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Party 1: ${betItem.party1}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Party 2: ${betItem.party2}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Party 1 Win Amount: ${betItem.party1WinAmount}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Party 2 Win Amount: ${betItem.party2WinAmount}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Status: ${betItem.resolutionStatus.cleanName}")
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Created Date: ${betItem.createDate}",
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}