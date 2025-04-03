package com.company.timetable.ui.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.timetable.data.model.ClassEntry
import com.company.timetable.data.model.TimetableData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableResultScreen(
    onBackClicked: () -> Unit,
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val timetableState by viewModel.timetableState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timetable Result") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = timetableState) {
                is TimetableState.Success -> {
                    TimetableGrid(state.data)
                }
                is TimetableState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    // Should not reach here normally, but just in case
                    Text(
                        text = "No timetable data available",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimetableGrid(timetableData: TimetableData) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header row with days
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Empty cell for the time column
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(50.dp)
                        .border(0.5.dp, Color.Gray)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Time",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                
                // Day headers
                timetableData.days.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .border(0.5.dp, Color.Gray)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Rows for each time slot
        items(timetableData.timeSlots) { timeSlot ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Time column
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(100.dp)
                        .border(0.5.dp, Color.Gray)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeSlot,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Cells for each day
                timetableData.days.forEach { day ->
                    val classesForThisCell = timetableData.classes.filter { 
                        it.day == day && it.timeSlot == timeSlot 
                    }
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .border(0.5.dp, Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (classesForThisCell.isNotEmpty()) {
                            ClassCell(classesForThisCell.first())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassCell(classEntry: ClassEntry) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = classEntry.className,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            
            classEntry.location?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
            
            classEntry.professor?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
} 