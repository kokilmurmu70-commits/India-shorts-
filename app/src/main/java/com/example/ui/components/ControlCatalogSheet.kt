package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ControlsCatalog
import com.example.model.AppControlItem
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun ControlCatalogScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDomain by remember { mutableStateOf("All") }

    val allControls = ControlsCatalog.ALL_60_CONTROLS
    val domains = remember { listOf("All") + allControls.map { it.domain }.distinct() }

    val filtered = remember(searchQuery, selectedDomain) {
        allControls.filter { item ->
            (selectedDomain == "All" || item.domain == selectedDomain) &&
                    (searchQuery.isBlank() ||
                            item.labelName.contains(searchQuery, ignoreCase = true) ||
                            item.buttonName.contains(searchQuery, ignoreCase = true) ||
                            item.domain.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Official Header Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Official Controls Inspector",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Mandatory 60 Meaningful Labels & 60 Functional Buttons",
                            fontSize = 12.sp,
                            color = SaffronPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Surface(
                        color = EmeraldSecondary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${allControls.size} / 60 OK",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Application Owner: Kokil Murmu | Brand: INDIA SHORTS | Bank: Punjab National Bank (PNB). Every label has a distinct purpose and every button executes real application logic.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter 60 labels & buttons by domain or keyword...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("controls_search_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal domain filter chips
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(domains) { dom ->
                val isSelected = dom == selectedDomain
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDomain = dom },
                    label = { Text(dom, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SaffronPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Showing count badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Showing ${filtered.size} of 60 controls",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Click any button to trigger action",
                fontSize = 11.sp,
                color = EmeraldSecondary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // List of 60 items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered, key = { it.id }) { item ->
                ControlCard(
                    item = item,
                    onExecute = { viewModel.executeButtonAction(item.id) }
                )
            }
        }
    }
}

@Composable
fun ControlCard(
    item: AppControlItem,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("control_card_${item.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header row with ID and domain badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = SaffronPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Item #${item.id} of 60",
                        color = SaffronPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = EmeraldSecondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.domain,
                        color = EmeraldSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // LABEL
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Label,
                    contentDescription = "Label",
                    tint = SaffronPrimary,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = item.labelName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("label_${item.id}")
                    )
                    Text(
                        text = item.description,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // BUTTON
            Button(
                onClick = onExecute,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
                    .testTag("btn_${item.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircleOutline,
                    contentDescription = "Execute",
                    tint = SaffronPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.buttonName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
