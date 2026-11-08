package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReportCategory
import com.example.ui.theme.*
import com.example.viewmodel.IndiaShortsViewModel

@Composable
fun CopyrightAndReportsScreen(
    viewModel: IndiaShortsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(ReportCategory.ADULT_CONTENT) }
    var targetInput by remember { mutableStateOf("https://indiashorts.org/v/sample_report") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Critical Policy Banner: Views Never Close Accounts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSecondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OFFICIAL ACCOUNT PROTECTION POLICY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Views Alone NEVER Close or Ban Accounts",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "• 100 views must NEVER close an account.\n• 1,000 views must NEVER close an account.\n• 1,000,000 views must NEVER close an account.\n\nAccount penalties occur strictly due to confirmed policy violations (such as child endangerment, sexually explicit material, or verified copyright theft), never view metrics.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Automated Content ID & Copyright Scanner (Label 53, 54, 55 & Button 53, 54, 55)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 53: Content ID & Digital Waveform Scanner",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Automated real-time acoustic fingerprinting and frame-similarity matching to protect original Indian creators and international rights holders.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.scanCopyrightInfringement() },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_53")
                        ) {
                            Text("Btn 53: Scan Content", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(54) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_54")
                        ) {
                            Text("Btn 54: File Claim", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }

                        Button(
                            onClick = { viewModel.executeButtonAction(55) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_55")
                        ) {
                            Text("Btn 55: Appeal", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        // Safety Violation Reporting Terminal (Label 56, 57 & Button 56, 57)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Label 56: Community Safety & Violation Reporting",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Select Violation Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))

                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ReportCategory.values().size) { idx ->
                            val cat = ReportCategory.values()[idx]
                            val isSel = cat == selectedCategory
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat.title, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentRed,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        label = { Text("Video or Channel URL / Identifier") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("report_target_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.submitReport(selectedCategory, targetInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_56")
                        ) {
                            Text("Btn 56: Submit Report", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.executeButtonAction(57) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_57")
                        ) {
                            Text("Btn 57: Appeal Sanction", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
