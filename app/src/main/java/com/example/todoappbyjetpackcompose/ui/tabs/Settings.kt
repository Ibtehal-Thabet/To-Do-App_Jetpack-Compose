package com.example.todoappbyjetpackcompose.ui.tabs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoappbyjetpackcompose.R
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import com.example.todoappbyjetpackcompose.utils.TopAppBar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Settings(navController: NavController? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        SettingsContent()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsContent() {
    Scaffold(
        topBar = {
            TopAppBar(stringResource(id = R.string.settings))
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LanguageSpinner()
        }

    }
}

var isLangChanged = false
var langIndex = 0

@Composable
fun LanguageSpinner() {

    val languages =
        listOf(stringResource(id = R.string.english), stringResource(id = R.string.arabic))
    val langTitle = stringResource(id = R.string.language)

    Spinner(options = languages, title = langTitle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spinner(options: List<String>, title: String) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[langIndex]) }

    val context = LocalContext.current

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, top = 20.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
//        colors = ExposedDropdownMenuDefaults.textFieldColors(
//            unfocusedIndicatorColor = Color.Transparent,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedContainerColor = Color.Transparent,
//            focusedContainerColor = Color.Transparent
//        ),
        modifier = Modifier
            .border(width = 1.5.dp, color = LightPrimary)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )
            .shadow(elevation = 0.dp, spotColor = MaterialTheme.colorScheme.onSecondary),
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .background(
                    MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(8.dp)
                ),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary)
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary),
                    text = {
                        Text(text = selectionOption)
                    },
                    onClick = {

                        if (selectionOption.equals("English") || selectionOption.equals("الانجليزية")) {
                            changeLocales(context = context, language = "en")
                            langIndex = 0
                            isLangChanged = true
                        } else {
                            changeLocales(context = context, language = "ar")
                            langIndex = 1
                            isLangChanged = true
                        }
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }

}

fun changeLocales(context: Context, language: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(language)
    } else {
        val resources = context.resources
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration?.setLocale(locale)
        configuration?.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
//        context.startActivity(Intent(context, context::class.java))
    }
    val activity = context as Activity
    context.startActivity(Intent(context, context::class.java))
//    activity.finish()


//    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Settings", showSystemUi = true)
@Composable
fun SettingsPreview() {
    Settings()
}