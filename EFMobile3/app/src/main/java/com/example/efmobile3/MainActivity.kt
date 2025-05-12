package com.example.efmobile3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.efmobile3.ui.theme.EFMobile3Theme
import com.example.efmobile3.viewmodel.ContactViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.focus.focusModifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.efmobile3.model.Contact

class MainActivity : ComponentActivity() {
    private val contactViewModel by lazy { ContactViewModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ContactScreen(viewModel = contactViewModel)
                }
            }
        }
    }
}

@Composable
fun ContactScreen(viewModel: ContactViewModel){
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("In Progress") }
    var priority by remember { mutableStateOf("Medium") }
    var isEditing by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var error by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)){
        if (error.isNotBlank()){
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        DropdownSelector("Status", listOf("In Progress", "Completed", "Cancelled"), status) {
            status = it
        }

        Spacer(Modifier.height(8.dp))
        DropdownSelector("priority", listOf("High", "Medium", "low"), priority) {
            priority = it
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isBlank()){
                error = "Name is required"
               } else {
                   error = ""
                    if (isEditing && selectedContact != null) {
                        viewModel.updateContact(selectedContact!!.copy(name = name, status = status, priority = priority)
                        )
                        isEditing = false
                        selectedContact = null
                    } else {
                        viewModel.addContact(name, status, priority)
                    }
                    name = ""
               }
            }
        ) {
            Text(if (isEditing) "Update Contact" else "Add Contact")
        }
        Spacer(Modifier.height(16.dp))
        Text("Contacts List", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(viewModel.contacts) { contact ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Name: ${contact.name}")
                        Text("Status: ${contact.status}")
                        Text("Priority: ${contact.priority}")
                        Row {
                            TextButton(onClick = {
                                name = contact.name
                                status = contact.status
                                priority = contact.priority
                                selectedContact = contact
                                isEditing = true
                            }) {
                                Text("Edit")
                            }
                            TextButton(onClick = {
                                viewModel.deleteContact(contact)
                            }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedOption)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                options.forEach { option ->
                    DropdownMenuItem(text = {Text(option)}, onClick = {
                        onOptionSelected(option)
                        expanded = true
                    })
                }
            }
        }
    }
}